package com.book.backend.domain.book.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    public void logTest(){
        log.trace("logTest()");
    }

    public void errorTest(){
        log.trace("errorTest()");
        throw new RuntimeException("error");
    }
}
