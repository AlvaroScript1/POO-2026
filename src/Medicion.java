import java.time.*; // esto para usar el LocalDateTime
import java.time.format.DateTimeFormatter; // esto para usar el formatter
public final class Medicion { // Hacemos inmutable la clase
    private final LocalDateTime fechaHora; // Hacemos inmutable el atributo fechahora
    private final float valor; // Hacemos inmutable el atributo valor

    public Medicion(LocalDateTime fechaHora, float valor){
        this.fechaHora = fechaHora;
        this.valor = valor;
    }

    public LocalDateTime getFechaHora(){
        return this.fechaHora; // retornamos la fecha y hora para su respectivo uso
    }
    public float getValor(){
        return this.valor; // retornamos el valor para su respectivo uso
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Medicion medicion){ //Nos aseguramos que o pertenezca a Medicion
            return this.fechaHora.equals(medicion.getFechaHora());
        }
        return false;//Si no es asi retornamos falso
    }

    @Override
    public String toString(){
        DateTimeFormatter formatoRequerido = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Damos formato requerido por el uml y guia
        String formatoNuevo = this.fechaHora.format(formatoRequerido);
        return formatoNuevo + "; "+this.valor; // Sobreescribimos el toString a la fecha hora y al valor
    }

}
