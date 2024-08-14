package com.book.backend.domain.auth.repository;

import com.book.backend.domain.auth.entity.RefreshToken;
import com.book.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
}
