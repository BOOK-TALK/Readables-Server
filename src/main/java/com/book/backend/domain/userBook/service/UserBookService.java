package com.book.backend.domain.userBook.service;

import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.domain.userBook.dto.UserBookDto;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBookService {
    private final UserService userService;
    private final UserRepository userRepository;

    // 찜 설정
    public List<UserBookDto> setDibs(String isbn, String bookname, String bookImgUrl) {
        log.trace("UserBookService > setDibs()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        UserBookDto userBookDto = UserBookDto.builder()
                .isbn(isbn)
                .bookname(bookname)
                .bookImageURL(bookImgUrl)
                .build();
        user.getDibsBooks().add(userBookDto);
        userRepository.save(user);

        return user.getDibsBooks();
    }

    // 찜 해제
    public List<UserBookDto> removeDibs(String isbn) {
        log.trace("UserBookService > removeDibs()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        user.getDibsBooks().removeIf(userBookDto -> userBookDto.getIsbn().equals(isbn));
        userRepository.save(user);
        return user.getDibsBooks();
    }

    // 찜 리스트 조회
    public List<UserBookDto> getDibsList() {
        log.trace("UserBookService > getDibsList()");
        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user.getDibsBooks();
    }

    // 찜 여부 확인
    public boolean isDibs(String isbn) {
        log.trace("UserBookService > isDibs()");
        User user = userService.loadLoggedinUser();
        return user.getDibsBooks().stream().anyMatch(userBookDto -> userBookDto.getIsbn().equals(isbn));
    }
}
