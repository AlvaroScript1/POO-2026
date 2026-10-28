package modelo;

import java.util.Optional;

public class SensorPrecipitacion extends Sensor implements Alertable {
    public SensorPrecipitacion(String codigo, String marca, String modelo, EstacionMeteorologica estacion) {
        super(codigo, marca, modelo, estacion);
    }
    @Override
    public String getUnidad(){
        return "mm";
    }
    @Override
    public boolean esValorAdmisible(float valor){
        if(valor>=0 && valor<=500){
            return true;
        }
        return false;
    }

    @Override
    public Optional<NivelAlerta> evaluaAlerta(float valor) {
        if(valor >= 20 && valor < 40){
            return Optional.of(NivelAlerta.AVISO);
        }else if(valor >= 40 && valor < 60){
            return Optional.of(NivelAlerta.ALERTA);
        }else if(valor >= 60){
            return Optional.of(NivelAlerta.ALARMA);
        }
        return Optional.empty();
    }

    @Override
    public String getFenomeno(float valor) {
        if(valor >= 20){
            return "Precipitacion intensa";
        }
        return "";
    }

    @Override
    public TipoSensor getTipo() {
        return TipoSensor.PRECIPITACION;
    }
}
