package modelo;
import java.util.*; //viene el import del Optional

public interface Alertable {
    Optional<NivelAlerta> evaluaAlerta(float valor);
    String getFenomeno(float valor);
    default boolean generaAlerta(float valor){
        return evaluaAlerta(valor).isPresent();
    }
}
