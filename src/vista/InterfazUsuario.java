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

    public void crearRegion() {
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

    public void crearComuna() {//publico por avance 3
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

    public void crearEstacionMeteorologica() {
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

    public void listarRegiones() {
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

    public void listarComunas() {
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

    public void listarEstaciones() {
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

    public void listarSensores(){
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

    public void listarMediciones(){
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

    public void listarResumenMediciones(){
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

