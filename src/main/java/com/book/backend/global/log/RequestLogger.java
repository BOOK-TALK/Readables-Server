package com.book.backend.global.log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Dto 객체의 필드와 값을 로깅 (request logging)
public class RequestLogger {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    public static void param(String[] keys, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestParam : {");

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Object value = (params != null && i < params.length) ? params[i] : null; // params가 null이거나 인덱스를 넘어가면 null로 처리

            sb.append(key).append(": ").append(value != null ? value.toString() : "null");
            if (i < keys.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("}");
        logger.trace(sb.toString());
    }

//    public static void body(Object dto) {
//        StringBuilder logMessage = new StringBuilder();
//        Class<?> clazz = dto.getClass();
//        String dtoName = clazz.getSimpleName();
//        Field[] fields = clazz.getDeclaredFields(); // 모든 필드를 가져옴
//
//        // log 커스텀
//        logMessage.append("RequestBody : ");
//        logMessage.append(dtoName).append(" {");
//        for (Field field : fields) {
//            field.setAccessible(true); // private 필드 접근 허용
//            String key = field.getClass().getSimpleName();
//            Object value;
//            try {
//                value = field.get(key); // 필드의 값을 가져옴
//            } catch (IllegalAccessException e) {
//                value = "access error"; // 필드에 접근할 수 없을 경우
//            }
//            logMessage.append(key).append(": ").append(value != null ? value.toString() : "null").append(", ");
//        }
//        // 마지막 콤마 제거
//        if (logMessage.length() > 2) {
//            logMessage.setLength(logMessage.length() - 2);
//        }
//        logMessage.append("}");
//
//        logger.trace(logMessage.toString());
//    }

}
