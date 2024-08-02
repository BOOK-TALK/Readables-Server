package com.book.backend.domain.genre.entity;

import com.book.backend.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "genre")
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    private String name;

    private String kdcNum;

    @ManyToOne
    @JoinColumn(name = "parent_genre_id", nullable = true)
    private Genre parentGenre;

    @OneToMany(mappedBy = "parentGenre", cascade = CascadeType.ALL)
    private List<Genre> subGenres;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Book> books;

    public Integer getLevel() {
        if (parentGenre == null) {
            return 1;
        }
        return 2;
    }

    public List<Book> getBooks() {
        if (this.getLevel() == 3) {
            return books;
        } else {
            return null;
        }
    }

    public void addBook(Book book) {
        if (this.getLevel() == 3) {
            this.books.add(book);
            if (book.getGenre() != this) {
                book.setGenre(this);
            }
        } else {
            throw new UnsupportedOperationException("2단계 장르만 책 리스트를 가질 수 있습니다.");
        }
    }

}
