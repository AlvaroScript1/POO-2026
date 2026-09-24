import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InstitutoMeteorologia {

    private List<Region> regiones;
    private List<EstacionMeteorologica> estaciones;

    public InstitutoMeteorologia() {
        this.regiones = new ArrayList<>();
        this.estaciones = new ArrayList<>();
    }

    public boolean creaRegion(int codigo, String nombre) {
        for (Region r : regiones) {
            if (r.getCodigo() == codigo || r.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }

        Region nuevaRegion = new Region(codigo, nombre);
        regiones.add(nuevaRegion);
        return true;
    }

    public boolean creaComuna(int codigo, String nombre, int codigoRegion) {
        Region regionEncontrada = null;
        for (Region r : regiones) {
            if (r.getCodigo() == codigoRegion) {
                regionEncontrada = r;
                break;
            }
        }

        if (regionEncontrada == null) {
            return false;
        }

        return regionEncontrada.addComuna(codigo, nombre);
    }

    public boolean creaEstacion(String codigo, String nombre, float lon, float lat, float alt, int codRegion, int codComuna) {
        Region regionEncontrada = null;
        for (Region r : regiones) {
            if (r.getCodigo() == codRegion) {
                regionEncontrada = r;
                break;
            }
        }
        if (regionEncontrada == null) {
            return false;
        }

        Comuna comunaEncontrada = regionEncontrada.findComunaById(codComuna);
        if (comunaEncontrada == null) {
            return false;
        }

        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codigo)) {
                return false;
            }
        }

        EstacionMeteorologica estacion = new EstacionMeteorologica(codigo, nombre, lon, lat, alt, comunaEncontrada);
        comunaEncontrada.addEstacion(estacion);
        estaciones.add(estacion);

        return true;
    }

    public boolean instalaSensor(String codigo, String marca, String modelo, TipoSensor tipo, String codigoEstacion) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codigoEstacion)) {
                return e.instalaSensor(codigo, marca, modelo, tipo);
            }
        }
        return false;
    }

    public boolean registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codEstacion)) {
                return e.registraMedicion(codSensor, fechaHora, valor);
            }
        }
        return false;
    }

    public String[][] listaRegiones() {
        if (regiones.size() == 0) {
            return new String[0][0];
        }

        String[][] matriz = new String[regiones.size()][4];
        for (int i = 0; i < regiones.size(); i++) {
            Region r = regiones.get(i);
            matriz[i][0] = String.valueOf(r.getCodigo());
            matriz[i][1] = r.getNombre();

            Comuna[] comunas = r.getComunas();
            matriz[i][2] = String.valueOf(comunas != null ? comunas.length : 0);
            matriz[i][3] = String.valueOf(r.getCantidadEstaciones());
        }
        return matriz;
    }

    public String[][] listaComunas() {
        int totalComunas = 0;
        for (Region r : regiones) {
            Comuna[] comunas = r.getComunas();
            if (comunas != null) {
                totalComunas += comunas.length;
            }
        }

        if (totalComunas == 0) {
            return new String[0][0];
        }

        String[][] matriz = new String[totalComunas][5];
        int fila = 0;
        for (Region r : regiones) {
            Comuna[] comunas = r.getComunas();
            if (comunas != null) {
                for (Comuna c : comunas) {
                    matriz[fila][0] = String.valueOf(c.getCodigo());
                    matriz[fila][1] = c.getNombre();
                    matriz[fila][2] = r.getNombre();
                    matriz[fila][3] = String.valueOf(c.getCantidadEstaciones());
                    matriz[fila][4] = String.valueOf(c.getCantidadEstacionesActivas());
                    fila++;
                }
            }
        }
        return matriz;
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) {
        Region rEncontrada = null;
        for (Region r : regiones) {
            if (r.getCodigo() == codigoRegion) {
                rEncontrada = r;
                break;
            }
        }
        if (rEncontrada == null) return new String[0][0];

        Comuna cEncontrada = rEncontrada.findComunaById(codigoComuna);
        if (cEncontrada == null || cEncontrada.getCantidadEstaciones() == 0) {
            return new String[0][0];
        }

        String[][] matriz = new String[cEncontrada.getCantidadEstaciones()][5];
        int fila = 0;

        for (EstacionMeteorologica e : estaciones) {
            if (cEncontrada.findEstacionById(e.getCodigo()) != null) {
                matriz[fila][0] = e.getCodigo();
                matriz[fila][1] = e.getNombre();

                matriz[fila][2] = "(" + e.getLatitud() + "; " + e.getLongitud() + "; " + (int)e.getAltitud() + " m)";
                matriz[fila][3] = e.getEstado().name();

                matriz[fila][4] = String.valueOf(e.getCantidadSensoresOperativos());
                fila++;
            }
        }
        return matriz;
    }

    public String[][] listaSensores(String codigoEstacion) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codigoEstacion)) {
                return e.getResumenSensores();
            }
        }
        return new String[0][0];
    }

    public String[][] listaMediciones(String codEstacion, String codSensor, LocalDateTime inicio, LocalDateTime fin) {
        for (EstacionMeteorologica e : estaciones) {
            if (e.getCodigo().equals(codEstacion)) {
                return e.getMedicionesSensorBetween(codSensor, inicio, fin);
            }
        }
        return new String[0][0];
    }
}