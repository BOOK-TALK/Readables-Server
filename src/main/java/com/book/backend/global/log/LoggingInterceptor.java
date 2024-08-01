package com.book.backend.global.log;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    StringBuilder sb = new StringBuilder();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException, ServletException {
        sb.append("-------------------------------------------\n");
        sb.append("URL : ").append(request.getRequestURI()).append("\n");

        Iterator<String> keys = request.getParameterMap().keySet().iterator();
        sb.append("Request : { ");
        while(keys.hasNext()){
            String key = keys.next();
            sb.append(key);
            if(keys.hasNext()){
                sb.append(", ");
            } else {
                sb.append(" } \n");
            }
        }
        logger.trace(sb.toString());
        return true;
    }
}
