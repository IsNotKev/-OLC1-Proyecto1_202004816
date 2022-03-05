/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto1; 

import ExpresionRegular.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    public static int contador = 0;
    public static String salida;
    
    public static void main(String[] args) {
        Inicio in = new Inicio();
        in.setVisible(true);
        
    }
    
    public static void obtenerConjuntos(){
        //System.out.println("-----------"+conjuntos.size()+"------------");
        for(int i = 0; i< conjuntos.size();i++){
            //System.out.println("------ " + conjuntos.get(i).getExp() );
            if(conjuntos.get(i).getExp().contains("~")){
                String s = (conjuntos.get(i).getExp().split("~"))[0];
                String s2 = (conjuntos.get(i).getExp().split("~"))[1];
                byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
                byte[] bytes2 = s2.getBytes(StandardCharsets.US_ASCII);
                //System.out.println(bytes[0] + "~" + bytes2[0] + "******Suma ="+(bytes[0]+bytes2[0]));

                for(int j = bytes[0]; j<=bytes2[0];j++){
                    //System.out.println((char)j);
                    conjuntos.get(i).addCaracter((char)j);
                }
            }else{
                String[] caracteres = conjuntos.get(i).getExp().split(",");
                for(int j = 0;j<caracteres.length;j++){
                   // System.out.println(caracteres[j]);
                    conjuntos.get(i).addCaracter(caracteres[j].charAt(0));
                }
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
                        salida += ">>>  Entrada: " + lex[1] + " Válida Con Expresión: " + entradas.get(i).getExp() + ".\n";
                    }else{
                        System.out.println("Entrada: " + lex[1] + " NO Válida Con Expresión: " + entradas.get(i).getExp());
                        salida += ">>>  Entrada: " + lex[1] + " NO Válida Con Expresión: " + entradas.get(i).getExp() + ".\n";
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
                    return true;
                default:
                    System.out.println("No es nada");
                    return false;
            }
        }
    }
    
    public static void graficar(){
        for(int i = 0; i<expresiones.size();i++){
            Object g = expresiones.get(i).getExpresion();
            String resultado = "digraph G{\nlabel=\""+expresiones.get(i).getId()+"_Thomson\";\nnode [shape=circle];\nrankdir=\"LR\";inicio[shape=point]\ninicio->S0[taillabel=\"INICIO\"];\n";
            ArrayList<String> aux = graficarEstructuraThomson(g);
            escribirDot(expresiones.get(i).getId()+"_Thomson",resultado + aux.get(2) + "S"+(contador-1)+"[shape=doublecircle];\n}");
            graficarImagen(expresiones.get(i).getId()+"_Thomson");
            contador = 0;
            
            Expresion nueva = new Expresion(".",g,new Dato("cadena","\"#\""));
            String resultado2 = "digraph G{\nlabel=\""+expresiones.get(i).getId()+"_Arbol\";\nnode [shape=circle];\nrankdir=\"TB\";\n";
            ArrayList<String> aux2 = graficarMetodoDelArbol(nueva);
            escribirDot(expresiones.get(i).getId()+"_Arbol",resultado2 + aux2.get(1)+"\n}");
            graficarImagen(expresiones.get(i).getId()+"_Arbol");
            contador = 0;
        }
        
        salida = ">>>   Grafos creados exitosamente.\n";
    }
    
    public static ArrayList<String> graficarEstructuraThomson(Object expresionActual){
        ArrayList<String> respuesta = new ArrayList<String>();
        
        if(expresionActual.getClass().getName().equals("ExpresionRegular.Dato")){
            Dato data = (Dato)expresionActual;
            if(data.getTipo().equals("id")){
                respuesta.add("S"+contador);
                respuesta.add("S" + contador);
                respuesta.add("S"+contador+"[label=\"S"+contador+"\"];\n");
                respuesta.add(data.getLex());
                contador += 1;          
            }else{
                respuesta.add("S"+contador);
                respuesta.add("S" + contador);
                respuesta.add("S"+contador+"[label=\"S"+contador+"\"];\n");
                respuesta.add(data.getLex().split("\"")[1]);
                contador += 1; 
            }
            
        }else{
            Expresion exp = (Expresion)expresionActual;
            String conexiones = "";
            String nodos = "";
            String aux;
            ArrayList<String> first;
            ArrayList<String> next;
            switch (exp.getOperador()) {
                case ".":
                    aux = "S"+contador;
                    nodos += aux+"[label=\"S"+contador+"\"];\n";
                    contador += 1;
                    
                    first = graficarEstructuraThomson(exp.getPrimero());
                    nodos += first.get(2);
                    next = graficarEstructuraThomson(exp.getSiguiente());
                    nodos += next.get(2);
                    
                    if(!first.get(3).equals("")){
                        conexiones += aux +"->"+first.get(0)+"[taillabel=\""+first.get(3)+"\"];\n";
                    }else{
                        conexiones += aux +"->"+first.get(0)+"[taillabel=\"ε\"];\n";
                    }
                    
                    if(!next.get(3).equals("")){
                        conexiones += first.get(1) +"->"+next.get(0)+"[taillabel=\""+next.get(3)+"\"];\n";
                    }else{
                        conexiones += first.get(1) +"->"+next.get(0)+"[taillabel=\"ε\"];\n";
                    }
                   
                    respuesta.add(aux);
                    respuesta.add(next.get(1));
                    respuesta.add(nodos + conexiones);
                    respuesta.add("");
                    
                    break;
                case "|":         
                    aux = "S"+(contador);
                    String s = "S"+(contador+1);
                    String aux2 = "S"+(contador+2);
                    nodos += "S"+contador+"[label=\"S"+contador+"\"];\n" + "S"+(contador+1)+"[label=\"S"+(contador+1)+"\"];\n" +"S"+(contador+2)+"[label=\"S"+(contador+2)+"\"];\n";
                    
                    
                    conexiones += "S"+contador+"->S"+(contador+1)+"[taillabel=\"ε\"];\n";
                    conexiones += "S"+contador+"->S"+(contador+2)+"[taillabel=\"ε\"];\n";
                            
                    contador += 3;
                    
                    
                    first = graficarEstructuraThomson(exp.getPrimero());
                    nodos += first.get(2);
                    next = graficarEstructuraThomson(exp.getSiguiente());
                    nodos += next.get(2);
                    
                    if(!first.get(3).equals("")){
                        conexiones += s +"->"+first.get(0)+"[taillabel=\""+first.get(3)+"\"];\n";
                    }else{
                        conexiones += s +"->"+first.get(0)+"[taillabel=\"ε\"];\n";
                    }
                    
                    if(!next.get(3).equals("")){
                        conexiones += aux2 +"->"+next.get(0)+"[taillabel=\""+next.get(3)+"\"];\n";
                    }else{
                        conexiones += aux2 +"->"+next.get(0)+"[taillabel=\"ε\"];\n";
                    }
                   
                    nodos +="S"+contador+"[label=\"S"+contador+"\"];\n";
                    contador +=1;
                    
                    conexiones += first.get(1) +"->S"+(contador-1)+"[taillabel=\"ε\"];\n";
                    conexiones += next.get(1) +"->S"+(contador-1)+"[taillabel=\"ε\"];\n";
                    
                    respuesta.add(aux);
                    respuesta.add("S"+(contador-1));
                    respuesta.add(nodos + conexiones);
                    respuesta.add("");
                    
                    break;
                case "*":
                    aux = "S"+contador;
                    String x = "S"+(contador+1);
                    nodos += aux+"[label=\"S"+contador+"\"];\n"+"S"+(contador+1)+"[label=\"S"+(contador+1)+"\"];\n";
                    
                    conexiones += "S"+contador+"->S"+(contador+1)+"[taillabel=\"ε\"];\n";
                    
                    contador += 2;
                                                           
                    first = graficarEstructuraThomson(exp.getPrimero());
                    nodos += first.get(2);
                    
                    if(!first.get(3).equals("")){
                        conexiones += x +"->"+first.get(0)+"[taillabel=\""+first.get(3)+"\"];\n";
                    }else{
                        conexiones += x +"->"+first.get(0)+"[taillabel=\"ε\"];\n";
                    }
                    
                    nodos += "S"+contador+"[label = \"S"+contador+"\"];";
                    conexiones += first.get(1)+"->"+"S"+contador+"[taillabel=\"ε\"];\n";
                    conexiones += "S"+contador+"->"+x+"[taillabel=\"ε\"];\n";
                    conexiones += aux+"->"+"S"+contador+"[taillabel=\"ε\"];\n";
                    
                    contador +=1;
                    
                    respuesta.add(aux);
                    respuesta.add("S"+(contador-1));
                    respuesta.add(nodos + conexiones);
                    respuesta.add("");
                    
                    break;
                case "+":
                    aux = "S"+contador;
                    String x1 = "S"+(contador+1);
                    nodos += aux+"[label=\"S"+contador+"\"];\n"+"S"+(contador+1)+"[label=\"S"+(contador+1)+"\"];\n";
                    
                    conexiones += "S"+contador+"->S"+(contador+1)+"[taillabel=\"ε\"];\n";
                    
                    contador += 2;
                                                           
                    first = graficarEstructuraThomson(exp.getPrimero());
                    nodos += first.get(2);
                    
                    if(!first.get(3).equals("")){
                        conexiones += x1 +"->"+first.get(0)+"[taillabel=\""+first.get(3)+"\"];\n";
                    }else{
                        conexiones += x1 +"->"+first.get(0)+"[taillabel=\"ε\"];\n";
                    }
                    
                    nodos += "S"+contador+"[label = \"S"+contador+"\"];";
                    conexiones += first.get(1)+"->"+"S"+contador+"[taillabel=\"ε\"];\n";
                    conexiones += "S"+contador+"->"+x1+"[taillabel=\"ε\"];\n";
                   
                    contador +=1;
                    
                    respuesta.add(aux);
                    respuesta.add("S"+(contador-1));
                    respuesta.add(nodos + conexiones);
                    respuesta.add("");
                    break;
                case "?":
                    aux = "S"+contador;
                    String x2 = "S"+(contador+1);
                    nodos += aux+"[label=\"S"+contador+"\"];\n"+"S"+(contador+1)+"[label=\"S"+(contador+1)+"\"];\n";
                    
                    conexiones += "S"+contador+"->S"+(contador+1)+"[taillabel=\"ε\"];\n";
                    
                    contador += 2;
                                                           
                    first = graficarEstructuraThomson(exp.getPrimero());
                    nodos += first.get(2);
                    
                    if(!first.get(3).equals("")){
                        conexiones += x2 +"->"+first.get(0)+"[taillabel=\""+first.get(3)+"\"];\n";
                    }else{
                        conexiones += x2 +"->"+first.get(0)+"[taillabel=\"ε\"];\n";
                    }
                    
                    nodos += "S"+contador+"[label = \"S"+contador+"\"];";
                    conexiones += first.get(1)+"->"+"S"+contador+"[taillabel=\"ε\"];\n";
                    conexiones += aux+"->"+"S"+contador+"[taillabel=\"ε\"];\n";
                   
                    contador +=1;
                    
                    respuesta.add(aux);
                    respuesta.add("S"+(contador-1));
                    respuesta.add(nodos + conexiones);
                    respuesta.add("");
                    break;
                default:
                    throw new AssertionError();
            }
        }
        
        
        return respuesta;
    }
    
    public static void escribirDot(String title, String resultado){
        try {
            String ruta = System.getProperty("user.dir") + "\\"+title+".txt";
            File file = new File(ruta);
            
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(resultado);
            bw.close(); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void graficarImagen(String title){ 
       try {
           
           String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
           String fileInputPath = System.getProperty("user.dir") + "\\"+title+".txt";
           String fileOutputPath = System.getProperty("user.dir") + "\\"+title+".jpg";;

           String tParam = "-Tjpg";
           String tOParam = "-o";
           
           String[] cmd = new String[5];
           cmd[0] = dotPath;
           cmd[1] = tParam;
           cmd[2] = fileInputPath;
           cmd[3] = tOParam;
           cmd[4] = fileOutputPath;

           Runtime rt = Runtime.getRuntime();

           rt.exec( cmd );
           
           //System.out.println("Graficado");
           
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    
    public static ArrayList<String> graficarMetodoDelArbol(Object expresionActual){
        ArrayList<String> respuesta = new ArrayList<String>();
        
        if(expresionActual.getClass().getName().equals("ExpresionRegular.Dato")){
            Dato data = (Dato)expresionActual;
            if(data.getTipo().equals("id")){
                respuesta.add("S"+contador);
                respuesta.add("S"+contador+"[label=\""+data.getLex()+"\"];\n");
                contador += 1;          
            }else{
                respuesta.add("S"+contador);
                respuesta.add("S"+contador+"[label=\""+data.getLex().split("\"")[1]+"\"];\n");
                contador += 1;
            }           
        }else{
            Expresion exp = (Expresion)expresionActual;
            String conexiones = "";
            String nodos = "";
            String aux;
            ArrayList<String> first;
            ArrayList<String> next;
            
            switch(exp.getOperador()){
                case ".":
                    aux = "S"+contador;
                    contador += 1;
                    nodos += aux + "[label = \".\"];\n";
                    first = graficarMetodoDelArbol(exp.getPrimero());
                    next = graficarMetodoDelArbol(exp.getSiguiente());
                    conexiones += aux + "->" + first.get(0) + ";\n";
                    conexiones += aux + "->" + next.get(0)+ ";\n";
                    
                    respuesta.add(aux);
                    respuesta.add(nodos + first.get(1) + next.get(1) + conexiones);
                    
                    break;
                case "|":
                    aux = "S"+contador;
                    contador += 1;
                    nodos += aux + "[label = \"|\"];\n";
                    first = graficarMetodoDelArbol(exp.getPrimero());
                    next = graficarMetodoDelArbol(exp.getSiguiente());
                    conexiones += aux + "->" + first.get(0)+ ";\n";
                    conexiones += aux + "->" + next.get(0)+ ";\n";
                    
                    respuesta.add(aux);
                    respuesta.add(nodos + first.get(1) + next.get(1) + conexiones);
                    break;
                case "*":
                    aux = "S"+contador;
                    contador += 1;
                    nodos += aux + "[label = \"*\"];\n";
                    first = graficarMetodoDelArbol(exp.getPrimero());
                    conexiones += aux + "->" + first.get(0)+ ";\n";
                    
                    respuesta.add(aux);
                    respuesta.add(nodos + first.get(1)+ conexiones);
                    break;
                case "+":
                    aux = "S"+contador;
                    contador += 1;
                    nodos += aux + "[label = \"+\"];\n";
                    first = graficarMetodoDelArbol(exp.getPrimero());
                    conexiones += aux + "->" + first.get(0)+ ";\n";
                    
                    respuesta.add(aux);
                    respuesta.add(nodos + first.get(1)+ conexiones);
                    break;
                case "?":
                    aux = "S"+contador;
                    contador += 1;
                    nodos += aux + "[label = \"?\"];\n";
                    first = graficarMetodoDelArbol(exp.getPrimero());
                    conexiones += aux + "->" + first.get(0)+ ";\n";
                    
                    respuesta.add(aux);
                    respuesta.add(nodos + first.get(1)+ conexiones);
                    break;
            }
        }
        
        
        return respuesta;       
    }
}
