package com.book.backend.domain.opentalk.entity;

import com.book.backend.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "opentalk")
@Getter
@Setter
public class Opentalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opentalkId;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // TODO: 다른 필드 추가 예정
}
