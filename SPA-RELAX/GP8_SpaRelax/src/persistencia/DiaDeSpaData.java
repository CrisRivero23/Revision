package persistencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.DiaDeSpa;

public class DiaDeSpaData {
    
    private Connection con;

    public DiaDeSpaData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Inserta un nuevo día de spa
     */
    public void insertar(DiaDeSpa diaSpa) throws SQLException {
        String sql = "INSERT INTO dia_de_spa (codPack, fecha, hora, preferencias, idCliente, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, diaSpa.getCodPack());
            ps.setDate(2, Date.valueOf(diaSpa.getFecha()));
            ps.setTime(3, Time.valueOf(diaSpa.getHora()));
            ps.setString(4, diaSpa.getPreferencias());
            ps.setInt(5, diaSpa.getIdCliente());
            ps.setBoolean(6, diaSpa.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                diaSpa.setIdDiaSpa(rs.getInt(1));
            }
        }
    }

    /**
     * Actualiza un día de spa existente
     */
    public void actualizar(DiaDeSpa diaSpa) throws SQLException {
        String sql = "UPDATE dia_de_spa SET codPack=?, fecha=?, hora=?, preferencias=?, idCliente=?, estado=? WHERE idDiaSpa=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, diaSpa.getCodPack());
            ps.setDate(2, Date.valueOf(diaSpa.getFecha()));
            ps.setTime(3, Time.valueOf(diaSpa.getHora()));
            ps.setString(4, diaSpa.getPreferencias());
            ps.setInt(5, diaSpa.getIdCliente());
            ps.setBoolean(6, diaSpa.isEstado());
            ps.setInt(7, diaSpa.getIdDiaSpa());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente un día de spa
     */
    public void eliminar(int idDiaSpa) throws SQLException {
        String sql = "DELETE FROM dia_de_spa WHERE idDiaSpa=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDiaSpa);
            ps.executeUpdate();
        }
    }

    /**
     * Baja lógica de un día de spa
     */
    public void bajaLogica(int idDiaSpa) throws SQLException {
        String sql = "UPDATE dia_de_spa SET estado=0 WHERE idDiaSpa=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDiaSpa);
            ps.executeUpdate();
        }
    }

    /**
     * Alta lógica de un día de spa
     */
    public void altaLogica(int idDiaSpa) throws SQLException {
        String sql = "UPDATE dia_de_spa SET estado=1 WHERE idDiaSpa=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDiaSpa);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un día de spa por ID
     */
    public DiaDeSpa buscarPorId(int idDiaSpa) throws SQLException {
        String sql = "SELECT * FROM dia_de_spa WHERE idDiaSpa=?";
        DiaDeSpa diaSpa = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDiaSpa);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                diaSpa = new DiaDeSpa();
                diaSpa.setIdDiaSpa(rs.getInt("idDiaSpa"));
                diaSpa.setCodPack(rs.getString("codPack"));
                diaSpa.setFecha(rs.getDate("fecha").toLocalDate());
                diaSpa.setHora(rs.getTime("hora").toLocalTime());
                diaSpa.setPreferencias(rs.getString("preferencias"));
                diaSpa.setIdCliente(rs.getInt("idCliente"));
                diaSpa.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return diaSpa;
    }

    /**
     * Busca días de spa por cliente
     */
    public List<DiaDeSpa> buscarPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT * FROM dia_de_spa WHERE idCliente=? ORDER BY fecha DESC, hora DESC";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(rs.getBoolean("estado"));
                lista.add(d);
            }
        }
        
        return lista;
    }

    /**
     * Busca días de spa por fecha
     */
    public List<DiaDeSpa> buscarPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM dia_de_spa WHERE fecha=? AND estado=1 ORDER BY hora";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(rs.getBoolean("estado"));
                lista.add(d);
            }
        }
        
        return lista;
    }

    /**
     * Busca días de spa por rango de fechas
     */
    public List<DiaDeSpa> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT * FROM dia_de_spa WHERE fecha BETWEEN ? AND ? AND estado=1 ORDER BY fecha, hora";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(rs.getBoolean("estado"));
                lista.add(d);
            }
        }
        
        return lista;
    }

    /**
     * Lista todos los días de spa
     */
    public List<DiaDeSpa> listarTodos() {
        String sql = "SELECT * FROM dia_de_spa ORDER BY fecha DESC, hora DESC";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(rs.getBoolean("estado"));
                lista.add(d);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar días de spa: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo días de spa activos
     */
    public List<DiaDeSpa> listarActivos() {
        String sql = "SELECT * FROM dia_de_spa WHERE estado=1 ORDER BY fecha DESC, hora DESC";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(true);
                lista.add(d);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar días de spa activos: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista días de spa próximos (desde hoy en adelante)
     */
    public List<DiaDeSpa> listarProximos() {
        String sql = "SELECT * FROM dia_de_spa WHERE fecha >= CURDATE() AND estado=1 ORDER BY fecha, hora";
        List<DiaDeSpa> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DiaDeSpa d = new DiaDeSpa();
                d.setIdDiaSpa(rs.getInt("idDiaSpa"));
                d.setCodPack(rs.getString("codPack"));
                d.setFecha(rs.getDate("fecha").toLocalDate());
                d.setHora(rs.getTime("hora").toLocalTime());
                d.setPreferencias(rs.getString("preferencias"));
                d.setIdCliente(rs.getInt("idCliente"));
                d.setEstado(true);
                lista.add(d);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar días de spa próximos: " + ex.getMessage());
        }
        
        return lista;
    }
}