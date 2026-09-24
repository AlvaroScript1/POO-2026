public class SensorPrecipitacion extends Sensor{
    public SensorPrecipitacion(String codigo, String marca, String modelo, EstacionMeteorologica estacion){
        super(codigo, marca, modelo, estacion);
    }

    public String getUnidad(){
        return "mm";
    }

    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=500){
            return true;
        }
        return false;
    }
}
