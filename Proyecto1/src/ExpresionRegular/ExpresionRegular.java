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
public class ExpresionRegular {
    String id;
    String expresion;

    public ExpresionRegular(String id, String expresion) {
        this.id = id;
        this.expresion = expresion;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }
    
    
    
}
