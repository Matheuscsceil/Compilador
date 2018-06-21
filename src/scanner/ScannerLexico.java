package scanner;

import java.util.Scanner;

public class ScannerLexico {
    
    private final Scanner leitor;
    private String caractere = null;
    
    //atributos - token
    private int codigo;
    private String lexema;
    private Token token = null;
    
    //end of file false
    private boolean EOF = false;
    
    public ScannerLexico(Scanner scan){
        leitor = scan;      
        leitor.useDelimiter("");
          
        if(leitor.hasNext()){
	    caractere =  leitor.next();
	}else{
	    caractere = null;
	    EOF = true;
	}
    }
    
    public Token scan(){
        lexema = "";
        token = null;
        
        if(EOF){
            return new Token(-1, null);
        }
        
        while(ehCharBranco(caractere)){
            if(leitor.hasNext()){
	        caractere = leitor.next();
	    }else{
	        caractere = null;
	        EOF = true;
	    }
            if(EOF){
                return new Token(-1, null);
            }          
        }
                            
        if(CodigosToken.ehSimboloSimples(caractere)){ // simbolos simples: ( ) { } , ; + - * /
            defineTokenFound(true);
        }else{
            automato();
        }
        
        if(token == null){
            return new Token(-1,"");
        }else{
            System.out.println(token.getCodigo() +" - "+token.getLexema());
            return token;
        }    
        
    }
    
    public void automato(){
        if(caractere.equals("<")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("=")){
                    defineTokenFound(true);
                }else{
                    defineTokenFound(false);
                }
            }
        }else if(caractere.equals(">")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("=")){
                    defineTokenFound(true);
                }else{
                    defineTokenFound(false);
                }
            }
        }else if(caractere.equals("=")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("=")){
                    defineTokenFound(true);
                }else{
                    defineTokenFound(false);
                }
            }
        }else if(caractere.equals("|")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("|")){
                    defineTokenFound(true);
                }else{
                    defineTokenFound(false);
                }
            }
        }else if(caractere.equals("&")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("&")){
                    defineTokenFound(true);
                }else{
                    defineTokenFound(false);
                }
            }
        }else if(caractere.equals("!")){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();

            if(caractere != null){
                if(caractere.equals("=")){
                    defineTokenFound(true);
                }else{
                    token = new Token(23,"!");
                }
            }
        }else if(CodigosToken.ehLetra(caractere) || caractere.equals("_")){ //pode ser identificador ou palavra reservada
            while(CodigosToken.ehLetra(caractere) || caractere.equals("_") || CodigosToken.ehDigito(caractere)){
                adicionaCaractereNoLexemaAndSetaProximoCaractere();
            }
    
            defineTokenFound(false);
        }else if(CodigosToken.ehDigito(caractere) || caractere.equals(".")){ //pode ser INTEIRO OU FLOAT
            //codErro = montarNumero();
            while(CodigosToken.ehDigito(caractere)){
                adicionaCaractereNoLexemaAndSetaProximoCaractere();
            }
            if(!caractere.equals(".")){ //é inteiro
                defineTokenFound(false);
            }else{ //vai ser float ou dar erro de má formacao
                adicionaCaractereNoLexemaAndSetaProximoCaractere();
                if(CodigosToken.ehDigito(caractere)){
                    while(CodigosToken.ehDigito(caractere)){
                        adicionaCaractereNoLexemaAndSetaProximoCaractere();
                    }
                defineTokenFound(false);
                }
            }
        }else{
            System.out.println("caractere invalido");
        }
    }
    
    public void adicionaCaractereNoLexemaAndSetaProximoCaractere(){
        lexema += caractere;
        if(leitor.hasNext()){
            caractere =  leitor.next();
        }else{
            caractere = null;
            EOF = true;
        }
    }
    
    private boolean ehCharBranco(String ch){
        if(ch != null && ch.equals(" ")){
            return true;
        } else{
            return false;
        }
    }

    private void defineTokenFound(boolean lerProximo) {
        if(lerProximo){
            adicionaCaractereNoLexemaAndSetaProximoCaractere();
        }
        codigo = CodigosToken.getCodigoToken(lexema);
        token = new Token(codigo, lexema);
    }
    
    public boolean eof() {
        return EOF;
    }

}