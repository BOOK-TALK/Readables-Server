package com.book.backend.domain.goal.controller;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.service.GoalService;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "목표", description = "")
public class GoalController {
    private final GoalService goalService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    @GetMapping("/get")
    public ResponseEntity<?> getGoal(@RequestParam Long goalId) throws Exception {
        log.trace("GoalController > getGoal()");

        GoalDto goalDto = goalService.getGoal(goalId);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGoal(@RequestParam String isbn,
                                        @RequestParam String totalPage) throws Exception {
        log.trace("GoalController > createGoal()");
        requestValidate.isValidIsbn(isbn);
        requestValidate.isValidBookPageNum(totalPage);

        GoalDto goalDto = goalService.createGoal(isbn, totalPage);

        return responseTemplate.success(goalDto, HttpStatus.CREATED);
    }

    @PutMapping("/finish")
    public ResponseEntity<?> finishGoal(@RequestParam Long goalId) throws Exception {
        log.trace("GoalController > finishGoal()");

        GoalDto goalDto = goalService.finishGoal(goalId);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }
}
