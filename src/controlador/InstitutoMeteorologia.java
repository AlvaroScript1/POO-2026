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
                throw new MeteorologiaException("Ya existe una región con el código <"+codigo+">"); // lanzamos una excepcion con el mensaje dentro si existe una region con igual codigo
            }
        }
        for(Region r : regiones){ // Hacemos este for por si existe una region que no tiene mismo codigo que otra, pero si mismo nombre
            if(r.getNombre().equalsIgnoreCase(nombre)){
                throw new MeteorologiaException("Ya existe una región con el nombre <"+nombre+">");
            }
        }

        Region r1 = new Region(codigo, nombre);
        regiones.add(r1);
    }
    public void creaComuna(int codigo, String nombre, int codigoRegion) throws MeteorologiaException{ // Avisamos de que puede haber una excepcion{
        Optional<Region> region = findRegion(codigoRegion);
        if(region.isEmpty()){ // Si el metodo findRegion lanza un Optional que esta vacio, significa que no se encontro el objeto
            throw new MeteorologiaException("No existe una región con el código <"+codigoRegion+">");
        }
        Region r = region.get(); // Obtenemos el objeto para acceder al metodo addComuna de Region
        r.addComuna(codigo, nombre);
    }
    public void creaEstacion(String cod, String nombre, float lon, float lat, float alt, int codRegion, int codComuna) throws MeteorologiaException {
        if(findEstacion(cod).isPresent()){ // En el orden del enunciado/word, vemos si existe la estacion con ese codigo con el metodo findEstacion
            throw new MeteorologiaException("Ya existe una estación con el código <"+cod+">");
        }
        Optional<Region> region = findRegion(codRegion);
        if(region.isEmpty()){
            throw new MeteorologiaException("No existe una región con el código <"+codRegion+">");
        }
        Region r = region.get(); // obtenemos r como objeto de tipo Region para usarlo en la excepcion con r.getnombre()
        Optional<Comuna> comuna = findComuna(codRegion,codComuna); // creamos un optional de tipo comuna referenciado con el metodo findComuna
        if(comuna.isEmpty()){ // si comuna es un optional vacio entonces no existe
            throw new MeteorologiaException("No existe una comuna con el código <"+codComuna+"> en la región <"+r.getNombre()+">");
        }
        Comuna c = comuna.get(); // obtenemos c como objeto tipo Comuna para usarlo en la creacion de la EstacionMeteorologica
        EstacionMeteorologica est = new EstacionMeteorologica(cod, nombre, lon, lat, alt, c);
        c.addEstacion(est);
    }
    public void instalaSensor(String cod, String marca, String modelo, TipoSensor tipo, String codigoEstacion) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codigoEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
        }
        EstacionMeteorologica estacion =  est.get();
        estacion.instalaSensor(cod, marca, modelo, tipo);
        return;
    }
    public void registraMedicion(LocalDateTime fechaHora, float valor, String codEstacion, String codSensor) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codEstacion+">");
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
        throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
    }
    public void cargaDatos() throws MeteorologiaException{
        if(regiones.isEmpty() && estacionesMeteorologicas.isEmpty()){
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
                }catch(MeteorologiaException exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo regiones.txt: "+ exc.getMessage()+" No se cargo ningun dato");
                }catch(Exception exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo regiones.txt: formato invalido. No se cargo ningun archivo");
                }
                cantidad++;
            }
            cantidad = 1;
            for (String co : c) {
                try {
                    // En este caso repetimos el proceso de arriba solo que con mas parametros para la creacion del objeto mediante su respectivo metodo
                    String[] datos = co.split(";");
                    int codigo = Integer.parseInt(datos[0].trim());
                    int codigoRegion = Integer.parseInt(datos[1].trim());
                    String nombre = datos[2];
                    creaComuna(codigo, nombre, codigoRegion);
                }catch(MeteorologiaException exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo comunas.txt: "+exc.getMessage()+" No se cargo ningun dato");
                }catch(Exception exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo comunas.txt: formato invalido. No se cargo ningun archivo");
                }
                cantidad++;
            }
            cantidad = 1;
            for (String est : e){
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
                }catch(MeteorologiaException exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo estaciones.txt: "+exc.getMessage()+" No se cargo ningun dato");
                }catch(Exception exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo estaciones.txt: formato invalido. No se cargo ningun dato");
                }
                cantidad++;
            }
            Estado[] tiposEstado = new Estado[s.length];
            ArrayList<String> copia = new ArrayList<>();
            cantidad = 1;
            for (String sen : s) {
                try {
                    String[] datos = sen.split(";");
                    String codigoEstacion = datos[0];
                    String codigo = datos[1];
                    TipoSensor nuevoSensor = TipoSensor.valueOf(datos[2].trim().toUpperCase()); // Extraemos el texto, con trim eliminamos espacios a los costados, con valueOf si tienes TEMPERATURA, buscara en el enum la coincidencia y te devolvera TEMPERATURA
                    String marca = datos[3];
                    String modelo = datos[4];
                    Estado estado = Estado.valueOf(datos[5].trim().toUpperCase());// el to upper case se asegura de que el String este en mayusculas para que coincida con los enums en mayusculas.
                    instalaSensor(codigo, marca, modelo, nuevoSensor, codigoEstacion);
                    if (estado == Estado.ACTIVO) {
                        copia.add(codigo);
                        copia.add(codigoEstacion);
                    }
                }catch(MeteorologiaException exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo sensores.txt: "+exc.getMessage()+" No se cargo ningun dato");
                }catch(Exception exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo sensores.txt: formato invalido. No se cargo ningun dato");
                }
                cantidad++;
            }
            cantidad = 1;
            for(String med : m){
                try {
                    String[] datos = med.split(";");
                    String codEstacion = datos[0];
                    String codSensor = datos[1];
                    LocalDateTime fechaYHora = LocalDateTime.parse(datos[2]);
                    Float valor = Float.parseFloat(datos[3].trim());
                    registraMedicion(fechaYHora, valor, codEstacion, codSensor);
                }catch(MeteorologiaException exc){
                    throw new MeteorologiaException("Linea "+cantidad+" del archivo mediciones.txt: "+exc.getMessage()+" No se cargo ningun dato");
                }catch(Exception exc){
                    throw new MeteorologiaException("Linea "+cantidad+"del archivo mediciones.txt: formato invalido. No se cargo ningun dato");
                }
                cantidad++;
            }
            for(int i = 0; i<copia.size();i+=2){
                String codSen = copia.get(i);
                String codEst = copia.get(i+1);
                cambiaEstadoSensor(codEst, codSen, Estado.ACTIVO);
            }
        }else{
            throw new MeteorologiaException("La carga de datos solo puede realizarse cuando no existen datos registrados");
        }
    }

    public void guardaDatos() throws MeteorologiaException{
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
        String[][] listaRegiones = new String[regiones.size()][4];
        for(int i = 0; i < regiones.size(); i++){
            Region re = regiones.get(i); // Guardamos en la variable re de tipo modelo.Region, regiones.get(i)
            listaRegiones[i][0] = String.valueOf(re.getCodigo()); // Convierte valor numerico a String debido a que el arreglo es de String
            listaRegiones[i][1] = re.getNombre();
            listaRegiones[i][2] = String.valueOf(re.getComunas().length);
            listaRegiones[i][3] = String.valueOf(re.getCantidadEstaciones());
        }
        return listaRegiones;
    }
    public String[][] listaComunas(){
        int cantidadComunas = 0;
        for(Region r : regiones){
            cantidadComunas += r.getComunas().length;
        }

        String[][]listaComunas = new String[cantidadComunas][5];
        int nFila = 0;
        //en este apartado se recorre las comunas de cada region y se arma una fila por comuna
        for(Region r : regiones){
            Comuna[] comunasPorRegion = r.getComunas();
            for(Comuna c : comunasPorRegion){
                listaComunas[nFila][0] = String.valueOf(c.getCodigo());
                listaComunas[nFila][1] = c.getNombre();
                listaComunas[nFila][2] = r.getNombre();
                listaComunas[nFila][3] = String.valueOf(c.getEstaciones().length);
                listaComunas[nFila][4] = String.valueOf(c.getCantidadEstacionesActivas());
                nFila++;
            }

        }
        return listaComunas;
    }

    public String[][] listaEstaciones(int codigoRegion, int codigoComuna) throws MeteorologiaException{
        for(Region re : regiones){
            if(codigoRegion == re.getCodigo()){
                Optional<Comuna> comuna = re.findComunaById(codigoComuna);
                if(comuna.isEmpty()){
                    throw new MeteorologiaException("No existe una comuna con el código <"+codigoComuna+"> en la región <"+re.getNombre()+">");
                }
                Comuna c = comuna.get();
                String[][] listaEstaciones = new String[c.getEstaciones().length][]; // Luego de la verificacion creamos la matriz
                for(int i = 0; i<c.getEstaciones().length;i++){
                    listaEstaciones[i] = c.getEstaciones()[i].toString().split("; "); // Gracias al metodo getEstaciones de comuna podemos iterar directamente desde el metodo y por cada iteracion hacer un toString.split(" )
                }
                return listaEstaciones;
            }
        }
        throw new MeteorologiaException("No existe una región con el código <"+codigoRegion+">");// Retornamos una matriz vacia como se nos indica si es que la region no existe
    }

    public String[][] listaSensores(String codigoEstacion) throws MeteorologiaException{
        Optional<EstacionMeteorologica> estacion = findEstacion(codigoEstacion); // aprovechamos findEstacion para ver si existe la estacion o no
        if(estacion.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">"); // Lanzamos inmediatamente la excepcion si no existe una estacion con el codigo indicado
        }
        EstacionMeteorologica est = estacion.get();
        return est.getResumenSensores();
    }

    public String[][] listaMediciones(String codEstacion, String codSensor, LocalDateTime inicio, LocalDateTime fin)throws MeteorologiaException{
        Optional<EstacionMeteorologica> estacion = findEstacion(codEstacion);
        if(estacion.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codEstacion+">");
        }
        EstacionMeteorologica est = estacion.get();
        Optional<Sensor> sensor = findSensor(codEstacion, codSensor);
        if(sensor.isEmpty()){
            throw new MeteorologiaException("No existe el sensor <"+codSensor+"> en la estación <"+codEstacion+">");
        }
        if(fin.isBefore(inicio)){
            throw new MeteorologiaException("La fecha y hora de término (<"+fin+">) es anterior a la de inicio (<"+inicio+">)");
        }
        return est.getMedicionesSensorBetween(codSensor, inicio, fin);
    }
    public String[] listaResumenMediciones(String codigoEstacion, String codigoSensor, LocalDateTime inicio, LocalDateTime fin) throws MeteorologiaException{
        Optional<EstacionMeteorologica> est = findEstacion(codigoEstacion);
        if(est.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
        }
        Optional<Sensor> sen = findSensor(codigoEstacion, codigoSensor);
        if(sen.isEmpty()){
            throw new MeteorologiaException("No existe el sensor <"+codigoSensor+"> en la estación <"+codigoEstacion+">");
        }
        Sensor sensor = sen.get();
        if(fin.isBefore(inicio)){
            throw new MeteorologiaException("La fecha y hora de término (<"+fin+">) es anterior a la de inicio (<"+inicio+">)");
        }
        Optional<ResumenMediciones> medicionRes = sensor.getResumenBetween(inicio, fin);
        if(medicionRes.isEmpty()){
            return new String[0];
        }
        ResumenMediciones resum = medicionRes.get();
        String[] resumen = new String[4]; // Creamos un arreglo para guardar dentro el max, min, cantidad, promedio junto a la hora
        Medicion minim = resum.getMinimo();
        Medicion maxim = resum.getMaximo();

        resumen[0] = String.valueOf(resum.getCantidad());
        resumen[1] = String.valueOf(resum.getPromedio());
        resumen[2] = minim.getFechaHora() +";"+ minim.getValor();
        resumen[3] = maxim.getFechaHora() +";"+maxim.getValor();
        return resumen;
    }
    public String[][] listaAlertas(String codigoEstacion, LocalDateTime inicio, LocalDateTime fin)throws MeteorologiaException{
        Optional<EstacionMeteorologica> estacion = findEstacion(codigoEstacion);
        if(estacion.isEmpty()){
            throw new MeteorologiaException("No existe una estación con el código <"+codigoEstacion+">");
        }
        if(fin.isBefore(inicio)){
            throw new MeteorologiaException("La fecha y hora de termino (<"+fin+">) es anterior a la de inicio (<"+inicio+">)");
        }
        EstacionMeteorologica est = estacion.get();
        AlertaMeteorologica[] objAlertas = est.getAlertasBetween(inicio, fin);
        String[][] alertas = new String[objAlertas.length][6];
        for(int i = 0; i< objAlertas.length;i++){
            alertas[i][0] = String.valueOf(objAlertas[i].getFechaHora());
            alertas[i][1] = objAlertas[i].getSensor().getCodigo();
            alertas[i][2] = objAlertas[i].getFenomeno();
            alertas[i][3] = String.valueOf(objAlertas[i].getValor());
            alertas[i][4] = objAlertas[i].getSensor().getUnidad();
            alertas[i][5] = String.valueOf(objAlertas[i].getNivel());
        }
        return alertas;
    }

    public Optional<Region> findRegion(int codigo){ // La verdad no entiendo mucho la logica del Optional asi que me fui a la diapo como ejemplo y vi que se estaba usando casi lo mismo que pide el enunciado
        for(Region r : regiones){
            if(codigo == r.getCodigo()){
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public Optional<Comuna> findComuna(int codRegion, int codComuna){
        for(Region r : regiones){
            if(codRegion == r.getCodigo()){
                return r.findComunaById(codComuna); // JODER QUE COSTO ESTA PARTE SOLO PQ PENSE QUE TIRABA ERROR ESTABA MALO Y ES PQ EL METODO NO EST ACTUALIZADO EN REGION
            } // nooooooo esto puede retornar null... tocara volver a ver como se retorna para que no de null o algo parecido
        }
        return Optional.empty();
    }

    public Optional<EstacionMeteorologica> findEstacion(String codigo){
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codigo)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Optional<Sensor> findSensor(String codEstacion, String codSensor){
        for(EstacionMeteorologica e : estacionesMeteorologicas){
            if(e.getCodigo().equalsIgnoreCase(codEstacion)){
                return e.findSensorById(codSensor); // Por el momento lanza error imagino pq no existe ese metodo en la estacion
            }
        }
        return Optional.empty();
    }
}
