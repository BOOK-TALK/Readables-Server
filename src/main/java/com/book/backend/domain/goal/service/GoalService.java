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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserService userService;
    private final GoalMapper goalMapper;

    public GoalDto getGoal(Long goalId) throws Exception {
        log.trace("GoalService > getGoal()");

        // 목표 조회
        Goal goal = validateAndGetGoal(goalId);

        return goalMapper.convertToGoalDto(goal);
    }

    @Transactional
    public GoalDto createGoal(String isbn, String totalPage) throws Exception {
        log.trace("GoalService > createGoal()");

        User user = validateAndGetLoggedInUser();
        validateIsExistGoal(user, isbn);

        LocalDateTime now = LocalDateTime.now();

        // 목표 생성
        Goal goal = Goal.builder()
                .isbn(isbn)
                .user(user)
                .recentPage(null)
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

        User user = validateAndGetLoggedInUser();
        Goal goal = validateAndGetGoal(goalId);
        validateUserMatchesGoal(user, goal);

        // 목표 완료
        goal.setIsFinished(true);
        goalRepository.save(goal);

        return goalMapper.convertToGoalDto(goal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        log.trace("GoalService > deleteGoal()");

        User user = validateAndGetLoggedInUser();
        Goal goal = validateAndGetGoal(goalId);
        validateUserMatchesGoal(user, goal);

        // 목표 삭제
        goalRepository.delete(goal);
    }

    // 유저 검증 및 로그인된 유저 로드
    private User validateAndGetLoggedInUser() {
        log.trace("GoalService > validateAndGetLoggedInUser()");

        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    // 목표 검증 및 로드
    private Goal validateAndGetGoal(Long goalId) {
        log.trace("GoalService > validateAndGetLoggedInUser()");

        return goalRepository.findById(goalId)
                .orElseThrow(() -> new CustomException(ErrorCode.GOAL_NOT_FOUND));
    }

    // 해당 유저의 목표인지 검증
    private void validateUserMatchesGoal(User user, Goal goal) {
        log.trace("GoalService > validateUserMatchesGoal()");

        if (user != goal.getUser()) {
            throw new CustomException(ErrorCode.CANNOT_ACCESS_GOAL);
        }
    }

    // 해당 책에 대한 목표가 이미 존재하는지 검증
    private void validateIsExistGoal(User user, String isbn) {
        log.trace("GoalService > validateIsExistGoal()");

        if (goalRepository.findByUserAndIsbn(user, isbn).isPresent()) {
            throw new CustomException(ErrorCode.GOAL_IS_ALREADY_EXIST);
        }
    }
}
