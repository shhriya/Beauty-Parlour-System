package dao;
//import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
public class AppointmentDAO {
    private Connection connection;
    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Appointment> getFinishedAppointments() {
        List<Appointment> finishedAppointments = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Appointments")) {
            while (resultSet.next()) {
                Appointment appointment = extractAppointmentFromResultSet(resultSet);
                if (appointment.isFinished()) {
                    finishedAppointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finishedAppointments;
    }
    public List<Appointment> getIncomingAppointments() {
        List<Appointment> incomingAppointments = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Appointments")) {
            while (resultSet.next()) {
                Appointment appointment = extractAppointmentFromResultSet(resultSet);
                if (appointment.isIncoming()) {
                    incomingAppointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomingAppointments;
    }
    private Appointment extractAppointmentFromResultSet(ResultSet resultSet) throws SQLException {
        int appointmentId = resultSet.getInt("appointmentId");
        int customerId = resultSet.getInt("customerId");
        int serviceId = resultSet.getInt("serviceId");
        LocalDate date = null;
        LocalTime time = null;
        if (resultSet.getObject("date") != null) {
            date = resultSet.getDate("date").toLocalDate();
        }
        if (resultSet.getObject("time") != null) {
            time = resultSet.getTime("time").toLocalTime();
        }
        return new Appointment(appointmentId,customerId, serviceId, date, time);
    }
    public int addAppointment(Appointment appointment, int customerId) {
        try {
            String checkConflictQuery = "SELECT COUNT(*) FROM Appointments WHERE serviceId = ? AND date = ? AND time = ?";
            try (PreparedStatement checkConflictStatement = connection.prepareStatement(checkConflictQuery)) {
                checkConflictStatement.setInt(1, appointment.getServiceId());
                checkConflictStatement.setDate(2, java.sql.Date.valueOf(appointment.getDate()));
                checkConflictStatement.setTime(3, java.sql.Time.valueOf(appointment.getTime()));
                try (ResultSet resultSet = checkConflictStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        System.out.println("Appointment conflict. Another appointment already exists at the same time.");
                        return 0;
                    }
                }
            }
            LocalTime startTime = LocalTime.of(8, 0);  // 08:00:00
            LocalTime endTime = LocalTime.of(18, 0);   // 18:00:00

            if (appointment.getTime().isBefore(startTime) || appointment.getTime().isAfter(endTime)) {
                System.out.println("Failed to book , Appointment time should be between 08:00:00 and 18:00:00.");
                return 0;
            }
            if (appointment.getDate().isBefore(LocalDate.now()) ||(appointment.getDate().isEqual(LocalDate.now()) && appointment.getTime().isBefore(java.time.LocalTime.now()))) {
                System.out.println("Cannot insert appointment with a past date and time.");
                return 0;
            }
            String insertQuery = "INSERT INTO Appointments (customerId, serviceId, date, time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.setInt(2, appointment.getServiceId());
                preparedStatement.setDate(3, java.sql.Date.valueOf(appointment.getDate()));
                preparedStatement.setTime(4, java.sql.Time.valueOf(appointment.getTime()));
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating appointment failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Appointment> getAppointmentById(int appointmentId) {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String query = "SELECT * FROM Appointments WHERE appointmentId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, appointmentId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        appointments.add(extractAppointmentFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String query = "SELECT * FROM Appointments";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    LocalTime time = resultSet.getTime("time").toLocalTime();
                    if (date != null) {
                        appointments.add(new Appointment(resultSet.getInt("appointmentId"),resultSet.getInt("customerId"),resultSet.getInt("serviceId"),date,time));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public void updateAppointment(Appointment appointment) {
        try {
            connection.setAutoCommit(false);
            String query = "UPDATE Appointments SET customerId = ?, serviceId = ?, date = ?, time = ? WHERE appointmentId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, appointment.getCustomerId());
                preparedStatement.setInt(2, appointment.getServiceId());
                preparedStatement.setDate(3, java.sql.Date.valueOf(appointment.getDate()));
                preparedStatement.setTime(4, java.sql.Time.valueOf(appointment.getTime()));
                preparedStatement.setInt(5, appointment.getAppointmentId());
                //int rowsAffected = preparedStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteAppointment(int appointmentId) {
        try {
            connection.setAutoCommit(false);
            String query = "DELETE FROM Appointments WHERE appointmentId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, appointmentId);
                preparedStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String query = "SELECT * FROM Appointments WHERE date = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(date));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                    	LocalDate date1 = resultSet.getDate("date").toLocalDate();
                        LocalTime time = resultSet.getTime("time").toLocalTime();
                        appointments.add(new Appointment(resultSet.getInt("appointmentId"),resultSet.getInt("customerId"),resultSet.getInt("serviceId"),date1,time));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public void deleteAppointmentsByCustomerId(int customerId) throws SQLException {
        String sql = "DELETE FROM Appointments WHERE customerId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}