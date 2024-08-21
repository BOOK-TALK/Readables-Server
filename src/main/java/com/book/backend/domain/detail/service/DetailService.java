package com.book.backend.domain.detail.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.book.repository.BookRepository;
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

import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
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
    private final OpentalkRepository opentalkRepository;
    private final BookRepository bookRepository;

    public DetailResponseDto detail(DetailRequestDto requestDto) throws Exception {
        log.trace("detail()");
        String subUrl = "usageAnalysisList";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.detail(jsonResponse);
    }

    public List<LoanAvailableDto> getLoanAvailable(String isbn) {
        log.trace("getLoanAvailable()");

        User user = userService.loadLoggedinUser();
        List<LoanAvailableDto> loanAvailableList = new LinkedList<>();
        if(!user.getLibraries().isEmpty()) {
            user.getLibraries().forEach(libraryDto -> {
                try {
                    BookExistRequestDto bookExistRequestDto = BookExistRequestDto.builder()
                            .libCode(libraryDto.getCode())
                            .isbn13(isbn).build();

                    String subUrl = "bookExist";
                    JSONObject jsonResponse = openAPI.connect(subUrl, bookExistRequestDto, new BookExistResponseDto());
                    ResponseParser responseParser = new ResponseParser();
                    boolean isLoanable = responseParser.loanAvailable(jsonResponse);

                    LoanAvailableDto dto = LoanAvailableDto.builder()
                            .libCode(libraryDto.getCode())
                            .libName(libraryDto.getName())
                            .isLoanable(isLoanable)
                            .build();
                    loanAvailableList.add(dto);
                } catch (Exception e) {
                    throw new RuntimeException("대출 가능 여부 조회 중 오류가 발생했습니다. (존재하는 libCode 인지 확인해주세요)");
                }
            });
        }
        return loanAvailableList;
    }

    @Transactional
    public Long getOpentalkId(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) { // 새로운 오픈톡 생성
            Book newBook = new Book();
            newBook.setIsbn(isbn);
            book = bookRepository.save(newBook);

            Opentalk opentalk = new Opentalk();
            opentalk.setBook(book);
            opentalk = opentalkRepository.save(opentalk);
            return opentalk.getOpentalkId();
        }
        Opentalk opentalk = opentalkRepository.findByBook(book);
        return opentalk.getOpentalkId();
    }
}
