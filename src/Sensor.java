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
                if(m.getFechaHora().equals(fechaHora)){ // Usamos método getFechaHora() de la clase medicion
                    return false;
                }
            }
            Medicion medicionNueva = new Medicion(fechaHora, valor); // creamos un nuevo objeto Medicion
            mediciones.add(medicionNueva); // Añadimos el nuevo objeto creado con anterioridad al arrayList mediciones
            return true;
        }
        return false;
    }

    public Medicion getLastMedicion(){ // En este caso queremos el objeto medicion más reciente de nuestra lista mediciones
        if(mediciones.isEmpty()){
            return null;// Pero sí esta vacía debería retornar null
        }
        Medicion actualCronologicamente = mediciones.get(0); // tomamos la primera medicion como última cronológicamente
        for(Medicion m : mediciones){
            if(m.getFechaHora().isAfter(actualCronologicamente.getFechaHora())){ // Hacemos un for-each y si una medicion viene después cronológicamente que la actualCronologicamente
                actualCronologicamente = m; // si una medicion es mas actual, entonces pasa a ser la mas actualCronologicamente
            }
        }
        return actualCronologicamente; // retornamos finalmente la cronológicamente más actual
    }
    public Medicion[] getMedicionesBetween(LocalDateTime inicio, LocalDateTime fin){
        int contador = 0;
        for(Medicion m : mediciones){
            if(!m.getFechaHora().isBefore(inicio) && !m.getFechaHora().isAfter(fin)){// Use los metodos Before y After negados para hacerlos inclusivos
                contador++;
            }
        }
        int i = 0;
        Medicion[] medicionesAptas = new Medicion[contador];
        for(Medicion m : mediciones){
            if(!m.getFechaHora().isBefore(inicio) && !m.getFechaHora().isAfter(fin)) { //Usamos mismo codigo de arriba para verificar las mediciones
                medicionesAptas[i] = m;
                i++; // De esta forma hacemos un for-each que recorra las mediciones y además por cada vuelta válida del if, se suma una i, que sirve como contador
                // Este contador es para que se simule un for normal
            }
        }
        return medicionesAptas;
    }
    public abstract String getUnidad(); //NO SE VEN BIEN EN EL UML SI ES ABSTRACT O NOOOO
    public abstract boolean esValorAdmisible(float valor); // metodo abstract
}
