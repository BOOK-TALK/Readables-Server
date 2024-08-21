package com.book.backend.domain.detail.service;

import com.book.backend.domain.detail.dto.LoanAvailableDto;
import com.book.backend.domain.openapi.dto.request.BookExistRequestDto;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.BookExistResponseDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;

import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import com.book.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.book.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DetailService {
    private final OpenAPI openAPI;
    private final UserService userService;

    public DetailResponseDto detail(DetailRequestDto requestDto) throws Exception {
        log.trace("detail()");
        String subUrl = "usageAnalysisList";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.detail(jsonResponse);
    }

    public void setLoanAvailable(DetailResponseDto detailDto) throws Exception {
        log.trace("setLoanAvailable()");

        User user = userService.loadLoggedinUser();
        List<LoanAvailableDto> loanAvailableList = new LinkedList<>();
        user.getLibraries().forEach(libCode -> {
            try {
                BookExistRequestDto bookExistRequestDto = BookExistRequestDto.builder()
                        .libCode(libCode)
                        .isbn13(detailDto.getBookInfoDto().getIsbn13()).build();

                String subUrl = "bookExist";
                JSONObject jsonResponse = openAPI.connect(subUrl, bookExistRequestDto, new BookExistResponseDto());
                ResponseParser responseParser = new ResponseParser();

                LoanAvailableDto dto = LoanAvailableDto.builder().libCode(libCode).isLoanable(responseParser.loanAvailable(jsonResponse)).build();
                loanAvailableList.add(dto);
            } catch (Exception e) {
                throw new RuntimeException("대출 가능 여부 조회 중 오류가 발생했습니다.");
            }
            detailDto.setLoanAvailableList(loanAvailableList);
        });
    }
}
