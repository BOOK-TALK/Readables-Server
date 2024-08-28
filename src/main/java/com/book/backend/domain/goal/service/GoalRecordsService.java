package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordDto;
import com.book.backend.domain.goal.dto.RecordIntervalDto;
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

        if (goal.getIsFinished()) {
            throw new CustomException(ErrorCode.GOAL_IS_ALREADY_FINISHED);
        }
        if (recentPage > goal.getTotalPage()) {
            throw new CustomException(ErrorCode.EXCEED_TOTAL_PAGE);
        }

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

    public List<RecordIntervalDto> getTotalAWeekRecords() {
        log.trace("GoalService > getTotalAWeekRecords()");

        // 유저 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        List<RecordIntervalDto> totalAWeekRecords = goalMapper.initializesAWeekRecords();

        List<Goal> goals = user.getGoals();
        for (Goal goal : goals) {
            List<RecordDto> records = goal.getRecords();
            List<RecordIntervalDto> aWeekRecords = goalMapper.convertAWeekRecords(records);

            // 날짜별로 합산 처리
            for (RecordIntervalDto record : aWeekRecords) {
                LocalDate recordDate = record.getDate();
                // userAWeekRecords에서 동일한 날짜를 찾아 pageInterval 합산
                for (RecordIntervalDto totalRecord : totalAWeekRecords) {
                    if (totalRecord.getDate().equals(recordDate)) {
                        // 기존 값에 새로운 값 합산, null이면 0으로 처리
                        Integer existingInterval = totalRecord.getPageInterval();
                        Integer newInterval = record.getPageInterval();
                        totalRecord.setPageInterval(
                                (existingInterval == null ? 0 : existingInterval)
                                        + (newInterval == null ? 0 : newInterval)
                        );
                        break;  // 해당 날짜에 대한 처리 완료 후 반복 중단
                    }
                }
            }
        }

        return totalAWeekRecords;
    }

    private RecordDto getMostRecentRecord(Goal goal) {
        log.trace("GoalRecordsService > getMostRecentRecord()");

        Optional<RecordDto> mostRecentRecordOptional = goal.getRecords().stream()
                .max(Comparator.comparing(RecordDto::getDate));
        return mostRecentRecordOptional.orElse(null);
    }
}
