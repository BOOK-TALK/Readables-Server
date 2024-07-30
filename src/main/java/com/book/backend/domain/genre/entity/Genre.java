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

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Genre parentGenre;

    @OneToMany(mappedBy = "parentGenre", cascade = CascadeType.ALL)
    private List<Genre> subGenres;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Book> books;

    public boolean isLeaf() {
        return subGenres == null || subGenres.isEmpty();
    }

    public List<Book> getBooks() {
        if (isLeaf()) {
            return books;
        } else {
            return null;
        }
    }

}
