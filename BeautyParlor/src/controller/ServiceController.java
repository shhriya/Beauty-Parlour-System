package controller;
import dao.ServiceDAO;
import model.Service;
import parlor.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public class ServiceController {
	private ServiceDAO serviceDAO;
    public ServiceController(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }
    public Service getServiceById(int serviceId) throws SQLException {
        return serviceDAO.getServiceById(serviceId);
    }
    public ServiceController() {
        try {
            Connection connection = DBConnection.getConnection();
            this.serviceDAO = new ServiceDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addService(int serviceId,String name, String description, double price) {
        try {
			Service service = new Service(serviceId,name, description, price);
            serviceDAO.addService(service);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean serviceExists(String serviceId) {
        try {
            return serviceDAO.serviceExists(serviceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updateService(int serviceId,String name, String description, double price) {
        try {
            Service service = new Service(serviceId,name, description, price);
            serviceDAO.updateService(service);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteService(String serviceId) {
        try {
            serviceDAO.deleteService(serviceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Service> getAllServices() {
        try {
            return serviceDAO.getAllServices();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Service> getAvailableServices() {
        try {
            return serviceDAO.getAllServices();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getFormattedServiceInfo(Service service) {
        return "Service ID: " + service.getServiceId() +
               "\nService Name: " + service.getName() +
               "\nDescription: "+service.getDescription()+
               "\nPrice: " + service.getPrice() +
               "\n-------------------------";
    }
}