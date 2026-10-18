package modelo;

import java.util.ArrayList;
import java.util.Optional;

public class Region {
    private int codigo;
    private String nombre;

    //array list auxiliar
    private ArrayList<Comuna> comunas;

    //constructor
    public Region(int cod, String nom){
        this.codigo = cod;
        this.nombre = nom;
        this.comunas = new ArrayList<>(); //Se crea dentro la lista de comunas para cumplir con la composicion
    }
    public int getCodigo(){
        return codigo;
    }
    public String getNombre(){
        return nombre;
    }

    public void addComuna(int cod, String nom) throws MeteorologiaException{ // Avisamos que el metodo puede arrojar una excepcion
        for(Comuna c : comunas){
            if(cod == c.getCodigo()){ // si el codigo es igual, significa que existe una comuna con igual codigo pero distinto nombre
                throw new MeteorologiaException("La región <"+this.nombre+"> ya tiene una comuna con el código <"+cod+">"); // Arrojamos excepcion si la region tiene una comuna con el mismo codigo
            }else if(c.getNombre().equalsIgnoreCase(nom)){ // si el nombre es igual, significa que existe una comuna con igual nombre pero distinto codigo
                throw new MeteorologiaException("La región <"+this.nombre+"> ya tiene una comuna con el nombre <"+nom+">"); // Arrojamos excepcion si la region tiene una comuna con el mismo nombre
            }
        }
        Comuna nuevaComuna = new Comuna(cod, nom, this);
        comunas.add(nuevaComuna);
    }
    public Optional<Comuna> findComunaById(int cod){ // Implementamos formato Optional para que solamente retorne un optional del objeto o optional vacio
        for(Comuna c : comunas){
            if(cod == c.getCodigo()){
                return Optional.of(c);
            }
        }
        return Optional.empty();
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
}
