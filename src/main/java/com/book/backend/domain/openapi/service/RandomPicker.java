package com.book.backend.domain.openapi.service;

import java.util.Collections;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomPicker {

    // list 에서 무작위로 nums 갯수만큼 추출한 리스트를 반환
    public static <T> LinkedList<T> randomPick(LinkedList<T> list, int nums){
        log.trace("RandomPicker > randomPick()");

        if (nums > list.size()) {
            throw new IllegalArgumentException("nums는 list의 크기를 넘을 수 없습니다.");
        }
        Collections.shuffle(list);

        return new LinkedList<>(list.subList(0, 10));
    }
}
