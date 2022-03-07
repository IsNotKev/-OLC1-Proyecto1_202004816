/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto1;

import java.util.ArrayList;

/**
 *
 * @author kevin
 */
public class Estado {
    String nombre;
    ArrayList<Integer> hojas;
    ArrayList<Transicion> transiciones;
    
    public class Transicion{
        String estado;
        String caracter;
        public Transicion(String estado, String caracter){
            this.caracter = caracter;
            this.estado = estado;
        }
    }

    public Estado(String nombre, ArrayList<Integer> hojas) {
        this.nombre = nombre;
        this.hojas = hojas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Integer> getHojas() {
        return hojas;
    }

    public void setHojas(ArrayList<Integer> hojas) {
        this.hojas = hojas;
    }

    public ArrayList<Transicion> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }
    
    
    
}
