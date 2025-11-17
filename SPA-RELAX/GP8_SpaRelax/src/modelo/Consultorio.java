package modelo;

public class Consultorio {
    
    private int idConsultorio;
    private String usos;
    private String equipamiento;
    private boolean apto;
    private boolean estado;

    public Consultorio() {
    }

    public Consultorio(String usos, String equipamiento, boolean apto, boolean estado) {
        this.usos = usos;
        this.equipamiento = equipamiento;
        this.apto = apto;
        this.estado = estado;
    }

    public Consultorio(int idConsultorio, String usos, String equipamiento, boolean apto, boolean estado) {
        this.idConsultorio = idConsultorio;
        this.usos = usos;
        this.equipamiento = equipamiento;
        this.apto = apto;
        this.estado = estado;
    }

    public int getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(int idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public String getUsos() {
        return usos;
    }

    public void setUsos(String usos) {
        this.usos = usos;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public boolean isApto() {
        return apto;
    }

    public void setApto(boolean apto) {
        this.apto = apto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Consultorio " + idConsultorio + " - " + usos;
    }
}