package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import model.Appointment;
import model.Bill;
public class BillDAO {
    private Connection connection;
    public BillDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Bill> getBillsForCustomer(int customerId) {
        List<Bill> bills = new ArrayList<>();
        try {
            String query = "SELECT * FROM Bill WHERE customerId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Bill bill = createBillFromResultSet(resultSet);
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
    public Bill getBillForAppointmentId(int appointmentId,int customerId) {
        try {
            String query = "SELECT * FROM Bill WHERE appointmentId = ? AND customerId=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.setInt(2, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createBillFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Bill createBillFromResultSet(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(resultSet.getInt("billId"));
        bill.setCustomerId(resultSet.getInt("customerId"));
        bill.setEmail(resultSet.getString("email"));
        bill.setAppointmentId(resultSet.getInt("appointmentId"));
        bill.setTotalAmount(resultSet.getDouble("totalAmount"));
        bill.setDiscount(resultSet.getDouble("discount"));
        bill.setAmountToPay(resultSet.getDouble("amountToPay"));
        return bill;
    }
    public int createBill(Bill bill) {
        try {
            String query = "INSERT INTO Bill (customerId, email, appointmentId, totalAmount, discount, amountToPay) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, bill.getCustomerId());
                preparedStatement.setString(2, bill.getEmail());
                preparedStatement.setInt(3, bill.getAppointmentId());
                preparedStatement.setDouble(4, bill.getTotalAmount());
                preparedStatement.setDouble(5, bill.getDiscount());
                preparedStatement.setDouble(6, bill.getAmountToPay());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                } else {
                    System.out.println("Creating bill failed, no rows affected.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return -1;
    }
}
