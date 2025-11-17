package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Masajista;

public class MasajistaData {
    
    private Connection con;

    public MasajistaData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Inserta un nuevo masajista en la base de datos
     */
    public void insertar(Masajista masajista) throws SQLException {
        String sql = "INSERT INTO masajista (nombre, matricula, telefono, especialidad, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, masajista.getNombre());
            ps.setString(2, masajista.getMatricula());
            ps.setString(3, masajista.getTelefono());
            ps.setString(4, masajista.getEspecialidad());
            ps.setBoolean(5, masajista.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                masajista.setIdMasajista(rs.getInt(1));
            }
        }
    }

    /**
     * Actualiza los datos de un masajista
     */
    public void actualizar(Masajista masajista) throws SQLException {
        String sql = "UPDATE masajista SET nombre=?, matricula=?, telefono=?, especialidad=?, estado=? WHERE idMasajista=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, masajista.getNombre());
            ps.setString(2, masajista.getMatricula());
            ps.setString(3, masajista.getTelefono());
            ps.setString(4, masajista.getEspecialidad());
            ps.setBoolean(5, masajista.isEstado());
            ps.setInt(6, masajista.getIdMasajista());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente un masajista
     */
    public void eliminar(int idMasajista) throws SQLException {
        String sql = "DELETE FROM masajista WHERE idMasajista=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMasajista);
            ps.executeUpdate();
        }
    }

    /**
     * Baja lógica de un masajista
     */
    public void bajaLogica(int idMasajista) throws SQLException {
        String sql = "UPDATE masajista SET estado=0 WHERE idMasajista=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMasajista);
            ps.executeUpdate();
        }
    }

    /**
     * Alta lógica de un masajista
     */
    public void altaLogica(int idMasajista) throws SQLException {
        String sql = "UPDATE masajista SET estado=1 WHERE idMasajista=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMasajista);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un masajista por ID
     */
    public Masajista buscarPorId(int idMasajista) throws SQLException {
        String sql = "SELECT * FROM masajista WHERE idMasajista=?";
        Masajista masajista = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMasajista);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                masajista = new Masajista();
                masajista.setIdMasajista(rs.getInt("idMasajista"));
                masajista.setNombre(rs.getString("nombre"));
                masajista.setMatricula(rs.getString("matricula"));
                masajista.setTelefono(rs.getString("telefono"));
                masajista.setEspecialidad(rs.getString("especialidad"));
                masajista.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return masajista;
    }

    /**
     * Busca masajistas por especialidad
     */
    public List<Masajista> buscarPorEspecialidad(String especialidad) throws SQLException {
        String sql = "SELECT * FROM masajista WHERE especialidad LIKE ? AND estado=1";
        List<Masajista> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + especialidad + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Masajista m = new Masajista();
                m.setIdMasajista(rs.getInt("idMasajista"));
                m.setNombre(rs.getString("nombre"));
                m.setMatricula(rs.getString("matricula"));
                m.setTelefono(rs.getString("telefono"));
                m.setEspecialidad(rs.getString("especialidad"));
                m.setEstado(rs.getBoolean("estado"));
                lista.add(m);
            }
        }
        
        return lista;
    }

    /**
     * Lista todos los masajistas
     */
    public List<Masajista> listarTodos() {
        String sql = "SELECT * FROM masajista ORDER BY nombre";
        List<Masajista> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Masajista m = new Masajista();
                m.setIdMasajista(rs.getInt("idMasajista"));
                m.setNombre(rs.getString("nombre"));
                m.setMatricula(rs.getString("matricula"));
                m.setTelefono(rs.getString("telefono"));
                m.setEspecialidad(rs.getString("especialidad"));
                m.setEstado(rs.getBoolean("estado"));
                lista.add(m);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar masajistas: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo masajistas activos
     */
    public List<Masajista> listarActivos() {
        String sql = "SELECT * FROM masajista WHERE estado=1 ORDER BY nombre";
        List<Masajista> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Masajista m = new Masajista();
                m.setIdMasajista(rs.getInt("idMasajista"));
                m.setNombre(rs.getString("nombre"));
                m.setMatricula(rs.getString("matricula"));
                m.setTelefono(rs.getString("telefono"));
                m.setEspecialidad(rs.getString("especialidad"));
                m.setEstado(rs.getBoolean("estado"));
                lista.add(m);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar masajistas activos: " + ex.getMessage());
        }
        
        return lista;
    }
}