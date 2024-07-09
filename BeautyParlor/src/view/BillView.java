package view;
import java.util.List;
import java.util.Scanner;
import controller.BillController;
import model.Bill;
public class BillView {
    private BillController billController;
    private Scanner scanner;
    public BillView(BillController billController, Scanner scanner) {
        this.billController = billController;
        this.scanner = scanner;
    }
    public void displayMenu(int custId) {
        try {
            boolean exit = false;
            while (!exit) {
                System.out.println("-----------------Bill Management---------------");
                System.out.println("1. View All My Bills");
                System.out.println("2. View Bill for Appointment ID");
                System.out.println("3. Exit");
                System.out.println("Enter your choice:");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        viewBillsForCustomer(custId);
                        break;
                    case 2:
                        viewBillForAppointmentId(custId);
                        break;
                    case 3:
                        System.out.println("Exiting Bill management.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewBillsForCustomer(int customerId) {
        List<Bill> bills = billController.getBillsForCustomer(customerId);
        if (bills != null && !bills.isEmpty()) {
            System.out.println("Bills for Customer ID " + customerId + ":");
            for (Bill bill : bills) {
                displayBillInfo(bill);
            }
        } else {
            System.out.println("You have not booked any appointments to generate bills");
        }
    }
    private void viewBillForAppointmentId(int custId) {
        System.out.println("Enter Appointment ID to view bill:");
        int appointmentId = scanner.nextInt();
        Bill bill = billController.getBillForAppointmentId(appointmentId,custId);
        if (bill != null) {
            System.out.println("Bill for Appointment ID " + appointmentId + ":");
            displayBillInfo(bill);
        } else {
            System.out.println("No bill found for Appointment ID " + appointmentId + " in your account.");
        }
    }
    private void displayBillInfo(Bill bill) {
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Customer ID: " + bill.getCustomerId());
        System.out.println("Email: " + bill.getEmail());
        System.out.println("Appointment ID: " + bill.getAppointmentId());
        System.out.println("Total Amount: " + bill.getTotalAmount());
        System.out.println("Discount: " + bill.getDiscount());
        System.out.println("Amount to Pay: " + bill.getAmountToPay());
        System.out.println("---------------------------");
    }
}