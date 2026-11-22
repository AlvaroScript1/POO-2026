package modelo;

import java.util.Comparator;
import java.util.*;

public class ResumenMediciones {
    private final int cantidad;
    private final float promedio;
    private final Medicion minimo;
    private final Medicion maximo;
    public ResumenMediciones(Medicion[] mediciones) {

        if (mediciones == null || mediciones.length == 0) {
            throw new IllegalArgumentException("Debe existir al menos una medición.");
        }

        this.cantidad = mediciones.length;
        Comparator<Medicion> min = Comparator.comparing(Medicion::getValor) // Ordena en valor ascendente priorizando hora y fecha mas antiguas
                .thenComparing(Medicion::getFechaHora);
        Comparator<Medicion> max = Comparator.comparing(Medicion::getValor) // Ordena en valor ascendente, si hay empate invierte el orden
                .thenComparing(Comparator.comparing(Medicion::getFechaHora).reversed());

        this.minimo = Arrays.stream(mediciones)
                .min(min)
                .orElseThrow();

        this.maximo = Arrays.stream(mediciones)
                .max(max)
                .orElseThrow();

        this.promedio = (float) Arrays.stream(mediciones)
                .mapToDouble(Medicion::getValor)
                .average() // Average es el promedio solo que en ingles ja ja ja y saca el promedio
                .orElse(0.0);
    }
    public int getCantidad() {return cantidad;}
    public float getPromedio() {return promedio;}
    public Medicion getMinimo() {return minimo;}
    public Medicion getMaximo() {return maximo;}
}

