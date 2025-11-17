package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/spa_reality";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    
    private Conexion() {}
    
    public static Connection getConexion() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("✅ Conexión establecida correctamente con la base de datos.");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar el driver JDBC: " + ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage());
            }
        }
        return connection;
    }
    
    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("🔒 Conexión cerrada correctamente.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}