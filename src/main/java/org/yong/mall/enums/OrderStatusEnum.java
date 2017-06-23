package org.yong.mall.enums;

/**
 * Created by Yong on 2017/6/19.
 */
public enum OrderStatusEnum {
    CANCELED(0, "已取消"),
    NON_PAY(10, "未付款"),
    PAID(20, "已付款"),
    SHIPPED(40, "已发货"),
    ORDER_SUCCESS(50, "交易完成"),
    ORDER_CLOSE(60, "交易关闭");


    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatusEnum codeOf(int code){
        for (OrderStatusEnum statusEnum : values()){
            if (statusEnum.getCode() == code){
                return statusEnum;
            }
        }
        return null;
    }
}
