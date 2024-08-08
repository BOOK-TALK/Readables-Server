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

    public LinkedList<LoanItemSrchResponseDto> periodTrend(JSONObject jsonResponse, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();

        for (LoanItemSrchResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }
            responseList.add(response);
        }
        return responseList;
    }

    public LinkedList<LoanItemSrchResponseDto> random(JSONObject jsonResponse, int resultSize, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();

        for (LoanItemSrchResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }
            responseList.add(response);
        }
        return RandomPicker.randomPick(responseList, resultSize);
    }

    public LinkedList<LoanItemSrchResponseDto> newTrend(JSONObject jsonResponse, int currentYear, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();

        for (LoanItemSrchResponseDto response : loanTrendResponseList) {
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
