package parlor;
import view.AdminView;
import dao.AppointmentDAO;
import dao.CustomerDAO;
import controller.BillController;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.InputMismatchException;
import view.CustomerView;
import controller.AppointmentController;
import controller.CustomerController;
import controller.ServiceController;
import controller.AdminController;
import dao.ServiceDAO;
import dao.AdminDAO;
public class Main {
   public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            ServiceDAO serviceDAO = new ServiceDAO(con);
            AppointmentDAO appointmentDAO=new AppointmentDAO(con);
            CustomerDAO customerDAO=new CustomerDAO(con);
            ServiceController serviceController = new ServiceController(serviceDAO);
            CustomerController customerController=new CustomerController(con);
            AppointmentController appointmentController=new AppointmentController(con);
            AdminDAO adminDAO=new AdminDAO(con);
            AdminController adminController=new AdminController(adminDAO);
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("-----------------Beauty Parlour Management---------------");
                System.out.println("1. Admin Login");
                System.out.println("2. Customer Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice:");
                int mainChoice = getIntegerInput(scanner);
                scanner.nextLine();
                switch (mainChoice) {
                    case 1:
                        System.out.println("--------------------Admin login selected.--------------------");
                        AdminView adminView = new AdminView(serviceController, customerController, appointmentController, adminController, scanner);
                        if (adminView.login()) {
                            System.out.println("Admin login successful. Welcome, Admin!");
                            adminView.displayMenu();
                        } else {
                            System.out.println("Admin login failed. Invalid credentials.");
                        }
                        break;

                    case 2:
                        CustomerView customerView = new CustomerView(customerController,serviceController,appointmentController,new BillController(con, serviceController,customerDAO,appointmentDAO),scanner);
                        customerView.displayMenu();
                        break;
                    case 3:
                        System.out.println("Exiting program.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        break;
                }
            }
            scanner.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
    private static int getIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }
}