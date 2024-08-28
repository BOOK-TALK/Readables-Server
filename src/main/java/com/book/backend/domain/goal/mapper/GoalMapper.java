package com.book.backend.domain.goal.mapper;

import com.book.backend.domain.book.dto.BookInfoDto;
import com.book.backend.domain.book.dto.BookSummaryDto;
import com.book.backend.domain.book.mapper.BookMapper;
import com.book.backend.domain.goal.dto.GoalDto;
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
        return goalDto;
    }

    private BookSummaryDto getBookSummaryDto(String isbn) throws Exception {
        String subUrl = "usageAnalysisList";
        DetailRequestDto requestDto = new DetailRequestDto(isbn);

        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        BookInfoDto bookInfo = responseParser.getBookInfo(jsonResponse);

        return bookMapper.convertToBookSummaryDto(bookInfo);
    }

//    private List<RecordDto> getAWeekRecords(List<RecordDto> records) {
//        LocalDate today = LocalDate.now();
//
//        List<RecordDto> filteredAndSortedRecords = records.stream()
//                .filter(record -> !record.getDate().isBefore(today.minusDays(6)))
//                .sorted(Comparator.comparing(RecordDto::getDate))
//                .toList();
//
//        List<RecordDto> aWeekRecords = new LinkedList<>();
//        IntStream.rangeClosed(0, 6).forEach(i -> {
//            LocalDate date =
//        });
//    }

}
