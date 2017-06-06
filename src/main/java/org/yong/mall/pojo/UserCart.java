package org.yong.mall.pojo;

import java.util.Date;

public class UserCart {
    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

    public UserCart(Integer id, Integer userId, Integer goodsId, Integer quantity, Integer checked, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.checked = checked;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserCart() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}