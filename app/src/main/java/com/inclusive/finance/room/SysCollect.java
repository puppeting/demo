package com.inclusive.finance.room;

public class SysCollect {
    public Integer id;
    public String UserId;
    public String StandardId	;
    public String BaseCreateTime;



    public SysCollect() {
    }

    public SysCollect(Integer id, String userId, String standardId, String baseCreateTime) {
        this.id = id;
        UserId = userId;
        StandardId = standardId;
        BaseCreateTime = baseCreateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStandardId() {
        return StandardId;
    }

    public void setStandardId(String standardId) {
        StandardId = standardId;
    }

    public String getBaseCreateTime() {
        return BaseCreateTime;
    }

    public void setBaseCreateTime(String baseCreateTime) {
        BaseCreateTime = baseCreateTime;
    }
}
