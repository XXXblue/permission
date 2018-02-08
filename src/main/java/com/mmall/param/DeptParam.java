package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2816:12
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class DeptParam {
    private Integer id;
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15, min = 2, message = "部门名称在2-15之间")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;
    @Length(max = 150, message = "备注的长度不能超过150字")
    private String remark;
}
