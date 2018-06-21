package scanner;

import java.util.Arrays;
import java.util.List;

public class CodigosToken {
    //identificador de final de arquivo
    public static final int EOF = -1;
    
    public static final int MAIN = 0;
    public static final int WHILE = 1;
    public static final int INT = 2;
    public static final int FLOAT = 3;   
    public static final int AND = 4;
    public static final int OR = 5;
    public static final int MENOR = 6;
    public static final int MAIOR = 7;
    public static final int MENOR_IGUAL = 8;
    public static final int MAIOR_IGUAL = 9;
    public static final int IGUAL = 10;
    public static final int DIFERENTE = 11;
    public static final int SOMA = 12;
    public static final int SUBTRACAO = 13;
    public static final int MULTIPLICACAO = 14;
    public static final int DIVISAO = 15;
    public static final int ATRIBUICAO = 16;
    public static final int FECHA_PARENTESES = 17;
    public static final int ABRE_PARENTESES = 18;
    public static final int ABRE_CHAVES = 19;
    public static final int FECHA_CHAVES = 20;
    public static final int VIRGULA = 21;
    public static final int PONTO_VIRGULA = 22;
    public static final int DIFERENTE_ALONE = 23;
    
    //Valores não fixos que dependem da verificacao de outros metodos
    public static final int ID = 50;
    public static final int VALOR_INTEIRO = 51;
    public static final int VALOR_REAL = 52;
    
    //expressoes regulares para o alfabeto
    static String L = "[a-z]";
    static String D = "[0-9]";
    static String VAR = "(_|"+L+")(_|"+L+"|"+D+")*";
    static String INTEIRO = D+"+";
    static String REAL = D+"*."+D+"+";
    
    private static final List<String> TABELA = Arrays.asList(
        "main",
        "while",
        "int",
        "float",
        "&&",
        "||",
        "<",
        ">",
        "<=",
        ">=",
        "==",
        "!=",
        "+",
        "-",
        "*",
        "/",
        "=",
        ")",
        "(",
        "{",
        "}",
        ",",
        ";",
        "!"
    );
 
    public static int getCodigoToken(String lexema){
        if(TABELA.contains(lexema)){
            return TABELA.indexOf(lexema);
        } else if(ehIdentificador(lexema)){
            return 50;
        } else if(ehInteiro(lexema)){
            return 51;
        } else if(ehFloat(lexema)){
            return 52;
        }else{
            return -1;
        }
    }
        
    public static boolean ehLetra(String ch) {
        if(ch.matches(L)){
            return true;
        } else{
            return false;
        }
    }
    
    public static boolean ehDigito(String ch) {
        if(ch.matches(D)){ 
            return true;
        } else{
            return false;
        }
        
    }
    
    public static boolean ehIdentificador(String lexema) {
        if(lexema.matches(VAR)){
            return true;
        }else{
            return false;                
        }
    }

    public static boolean ehInteiro(String lexema) {
        if(lexema.matches(INTEIRO)){
            return true;
        }else{
            return false;                
        }
    }

    public static boolean ehFloat(String lexema) {
        if(lexema.matches(REAL)){
            return true;
        }else{
            return false;                
        }
    }
    
    public static boolean ehSimboloSimples(String ch){
        char c = ch.charAt(0);
        if(
            c == '(' ||
            c == ')' ||
            c == '{' ||
            c == '}' ||
            c == ',' ||
            c == ';' ||
            c == '+' ||
            c == '-' ||
            c == '*' ||
            c == '/'    
        )
            return true;
        else
            return false;
    }
        
    public static List<Integer> atribuicao = Arrays.asList(
            CodigosToken.ID
    );
    
    public static List<Integer> iteracao = Arrays.asList(
            CodigosToken.WHILE
    );
    
    public static List<Integer> bloco = Arrays.asList(
            CodigosToken.ABRE_CHAVES
    );
        
    public static List<Integer> comando_basico = Arrays.asList(
            CodigosToken.ABRE_CHAVES,
            CodigosToken.ID
    );
    
    public static List<Integer> comando = Arrays.asList(
            CodigosToken.ABRE_CHAVES,
            CodigosToken.ID,
            CodigosToken.WHILE
    );
    
    public static List<Integer> tipo = Arrays.asList(
            CodigosToken.INT,
            CodigosToken.FLOAT
    );
    
    public static List<Integer> decl_var = tipo;
    
    public static List<Integer> op_relacional = Arrays.asList(
            CodigosToken.IGUAL,
            CodigosToken.DIFERENTE,
            CodigosToken.MENOR,
            CodigosToken.MAIOR,
            CodigosToken.MENOR_IGUAL,
            CodigosToken.MAIOR_IGUAL
    );
    
    
    
    
    
    public static String getNome(int codigo){
        if(codigo >= 50){
            switch (codigo){
                case 50: return "ID";
                case 51: return "VALOR_INTEIRO";
                case 52: return "VALOR_REAL";
                case 53: return "VALOR_CHAR";
            }
        } else if(codigo >= 0){
            switch(TABELA.get(codigo)){
                case "main":    return "MAIN";
                case "if":      return "IF";
                case "else":    return "ELSE";
                case "while":   return "WHILE";
                case "do":      return "DO";
                case "for":     return "FOR";
                case "int":     return "INT";
                case "float":   return "FLOAT";
                case "char":    return "CHAR";
                case "<":       return "MENOR";
                case ">":       return "MAIOR";
                case "<=":      return "MENOR_IGUAL";
                case ">=":      return "MAIOR_IGUAL";
                case "==":      return "IGUAL";        
                case "!=":      return "DIFERENTE";    
                case "+":       return "SOMA";
                case "-":       return "SUBTRACAO";
                case "*":       return "MULTIPLICACAO";
                case "/":       return "DIVISAO";
                case "=":       return "ATRIBUICAO";
                case ")":       return "FECHA_PARENTESES";
                case "(":       return "ABRE_PARENTESES";
                case "{":       return "ABRE_CHAVES";
                case "}":       return "FECHA_CHAVES";
                case ",":       return "VIRGULA";
                case ";":       return "PONTO_VIRGULA";
                }
        }
        return "EOF";
    }
    
    public static String getLexema(int codigo){
        if(codigo > -1 && codigo < 50){
            return TABELA.get(codigo);
        } else return getNome(codigo);
    }
    
}