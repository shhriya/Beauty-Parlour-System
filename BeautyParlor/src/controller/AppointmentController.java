package controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import dao.AppointmentDAO;
import model.Appointment;
public class AppointmentController {
	private AppointmentDAO appointmentDAO;
    public AppointmentController(Connection connection) {
        this.appointmentDAO = new AppointmentDAO(connection);
    }
    public int addAppointment(Appointment appointment, int customerId) {
        try {
            return appointmentDAO.addAppointment(appointment, customerId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }
    public List<Appointment> getAppointmentById(int customerId) {
        return appointmentDAO.getAppointmentById(customerId);
    }
    public List<Appointment> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        return appointmentDAO.getAppointmentsByDate(today);
    }
    public List<Appointment> getAllAppointments() {
        try {
            return appointmentDAO.getAllAppointments();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    public boolean updateAppointment(Appointment appointment) {
        try {
            appointmentDAO.updateAppointment(appointment);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public boolean deleteAppointment(int appointmentId) {
        try {
            appointmentDAO.deleteAppointment(appointmentId);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public AppointmentController(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }
    public List<Appointment> getFinishedAppointments() {
        try {
            return appointmentDAO.getFinishedAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Appointment> getIncomingAppointments() {
        try {
            return appointmentDAO.getIncomingAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void deleteAppointmentFromCust(int customerId) {
        try {
            appointmentDAO.deleteAppointmentsByCustomerId(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}