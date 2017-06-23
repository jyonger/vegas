package org.yong.mall.enums;

/**
 * Created by Yong on 2017/6/19.
 */
public enum PayPlatformEnum {
    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信");

    private int code;
    private String value;

    PayPlatformEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {

        return value;
    }

    public static PayPlatformEnum codeOf(int code) {
        for (PayPlatformEnum platformEnum : values()) {
            if (platformEnum.code == code) {
                return platformEnum;
            }
        }
        return null;
    }
}
