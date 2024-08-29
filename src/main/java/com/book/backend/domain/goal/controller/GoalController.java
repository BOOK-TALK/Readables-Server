package com.book.backend.domain.goal.controller;

import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordIntervalDto;
import com.book.backend.domain.goal.dto.UserProgressDto;
import com.book.backend.domain.goal.service.GoalRecordsService;
import com.book.backend.domain.goal.service.GoalService;
import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.global.ResponseTemplate;
import io.lettuce.core.dynamic.annotation.Param;
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

import java.util.List;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "목표", description = "목표 삭제 / 목표 조회 / 목표 진행한 사용자 조회 / 유저 목표 조회 / " +
        "전체 목표에 대한 합산 일주일 기록 조회 / 목표 생성 / 기록 추가 / 목표 완료")
public class GoalController {
    private final GoalService goalService;
    private final GoalRecordsService goalRecordsService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    @Operation(summary = "목표 조회", description = "목표 ID에 해당하는 목표의 정보를 조회합니다.",
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

    @Operation(summary = "전체 목표 조회", description = "유저가 생성한 모든 목표를 반환합니다. " +
            "완료 여부 미입력 시 모든 목표가 반환되며, 입력 시 완료 여부에 해당하는 목표를 반환합니다.",
            parameters = {
                    @Parameter(name = "isFinished", description = "완료 여부")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GoalDto.class)),
                    description = GoalDto.description)})
    @GetMapping("/get/total")
    public ResponseEntity<?> getUserGoals(@RequestParam(required = false) Boolean isFinished) throws Exception {
        log.trace("GoalController > getUserGoals()");

        List<GoalDto> goalDtos = goalService.getUserGoals(isFinished);

        return responseTemplate.success(goalDtos, HttpStatus.OK);
    }

    @Operation(summary = "전체 목표에 대한 합산 일주일 기록 조회", description = "유저의 모든 목표에 대해 합산된 일주일 기록을 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RecordIntervalDto.class)),
                    description = RecordIntervalDto.description)})
    @GetMapping("/get/totalAWeek")
    public ResponseEntity<?> getTotalAWeekRecords() {
        log.trace("GoalController > getTotalAWeekRecords()");

        List<RecordIntervalDto> totalAWeekRecords = goalRecordsService.getTotalAWeekRecords();

        return responseTemplate.success(totalAWeekRecords, HttpStatus.OK);
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
                                        @RequestParam Integer totalPage) throws Exception {
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
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGoal(@RequestParam Long goalId) {
        log.trace("GoalController > deleteGoal()");

        goalService.deleteGoal(goalId);

        return responseTemplate.success("목표가 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "기록 추가", description = "목표 ID에 해당하는 목표에 기록을 추가합니다. " +
            "최근 기록에서 중단한 페이지보다 큰 페이지가 입력되어야 합니다.",
            parameters = {
                    @Parameter(name = "goalId", description = "목표 ID"),
                    @Parameter(name = "recentPage", description = "중단한 페이지")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GoalDto.class)),
                    description = GoalDto.description)})
    @PostMapping("/addRecord")
    public ResponseEntity<?> addRecord(@RequestParam Long goalId,
                                       @RequestParam Integer recentPage) throws Exception {
        log.trace("GoalController > addRecord()");
        requestValidate.isValidBookPageNum(recentPage);

        GoalDto goalDto = goalRecordsService.addRecord(goalId, recentPage);

        return responseTemplate.success(goalDto, HttpStatus.OK);
    }

    @Operation(summary = "목표 진행한 사용자 조회",
            description = "책 isbn을 입력받아 해당 책에 대한 목표를 진행한 사용자(닉네임 + 완독율) 목록을 반환합니다. " +
                    "완료 여부 미입력 시 모든 사용자 목록이 반환되며, 입력 시 완료 여부에 해당하는 사용자 목록을 반환합니다. " +
                    "완독율은 소수점 아래 둘째 자리까지 반환됩니다.",
            parameters = {
                    @Parameter(name = "isbn", description = "책 isbn"),
                    @Parameter(name = "isFinished", description = "완료 여부")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserProgressDto.class)),
                    description = UserProgressDto.description)})
    @GetMapping("/get/usersInGoal")
    public ResponseEntity<?> getUsersInGoal(@RequestParam String isbn,
                                            @RequestParam(required = false) Boolean isFinished) {
        log.trace("GoalController > getUsersInGoal()");
        requestValidate.isValidIsbn(isbn);

        List<UserProgressDto> usersInGoal = goalService.getUsersInGoal(isbn, isFinished);

        return responseTemplate.success(usersInGoal, HttpStatus.OK);
    }

}
