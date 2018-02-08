package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/815:15
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class SearchLogParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private String fromTime;//yyyy-MM-dd

    private String toTime;
}
