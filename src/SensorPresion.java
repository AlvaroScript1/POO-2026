public class SensorPresion extends Sensor{
    public SensorPresion(String codigo, String marca, String modelo, EstacionMetereologica estacion){
        super(codigo, marca, modelo, estacion);
    }
    public String getUnidad(){
        return "Ola";
    }
    public boolean esValorAdmisible(float valor){
        if(valor>=800 && valor<=1100){
            return true;
        }
        return false;
    }
}
