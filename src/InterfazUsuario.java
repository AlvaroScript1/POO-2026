import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
public class InterfazUsuario {
    private Scanner sc = new Scanner(System.in);
    private InstitutoMeteorologia instituto; // Asociacion con InstitutoMetereologia
    public static void main(String[]args) {
        InterfazUsuario interfaz = new InterfazUsuario();
        interfaz.menuPrincipal();
    }

    private void menuPrincipal() { // Menu desplegable junto a un switch que se encarga de dirigir al usuario para que opcion tomar
        instituto = new InstitutoMeteorologia();
        int opciones;
        do{
            System.out.println("SISTEMA DE INFORMACIÓN METEOROLÓGICA");
            System.out.println("----------------------------------------------------------");
            System.out.println("1. Crear región");
            System.out.println("2. Crear comuna");
            System.out.println("3. Crear estación meteorológica");
            System.out.println("4. Instalar sensor");
            System.out.println("5. Registrar medición");
            System.out.println("6. Generar listados");
            System.out.println("7. Salir");
            System.out.print("Opcion: ");
            opciones = Integer.parseInt(sc.nextLine().trim()); // Leemos un numero en formato String, eliminamos espacios y transformamos el String a int
            switch(opciones){
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
                    menuListados();
                    break;
                case 7:
                    System.out.println("Has salido del sistema");
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
            }
        }while(opciones != 7);
    }

