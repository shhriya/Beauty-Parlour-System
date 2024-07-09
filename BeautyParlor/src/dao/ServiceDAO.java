package dao;
import model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ServiceDAO {
	private Connection connection;
    public ServiceDAO(Connection connection) {
        this.connection = connection;
    }
    public void addService(Service service) throws SQLException {
        String sql = "INSERT INTO Service (name, description, price) VALUES ( ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setDouble(3, service.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Service service = new Service(resultSet.getInt("serviceId"),resultSet.getString("name"),resultSet.getString("description"),resultSet.getDouble("price"));
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return services;
    }
    public void updateService(Service service) throws SQLException {
        String sql = "UPDATE Service SET name = ?, description = ?, price = ? WHERE serviceId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setDouble(3, service.getPrice());
            preparedStatement.setInt(4, service.getServiceId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void deleteService(String serviceId) throws SQLException {
        String sql = "DELETE FROM Service WHERE serviceId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, serviceId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean serviceExists(String serviceId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Service WHERE serviceId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, serviceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public Service getServiceById(int serviceId) throws SQLException {
        String sql = "SELECT * FROM Service WHERE serviceId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, serviceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Service(resultSet.getInt("serviceId"),resultSet.getString("name"),resultSet.getString("description"),resultSet.getDouble("price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
}