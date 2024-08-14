package com.book.backend.domain.auth.entity;

import com.book.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    // TODO: 향후 Redis 사용 고려
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
