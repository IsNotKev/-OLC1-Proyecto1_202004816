/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExpresionRegular;

import java.util.ArrayList;

/**
 *
 * @author kevin
 */
public class Conjunto {
    String exp;
    String id;
    ArrayList caracteres;

    public Conjunto(String id, String exp) {
        this.id = id;
        this.exp = exp;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public ArrayList getCaracteres() {
        return caracteres;
    }

    public void setCaracteres(ArrayList caracteres) {
        this.caracteres = caracteres;
    }
    
    
    
}
