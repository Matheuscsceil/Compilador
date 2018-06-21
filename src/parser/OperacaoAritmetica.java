package parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import scanner.CodigosToken;
import scanner.ScannerLexico;
import scanner.Token;

public class OperacaoAritmetica {
    
    public static Token token;
    private ScannerLexico scanner;
    public static ArrayDeque<Integer> pilha = new ArrayDeque<>();
    public static ArrayList<Token> operacao = new ArrayList<>();
    
    public OperacaoAritmetica(ScannerLexico scanner){
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
        if(token.getCodigo() != CodigosToken.PONTO_VIRGULA){
            operacao.add(token);
        }
    }
    
    public void estado0(){
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado1();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado4();
        }else if(token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.SOMA){
            scan();
            estado2();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    public void estado1(){
        while(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
        }
        if(token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.SOMA){
            scan();
            estado3();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado5();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    public void estado2(){
        if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado4();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    public void estado3(){
        if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado5();
        }else if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado1();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    //estado final
    public void estado4(){
        if(token.getCodigo() == CodigosToken.DIVISAO || token.getCodigo() == CodigosToken.SOMA || token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.MULTIPLICACAO){
            scan();
            estado3();
        }else{
            if(!pilha.isEmpty()){
                System.out.println("ERRO NA OPERAÇÃO ARITMÉTICA");
            }
        }
    }
    
    //estado final
    public void estado5(){
        if(token.getCodigo() == CodigosToken.DIVISAO || token.getCodigo() == CodigosToken.SOMA || token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.MULTIPLICACAO){
            scan();
            estado7();
        }else if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            if(!pilha.isEmpty()){
                pilha.removeFirst();
                scan();
                estado6();
            }else{
                System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO ARITMÉTICA");
            }
        }else{
            if(!pilha.isEmpty()){
                System.out.println("ERRO NA OPERAÇÃO ARITMÉTICA");
            }
        }
    }
    
    //estado final
    public void estado6(){
        while(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            if(!pilha.isEmpty()){
                pilha.removeFirst();
                scan();
            }else{
                System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO ARITMÉTICA");
                break;
            }
        }
        if(token.getCodigo() == CodigosToken.DIVISAO || token.getCodigo() == CodigosToken.SOMA || token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.MULTIPLICACAO){
            scan();
            estado7();
        }
    }
    
    public void estado7(){
        if(token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.SOMA){
            scan();
            estado9();
        }else if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado8();
        }else if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            pilha.addFirst(token.getCodigo());
            scan();
            estado1();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    //estado final
    public void estado8(){
        if(token.getCodigo() == CodigosToken.DIVISAO || token.getCodigo() == CodigosToken.SOMA || token.getCodigo() == CodigosToken.SUBTRACAO || token.getCodigo() == CodigosToken.MULTIPLICACAO){
            scan();
            estado7();
        }else if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
            if(!pilha.isEmpty()){
                pilha.removeFirst();
                scan();
                estado6();
            }else{
                System.out.println("ERRO AO FECHAR PARENTESES NA OPERAÇÃO ARITMÉTICA");
            }
        }else{
            if(!pilha.isEmpty()){
                System.out.println("ERRO NA OPERAÇÃO ARITMÉTICA");
            }
        }
    }
    
    public void estado9(){
        if(token.getCodigo() == CodigosToken.ID || token.getCodigo() == CodigosToken.VALOR_INTEIRO || token.getCodigo() == CodigosToken.VALOR_REAL){
            scan();
            estado8();
        }else{
            System.out.println("ERRO OPERAÇÃO ARITMÉTICA");
        }
    }
    
    
}
