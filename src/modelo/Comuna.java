package modelo;
import java.util.*;

public class Comuna {
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
        for(EstacionMeteorologica e : estacionesPorComuna){
            if(e.getCodigo().equalsIgnoreCase(codigo)){
                return Optional.of(e); // en este caso retornamos el optional con la estacion meteorologica que tiene el codigo indicado
            }
        }
        return Optional.empty(); // Si no encontramos la region entonces retornamos un Optional vacio
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
