package com.mmall.param;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/122:26
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class AclMoudleParam {

    private Integer id;

    @NotBlank(message = "权限模块名字不能为空")
    @Length(min = 2, max = 20, message = "权限模块名称长度2-20")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "权限模块顺序不能为空")
    private Integer seq;
    @NotNull
    @Min(value = 0,message = "状态非法")
    @Max(value = 1,message = "状态非法")
    private Integer status;
    @Length(max = 200, min = 2,message = "权限模块备注2-200")
    private String remark;
}
