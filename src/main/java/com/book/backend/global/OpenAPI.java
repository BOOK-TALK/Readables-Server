package com.book.backend.global;

import com.book.backend.domain.openapi.dto.OpenAPIDtoInterface;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
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

    public void connect(String subUrl, OpenAPIDtoInterface dto) throws Exception {
        InputStreamReader streamResponse = setRequest(subUrl, dto); // 요청 만들기
        readStreamToString(streamResponse); // 응답 stream 을 string 으로 변환
    }

    // 요청 셋팅
    private InputStreamReader setRequest(String subUrl, OpenAPIDtoInterface dto) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl + subUrl + "?").append("authKey=" + authKey + "&" + "format=json&");

        for(Field field : dto.getClass().getDeclaredFields()){
            field.setAccessible(true); //private 필드 접근 가능하게
            Object value = field.get(dto);
            System.out.println("!!! " + field.getName() + " : " + value + " " + value.getClass().getTypeName()); //TODO : 타입때문에?
            if(value != null) {
                sb.append(field.getName() + "=" + value.toString() + "&");
            }
        }
        String fullUrl = sb.toString().substring(0, sb.length()-1); // 맨마지막 & 삭제
        URL url = new URL(fullUrl);

        return new InputStreamReader(url.openStream(), "UTF-8"); //response
    }


    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    private void readStreamToString(InputStreamReader stream) throws Exception{
        BufferedReader br = new BufferedReader(stream);
        String result = br.readLine();
        System.out.println("???"+result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        JSONObject bookListJson = (JSONObject) jsonObject.get("response");
        JSONArray docs = (JSONArray) bookListJson.get("docs");

        System.out.println("null?????? " + docs);
    }
}
