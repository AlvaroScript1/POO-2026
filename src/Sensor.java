import java.util.ArrayList;
import java.time.*;
public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private ArrayList<Medicion> mediciones;
    private EstacionMeteorologica estacion;

    protected Sensor(String codigo, String marca, String modelo, EstacionMeteorologica estacion) {
        this.codigo = codigo;
        this.marca = marca;
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
        if(estado.equals(Estado.ACTIVO) && (this.esValorAdmisible(valor))){
            for(Medicion m: mediciones){
                if(m.getFechaHora().equals(fechaHora)){ // Usamos metodo getFechaHora() de la clase medicion
                    return false;
                }
            }
            Medicion medicionNueva = new Medicion(fechaHora, valor); // creamos un nuevo objeto Medicion
            mediciones.add(medicionNueva); // Añadimos el nuevo objeto creado con anterioridad al arrayList mediciones
            return true;
        }
        return false;
    }

    public Medicion getLastMedicion(){ // En este caso queremos el ultimo objeto medicion de nuestra lista mediciones
        if(mediciones.isEmpty()){
            return null;// Pero si esta vacia deberia retornar null
        }
        Medicion lastMedicion = mediciones.getLast();
        return lastMedicion; // Si tiene objetos entonces retornamos simplemente la ultima medicion
    }
    public abstract String getUnidad(); //NO SE VEN BIEN EN EL UML SI ES ABSTRACT O NOOOO
    public abstract boolean esValorAdmisible(float valor); // metodo abstract

}
