package com.book.backend.domain.openapi.dto.response;

import com.book.backend.domain.detail.dto.BookInfoDto;
import com.book.backend.domain.detail.dto.CoLoanBooksDto;
import com.book.backend.domain.detail.dto.HighLoanUserGroupDto;
import com.book.backend.domain.detail.dto.KeywordDto;
import com.book.backend.domain.detail.dto.RecommendDto;
import java.util.LinkedList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailResponseDto implements OpenAPIResponseInterface {
    private BookInfoDto bookInfoDto;
    private List<HighLoanUserGroupDto> highLoanUserGroupDtoArray;
    private LinkedList<String> keywordDtoList;
    private LinkedList<CoLoanBooksDto> coLoanBooksDtoList;
    private LinkedList<RecommendDto> recommendResponseDtoList;
}
