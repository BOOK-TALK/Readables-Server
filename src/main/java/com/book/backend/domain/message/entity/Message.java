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

    private Date createdAt;

    private String content; // TODO : text로 리팩

    private String imageUrl;

    public void createMessage(Opentalk opentalk, User user, String content, String imageUrl){
        this.opentalk = opentalk;
        this.user = user;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = new Date();
    }
}
