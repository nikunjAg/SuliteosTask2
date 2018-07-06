package com.roshanjha.loginscreenmd;

public class HospitalModelClass {

    String name,myaddress,myemail,number,bloodRequired;

    public HospitalModelClass() {}

    public HospitalModelClass(String name, String myaddress, String myemail, String number, String bloodRequired) {
        this.name = name;
        this.myaddress = myaddress;
        this.myemail = myemail;
        this.number = number;
        this.bloodRequired = bloodRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyemail() {
        return myemail;
    }

    public void setMyemail(String myemail) {
        this.myemail = myemail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMyaddress() {
        return myaddress;
    }

    public void setMyaddress(String myaddress) {
        this.myaddress = myaddress;
    }

    public String getBloodRequired() {
        return bloodRequired;
    }

    public void setBloodRequired(String bloodRequired) {
        this.bloodRequired = bloodRequired;
    }
}
