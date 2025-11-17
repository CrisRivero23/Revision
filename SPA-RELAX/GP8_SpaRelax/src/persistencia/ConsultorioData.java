package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Consultorio;

public class ConsultorioData {
    
    private Connection con;

    public ConsultorioData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Inserta un nuevo consultorio
     */
    public void insertar(Consultorio consultorio) throws SQLException {
        String sql = "INSERT INTO consultorio (usos, equipamiento, apto, estado) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, consultorio.getUsos());
            ps.setString(2, consultorio.getEquipamiento());
            ps.setBoolean(3, consultorio.isApto());
            ps.setBoolean(4, consultorio.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                consultorio.setIdConsultorio(rs.getInt(1));
            }
        }
    }

    /**
     * Actualiza un consultorio existente
     */
    public void actualizar(Consultorio consultorio) throws SQLException {
        String sql = "UPDATE consultorio SET usos=?, equipamiento=?, apto=?, estado=? WHERE idConsultorio=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, consultorio.getUsos());
            ps.setString(2, consultorio.getEquipamiento());
            ps.setBoolean(3, consultorio.isApto());
            ps.setBoolean(4, consultorio.isEstado());
            ps.setInt(5, consultorio.getIdConsultorio());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente un consultorio
     */
    public void eliminar(int idConsultorio) throws SQLException {
        String sql = "DELETE FROM consultorio WHERE idConsultorio=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ps.executeUpdate();
        }
    }

    /**
     * Baja lógica de un consultorio
     */
    public void bajaLogica(int idConsultorio) throws SQLException {
        String sql = "UPDATE consultorio SET estado=0 WHERE idConsultorio=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ps.executeUpdate();
        }
    }

    /**
     * Alta lógica de un consultorio
     */
    public void altaLogica(int idConsultorio) throws SQLException {
        String sql = "UPDATE consultorio SET estado=1 WHERE idConsultorio=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un consultorio por ID
     */
    public Consultorio buscarPorId(int idConsultorio) throws SQLException {
        String sql = "SELECT * FROM consultorio WHERE idConsultorio=?";
        Consultorio consultorio = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                consultorio = new Consultorio();
                consultorio.setIdConsultorio(rs.getInt("idConsultorio"));
                consultorio.setUsos(rs.getString("usos"));
                consultorio.setEquipamiento(rs.getString("equipamiento"));
                consultorio.setApto(rs.getBoolean("apto"));
                consultorio.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return consultorio;
    }

    /**
     * Busca consultorios por uso
     */
    public List<Consultorio> buscarPorUso(String uso) throws SQLException {
        String sql = "SELECT * FROM consultorio WHERE usos LIKE ? AND estado=1 AND apto=1";
        List<Consultorio> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + uso + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Consultorio c = new Consultorio();
                c.setIdConsultorio(rs.getInt("idConsultorio"));
                c.setUsos(rs.getString("usos"));
                c.setEquipamiento(rs.getString("equipamiento"));
                c.setApto(rs.getBoolean("apto"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        }
        
        return lista;
    }

    /**
     * Lista todos los consultorios
     */
    public List<Consultorio> listarTodos() {
        String sql = "SELECT * FROM consultorio ORDER BY idConsultorio";
        List<Consultorio> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Consultorio c = new Consultorio();
                c.setIdConsultorio(rs.getInt("idConsultorio"));
                c.setUsos(rs.getString("usos"));
                c.setEquipamiento(rs.getString("equipamiento"));
                c.setApto(rs.getBoolean("apto"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar consultorios: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo consultorios activos y aptos
     */
    public List<Consultorio> listarActivos() {
        String sql = "SELECT * FROM consultorio WHERE estado=1 AND apto=1 ORDER BY idConsultorio";
        List<Consultorio> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Consultorio c = new Consultorio();
                c.setIdConsultorio(rs.getInt("idConsultorio"));
                c.setUsos(rs.getString("usos"));
                c.setEquipamiento(rs.getString("equipamiento"));
                c.setApto(rs.getBoolean("apto"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar consultorios activos: " + ex.getMessage());
        }
        
        return lista;
    }
}