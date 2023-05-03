package com.example.ics.models.dtos;

import com.example.ics.validation.ValidImageAddress;

public class ImageAddressDto {
    @ValidImageAddress
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
