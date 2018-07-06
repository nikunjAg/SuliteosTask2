package com.roshanjha.loginscreenmd;

public class DonatorModelClass {

    public String name1,email1, address1, mobile1, bloodGroup1,lastDonatedDate1;

    public DonatorModelClass() {
    }

    public DonatorModelClass(String name1, String email1, String address1, String mobile1, String bloodGroup1, String lastDonatedDate1) {
        this.name1 = name1;
        this.email1 = email1;
        this.address1 = address1;
        this.mobile1 = mobile1;
        this.bloodGroup1 = bloodGroup1;
        this.lastDonatedDate1 = lastDonatedDate1;
    }

    public String getName1() {
        return name1;
    }


    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getBloodGroup1() {
        return bloodGroup1;
    }

    public void setBloodGroup1(String bloodGroup1) {
        this.bloodGroup1 = bloodGroup1;
    }

    public String getLastDonatedDate1() {
        return lastDonatedDate1;
    }

    public void setLastDonatedDate1(String lastDonatedDate1) {
        this.lastDonatedDate1 = lastDonatedDate1;
    }
}
