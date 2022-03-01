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
public class LexemaEntrada {
    String lexema;
    String exp;

    public LexemaEntrada(String lexema, String exp) {
        this.lexema = lexema;
        this.exp = exp;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
    
    
}
