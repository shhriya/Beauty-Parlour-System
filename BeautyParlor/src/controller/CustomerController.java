package controller;
import dao.CustomerDAO;
import dao.ServiceDAO;
import model.Customer;
import model.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CustomerController {
    private CustomerDAO customerDAO;
    private ServiceDAO serviceDAO;
    private List<Customer> customers;
    private AppointmentController appointmentController;
    public CustomerController(Connection connection) {
        this.customerDAO = new CustomerDAO(connection);
        this.serviceDAO = new ServiceDAO(connection);
        this.customers = new ArrayList<>();
        this.appointmentController = new AppointmentController(connection);
    }
    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }
    public boolean login(String email, String pass) {
        try {
            return customerDAO.login(email, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean register(Customer customer) {
        try {
            customerDAO.register(customer);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Customer getCustomerByEmail(String email) {
        try {
            return customerDAO.getCustomerByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean updateCustomer(Customer customer) {
        try {
            customerDAO.updateCustomer(customer);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteCustomer(Customer customer) {
        int customerId = customer.getCustomerId();
        System.out.println(customerId);
        try {
            appointmentController.deleteAppointmentFromCust(customerId);
            customerDAO.deleteCustomer(customerId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
    public boolean customerExists(String email) {
        Customer customer = getCustomerByEmail(email);
        return customer != null;
    }
    public int getCustomerIdByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                return customer.getCustomerId();
            }
        }
        return 0;
    }
    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}