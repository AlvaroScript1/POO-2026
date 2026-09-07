import java.util.ArrayList;
public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;
    private ArrayList<Medicion> mediciones;
    private EstacionMetereologica estacion;

    protected Sensor(String codigo, String marca, String modelo, EstacionMetereologica estacion) {
        this.codigo = codigo;
        this.modelo = modelo;
        this.estacion = estacion;
        this.estado = Estado.ACTIVO;
        this.mediciones = new ArrayList<>();
    }

}
