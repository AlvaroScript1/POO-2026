package controlador;
import java.io.*;

public class PersistenciaObjetos {
    public static final String CARPETA = "datos";

    private PersistenciaObjetos(){}

    public static void escribeObjeto(String nombreArchivo, Object objeto) throws MeteorologiaException{
        File carpeta = new File(CARPETA);
        if(!carpeta.exists()){
            carpeta.mkdirs();
        }
        File arch = new File(carpeta, nombreArchivo);
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arch))){
            out.writeObject(objeto);
        }catch(IOException exc){
            throw new MeteorologiaException("No fue posible escribir el archivo "+ nombreArchivo);
        }
    }

    public static Object leeObjeto(String nombreArchivo) throws MeteorologiaException{
        File arch = new File(CARPETA, nombreArchivo);
        if(!arch.exists()){
            throw new MeteorologiaException("No se encontro el archivo "+nombreArchivo);
        }
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(arch))){
            return out.readObject();
        }catch(IOException | ClassNotFoundException exc){
            throw new MeteorologiaException("No fue posible leer el archivo "+ nombreArchivo);
        }
    }
}
