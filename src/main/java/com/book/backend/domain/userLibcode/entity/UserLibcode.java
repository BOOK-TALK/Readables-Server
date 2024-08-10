package com.book.backend.domain.userLibcode.entity;

import com.book.backend.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/* 중간 테이블 */
@Entity
@Table(name = "user_libcode")
@Getter
@Setter
public class UserLibcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLibcodeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "libcode_id")
    private User libcodeId;
}
