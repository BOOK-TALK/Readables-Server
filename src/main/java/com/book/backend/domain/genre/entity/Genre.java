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


    public Integer getLevel() {
        if (parentGenre == null) {
            return 1;
        }
        return 2;
    }
}
