package org.yong.mall.dict;

/**
 * Created by Yong on 2017/6/19.
 */
public class PayDict {

    public interface AliPayStatus {
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

}
