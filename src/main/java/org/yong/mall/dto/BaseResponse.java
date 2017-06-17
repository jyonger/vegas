package org.yong.mall.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.yong.mall.enums.ResultEnum;

/**
 * Created by Yong on 2017/6/6.
 */
@JsonSerialize( include =  JsonSerialize.Inclusion.NON_NULL)
public class BaseResponse<T> {
    private int status;
    private String info;
    private T data;

    public BaseResponse(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public BaseResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public BaseResponse(int status, String info, T data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public T getData() {
        return data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResultEnum.SUCCESS.getCode();
    }

    public static BaseResponse getErrorMessage(String info) {
        return new BaseResponse(ResultEnum.ERROR.getCode(), info);
    }

    public static BaseResponse getSuccessMessage(String info) {
        return new BaseResponse(ResultEnum.ERROR.getCode(), info);
    }

    public static <T> BaseResponse<T> getSuccess(String data) {
        return new BaseResponse<T>(ResultEnum.SUCCESS.getCode(), data);
    }

    public static <T> BaseResponse<T> getError(String data) {
        return new BaseResponse<T>(ResultEnum.SUCCESS.getCode(), data);
    }
}
