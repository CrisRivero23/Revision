package persistencia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.SesionTurno;

public class SesionTurnoData {
    
    private Connection con;

    public SesionTurnoData() {
        this.con = Conexion.getConexion();
    }

    /**
     * Inserta una nueva sesión/turno
     */
    public void insertar(SesionTurno sesion) throws SQLException {
        String sql = "INSERT INTO sesion_turno (idCliente, idMasajista, id_tratamiento, idConsultorio, fecha_hora_inicio, fecha_hora_fin, observaciones, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sesion.getIdCliente());
            ps.setInt(2, sesion.getIdMasajista());
            ps.setInt(3, sesion.getIdTratamiento());
            ps.setInt(4, sesion.getIdConsultorio());
            ps.setTimestamp(5, Timestamp.valueOf(sesion.getFechaHoraInicio()));
            
            if (sesion.getFechaHoraFin() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(sesion.getFechaHoraFin()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            
            ps.setString(7, sesion.getObservaciones());
            ps.setBoolean(8, sesion.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sesion.setIdSesion(rs.getInt(1));
            }
        }
    }

    /**
     * Actualiza una sesión/turno existente
     */
    public void actualizar(SesionTurno sesion) throws SQLException {
        String sql = "UPDATE sesion_turno SET idCliente=?, idMasajista=?, id_tratamiento=?, idConsultorio=?, fecha_hora_inicio=?, fecha_hora_fin=?, observaciones=?, estado=? WHERE idSesion=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sesion.getIdCliente());
            ps.setInt(2, sesion.getIdMasajista());
            ps.setInt(3, sesion.getIdTratamiento());
            ps.setInt(4, sesion.getIdConsultorio());
            ps.setTimestamp(5, Timestamp.valueOf(sesion.getFechaHoraInicio()));
            
            if (sesion.getFechaHoraFin() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(sesion.getFechaHoraFin()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            
            ps.setString(7, sesion.getObservaciones());
            ps.setBoolean(8, sesion.isEstado());
            ps.setInt(9, sesion.getIdSesion());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina físicamente una sesión/turno
     */
    public void eliminar(int idSesion) throws SQLException {
        String sql = "DELETE FROM sesion_turno WHERE idSesion=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSesion);
            ps.executeUpdate();
        }
    }

    /**
     * Baja lógica de una sesión/turno
     */
    public void bajaLogica(int idSesion) throws SQLException {
        String sql = "UPDATE sesion_turno SET estado=0 WHERE idSesion=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSesion);
            ps.executeUpdate();
        }
    }

    /**
     * Alta lógica de una sesión/turno
     */
    public void altaLogica(int idSesion) throws SQLException {
        String sql = "UPDATE sesion_turno SET estado=1 WHERE idSesion=?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSesion);
            ps.executeUpdate();
        }
    }

    /**
     * Busca una sesión por ID
     */
    public SesionTurno buscarPorId(int idSesion) throws SQLException {
        String sql = "SELECT * FROM sesion_turno WHERE idSesion=?";
        SesionTurno sesion = null;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSesion);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                sesion = new SesionTurno();
                sesion.setIdSesion(rs.getInt("idSesion"));
                sesion.setIdCliente(rs.getInt("idCliente"));
                sesion.setIdMasajista(rs.getInt("idMasajista"));
                sesion.setIdTratamiento(rs.getInt("id_tratamiento"));
                sesion.setIdConsultorio(rs.getInt("idConsultorio"));
                sesion.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                
                Timestamp fin = rs.getTimestamp("fecha_hora_fin");
                if (fin != null) {
                    sesion.setFechaHoraFin(fin.toLocalDateTime());
                }
                
                sesion.setObservaciones(rs.getString("observaciones"));
                sesion.setEstado(rs.getBoolean("estado"));
            }
        }
        
        return sesion;
    }

    /**
     * Busca sesiones por cliente
     */
    public List<SesionTurno> buscarPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT * FROM sesion_turno WHERE idCliente=? ORDER BY fecha_hora_inicio DESC";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        }
        
        return lista;
    }

    /**
     * Busca sesiones por masajista
     */
    public List<SesionTurno> buscarPorMasajista(int idMasajista) throws SQLException {
        String sql = "SELECT * FROM sesion_turno WHERE idMasajista=? ORDER BY fecha_hora_inicio DESC";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMasajista);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        }
        
        return lista;
    }

    /**
     * Busca sesiones por fecha
     */
    public List<SesionTurno> buscarPorFecha(LocalDateTime fecha) throws SQLException {
        String sql = "SELECT * FROM sesion_turno WHERE DATE(fecha_hora_inicio)=? AND estado=1 ORDER BY fecha_hora_inicio";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha.toLocalDate()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        }
        
        return lista;
    }

    /**
     * Busca sesiones por consultorio
     */
    public List<SesionTurno> buscarPorConsultorio(int idConsultorio) throws SQLException {
        String sql = "SELECT * FROM sesion_turno WHERE idConsultorio=? ORDER BY fecha_hora_inicio DESC";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        }
        
        return lista;
    }

    /**
     * Lista todas las sesiones
     */
    public List<SesionTurno> listarTodas() {
        String sql = "SELECT * FROM sesion_turno ORDER BY fecha_hora_inicio DESC";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar sesiones: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista solo sesiones activas
     */
    public List<SesionTurno> listarActivas() {
        String sql = "SELECT * FROM sesion_turno WHERE estado=1 ORDER BY fecha_hora_inicio DESC";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar sesiones activas: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Lista sesiones próximas (desde hoy en adelante)
     */
    public List<SesionTurno> listarProximas() {
        String sql = "SELECT * FROM sesion_turno WHERE fecha_hora_inicio >= NOW() AND estado=1 ORDER BY fecha_hora_inicio";
        List<SesionTurno> lista = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                SesionTurno s = crearSesionDesdeResultSet(rs);
                lista.add(s);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar sesiones próximas: " + ex.getMessage());
        }
        
        return lista;
    }

    /**
     * Verifica disponibilidad de consultorio en un horario
     */
    public boolean verificarDisponibilidadConsultorio(int idConsultorio, LocalDateTime inicio, LocalDateTime fin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sesion_turno WHERE idConsultorio=? AND estado=1 AND " +
                     "((fecha_hora_inicio BETWEEN ? AND ?) OR (fecha_hora_fin BETWEEN ? AND ?) OR " +
                     "(fecha_hora_inicio <= ? AND fecha_hora_fin >= ?))";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsultorio);
            ps.setTimestamp(2, Timestamp.valueOf(inicio));
            ps.setTimestamp(3, Timestamp.valueOf(fin));
            ps.setTimestamp(4, Timestamp.valueOf(inicio));
            ps.setTimestamp(5, Timestamp.valueOf(fin));
            ps.setTimestamp(6, Timestamp.valueOf(inicio));
            ps.setTimestamp(7, Timestamp.valueOf(fin));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        
        return true;
    }

    /**
     * Método auxiliar para crear una sesión desde ResultSet
     */
    private SesionTurno crearSesionDesdeResultSet(ResultSet rs) throws SQLException {
        SesionTurno s = new SesionTurno();
        s.setIdSesion(rs.getInt("idSesion"));
        s.setIdCliente(rs.getInt("idCliente"));
        s.setIdMasajista(rs.getInt("idMasajista"));
        s.setIdTratamiento(rs.getInt("id_tratamiento"));
        s.setIdConsultorio(rs.getInt("idConsultorio"));
        s.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
        
        Timestamp fin = rs.getTimestamp("fecha_hora_fin");
        if (fin != null) {
            s.setFechaHoraFin(fin.toLocalDateTime());
        }
        
        s.setObservaciones(rs.getString("observaciones"));
        s.setEstado(rs.getBoolean("estado"));
        
        return s;
    }
}