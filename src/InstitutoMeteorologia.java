import java.util.*;
public class InstitutoMeteorologia {

    //ArrayList auxiliares
    private ArrayList<EstacionMeteorologica> estacionesMeteorologicas = new ArrayList<>();
    private ArrayList<Region> regiones = new ArrayList<>();

    public boolean creaRegion(int codigo, String nombre){
        for(Region r : regiones){
            if(r.getCodigo()==codigo){
                return false;
            }
        }
        Region nuevaRegion = new Region(codigo, nombre);
        return regiones.add(nuevaRegion);
    }
    public boolean creaComuna(int codigo, String nombre, int codigoRegion){
        for(Region r : regiones){
            if(codigoRegion == r.getCodigo()){
                return r.addComuna(codigo, nombre);
            }
        }
        return false;
    }
    public boolean creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna){
        for(Region r : regiones){
            if(codRegion == r.getCodigo()){
                Comuna c = r.findComunaByid(codComuna);
                if(c == null){
                    return false;
                }
                if(c.findEstacionesById(cod) != null){
                    return false;
                }
            }
        }
        EstacionMeteorologica nuevaEstacion = new EstacionMeteorologica(cod, nombre, lon, lat, alt, codRegion, codComuna);
        return estacionesMeteorologicas.add(nuevaEstacion);
    }
    public boolean instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion){
        return false;
    }
    public String[][] listaRegiones(){
        String[][] listaRegiones = new String[regiones.size()][1];
        for(int i = 0; i < regiones.size(); i++){
            listaRegiones[i][0] = regiones.get(i).getNombre();
        }
        return listaRegiones;
    }
}
