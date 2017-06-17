package org.yong.mall.dict;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Yong on 2017/6/15.
 */
public class GoodsDict {

    public interface Order {
        public Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum StatusEnum {

        ON_SALE(1, "上架"),
        OFF_SALE(2, "下架"),;

        private int code;
        private String desc;

        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
