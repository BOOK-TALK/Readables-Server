package com.book.backend.domain.goal.repository;

import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findByUserAndIsbn(User user, String isbn);
}
