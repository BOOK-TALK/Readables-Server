package com.book.backend.domain.openapi.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomPicker {

    // list 에서 무작위로 nums 갯수만큼 추출한 리스트를 반환
    public static <T> LinkedList<T> randomPick(LinkedList<T> list, Integer nums){
        log.trace("RandomPicker > randomPick()");
        if(list.size() <= nums) return list;

        int maxSize = Objects.requireNonNullElse(nums, 200);
        Collections.shuffle(list);

        return new LinkedList<>(list.subList(0, maxSize));
    }
}
