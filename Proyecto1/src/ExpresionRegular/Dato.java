/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExpresionRegular;

/**
 *
 * @author kevin
 */
public class Dato {
    String tipo;
    String lex;

    public Dato(String tipo, String lex) {
        this.tipo = tipo;
        this.lex = lex;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }
    
    
}
