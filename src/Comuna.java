import java.util.ArrayList;

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
    public int getCodigo(){return codigo;}
    public String getNombre(){return nombre;}
    public void addEstacion(EstacionMeteorologica estacion){
        estacionesPorComuna.add(estacion);
    }
    public EstacionMeteorologica findEstacionesById(String codigo){
        for(EstacionMeteorologica e : estacionesPorComuna){
            if(e.getCodigo().equalsIgnoreCase(codigo)){
                return e;
            }
        }
        return null;
    }
    public EstacionMeteorologica findEstacionById(String codigo){
        for(EstacionMeteorologica e : estacionesPorComuna){
            if(e.getCodigo().equalsIgnoreCase(codigo)){
                return e;
            }
        }
        return null;
    }
    public Region getRegion(){
        return this.region;
    }
    public int getCantidadEstaciones(){
        int i = 0;
        for(EstacionMeteorologica e : estacionesPorComuna){
            i++;
        }
        return i;
    }
    public int getEstacionesActivas(){
        int i = 0;
        for(EstacionMeteorologica e : estacionesPorComuna){
            if(e.getEstado() == Estado.ACTIVO){
                i++;
            }
        }
        return i;
    }
}
