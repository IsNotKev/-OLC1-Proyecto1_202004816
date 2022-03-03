/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;
import Error_.*;
import ExpresionRegular.*;
import java.io.BufferedReader;
import java.io.StringReader;
/**
 *
 * @author kevin
 */
public class AnalizadorLenguaje {
    public static AnalizadorLenguaje analizador;
    public static LinkedListError errores;
    public static LinkedListExpresion expresiones;
            
    public void analize(String text){
	try {
            System.out.println("Inicio de analisis");
            errores = new LinkedListError();
            expresiones = new LinkedListExpresion();
            Scanner scanner = new Scanner(new BufferedReader(new StringReader(text)));
            Parser parser = new Parser(scanner);
            parser.parse();
            System.out.println("Fin de analisis");
	} catch (Exception e) {
	}
    }
	
    public static AnalizadorLenguaje getInstance(){
	if(analizador == null){
            errores = new LinkedListError();
            analizador = new AnalizadorLenguaje();
            expresiones = new LinkedListExpresion();
	}
	return analizador;
    }

    public static LinkedListError getErrores(){
	return errores;
    }
	
    public static void  setErrores(LinkedListError list){
	AnalizadorLenguaje.errores = list;
    }

    public static LinkedListExpresion getExpresiones() {
        return expresiones;
    }

    public static void setExpresiones(LinkedListExpresion expresiones) {
        AnalizadorLenguaje.expresiones = expresiones;
    }
    
    public static void LimpiarInstancia() {
        if (analizador != null) {
            errores.clear();
            expresiones.clear();
        } else {
            System.out.println("No existe un analizador");
        }
    }
    
}
