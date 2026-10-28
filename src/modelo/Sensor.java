package modelo;

import java.util.ArrayList;
import java.time.*;
import controlador.MeteorologiaException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private ArrayList<Medicion> mediciones;
    private EstacionMeteorologica estacion;

    protected Sensor(String codigo, String marca, String modelo, EstacionMeteorologica estacion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.estacion = estacion;
        this.estado = Estado.ACTIVO;
        this.mediciones = new ArrayList<>();
    }

    public String getCodigo(){
        return this.codigo;
    }
    public String getMarca(){
        return this.marca;
    }
    public String getModelo(){
        return this.modelo;
    }
    public Estado getEstado(){
        return this.estado;
    }
    public void setEstado(Estado estado){
        this.estado = estado;
    }

    public void addMedicion(LocalDateTime fechaHora, float valor) throws MeteorologiaException {
        if (estado != Estado.ACTIVO) {
            throw new MeteorologiaException("El sensor " + codigo + " no está activo.");
        }
        if (!esValorAdmisible(valor)) {
            throw new MeteorologiaException("El valor " + String.format("%.1f", valor) + " " + getUnidad()
                    + " no es admisible para un sensor de tipo " + getTipo() + ".");
        }

        // Como la lista ya está ordenada, la posición es la cantidad de mediciones anteriores.
        int posicion = 0;
        for (Medicion m : mediciones) {
            if (m.getFechaHora().equals(fechaHora)) {
                throw new MeteorologiaException("El sensor " + codigo + " ya registra una medición el "
                        + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a las "
                        + fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")) + ".");
            }
            if (m.getFechaHora().isBefore(fechaHora)) {
                posicion++;
            }
        }
        mediciones.add(posicion, new Medicion(fechaHora, valor));
    }

    public Medicion[] getMediciones() {
        return mediciones.toArray(new Medicion[0]);
    }

    public Medicion getLastMedicion(){
        if(mediciones.isEmpty()){
            return null;
        }
        Medicion actualCronologicamente = mediciones.get(0);
        for(Medicion m : mediciones){
            if(m.getFechaHora().isAfter(actualCronologicamente.getFechaHora())){
                actualCronologicamente = m;
            }
        }
        return actualCronologicamente;
    }

    public Medicion[] getMedicionesBetween(LocalDateTime inicio, LocalDateTime fin){
        int contador = 0;
        for(Medicion m : mediciones){
            if(!m.getFechaHora().isBefore(inicio) && !m.getFechaHora().isAfter(fin)){// Use los metodos Before y After negados para hacerlos inclusivos
                contador++;
            }
        }
        int i = 0;
        Medicion[] medicionesAptas = new Medicion[contador];
        for(Medicion m : mediciones){
            if(!m.getFechaHora().isBefore(inicio) && !m.getFechaHora().isAfter(fin)) { //Usamos mismo código de arriba para verificar las mediciones
                medicionesAptas[i] = m;
                i++; // De esta forma hacemos un for-each que recorra las mediciones y además por cada vuelta válida del if, se suma una i, que sirve como contador
                // Este contador es para que se simule un for normal
            }
        }
        return medicionesAptas;
    }

    public Optional<ResumenMediciones> getResumenBetween(LocalDateTime inicio, LocalDateTime fin) {
        Medicion[] delIntervalo = getMedicionesBetween(inicio, fin);
        if (delIntervalo.length == 0) {
            return Optional.empty();
        }
        return Optional.of(new ResumenMediciones(delIntervalo));
    }

    public abstract TipoSensor getTipo();
    public abstract String getUnidad();
    public abstract boolean esValorAdmisible(float valor); // metodo abstract
}
