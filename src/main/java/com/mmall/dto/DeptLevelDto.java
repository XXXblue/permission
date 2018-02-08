package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2817:20
 * @Description:
 * @Modified By:
 */
//适配
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> deptList = Lists.newArrayList();

//    原数据库类转适配类
    public static DeptLevelDto adapt(SysDept sysDept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(sysDept, dto);
        return dto;
    }
}
