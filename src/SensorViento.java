public class SensorViento extends Sensor{
    public SensorViento(String codigo, String marca, String modelo, EstacionMeteorologica estacion){
        super(codigo, marca, modelo, estacion);
    }
    @Override
    public String getUnidad(){
        return "km/h";
    }
    @Override
    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=250){
            return true;
        }
        return false;
    }
}
