package modelo;

public class Masajista {
    
    private int idMasajista;
    private String nombre;
    private String matricula;
    private String telefono;
    private String especialidad;
    private boolean estado;

    public Masajista() {
    }

    public Masajista(String nombre, String matricula, String telefono, String especialidad, boolean estado) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public Masajista(int idMasajista, String nombre, String matricula, String telefono, String especialidad, boolean estado) {
        this.idMasajista = idMasajista;
        this.nombre = nombre;
        this.matricula = matricula;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public int getIdMasajista() {
        return idMasajista;
    }

    public void setIdMasajista(int idMasajista) {
        this.idMasajista = idMasajista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " - " + especialidad;
    }
}