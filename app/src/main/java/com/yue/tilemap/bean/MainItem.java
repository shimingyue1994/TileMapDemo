package com.yue.tilemap.bean;

/**
 * 时间：2018/1/16 9:40
 * 作者：shimy
 * 类名：MainItem
 * 描述：首页跳转item
 */


public class MainItem {

    private String name;
    private String desc;
    private Class aClass;

    public MainItem(String name, String desc, Class aClass) {
        this.name = name;
        this.desc = desc;
        this.aClass = aClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
