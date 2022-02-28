
package analizadores;
import Error.*;
import Instruction.*;
import java_cup.runtime.Symbol;

%% 
%class Scanner
%public 
%line 
%char 
%cup 
%unicode
%ignorecase

%init{ 
    yyline = 1; 
    yycolumn = 1; 
%init}

DIGITO =[\d]
CARACTERMIN =[a-z]
CARACTERMAY =[A-Z]
WHITE =[ \t\r\f\n]+
COMENTARIOL =[\/\/][^\n]*[\n]
COMENTARIOML =[<][!][^!]*[!][>]
ID =[a-zA-z\_][a-zA-Z\d\_]*
CADENA =[\"][^\"]*[\"]

%%

"{" {return new Symbol(sym.LLAVELEFT, yycolumn, yyline, yytext());}
"}" {return new Symbol(sym.LLAVERIGHT, yycolumn, yyline, yytext());}
"CONJ" {return new Symbol(sym.CONJ,yycolumn,yyline,yytext());}
":" {return new Symbol(sym.DOSPUNTOS,yycolumn,yyline,yytext());}
"->" {return new Symbol(sym.FLECHA,yycolumn,yyline,yytext());}
"~" {return new Symbol(sym.VIRGULILLA, yycolumn, yyline, yytext());}
"," {return new Symbol(sym.COMA, yycolumn, yyline, yytext());}
"." {return new Symbol(sym.PUNTO, yycolumn, yyline, yytext());}
"|" {return new Symbol(sym.OR, yycolumn, yyline, yytext());}
"*" {return new Symbol(sym.POR, yycolumn, yyline, yytext());}
"+" {return new Symbol(sym.MAS, yycolumn, yyline, yytext());}
"?" {return new Symbol(sym.INTERROGACION, yycolumn, yyline, yytext());}
";" {return new Symbol(sym.PUNTOYCOMA, yycolumn, yyline, yytext());}
"%%" {return new Symbol(sym.SEPARADOR, yycolumn, yyline, yytext());}

{WHITE} {}
{COMENTARIOL} {}
{COMENTARIOML} {}
{CARACTERMIN} {return new Symbol(sym.CARACTERMIN, yycolumn, yyline, yytext());}
{CARACTERMAY} {return new Symbol(sym.CARACTERMAY, yycolumn, yyline, yytext());}
{ID} {return new Symbol(sym.ID, yycolumn, yyline, yytext());}
{CADENA} {return new Symbol(sym.CADENA, yycolumn, yyline, yytext());}
{DIGITO} {return new Symbol(sym.DIGITO, yycolumn, yyline, yytext());}

. {
    System.out.println("Este es un error lexico: "+yytext()+", en la linea: "+yyline+", en la columna: "+yycolumn);
    Instruction.list.addError(new Error_("Se detectó un error léxico (caracter "+yytext()+")","Léxico",yyline, yycolumn));
}






