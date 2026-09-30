import java.time.LocalDateTime;
import java.util.ArrayList;

public class EstacionMeteorologica {
    private String codigo, nombre;
    private float longitud, latitud, altitud;
    private Estado estado;
    private Comuna comuna;
    //complemento (creamos un array list de los sensores
    private ArrayList<Sensor> sensores;
    //constructor de EstacionMeteorologica (Falta las comunas)
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
    public boolean instalaSensor(String codigo, String marca, String modelo, TipoSensor tipo){
        if(estado != Estado.ACTIVO){
            return false;
        }
        Sensor nuevoSensor = switch(tipo){
            case HUMEDAD -> new SensorHumedad(codigo, marca, modelo, this);
            case TEMPERATURA -> new SensorTemperatura(codigo, marca, modelo, this);
            case PRESION -> new SensorPresion(codigo, marca, modelo, this);
            case VIENTO -> new SensorViento(codigo, marca, modelo, this);
            case PRECIPITACION -> new SensorPrecipitacion(codigo, marca, modelo, this);
        };
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigo)){
                return false;
            }
            if(s.getEstado() == Estado.ACTIVO && (s.getClass() == nuevoSensor.getClass())){ // si existe un sensor activo igual a otro sensor activo, entonces rechazamos el cambio
                return false;
            }
        }
        return sensores.add(nuevoSensor);
    }
    public boolean registraMedicion(LocalDateTime fechaHora, float valor, String codigoSensor){
        if(estado != Estado.ACTIVO){
            return false;
        }
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigoSensor)){ //la condicion que evalua s como obj y compara su codigo con el parametro para buscar el sensor.
                return s.addMedicion(fechaHora, valor); //llamamos el metodo para agregar la medición con los parametros
            }
        }
        return false; //no se encontro ningún codigo que coincida
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
    public String[][] getResumenSensores(){
        String[][] resumenSensores = new String[sensores.size()][7];
        for(int i = 0; i < sensores.size(); i++) {
            String nomClase = sensores.get(i).getClass().getSimpleName(); // Tomamos la clase y la llevamos a String
            String sensor = nomClase.replace("Sensor", ""); // replace reemplaza un algo por algo, en este caso Sensor por "" que seria nada
            resumenSensores[i][0] = sensores.get(i).getCodigo();
            resumenSensores[i][1] = sensor.toUpperCase(); // Mostramos solo el tipo de sensor / Ej: HUMEDAD sin el Sensor por delante, sin el cambio hubiese quedado como SensorHumedad
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

    //metodos auxiliares:
    public String getCodigo(){return codigo;}
    public Estado getEstado(){return estado;}
}
