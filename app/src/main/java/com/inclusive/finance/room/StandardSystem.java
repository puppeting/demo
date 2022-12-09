package com.inclusive.finance.room;

public class StandardSystem extends MyObject {
    public int id; //
    public Boolean isClick;

    public String StandardName;//一级体系名称
    public String StandardId;//一级体系ID

    public String StandardsSecondName;//2级体系名称
    public String StandardsSecondId;//2级体系名称
    public String StandardsThirdName;//3级体系名称
    public String StandardsThirdId;//3级体系名称

    public StandardSystem() {
    }

    public StandardSystem(int id, Boolean isClick, String standardName, String standardId, String standardsSecondName, String standardsSecondId, String standardsThirdName, String standardsThirdId) {
        this.id = id;
        this.isClick = isClick;
        StandardName = standardName;
        StandardId = standardId;
        StandardsSecondName = standardsSecondName;
        StandardsSecondId = standardsSecondId;
        StandardsThirdName = standardsThirdName;
        StandardsThirdId = standardsThirdId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public String getStandardName() {
        return StandardName;
    }

    public void setStandardName(String standardName) {
        StandardName = standardName;
    }

    public String getStandardId() {
        return StandardId;
    }

    public void setStandardId(String standardId) {
        StandardId = standardId;
    }

    public String getStandardsSecondName() {
        return StandardsSecondName;
    }

    public void setStandardsSecondName(String standardsSecondName) {
        StandardsSecondName = standardsSecondName;
    }

    public String getStandardsSecondId() {
        return StandardsSecondId;
    }

    public void setStandardsSecondId(String standardsSecondId) {
        StandardsSecondId = standardsSecondId;
    }

    public String getStandardsThirdName() {
        return StandardsThirdName;
    }

    public void setStandardsThirdName(String standardsThirdName) {
        StandardsThirdName = standardsThirdName;
    }

    public String getStandardsThirdId() {
        return StandardsThirdId;
    }

    public void setStandardsThirdId(String standardsThirdId) {
        StandardsThirdId = standardsThirdId;
    }
}
