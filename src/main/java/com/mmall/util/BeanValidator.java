package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2723:24
 * @Description:
 * @Modified By:
 */
//全局校验工厂
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    //这个<T>在这里是为了说明后面是T的类不是泛型
    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
//                key是有问题的字段 value是错误信息
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map<String, String> errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

//    封装上面的两个
    public static Map<String, String> validateObject(Object object, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(object, objects));
        } else {
            return validate(object, new Class[0]);
        }
    }

//    交验完后如果集合为空不抛出异常，否则抛出异常给全局异常处理器处理
    public  static void check(Object param)throws ParamException{
        Map<String,String> map = BeanValidator.validateObject(param);
//        这里用了MapUtils做map的非空校验
        if(MapUtils.isNotEmpty(map)){
            throw new ParamException(map.toString());
        }
    }
}
