package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2816:43
 * @Description:
 * @Modified By:
 */
//这里一定要注意使用的StringUtils
    //0
    //0.1
    //0.1.2
public class LevelUtil {
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    public final static String calculateLevel(String parentLevel,int parentId){
        if(StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else {
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }
}
