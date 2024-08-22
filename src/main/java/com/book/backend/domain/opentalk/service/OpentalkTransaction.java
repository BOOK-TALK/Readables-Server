package com.book.backend.domain.opentalk.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.book.repository.BookRepository;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.opentalk.repository.OpentalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class OpentalkTransaction {
    private final BookRepository bookRepository;
    private final OpentalkRepository opentalkRepository;

    // 같은 클래스 내의 메소드 호출인 자기호출을 하면 트랜잭션이 수행되지 않는다고 해서 분리함
    @Transactional
    public Long createOpentalkIdByIsbn(String isbn) { // 새로운 오픈톡 생성
        Book newBook = new Book();
        newBook.setIsbn(isbn);
        Book book = bookRepository.save(newBook);

        Opentalk newOpentalk = new Opentalk();
        newOpentalk.setBook(book);
        Opentalk opentalk = opentalkRepository.save(newOpentalk);
        return opentalk.getOpentalkId();
    }

    public Long checkExistOpentalk(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null) return null;
        return opentalkRepository.findByBook(book).getOpentalkId();
    }
}
