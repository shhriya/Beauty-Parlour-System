package model;
public class Bill {
    private int billId;
    private int customerId;
    private String email;
    private int appointmentId;
    private double totalAmount;
    private double discount;
    private double amountToPay;
    public Bill() {
    }
    public Bill(int customerId, String email, int appointmentId, double totalAmount,double discount, double amountToPay) {
        this.customerId = customerId;
        this.email = email;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.amountToPay = amountToPay;
    }
    public int getBillId() {
        return billId;
    }
    public void setBillId(int billId) {
        this.billId = billId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public double getAmountToPay() {
        return amountToPay;
    }
    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }
    public double calculateNetAmount() {
        return totalAmount - discount;
    }
}