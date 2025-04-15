package com.ninetrees.musicapp.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Classname:CodeUtils
 * Description:
 */
public class CodeUtils {
    public static String generateCode() {
        return RandomStringUtils.randomNumeric(6); // 生成 6 位数字验证码
    }
}
