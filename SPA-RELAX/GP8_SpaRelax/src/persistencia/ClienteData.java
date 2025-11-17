package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

public class ClienteData {
    
    private Connection con;

    public ClienteData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Agrega un nuevo cliente a la base de datos
     */
    public void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, apellido, dni, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setBoolean(6, cliente.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cliente.setIdCliente(rs.getInt(1));
            }
        }
    }

    /**
     * Modifica los datos de un cliente existente
     */
    public void modificarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre=?, apellido=?, dni=?, telefono=?, correo=?, estado=? WHERE idCliente=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setBoolean(6, cliente.isEstado());
            ps.setInt(7, cliente.getIdCliente());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente un cliente de la base de datos
     */
    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE idCliente=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }

    /**
     * Realiza una baja lógica del cliente (cambia estado a false)
     */
    public void bajaLogica(int idCliente) throws SQLException {
        String sql = "UPDATE cliente SET estado=0 WHERE idCliente=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }

    /**
     * Realiza un alta lógica del cliente (cambia estado a true)
     */
    public void altaLogica(int idCliente) throws SQLException {
        String sql = "UPDATE cliente SET estado=1 WHERE idCliente=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un cliente por su ID
     */
    public Cliente buscarClientePorId(int idCliente) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE idCliente=?";
        Cliente cliente = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getInt("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return cliente;
    }

    /**
     * Busca un cliente por su DNI
     */
    public Cliente buscarClientePorDni(int dni) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE dni=?";
        Cliente cliente = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dni);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getInt("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return cliente;
    }

    /**
     * Lista todos los clientes (activos e inactivos)
     */
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM cliente ORDER BY apellido, nombre";
        List<Cliente> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setDni(rs.getInt("dni"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar clientes: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo los clientes activos
     */
    public List<Cliente> listarActivos() {
        String sql = "SELECT * FROM cliente WHERE estado=1 ORDER BY apellido, nombre";
        List<Cliente> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setDni(rs.getInt("dni"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar clientes activos: " + ex.getMessage());
        }
        
        return lista;
    }
}