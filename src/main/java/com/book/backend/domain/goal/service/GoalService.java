package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.mapper.GoalMapper;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.service.UserService;
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

    public GoalDto getGoal(Long goalId) throws Exception {
        log.trace("GoalService > getGoal()");

        // 목표 조회
        Goal goal = goalRequestValidate.validateAndGetGoal(goalId);

        return goalMapper.convertToGoalDto(goal);
    }

    public List<GoalDto> getUserGoals() throws Exception {
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


}
