public class Powerbank {
    private String id;
    private boolean alquilada;
    private boolean cargando; 
    private int nivelCarga;

    public Powerbank(String id) {
        this.id = id;
        this.alquilada = false;
        this.cargando = false;
        this.nivelCarga = 100; 
    }

    // Getters y Setters
    public String getId() { return id; }
    
    public boolean isAlquilada() { return alquilada; }
    public void setAlquilada(boolean estado) { this.alquilada = estado; }

    public boolean isCargando() { return cargando; }
    public void setCargando(boolean estado) { this.cargando = estado; }

    public int getNivelCarga() { return nivelCarga; }
    public void setNivelCarga(int nivel) { this.nivelCarga = nivel; }
}