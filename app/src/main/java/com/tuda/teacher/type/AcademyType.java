package com.tuda.teacher.type;

public enum AcademyType {
    H("H"),
    A("A"),
    P("P");

    private String type;

    AcademyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
