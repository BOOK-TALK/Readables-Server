package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import com.book.backend.domain.openapi.dto.response.OpenAPIResponseInterface;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAPI {
    @Value("${openapi.url}")
    private String baseUrl;

    @Value("${openapi.authKey}")
    private String authKey;

    @Value("${openapi.timeoutSeconds}")
    private int TIMEOUT_SECONDS;  // 타임아웃 시간 (초)

    @Value("${openapi.maxRetryCounts}")
    private int MAX_RETRY_COUNTS;  // 최대 재시도 횟수

    private final String format = "json";
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public JSONObject connect(String subUrl, OpenAPIRequestInterface dto, OpenAPIResponseInterface responseDto) throws Exception {
        log.trace("OpenAPI > connect()");
        int retryCount = 0;

        while (retryCount < MAX_RETRY_COUNTS) {
            try {
                CompletableFuture<JSONObject> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        URL url = setRequest(subUrl, dto); // 요청 만들기
                        InputStreamReader streamResponse = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8); // 요청 보내기
                        return readStreamToJson(streamResponse, responseDto); // 응답 stream 을 json 으로 변환
                    } catch (Exception e) {
                        throw new RuntimeException(e);  // 커스텀 불필요, 런타임 에러로 처리
                    }
                }, executorService);

                // 타임아웃 설정 및 결과 가져오기
                return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                log.warn("OPEN API 응답을 요청하는 중 타임아웃이 발생했습니다. 재시도합니다...(" + (retryCount + 1) + "/" + MAX_RETRY_COUNTS + ")");
                retryCount++;
            }
        }

        // 재시도 횟수를 초과하면 예외 던지기
        throw new CustomException(ErrorCode.OPENAPI_REQUEST_TIMEOUT);
    }

    private URL setRequest(String subUrl, OpenAPIRequestInterface dto) throws Exception {
        log.trace("OpenAPI > setRequest()");
        StringBuilder sb = new StringBuilder();

        // url 에 request Param 추가
        sb.append(baseUrl + subUrl + "?" + "authKey=" + authKey + "&" + "format=" + format + "&");
        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true); //private 필드 접근 가능하게
            Object value = field.get(dto);
            if (value != null) {
                sb.append(field.getName() + "=" + value.toString() + "&");
            }
        }
        String fullUrl = sb.substring(0, sb.length() - 1); // 맨마지막 & 삭제

        return new URL(fullUrl);
    }

    /* InputStream 을 json 파싱 */
    private JSONObject readStreamToJson(InputStreamReader streamResponse, OpenAPIResponseInterface responseDto) throws Exception {
        log.trace("OpenAPI > readStreamToJson()");
        String fullResponse = new BufferedReader(streamResponse).readLine();

        // response JSON 파싱
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fullResponse);
        JSONObject response = (JSONObject) jsonObject.get("response");

        // API 일일 호출 횟수 초과 에러 (일 최대 500건)
        if(response.get("error") != null){
            throw new CustomException(ErrorCode.API_CALL_LIMIT_EXCEEDED);
        }
        return response;
    }
}

