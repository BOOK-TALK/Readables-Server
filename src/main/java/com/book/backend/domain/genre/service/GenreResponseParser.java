package com.book.backend.domain.genre.service;

import com.book.backend.domain.openapi.dto.response.LoanTrendResponseDto;
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

    public LinkedList<LoanTrendResponseDto> periodTrend(JSONObject jsonResponse, Integer maxSize) {
        LinkedList<LoanTrendResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        for (LoanTrendResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }
            responseList.add(response);
        }
        return responseList;
    }

    public LinkedList<LoanTrendResponseDto> random(JSONObject jsonResponse, int resultSize, Integer maxSize) {
        LinkedList<LoanTrendResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        for (LoanTrendResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }
            responseList.add(response);
        }
        return RandomPicker.randomPick(responseList, resultSize);
    }

    public LinkedList<LoanTrendResponseDto> newTrend(JSONObject jsonResponse, int currentYear, Integer maxSize) {
        LinkedList<LoanTrendResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanTrendResponseDto> responseList = new LinkedList<>();

        for (LoanTrendResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            int publication_year;
            try {
                publication_year = Integer.parseInt(response.getPublication_year());
            } catch (NumberFormatException e) {
                continue;
            }

            if (publication_year >= currentYear - 2) {
                responseList.add(response);
            }
        }
        return responseList;
    }
}
