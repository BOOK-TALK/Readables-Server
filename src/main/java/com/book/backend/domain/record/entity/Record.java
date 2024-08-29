package com.book.backend.domain.record.entity;

import com.book.backend.domain.goal.entity.Goal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private LocalDateTime date;

    private Integer recentPage;
}
