package com.book.backend.domain.goal.service;

import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.goal.repository.GoalRepository;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.service.UserService;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalRequestValidate {
    private final UserService userService;
    private final GoalRepository goalRepository;

    // 유저 검증 및 로그인된 유저 로드
    public User validateAndGetLoggedInUser() {
        log.trace("GoalService > validateAndGetLoggedInUser()");

        User user = userService.loadLoggedinUser();
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    // 목표 검증 및 로드
    public Goal validateAndGetGoal(Long goalId) {
        log.trace("GoalService > validateAndGetLoggedInUser()");

        return goalRepository.findById(goalId)
                .orElseThrow(() -> new CustomException(ErrorCode.GOAL_NOT_FOUND));
    }

    // 해당 유저의 목표인지 검증
    public void validateUserMatchesGoal(User user, Goal goal) {
        log.trace("GoalService > validateUserMatchesGoal()");

        if (user != goal.getUser()) {
            throw new CustomException(ErrorCode.CANNOT_ACCESS_GOAL);
        }
    }

    // 해당 책에 대한 목표가 이미 존재하는지 검증
    public void validateIsExistGoal(User user, String isbn) {
        log.trace("GoalService > validateIsExistGoal()");

        if (goalRepository.findByUserAndIsbn(user, isbn).isPresent()) {
            throw new CustomException(ErrorCode.GOAL_IS_ALREADY_EXIST);
        }
    }
}
