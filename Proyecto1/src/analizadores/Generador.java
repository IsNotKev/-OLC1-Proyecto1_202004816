/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;

/**
 *
 * @author kevin
 */
public class Generador {
    public static void main(String[] args) {
        try {
            
            String ruta = "src/analizadores/";
            String opcFlex[] = {ruta+"lex.jflex","-d",ruta};
            jflex.Main.generate(opcFlex);
			
            String opcCup[]={"-destdir",ruta, "-parser","Parser",ruta+"parser.cup"};
            java_cup.Main.main(opcCup);
            
        } catch (Exception e) {
        }
    }
}
