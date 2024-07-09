package model;
public class Service {
    private int serviceId;
    private String name;
    private String description;
    private double price;
    public Service(int serviceId,String name, String description, double price) {
    	this.serviceId=serviceId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
	public int getServiceId() {
        return serviceId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}