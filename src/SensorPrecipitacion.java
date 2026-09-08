public class SensorPrecipitacion extends Sensor{
    public SensorPrecipitacion(String codigo, String marca, String modelo, EstacionMetereologica estacion){
        super(codigo, marca, modelo, estacion);
    }

    public String getUnidad(){
        return "Ola";
    }

    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=500){
            return true;
        }
        return false;
    }
}
