/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto1;

/**
 *
 * @author kevin
 */
public class Hoja {
    int no;
    String transicion;
    String siguiente;

    public Hoja(int no, String transicion) {
        this.no = no;
        this.transicion = transicion;
    }
   
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTransicion() {
        return transicion;
    }

    public void setTransicion(String transicion) {
        this.transicion = transicion;
    }

    public String getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public void imprimir(){
        if(siguiente != null){
            System.out.println(">>> Para el nodo " + no + " los siguientes son: " + siguiente);
        }else{
            System.out.println(">>> Para el nodo " + no + " ,Estado Final");
        }
   
    }
}
