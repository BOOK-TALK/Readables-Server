package com.book.backend.domain.goal.entity;

import com.book.backend.domain.record.entity.Record;
import com.book.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records;
}
