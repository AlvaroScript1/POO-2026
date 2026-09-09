import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EstacionMeteorologica {
    private String codigo, nombre;
    private float longitud, latitud, altitud;
    private Estado estado;
    //complemento (creamos un array list de los sensores
    private List<Sensor> sensores;
    //constructor de EstacionMeteorologica (Falta las comunas)
    public EstacionMeteorologica(String cod, String nombre, float lon, float lat, float alt){
        cod = codigo;
        this.nombre = nombre;
        lon = longitud;
        lat = latitud;
        alt = altitud;
        this.estado = Estado.ACTIVO; //lo activamos por defecto
        //declaramos el array list de sensor
        this.sensores = new ArrayList<>();
    }
    public boolean instalaSensor(String codigo, String marca, String modelo, TipoSensor tipo){
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigo)){
                return false;
            }
        }
        Sensor nuevoSensor = switch(tipo){
            case HUMEDAD -> new SensorHumedad(codigo, marca, modelo, this);
            case TEMPERATURA -> SensorTemperatura(codigo, marca, modelo, this);
            case PRESION -> new SensorPresion(codigo, marca, modelo, this);
            case VIENTO -> new SensorViento(codigo, marca, modelo, this);
            case PRECIPITACION -> new SensorPrecipitacion(codigo, marca, modelo, this);
        };
        return sensores.add(nuevoSensor);
    }
    public boolean RegistraMedicion(LocalDateTime fechaHora, float valor, String codigoSensor){
        for(Sensor s : sensores){
            if(s.getCodigo().equalsIgnoreCase(codigoSensor){ //la condicion que evalua s como obj y compara su codigo con el parametro para buscar el sensor.
                return s.addMedicion(LocalDateTime fechaHora, float valor); //llamamos el metodo para agregar la medición con los parametros
            }
        }
        return false; //no se encontro ningún codigo que coincida
    }

    @Override
    public String toString() {
        return String.format("%s, %s, (%.2f, %.2f, %.2f), %s, %d",
                codigo, nombre, latitud, longitud, altitud, estado, sensores.size());
    }
    public String[][] getResumenSensores(){
        String[][]resumenSensores = new String[sensores.size][6];
        for(int i = 0; i < sensores.size; i++){
            resumenSensores[i][0] = sensores.get(i).getCodigo();
            resumenSensores[i][1] = sensores.get(i).getMarca();
            resumenSensores[i][2] = sensores.get(i).getModelo();
            resumenSensores[i][3] = sensores.get(i).getEstado();
            resumenSensores[i][4] = sensores.get(i).getLastMedicion();
        }

    }
}
