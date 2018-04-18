package com.tuda.teacher.type;

public enum SortType {
    ASC("ASC"),
    DESC("DESC");

    private String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
