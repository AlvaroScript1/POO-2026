public class SensorHumedad extends Sensor{

    public SensorHumedad(String codigo, String marca, String modelo, EstacionMeteorologica estacion){
        super(codigo, marca, modelo, estacion);
    }

    public String getUnidad(){
        return "%";
    }
    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=100){
            return true;
        }
        return false;
    }
}
