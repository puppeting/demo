package com.inclusive.finance.room;

public class SysLog {
    public Integer id;
    public String UserId;
    public String Keyword	;
    public String BaseCreateTime;
    public String UserName;
    public String RealName;
    public String DepartmentName;
    public Boolean isClick;

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public SysLog() {
    }

    public SysLog(Integer id, String userId, String keyword, String baseCreateTime, String userName, String realName, String departmentName) {
        this.id = id;
        UserId = userId;
        Keyword = keyword;
        BaseCreateTime = baseCreateTime;
        UserName = userName;
        RealName = realName;
        DepartmentName = departmentName;
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

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public String getBaseCreateTime() {
        return BaseCreateTime;
    }

    public void setBaseCreateTime(String baseCreateTime) {
        BaseCreateTime = baseCreateTime;
    }
}
