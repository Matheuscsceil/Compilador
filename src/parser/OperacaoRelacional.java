package parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import scanner.CodigosToken;
import scanner.ScannerLexico;
import scanner.Token;

public class OperacaoRelacional {
    
    public static Token token;
    private ScannerLexico scanner;
    public static ArrayDeque<Integer> pilha = new ArrayDeque<>();
    public static ArrayList<Token> operacaoRelacional = new ArrayList<>();
    
    public OperacaoRelacional(ScannerLexico scanner){
        this.scanner = scanner;
    }
    
    public static boolean pilhaEstaVazia(){
        if(pilha.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    private void scan() {
        token = scanner.scan();
        if(token.getCodigo() != CodigosToken.ABRE_CHAVES){
            operacaoRelacional.add(token);
        }
    }
    
    public void estado0(){
        //scan();
        if(token.getCodigo() == CodigosToken.DIFERENTE_ALONE){
            scan();
            estado1();
        }else if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado3();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado2();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    public void estado1(){
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado3();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    public void estado2(){
        if(token.getCodigo() == CodigosToken.MAIOR || token.getCodigo() == CodigosToken.MAIOR_IGUAL || token.getCodigo() == CodigosToken.MENOR || token.getCodigo() == CodigosToken.MENOR_IGUAL || token.getCodigo() == CodigosToken.IGUAL || token.getCodigo() == CodigosToken.DIFERENTE){
            scan();
            estado7();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    public void estado3(){
        while(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
        }
        if(token.getCodigo() == CodigosToken.DIFERENTE_ALONE){
            scan();
            estado4();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado5();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    public void estado4(){
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado3();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    //estado final
    public void estado5(){
        if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            if(!pilha.isEmpty()){
                pilha.removeFirst();
                scan();
                estado6();
            }else{
                System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO RELACIONAL");
            }
        }else if(token.getCodigo() == CodigosToken.MAIOR || token.getCodigo() == CodigosToken.MAIOR_IGUAL || token.getCodigo() == CodigosToken.MENOR || token.getCodigo() == CodigosToken.MENOR_IGUAL || token.getCodigo() == CodigosToken.IGUAL || token.getCodigo() == CodigosToken.DIFERENTE){
            scan();
            estado7();
        }
    }
    
    //estado final
    public void estado6(){
        while(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            if(!pilha.isEmpty()){
                pilha.removeFirst();
                scan();
            }else{
                System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO RELACIONAL");
            }
        }       
        if(token.getCodigo() == CodigosToken.MAIOR || token.getCodigo() == CodigosToken.MAIOR_IGUAL || token.getCodigo() == CodigosToken.MENOR || token.getCodigo() == CodigosToken.MENOR_IGUAL || token.getCodigo() == CodigosToken.IGUAL || token.getCodigo() == CodigosToken.DIFERENTE){
            scan();
            estado7();
        }
    }
    
    public void estado7(){
        if(token.getCodigo() == CodigosToken.DIFERENTE_ALONE){
            scan();
            estado8();
        }else if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado10();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado11();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    public void estado8(){
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado10();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    //estado final
    public void estado9(){
        while(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            scan();
            if(token.getCodigo() != CodigosToken.ABRE_CHAVES){
                if(!pilha.isEmpty()){
                    pilha.removeFirst();
                }else{
                    System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO RELACIONAL");
                }
            }else{
                break;
            }
        }
        if(token.getCodigo() == CodigosToken.AND){
            scan();
            estado0();
        }else if(token.getCodigo() == CodigosToken.OR){
            scan();
            estado0();
        }
    }
    
    public void estado10(){
        while(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
        }
        if(token.getCodigo() == CodigosToken.DIFERENTE_ALONE){
            scan();
            estado12();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado13();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    //estado final
    public void estado11(){           
            if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                scan();
                if(token.getCodigo() != CodigosToken.ABRE_CHAVES){
                    if(!pilha.isEmpty()){
                        pilha.removeFirst();
                        estado9();
                    }else{
                        System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO RELACIONAL");
                    }
                }
            }else if(token.getCodigo() == CodigosToken.AND){
                scan();
                estado0();
            }else if(token.getCodigo() == CodigosToken.OR){
                scan();
                estado0();
            }
    }
    
    public void estado12(){
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado10();
        }else{
            System.out.println("ERRO OPERAÇÃO RELACIONAL");
        }
    }
    
    //estado final
    public void estado13(){
        if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            pilha.removeFirst();
            scan();
            estado14();
        }
    }
    
    //estado final
    public void estado14(){
        if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            pilha.removeFirst();
            scan();
            estado13();
        }
    }
    
    
}
