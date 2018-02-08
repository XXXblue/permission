package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3022:50
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
public class UserParam {
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(min = 0, max = 20, message = "用户名长度为2-20")
    private String username;

    @NotBlank(message = "电话不能为空")
    @Length(min = 1, max = 13, message = "电话长度为1-13")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Length(min = 5, max = 20, message = "邮箱长度为5-20")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "状态不能为空")
    @Min(value = 0,message = "用户状态不合法")
    @Max(value = 2,message = "用户状态不合法")
    private Integer status;

    @Length(min=0,max =200,message = "长度要200字以内")
    private String remark;

}
