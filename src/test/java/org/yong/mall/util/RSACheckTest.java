package org.yong.mall.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yong on 2017/6/21.
 */
public class RSACheckTest {


    public static void main(String[] args) throws AlipayApiException {
        Map params = Maps.newHashMap();
        params.put("gmt_create","2017-06-21 16:09:05");
        params.put("charset","utf-8");
        params.put("seller_email","wgjiqe9403@sandbox.com");
        System.out.print(AlipaySignature.getSignCheckContentV2(params));
    }
}
