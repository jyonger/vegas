package org.yong.mall.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * Created by Yong on 2017/6/8.
 */
public class MD5Util {

    public static String encode(String value) {
        return Hashing.md5().newHasher().putString(value, Charsets.UTF_8).hash().toString();
    }

}
