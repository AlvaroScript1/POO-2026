package modelo;

import java.util.Optional;

public class SensorViento extends Sensor implements Alertable {
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

    @Override
    public Optional<NivelAlerta> evaluaAlerta(float valor) {
        if(valor >= 50 && valor < 70){
            return Optional.of(NivelAlerta.AVISO);
        }else if(valor >= 70 && valor < 90){
            return Optional.of(NivelAlerta.ALERTA);
        }else if(valor >= 90){
            return Optional.of(NivelAlerta.ALARMA);
        }
        return Optional.empty();
    }

    @Override
    public String getFenomeno(float valor) {
        if(valor >= 50){
            return "Viento intenso";
        }
        return "";
    }
    @Override
    public TipoSensor getTipo() {
        return TipoSensor.VIENTO;
    }
}
