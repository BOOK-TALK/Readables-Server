package com.book.backend.domain.openapi.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class RandomPicker {
    // list 에서 nums 갯수를 랜덤 추출, 결과를 섞을지
    public static <T> LinkedList<T> randomPick(LinkedList<T> list, int nums, boolean shuffle){
        Random random = new Random();
        LinkedList<T> randomList = new LinkedList<>();
        HashSet<Integer> idxSet = new HashSet<>();  // 중복 확인용
        for(int i=0; i<nums; i++){
            int idx = random.nextInt(list.size());
            if(idxSet.add(idx)) randomList.add(list.get(idx));
        }
        if(shuffle) Collections.shuffle(list);
        return randomList;
    }
}
