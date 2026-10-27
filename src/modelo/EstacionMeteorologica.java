package modelo;

import controlador.MeteorologiaException;

import java.sql.Array;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
public class EstacionMeteorologica {
    private String codigo, nombre;
    private float longitud, latitud, altitud;
    private Estado estado;
    private Comuna comuna;
    private ArrayList<AlertaMeteorologica> alertas = new ArrayList<>();
    //complemento (creamos un array list de los sensores
    private ArrayList<Sensor> sensores;
    //constructor de modelo.EstacionMeteorologica (Falta las comunas)
    public EstacionMeteorologica(String cod, String nombre,
                                 float lon, float lat, float alt,
                                 Comuna comuna){
        this.codigo = cod;
        this.nombre = nombre;
        this.longitud = lon;
        this.latitud = lat;
        this.altitud = alt;
        this.comuna = comuna;
        this.estado = Estado.ACTIVO; //lo activamos por defecto
        //declaramos el array list de sensor
        this.sensores = new ArrayList<>();
    }
    public String getCodigo(){
        return this.codigo;
    }
    public String getNombre(){
        return this.nombre;
    }
    public String getPosicion(){
        return this.longitud +";"+this.latitud+";"+this.altitud;
    }
    public Estado getEstado(){
        return this.estado;
    }
    public Comuna getComuna(){
        return this.comuna;
    }
    public void instalaSensor(String codigo, String marca, String modelo, TipoSensor tipo) throws MeteorologiaException {
        if(this.estado != Estado.ACTIVO){ // si no esta activo lanzamos excepcion
            throw new MeteorologiaException("La estacion <"+this.codigo+"> no esta activa");
        }
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigo)){ // Si existe un sensor con igual codigo, entonces lanzamos excepcion
                throw new MeteorologiaException("La estacion <"+this.codigo+"> ya tiene un sensor con el código <"+codigo+">");
            }
            if(s.getEstado() == Estado.ACTIVO && (s.getTipo() == tipo)){ // si existe un sensor activo igual a otro sensor activo, entonces rechazamos el cambio, aprovechando getTipo de Sensor
                throw new MeteorologiaException("La estacion <"+this.codigo+"> ya tiene un sensor ACTIVO de tipo <"+tipo+"> (<"+s.getCodigo()+">)");
            }
        }
        Sensor nuevoSensor = switch(tipo){ // si se cumple lo requerido, creamos un nuevo sensor y lo agregamos a la lista de sensores
            case HUMEDAD -> new SensorHumedad(codigo, marca, modelo, this);
            case TEMPERATURA -> new SensorTemperatura(codigo, marca, modelo, this);
            case PRESION -> new SensorPresion(codigo, marca, modelo, this);
            case VIENTO -> new SensorViento(codigo, marca, modelo, this);
            case PRECIPITACION -> new SensorPrecipitacion(codigo, marca, modelo, this);
        };
        sensores.add(nuevoSensor);
    }
    public void cambiaEstadoSensor(String codigoSensor, Estado estado) throws MeteorologiaException{
        Optional<Sensor> sen = findSensorById(codigoSensor); // usamos el metodo findSensorById, si es un Optional vacio, entonces no existe ese sensor en la estacion
        if(sen.isEmpty()){
            throw new MeteorologiaException("No existe el sensor <"+codigoSensor+"> en la estación <"+this.codigo+">");
        }
        Sensor sensor = sen.get(); // obtenemos el objeto para usar sus metodos
        if(sensor.getEstado() == estado){ // si el estado de sensor es igual al estado del parametro indicado, entonces no hay nada que cambiar y lanzamos excepcion
            throw new MeteorologiaException("El sensor <"+codigoSensor+"> ya se encuentra en estado <"+estado+">");
        }
        if(estado == Estado.ACTIVO) { // verificamos que este el estado sea activo en base al enunciado
            for (Sensor s : sensores) { // recorremos la lista de sensores
                if (!s.getCodigo().equalsIgnoreCase(codigoSensor) && (s.getTipo() == sensor.getTipo()) && (s.getEstado() == Estado.ACTIVO)) { // esto es medio confuso, pero en una sola "condicion" o if, nos aseguramos de que se pase por ese if cuando el codigo no es igual a si mismo y
                    throw new MeteorologiaException("No es posible activar el sensor <" + codigoSensor + ">: la estación ya tiene activo el sensor <" + s.getCodigo() + "> de tipo <" + s.getTipo() + ">"); // los tipos sean iguales y el sensor este activo, finalmente arrojando una excepcion
                }
            }
        }
        sensor.setEstado(estado); // luego de pasar todas las verificaciones, hacemos un set estado al objeto de tipo Sensor sensor, el cual esta vinculado al sensor guardado en la lista
    }
    public void registraMedicion(LocalDateTime fechaHora, float valor, String codigoSensor)throws MeteorologiaException{
        if(estado != Estado.ACTIVO){
            throw new MeteorologiaException("La estacion <"+this.codigo+"> no está activa");
        }
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigoSensor)){ //la condicion que evalua s como obj y compara su codigo con el parametro para buscar el sensor.
                s.addMedicion(fechaHora, valor); //llamamos el metodo para agregar la medición con los parametros
                return; // Este return es vital para que no se lance la excepcion de mas abajo
            }
        }
        throw new MeteorologiaException("No existe el sensor <"+codigoSensor+"> en la estación <"+this.codigo+">"); //no se encontro ningún codigo que coincida
    }

    public Sensor[] getSensores(){
        return sensores.toArray(new Sensor[0]);
    }
    public String[][] getResumenSensores() {
        String[][] resumenSensores = new String[sensores.size()][7];
        for (int i = 0; i < sensores.size(); i++) {
            String nomClase = sensores.get(i).getClass().getSimpleName(); // Tomamos la clase y la llevamos a String
            String sensor = nomClase.replace("Sensor", ""); // replace reemplaza un algo por algo, en este caso modelo.Sensor por "" que seria nada
            resumenSensores[i][0] = sensores.get(i).getCodigo();
            resumenSensores[i][1] = sensor.toUpperCase(); // Mostramos solo el tipo de sensor / Ej: HUMEDAD sin el modelo.Sensor por delante, sin el cambio hubiese quedado como modelo.SensorHumedad
            resumenSensores[i][2] = sensores.get(i).getMarca();
            resumenSensores[i][3] = sensores.get(i).getModelo();
            resumenSensores[i][4] = sensores.get(i).getUnidad();
            resumenSensores[i][5] = String.valueOf(sensores.get(i).getEstado()); // Tomamos el estado y lo transformamos en String

            Medicion medUltima = sensores.get(i).getLastMedicion(); // Tomamos la ultima medicion
            if (medUltima == null) { // Vemos el caso en el que la ultima medicion es null, no existe
                resumenSensores[i][6] = "No existe medicion";
            } else {
                resumenSensores[i][6] = medUltima.toString() + " " + sensores.get(i).getUnidad();
                // Transformamos la ultima medicion a String y de paso le concatenamos su unidad correspondiente polimorficamente
            }
        }
        return resumenSensores;
    }

    public String [][] getMedicionesSensorBetween(String codigoSensor, LocalDateTime inicio, LocalDateTime fin) {
        for (Sensor s : sensores) { // hacemos un for-each de sensores
            if (s.getCodigo().equalsIgnoreCase(codigoSensor)) { // si el codigo de sensores es igual a codigoSensor, entonces existe y podemos seguir
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // formateamos la fecha y hora
                DateTimeFormatter h = DateTimeFormatter.ofPattern("HH:mm");
                Medicion[] l = s.getMedicionesBetween(inicio, fin); // Toma las mediciones y usa el metodo getMedicionesBetween de modelo.Sensor
                String[][] requerido = new String[l.length][4];
                for (int i = 0; i < l.length; i++) {
                    requerido[i][0] = l[i].getFechaHora().format(f);
                    requerido[i][1] = l[i].getFechaHora().format(h);
                    requerido[i][2] = String.format("%.1f", l[i].getValor()); // pasamos el valor a String y le damos solamente 1 decimal
                    requerido[i][3] = s.getUnidad(); // simplemente llamamos a la unidad polimorficamente
                }
                return requerido; // retornamos la matriz con los valores ya puestos
            }
        }
        return new String[0][4]; // retornamos una matriz vacia si el sensor no existe en la estacion dada
    }
    public AlertaMeteorologica[] getAlertasBetween(LocalDateTime inicio, LocalDateTime fin){
        ArrayList<AlertaMeteorologica> aler = new ArrayList<>();
        for(AlertaMeteorologica a : alertas){
            if(!a.getFechaHora().isBefore(inicio) && !a.getFechaHora().isAfter(fin)){
                aler.add(a);
            }
        }
        aler.toArray(new AlertaMeteorologica[0]);
    }

    public Optional<Sensor> findSensorById(String codigo){ // recorremos con un for-each la lista de sensores y buscamos una coincidencia mediante el codigo de sensor, si es asi retornamos un Optional del objeto
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigo)){
                return Optional.of(s);
            }
        }
        return Optional.empty(); // si no hay coincidencias entonces retornamos un optional vacio
    }

    @Override
    public String toString() {
        int senActivo = 0;
        for(Sensor s : sensores){
            if(s.getEstado() == Estado.ACTIVO){ // Cuenta solo los sensores activos
                senActivo++;
            }
        }
        return String.format("%s; %s; (%.4f, %.4f, %.0f m); %s; %d",
                codigo, nombre, latitud, longitud, altitud, estado, senActivo);
    }
}