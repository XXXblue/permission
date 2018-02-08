package com.mmall.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

public class StringUtil {

    public static List<Integer> splitToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        List<Integer> list = Lists.newArrayList();
        for (String s : strList){
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
