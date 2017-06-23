package org.yong.mall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Yong on 2017/6/21.
 */
public class OrderGoodsVo {

    private List<OrderItemVo> itemVos;
    private BigDecimal total;
    private String imageHost;

    public List<OrderItemVo> getItemVos() {
        return itemVos;
    }

    public void setItemVos(List<OrderItemVo> itemVos) {
        this.itemVos = itemVos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
