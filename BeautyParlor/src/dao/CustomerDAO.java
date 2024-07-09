package dao;
//import model.Customer;
import java.util.*;
import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import parlor.DBConnection;
public class CustomerDAO {
    private Connection connection;
    public CustomerDAO(Connection connection2) {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void register(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (name, phoneNumber, email, address,password) VALUES ( ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhoneNumber());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getPass());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean login(String email, String pass) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public Customer getCustomerByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int customerId = resultSet.getInt("customerId");
                    String name = resultSet.getString("name");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    String customerEmail = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String pass=resultSet.getString("password");
                    return new Customer(customerId, name, phoneNumber, customerEmail, address,pass);
                } else {
                    return null; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET name = ?, phoneNumber = ?, email = ?, address = ? WHERE customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhoneNumber());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setInt(5, customer.getCustomerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customerId");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");
                Customer customer = new Customer(customerId, name, phoneNumber, email, address, password);
                customers.add(customer);
            }
        }
        return customers;
    }
    public Customer getCustomerById(int customerId) {
        try {
            String query = "SELECT * FROM Customer WHERE customerId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createCustomerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Customer createCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customerId"));
        customer.setName(resultSet.getString("name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhoneNumber(resultSet.getString("phoneNumber"));
        customer.setAddress(resultSet.getString("address"));
        return customer;
    }
}
