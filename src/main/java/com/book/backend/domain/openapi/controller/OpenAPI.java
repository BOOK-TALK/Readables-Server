package com.book.backend.domain.openapi.controller;

import com.book.backend.domain.openapi.dto.ManiaDto;
import com.book.backend.domain.openapi.dto.ManiaEnum;
import com.book.backend.domain.openapi.dto.OpenAPIDtoInterface;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class OpenAPI {
    @Value("${openapi.url}")
    private String baseUrl;

    @Value("${openapi.authKey}")
    private String authKey;

    private final String format = "json";


    public LinkedList<ManiaDto> connect(String subUrl, OpenAPIDtoInterface dto) throws Exception {
        URL url = setRequest(subUrl, dto); // 요청 만들기
        InputStreamReader streamResponse = new InputStreamReader(url.openStream(), "UTF-8"); // 요청 보내기
        return readStreamToString(streamResponse); // 응답 stream 을 string 으로 변환
    }

    /* 요청 셋팅 */
    private URL setRequest(String subUrl, OpenAPIDtoInterface dto) throws Exception {
        StringBuilder sb = new StringBuilder();

        // url 에 request Param 추가
        sb.append(baseUrl + subUrl + "?" + "authKey=" + authKey + "&" + "format=" + format + "&");
        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true); //private 필드 접근 가능하게
            Object value = field.get(dto);
            if (value != null) {
                sb.append(field.getName() + "=" + value.toString() + "&"); // request param 추가
            }
        }
        String fullUrl = sb.substring(0, sb.length() - 1); // 맨마지막 & 삭제
        return new URL(fullUrl);
    }

    /* InputStream 을 json 파싱 */
    private LinkedList<ManiaDto> readStreamToString(InputStreamReader streamResponse) throws Exception {
        String fullResponse = new BufferedReader(streamResponse).readLine();

        // response 를 파싱
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fullResponse);
        JSONObject jsonResponse = (JSONObject) jsonObject.get("response");

        //EnumHierarchy.A.children();
        System.out.println(ManiaEnum.docs.children());
        System.out.println(ManiaEnum.book.children());

//        HashMap<String, HashMap<String, String[]>> responseDto = new ManiaResponseDto().makeTemplate();

        JSONArray docs = (JSONArray) jsonResponse.get("docs"); // TODO : response 에 따라 달라지는 듯
        LinkedList<ManiaDto> finalResponse = new LinkedList<>();

        /* step1:docs,  step2:book, step3:{bookname=, authors=, ...} */
        for(int i = 0; i<docs.size();i++) {
            JSONObject temp = (JSONObject) docs.get(i);
            temp.forEach((step2, step3) -> {
                ManiaDto maniaDto = new ManiaDto();
                // Dto 채우기
                Arrays.stream(maniaDto.getClass().getDeclaredFields()).iterator().forEachRemaining( field -> {
                    field.setAccessible(true); // private 필드 접근 가능하게
                    try {
                        field.set(maniaDto, ((JSONObject) step3).get(field.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                finalResponse.add(maniaDto);
            });
        }
        return finalResponse;
    }
}

