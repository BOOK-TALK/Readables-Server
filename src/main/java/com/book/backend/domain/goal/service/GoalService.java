package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.dto.*;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.mapper.GoalMapper;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.record.dto.RecordDto;
import com.book.backend.domain.record.entity.Record;
import com.book.backend.domain.record.mapper.RecordMapper;
import com.book.backend.domain.record.repository.RecordRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    public GoalDto getGoal(Long goalId) throws Exception {
        log.trace("GoalService > getGoal()");

        // 목표 조회
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);

        return goalMapper.convertToGoalDto(goal);
    }

    public List<GoalDto> getUserGoals(Boolean isFinished) throws Exception {
        log.trace("GoalService > getUserGoals()");

        // 유저 검증
        User user = goalRequestValidate.validateAndGetLoggedInUser();

        List<Goal> goals = user.getGoals();
        List<GoalDto> goalDtos = new LinkedList<>();

        // GoalDto로 변경
        for (Goal goal : goals) {
            if (isFinished == null || isFinished.equals(goal.getIsFinished())) {
                GoalDto dto = goalMapper.convertToGoalDto(goal);
                goalDtos.add(dto);
            }
        }

        return goalDtos;
    }

    public List<UserProgressDto> getUsersInGoal(String isbn, Boolean isFinished) {
        log.trace("GoalService > getUsersInGoal()");

        List<Goal> goals;
        if (isFinished == null) {
            goals = goalRepository.findByIsbn(isbn);
        } else {
            goals = goalRepository.findByIsbnAndIsFinished(isbn, isFinished);
        }

        List<UserProgressDto> userProgressDtos = new LinkedList<>();

        for (Goal goal : goals) {
            List<Record> records = recordRepository.findAllByGoal(goal);
            List<RecordDto> recordDtos = recordMapper.convertToRecordsDto(records);
            Integer mostRecentPage = goalMapper.getMostRecentPage(recordDtos);

            double progressRate = goalMapper.getFormattedProgressRate((double) goal.getTotalPage(), (double) mostRecentPage);

            UserProgressDto userProgressDto = UserProgressDto.builder()
                    .nickname(goal.getUser().getNickname())
                    .progressRate(progressRate)
                    .profileImageUrl(goal.getUser().getProfileImageUrl())
                    .build();
            userProgressDtos.add(userProgressDto);
        }

        return userProgressDtos;
    }

    public MyProgressDto getMyProgress(String isbn) {
        log.trace("GoalService > getMyProcess()");

        User user = goalRequestValidate.validateAndGetLoggedInUser();
        Goal goal = goalRepository.findByUserAndIsbn(user, isbn)
                .orElse(null);

        if (goal == null) {
            return MyProgressDto.builder()
                    .isInProgress(false)
                    .progressRate(null)
                    .build();
        }
        List<Record> records = recordRepository.findAllByGoal(goal);
        List<RecordDto> recordDtos = recordMapper.convertToRecordsDto(records);

        double progressRate = goalMapper.getFormattedProgressRate((double) goal.getTotalPage(),
                (double) goalMapper.getMostRecentPage(recordDtos));

        if (goal.getIsFinished() == Boolean.TRUE) {
            return MyProgressDto.builder()
                    .isInProgress(false)
                    .progressRate(progressRate)
                    .build();
        } else {
            return MyProgressDto.builder()
                    .isInProgress(true)
                    .progressRate(progressRate)
                    .build();
        }
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

        // 이미 종료되어 있다면 에러 발생
        if (goal.getIsFinished()) {
            throw new CustomException(ErrorCode.GOAL_IS_ALREADY_FINISHED);
        }

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

}
