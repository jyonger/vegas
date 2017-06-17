package org.yong.mall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Yong on 2017/6/15.
 */
public class CartVo {

    private List<CartGoodsVo> goodsVoList;
    private boolean checkAll;
    private BigDecimal total;
    private String imageHost;

    public List<CartGoodsVo> getGoodsVoList() {
        return goodsVoList;
    }

    public void setGoodsVoList(List<CartGoodsVo> goodsVoList) {
        this.goodsVoList = goodsVoList;
    }

    public boolean isCheckAll() {
        return checkAll;
    }

    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
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
