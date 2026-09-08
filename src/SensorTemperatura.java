public class SensorTemperatura extends Sensor{
    public SensorTemperatura(String codigo, String marca, String modelo, EstacionMetereologica estacion){
        super(codigo, marca, modelo, estacion);
    }

    public static float convertirCelsiusAFahrenheit(float valor){
        return 90;
    }
    public String getUnidad(){
        return "Ola";
    }
    public boolean esValorAdmisible(float valor){
        if(valor>=-80 && valor<=60){
            return true;
        }
        return false;
    }

}
