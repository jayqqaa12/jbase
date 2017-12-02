package com.jayqqaa12.jbase.util;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandKit {

    /***
     * 输入 id 和 概率 返回 id
     *
     * 如果概率不满 100 可能返回 null
     *
     * 如 {[1,5],[2,15],[3,80] }
     *
     */

    public static <T> T probabilityRandom(T[] input, int[] probability) {

        if (input.length == 0 || probability.length == 0 || probability.length != input.length)
            throw new IllegalArgumentException("error input or probability length ");

        List<T> list = Lists.newArrayList();
        for (int i = 0; i < probability.length; i++) {
            for (int j = 0; j < probability[i]; j++) {
                list.add(input[j]);
            }
        }

        return list.get(new Random().nextInt(Arrays.stream(probability).sum()));

    }

}
