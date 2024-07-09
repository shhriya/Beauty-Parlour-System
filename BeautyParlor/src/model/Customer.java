package model;
public class Customer {
    private int customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String pass;
    public Customer() {
    }
    public Customer(int customerId,String name, String phoneNumber, String email, String address, String pass) {
    	this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.pass=pass;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPass() {
        return pass;
    }
}