package com.book.backend.domain.userOpentalk.entity;

import com.book.backend.domain.opentalk.entity.Opentalk;
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

@Entity
@Table(name = "user_opentalk")
@Getter
@Setter
public class userOpentalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userOpentalkId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "opentalk_id")
    private Opentalk opentalkId;
}
