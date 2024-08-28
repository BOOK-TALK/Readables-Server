package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordDto;
import com.book.backend.domain.goal.dto.RecordIntervalDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.mapper.GoalMapper;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GoalService {
    private final GoalRepository goalRepository;
    private final GoalRequestValidate goalRequestValidate;
    private final GoalMapper goalMapper;

    public GoalDto getGoal(Long goalId) throws Exception {
        log.trace("GoalService > getGoal()");

        // 목표 조회
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);

        return goalMapper.convertToGoalDto(goal);
    }

    public List<GoalDto> getUserGoals() throws Exception {
        log.trace("GoalService > getUserGoals()");

        // 유저 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();

        List<Goal> goals = user.getGoals();
        List<GoalDto> goalDtos = new LinkedList<>();

        // GoalDto로 변경
        for (Goal goal : goals) {
            GoalDto dto = goalMapper.convertToGoalDto(goal);
            goalDtos.add(dto);
        }

        return goalDtos;
    }

    public List<RecordIntervalDto> getTotalAWeekRecords() {
        log.trace("GoalService > getTotalAWeekRecords()");

        // 유저 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        List<RecordIntervalDto> totalAWeekRecords = initializesAWeekRecords();

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

    @Transactional
    public GoalDto createGoal(String isbn, Integer totalPage) throws Exception {
        log.trace("GoalService > createGoal()");

        // 유저, 목표 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        goalRequestValidate.validateIsExistGoal(user, isbn);

        LocalDateTime now = LocalDateTime.now();

        // 목표 생성
        Goal goal = Goal.builder()
                .isbn(isbn)
                .user(user)
                .totalPage(totalPage)
                .createdAt(now)
                .updatedAt(now)
                .isFinished(false)
                .build();

        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    @Transactional
    public GoalDto finishGoal(Long goalId) throws Exception {
        log.trace("GoalService > finishGoal()");

        // 유저, 목표 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);
        goalRequestValidate.validateUserMatchesGoal(user, goal);

        // 목표 완료
        goal.setIsFinished(true);
        goal.setUpdatedAt(LocalDateTime.now());

        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        log.trace("GoalService > deleteGoal()");

        // 유저, 목표 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);
        goalRequestValidate.validateUserMatchesGoal(user, goal);

        // 목표 삭제
        goalRepository.delete(goal);
    }

    public List<RecordIntervalDto> initializesAWeekRecords() {
        log.trace("GoalService > initializesAWeekRecords()");

        List<RecordIntervalDto> aWeekRecords = new LinkedList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            RecordIntervalDto dto = RecordIntervalDto.builder()
                    .date(today.minusDays(i))
                    .pageInterval(null)
                    .build();
            aWeekRecords.add(dto);
        }
        return aWeekRecords;
    }

}
