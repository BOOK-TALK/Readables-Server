package com.book.backend.domain.goal.controller;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.service.GoalService;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "목표", description = "목표 삭제 / 목표 조회 / 목표 생성 / 목표 완료")
public class GoalController {
    private final GoalService goalService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    @Operation(summary = "목표 정보 조회", description = "목표 ID에 해당하는 목표의 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "goalId", description = "목표 ID")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GoalDto.class)),
                    description = GoalDto.description)})
    @GetMapping("/get")
    public ResponseEntity<?> getGoal(@RequestParam Long goalId) throws Exception {
        log.trace("GoalController > getGoal()");

        GoalDto goalDto = goalService.getGoal(goalId);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }

    @Operation(summary = "목표 생성", description = "책 isbn 번호와 총 페이지 수를 입력받아 해당 책에 대한 목표를 생성합니다. " +
            "유저는 하나의 책 당 하나의 목표만 생성할 수 있습니다.",
            parameters = {
                    @Parameter(name = "isbn", description = "책 isbn"),
                    @Parameter(name = "totalPage", description = "총 페이지 수")
            },
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = GoalDto.class)),
                    description = GoalDto.description)})
    @PostMapping("/create")
    public ResponseEntity<?> createGoal(@RequestParam String isbn,
                                        @RequestParam String totalPage) throws Exception {
        log.trace("GoalController > createGoal()");
        requestValidate.isValidIsbn(isbn);
        requestValidate.isValidBookPageNum(totalPage);

        GoalDto goalDto = goalService.createGoal(isbn, totalPage);

        return responseTemplate.success(goalDto, HttpStatus.CREATED);
    }

    @Operation(summary = "목표 완료", description = "목표 ID에 해당하는 목표를 완료 처리합니다.",
            parameters = {
                    @Parameter(name = "goalId", description = "목표 ID")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GoalDto.class)),
                    description = GoalDto.description)})
    @PutMapping("/finish")
    public ResponseEntity<?> finishGoal(@RequestParam Long goalId) throws Exception {
        log.trace("GoalController > finishGoal()");

        GoalDto goalDto = goalService.finishGoal(goalId);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }

    @Operation(summary = "목표 삭제", description = "목표 ID에 해당하는 목표를 삭제합니다.",
            parameters = {
                    @Parameter(name = "goalId", description = "목표 ID")
            },
            responses = {@ApiResponse(responseCode = "200")})
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteGoal(@RequestParam Long goalId) {
        log.trace("GoalController > deleteGoal()");

        goalService.deleteGoal(goalId);

        return responseTemplate.success("목표가 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
