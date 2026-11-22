package modelo;
import java.io.Serializable;
import java.util.*;

public class Comuna implements Serializable {
    private static final long serialVersionUID = 1L;
    private int codigo;
    private String nombre;
    private Region region;
    //arrayList Auxiliar
    private ArrayList<EstacionMeteorologica> estacionesPorComuna = new ArrayList<>();
    //constructor
    public Comuna(int codigo, String nombre, Region region){
        this.codigo = codigo;
        this.nombre = nombre;
        this.region = region;
    }
    public int getCodigo(){
        return codigo;
    }
    public String getNombre(){
        return nombre;
    }
    public void addEstacion(EstacionMeteorologica estacion){
        estacionesPorComuna.add(estacion);
    }
    public Optional<EstacionMeteorologica> findEstacionById(String codigo){
        return estacionesPorComuna.stream().filter(e-> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }
    public Region getRegion(){
        return this.region;
    }

    public EstacionMeteorologica[] getEstaciones(){
        return estacionesPorComuna.toArray(new EstacionMeteorologica[0]); // retornamos un arrayList que es convertido a arreglo de tipo EstacionMeteorologica
    }
    public int getCantidadEstacionesActivas(){
        int i = 0;
        for(EstacionMeteorologica e : estacionesPorComuna){
            if(e.getEstado() == Estado.ACTIVO){
                i++;
            }
        }
        return i;
    }
}
