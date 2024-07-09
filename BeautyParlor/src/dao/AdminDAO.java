package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AdminDAO {
    private Connection connection;
    public AdminDAO(Connection connection) {
        this.connection = connection;
    }
    public boolean authenticateAdmin(String username, String password) throws SQLException {
        String query = "SELECT * FROM Admin WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}