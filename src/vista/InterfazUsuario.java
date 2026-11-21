package vista;
import controlador.*; //importes de los packages
import modelo.*;

import java.sql.SQLOutput;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class InterfazUsuario {
    private static InterfazUsuario instance = null;
    private Scanner sc;
    private InterfazUsuario() {
        sc = new Scanner(System.in);
    }
    public static InterfazUsuario getInstance(){ // Se añadio el patron singleton en su totalidad
        if(instance == null){
            instance = new InterfazUsuario();
        }
        return instance;
    }


    public void menuPrincipal(){ // Menu desplegable junto a un switch que se encarga de dirigir al usuario para que opcion tomar
        int opciones;
        do {
            System.out.println("SISTEMA DE INFORMACIÓN METEOROLÓGICA");
            System.out.println("----------------------------------------------------------");
            System.out.println(" 1. Crear región");
            System.out.println(" 2. Crear comuna");
            System.out.println(" 3. Crear estación meteorológica");
            System.out.println(" 4. Instalar sensor");
            System.out.println(" 5. Registrar medición");
            System.out.println(" 6. Cambiar estado de sensor");
            System.out.println(" 7. Generar listados");
            System.out.println(" 8. Cargar datos");
            System.out.println(" 9. Guardar datos");
            System.out.println("10. Salir");
            System.out.print("Opcion: ");
            opciones = convertidorInt("Opcion: "); // Leemos un numero en formato String, eliminamos espacios y transformamos el String a int
            switch (opciones) {
                case 1:
                    crearRegion();
                    break;
                case 2:
                    crearComuna();
                    break;
                case 3:
                    crearEstacionMeteorologica();
                    break;
                case 4:
                    instalarSensor();
                    break;
                case 5:
                    registrarMedicion();
                    break;
                case 6:
                    cambiarEstadoSensor();
                    break;
                case 7:
                    menuListados();
                    break;
                case 8:
                    cargaDatos();
                    break;
                case 9:
                    guardaDatos();
                    break;
                case 10:
                    if(salir() == false){
                        opciones = 0;// Aqui se añadieron nuevos cases junto a llamadas a metodos en cada case ya que esta propuesto en el UML
                    }
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
            }
        } while (opciones != 10);
    }

    private void crearRegion() {
        System.out.println("CREAR REGIÓN");
        System.out.println("----------------------------------------------------------");
        int codigo = convertidorInt("Código de región: "); // Leemos el codigo como String, borramos los espacios y transformamos el String(numero) a Int.
        String nombre = convertidorTexto("Nombre: "); // Leemos el nombre como String y con trim borramos los espacios.
        try {
            InstitutoMeteorologia.getInstance().creaRegion(codigo, nombre);
            System.out.println("Región creada con éxito.");
        } catch (MeteorologiaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void crearComuna() {
        System.out.println("CREAR COMUNA");
        System.out.println("----------------------------------------------------------");
        int codigoRegion = convertidorInt("Código de región: ");
        int codigo = convertidorInt("Código de comuna: "); // Leemos el codigo como String, borramos los espacios y transformamos el String(numero) a Int.
        String nombre = convertidorTexto("Nombre: "); // Leemos el nombre como String y con trim borramos los espacios.
        try {
            InstitutoMeteorologia.getInstance().creaComuna(codigo, nombre, codigoRegion);
            System.out.println("Comuna creada con éxito.");
        } catch (MeteorologiaException e) {
            System.out.println("Error: " + e);
        }
    }

    private void crearEstacionMeteorologica() {
        System.out.println("CREAR ESTACIÓN METEOROLÓGICA");
        System.out.println("----------------------------------------------------------");
        String codigo = convertidorTexto("Código de estación: ");
        String nombre = convertidorTexto("Nombre: ");
        float lon = convertidorFloat("Longitud: ");
        float lat = convertidorFloat("Latitud: ");
        float alt = convertidorFloat("Altitud (m): ");
        int codRegion = convertidorInt("Código de región: ");
        int codComuna = convertidorInt("Código de comuna: ");
        try { // try-catch para validar y que no se caiga el programa
            InstitutoMeteorologia.getInstance().creaEstacion(codigo, nombre, lon, lat, alt, codRegion, codComuna);
            System.out.println("Estacion creada con éxito");
        } catch (MeteorologiaException e) {
            System.out.println("Error: " + e);
        }
    }

    private void instalarSensor() {
        System.out.println("INSTALAR SENSOR");
        System.out.println("----------------------------------------------------------");
        String codigoEstacion = convertidorTexto("Código de estación: ");
        int opcion = convertidorInt("Tipo [1 Temp.  2 Hum.  3 Presión  4 Viento  5 Precip.]: ");
        TipoSensor tipoSensor;
        switch (opcion) {
            case 1:
                tipoSensor = TipoSensor.TEMPERATURA;
                break;
            case 2:
                tipoSensor = TipoSensor.HUMEDAD;
                break;
            case 3:
                tipoSensor = TipoSensor.PRESION;
                break;
            case 4:
                tipoSensor = TipoSensor.VIENTO;
                break;
            case 5:
                tipoSensor = TipoSensor.PRECIPITACION;
                break;
            default:
                System.out.println("> Sensor invalido");
                return;
        }
        String codigoSen = convertidorTexto("Código de sensor: ");
        String marca = convertidorTexto("Marca: ");
        String modelo = convertidorTexto("Modelo: ");
        try { // try-catch para que no se caiga el programa y se valide correctamente
            InstitutoMeteorologia.getInstance().instalaSensor(codigoSen, marca, modelo, tipoSensor, codigoEstacion);
            System.out.println("> Sensor de " + tipoSensor.name().toLowerCase() + " instalado correctamente"); // lo que esta entre medio es el tipo de sensor formateado a lowerCase para que entre algo tipo "modelo.Sensor de humedad"
        } catch (MeteorologiaException e) {
            System.out.println("> Error: "+ e.getMessage());
        }
    }

    private void registrarMedicion() {
        System.out.println("REGISTRAR MEDICIÓN");
        System.out.println("----------------------------------------------------------");
        String codEstacion = convertidorTexto("Código de estación: ");
        String codSensor = convertidorTexto("Código de sensor: ");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // formateamos la fecha
        LocalDateTime fechaYHora = convertidorFechaHora("Fecha y hora [dd/MM/yyyy HH:mm]: ",formato); // pasamos la fecha al formato indicado y lo pasamos a LocalDateTime
        float val = convertidorFloat("Valor: ");
        try {  // Este try-catch es solicitado para que no hayan errores
            InstitutoMeteorologia.getInstance().registraMedicion(fechaYHora, val, codEstacion, codSensor);
            System.out.println("> Medición registrada correctamente.");
        } catch (MeteorologiaException e) {
            System.out.println("> Error: "+ e.getMessage());
        }
    }

    private void cambiarEstadoSensor(){ // No existian estos metodos en la entrega 2 que se realizo y debian estar, asi que los estoy realizando desde 0
        System.out.println("CAMBIAR ESTADO DE SENSOR");
        System.out.println("----------------------------------------------------------");
        String codEst = convertidorTexto("Código de estación: ");
        String codSen = convertidorTexto("Código de sensor: ");
        int opSen = convertidorInt("Nuevo estado [1 ACTIVO  2 INACTIVO]: ");
        Estado estado;
        if(opSen == 1){
            estado = Estado.ACTIVO;
        }else if(opSen == 2){
            estado = Estado.INACTIVO;
        }else{
            System.out.println("> Error: Opción inválida.");
            return;
        }

        try{
            InstitutoMeteorologia.getInstance().cambiaEstadoSensor(codEst, codSen, estado); // Nos encargamos de que si hay algun error entonces se lanza la excepcion
            System.out.println("> Estado del sensor actualizado correctamente.");
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
        }
    }

    private void cargaDatos(){
        System.out.println("CARGAR DATOS");
        System.out.println("----------------------------------------------------------");
        try{
            InstitutoMeteorologia.getInstance().cargaDatos();
            System.out.println("> Datos cargados correctamente."); // Simplemente cargamos los datos y hacemos un try-catch en caso de
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
        }
    }

    private boolean guardaDatos(){ // Se complementa con salir()
        System.out.println("GUARDAR DATOS");
        System.out.println("----------------------------------------------------------");
        try{
            InstitutoMeteorologia.getInstance().guardaDatos(); // Guardamos los datos y nuevamente un try-catch
            System.out.println("> Datos guardados correctamente.");
            return true;
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
            return false;
        }
    }
    private boolean salir(){
        while(true) {
            String sino = convertidorTexto("¿Desea guardar los datos antes de salir? [S/N]: "); // Solicitamos un guardado de datos y guardaDatos se encarga de procesar correctamente la solicitud, si sucede un error entonces no se guardan los datos
            if (sino.equalsIgnoreCase("S")) {
                if(guardaDatos() == true){
                    System.out.println("> Fin del programa.");
                    return true;
                }
                return false;
            }else if(sino.equalsIgnoreCase("N")){
                System.out.println("> Fin del programa.");
                return true;
            }else{
                System.out.println("> Error: Debe responder S o N");
            }
        }
    }

    private void menuListados(){
        int opcion;
        do {
            System.out.println("LISTADOS");
            System.out.println("----------------------------------------------------------");
            System.out.println("1. Regiones");
            System.out.println("2. Comunas");
            System.out.println("3. Estaciones de una comuna");
            System.out.println("4. Sensores de una estacion");
            System.out.println("5. Mediciones de un sensor");
            System.out.println("6. Resumen estadistico de un sensor");
            System.out.println("7. Alertas de una estación");
            System.out.println("8. Volver al menu principal");
            opcion = convertidorInt("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    listarRegiones();
                    break;
                case 2:
                    listarComunas();
                    break;
                case 3:
                    listarEstaciones();
                    break;
                case 4:
                    listarSensores();
                    break;
                case 5:
                    listarMediciones();
                    break;
                case 6:
                    listarResumenMediciones();
                    break;
                case 7:
                    listarAlertas(); // se agregan nuevos cases con llamadas a metodos dentro de interfaz
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        } while (opcion != 8);
    }

    private void listarRegiones() {
        String[][] m = InstitutoMeteorologia.getInstance().listaRegiones(); // Usamos listaRegiones de InstitutoMeteorologia pero con su instancia
        System.out.println("LISTADO DE REGIONES");
        System.out.println("----------------------------------------------------------");
        if (m.length == 0) {
            System.out.println("> No existen regiones registradas en el sistema");
            return;   // Esto es para regresar y no seguir con el resto de codigo
        }
        System.out.println("CÓDIGO\tNOMBRE\t\tCOMUNAS\tESTACIONES");
        for (String[] f : m) {
            System.out.println(f[0] + "\t" + f[1] + "\t\t" + f[2] + "\t" + f[3]); // Existia un indice de mas que podia provocar un error indexOutofBounds, era Index no me se el error en si
        }
    }

    private void listarComunas() {
        String[][] m = InstitutoMeteorologia.getInstance().listaComunas(); // Usamos listaComunas de InstitutoMeteorologia pero con su instancia
        System.out.println("LISTADO DE COMUNAS");
        System.out.println("----------------------------------------------------------");
        if (m.length == 0) {
            System.out.println("> No existen comunas registradas en el sistema");
            return;
        }
        System.out.println("CODIGO\tNOMBRE\t\tREGION\t\tESTACIONES\tACTIVAS");
        for (String[] f : m) {
            System.out.println(f[0] + "\t\t" + f[1] + "\t\t" + f[2] + "\t" + f[3] + "\t" + f[4]);
        }
    }

    private void listarEstaciones() {
        int codR = convertidorInt("Código de región: ");
        int codC = convertidorInt("Código de comuna: ");
        try {
            String[][] m = InstitutoMeteorologia.getInstance().listaEstaciones(codR, codC); // Usamos listaEstaciones de InstitutoMeteorologia pero con su instancia
            System.out.println("ESTACIONES DE LA COMUNA " + codC);
            System.out.println("----------------------------------------------------------");
            if (m.length == 0) {
                System.out.println("> No hay estaciones existentes para la comuna y region indicadas");
                return;
            }
            System.out.println("CÓDIGO\t\tNOMBRE\t\tUBICACION\t\t\tESTADO\tSENSORES OPERATIVOS");
            for (String[] f : m) {
                System.out.println(f[0] + "\t\t" + f[1] + "\t\t" + f[2] + "\t" + f[3] + "\t" + f[4]);
            }
        } catch (MeteorologiaException e) {
            System.out.println("> Error: "+ e.getMessage());
        }
    }

    private void listarSensores(){
        String codE = convertidorTexto("Código de estación: ");
        try {
            String[][] m = InstitutoMeteorologia.getInstance().listaSensores(codE); // Usamos listaSensores de InstitutoMeteorologia pero con su instancia
            System.out.println("SENSORES DE " + codE.toUpperCase());
            System.out.println("----------------------------------------------------------");
            if (m.length == 0) {
                System.out.println("> Estacion inexistente o sin sensores instalados en ella");
                return;
            }
            System.out.println("CODIGO\tTIPO\t\tMARCA\tMODELO\tUNIDAD\tESTADO\tULTIMA MEDICIÓN");
            for (String[] f : m) {
                System.out.println(f[0] + "\t" + f[1] + "\t" + f[2] + "\t" + f[3] + "\t" + f[4] + "\t" + f[5] + "\t" + f[6]);
            }
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
        }
    }

    private void listarMediciones(){
        String codE = convertidorTexto("Código de estación: ");
        String codS = convertidorTexto("Código de sensor: ");
        DateTimeFormatter formateado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // formateamos fecha
        LocalDateTime i = convertidorFechaHora("Inicio [dd/MM/yyyy HH:mm]: ", formateado); // pasamos un string de fecha a un formato y esa nueva fecha formateada la pasamos a tipo LocalDateTime
        LocalDateTime f = convertidorFechaHora("Fin [dd/MM/yyyy HH:mm]: ", formateado);
        try {
            String[][] m = InstitutoMeteorologia.getInstance().listaMediciones(codE, codS, i, f); // Usamos listaMediciones de controlador.InstitutoMeteorologia

            System.out.println("MEDICIONES DEL SENSOR " + codS.toUpperCase());
            System.out.println("----------------------------------------------------------");
            if (m.length == 0) {
                System.out.println("> No hay mediciones existentes para los datos y fecha indicados");
                return;
            }
            System.out.println("Período: " + i.format(formateado) + " a " + f.format(formateado)); // Este es el titulo que se mostrara con las fechas formateadas
            System.out.println("FECHA\t\tHORA\tVALOR\tUNIDAD");
            for (String[] a : m) {
                System.out.println(a[0] + "\t" + a[1] + "\t" + a[2] + "\t" + a[3]);
            }
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
        }
    }

    private void listarResumenMediciones(){
        String codEst = convertidorTexto("Código de estación: ");
        String codSen = convertidorTexto("Código de sensor: ");

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime i = convertidorFechaHora("Inicio [dd/MM/yyyy HH:mm]: ", formato);
        LocalDateTime f = convertidorFechaHora("Fin [dd/MM/yyyy HH:mm]: ", formato);

        try{
            String[] resumen = InstitutoMeteorologia.getInstance().listaResumenMediciones(codEst, codSen, i, f);
            System.out.println("RESUMEN DE MEDICIONES DEL SENSOR "+ codSen.toUpperCase());
            System.out.println("----------------------------------------------------------");
            if(resumen.length == 0){
                System.out.println("> No hay mediciones en el período indicado.");
                return;
            }
            System.out.println("Período: "+i.format(formato) +" a "+f.format(formato));
            System.out.println("Tipo de sensor : "+ resumen[0]);
            System.out.println("Unidad         : "+ resumen[1]);
            System.out.println("Cantidad       : "+ resumen[2]);
            System.out.println("Mínimo         : "+ resumen[3]+" "+resumen[1]+" ("+resumen[4]+")");
            System.out.println("Máximo         : "+ resumen[5]+" "+resumen[1]+" ("+resumen[6]+")");
            System.out.println("Promedio       : "+ resumen[7]+" "+resumen[1]);
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+ exc.getMessage());
        }
    }

    private void listarAlertas(){
        String codEst = convertidorTexto("Código de estación: ");

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime i = convertidorFechaHora("Inicio [dd/MM/yyyy HH:mm]: ", formato);
        LocalDateTime f = convertidorFechaHora("Fin [dd/MM/yyyy HH:mm]: ", formato);

        try{
            String [][] aler = InstitutoMeteorologia.getInstance().listaAlertas(codEst, i, f);
            System.out.println("ALERTAS DE LA ESTACION "+ codEst.toUpperCase());
            System.out.println("----------------------------------------------------------");
            if(aler.length == 0){
                System.out.println("> No hay alertas en el período indicado.");
                return;
            }
            System.out.println("Período: "+ i.format(formato) +" a "+f.format(formato));
            System.out.println("FECHA\t\tHORA\tSENSOR\tFENÓMENO\t\tVALOR\tUNIDAD\tNIVEL");
            for(String[] fila : aler){
                System.out.println(fila[0]+"\t"+fila[1]+"\t"+fila[2]+"\t"+fila[3]+"\t\t"+fila[4]+"\t"+fila[5]+"\t"+fila[6]);
            }
        }catch(MeteorologiaException exc){
            System.out.println("> Error: "+exc.getMessage());
        }
    }


    /* Abajo iran metodos privados para hacer mas eficiente el codigo, ya que me estoy dando cuenta
     que se esta repitiendo mucho codigo y puede afectar negativamente la ejecucion del programa o
     la lectura de este mismo*/

    private int convertidorInt(String texto) { // convierte la funcion convertidorTexto a Int
        while(true){ //Este es un bucle que se repetira hasta que el usuario ingrese un valor valido y funciona para los metodos de abajo tambien
            System.out.println(texto); // esto es con el fin de que el usuario ingrese caracteres validos y no bote el programa por ingresar caracteres invalidos, espacios, etc.
            String e = sc.nextLine().trim();
            try{
                return Integer.parseInt(e);
            }catch(NumberFormatException exc){
                System.out.println("> Error: Debe ingresar un numero entero valido");
            }
        }
    }


    private String convertidorTexto(String texto) { // Imprime el texto y ademas retorna una lectura que elimina los espacios
        while(true){
            System.out.println(texto);
            String t = sc.nextLine().trim();
            if(!t.isEmpty()){
                return t;
            }
            System.out.println("> Error: El valor no puede estar vacio");
        }
    }

    private float convertidorFloat(String texto) {
        while(true){
            System.out.println(texto);
            String e = sc.nextLine().trim();
            try{
                return Float.parseFloat(e);
            }catch(NumberFormatException exc){
                System.out.println("> Error: Debe ingresar un número decimal válido.");
            }
        }
    }

    private LocalDateTime convertidorFechaHora(String texto, DateTimeFormatter formato){ // Se tiene que ingresar una fecha valida si no, se repite el ciclo
        while(true){
            String e = convertidorTexto(texto);
            try{
                return LocalDateTime.parse(e,formato);
            }catch(DateTimeParseException exc){
                System.out.println("> Error: Formato de fecha y hora inválidos. Utilice dd/MM/yyyy HH:mm");
            }
        }
    }
    // Lo que hacen estos metodos es leer un String que asumimos que seran datos correctos en su caso, eliminar espacios inadecuados que existan y
    // transformarlos a un tipo de dato correspondiente, en este caso puede ser Float o Int, para el metodo convertidorTexto, muestra un texto correspondiente
    // ademas de eso hace una lectura de parte del usuario.
}

