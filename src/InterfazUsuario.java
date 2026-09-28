import java.util.*;
import java.time.*;
public class InterfazUsuario {
    private Scanner sc = new Scanner(System.in);
    private InstitutoMeteorologia instituto; // Asociacion con InstitutoMetereologia
    public static void main() {
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
            System.out.println("Opcion: ");
            opciones = sc.nextInt();
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

    }

    private void crearComuna() {

    }

    private void crearEstacionMeteorologica() {

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
}
