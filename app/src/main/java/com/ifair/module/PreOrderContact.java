package com.ifair.module;

/**
 * 功能
 */

public class PreOrderContact {

    private String order_address_id;
    private String email;
    private String type;
    private String name;
    private String phone;
    private String address;
    private boolean ischeck;

    public PreOrderContact(String order_address_id, String email, String type, String name, String phone, String address, boolean ischeck) {
        this.order_address_id = order_address_id;
        this.email = email;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.ischeck = ischeck;
    }

    public String getOrder_address_id() {
        return order_address_id;
    }

    public void setOrder_address_id(String order_address_id) {
        this.order_address_id = order_address_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    @Override
    public String toString() {
        return "PreOrderContact{" +
                "order_address_id='" + order_address_id + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", ischeck=" + ischeck +
                '}';
    }
}
