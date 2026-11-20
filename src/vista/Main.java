package vista;

import controlador.MeteorologiaException;

public class Main {
    public static void main(String[] args) throws MeteorologiaException {
        InterfazUsuario interfaz = InterfazUsuario.getInstance(); // Se corrigio el error de que main solo imprimia hello world, ahora se
        interfaz.menuPrincipal(); // hace la instancia de InterfazUsuario y ademas de eso se llama a su metodo menuPrincipal
    }
    //RAMA MAIN Y DEVELOP SON OBLIGATORIAS EN EL GITHUB (PUBLICAS) SIEMPRE HAY QUE HACER MERGE
    //EL PROPOSITO DEL MAIN ES PARA LA ENTREGA FORMAL DE LA EVALUACION
    //RAMA DEVELOP: ES CODIGO OPERATIVON DE DESARROLLO: PROBADO, FUNCIONANDO Y SIN ERRORES DE COMPILACION
    //LAS RAMAS FEATURES SON PERSONALES HASTA QUE SE SUBAN FUNCIONANDO Y OPERATIVAS PARA EL TRABAJO SIN ERRORES Y SEAN NECESARIAS PARA EL CODIGO FINAL. (DEVELOP)
    //FEATURE/LOGIN: ESPACIO PERSONAL DE TRABAJO (IDEAS, PRUEBAS Y ERRORES) HASTA QUE FUNCIONE. AL SUBIRLAS UNA VEZ FUNCIONEN, SE SUBEN AL DEVELOP.

}
