package com.inclusive.finance.room;

public class SysDepartment {
     int id=0;
    String BaseCreateTime="";
    String BaseModifyTime="";
    String BaseCreatorId="";
    String BaseModifierId="";
    String DepartmentName="";//部门名称
    String Telephone="";//手机号
    String DepartmentSort="";//部门排序
    String Remark="";
    String DepartmentType="";//部门类型
    String DepartmentTwoName="";//二级部门名称
    String DepartmentId="";//一级部门id
    public Boolean isClick=false;
    public SysDepartment() {
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public SysDepartment(int id, String baseCreateTime, String baseModifyTime, String baseCreatorId, String baseModifierId, String departmentName, String telephone, String departmentSort, String remark, String departmentType, String departmentTwoName, String departmentId) {
        this.id = id;
        BaseCreateTime = baseCreateTime;
        BaseModifyTime = baseModifyTime;
        BaseCreatorId = baseCreatorId;
        BaseModifierId = baseModifierId;
        DepartmentName = departmentName;
        Telephone = telephone;
        DepartmentSort = departmentSort;
        Remark = remark;
        DepartmentType = departmentType;
        DepartmentTwoName = departmentTwoName;
        DepartmentId = departmentId;
    }

    public String getDepartmentTwoName() {
        return DepartmentTwoName;
    }

    public void setDepartmentTwoName(String departmentTwoName) {
        DepartmentTwoName = departmentTwoName;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
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

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getDepartmentSort() {
        return DepartmentSort;
    }

    public void setDepartmentSort(String departmentSort) {
        DepartmentSort = departmentSort;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDepartmentType() {
        return DepartmentType;
    }

    public void setDepartmentType(String departmentType) {
        DepartmentType = departmentType;
    }
}
