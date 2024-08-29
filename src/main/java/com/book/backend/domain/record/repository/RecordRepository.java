package com.book.backend.domain.record.repository;

import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByGoal(Goal goal);
    Record findByGoalAndDate(Goal goal, LocalDateTime date);
    void deleteAllByGoal(Goal goal);
}
