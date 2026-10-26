import java.util.*;
import java.time.*;
public class InstitutoMeteorologia {

    //ArrayList auxiliares
    private ArrayList<EstacionMeteorologica> estacionesMeteorologicas = new ArrayList<>();
    private ArrayList<Region> regiones = new ArrayList<>();

    public boolean creaRegion(int codigo, String nombre){
        for(Region r : regiones){
            if(r.getCodigo()==codigo || r.getNombre().equalsIgnoreCase(nombre)){
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
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(cod)){
                return false;
            }
        }

        for(Region r : regiones){
            if(codRegion == r.getCodigo()){
                Comuna c = r.findComunaById(codComuna);
                if(c == null){
                    return false;
                }
                EstacionMeteorologica nuevaEstacion = new EstacionMeteorologica(cod, nombre, lon, lat, alt, c);
                c.addEstacion(nuevaEstacion);
                return estacionesMeteorologicas.add(nuevaEstacion);
            }
        }
        return false;
    }
    public boolean instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion){
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codigoEstacion)){
                return e.instalaSensor(cod, marca, modelo, tipo);
            }
        }
        return false;
    }
    public boolean registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor){
        for(EstacionMeteorologica est : estacionesMeteorologicas){
            if(est.getCodigo().equalsIgnoreCase(codEstacion)){
                return est.registraMedicion(fechaHora, valor, codSensor); // usa registraMedicion de EstacionMeteorologica
            }
        }
        return false;
    }
    public String[][] listaRegiones(){
        String[][] listaRegiones = new String[regiones.size()][4];
        for(int i = 0; i < regiones.size(); i++){
            Region re = regiones.get(i); // Guardamos en la variable re de tipo Region, regiones.get(i)
            listaRegiones[i][0] = String.valueOf(re.getCodigo()); // Convierte valor numerico a String debido a que el arreglo es de String
            listaRegiones[i][1] = re.getNombre();
            listaRegiones[i][2] = String.valueOf(re.getComunas().length);
            listaRegiones[i][3] = String.valueOf(re.getCantidadEstaciones());
        }
        return listaRegiones;
    }
    public String[][] listaComunas(){
        int cantidadComunas = 0;
        for(Region r : regiones){
            cantidadComunas += r.getComunas().length;
        }

        String[][]listaComunas = new String[cantidadComunas][5];
        int nFila = 0;
        //en este apartado se recorre las comunas de cada region y se arma una fila por comuna
        for(Region r : regiones){
            Comuna[] comunasPorRegion = r.getComunas();
            for(Comuna c : comunasPorRegion){
                listaComunas[nFila][0] = String.valueOf(c.getCodigo());
                listaComunas[nFila][1] = c.getNombre();
                listaComunas[nFila][2] = r.getNombre();
                listaComunas[nFila][3] = String.valueOf(c.getCantidadEstaciones());
                listaComunas[nFila][4] = String.valueOf(c.getCantidadEstacionesActivas());
                nFila++;
            }

        }
        return listaComunas;
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna){
        for(Region re : regiones){
            if(codigoRegion == re.getCodigo()){
                Comuna comuna = re.findComunaById(codigoComuna);
                if(comuna == null){ // Nos aseguramos de si comuna es null desde el primer momento
                    return new String[0][5]; // si comuna es null/no existe entonces retornamos una matriz vacia
                }
                String[][] listaEstaciones = new String[comuna.getCantidadEstaciones()][5]; // Luego de la verificacion creamos la matriz
                int nFila = 0;
                for(EstacionMeteorologica e : estacionesMeteorologicas){
                    if(comuna.findEstacionById(e.getCodigo()) != null){ // Si al buscar la estacion pertenece a esa comuna entonces no es null
                        listaEstaciones[nFila] = e.toString().split("; ");// Se entrega el toString de EstacionMeteorologica y se hace un split en cada "; "
                        nFila++;
                    }
                }
                return listaEstaciones;
            }
        }
        return new String[0][5]; // Retornamos una matriz vacia como se nos indica si es que la region no existe
    }

    public String[][] listaSensores(String codigoEstacion){
        for(EstacionMeteorologica est : estacionesMeteorologicas){
            if(est.getCodigo().equalsIgnoreCase(codigoEstacion)){
                return est.getResumenSensores(); // Si una estacion tiene el mismo codigo entonces regresamos un resumen de esa estacion
            }
        }
        return new String[0][7]; // Si no pasa entonces se retorna una matriz vacia como se indica
    }

    public String[][] listaMediciones(String codEstacion, String codSensor, LocalDateTime inicio, LocalDateTime fin){
        for(EstacionMeteorologica est : estacionesMeteorologicas){
            if(est.getCodigo().equalsIgnoreCase(codEstacion)){
                return est.getMedicionesSensorBetween(codSensor, inicio, fin);
            }
        }
        return new String[0][4]; // si no pasa el if, entonces la estacion no existe y se devuelve una matriz vacia
    }
}
