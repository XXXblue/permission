package com.mmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/815:23
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class SearchLogDto {
    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;//yyyy-MM-dd hh:mm:ss

    private Date toTime;//yyyy-MM-dd hh:mm:ss
}
