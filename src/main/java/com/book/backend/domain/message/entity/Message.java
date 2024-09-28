package com.book.backend.domain.message.entity;

import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "message", indexes = @Index(name = "idx_message_createdAt", columnList = "createdAt"))
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "opentalk_id")
    private Opentalk opentalk;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type;

    private String content;

    private String imageUrl;

    private Date createdAt;
}
