package com.book.backend.domain.genre.service;

import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.service.RandomPicker;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreResponseParser {
    private final ResponseParser responseParser;
    private static final int NEW_TREND_YEAR_OFFSET = 2;  // 최근 트렌드 연도 범위

    public LinkedList<LoanItemSrchResponseDto> periodTrend(JSONObject jsonResponse,
                                                           String filteredPageNo, String filteredPageSize) {
        log.trace("GenreResponseParser > periodTrend()");

        LinkedList<LoanItemSrchResponseDto> filteredResponse = filterResponses(jsonResponse, null, null);
        return responseParser.customPageFilter(filteredResponse, filteredPageNo, filteredPageSize);
    }

    public LinkedList<LoanItemSrchResponseDto> random(JSONObject jsonResponse, Integer maxSize) {
        log.trace("GenreResponseParser > random()");

        LinkedList<LoanItemSrchResponseDto> filteredResponses = filterResponses(jsonResponse, null, maxSize);
        return RandomPicker.randomPick(filteredResponses, maxSize);
    }

    public LinkedList<LoanItemSrchResponseDto> newTrend(JSONObject jsonResponse, int currentYear,
                                                        String filteredPageNo, String filteredPageSize) {
        log.trace("GenreResponseParser > newTrend()");

        LinkedList<LoanItemSrchResponseDto> filteredResponse = filterResponses(jsonResponse, currentYear, null);
        return responseParser.customPageFilter(filteredResponse, filteredPageNo, filteredPageSize);
    }

    private LinkedList<LoanItemSrchResponseDto> filterResponses(JSONObject jsonResponse, Integer yearThreshold, Integer maxSize) {
        log.trace("GenreResponseParser > filterResponses()");

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
