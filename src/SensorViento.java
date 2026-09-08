public class SensorViento extends Sensor{
    public SensorViento(String codigo, String marca, String modelo, EstacionMetereologica estacion){
        super(codigo, marca, modelo, estacion);
    }
    public String getUnidad(){
        return "Ola";
    }
    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=250){
            return true;
        }
        return false;
    }
}
