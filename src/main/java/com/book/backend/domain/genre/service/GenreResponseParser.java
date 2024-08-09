package com.book.backend.domain.genre.service;

import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.service.RandomPicker;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class GenreResponseParser {
    private final ResponseParser responseParser;
    private static final int NEW_TREND_YEAR_OFFSET = 2; // 최근 트렌드로 간주되는 연도 범위

    public LinkedList<LoanItemSrchResponseDto> periodTrend(JSONObject jsonResponse, Integer maxSize) {
        return filterResponses(jsonResponse, maxSize, null);
    }

    public LinkedList<LoanItemSrchResponseDto> random(JSONObject jsonResponse, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> filteredResponses = filterResponses(jsonResponse, maxSize, null);
        return RandomPicker.randomPick(filteredResponses, maxSize);
    }

    public LinkedList<LoanItemSrchResponseDto> newTrend(JSONObject jsonResponse, int currentYear, Integer maxSize) {
        return filterResponses(jsonResponse, maxSize, currentYear);
    }

    private LinkedList<LoanItemSrchResponseDto> filterResponses(JSONObject jsonResponse, Integer maxSize, Integer yearThreshold) {
        LinkedList<LoanItemSrchResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();

        for (LoanItemSrchResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            if (yearThreshold != null) {
                int publicationYear;
                try {
                    publicationYear = Integer.parseInt(response.getPublication_year());
                } catch (NumberFormatException e) {
                    continue; // 유효하지 않은 년도 데이터는 무시
                }

                if (publicationYear >= yearThreshold - NEW_TREND_YEAR_OFFSET) {
                    responseList.add(response);
                }
            } else {
                responseList.add(response);
            }
        }
        return responseList;
    }
}
