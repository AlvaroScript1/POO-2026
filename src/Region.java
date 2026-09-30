import java.util.ArrayList;

public class Region {
    private int codigo;
    private String nombre;

    //array list auxiliar
    private ArrayList<Comuna> comunas = new ArrayList<>();

    //constructor
    public Region(int cod, String nom){
        this.codigo = cod;
        this.nombre = nom;
    }
    public int getCodigo(){
        return codigo;
    }
    public String getNombre(){return nombre;}

    public boolean addComuna(int cod, String nom){
        for(Comuna c : comunas){
            if(cod == c.getCodigo()){
                return false;
            }
        }
        Comuna nuevaComuna = new Comuna(cod, nom, this);
        return comunas.add(nuevaComuna);
    }
    public Comuna findComunaByid(int cod) {
        for (Comuna c : comunas) {
            if (cod == c.getCodigo()) {
                return c; //"c" es el parametro de la Comuna.
            }
        }
        return null;
    }
    public Comuna[] getComunas(){
        Comuna[] arregloComunas = new Comuna[comunas.size()];
        for(int i = 0; i < comunas.size(); i++){
            arregloComunas[i] = comunas.get(i);
        }
        return arregloComunas;
    }
    public int getCantidadEstaciones(){
        int cont = 0;
        for(int i = 0; i < comunas.size(); i++){
            cont += comunas.get(i).getCantidadEstaciones();
        }
        return cont;
    }

    //metodos auxiliares (son metodos a parte de los que establece el UML)
    public int getCantidadComunas(){
        int cont = 0;
        for(Comuna c : comunas){
            cont++;
        }
        return cont;
    }
}
