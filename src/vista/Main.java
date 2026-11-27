package vista;

import controlador.MeteorologiaException;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true)); //Metofo lambda que llama a swingUtilities y que cuando el main se ejecute aparece lamda() y lambda NO recibe parametros creando un nuevo objeto ventana con visibilidad de tipo true para que aparezca.
    }
    //RAMA MAIN Y DEVELOP SON OBLIGATORIAS EN EL GITHUB (PUBLICAS) SIEMPRE HAY QUE HACER MERGE
    //EL PROPOSITO DEL MAIN ES PARA LA ENTREGA FORMAL DE LA EVALUACION
    //RAMA DEVELOP: ES CODIGO OPERATIVON DE DESARROLLO: PROBADO, FUNCIONANDO Y SIN ERRORES DE COMPILACION
    //LAS RAMAS FEATURES SON PERSONALES HASTA QUE SE SUBAN FUNCIONANDO Y OPERATIVAS PARA EL TRABAJO SIN ERRORES Y SEAN NECESARIAS PARA EL CODIGO FINAL. (DEVELOP)
    //FEATURE/LOGIN: ESPACIO PERSONAL DE TRABAJO (IDEAS, PRUEBAS Y ERRORES) HASTA QUE FUNCIONE. AL SUBIRLAS UNA VEZ FUNCIONEN, SE SUBEN AL DEVELOP.

}
