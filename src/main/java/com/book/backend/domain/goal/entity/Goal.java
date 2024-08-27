package com.book.backend.domain.goal.entity;

import com.book.backend.domain.goal.dto.RecordsDto;
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

    private String recentPage;

    private String totalPage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isFinished;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "goal_a_week_records", joinColumns = @JoinColumn(name = "goal_id"))
    @Column(name = "records")
    private List<RecordsDto> AWeekRecords;
}
