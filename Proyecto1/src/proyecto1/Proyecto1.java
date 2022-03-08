/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto1; 

import ExpresionRegular.*;
import Error_.*;
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
    public static ArrayList<Error_> errores = new ArrayList<Error_>();
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
                    if(bytes[0] < 65 || (bytes[0]>90 && bytes[0]<97) || bytes[0]>122){
                        if(j < 65 || (j>90 && j<97) || j>122){
                            //System.out.println((char)j);
                            conjuntos.get(i).addCaracter((char)j);
                        }
                    }else{
                        //System.out.println((char)j);
                        conjuntos.get(i).addCaracter((char)j);
                    }
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
        String salidajson = "[";
        for(int i = 0 ; i<entradas.size();i++){
            for(int j = 0; j<expresiones.size();j++){
                if(entradas.get(i).getExp().equals(expresiones.get(j).getId())){
                    //System.out.println("Entrada: " + entradas.get(i).getLexema() + " - Con Expresion: " + expresiones.get(j).getId());
                    String[] lex = entradas.get(i).getLexema().split("\"");
                    if(verificar(expresiones.get(j).getExpresion(),lex[1])){
                        //System.out.println("Entrada: " + lex[1] + " Válida Con Expresión: " + entradas.get(i).getExp());
                        salida += ">>>  Entrada: " + lex[1] + " Válida Con Expresión: " + entradas.get(i).getExp() + ".\n";
                        salidajson += "\n   {\n     \"Valor\":"+entradas.get(i).getLexema()+",\n"
                                + "     \"ExpresionRegular\":\""+entradas.get(i).getExp()+"\",\n"
                                + "     \"Resultado\":\"Cadena Válida\"\n   },";
                    }else{
                        //System.out.println("Entrada: " + lex[1] + " NO Válida Con Expresión: " + entradas.get(i).getExp());
                        salida += ">>>  Entrada: " + lex[1] + " NO Válida Con Expresión: " + entradas.get(i).getExp() + ".\n";
                        salidajson += "\n   {\n     \"Valor\":"+entradas.get(i).getLexema()+",\n"
                                + "     \"ExpresionRegular\":\""+entradas.get(i).getExp()+"\",\n"
                                + "     \"Resultado\":\"Cadena No Válida\"\n   },";
                    }
                }
            }
        }
        
        salidajson = salidajson.substring(0,salidajson.length()-1);
        salidajson += "\n]";
        escribirDot("Salida", salidajson, "json");
    }

    public static boolean verificar(Object exp, String lexema){ 
        String clase = exp.getClass().getName();
        //System.out.println(clase);
        //System.out.println(lexema);
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
            //METODO DE THOMPSON
            Object g = expresiones.get(i).getExpresion();
            String resultado = "digraph G{\nlabel=\""+expresiones.get(i).getId()+"_Thomson\";\nnode [shape=circle];\nrankdir=\"LR\";inicio[shape=point]\ninicio->S0[taillabel=\"INICIO\"];\n";
            ArrayList<String> aux = graficarEstructuraThomson(g);
            escribirDot(expresiones.get(i).getId()+"_Thomson",resultado + aux.get(2) + "S"+(contador-1)+"[shape=doublecircle];\n}","afnd");
            graficarImagen(expresiones.get(i).getId()+"_Thomson","afnd");
            contador = 0;
            
            //ARBOL
            Expresion nueva = new Expresion(".",g,new Dato("cadena","\"#\""));
            String resultado2 = "digraph G{\nlabel=\""+expresiones.get(i).getId()+"_Arbol\";\nnode [shape=circle];\nrankdir=\"TB\";\n";
            ArrayList<String> aux2 = graficarMetodoDelArbol(nueva);
            escribirDot(expresiones.get(i).getId()+"_Arbol",resultado2 + aux2.get(1)+"\n}","arbol");
            graficarImagen(expresiones.get(i).getId()+"_Arbol","arbol");
            contador = 0;
            
            //TABLA DE SIGUIENTES
            ArrayList<Object> aux3 = obtenerTablaDeSiguientes(nueva);
            ArrayList<Hoja> hojas = (ArrayList<Hoja>)aux3.get(2);           
            String resultado3="digraph G{\nlabel=\""+expresiones.get(i).getId()+"_TSiguiente\";\n",f1= "{No",f2= "{Hoja",f3 = "{Siguientes";                       
            //System.out.println("---------------"+expresiones.get(i).getId()+"------------------");
            for(int j = 0;j<hojas.size();j++){
                //hojas.get(j).imprimir();
                f1 += "|" + hojas.get(j).getNo();
                f2 += "|" + hojas.get(j).getTransicion();
                
                if(hojas.get(j).getSiguiente() != null){
                    f3 += "|" + hojas.get(j).getSiguiente();
                }else{
                    f3 += "| FINAL";
                }                             
            }           
            f1 += "}"; f2 += "}"; f3 += "}";
            resultado3 += "node [fontname=\"Arial\"];\n node_A [shape=record label=\""+f1+"|"+f2+"|"+f3+"\"];\n}";
            escribirDot(expresiones.get(i).getId()+"_TSiguiente",resultado3,"s");
            graficarImagen(expresiones.get(i).getId()+"_TSiguiente","s");
            contador = 0;           
            
            //AFD
            String resultado4 = "digraph G{\nlabel=\""+expresiones.get(i).getId()+"_AFD\";\nnode [shape=circle];\nrankdir=\"LR\";\n";
            ArrayList<String> aux4 = graficarAFD(g);
            
            String[] ultimos = aux4.get(1).split("\\,");
            String dd = "";
            for(int l = 0; l<ultimos.length;l++){
                dd+= ultimos[l] + "[shape=doublecircle];\n";
            }
            
            escribirDot(expresiones.get(i).getId()+"_AFD",resultado4 + aux4.get(2) + dd+"}","afd");
            graficarImagen(expresiones.get(i).getId()+"_AFD","afd");
            contador = 0;
            
            //Tabla de Transiciones
            String resultado5 = obtenerTablaTransiciones(expresiones.get(i).getId(), aux4.get(4));
            escribirDot(expresiones.get(i).getId()+"_TTransiciones", resultado5,"t");
            graficarImagen(expresiones.get(i).getId()+"_TTransiciones","t");
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
    
    public static void escribirDot(String title, String resultado, String type){
        try {
            String ruta;
            switch(type){
                case "afnd":
                    ruta = System.getProperty("user.dir") + "\\AFND_202004816\\"+title+".txt";
                    break;
                case "arbol":
                    ruta = System.getProperty("user.dir") + "\\ARBOLES_202004816\\"+title+".txt";
                    break;
                case "s":
                    ruta = System.getProperty("user.dir") + "\\SIGUIENTES_202004816\\"+title+".txt";
                    break;
                case "t":
                    ruta = System.getProperty("user.dir") + "\\TRANSICIONES_202004816\\"+title+".txt";
                    break;
                case "afd":
                    ruta = System.getProperty("user.dir") + "\\AFD_202004816\\"+title+".txt";
                    break;
                case "json":
                    ruta = System.getProperty("user.dir")+"\\SALIDAS_202004816\\"+title+".json";
                    break;
                case "html":
                    ruta = System.getProperty("user.dir")+"\\ERRORES_202004816\\"+title+".html";
                    break;
                default:
                    ruta = System.getProperty("user.dir") + "\\"+title+".txt";
                    break;
            }
                      
            File file = new File(ruta);
            
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(resultado);
            bw.close(); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void graficarImagen(String title, String type){ 
       try {
           
           String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
           String fileInputPath;
           String fileOutputPath;        
           
           switch(type){
                case "afnd":
                    fileInputPath = System.getProperty("user.dir") + "\\AFND_202004816\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\AFND_202004816\\"+title+".jpg"; 
                    break;
                case "arbol":
                    fileInputPath = System.getProperty("user.dir") + "\\ARBOLES_202004816\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\ARBOLES_202004816\\"+title+".jpg";
                    break;
                case "s":
                    fileInputPath = System.getProperty("user.dir") + "\\SIGUIENTES_202004816\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\SIGUIENTES_202004816\\"+title+".jpg";
                    break;
                case "t":
                    fileInputPath = System.getProperty("user.dir") + "\\TRANSICIONES_202004816\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\TRANSICIONES_202004816\\"+title+".jpg";
                    break;
                case "afd":
                    fileInputPath = System.getProperty("user.dir") + "\\AFD_202004816\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\AFD_202004816\\"+title+".jpg";
                    break;
                default:
                    fileInputPath = System.getProperty("user.dir") + "\\"+title+".txt";
                    fileOutputPath = System.getProperty("user.dir") + "\\"+title+".jpg"; 
                    break;
            }
           
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
    
    
    public static ArrayList<Object> obtenerTablaDeSiguientes(Object exp){
        ArrayList<Object> respuesta = new ArrayList<Object>();
        
        if(exp.getClass().getName().equals("ExpresionRegular.Dato")){       
            Dato data = (Dato)exp;
            Hoja n;
            
            ArrayList<Integer> primeros = new ArrayList<Integer>();
            ArrayList<Integer> ultimos = new ArrayList<Integer>();
            ArrayList<Hoja> resto = new ArrayList<Hoja>();
            
            if(data.getTipo().equals("id")){
                n = new Hoja(contador,data.getLex());         
            }else{
                n = new Hoja(contador,data.getLex().split("\"")[1]);
            }                         
            
            primeros.add(contador);
            ultimos.add(contador);
            resto.add(n);
            contador += 1;
            
            respuesta.add(primeros);
            respuesta.add(ultimos);
            respuesta.add(resto);
        }else{
            Expresion expresionActual = (Expresion)exp;       
            
            ArrayList<Object> primero;
            ArrayList<Object> siguiente;
                     
            ArrayList<Hoja> t1 = new ArrayList<Hoja>();
            ArrayList<Hoja> t2 = new ArrayList<Hoja>();
            ArrayList<Hoja> terminados = new ArrayList<Hoja>();
            
            switch(expresionActual.getOperador()){
                case ".":
                    
                    primero = obtenerTablaDeSiguientes(expresionActual.getPrimero());
                    
                    siguiente = obtenerTablaDeSiguientes(expresionActual.getSiguiente());
                    
                    ArrayList<Integer> p1 = (ArrayList<Integer>)primero.get(0);
                    ArrayList<Integer> p2 = (ArrayList<Integer>)siguiente.get(0);
                    ArrayList<Integer> sig1 = (ArrayList<Integer>)primero.get(1);
                    ArrayList<Integer> sig2 = (ArrayList<Integer>)siguiente.get(1);
                    
                    
                    t1 = (ArrayList<Hoja>)primero.get(2);
                    t2 = (ArrayList<Hoja>)siguiente.get(2);
                    
                    for(int i = 0; i< sig1.size(); i++){
                        for(int j = 0 ; j< p2.size() ; j++){
                            for(int k = 0;k<t1.size();k++){
                                Hoja n = t1.get(k);
                                if(n.getNo()== sig1.get(i)){
                                    if(n.siguiente != null){
                                        n.setSiguiente(n.getSiguiente()+"," + p2.get(j));
                                    }else{
                                        n.setSiguiente(String.valueOf(p2.get(j)));
                                    }
                                    
                                }
                            }                           
                        }
                    }
                    
                    terminados.addAll(t1);
                    terminados.addAll(t2);
                                       
                    
                    
                    String x;
                    String x2;
                    String r = "";
                    if(primero.size() > 3 && siguiente.size() >3){
                        x = (String) primero.get(3);
                        x2 = (String) siguiente.get(3);
                        
                        if((x.equals("x") && x2.equals("x")) || x.equals("xdi") && x2.equals("xdi")){
                            r=("xdi");
                        }else if(x.equals("xd") && x2.equals("xd")){
                            
                            for(int i = 0; i< p1.size(); i++){
                                for(int j = 0 ; j< p2.size() ; j++){
                                    for(int k = 0;k<t1.size();k++){
                                        Hoja n = t1.get(k);
                                        if(n.getNo()== p1.get(i)){
                                            if(n.siguiente != null){
                                                n.setSiguiente(n.getSiguiente()+"," + p2.get(j));
                                            }else{
                                                n.setSiguiente(String.valueOf(p2.get(j)));
                                            }

                                        }
                                    }                           
                                }
                            }
                        
                        }else if(x.equals("xi") && x2.equals("xi")){
                            
                            for(int i = 0; i< sig1.size(); i++){
                                for(int j = 0 ; j< sig2.size() ; j++){
                                    for(int k = 0;k<t1.size();k++){
                                        Hoja n = t1.get(k);
                                        if(n.getNo()== sig1.get(i)){
                                            if(n.siguiente != null){
                                                n.setSiguiente(n.getSiguiente()+"," + sig2.get(j));
                                            }else{
                                                n.setSiguiente(String.valueOf(sig2.get(j)));
                                            }
                                        }
                                    }                           
                                }
                            }
                        
                        }else if(x.equals("xi") && x2.equals("xd")){
                            for(int i = 0; i< sig1.size(); i++){
                                for(int j = 0 ; j< p2.size() ; j++){
                                    for(int k = 0;k<t1.size();k++){
                                        Hoja n = t1.get(k);
                                        if(n.getNo()== sig1.get(i)){
                                            if(n.siguiente != null){
                                                n.setSiguiente(n.getSiguiente()+"," + p2.get(j));
                                            }else{
                                                n.setSiguiente(String.valueOf(p2.get(j)));
                                            }

                                        }
                                    }                           
                                }
                            }
                        }else if(x.equals("xd") && x2.equals("xi")){
                            
                            for(int i = 0; i< p1.size(); i++){
                                for(int j = 0 ; j< sig2.size() ; j++){
                                    for(int k = 0;k<t1.size();k++){
                                        Hoja n = t1.get(k);
                                        if(n.getNo()== p1.get(i)){
                                            if(n.siguiente != null){
                                                n.setSiguiente(n.getSiguiente()+"," + sig2.get(j));
                                            }else{
                                                n.setSiguiente(String.valueOf(sig2.get(j)));
                                            }

                                        }
                                    }                           
                                }
                            }
                        
                        }
                    }else if(primero.size() > 3 && siguiente.size() <=3){
                        x = (String) primero.get(3);
                        switch(x){
                            case "x":
                                r=("xi");
                                break;
                            case "xi":
                                for(int i = 0; i< sig1.size(); i++){
                                    for(int j = 0 ; j< p2.size() ; j++){
                                        for(int k = 0;k<t1.size();k++){
                                            Hoja n = t1.get(k);
                                            if(n.getNo()== sig1.get(i)){
                                                if(n.siguiente != null){
                                                    n.setSiguiente(n.getSiguiente()+"," + p2.get(j));
                                                }else{
                                                    n.setSiguiente(String.valueOf(p2.get(j)));
                                                }                                            
                                            }
                                        }                           
                                    }
                                }
                                r=("xi");
                                break;
                            case "xd":
                                for(int i = 0; i< p1.size(); i++){
                                    for(int j = 0 ; j< p2.size() ; j++){
                                        for(int k = 0;k<t1.size();k++){
                                            Hoja n = t1.get(k);
                                           if(n.getNo()== p1.get(i)){
                                                if(n.siguiente != null){
                                                    n.setSiguiente(n.getSiguiente()+"," + p2.get(j));
                                                }else{
                                                    n.setSiguiente(String.valueOf(p2.get(j)));
                                                }                                               
                                            }
                                        }                           
                                    }
                                }
                                break;
                            
                            case "xdi":
                                r=("xi");
                                break;
                            default:
                                break;
                        }
                    
                    }else if(primero.size() <= 3 && siguiente.size() >3){
                        x = (String) siguiente.get(3);
                        switch(x){
                            case "x":
                                r=("xd");
                                break;
                            case "xi":
                                for(int i = 0; i< sig1.size(); i++){
                                    for(int j = 0 ; j< sig2.size() ; j++){
                                        for(int k = 0;k<t1.size();k++){
                                            Hoja n = t1.get(k);
                                            if(n.getNo()== sig1.get(i)){
                                                if(n.siguiente != null){
                                                    n.setSiguiente(n.getSiguiente()+"," + sig2.get(j));
                                                }else{
                                                    n.setSiguiente(String.valueOf(sig2.get(j)));
                                                }
                                            }
                                        }                           
                                    }
                                }
                                break;
                            case "xd":
                                r=("xd");
                                break;
                            case "xdi":
                                r=("xd");
                                break;
                            default:
                                break;
                        }
                    }
                    
                    
                    respuesta.add(primero.get(0));
                    respuesta.add(siguiente.get(1));
                    respuesta.add(terminados);
                    if(r!= ""){
                    respuesta.add(r);}
                    break;
                    
                    
                case "|":
                    
                    primero = obtenerTablaDeSiguientes(expresionActual.getPrimero());                   
                    siguiente = obtenerTablaDeSiguientes(expresionActual.getSiguiente());
                    
                    ArrayList<Integer> primeros = (ArrayList<Integer>)primero.get(0);
                    primeros.addAll((ArrayList<Integer>)siguiente.get(0));
                    
                    ArrayList<Integer> ultimos = (ArrayList<Integer>)primero.get(1);
                    ultimos.addAll((ArrayList<Integer>)siguiente.get(1));
                    
                    t1 = (ArrayList<Hoja>)primero.get(2);
                    t2 = (ArrayList<Hoja>)siguiente.get(2);
                    
                    terminados.addAll(t1);
                    terminados.addAll(t2);
                                       
                    respuesta.add(primeros);
                    respuesta.add(ultimos);
                    respuesta.add(terminados);
                    
                    break;
                case "*":
                    
                    primero = obtenerTablaDeSiguientes(expresionActual.getPrimero());                   
                    
                    ArrayList<Integer> first = (ArrayList<Integer>)primero.get(1);
                    ArrayList<Integer> second = (ArrayList<Integer>)primero.get(1);

                    
                    t1 = (ArrayList<Hoja>)primero.get(2);
                    
                    for(int i = 0; i< second.size(); i++){
                        for(int j = 0; j<first.size();j++){
                            for(int k = 0;k<t1.size();k++){
                                Hoja n = t1.get(k);
                                if(n.getNo()== second.get(i)){
                                    if(n.getSiguiente() != null){
                                        n.setSiguiente(n.getSiguiente()+"," + first.get(j));
                                    }else{
                                        n.setSiguiente(String.valueOf(first.get(j)));
                                    }
                                    
                                }
                            } 
                        }                                                  
                    }
                    
                    terminados.addAll(t1);
                                       
                    respuesta.add(primero.get(0));
                    respuesta.add(primero.get(1));
                    respuesta.add(terminados);
                    respuesta.add("x");
                    break;
                case "+":
                    
                    primero = obtenerTablaDeSiguientes(expresionActual.getPrimero());                   
                    
                    ArrayList<Integer> first1 = (ArrayList<Integer>)primero.get(1);
                    ArrayList<Integer> second1 = (ArrayList<Integer>)primero.get(1);

                    
                    t1 = (ArrayList<Hoja>)primero.get(2);
                    
                    for(int i = 0; i< second1.size(); i++){
                        for(int j = 0; j<first1.size();j++){
                            for(int k = 0;k<t1.size();k++){
                                Hoja n = t1.get(k);
                                if(n.getNo()== second1.get(i)){
                                    if(n.getSiguiente() != null){
                                        n.setSiguiente(n.getSiguiente()+"," + first1.get(j));
                                    }else{
                                        n.setSiguiente("" + first1.get(j));
                                    }
                                    
                                }
                            } 
                        }                                                  
                    }
                    
                    terminados.addAll(t1);
                                       
                    respuesta.add(primero.get(0));
                    respuesta.add(primero.get(1));
                    respuesta.add(terminados);
                    
                    break;
                case "?":
                    
                    primero = obtenerTablaDeSiguientes(expresionActual.getPrimero());                                       
                    
                                       
                    respuesta.add(primero.get(0));
                    respuesta.add(primero.get(1));
                    respuesta.add(primero.get(2));
                    respuesta.add("x");
                    break;               
            }
        }
        return respuesta;
    }
    
    public static String obtenerTablaTransiciones(String id, String siguientes){       
        String[] ss = siguientes.split("\\,");
        String resultado = "digraph G{\nlabel=\""+id+"_TTransiciones\";\n A[shape=record,label=\"";
        String c1 = "{Estado", c2 ="{Transicion" , c3="{Siguiente";
        
        for(int i = 1; i<ss.length ;i++){
            //System.out.println(ss[i]);
            String[] sig = ss[i].split("\\-");
            //System.out.println(sig[0]+sig[1]+sig[2]);
            c1 += "|"+sig[0];
            c2 += "|"+sig[1];
            c3 += "|"+sig[2];         
        }
        resultado += c1 + "}|" + c2 + "}|" + c3+ "}\"]\n";
        resultado += "}";
        return resultado;
    }
    
    public static ArrayList<String> graficarAFD(Object expresion){
        ArrayList<String> respuesta = new ArrayList<String>();
    
        if(expresion.getClass().getName().equals("ExpresionRegular.Dato")){
            Dato data = (Dato)expresion;
            if(data.getTipo().equals("id")){
                respuesta.add("S"+contador);
                respuesta.add("S" + contador);
                respuesta.add("S"+contador+"[label=\"S"+contador+"\"];\n");
                respuesta.add(data.getLex());
                respuesta.add("");
                contador += 1;          
            }else{
                respuesta.add("S"+contador);
                respuesta.add("S" + contador);
                respuesta.add("S"+contador+"[label=\"S"+contador+"\"];\n");
                respuesta.add(data.getLex().split("\"")[1]);
                respuesta.add("");
                contador += 1; 
            }
            
        }else{
            Expresion exp = (Expresion)expresion;
            String conexiones = "";
            String nodos = "";
            String siguientes = "";
            ArrayList<String> first;
            ArrayList<String> next;
            Boolean primero = false;
            String[] p;
            String[] s;
             String[] t;
            switch(exp.getOperador()){
                case ".":
                    
                    if(contador == 0){
                        nodos += "S[shape=point];";
                        nodos += "S0;";
                        conexiones += "S->S0[taillabel=\"INICIO\"]";
                        contador += 1;
                        primero = true;
                    }
                    
                    first = graficarAFD(exp.getPrimero());
                    next = graficarAFD(exp.getSiguiente());
                                     
                    p = first.get(1).split(",");
                    s = next.get(0).split(",");
                    
                    String[] primeros=first.get(0).split(",");;
                    if(primero == true){
                        for(int i = 0;i<primeros.length;i++){
                            t = first.get(3).split(",");
                            conexiones += "S0->" + primeros[i]+"[taillabel=\""+t[i]+"\"]";
                            siguientes += ",S0-"+t[i]+"-"+ primeros[i];
                        }
                    }
                   
                    for(int i = 0;i<p.length;i++){
                        t = next.get(3).split(",");
                        for(int j = 0;j<s.length;j++){
                            conexiones += p[i] + "->" + s[j]+"[taillabel=\""+t[j]+"\"]";
                            siguientes += ","+ p[i] +"-"+t[j]+"-"+ s[j];
                        }
                    }
                    
                    if(primero == true){
                        respuesta.add("S0");
                    }else{
                        respuesta.add(first.get(0));
                    }
                    
                    respuesta.add(next.get(1));
                    respuesta.add(nodos+conexiones+first.get(2)+next.get(2));
                    respuesta.add(first.get(3));
                    respuesta.add(siguientes+first.get(4)+next.get(4));
                    break;
                case "|":
                    if(contador == 0){
                        nodos += "S[shape=point];";
                        nodos += "S0;";
                        conexiones += "S->S0[taillabel=\"INICIO\"]";
                        contador += 1;
                        primero = true;
                    }
                    
                    first = graficarAFD(exp.getPrimero());
                    next = graficarAFD(exp.getSiguiente());
                    
                    if(primero == true){
                        p = first.get(0).split(",");
                        s = next.get(0).split(",");
                   
                        for(int i = 0;i<p.length;i++){
                            t=first.get(3).split(",");
                            conexiones += "S0->"+p[i]+"[taillabel=\""+t[i]+"\"]";
                            siguientes += ",S0-"+t[i]+"-"+ p[i];
                        }
                        for(int i = 0;i<s.length;i++){
                            t=next.get(3).split(",");
                            conexiones += "S0->"+s[i]+"[taillabel=\""+t[i]+"\"]";
                            siguientes += ",S0-"+t[i]+"-"+ s[i];
                        }
                        
                    }
                    
                                       
                    respuesta.add(first.get(0)+","+next.get(0));
                    respuesta.add(first.get(1)+","+next.get(1));
                    respuesta.add(nodos+conexiones+first.get(2)+next.get(2));
                    respuesta.add(first.get(3)+","+next.get(3));
                    respuesta.add(siguientes+first.get(4)+next.get(4));
                    break;
                case "*":
                    
                    if(contador == 0){
                        nodos += "S[shape=point];";
                        nodos += "S0[shape=doublecircle];";
                        conexiones += "S->S0[taillabel=\"INICIO\"]";
                        contador += 1;
                        primero = true;
                    }
                    
                    first = graficarAFD(exp.getPrimero());
                    
                    
                    p = first.get(0).split(",");
                    s = first.get(1).split(",");
                    t = first.get(3).split(",");
                    
                    for(int i = 0;i<p.length;i++){                          
                        for(int j = 0; j<s.length;j++){
                            conexiones += s[j]+"->"+p[i]+"[taillabel=\""+t[i]+"\"];";
                            siguientes += ","+s[j]+"-"+t[i]+"-"+ p[i];
                        }
                    }
                    
                    if(primero == true){
                        for(int i = 0;i<p.length;i++){
                            conexiones += "S0->"+p[i]+"[taillabel=\""+t[i]+"\"];";
                            siguientes += ",S0-"+t[i]+"-"+ p[i];
                        }                       
                    }
                      
                    
                                      
                    respuesta.add(first.get(0));
                    respuesta.add(first.get(1));
                    respuesta.add(nodos+conexiones+first.get(2));
                    respuesta.add(first.get(3));
                    respuesta.add(siguientes+first.get(4));
                    break;
                case "+":
                    if(contador == 0){
                        nodos += "S[shape=point];";
                        nodos += "S0;";
                        conexiones += "S->S0[taillabel=\"INICIO\"]";
                        contador += 1;
                        primero = true;
                    }
                    
                    first = graficarAFD(exp.getPrimero());
                    
                    p = first.get(0).split(",");
                    s = first.get(1).split(",");
                    t = first.get(3).split(",");
                    
                    for(int i = 0;i<p.length;i++){                          
                        for(int j = 0; j<s.length;j++){
                            conexiones += s[j]+"->"+p[i]+"[taillabel=\""+t[i]+"\"];";
                            siguientes += ","+s[j]+"-"+t[i]+"-"+ p[i];
                        }
                    }
                    
                    if(primero == true){
                        for(int i = 0;i<p.length;i++){
                            conexiones += "S0->"+p[i]+"[taillabel=\""+t[i]+"\"];";
                            siguientes += ",S0-"+t[i]+"-"+ p[i];
                        }                       
                    }                      
                                      
                    respuesta.add(first.get(0));
                    respuesta.add(first.get(1));
                    respuesta.add(nodos+conexiones+first.get(2));
                    respuesta.add(first.get(3));
                    respuesta.add(siguientes+first.get(4));
                    break;
                case "?":
                    /*if(contador == 0){
                        nodos += "S[shape=point];";
                        nodos += "S0[shape=doublecircle];";
                        conexiones += "S->S0[taillabel=\"INICIO\"]";
                        contador += 1;
                        primero = true;
                    }
                                       
                    if(primero == true){
                        conexiones += "S0->S0[taillbel=\""+first.get(3)+"\"];";
                    }
                     */  
                    
                    first = graficarAFD(exp.getPrimero());
                    respuesta.add(first.get(0));
                    respuesta.add(first.get(1));
                    respuesta.add(nodos+conexiones+first.get(2));
                    respuesta.add(first.get(3));
                    respuesta.add(first.get(4));
                    break;
                default:
                    break;
            }
            
        }
        
        
        return respuesta;
    }
}
