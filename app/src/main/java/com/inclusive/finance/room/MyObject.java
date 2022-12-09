package com.inclusive.finance.room;

public class MyObject extends Object implements Cloneable{
     @Override
     public Object clone(){
        MyObject sc = null;
        try
        {
            sc = (MyObject) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return sc;
    }
}
