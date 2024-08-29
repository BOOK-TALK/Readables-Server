package com.book.backend.domain.record.mapper;

import com.book.backend.domain.record.dto.RecordDto;
import com.book.backend.domain.record.entity.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordMapper {
    public List<RecordDto> convertToRecordsDto(List<Record> records) {
        List<RecordDto> recordDtos = new LinkedList<>();
        for(Record record : records) {
            RecordDto recordDto = RecordDto.builder()
                    .date(LocalDate.from(record.getDate()))
                    .recentPage(record.getRecentPage())
                    .build();
            recordDtos.add(recordDto);
        }
        return recordDtos;
    }
}
