package com.mmall.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3121:26
 * @Description:
 * @Modified By:
 */
public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1,message="当前页面不合法")
    private int pageNo=1;

    @Getter
    @Setter
    @Min(value = 1,message="当页条数不合法")
    private int pageSize=10;

    @Setter
    private int offset;

    public int getOffset(){
        return  (pageNo-1)*pageSize;
    }
}
