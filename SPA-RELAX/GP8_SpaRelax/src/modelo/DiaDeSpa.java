package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class DiaDeSpa {
    
    private int idDiaSpa;
    private String codPack;
    private LocalDate fecha;
    private LocalTime hora;
    private String preferencias;
    private int idCliente;
    private boolean estado;

    public DiaDeSpa() {
    }

    public DiaDeSpa(String codPack, LocalDate fecha, LocalTime hora, String preferencias, int idCliente, boolean estado) {
        this.codPack = codPack;
        this.fecha = fecha;
        this.hora = hora;
        this.preferencias = preferencias;
        this.idCliente = idCliente;
        this.estado = estado;
    }

    public DiaDeSpa(int idDiaSpa, String codPack, LocalDate fecha, LocalTime hora, String preferencias, int idCliente, boolean estado) {
        this.idDiaSpa = idDiaSpa;
        this.codPack = codPack;
        this.fecha = fecha;
        this.hora = hora;
        this.preferencias = preferencias;
        this.idCliente = idCliente;
        this.estado = estado;
    }

    public int getIdDiaSpa() {
        return idDiaSpa;
    }

    public void setIdDiaSpa(int idDiaSpa) {
        this.idDiaSpa = idDiaSpa;
    }

    public String getCodPack() {
        return codPack;
    }

    public void setCodPack(String codPack) {
        this.codPack = codPack;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Día de Spa " + idDiaSpa + " - " + fecha + " " + hora;
    }
}