package controller;
import dao.AdminDAO;
import java.sql.SQLException;
public class AdminController {
    private AdminDAO adminDAO;
    public AdminController(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }
    public boolean authenticateAdmin(String username, String password) {
        try {
            return adminDAO.authenticateAdmin(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
