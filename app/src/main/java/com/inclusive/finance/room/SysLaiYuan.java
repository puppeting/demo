package com.inclusive.finance.room;

public class SysLaiYuan {
    public Integer id;
    public String Name;
    public Boolean isClick;

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public SysLaiYuan(Integer id, String name) {
        this.id = id;
        Name = name;
    }

    public SysLaiYuan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
