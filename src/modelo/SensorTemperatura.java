package modelo;

import java.util.Optional;

public class SensorTemperatura extends Sensor implements Alertable {
    public SensorTemperatura(String codigo, String marca, String modelo, EstacionMeteorologica estacion){
        super(codigo, marca, modelo, estacion);
    }
    public static float convertirCelciusAFahrenheit(float valor){
        return (valor * 9f / 5f) + 32;
    }
    @Override
    public String getUnidad(){
        return "°C";
    }
    @Override
    public boolean esValorAdmisible(float valor){
        if(valor>=-80 && valor<=60){
            return true;
        }
        return false;
    }

    @Override
    public Optional<NivelAlerta> evaluaAlerta(float valor) {
        if(valor <= -5 || valor >= 38){
            return Optional.of(NivelAlerta.ALARMA);
        }else if(valor <= -2 || valor >= 35){
            return Optional.of(NivelAlerta.ALERTA);
        }else if(valor <= 0 || valor >= 32){
            return Optional.of(NivelAlerta.AVISO);
        }
        return Optional.empty();
    }
    @Override
    public String getFenomeno(float valor) {
        if(valor <= 0){
            return "Helada";
        }else if(valor >= 32){
            return "Calor extremo";
        }
        return "";
    }
}
