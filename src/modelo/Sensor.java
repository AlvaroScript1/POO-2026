package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.*;
import controlador.MeteorologiaException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public abstract class Sensor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private ArrayList<Medicion> mediciones;

    protected Sensor(String codigo, String marca, String modelo, EstacionMeteorologica estacion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
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
        if(mediciones.stream().anyMatch(m-> m.getFechaHora().equals(fechaHora))){
            throw new MeteorologiaException("El sensor "+codigo+" ya registra una medicion el " +
                    fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" a las "+
                    fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        int posicion = (int) mediciones.stream()
                .filter(m->m.getFechaHora().isBefore(fechaHora))
                .count();

        mediciones.add(posicion, new Medicion(fechaHora, valor));
    }

    public Medicion[] getMediciones() {
        return mediciones.toArray(new Medicion[0]);
    }

    public Medicion[] getMedicionesBetween(LocalDateTime inicio, LocalDateTime fin){
        return mediciones.stream()
                .filter(m -> !m.getFechaHora().isBefore(inicio) && !m.getFechaHora().isAfter(fin))
                .sorted(Comparator.comparing(Medicion::getFechaHora))
                .toArray(Medicion[]::new);
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
