public class SensorTemperatura extends Sensor{
    public SensorTemperatura(String codigo, String marca, String modelo, EstacionMeteorologica estacion){
        super(codigo, marca, modelo, estacion);
    }

    public static float convertirCelsiusAFahrenheit(float valor){
        return (valor*9/5) + 32;
    }
    public String getUnidad(){
        return "°C";
    }
    public boolean esValorAdmisible(float valor){
        if(valor>=-80 && valor<=60){
            return true;
        }
        return false;
    }

}
