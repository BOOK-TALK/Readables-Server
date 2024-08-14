package com.book.backend.global;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CustomException customException = new CustomException(ErrorCode.LOGIN_REQUIRED);
        response.setStatus(customException.getCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"error\": \"" + customException.getCode().getMessage() + "\", " +
                    "\"code\": \"" + customException.getCode().getCode() + "\"}");
        response.getWriter().flush();
    }
}
