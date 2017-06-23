package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;

import java.util.HashMap;

/**
 * Created by Yong on 2017/6/19.
 */
public interface IPayService {

    BaseResponse aliPay(Integer userId, Long orderNo, String path);

    BaseResponse aliCallback(HashMap<String, String> params);
}
