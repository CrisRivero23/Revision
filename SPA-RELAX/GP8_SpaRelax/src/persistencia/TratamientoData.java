package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Tratamiento;

public class TratamientoData {
    
    private Connection con;

    public TratamientoData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Inserta un nuevo tratamiento
     */
    public void insertar(Tratamiento tratamiento) throws SQLException {
        String sql = "INSERT INTO tratamiento (nombre, tipo, duracion_min, costo, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tratamiento.getNombre());
            ps.setString(2, tratamiento.getTipo());
            ps.setInt(3, tratamiento.getDuracionMin());
            ps.setDouble(4, tratamiento.getCosto());
            ps.setString(5, tratamiento.isEstado() ? "activo" : "inactivo");
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                tratamiento.setIdTratamiento(rs.getInt(1));
            }
        }
    }

    /**
     * Actualiza un tratamiento existente
     */
    public void actualizar(Tratamiento tratamiento) throws SQLException {
        String sql = "UPDATE tratamiento SET nombre=?, tipo=?, duracion_min=?, costo=?, estado=? WHERE id_tratamiento=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tratamiento.getNombre());
            ps.setString(2, tratamiento.getTipo());
            ps.setInt(3, tratamiento.getDuracionMin());
            ps.setDouble(4, tratamiento.getCosto());
            ps.setString(5, tratamiento.isEstado() ? "activo" : "inactivo");
            ps.setInt(6, tratamiento.getIdTratamiento());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente un tratamiento
     */
    public void eliminar(int idTratamiento) throws SQLException {
        String sql = "DELETE FROM tratamiento WHERE id_tratamiento=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTratamiento);
            ps.executeUpdate();
        }
    }

    /**
     * Baja lógica de un tratamiento
     */
    public void bajaLogica(int idTratamiento) throws SQLException {
        String sql = "UPDATE tratamiento SET estado='inactivo' WHERE id_tratamiento=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTratamiento);
            ps.executeUpdate();
        }
    }

    /**
     * Alta lógica de un tratamiento
     */
    public void altaLogica(int idTratamiento) throws SQLException {
        String sql = "UPDATE tratamiento SET estado='activo' WHERE id_tratamiento=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTratamiento);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un tratamiento por ID
     */
    public Tratamiento buscarPorId(int idTratamiento) throws SQLException {
        String sql = "SELECT * FROM tratamiento WHERE id_tratamiento=?";
        Tratamiento tratamiento = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTratamiento);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                tratamiento = new Tratamiento();
                tratamiento.setIdTratamiento(rs.getInt("id_tratamiento"));
                tratamiento.setNombre(rs.getString("nombre"));
                tratamiento.setTipo(rs.getString("tipo"));
                tratamiento.setDuracionMin(rs.getInt("duracion_min"));
                tratamiento.setCosto(rs.getDouble("costo"));
                tratamiento.setEstado(rs.getString("estado").equals("activo"));
            }
        }
        
        return tratamiento;
    }

    /**
     * Busca tratamientos por tipo
     */
    public List<Tratamiento> buscarPorTipo(String tipo) throws SQLException {
        String sql = "SELECT * FROM tratamiento WHERE tipo LIKE ? AND estado='activo'";
        List<Tratamiento> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tipo + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Tratamiento t = new Tratamiento();
                t.setIdTratamiento(rs.getInt("id_tratamiento"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(rs.getString("tipo"));
                t.setDuracionMin(rs.getInt("duracion_min"));
                t.setCosto(rs.getDouble("costo"));
                t.setEstado(rs.getString("estado").equals("activo"));
                lista.add(t);
            }
        }
        
        return lista;
    }

    /**
     * Lista todos los tratamientos
     */
    public List<Tratamiento> listarTodos() {
        String sql = "SELECT * FROM tratamiento ORDER BY nombre";
        List<Tratamiento> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Tratamiento t = new Tratamiento();
                t.setIdTratamiento(rs.getInt("id_tratamiento"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(rs.getString("tipo"));
                t.setDuracionMin(rs.getInt("duracion_min"));
                t.setCosto(rs.getDouble("costo"));
                t.setEstado(rs.getString("estado").equals("activo"));
                lista.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar tratamientos: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo tratamientos activos
     */
    public List<Tratamiento> listarActivos() {
        String sql = "SELECT * FROM tratamiento WHERE estado='activo' ORDER BY nombre";
        List<Tratamiento> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Tratamiento t = new Tratamiento();
                t.setIdTratamiento(rs.getInt("id_tratamiento"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(rs.getString("tipo"));
                t.setDuracionMin(rs.getInt("duracion_min"));
                t.setCosto(rs.getDouble("costo"));
                t.setEstado(true);
                lista.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar tratamientos activos: " + ex.getMessage());
        }
        
        return lista;
    }
}