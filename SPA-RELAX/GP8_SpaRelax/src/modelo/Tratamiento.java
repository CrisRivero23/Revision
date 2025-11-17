package modelo;

public class Tratamiento {
    
    private int idTratamiento;
    private String nombre;
    private String tipo;
    private int duracionMin;
    private double costo;
    private boolean estado;

    public Tratamiento() {
    }

    public Tratamiento(String nombre, String tipo, int duracionMin, double costo, boolean estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionMin = duracionMin;
        this.costo = costo;
        this.estado = estado;
    }

    public Tratamiento(int idTratamiento, String nombre, String tipo, int duracionMin, double costo, boolean estado) {
        this.idTratamiento = idTratamiento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionMin = duracionMin;
        this.costo = costo;
        this.estado = estado;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo + " ($" + costo + ")";
    }
}