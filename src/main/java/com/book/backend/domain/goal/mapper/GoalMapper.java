package com.book.backend.domain.goal.mapper;

import com.book.backend.domain.book.dto.BookInfoDto;
import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.book.mapper.BookMapper;
import com.book.backend.domain.goal.dto.GoalDto;
import com.book.backend.domain.goal.dto.RecordDto;
import com.book.backend.domain.goal.dto.RecordIntervalDto;
import com.book.backend.domain.goal.entity.Goal;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class GoalMapper {
    private final ModelMapper mapper;
    private final OpenAPI openAPI;
    private final ResponseParser responseParser;
    private final BookMapper bookMapper;

    public GoalDto convertToGoalDto(Goal goal) throws Exception {
        GoalDto goalDto = mapper.map(goal, GoalDto.class);
        goalDto.setBookSummary(getBookSummaryDto(goal.getIsbn()));
        User user = goal.getUser();

        goalDto.setUserNickname(user.getNickname());

        List<RecordIntervalDto> aWeekRecords = getAWeekRecords(goal.getRecords());
        goalDto.setAWeekRecords(aWeekRecords);

        return goalDto;
    }

    private BookSummaryDto getBookSummaryDto(String isbn) throws Exception {
        String subUrl = "usageAnalysisList";
        DetailRequestDto requestDto = new DetailRequestDto(isbn);

        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        BookInfoDto bookInfo = responseParser.getBookInfo(jsonResponse);

        return bookMapper.convertToBookSummaryDto(bookInfo);
    }

    public List<RecordIntervalDto> getAWeekRecords(List<RecordDto> records) {
        Map<LocalDate, RecordDto> recordsMap = mapRecordsByDate(records);
        return generateAWeekDaysRecords(recordsMap);
    }

    private Map<LocalDate, RecordDto> mapRecordsByDate(List<RecordDto> records) {
        // 기존 레코드를 날짜별로 매핑
        return records.stream()
                .collect(Collectors.toMap(RecordDto::getDate,
                        record -> record,
                        (existing, replacement) -> existing));
    }

    private List<RecordIntervalDto> generateAWeekDaysRecords(Map<LocalDate, RecordDto> recordsMap) {
        LocalDate today = LocalDate.now();

        // 최근 7일 동안의 날짜를 생성하고 해당 날짜에 대응하는 RecordDto를 추출
        return IntStream.rangeClosed(0, 6)
                .mapToObj(today::minusDays)
                .map(date -> {
                    // RecordMap에서 해당 날짜의 RecordDto를 가져오거나, 없으면 새로운 RecordDto 생성
                    RecordDto record = recordsMap.getOrDefault(date, new RecordDto(date, 0));

                    // 이전 날짜의 RecordDto 가져오기
                    RecordDto previousRecord = recordsMap.get(date.minusDays(1));

                    // 페이지 간격 계산
                    int pageInterval = (previousRecord != null)
                            ? record.getRecentPage() - previousRecord.getRecentPage()
                            : record.getRecentPage();

                    // RecordIntervalDto 객체 생성
                    return new RecordIntervalDto(date, pageInterval);
                })
                .sorted(Comparator.comparing(RecordIntervalDto::getDate))
                .collect(Collectors.toList());
    }

}
