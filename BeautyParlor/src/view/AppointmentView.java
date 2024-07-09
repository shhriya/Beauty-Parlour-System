package view;
//import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import controller.AppointmentController;
import controller.BillController;
import controller.CustomerController;
import controller.ServiceController;
//import dao.ServiceDAO;
import model.Service;
//import parlor.DBConnection;
import model.Appointment;
public class AppointmentView {
    private AppointmentController appointmentController;
    private CustomerController customerController;
    private ServiceController serviceController;
    private Scanner scanner;
    private int customerId;
	private BillController billController;
    public AppointmentView(int customerId, AppointmentController appointmentController, CustomerController customerController, ServiceController serviceController,BillController billController) {
        this.customerId = customerId;
        this.appointmentController = appointmentController;
        this.customerController = customerController;
        this.serviceController = serviceController;
        this.billController=billController;
        this.scanner = new Scanner(System.in);
    }
    public void addAppointment(int customerId) throws SQLException, ParseException {
        try {
            List<Service> availableServices = serviceController.getAllServices();
            System.out.println("Available Services:");
            for (int i = 0; i < availableServices.size(); i++) {
                Service service = availableServices.get(i);
                System.out.println(service.getServiceId() + ". " + service.getName() + " " + service.getPrice());
            }
            System.out.println("Enter the number of the service you want to book:");
            int serviceNumber = scanner.nextInt();
            if (serviceNumber >= 1 && serviceNumber <= availableServices.size()) {
                Service selectedService = availableServices.get(serviceNumber - 1);
                int appointmentId = 0;
                System.out.println("Enter date for the appointment (YYYY-MM-DD):");
                String dateStr = scanner.next();
                LocalDate date = LocalDate.parse(dateStr);
                System.out.println("Enter time for the appointment (HH:MM:SS) in 24 hour pattern");
                System.out.println("Book time within morning 08:00:00 to evening 18:00:00");
                String timeStr = scanner.next();
                LocalTime time = LocalTime.parse(timeStr);
                Appointment appointment = new Appointment(appointmentId, customerId, selectedService.getServiceId(), date, time);
                int id = appointmentController.addAppointment(appointment, customerId);
                if (id > 0) {
                    System.out.println("Appointment booked successfully!");
                    int billId = billController.createBillForAppointment(appointment,id);
                    if (billId > 0) {
                        System.out.println("Bill generated successfully!");
                    } else {
                        System.out.println("Failed to generate the bill. Please contact support.");
                    }
                    boolean appointmentOptionsExit = false;
                    while (!appointmentOptionsExit) {
                        System.out.println("Edit Appointment if needed(Cannot update or delete appointment later)");
                        System.out.println("1. Update Appointment");
                        System.out.println("2. Cancel Appointment");
                        System.out.println("3. Exit");
                        System.out.println("Enter your choice:");
                        int appointmentOptionsChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (appointmentOptionsChoice) {
                            case 1:
                                updateAppointment(appointment, id);
                                break;
                            case 2:
                                cancelAppointment(appointment, id);
                                appointmentOptionsExit=true;
                                break;
                            case 3:
                                System.out.println("Exiting appointment options.");
                                appointmentOptionsExit = true;
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a valid option.");
                                break;
                        }
                    }
                } else {
                    System.out.println("Failed to book the appointment. Please try again.");
                }
            } else {
                System.out.println("Invalid service number. Please select a valid service.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateAppointment(Appointment appointment,int id) {
        System.out.println("Update Appointment:"+id);
        System.out.println("Enter new date for the appointment (YYYY-MM-DD):");
        String newDateStr = scanner.next();
        LocalDate newDate = LocalDate.parse(newDateStr);
        System.out.println("Enter new time for the appointment (HH:MM:SS):");
        String newTimeStr = scanner.next();
        LocalTime newTime = LocalTime.parse(newTimeStr);
        Appointment updatedAppointment = new Appointment(id,appointment.getCustomerId(),appointment.getServiceId(),newDate,newTime);
        if (appointmentController.updateAppointment(updatedAppointment)) {
            System.out.println("Appointment updated successfully!");
        } else {
            System.out.println("Failed to update the appointment. Please try again.");
        }
    }
    private void cancelAppointment(Appointment appointment,int id) {
        System.out.println("Cancel Appointment:");
        System.out.println("Are you sure you want to cancel this appointment? (yes/no):");
        String confirmation = scanner.next().toLowerCase();
        if (confirmation.equals("yes")) {
            if (appointmentController.deleteAppointment(id)) {
                System.out.println("Appointment canceled successfully!");
            } else {
                System.out.println("Failed to cancel the appointment. Please try again.");
            }
        } else {
            System.out.println("Appointment cancellation aborted.");
        }
    }
}