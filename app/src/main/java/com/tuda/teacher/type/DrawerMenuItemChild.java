package com.tuda.teacher.type;

public class DrawerMenuItemChild<T> {
    private String name;
    private String tag;
    private T data;
    private boolean selected = false;

    public DrawerMenuItemChild() {
    }

    public DrawerMenuItemChild(String name) {
        this(name, null);
    }

    public DrawerMenuItemChild(String name, String tag) {
        this(name, tag, null);
    }

    public DrawerMenuItemChild(String name, String tag, T data) {
        this.name = name;
        this.tag = tag;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return this.selected;
    }
}
