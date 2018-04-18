package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressEntity {
    private String zipcode;
    private String roadAddress;
    private String dongAddress;

    public AddressEntity() {
    }

    public AddressEntity(String zipcode, String roadAddress, String dongAddress) {
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.dongAddress = dongAddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getDongAddress() {
        return dongAddress;
    }

    public void setDongAddress(String dongAddress) {
        this.dongAddress = dongAddress;
    }
}
