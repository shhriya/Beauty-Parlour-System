package view;
import controller.ServiceController;
import model.Service;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class ServiceView {
    private ServiceController serviceController;
    private Scanner scanner;
    public ServiceView(ServiceController serviceController, Scanner scanner) {
        this.serviceController = serviceController;
        this.scanner = scanner;
    }
    public void displayMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("-----------------Service Management---------------");
            System.out.println("1. Add Service");
            System.out.println("2. View Services");
            System.out.println("3. Update Service");
            System.out.println("4. Delete Service");
            System.out.println("5. Exit");
            System.out.println("Enter your choice:");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addService();
                        break;
                    case 2:
                        viewServices();
                        break;
                    case 3:
                        updateService();
                        break;
                    case 4:
                        deleteService();
                        break;
                    case 5:
                        System.out.println("Exiting service management.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }
    private void addService() {
        try {
            System.out.println("Enter service details:");
            int serviceId = 0;
            System.out.println("Service Name:");
            String name = scanner.nextLine();
            System.out.println("Service Description:");
            String description = scanner.nextLine();
            System.out.println("Service Price:");
            double price = scanner.nextDouble();
            scanner.nextLine();
            serviceController.addService(serviceId, name, description, price);
            System.out.println("Service added successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        }
    }
    private void viewServices() {
        List<Service> services = serviceController.getAllServices();
        System.out.println("List of Services:--------------------");
        for (Service service : services) {
            System.out.println("Service ID: " + service.getServiceId());
            System.out.println("Service Name: " + service.getName());
            System.out.println("Service Description: " + service.getDescription());
            System.out.println("Service Price: " + service.getPrice());
            System.out.println("------------------------------");
        }
    }
    private void updateService() {
        try {
            System.out.println("Enter the Service ID to update:");
            int serviceId = scanner.nextInt();
            scanner.nextLine();
            String ser = String.valueOf(serviceId);
            if (serviceController.serviceExists(ser)) {
                System.out.println("Enter updated details for the service:");
                System.out.println("Updated Service Name:");
                String name = scanner.nextLine();
                System.out.println("Updated Service Description:");
                String description = scanner.nextLine();
                System.out.println("Updated Service Price:");
                double price = scanner.nextDouble();
                scanner.nextLine();
                serviceController.updateService(serviceId, name, description, price);
                System.out.println("Service updated successfully!");
            } else {
                System.out.println("Service with ID " + serviceId + " does not exist.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        }
    }
    private void deleteService() {
        try {
            System.out.println("Enter the Service ID to delete:");
            int serviceId = scanner.nextInt();
            scanner.nextLine();
            String ser = String.valueOf(serviceId);
            if (serviceController.serviceExists(ser)) {
                serviceController.deleteService(ser);
                System.out.println("Service deleted successfully!");
            } else {
                System.out.println("Service with ID " + serviceId + " does not exist.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
        }
    }
}