package view;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import controller.AppointmentController;
import controller.BillController;
import controller.CustomerController;
import controller.ServiceController;
import model.Customer;
import model.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;
import model.Appointment;
public class CustomerView {
    private CustomerController customerController;
    private ServiceController serviceController;
    private AppointmentController appointmentController;
    private Scanner scanner;
    private BillController billController;
    private Customer loggedInCustomer;
    boolean exit = false;
    boolean exitAfterDelete=false;
    public CustomerView(CustomerController customerController, ServiceController serviceController, AppointmentController appointmentController, BillController billController, Scanner scanner) {
        this.customerController = customerController;
        this.serviceController = serviceController;
        this.appointmentController = appointmentController;
        this.billController=billController;
        this.scanner = scanner;
    }
    public void displayMenu() throws SQLException, ParseException,InputMismatchException {
        while (!exit) {
            System.out.println("-----------------Customer Management---------------");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                	System.out.println("Exiting...");
                	exit=true;
                	break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
            if (!exit) {
                exit = false;
            }
        }
    }
    private void login() {
        try {
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();
            if (customerController.login(email, password)) {
                loggedInCustomer = customerController.getCustomerByEmail(email);
                System.out.println("Login successful. Welcome, " + email + "!");
                boolean exit = false;
                while (!exit) {
                	if(exitAfterDelete) {
                		exit=true;
                		break;
                	}
                    System.out.println("1. Edit Details");
                    System.out.println("2. View Services");
                    System.out.println("3. Book Appointments");
                    System.out.println("4. View Already Booked Timings By Others");
                    System.out.println("5. View My Appointments");
                    System.out.println("6. Generate Bill");
                    System.out.println("7. Exit");
                    System.out.println("Enter your choice:");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            editDetails(email);
                            break;
                        case 2:
                            viewServices();
                            break;
                        case 3:
                            bookAppointments();
                            break;
                        case 4:
                            viewAllBookedFutureAppointments();
                            break;
                        case 5:
                            viewMyAppointments();
                            break;
                        case 6:
                        	viewBills();
                        	break;
                        case 7:
                            System.out.println("Exiting Customer management.");
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                            break;
                    }
                }
            }
            else {
            	System.out.println("Enter valid email id and password");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void viewMyAppointments() {
        List<Appointment> myAppointments = appointmentController.getAppointmentById(loggedInCustomer.getCustomerId());
        if (myAppointments != null) {
            if (!myAppointments.isEmpty()) {
                System.out.println("Your Appointments:");
                for (Appointment appointment : myAppointments) {
                    System.out.println("Appointment ID: " + appointment.getAppointmentId());
                    System.out.println("Service ID: " + appointment.getServiceId());
                    System.out.println("Date: " + appointment.getDate());
                    System.out.println("Time: " + appointment.getTime());
                    System.out.println("---------------------------");
                }
            } else {
                System.out.println("You have no appointments booked.");
            }
        } else {
            System.out.println("Error: Unable to fetch your appointments. Please try again later.");
        }
    }
    private void viewAllBookedFutureAppointments() {
        List<Appointment> allAppointments = appointmentController.getAllAppointments();
        List<Appointment> futureAppointments = allAppointments.stream()
                .filter(appointment -> isFutureAppointment(appointment.getDate(), appointment.getTime()))
                .collect(Collectors.toList());
        displayAppointments("All", futureAppointments);
    }
    private boolean isFutureAppointment(LocalDate date, LocalTime time) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
        LocalDateTime currentDateTime = LocalDateTime.now();
        return appointmentDateTime.isAfter(currentDateTime);
    }
    private void displayAppointments(String type, List<Appointment> appointments) {
        if (appointments != null && !appointments.isEmpty()) {
            System.out.println(type + " Appointment Data:");
            for (Appointment appointment : appointments) {
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time: " + appointment.getTime());
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("No " + type.toLowerCase() + " appointment data available.");
        }
    }
    private void register() throws SQLException, ParseException {
    	int custId=0;
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter address:");
        String address = scanner.nextLine();
        System.out.println("Enter pass:");
        String pass = scanner.nextLine();
        if (customerController.register(new Customer(custId,name, phoneNumber, email, address, pass))) {
            System.out.println("Registration successful. You can now login.");
            login();
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
    private void editDetails(String email) {
    Customer customer = customerController.getCustomerByEmail(email);
      if (customer != null) {
          System.out.println("Current Details:");
          System.out.println("Name: " + customer.getName());
          System.out.println("Phone Number: " + customer.getPhoneNumber());
          System.out.println("Email: " + customer.getEmail());
          System.out.println("Address: " + customer.getAddress());
          System.out.println("What details do you want to update?");
          System.out.println("1. Name");
          System.out.println("2. Phone Number");
          System.out.println("3. Address");
          System.out.println("4. Delete Account");
          System.out.println("5. Cancel");
          int choice = scanner.nextInt();
          scanner.nextLine();
          switch (choice) {
              case 1:
                  System.out.println("Enter new name:");
                  String newName = scanner.nextLine();
                  customer.setName(newName);
                  break;
              case 2:
                  System.out.println("Enter new phone number:");
                  String newPhoneNumber = scanner.nextLine();
                  customer.setPhoneNumber(newPhoneNumber);
                  break;
              case 3:
                  System.out.println("Enter new address:");
                  String newAddress = scanner.nextLine();
                  customer.setAddress(newAddress);
                  break;
              case 4:
            	  System.out.println("Are you sure you want to delete your account? (yes/no)");
                  String confirmDelete = scanner.nextLine().toLowerCase();
                  if (confirmDelete.equals("yes")) {
                      if (customerController.deleteCustomer(customer)) {
                          System.out.println("Account deleted successfully!");
                          exit=true;
                          exitAfterDelete=true;
                          return;
                      } else {
                          System.out.println("Failed to delete account. Please try again.");
                      }
                  } else {
                      System.out.println("Account deletion cancelled.");
                  }
                  break;
              case 5:
                  System.out.println("Cancelled. Returning to the main menu.");
                  return;
              default:
                  System.out.println("Invalid choice. Returning to the main menu.");
                  return;
          }
          if (customerController.updateCustomer(customer)) {
              System.out.println("Details updated successfully!");
          } else {
              System.out.println("Failed to update details. Please try again.");
          }
      } else {
          System.out.println("Customer with email " + email + " does not exist.");
      }
  }
    private void viewServices() {
        List<Service> availableServices = serviceController.getAvailableServices();
        if (availableServices != null && !availableServices.isEmpty()) {
            System.out.println("Available Services:");
            for (Service service : availableServices) {
                System.out.println(serviceController.getFormattedServiceInfo(service));
            }
        } else {
            System.out.println("No services available at the moment.");
        }
    }
    private void bookAppointments() throws SQLException, ParseException {
    	if (loggedInCustomer == null) {
            System.out.println("Please login first to book appointments.");
            return;
        }
    	if (loggedInCustomer.getCustomerId() != 0) {
            AppointmentView appointmentView = new AppointmentView(loggedInCustomer.getCustomerId(),appointmentController,customerController,serviceController, billController);
            appointmentView.addAppointment(loggedInCustomer.getCustomerId());
        } else {
            System.out.println("Error: Customer ID is null for the logged-in customer.");
        }
    }
    private void viewBills() {
        try {
            BillView billView = new BillView(billController, scanner);
            billView.displayMenu(loggedInCustomer.getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}