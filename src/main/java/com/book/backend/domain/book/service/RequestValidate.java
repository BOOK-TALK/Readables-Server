package com.book.backend.domain.book.service;

import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import java.time.LocalDate;

public class RequestValidate {

    public void isValidSearchDt(String searchDt) {
        //yyyy-mm-dd 형식인지 확인
        if(!searchDt.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new CustomException(ErrorCode.INVALID_SEARCH_DT_FORMAT);
        }
        // 현재 날짜보다 2일 전인지 확인
        LocalDate currentDate = LocalDate.now();
        if(LocalDate.parse(searchDt).isAfter(currentDate.minusDays(2))) {
            throw new CustomException(ErrorCode.INVALID_SEARCH_DT_DATE);
        }
    }

    public void isValidIsbn(String isbn) {
        if(isbn.length() != 13) {
            throw new CustomException(ErrorCode.INVALID_ISBN);
        }
    }
}
