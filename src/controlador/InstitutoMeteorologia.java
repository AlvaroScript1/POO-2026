package controlador;

import modelo.*;

import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
    
public class InstitutoMeteorologia {
    private static InstitutoMeteorologia instance = null;
    private ArrayList<EstacionMeteorologica> estacionesMeteorologicas;
    private ArrayList<Region> regiones;

    private InstitutoMeteorologia(){
        estacionesMeteorologicas = new ArrayList<>();
        regiones = new ArrayList<>();
    }

    public static InstitutoMeteorologia getInstance(){
        if(instance == null){
            instance = new InstitutoMeteorologia();
        }
        return instance;
    }


    public void creaRegion(int codigo, String nombre) throws MeteorologiaException{
        Optional<Region> region = findRegion(codigo); // se crea un optional de region donde se le asigna el metodo findRegion(codigo)

        if(region.isPresent()){ // odio tener que volver a los ppt para ver las funciones/metodos de optional; este if retorna true si region contiene un valor
            Region r = region.get();
            if(r.getCodigo() == codigo){
                throw new MeteorologiaException("Ya existe una región con el código "+codigo); // lanzamos una excepcion con el mensaje dentro si existe una region con igual codigo
            }
        }
        if(regiones.stream().anyMatch(r->r.getNombre().equalsIgnoreCase(nombre))){
            throw new MeteorologiaException("Ya existe una región con el nombre"+nombre);
        }

        Region r1 = new Region(codigo, nombre);
        regiones.add(r1);
    }
    public void creaComuna(int codigo, String nombre, int codigoRegion) throws MeteorologiaException{ // Avisamos de que puede haber una excepcion{
        Optional<Region> region = findRegion(codigoRegion);
        if(region.isEmpty()){ // Si el metodo findRegion lanza un Optional que esta vacio, significa que no se encontro el objeto
            throw new MeteorologiaException("No existe una región con el código "+codigoRegion);
        }
        Region r = region.get(); // Obtenemos el objeto para acceder al metodo addComuna de Region
        r.addComuna(codigo, nombre);
    }
    public void creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna) throws MeteorologiaException {
        if(findEstacion(cod).isPresent()){ // En el orden del enunciado/word, vemos si existe la estacion con ese codigo con el metodo findEstacion
            throw new MeteorologiaException("Ya existe una estación con el código "+cod);
        }
        Optional<Region> region = findRegion(codRegion);
        if(region.isEmpty()){
            throw new MeteorologiaException("No existe una región con el código "+codRegion);
        }
        Region r = region.get(); // obtenemos r como objeto de tipo Region para usarlo en la excepcion con r.getnombre()
        Optional<Comuna> comuna = findComuna(codRegion,codComuna); // creamos un optional de tipo comuna referenciado con el metodo findComuna
        if(comuna.isEmpty()){ // si comuna es un optional vacio entonces no existe
            throw new MeteorologiaException("No existe una comuna con el código "+codComuna+" en la región "+r.getNombre());
        }
        Comuna c = comuna.get(); // obtenemos c como objeto tipo Comuna para usarlo en la creacion de la EstacionMeteorologica
        EstacionMeteorologica est = new EstacionMeteorologica(cod, nombre, lon, lat, alt, c);
        c.addEstacion(est);
        estacionesMeteorologicas.add(est); // En este caso agregamos al arrayList estacionesMeteorologicas la estacion, esto para que no se repitan las estaciones
    }
    public void instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codigoEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código "+codigoEstacion);
        }
        EstacionMeteorologica estacion =  est.get();
        estacion.instalaSensor(cod, marca, modelo, tipo);
        return;
    }
    public void registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código "+codEstacion);
        }
        EstacionMeteorologica estacion = est.get();
        estacion.registraMedicion(fechaHora, valor, codSensor);
    }

    public void cambiaEstadoSensor(String codigoEstacion, String codigoSensor, Estado estado) throws MeteorologiaException{
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codigoEstacion)) {
                e.cambiaEstadoSensor(codigoSensor, estado);
                return; // como en metodos anteriores, esto retorna al menu, la razon es que si dejamos pasar este return entonces se llega al throw de abajo y se ejecuta
            }
        }
        throw new MeteorologiaException("No existe una estación con el código "+codigoEstacion);
    }
    public void cargaDatos() throws MeteorologiaException{
        if(regiones.isEmpty() && estacionesMeteorologicas.isEmpty()){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            try {
                String[] r = PersistenciaTexto.leeRegistros("regiones.txt");
                String[] c = PersistenciaTexto.leeRegistros("comunas.txt");
                String[] e = PersistenciaTexto.leeRegistros("estaciones.txt");
                String[] s = PersistenciaTexto.leeRegistros("sensores.txt");
                String[] m = PersistenciaTexto.leeRegistros("mediciones.txt");
                int cantidad = 1;
                for (String re : r) {
                    try {
                        String[] datos = re.split(";"); // esto es para separar los datos en una arreglo cuando haya un ; como separador
                        int codigo = Integer.parseInt(datos[0].trim()); // convertimos el dato del indice 0 del arreglo a int de inmediato para luego crear el objeto region
                        String nombre = datos[1]; // el indice 1 corresponde al nombre de la region
                        creaRegion(codigo, nombre); // este metodo crea la region, en caso de error lanza excepcion
                    } catch (MeteorologiaException exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo regiones.txt: " + exc.getMessage() + " No se cargo ningun dato");
                    } catch (Exception exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo regiones.txt: formato invalido. No se cargo ningun archivo");
                    }
                    cantidad++;
                }
                cantidad = 1;
                for (String co : c) {
                    try {
                        // En este caso repetimos el proceso de arriba solo que con mas parametros para la creacion del objeto mediante su respectivo metodo
                        String[] datos = co.split(";");
                        int codigo = Integer.parseInt(datos[1].trim()); // corregimos el tema de que el indice 0 correspondia al nombre de la region y el indice 1 al nombre de la comuna
                        int codigoRegion = Integer.parseInt(datos[0].trim());
                        String nombre = datos[2];
                        creaComuna(codigo, nombre, codigoRegion);
                    } catch (MeteorologiaException exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo comunas.txt: " + exc.getMessage() + " No se cargo ningun dato");
                    } catch (Exception exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo comunas.txt: formato invalido. No se cargo ningun archivo");
                    }
                    cantidad++;
                }
                cantidad = 1;
                for (String est : e) {
                    try {
                        String[] datos = est.split(";");
                        String codigo = datos[0];
                        String nombre = datos[1];
                        float lon = Float.parseFloat(datos[2].trim());
                        float lat = Float.parseFloat(datos[3].trim());
                        float alt = Float.parseFloat(datos[4].trim());
                        int codRegion = Integer.parseInt(datos[5].trim());
                        int codComuna = Integer.parseInt(datos[6].trim());
                        creaEstacion(codigo, nombre, lon, lat, alt, codRegion, codComuna);
                    } catch (MeteorologiaException exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo estaciones.txt: " + exc.getMessage() + " No se cargo ningun dato");
                    } catch (Exception exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo estaciones.txt: formato invalido. No se cargo ningun dato");
                    }
                    cantidad++;
                }
                ArrayList<String> copia = new ArrayList<>();
                cantidad = 1;
                for (String sen : s) {
                    try {
                        String[] datos = sen.split(PersistenciaTexto.SEPARADOR);
                        String codigoEstacion = datos[0].trim();
                        String codigo = datos[1].trim();
                        TipoSensor nuevoSensor = TipoSensor.valueOf(datos[2].trim().toUpperCase()); // Extraemos el texto, con trim eliminamos espacios a los costados, con valueOf si tienes TEMPERATURA, buscara en el enum la coincidencia y te devolvera TEMPERATURA
                        String marca = datos[3].trim();
                        String modelo = datos[4].trim();
                        Estado estado = Estado.valueOf(datos[5].trim().toUpperCase());// el to upper case se asegura de que el String este en mayusculas para que coincida con los enums en mayusculas.
                        instalaSensor(codigo, marca, modelo, nuevoSensor, codigoEstacion);
                        cambiaEstadoSensor(codigoEstacion, codigo, Estado.INACTIVO);
                        if (estado == Estado.ACTIVO) {
                            copia.add(codigo);
                            copia.add(codigoEstacion);
                        }
                    } catch (MeteorologiaException exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo sensores.txt: " + exc.getMessage() + " No se cargo ningun dato");
                    } catch (Exception exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo sensores.txt: formato invalido. No se cargo ningun dato");
                    }
                    cantidad++;
                }
                cantidad = 1;
                for (String med : m) {
                    try {
                        String[] datos = med.split(PersistenciaTexto.SEPARADOR);
                        String codEstacion = datos[0].trim();
                        String codSensor = datos[1].trim();
                        LocalDateTime fechaYHora = LocalDateTime.parse(datos[2].trim(), formato); // entregamos la fechayhora en formato dd/MM/yyyy HH:mm
                        float valor = Float.parseFloat(datos[3].trim());

                        cambiaEstadoSensor(codEstacion, codSensor, Estado.ACTIVO);
                        registraMedicion(fechaYHora, valor, codEstacion, codSensor);
                        cambiaEstadoSensor(codEstacion, codSensor, Estado.INACTIVO);
                    } catch (MeteorologiaException exc) {
                        throw new MeteorologiaException("Linea " + cantidad + " del archivo mediciones.txt: " + exc.getMessage() + " No se cargo ningun dato");
                    } catch (Exception exc) {
                        throw new MeteorologiaException("Linea " + cantidad + "del archivo mediciones.txt: formato invalido. No se cargo ningun dato");
                    }
                    cantidad++;
                }
                for(int i = 0; i<copia.size(); i+=2){
                    String codSen = copia.get(i);
                    String codEst = copia.get(i+1);
                    cambiaEstadoSensor(codEst, codSen, Estado.ACTIVO); // Aqui cambiamos el estado de los sensores basados en el centinela o copia que teniamos realizado
                }
            }catch(MeteorologiaException exc){
                regiones.clear();
                estacionesMeteorologicas.clear(); // limpiamos en caso de error
                throw exc;
            }
        }else{
            throw new MeteorologiaException("La carga de datos solo puede realizarse cuando no existen datos registrados");
        }
    }

    public void guardaDatos() throws MeteorologiaException{
        if(regiones.isEmpty() && estacionesMeteorologicas.isEmpty()){
            throw new MeteorologiaException("No existen datos para guardar.");
        }
        ArrayList<String> regio = new ArrayList<>();
        for(Region r : regiones){
            String l1 = r.getCodigo()+";"+r.getNombre();
            regio.add(l1); // Este parece el proceso inverso de cargaDatos, jaja una funcion inversa que loco, simplemente vamos escribiendo en 1 linea todo un indice de un arreglo
        }
        String[] re = regio.toArray(new String[0]);
        PersistenciaTexto.escribeRegistros("regiones.txt", re);

        ArrayList<String> lisComunas = new ArrayList<>();
        for(Region r : regiones){
            for(Comuna c : r.getComunas()){
                String l1 = r.getCodigo()+";"+c.getCodigo()+";"+c.getNombre();
                lisComunas.add(l1);
            }
        }
        String[] co = lisComunas.toArray(new String[0]);
        PersistenciaTexto.escribeRegistros("comunas.txt", co);

        ArrayList<String> lisEstaciones = new ArrayList<>(); // POR QUE ES TAN LARGO ESTE CODIGOOOOOOOOOOOOOO Y SOLO VOY EN ESTACION TODAVIA ME QUEDAN MAS
        for(EstacionMeteorologica est : estacionesMeteorologicas){
            String l1 = est.getCodigo()+";"+est.getNombre()+";"+est.getPosicion()+";"+est.getComuna().getRegion().getCodigo()+";"+est.getComuna().getCodigo();
            lisEstaciones.add(l1);
        }
        String[] estac = lisEstaciones.toArray(new String[0]);
        PersistenciaTexto.escribeRegistros("estaciones.txt", estac);

        ArrayList<String> lisSensores = new ArrayList<>();
        for(EstacionMeteorologica est : estacionesMeteorologicas){ // Pero mira esta aberrasidad, recorrer las estaciones, luego los sensores y por ultimo las mediciones y de ahi extraer todo T-T
            for(Sensor s : est.getSensores()){ // Ayudante te pido ayuda
                String l1 = est.getCodigo()+";"+s.getCodigo()+";"+s.getTipo()+";"+s.getMarca()+";"+s.getModelo()+";"+s.getEstado();
                lisSensores.add(l1);
            }
        }
        String[] sens = lisSensores.toArray(new String[0]);
        PersistenciaTexto.escribeRegistros("sensores.txt", sens);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        ArrayList<String> lisMediciones = new ArrayList<>();
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            for(Sensor s : e.getSensores()){
                for(Medicion m : s.getMediciones()){ // QUEDA TIEMPO, PUEDO USAR EL FORMATEADOR? SI ALCANZO, SI
                    String fechaFormat = m.getFechaHora().format(format);
                    String l1 = e.getCodigo()+";"+s.getCodigo()+";"+fechaFormat+";"+m.getValor();
                    lisMediciones.add(l1);
                }
            }
        }
        String[] medicion = lisMediciones.toArray(new String[0]);
        PersistenciaTexto.escribeRegistros("mediciones.txt", medicion); // SIIIIIIII ME DIO EL TIEMPO A BUSCAR COMO SE HACIA EL FORMAT Y HACERLO, se me olvida por ratos como se hace, pero bueno
    }

    public String[][] listaRegiones(){
        return regiones.stream()
                .map(r-> new String[]{ String.valueOf(r.getCodigo()), r.getNombre(), String.valueOf(r.getComunas().length), String.valueOf(r.getCantidadEstaciones())})
                                .toArray(String[][]::new);// Un array con el codigo
        // de la region, su nombre, numero comunas y cantidad de estaciones
    }
    public String[][] listaComunas(){
        return regiones.stream().flatMap(r->Arrays.stream(r.getComunas()))
                .map(c-> new String[]{String.valueOf(c.getCodigo()), c.getNombre(), c.getRegion().getNombre(),
                        String.valueOf(c.getEstaciones().length), String.valueOf(c.getCantidadEstacionesActivas())})
                .toArray(String[][]::new);
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) throws MeteorologiaException{
        Region re = findRegion(codigoRegion).orElseThrow(() -> new MeteorologiaException("No existe una región con el código "+codigoRegion)); // busca la region y si no la encuentra entonces arroja la excepcion
        Comuna co = findComuna(codigoRegion, codigoComuna).orElseThrow(() -> new MeteorologiaException("No existe una comuna con el código "+codigoComuna+" en la región "+re.getNombre()));

        return Arrays.stream(co.getEstaciones())
                .map(e->e.toString().split("; "))
                .toArray(String[][]::new);
    }

    public String[][] listaSensores(String codigoEstacion) throws MeteorologiaException{
        EstacionMeteorologica estacion = findEstacion(codigoEstacion).orElseThrow(() -> new MeteorologiaException("No existe una estación con el código "+codigoEstacion));

        return Arrays.stream(estacion.getSensores())
                .map(s-> s.toString().split("; "))
                .toArray(String[][]::new);
    }

    public String[][] listaMediciones(String codEstacion, String codSensor, LocalDateTime inicio, LocalDateTime fin)throws MeteorologiaException{
        EstacionMeteorologica estacion = findEstacion(codEstacion).orElseThrow(()-> new MeteorologiaException("No existe una estación con el código "+codEstacion));
        Sensor sensor = findSensor(codEstacion, codSensor).orElseThrow(() -> new MeteorologiaException("No existe el sensor "+codSensor+" en la estación "+codEstacion));
        if(fin.isBefore(inicio)){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            throw new MeteorologiaException("La fecha y hora de término ("+fin.format(formato)+") es anterior a la de inicio ("+inicio.format(formato)+")");
            // Ajustamos el como se muestran las fechas de manera correcta
        }
        DateTimeFormatter ff = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hh = DateTimeFormatter.ofPattern("HH:mm");

        return Arrays.stream(sensor.getMedicionesBetween(inicio, fin))
                .map(med -> new String[])
        return est.getMedicionesSensorBetween(codSensor, inicio, fin);
    }
    public String[] listaResumenMediciones(String codigoEstacion, String codigoSensor, LocalDateTime inicio, LocalDateTime fin) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codigoEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código "+codigoEstacion);
        }
        Optional<Sensor> sen = findSensor(codigoEstacion, codigoSensor);
        if(sen.isEmpty()){
            throw new MeteorologiaException("No existe el sensor "+codigoSensor+" en la estación "+codigoEstacion);
        }
        Sensor sensor = sen.get();
        if(fin.isBefore(inicio)){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            throw new MeteorologiaException("La fecha y hora de término ("+fin.format(formato)+") es anterior a la de inicio ("+inicio.format(formato)+")");
            // Aplicamos a las fechas el formato correspondiente para que no salga como LocalDateTime y se vea de forma incorrecta
        }
        Optional<ResumenMediciones> medicionRes = sensor.getResumenBetween(inicio, fin);
        if(medicionRes.isEmpty()){
            return new String[0];
        }
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // formateador que usaremos mas adelante
        ResumenMediciones resum = medicionRes.get();
        String[] resumen = new String[8]; // Creamos un arreglo para guardar dentro el max, min, cantidad, promedio junto a la hora
        Medicion minim = resum.getMinimo();
        Medicion maxim = resum.getMaximo();

        resumen[0] = String.valueOf(sensor.getTipo()); // Es el sensor a mostrar
        resumen[1] = sensor.getUnidad(); // La unidad del sensor
        resumen[2] = String.valueOf(resum.getCantidad());
        resumen[3] = String.format("%.1f", minim.getValor());// limitamos a 1 decimal y obtenemos el valor min
        resumen[4] = minim.getFechaHora().format(formato); //obtenemos la hora de la medicion minima en formato dd/MM/yyyy HH:mm
        resumen[5] = String.format("%.1f", maxim.getValor()); // limitamos a 1 decimal y obtenemos el max valor
        resumen[6] = maxim.getFechaHora().format(formato); // obtenemos la hora de la medicion maxima en formato dd/MM/yyyy HH:mm
        resumen[7] = String.format("%.1f", resum.getPromedio()); // Obtenemos el promedio limitandolo a 1 decimal
        return resumen;
    }
    public String[][] listaAlertas(String codigoEstacion, LocalDateTime inicio, LocalDateTime fin)throws MeteorologiaException{

        EstacionMeteorologica estacion = findEstacion(codigoEstacion).orElseThrow(() -> new MeteorologiaException("No existe una estación con el código "+codigoEstacion));
        if(fin.isBefore(inicio)){
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            throw new MeteorologiaException("La fecha y hora de termino ("+fin.format(formato)+") es anterior a la de inicio ("+inicio.format(formato)+")");
            // Corregimos un detalle y es que la fecha y hora no se mostraba en su formato, si no que se mostraba como LocalDateTime
        }
        DateTimeFormatter ff = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hh = DateTimeFormatter.ofPattern("HH:mm");

        return Arrays.stream(estacion.getAlertasBetween(inicio, fin))
                .map(a->new String[]{a.getFechaHora().format(ff), a.getFechaHora().format(hh), a.getSensor().getCodigo(), a.getFenomeno(), String.format("%.1f", a.getValor()),
                        a.getSensor().getUnidad(), String.valueOf(a.getNivel())})
                .toArray(String[][]::new);
    }

    private Optional<Region> findRegion(int codigo){ // La verdad no entiendo mucho la logica del Optional asi que me fui a la diapo como ejemplo y vi que se estaba usando casi lo mismo que pide el enunciado
        return   regiones.stream()
                .filter(r-> r.getCodigo() == codigo)
                .findFirst();
    }

    private Optional<Comuna> findComuna(int codRegion, int codComuna){
        return findRegion(codRegion)
                .flatMap(r->r.findComunaById(codComuna));
    }

    private Optional<EstacionMeteorologica> findEstacion(String codigo){
        return estacionesMeteorologicas.stream()
                .filter(e->e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    private Optional<Sensor> findSensor(String codEstacion, String codSensor){
        return findEstacion(codEstacion)
                .flatMap(e->e.findSensorById(codSensor));
    }
}
