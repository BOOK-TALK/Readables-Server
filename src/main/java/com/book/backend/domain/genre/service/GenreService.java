package com.book.backend.domain.genre.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
    }

    @Transactional
    public void addBook(Genre genre, Book book) {
        if (genre.getLevel() == 2) {
            genre.getBooks().add(book);
            if (book.getGenre() != genre) {
                book.setGenre(genre);
            }
        } else {
            throw new UnsupportedOperationException("2단계 장르만 책 리스트를 가질 수 있습니다.");
        }
    }

}