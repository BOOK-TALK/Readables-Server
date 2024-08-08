package com.book.backend.domain.genre.service;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreRequestValidate {

    public void isValidMainKdc(String kdc) {
        // 한 자리 숫자로 구성된 KDC 번호
        if(!(kdc.matches("\\d")))  {
            throw new CustomException(ErrorCode.INVALID_MAIN_KDC);
        }
    }

    public void isValidSubKdc(String kdc) {
        // 두 자리 숫자로 구성된 KDC 번호
        if(!(kdc.matches("\\d{2}")))  {
            throw new CustomException(ErrorCode.INVALID_MAIN_KDC);
        }
    }
}