    private void crearRegion() {
        System.out.println("CREAR REGIÓN");
        System.out.println("----------------------------------------------------------");
        int codigo = convertidorInt("Código de región: "); // Leemos el codigo como String, borramos los espacios y transformamos el String(numero) a Int.
        String nombre = convertidorTexto("Nombre: "); // Leemos el nombre como String y con trim borramos los espacios.
        if(instituto.creaRegion(codigo, nombre)){
            System.out.println("> Región creada correctamente.");
        }else{
            System.out.println("> No se ha podido crear la región, ya existe una con nombre o codigo identico");
        }
    }
    private void crearComuna() {
        System.out.println("CREAR COMUNA");
        System.out.println("----------------------------------------------------------");
        int codigoRegion = convertidorInt("Código de región: ");
        int codigo = convertidorInt("Código de comuna: "); // Leemos el codigo como String, borramos los espacios y transformamos el String(numero) a Int.
        String nombre = convertidorTexto("Nombre: "); // Leemos el nombre como String y con trim borramos los espacios.

        if(instituto.creaComuna(codigo, nombre, codigoRegion)){
            System.out.println("> Comuna creada correctamente.");
        }else{
            System.out.println("> No se ha podido crear la comuna, ya existe una con nombre o codigo identico o la region no existe");
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
        if(instituto.creaEstacion(codigo, nombre, lon, lat, alt, codRegion, codComuna)){
            System.out.println("> Estación meteorológica creada correctamente.");
        }else{
            System.out.println("> No se ha podido crear la estacion meteorologica, el codigo ya existe, o la region o comuna no existen");
        }
    }

    private void instalarSensor() {
        System.out.println("INSTALAR SENSOR");
        System.out.println("----------------------------------------------------------");
        String codigoEstacion = convertidorTexto("Código de estación: ");
        int opcion = convertidorInt("Tipo [1 Temp.  2 Hum.  3 Presión  4 Viento  5 Precip.]: ");
        TipoSensor tipoSensor;
        switch(opcion){
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
        if(instituto.instalaSensor(codigoSen, marca, modelo, tipoSensor, codigoEstacion)){
            System.out.println("> Sensor de "+ tipoSensor.name().toLowerCase()+" instalado correctamente"); // lo que esta entre medio es el tipo de sensor formateado a lowerCase para que entre algo tipo "Sensor de humedad"
        }else{
            System.out.println("> No se ha instalado el sensor, debido a que no existe la estacion, no esta activa o ya existe un sensor con mismo codigo o activo del mismo tipo");
        }
    }

    private void registrarMedicion() {
        System.out.println("REGISTRAR MEDICIÓN");
        System.out.println("----------------------------------------------------------");
        String codEstacion = convertidorTexto("Código de estación: ");
        String codSensor = convertidorTexto("Código de sensor: ");
        String fecha = convertidorTexto("Fecha y hora [dd/MM/yyyy HH:mm]: ");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // formateamos la fecha
        LocalDateTime fechaYHora = LocalDateTime.parse(fecha, formato); // pasamos la fecha al formato indicado y lo pasamos a LocalDateTime
        float val = convertidorFloat("Valor: ");
        if(instituto.registraMedicion(fechaYHora, val, codEstacion, codSensor)) {
            System.out.println("> Medición registrada correctamente.");
        }else{
            System.out.println("> No se ha podido registrar la medicion, la estacion o el sensor no existen o se encuentran inactivos, "+
                    "el valor no es apto o ya existe una medicion en esa fecha y hora");
        }
    }

    private void menuListados() {
        int opcion;
        do{
            System.out.println("LISTADOS");
            System.out.println("----------------------------------------------------------");
            System.out.println("1. Regiones");
            System.out.println("2. Comunas");
            System.out.println("3. Estaciones de una comuna");
            System.out.println("4. Sensores de una estacion");
            System.out.println("5. Mediciones de un sensor");
            System.out.println("6. Volver al menu principal");
            opcion = convertidorInt("Seleccione una opcion: ");
            switch(opcion){
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
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }while(opcion != 6);
    }

    private void listarRegiones() {
        String[][] m = instituto.listaRegiones(); // Usamos listaRegiones de InstitutoMeteorologia
        System.out.println("LISTADO DE REGIONES");
        System.out.println("----------------------------------------------------------");
        if(m.length == 0){
            System.out.println("> No existen regiones registradas en el sistema");
            return;   // Esto es para regresar y no seguir con el resto de codigo
        }
        System.out.println("CÓDIGO\tNOMBRE\t\tCOMUNAS\tESTACIONES");
        for(String[] f : m){
            System.out.println(f[0]+"\t"+f[1]+"\t\t"+f[2]+"\t"+f[3]+"\t"+f[4]);
        }
    }

    private void listarComunas() {
        String[][] m = instituto.listaComunas(); // Usamos listaComunas de InstitutoMeteorologia
        System.out.println("LISTADO DE COMUNAS");
        System.out.println("----------------------------------------------------------");
        if(m.length == 0){
            System.out.println("> No existen comunas registradas en el sistema");
            return;
        }
        System.out.println("CODIGO\tNOMBRE\t\tREGION\t\tESTACIONES\tACTIVAS");
        for(String[] f : m){
            System.out.println(f[0]+"\t\t"+f[1]+"\t\t"+f[2]+"\t"+f[3]+"\t"+f[4]);
        }
    }

    private void listarEstaciones() {
        int codR = convertidorInt("Código de región: ");
        int codC = convertidorInt("Código de comuna: ");
        String[][] m = instituto.listaEstaciones(codR, codC); // Usamos listaEstaciones de InstitutoMeteorologia
        System.out.println("ESTACIONES DE LA COMUNA "+codC);
        System.out.println("----------------------------------------------------------");
        if(m.length == 0){
            System.out.println("> No hay estaciones existentes para la comuna y region indicadas");
            return;
        }
        System.out.println("CÓDIGO\t\tNOMBRE\t\tUBICACION\t\t\tESTADO\tSENSORES OPERATIVOS");
        for(String[] f : m){
            System.out.println(f[0]+"\t\t"+f[1]+"\t\t"+f[2]+"\t"+f[3]+"\t"+f[4]);
        }
    }

    private void listarSensores() {
        String codE = convertidorTexto("Código de estación: ");
        String[][] m = instituto.listaSensores(codE); // Usamos listaSensores de InstitutoMeteorologia
        System.out.println("SENSORES DE "+ codE.toUpperCase());
        System.out.println("----------------------------------------------------------");
        if(m.length == 0){
            System.out.println("> Estacion inexistente o sin sensores instalados en ella");
            return;
        }
        System.out.println("CODIGO\tTIPO\t\tMARCA\tMODELO\tUNIDAD\tESTADO\tULTIMA MEDICION");
        for(String[] f : m){
            System.out.println(f[0]+"\t"+f[1]+"\t"+f[2]+"\t"+f[3]+"\t"+f[4]+"\t"+f[5]+"\t"+f[6]);
        }
    }

    private void listarMediciones() {
        String codE = convertidorTexto("Código de estación: ");
        String codS = convertidorTexto("Código de sensor: ");
        DateTimeFormatter formateado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // formateamos fecha
        LocalDateTime i = LocalDateTime.parse(convertidorTexto("Inicio [dd/MM/yyyy HH:mm]: "), formateado); // pasamos un string de fecha a un formato y esa nueva fecha formateada la pasamos a tipo LocalDateTime
        LocalDateTime f = LocalDateTime.parse(convertidorTexto("Fin [dd/MM/yyyy HH:mm]: "), formateado);
        String[][] m = instituto.listaMediciones(codE, codS, i, f); // Usamos listaMediciones de InstitutoMeteorologia

        System.out.println("MEDICIONES DEL SENSOR "+codS.toUpperCase());
        System.out.println("----------------------------------------------------------");
        if(m.length == 0){
            System.out.println("> No hay mediciones existentes para los datos y fecha indicados");
            return;
        }
        System.out.println("Período: "+ i.format(formateado)+" a "+f.format(formateado)); // Este es el titulo que se mostrara con las fechas formateadas
        System.out.println("FECHA\t\tHORA\tVALOR\tUNIDAD");
        for(String[] a : m){
            System.out.println(a[0]+"\t"+a[1]+"\t"+a[2]+"\t"+a[3]);
        }
    }



    /* Abajo iran metodos privados para hacer mas eficiente el codigo, ya que me estoy dando cuenta
     que se esta repitiendo mucho codigo y puede afectar negativamente la ejecucion del programa o
     la lectura de este mismo*/

    private int convertidorInt(String texto){ // convierte la funcion convertidorTexto a Int
        return Integer.parseInt(convertidorTexto(texto));
    }


    private String convertidorTexto(String texto){ // Imprime el texto y ademas retorna una lectura que elimina los espacios
        System.out.print(texto); //
        return sc.nextLine().trim();
    }

    private float convertidorFloat(String texto){
        return Float.parseFloat(convertidorTexto(texto));
    }
    // Lo que hacen estos metodos es leer un String que asumimos que seran datos correctos en su caso, eliminar espacios inadecuados que existan y
    // transformarlos a un tipo de dato correspondiente, en este caso puede ser Float o Int, para el metodo convertidorTexto, muestra un texto correspondiente
    // ademas de eso hace una lectura de parte del usuario.
}
