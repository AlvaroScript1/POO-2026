package modelo;

import java.time.LocalDateTime;

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
        Medicion min = mediciones[0];
        Medicion max = mediciones[0];

        float suma = 0f;

        for (Medicion m : mediciones) {

            float valor = m.getValor();
            suma += valor;

            // Buscar la medición mínima
            if (valor < min.getValor() || valor == min.getValor() && m.getFechaHora().isBefore(min.getFechaHora())){
                min = m;
            }

            // Buscar la medición máxima
            if (valor > max.getValor() ||
                    (valor == max.getValor() && m.getFechaHora().isBefore(max.getFechaHora()))) {
                max = m;
            }
        }

        this.minimo = min;
        this.maximo = max;
        this.promedio = suma / cantidad;
    }
    public int getCantidad() {return cantidad;}
    public float getPromedio() {return promedio;}
    public Medicion getMinimo() {return minimo;}
    public Medicion getMaximo() {return maximo;}
}

