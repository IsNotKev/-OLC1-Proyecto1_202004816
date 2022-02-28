/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadores;
import Error_.*;
/**
 *
 * @author kevin
 */
public class AnalizadorLenguaje {
    public static AnalizadorLenguaje analizador;
    public static LinkedListError list;
            
    public void analize(String text){
	try {
            System.out.println("Inicio de analisis");
            list = new LinkedListError();
            //Scanner scanner = new Scanner(new BufferedReader(new StringReader(text)));
            //Parser parser = new Parser(scanner);
            //parser.parse();
            System.out.println("Fin de analisis");
	} catch (Exception e) {
	}
    }
	
    public static AnalizadorLenguaje getInstance(){
	if(analizador == null){
            list = new LinkedListError();
            analizador = new AnalizadorLenguaje();
	}
	return analizador;
    }
	
    public static LinkedListError getList(){
	return list;
    }
	
    public static void  setList(LinkedListError list){
	analizador.list = list;
    }
}
