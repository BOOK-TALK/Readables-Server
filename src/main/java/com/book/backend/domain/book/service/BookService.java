package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.ManiaDto;
import com.book.backend.domain.openapi.dto.TestDto;
import com.book.backend.global.OpenAPI;
import java.net.HttpURLConnection;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module.Open;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    @Autowired
    private final OpenAPI openAPI;

    public void mania(ManiaDto maniaDto) throws Exception {
        String subUrl = "recommandList";
        openAPI.connect(subUrl, maniaDto); //반환값
    }

    public void test(TestDto dto) throws Exception{
        String subUrl = "libSrch";
        openAPI.connect(subUrl, dto); //반환값
    }
}
