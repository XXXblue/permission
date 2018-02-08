package com.mmall.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3121:26
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult <T>{

    private List<T> data = Lists.newArrayList();

    private int total=0;
}
