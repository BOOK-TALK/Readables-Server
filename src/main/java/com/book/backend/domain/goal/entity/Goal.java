package com.book.backend.domain.goal.entity;

import com.book.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "goal")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private String isbn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer totalPage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isFinished;
}
