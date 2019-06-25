package com.vivolvle.redis_cache.utils;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:51
 */
public class Base64Util {

    /**
     * 加密
     *
     * @param string
     * @return
     */
    public static String string2Base(String string) {
        return new String(Base64.encodeBase64(string.getBytes()));
    }

    /**
     * 解密
     *
     * @param base
     * @return
     */
    public static String base2String(String base) {
        return new String(Base64.decodeBase64(base));
    }

    public static void main(String[] args) {
        System.out.println(base2String("bnVsbA=="));
    }
}
