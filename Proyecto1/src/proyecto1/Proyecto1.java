/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto1; 

import ExpresionRegular.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author kevin
 */
public class Proyecto1 {

    public static String ruta = "";
    public static ArrayList<Conjunto> conjuntos = new ArrayList<Conjunto>();
    public static ArrayList<ExpresionRegular> expresiones = new ArrayList<ExpresionRegular>();
    public static ArrayList<LexemaEntrada> entradas = new ArrayList<LexemaEntrada>();
    
    public static void main(String[] args) {
        Inicio in = new Inicio();
        in.setVisible(true);
        
    }
    
    public static void obtenerConjuntos(){
        //System.out.println("-----------"+conjuntos.size()+"------------");
        for(int i = 0; i< conjuntos.size();i++){
            //System.out.println("------ " + conjuntos.get(i).getExp() );
            String s = (conjuntos.get(i).getExp().split("~"))[0];
            String s2 = (conjuntos.get(i).getExp().split("~"))[1];
            byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
            byte[] bytes2 = s2.getBytes(StandardCharsets.US_ASCII);
            //System.out.println(bytes[0] + "~" + bytes2[0] + "******Suma ="+(bytes[0]+bytes2[0]));
            
            for(int j = bytes[0]; j<=bytes2[0];j++){
                System.out.println((char)j);
                conjuntos.get(i).addCaracter((char)j);
            }
        }
        //System.out.println("Conjunto Creado");
    }
    
    public static void verificarEntradas(){
        for(int i = 0 ; i<entradas.size();i++){
            for(int j = 0; j<expresiones.size();j++){
                if(entradas.get(i).getExp().equals(expresiones.get(j).getId())){
                    //System.out.println("Entrada: " + entradas.get(i).getLexema() + " - Con Expresion: " + expresiones.get(j).getId());
                    String[] lex = entradas.get(i).getLexema().split("\"");
                    if(verificar(expresiones.get(j).getExpresion(),lex[1])){
                        System.out.println("Entrada: " + lex[1] + " Válida Con Expresión: " + entradas.get(i).getExp());
                    }else{
                        System.out.println("Entrada: " + lex[1] + " NO Válida Con Expresión: " + entradas.get(i).getExp());
                    }
                }
            }
        }
    }

    public static boolean verificar(Object exp, String lexema){ 
        String clase = exp.getClass().getName();
        //System.out.println(clase);
        System.out.println(lexema);
        if(clase.equals("ExpresionRegular.Dato")){                     
            if(!lexema.equals("")){
                char c = lexema.charAt(0);                  
                Dato data = (Dato)exp;
                if(data.getTipo().equals("cadena")){
                    //return c.equals(data.getLex().split("\"")[1]);
                    char cadena = (data.getLex().split("\"")[1]).charAt(0);
                    //System.out.println(cadena);
                    //System.out.println(Character.compare(c, cadena) == 0);
                   
                    return (Character.compare(c, cadena) == 0);
                }else{
                    for(int i = 0; i<conjuntos.size();i++){
                        if(conjuntos.get(i).getId().equals(data.getLex())){
                            for(int j = 0; j<conjuntos.get(i).getCaracteres().size();j++){
                                //if(c.equals(conjuntos.get(i).getCaracteres().get(j))){
                                char cadena = (char) (conjuntos.get(i).getCaracteres().get(j));
                                if(Character.compare(c, cadena) == 0){
                                    //System.out.println("Si es " + conjuntos.get(i).getId());
                                    //System.out.println(c + "=" + cadena);
                                    
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
            return false;
        }else{
            Expresion expresionactual = (Expresion)exp;
            switch (expresionactual.getOperador()) {               
                case ".":
                    
                    //System.out.println("Estoy en .");
                    if(verificar(expresionactual.getPrimero(),lexema)){
                        if(lexema.length()>1){
                            lexema = lexema.substring(1);
                        }else{
                            lexema = "";
                        }    
                        
                        return(true && verificar(expresionactual.getSiguiente(),lexema));
                    }else{
                        return false;
                    }             
                case "|":
                    //System.out.println("Estoy en |");
                    if(verificar(expresionactual.getPrimero(),lexema)){
                        if(lexema.length()>1){
                            lexema = lexema.substring(1);
                        }else{
                            lexema = "";
                        }  
                        
                        return true;
                    }else{
                        return(false || verificar(expresionactual.getSiguiente(),lexema));
                    }
                case "+":
                    //System.out.println("Estoy en +");
                    if(verificar(expresionactual.getPrimero(),lexema)){
                        if(lexema.length()>1){
                            lexema = lexema.substring(1);
                        }else{
                            lexema = "";
                        }   
                        
                        while(verificar(expresionactual.getPrimero(),lexema)){
                            if(lexema.length()>1){
                                lexema = lexema.substring(1);
                            }else{
                                lexema = "";
                            }  
                            
                        }
                        return true;
                    }else{
                        return false;
                    }
                case "*":
                    //System.out.println("Estoy en *");
                    while(verificar(expresionactual.getPrimero(),lexema)){
                        if(lexema.length()>1){
                            lexema = lexema.substring(1);
                        }else{
                            lexema = "";
                        }  
                        
                    }
                    return(true);
                case "?":
                    //System.out.println("Estoy en ?");
                    if(verificar(expresionactual.getSiguiente(),lexema)){
                        if(lexema.length()>1){
                            lexema = lexema.substring(1);
                        }else{
                            lexema = "";
                        } 
                       
                    }
                    return(true);
                default:
                    return false;
            }
        }
    }
    
    
}
