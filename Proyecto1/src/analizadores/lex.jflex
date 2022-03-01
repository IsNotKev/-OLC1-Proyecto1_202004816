
package analizadores;
import Error_.*;
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

WHITE =[ \t\r\f\n]+
COMENTARIOL =[\/\/][^\n]*[\n]
COMENTARIOML =[<][!][^!]*[!][>]
ID =[a-zA-z\_][a-zA-Z\d\_]*
CADENA =[\"][^\"]*[\"]
CONJUNTOSIGNO =[\!\"\#\$\%\&\'\(\)\*\+\,\-\/\:\;\<\>\=\?\@\\\[\]\^\_\`\{\|\}][\~][\!\"\#\$\%\&\'\(\)\*\+\,\-\/\:\;\<\>\=\?\@\\\[\]\^\_\`\{\|\}]
CONJUNTODIGITO =[\d][\~][\d]
CONJUNTOMIN =[a-z][\~][a-z]
CONJUNTOMAY =[A-Z][\~][A-Z]


%%

"{" {return new Symbol(sym.LLAVELEFT, yycolumn, yyline, yytext());}
"}" {return new Symbol(sym.LLAVERIGHT, yycolumn, yyline, yytext());}
"CONJ" {return new Symbol(sym.CONJ,yycolumn,yyline,yytext());}
":" {return new Symbol(sym.DOSPUNTOS,yycolumn,yyline,yytext());}
"->" {return new Symbol(sym.FLECHA,yycolumn,yyline,yytext());}
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
{ID} {return new Symbol(sym.ID, yycolumn, yyline, yytext());}
{CADENA} {return new Symbol(sym.CADENA, yycolumn, yyline, yytext());}
{CONJUNTODIGITO} {return new Symbol(sym.CONJUNTODIGITO, yycolumn, yyline, yytext());}
{CONJUNTOMIN} {return new Symbol(sym.CONJUNTOMIN, yycolumn, yyline, yytext());}
{CONJUNTOMAY} {return new Symbol(sym.CONJUNTOMAY, yycolumn, yyline, yytext());}
{CONJUNTOSIGNO} {return new Symbol(sym.CONJUNTOSIGNO, yycolumn, yyline, yytext());}

. {
    System.out.println("Este es un error lexico: "+yytext()+", en la linea: "+yyline+", en la columna: "+yycolumn);
    AnalizadorLenguaje.errores.add(new Error_("Se detectó un error léxico (caracter "+yytext()+")","Léxico",yyline, yycolumn));
}






