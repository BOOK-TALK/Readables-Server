package com.book.backend.domain.goal.controller;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.service.GoalRecordsService;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goal/record")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "목표 기록", description = "기록 추가")
public class GoalRecordsController {
    private final GoalRecordsService goalRecordsService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    @PostMapping("/add")
    public ResponseEntity<?> addRecord(@RequestParam Long goalId,
                                       @RequestParam Integer recentPage) throws Exception {
        log.trace("GoalRecordsController > addRecord()");
        requestValidate.isValidBookPageNum(recentPage);

        GoalDto goalDto = goalRecordsService.addRecord(goalId, recentPage);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }
}
