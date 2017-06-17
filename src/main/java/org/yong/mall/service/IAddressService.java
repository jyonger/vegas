package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.UserAddress;

/**
 * Created by Yong on 2017/6/16.
 */
public interface IAddressService {

    BaseResponse add(Integer userId, UserAddress address);
    BaseResponse update(Integer userId, UserAddress address);
    BaseResponse remove(Integer userId,Integer addressId);
    BaseResponse listAddressByUser(Integer userId);
}
