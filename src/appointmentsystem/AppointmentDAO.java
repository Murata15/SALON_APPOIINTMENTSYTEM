package appointmentsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Method to add an appointment
    public void addAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (name, date, time, notes) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getName());
            stmt.setDate(2, new java.sql.Date(appointment.getDate().getTime()));
            stmt.setString(3, appointment.getTime());
            stmt.setString(4, appointment.getNotes());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setName(rs.getString("name"));
                appointment.setDate(rs.getDate("date"));
                appointment.setTime(rs.getString("time"));
                appointment.setNotes(rs.getString("notes"));
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    // Method to delete an appointment by ID
    public void deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted successfully.");
            } else {
                System.out.println("Appointment not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing appointment
    public void updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET name = ?, date = ?, time = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getName());
            stmt.setDate(2, new java.sql.Date(appointment.getDate().getTime()));
            stmt.setString(3, appointment.getTime());
            stmt.setString(4, appointment.getNotes());
            stmt.setInt(5, appointment.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully.");
            } else {
                System.out.println("Appointment not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search appointments by name or ID
  public List<Appointment> searchAppointments(String criteria) {
    List<Appointment> appointments = new ArrayList<>();
    String sql = "SELECT * FROM appointments WHERE name LIKE ? OR id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Search by name
        String searchName = "%" + criteria + "%";
        stmt.setString(1, searchName);
        
        // Try to parse criteria as an ID
        int id = -1;
        try {
            id = Integer.parseInt(criteria);
            stmt.setInt(2, id);
        } catch (NumberFormatException e) {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment();
            appointment.setId(rs.getInt("id"));
            appointment.setName(rs.getString("name"));
            appointment.setDate(rs.getDate("date"));
            appointment.setTime(rs.getString("time"));
            appointment.setNotes(rs.getString("notes"));
            appointments.add(appointment);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return appointments;
}

}
