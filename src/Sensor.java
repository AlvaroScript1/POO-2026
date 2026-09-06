public abstract class Sensor {
    private String codigo;
    private String marca;
    private String modelo;
    private Estado estado;

    protected Sensor(String codigo, String modelo, EstacioMetereologica estacion) {
        this.codigo = codigo;
        this.modelo = modelo;
        this.estacion = estacion;
    }

}
