package view;
import controller.AdminController;
import controller.AppointmentController;
import controller.CustomerController;
import controller.ServiceController;
import model.Appointment;
import model.Customer;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class AdminView {
	private ServiceController serviceController;
    private CustomerController customerController;
    private AppointmentController appointmentController;
    private AdminController adminController;
    private Scanner scanner;
    public AdminView(ServiceController serviceController, CustomerController customerController, AppointmentController appointmentController, AdminController adminController, Scanner scanner) {
        this.serviceController = serviceController;
        this.customerController = customerController;
        this.appointmentController = appointmentController;
        this.adminController = adminController;
        this.scanner = scanner;
    }
    public boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        boolean isAuthenticated = adminController.authenticateAdmin(username, password);
        return isAuthenticated;
    }

    public void displayMenu() {
        boolean adminMenuExit = false;
        while (!adminMenuExit) {
            System.out.println("--------------------Admin Menu:--------------------");
            System.out.println("1. Service Page");
            System.out.println("2. Customer Page");
            System.out.println("3. Appointment Page");
            System.out.println("4. Logout");
            System.out.println("Enter your choice:");
            int adminMenuChoice = scanner.nextInt();
            switch (adminMenuChoice) {
                case 1:
                    ServiceView serviceView = new ServiceView(serviceController, scanner);
                    serviceView.displayMenu();
                    break;
                case 2:
                    List<Customer> customers = customerController.getAllCustomers();
                    if (customers != null && !customers.isEmpty()) {
                        System.out.println("Customer Data:");
                        for (Customer customer : customers) {
                            System.out.println("Customer ID: " + customer.getCustomerId());
                            System.out.println("Name: " + customer.getName());
                            System.out.println("Phone Number: " + customer.getPhoneNumber());
                            System.out.println("Email: " + customer.getEmail());
                            System.out.println("Address: " + customer.getAddress());
                            System.out.println("---------------------------");
                        }
                    } else {
                        System.out.println("No customer data available.");
                    }
                    break;
                case 3:
                    handleAppointmentMenu();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    adminMenuExit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }
    private void handleAppointmentMenu() {
        boolean appointmentMenuExit = false;
        while (!appointmentMenuExit) {
            System.out.println("Appointment Menu:");
            System.out.println("1. View Finished Appointments");
            System.out.println("2. View Upcoming Appointments");
            System.out.println("3. View All Appointments");
            System.out.println("4. View Today's Appointments");
            System.out.println("5. Go Back to Admin Menu");
            System.out.println("Enter your choice:");
            int appointmentMenuChoice = getIntegerInput(scanner);
            scanner.nextLine();
            switch (appointmentMenuChoice) {
                case 1:
                    List<Appointment> finishedAppointments = appointmentController.getFinishedAppointments();
                    displayAppointments("Finished", finishedAppointments);
                    break;
                case 2:
                    List<Appointment> incomingAppointments = appointmentController.getIncomingAppointments();
                    displayAppointments("Incoming", incomingAppointments);
                    break;
                case 3:
                    List<Appointment> allAppointments = appointmentController.getAllAppointments();
                    displayAppointments("All", allAppointments);
                    break;
                case 4:
                    List<Appointment> todayAppointments = appointmentController.getTodayAppointments();
                    displayAppointments("Today's", todayAppointments);
                    break;
                case 5:
                    System.out.println("Going back to Admin Menu...");
                    appointmentMenuExit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }
    private void displayAppointments(String type, List<Appointment> appointments) {
    	if (appointments != null && !appointments.isEmpty()) {
          System.out.println(type + " Appointment Data--------------------------");
          for (Appointment appointment : appointments) {
              System.out.println("Appointment ID: " + appointment.getAppointmentId());
              System.out.println("Customer ID: " + appointment.getCustomerId());
              System.out.println("Service ID: " + appointment.getServiceId());
              System.out.println("Date: " + appointment.getDate());
              System.out.println("Time: " + appointment.getTime());
              System.out.println("---------------------------");
          }
      } else {
          System.out.println("No " + type.toLowerCase() + " appointment data available.");
      }
    }
    private int getIntegerInput(Scanner scanner) {
        while (true) {
            try {
                if (scanner.hasNextInt()) {
                    return scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }


}