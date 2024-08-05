package com.book.backend.domain.genre.service;

import com.book.backend.domain.book.entity.Book;
import com.book.backend.domain.genre.entity.Genre;
import com.book.backend.domain.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Genre> findSubGenresByKdcNum(String kdcNum) {
        Optional<Genre> genre = genreRepository.findByKdcNum(kdcNum);
        return genre.map(Genre::getSubGenres).orElse(null);
    }

    public List<Book> getBooksByMainKdcNum(Integer kdcNum) {
        String mainKdcNum = String.valueOf(kdcNum);

        List<Book> allBooks = new ArrayList<>();
        List<Genre> subGenres = findSubGenresByKdcNum(mainKdcNum);

        if (subGenres != null) {
            for (Genre subGenre : subGenres) {
                if (subGenre.getBooks() != null) {
                    allBooks.addAll(subGenre.getBooks());
                }
            }
        }

        return allBooks;
    }

    public List<Book> getBooksByMiddleKdcNum(Integer kdcNum) {
        String mainKdcNum = String.valueOf(kdcNum / 10);
        String middleKdcNum = String.valueOf(kdcNum % 10);

        Genre findGenre = genreRepository.findByParentGenreKdcNumAndKdcNum(mainKdcNum, middleKdcNum)
                .orElseThrow(() -> new IllegalArgumentException("KDC 번호가" + kdcNum + "인 장르를 찾을 수 없습니다."));
        return findGenre.getBooks();
    }

}
