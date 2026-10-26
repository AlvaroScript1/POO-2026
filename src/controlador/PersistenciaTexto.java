package controlador;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class PersistenciaTexto {
    private static PersistenciaTexto instance = null;
    private static final String SEPARADOR = ";"; // Aqui usamos el separador como constante final para que no se pueda modificar

    private PersistenciaTexto(){}

    public PersistenciaTexto getInstance(){
        if(instance == null){
            instance = new PersistenciaTexto(); // Patron singleton
        }
        return instance;
    }

    public static String[] leeRegistros(String nombreArchivo) throws MeteorologiaException{
        ArrayList<String> registros = new ArrayList<>(); // Cree un ArrayList de tipo String para ir añadiendo los datos sin importarme el tamaño, al final lo pasare a un arreglo
        try(Scanner sc = new Scanner(new File(nombreArchivo)).useDelimiter(SEPARADOR)){ // Usamos try-catch para crear el scanner para que lea archivos y capturar la excepcion si esta vacio el archivo o no se lee
            while(sc.hasNext()){
                String lectra = sc.next().trim(); // Vamos leyendo por tokens en esta ocasion, el trim quita espacios si es que hay
                if(lectra.isEmpty() == false){
                    registros.add(lectra);
                }
            }
        }catch(FileNotFoundException exc){
            throw new MeteorologiaException("El archivo no existe o no puede leerse"); // Lanzamos la excepcion personalizada si hay un FileNotFoundException
        }catch(IOException exc){
            throw new MeteorologiaException("No fue posible leer el archivo <"+nombreArchivo+">");
        }
        return registros.toArray(new String[0]);
    }

    public static void escribeRegistros(String nombreArchivo, String[] lineas) throws MeteorologiaException{ // la verdad en el uml y anexo no se especifican bien en el encabezado del metodo
        File archivo = new File("datos");
        if(!archivo.exists()){
            archivo.mkdir(); //Creamos la carpeta llamada datos si es que no existe originalmente
        }
        File arch = new File(archivo, nombreArchivo);
        try(PrintStream redacter = new PrintStream(arch)){ // hacemos un prinstream para que pueda escribir en el archivo arch
            for(String l : lineas){ // for-each para recorrer el arreglo String[] lineas
              redacter.println(l); // Esto escribe el contenido del arreglo l dentro del archivo de destino que en este caso es arch y esta conectado con redacter
            }
        }catch(IOException exc){ // lanzamos la excepcion si no podemos escribir dentro del archivo
            throw new MeteorologiaException("No fue posible escribir el archivo <"+nombreArchivo+">");
        }
    }
}
