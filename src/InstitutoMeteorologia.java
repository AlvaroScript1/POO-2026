import java.util.*;
public class InstitutoMeteorologia {
    //creo los ArrayList de lo que contiene el instituto, que son las estaciones y regiones.
    private ArrayList<EstacionMeteorologica> estacionesMeteorologicas = new ArrayList<>();
    private ArrayList<Region> regiones = new ArrayList<>();
    //estoy en proceso de terminar los metodos que faltan... coming soon
    public boolean creaRegion(int codigo, String nombre){
        return false;
    }
    public boolean creaComuna(int codigo, String nombre, int codigoRegion){
        return false;
    }
    public boolean creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna){
        return false;
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
