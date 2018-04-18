package com.tuda.teacher.type;

import java.util.ArrayList;

public class DrawerMenuItemParent<T> {
    private String name;
    private ArrayList<DrawerMenuItemChild> child = new ArrayList<>();
    private int icon = 0;
    private String iconUrl;
    private String tag;
    private T data;

    public DrawerMenuItemParent() {
    }

    public DrawerMenuItemParent(String name) {
        this(name, 0, null, null);
    }

    public DrawerMenuItemParent(String name, int iconResource) {
        this(name, iconResource, null, null);
    }

    public DrawerMenuItemParent(String name, int iconResource, String tag) {
        this(name, iconResource, tag, null);

    }

    public DrawerMenuItemParent(String name, int iconResource, ArrayList child) {
        this(name, iconResource, null, child);

    }

    public DrawerMenuItemParent(String name, String iconUrl, String tag, T data) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.tag = tag;
        this.data = data;
    }

    public DrawerMenuItemParent(String name, int iconResource, String tag, ArrayList<DrawerMenuItemChild> child) {
        this.name = name;
        this.icon = iconResource;
        this.tag = tag;
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DrawerMenuItemChild> getChilds() {
        return child;
    }

    public void setChilds(ArrayList child) {
        this.child = child;
    }

    public DrawerMenuItemChild getChild(int index) {
        return child.get(index);
    }

    public void addChild(DrawerMenuItemChild child) {
        this.child.add(child);
    }

    public void clearChild(DrawerMenuItemChild child) {
        this.child.clear();
    }

    public void setIcon(int iconResource) {
        this.icon = iconResource;
    }

    public int getIcon() {
        return this.icon;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setTag(Object data) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public boolean hasChild() {
        if (getChilds() != null && getChilds().size() > 0)
            return true;
        return false;
    }
}
