package com.framework.common.validator;

import org.apache.commons.lang.StringUtils;

import com.framework.common.exception.CommonException;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new CommonException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new CommonException(message);
        }
    }
}
