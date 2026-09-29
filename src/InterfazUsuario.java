import java.util.*;
import java.time.*;
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

    }

    private void registrarMedicion() {

    }

    private void menuListados() {

    }

    private void listarRegiones() {

    }

    private void listarComunas() {

    }

    private void listarEstaciones() {

    }

    private void listarSensores() {

    }

    private void listarMediciones() {

    }



    /* Abajo iran metodos privados para hacer mas eficiente el codigo, ya que me estoy dando cuenta
     que se esta repitiendo mucho codigo y puede afectar negativamente la ejecucion del programa o
     la lectura de este mismo*/

    private int convertidorInt(String texto){ // convierte la funcion convertidorTexto a Int
        return Integer.parseInt(convertidorTexto(texto));
    }


    private String convertidorTexto(String texto){ // Imprime el texto y ademas retorna una lectura que elimina los espacios
        System.out.println(texto); //
        return sc.nextLine().trim();
    }

    private float convertidorFloat(String texto){
        return Float.parseFloat(convertidorTexto(texto));
    }
}
