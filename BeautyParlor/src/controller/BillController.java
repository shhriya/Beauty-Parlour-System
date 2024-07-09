package controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import dao.AppointmentDAO;
import dao.BillDAO;
import model.Appointment;
import model.Bill;
import model.Customer;
import model.Service;
//import parlor.DBConnection;
import dao.CustomerDAO;
public class BillController {
    private BillDAO billDAO;
    //private Connection connection;
    private ServiceController serviceController;
    private CustomerDAO customerDAO;
    private AppointmentDAO appointmentDAO;
    public BillController(Connection connection, ServiceController serviceController, CustomerDAO customerDAO, AppointmentDAO appointmentDAO) {
        //this.connection = connection;
        this.billDAO = new BillDAO(connection);
        this.serviceController = serviceController;
        this.customerDAO = customerDAO;
        this.appointmentDAO = appointmentDAO;
    }
    public Bill getBillForAppointmentId(int appointmentId,int customerId) {
        return billDAO.getBillForAppointmentId(appointmentId,customerId);
    }
    public List<Bill> getBillsForCustomer(int customerId) {
        return billDAO.getBillsForCustomer(customerId);
    }
    public int createBillForAppointment(Appointment appointment,int appointmentId) throws SQLException {
        try {
            List<Appointment> appointments = appointmentDAO.getAppointmentById(appointmentId);
            if (appointments.isEmpty()) {
                System.out.println("Error: Appointment with ID " + appointment.getAppointmentId() + " not found."+appointmentId);
                return -1;
            }
            Appointment retrievedAppointment = appointments.get(0);
            double totalAmount = calculateTotalAmount(retrievedAppointment.getServiceId());
            double discount = calculateDiscount(totalAmount);
            double amountToPay = calculateAmountToPay(totalAmount, discount);
            Customer customer = customerDAO.getCustomerById(retrievedAppointment.getCustomerId());
            if (customer == null) {
                System.out.println("Error: Customer with ID " + retrievedAppointment.getCustomerId() + " not found.");
                return -1;
            }
            Bill bill = new Bill();
            bill.setCustomerId(retrievedAppointment.getCustomerId());
            bill.setEmail(customer.getEmail());
            bill.setAppointmentId(retrievedAppointment.getAppointmentId());
            bill.setTotalAmount(totalAmount);
            bill.setDiscount(discount);
            bill.setAmountToPay(amountToPay);
            return billDAO.createBill(bill);
        } catch (SQLException e) {
            System.err.println("Error creating bill: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    private double calculateTotalAmount(int serviceId) throws SQLException {
        Service service = serviceController.getServiceById(serviceId);
        if (service != null) {
            return service.getPrice();
        } else {
            return 0.0;
        }
    }
    private double calculateDiscount(double totalAmount) {
        return 10.0;
    }
    private double calculateAmountToPay(double totalAmount, double discount) {
        return totalAmount - discount;
    }
//    private String getCustomerEmail(int customerId) {
//        CustomerDAO customerDAO = new CustomerDAO(connection);
//        Customer customer = customerDAO.getCustomerById(customerId);
//        if (customer != null) {
//            return customer.getEmail();
//        } else {
//            return "customer_not_found@example.com";
//        }
//    }
}