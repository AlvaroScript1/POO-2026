import java.util.ArrayList;
import java.time.*;
public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private ArrayList<Medicion> mediciones;
    private EstacionMetereologica estacion;

    protected Sensor(String codigo, String marca, String modelo, EstacionMetereologica estacion) {
        this.codigo = codigo;
        this.modelo = modelo;
        this.estacion = estacion;
        this.estado = Estado.ACTIVO;
        this.mediciones = new ArrayList<>();
    }

    public String getCodigo(){
        return this.codigo;
    }
    public String getMarca(){
        return this.marca;
    }
    public String getModelo(){
        return this.modelo;
    }
    public Estado getEstado(){
        return this.estado;
    }
    public void setEstado(Estado estado){
        this.estado = estado;
    }

    public boolean addMedicion(LocalDateTime fechaHora, float valor){
        if(estado.equals(Estado.ACTIVO)){
            return true;
        }
        return false;
    }

    public Medicion getLastMedicion(){ // En este caso queremos el ultimo objeto medicion de nuestra lista mediciones
        Medicion lastMedicion = mediciones.getLast(); // Pero si esta vacia deberia retornar null
        return lastMedicion; // Si tiene objetos entonces retornamos simplemente la ultima medicion
    }

}
