package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.mapper.GoalMapper;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GoalRecordsService {
    private final GoalRepository goalRepository;
    private final GoalRequestValidate goalRequestValidate;
    private final GoalMapper goalMapper;

    @Transactional
    public GoalDto addRecord(Long goalId, Integer recentPage) throws Exception {
        log.trace("GoalRecordsService > addRecord()");

        // 유저, 목표 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);
        goalRequestValidate.validateUserMatchesGoal(user, goal);

        List<RecordDto> records = goal.getRecords();

        // 가장 최근 기록 로드
        RecordDto mostRecentRecord = getMostRecentRecord(goal);

        // 가장 최근 기록이 없다면
        if (mostRecentRecord == null) {
            RecordDto newRecord = RecordDto.builder()
                    .date(LocalDate.now())
                    .recentPage(recentPage)
                    .build();
            records.add(newRecord);
        }
        // 가장 최근 기록이 오늘이 아니면
        else if (mostRecentRecord.getDate().isBefore(LocalDate.now())) {
            if (recentPage <= mostRecentRecord.getRecentPage()) {
                throw new CustomException(ErrorCode.INVALID_RECENT_PAGE);
            }

            RecordDto newRecord = RecordDto.builder()
                    .date(LocalDate.now())
                    .recentPage(recentPage)
                    .build();
            records.add(newRecord);
        }
        // 가장 최근 기록이 오늘이면
        else {
            mostRecentRecord.setRecentPage(recentPage);
        }

        goal.setUpdatedAt(LocalDateTime.now());
        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    private RecordDto getMostRecentRecord(Goal goal) {
        log.trace("GoalRecordsService > getMostRecentRecord()");

        Optional<RecordDto> mostRecentRecordOptional = goal.getRecords().stream()
                .max(Comparator.comparing(RecordDto::getDate));
        return mostRecentRecordOptional.orElse(null);
    }
}
