package controlador;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

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
        }catch(FileNotFoundException e){
            throw new MeteorologiaException("El archivo no existe o no puede leerse"); // Lanzamos la excepcion personalizada si hay un FileNotFoundException
        }
        return registros.toArray(new String[0]);
    }


}
