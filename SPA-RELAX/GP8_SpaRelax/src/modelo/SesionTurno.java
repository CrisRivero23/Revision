package modelo;

import java.time.LocalDateTime;

public class SesionTurno {
    
    private int idSesion;
    private int idCliente;
    private int idMasajista;
    private int idTratamiento;
    private int idConsultorio;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String observaciones;
    private boolean estado;

    public SesionTurno() {
    }

    public SesionTurno(int idCliente, int idMasajista, int idTratamiento, int idConsultorio, 
                       LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, 
                       String observaciones, boolean estado) {
        this.idCliente = idCliente;
        this.idMasajista = idMasajista;
        this.idTratamiento = idTratamiento;
        this.idConsultorio = idConsultorio;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public SesionTurno(int idSesion, int idCliente, int idMasajista, int idTratamiento, 
                       int idConsultorio, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, 
                       String observaciones, boolean estado) {
        this.idSesion = idSesion;
        this.idCliente = idCliente;
        this.idMasajista = idMasajista;
        this.idTratamiento = idTratamiento;
        this.idConsultorio = idConsultorio;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdMasajista() {
        return idMasajista;
    }

    public void setIdMasajista(int idMasajista) {
        this.idMasajista = idMasajista;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public int getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(int idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Sesión " + idSesion + " - " + fechaHoraInicio;
    }
}