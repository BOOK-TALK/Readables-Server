package com.book.backend.domain.goal.mapper;

import com.book.backend.domain.book.dto.BookInfoDto;
import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.book.mapper.BookMapper;
import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordIntervalDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import com.book.backend.domain.record.dto.RecordDto;
import com.book.backend.domain.record.entity.Record;
import com.book.backend.domain.record.mapper.RecordMapper;
import com.book.backend.domain.record.repository.RecordRepository;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalMapper {
    private final ModelMapper mapper;
    private final OpenAPI openAPI;
    private final ResponseParser responseParser;
    private final BookMapper bookMapper;
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    public GoalDto convertToGoalDto(Goal goal) throws Exception {
        log.trace("GoalMapper > convertToGoalDto()");

        GoalDto goalDto = mapper.map(goal, GoalDto.class);
        goalDto.setBookSummary(getBookSummaryDto(goal.getIsbn()));
        User user = goal.getUser();

        // 유저 닉네임
        goalDto.setUserNickname(user.getNickname());

        // 일주일 기록
        List<Record> records = recordRepository.findAllByGoal(goal);
        List<RecordDto> recordDtos = recordMapper.convertToRecordsDto(records);
        List<RecordIntervalDto> aWeekRecords = convertAWeekRecords(recordDtos);
        goalDto.setAWeekRecords(aWeekRecords);

        // 최근 페이지 업데이트
        goalDto.setRecentPage(getMostRecentPage(recordDtos));

        return goalDto;
    }

    private BookSummaryDto getBookSummaryDto(String isbn) throws Exception {
        log.trace("GoalMapper > getBookSummaryDto()");

        String subUrl = "usageAnalysisList";
        DetailRequestDto requestDto = new DetailRequestDto(isbn);

        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto(), 1);
        BookInfoDto bookInfo = responseParser.getBookInfo(jsonResponse);

        return bookMapper.convertToBookSummaryDto(bookInfo);
    }

    // 일주일 기록으로 변경
    public List<RecordIntervalDto> convertAWeekRecords(List<RecordDto> records) {
        log.trace("GoalMapper > convertAWeekRecords()");

        Map<LocalDate, RecordDto> recordsMap = mapRecordsByDate(records);
        return generateAWeekDaysRecords(recordsMap);
    }

    // 마지막으로 읽은 페이지 반환
    public Integer getMostRecentPage(List<RecordDto> records) {
        log.trace("GoalMapper > getMostRecentPage()");

        if (records == null || records.isEmpty()) {
            return 0; // 레코드가 없는 경우 0을 반환
        }

        // 가장 최근 날짜의 레코드 찾기
        RecordDto mostRecentRecord = records.stream()
                .max(Comparator.comparing(RecordDto::getDate))
                .orElse(null);

        // 가장 최근 레코드의 recentPage 반환
        return mostRecentRecord.getRecentPage();
    }

    private Map<LocalDate, RecordDto> mapRecordsByDate(List<RecordDto> records) {
        log.trace("GoalMapper > mapRecordsByDate()");

        // records가 null이면 빈 리스트로 대체
        if (records == null) {
            records = new LinkedList<>();
        }

        // 기존 레코드를 날짜별로 매핑
        return records.stream()
                .collect(Collectors.toMap(RecordDto::getDate,
                        record -> record,
                        (existing, replacement) -> existing));
    }

    private List<RecordIntervalDto> generateAWeekDaysRecords(Map<LocalDate, RecordDto> recordsMap) {
        log.trace("GoalMapper > generateAWeekDaysRecords()");

        LocalDate today = LocalDate.now();

        // 최근 7일 동안의 날짜를 생성하고 해당 날짜에 대응하는 RecordDto를 추출
        return IntStream.rangeClosed(0, 6)
                .mapToObj(today::minusDays)
                .map(date -> {
                    // 이전 날짜의 RecordDto 가져오기
                    RecordDto previousRecord = recordsMap.get(date.minusDays(1));

                    // RecordMap에서 해당 날짜의 RecordDto를 가져오거나, 없으면 새로운 RecordDto 생성
                    RecordDto record = recordsMap.getOrDefault(date,
                            new RecordDto(date, (previousRecord != null) ? previousRecord.getRecentPage() : 0));

                    // 페이지 간격 계산
                    int pageInterval = (previousRecord != null)
                            ? record.getRecentPage() - previousRecord.getRecentPage()
                            : record.getRecentPage();

                    // RecordIntervalDto 객체 생성
                    return new RecordIntervalDto(date, pageInterval);
                })
                .sorted(Comparator.comparing(RecordIntervalDto::getDate))
                .toList();
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

    public double getFormattedProgressRate(double totalPage, double mostRecentPage) {
        double progressRate = 100 * mostRecentPage / totalPage;
        return Double.parseDouble(String.format("%.2f", progressRate));
    }

}
