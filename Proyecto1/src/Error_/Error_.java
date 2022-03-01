/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Error_;

/**
 *
 * @author kevin
 */
public class Error_ {
    private String message;
    private String type;
    private int fila;
    private int columna;

    
    
    public Error_(String message, String type, int fila, int columna) {
        this.message = message;
        this.type = type;
        this.fila = fila;
        this.columna = columna;
        
    }
    
    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
       

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }
    
    
}
