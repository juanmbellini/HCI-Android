package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class Address {

    @Expose
    int id;
    @Expose
    String name;
    @Expose
    String street;
    @Expose
    String number;
    @Expose
    String floor;
    @Expose
    String gate;
    @Expose
    String province;
    @Expose
    String city;
    @Expose
    String zipCode;
    @Expose
    String phoneNumber;

    public Address(int id, String name, String street, String number, String floor,
                   String gate, String province, String city, String zipCode,
                   String phoneNumber) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.number = number;
        this.floor = floor;
        this.gate = gate;
        this.province = province;
        this.city = city;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
