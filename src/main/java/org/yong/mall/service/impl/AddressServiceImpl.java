package org.yong.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yong.mall.dao.UserAddressMapper;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserAddress;
import org.yong.mall.service.IAddressService;

import java.util.List;

/**
 * Created by Yong on 2017/6/16.
 */
@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    UserAddressMapper addressMapper;

    @Override
    public BaseResponse add(Integer userId, UserAddress address) {
        address.setUserId(userId);
        if (addressMapper.insert(address) > 0) {
            return BaseResponse.getSuccess(address.getId().toString());
        }
        return BaseResponse.getErrorMessage("新增地址出错");
    }

    @Override
    public BaseResponse update(Integer userId, UserAddress address) {
        if (address.getId() == null) {
            return new BaseResponse(ResultEnum.ILLEGAL_ARGUMENT.getCode(), ResultEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        address.setUserId(userId);
        if (addressMapper.updateByIdAndUserId(address) > 0) {
            return BaseResponse.getSuccessMessage("更新地址成功");
        }
        return BaseResponse.getErrorMessage("更新地址出错");
    }

    @Override
    public BaseResponse remove(Integer userId, Integer addressId) {
        if (addressId == null) {
            return new BaseResponse(ResultEnum.ILLEGAL_ARGUMENT.getCode(), ResultEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        if (addressMapper.removeByIdAndUser(userId, addressId) > 0) {
            return BaseResponse.getSuccessMessage("删除地址成功");
        }
        return BaseResponse.getErrorMessage("删除地址出错");
    }

    @Override
    public BaseResponse listAddressByUser(Integer userId) {
        List<UserAddress> addressList = addressMapper.listAddressByUser(userId);
        if (CollectionUtils.isEmpty(addressList)) {
            return BaseResponse.getErrorMessage("暂无地址信息");
        }
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), addressList);
    }
}
