package com.inclusive.finance.room;




public class User extends MyObject {
 
    public int id; //

    public String BaseCreateTime;
    public String BaseModifyTime;
    public String BaseCreatorId;
    public String BaseModifierId;
    public String UserName;//登录名称
    public String Password;//登录密码
    public String RealName;//姓名
    public String DepartmentId;
    public String DepartmentName;//部门名称
    public String UserStatus;//用户状态 0 待审核 1已通过 2 未通过
    public String IsSystem;//用户角色
    public String LoginCount;
    public Boolean isClick;

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public User(int id, String baseCreateTime, String baseModifyTime, String baseCreatorId, String baseModifierId, String userName, String password, String realName, String departmentId, String departmentName, String userStatus, String isSystem, String loginCount) {
        this.id = id;
        BaseCreateTime = baseCreateTime;
        BaseModifyTime = baseModifyTime;
        BaseCreatorId = baseCreatorId;
        BaseModifierId = baseModifierId;
        UserName = userName;
        Password = password;
        RealName = realName;
        DepartmentId = departmentId;
        DepartmentName = departmentName;
        UserStatus = userStatus;
        IsSystem = isSystem;
        LoginCount = loginCount;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCreateTime() {
        return BaseCreateTime;
    }

    public void setBaseCreateTime(String baseCreateTime) {
        BaseCreateTime = baseCreateTime;
    }

    public String getBaseModifyTime() {
        return BaseModifyTime;
    }

    public void setBaseModifyTime(String baseModifyTime) {
        BaseModifyTime = baseModifyTime;
    }

    public String getBaseCreatorId() {
        return BaseCreatorId;
    }

    public void setBaseCreatorId(String baseCreatorId) {
        BaseCreatorId = baseCreatorId;
    }

    public String getBaseModifierId() {
        return BaseModifierId;
    }

    public void setBaseModifierId(String baseModifierId) {
        BaseModifierId = baseModifierId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getIsSystem() {
        return IsSystem;
    }

    public void setIsSystem(String isSystem) {
        IsSystem = isSystem;
    }

    public String getLoginCount() {
        return LoginCount;
    }

    public void setLoginCount(String loginCount) {
        LoginCount = loginCount;
    }
}