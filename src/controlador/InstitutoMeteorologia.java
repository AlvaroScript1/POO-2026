package controlador;

import modelo.*;

import java.util.*;
import java.time.*;

public class InstitutoMeteorologia {
    private static InstitutoMeteorologia instance = null;
    private ArrayList<EstacionMeteorologica> estacionesMeteorologicas;
    private ArrayList<Region> regiones;

    private InstitutoMeteorologia(){
        estacionesMeteorologicas = new ArrayList<>();
        regiones = new ArrayList<>();
    }

    public static InstitutoMeteorologia getInstance(){
        if(instance == null){
            instance = new InstitutoMeteorologia();
        }
        return instance;
    }


    public void creaRegion(int codigo, String nombre) throws MeteorologiaException{
        Optional<Region> region = findRegion(codigo); // se crea un optional de region donde se le asigna el metodo findRegion(codigo)

        if(region.isPresent()){ // odio tener que volver a los ppt para ver las funciones/metodos de optional; este if retorna true si region contiene un valor
            Region r = region.get();
            if(r.getCodigo() == codigo){
                throw new MeteorologiaException("Ya existe una región con el código <"+codigo+">"); // lanzamos una excepcion con el mensaje dentro si existe una region con igual codigo
            }
        }
        for(Region r : regiones){ // Hacemos este for por si existe una region que no tiene mismo codigo que otra, pero si mismo nombre
            if(r.getNombre().equalsIgnoreCase(nombre)){
                throw new MeteorologiaException("Ya existe una región con el nombre <"+nombre+">");
            }
        }

        Region r1 = new Region(codigo, nombre);
        regiones.add(r1);
    }
    public void creaComuna(int codigo, String nombre, int codigoRegion) throws MeteorologiaException{ // Avisamos de que puede haber una excepcion{
        Optional<Region> region = findRegion(codigoRegion);
        if(region.isEmpty()){ // Si el metodo findRegion lanza un Optional que esta vacio, significa que no se encontro el objeto
            throw new MeteorologiaException("No existe una región con el código <"+codigoRegion+">");
        }
        Region r = region.get(); // Obtenemos el objeto para acceder al metodo addComuna de Region
        r.addComuna(codigo, nombre);
    }
    public void creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna) throws MeteorologiaException {
        if(findEstacion(cod).isPresent()){ // En el orden del enunciado/word, vemos si existe la estacion con ese codigo con el metodo findEstacion
            throw new MeteorologiaException("Ya existe una estación con el código <"+cod+">");
        }
        Optional<Region> region = findRegion(codRegion);
        if(region.isEmpty()){
            throw new MeteorologiaException("No existe una región con el código <"+codRegion+">");
        }
        Region r = region.get(); // obtenemos r como objeto de tipo Region para usarlo en la excepcion con r.getnombre()
        Optional<Comuna> comuna = findComuna(codRegion,codComuna); // creamos un optional de tipo comuna referenciado con el metodo findComuna
        if(comuna.isEmpty()){ // si comuna es un optional vacio entonces no existe
            throw new MeteorologiaException("No existe una comuna con el código <"+codComuna+"> en la región <"+r.getNombre()+">");
        }
        Comuna c = comuna.get(); // obtenemos c como objeto tipo Comuna para usarlo en la creacion de la EstacionMeteorologica
        EstacionMeteorologica est = new EstacionMeteorologica(cod, nombre, lon, lat, alt, c);
        c.addEstacion(est);
    }
    public void instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codigoEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
        }
        EstacionMeteorologica estacion =  est.get();
        estacion.instalaSensor(cod, marca, modelo, tipo);
        return;
    }
    public void registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codEstacion+">");
        }
        EstacionMeteorologica estacion = est.get();
        estacion.registraMedicion(fechaHora, valor, codSensor);
    }

    public void cambiaEstadoSensor(String codigoEstacion, String codigoSensor, Estado estado) throws MeteorologiaException{
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codigoEstacion)) {
                e.cambiaEstadoSensor(codigoSensor, estado);
                return; // como en metodos anteriores, esto retorna al menu, la razon es que si dejamos pasar este return entonces se llega al throw de abajo y se ejecuta
            }
        }
        throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
    }

    public String[][] listaRegiones(){
        String[][] listaRegiones = new String[regiones.size()][4];
        for(int i = 0; i < regiones.size(); i++){
            Region re = regiones.get(i); // Guardamos en la variable re de tipo modelo.Region, regiones.get(i)
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
                        listaEstaciones[nFila] = e.toString().split("; ");// Se entrega el toString de modelo.EstacionMeteorologica y se hace un split en cada "; "
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

    public Optional<Region> findRegion(int codigo){ // La verdad no entiendo mucho la logica del Optional asi que me fui a la diapo como ejemplo y vi que se estaba usando casi lo mismo que pide el enunciado
        for(Region r : regiones){
            if(codigo == r.getCodigo()){
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public Optional<Comuna> findComuna(int codRegion, int codComuna){
        for(Region r : regiones){
            if(codRegion == r.getCodigo()){
                return r.findComunaById(codComuna); // JODER QUE COSTO ESTA PARTE SOLO PQ PENSE QUE TIRABA ERROR ESTABA MALO Y ES PQ EL METODO NO EST ACTUALIZADO EN REGION
            } // nooooooo esto puede retornar null... tocara volver a ver como se retorna para que no de null o algo parecido
        }
        return Optional.empty();
    }

    public Optional<EstacionMeteorologica> findEstacion(String codigo){
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codigo)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Optional<Sensor> findSensor(String codEstacion, String codSensor){
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codEstacion)){
                return e.findSensorById(codSensor); // Por el momento lanza error imagino pq no existe ese metodo en la estacion
            }
        }
        return Optional.empty();
    }
}
