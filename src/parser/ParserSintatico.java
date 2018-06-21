package parser;

import Gerador.Gerador;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import scanner.CodigosToken;
import scanner.Token;
import scanner.ScannerLexico;

public class ParserSintatico {
    private Token token;
    private ScannerLexico scanner;
    public static ArrayList<Simbolo> tabelaSimbolos = new ArrayList<>();
    private Integer escopo = 0;
    public static boolean errou = false;
    
    public static ArrayList<String[]> operacoesRelacionais = new ArrayList<>();
    public static ArrayList<String[]> operacoesAritmetica = new ArrayList<>();
    public static ArrayList<String> ordemOperacoes = new ArrayList<>();
    
    public ParserSintatico(ScannerLexico scanner){
        this.scanner = scanner;
    }
    
    public void run(){
        scan();
        
        if(!scanner.eof()){
            programa();
            if(!scanner.eof()){
                System.out.println("FIM DE ARQUIVO.");
            }
        }else {
            pararExecucao("Seu código está errado, conserte-o.");
        }
    }
    
    private void scan() {
        token = scanner.scan();
        if(token.getCodigo() == -1){
            Gerador gerador = new Gerador();
            gerador.gerarCodigo();
        }
    }
    
    private void programa(){
        //int main"("")" <bloco>      
        if(token.getCodigo() == CodigosToken.MAIN){
            scan();
            if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                scan();
                if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                    scan();
                    bloco();
                }else {
                    pararExecucao("ERRO AO FECHAR PARENTESES.");
                }
            }else {
                pararExecucao("ERRO AO ABRIR PARENTESES.");
            }
        }else{
            pararExecucao("ERRO AO ABRIR MAIN.");
        }
    }
    
    private void bloco() {
        //“{“ {<decl_var>}* {<comando>}* “}”
        escopo++;
                
        if(token.getCodigo() == CodigosToken.ABRE_CHAVES){
            scan();

            while(CodigosToken.decl_var.contains(token.getCodigo()) || CodigosToken.comando.contains(token.getCodigo())){

                while(CodigosToken.decl_var.contains(token.getCodigo())){
                    decl_var();
                }
                while(CodigosToken.comando.contains(token.getCodigo())){
                    comando();
                }

            }

            if(token.getCodigo() == CodigosToken.FECHA_CHAVES){
                scan();
            } else {
                pararExecucao("ERRO AO FECHAR CHAVES.");
            }
        }else{
            pararExecucao("ERRO AO ABRIR CHAVES.");
        }
        
        escopo--;
    }
    
    private void decl_var() {
        //<tipo> <id> {,<id>}* ";"
        Simbolo simbolo;
        int tipo = 0;
        
        if(CodigosToken.tipo.contains(token.getCodigo())){
            tipo = token.getCodigo();
            scan();  
        }else{
            pararExecucao("ERRO AO DECLARAR TIPO DE VARIAVEL.");
        }
        
        if(token.getCodigo() == CodigosToken.ID){
            
            simbolo = new Simbolo(token,escopo,tipo);
            if(!variavelFoiDeclarada(simbolo)){
                tabelaSimbolos.add(simbolo);
            }else{
                pararExecucao("VARIAVEL JA DECLARADA");
            }
            
            scan();
            while(token.getCodigo() == CodigosToken.VIRGULA){
                scan();
                if(token.getCodigo() == CodigosToken.ID){
                    
                    simbolo = new Simbolo(token,escopo,tipo);
                    if(!variavelFoiDeclarada(simbolo)){
                        tabelaSimbolos.add(simbolo);
                    }else{
                        pararExecucao("VARIAVEL JA DECLARADA");
                    }
            
                    scan();
                }else{
                    pararExecucao("ERRO AO DECLARAR VARIAVEL.");
                }
            }
            if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                scan();                
            }else {
                pararExecucao("ERRO FALTA DE ;");
            } 
        }else{
            pararExecucao("ERRO AO DECLARAR VARIAVEL.");
        } 
        
    }
    
    private void comando() {
        //    <comando_básico>
        //    <iteração>
        
        if(CodigosToken.comando_basico.contains(token.getCodigo())){ // comando_basico
            comando_basico();
        } else if(CodigosToken.iteracao.contains(token.getCodigo())){ // iteracao
            iteracao();
        }else{
            pararExecucao("ERRO NO CORPO DE COMANDO.");
        }
    }
        
    private void iteracao(){
        // while "("<expr_relacional>")" <comando>

        if(token.getCodigo() == CodigosToken.WHILE){
            _while();      
        }else{
            pararExecucao("ERRO WHILE.");
        }
    }
    
    private void _while(){
        //while
        
        scan();
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){            
            
            ordemOperacoes.add("INICIO RELACIONAL");
            
            scan();
            OperacaoRelacional.token = this.token;
            OperacaoRelacional.operacaoRelacional.add(token);
            OperacaoRelacional relacional = new OperacaoRelacional(this.scanner);
            relacional.estado0();
            token = OperacaoRelacional.token;
            
            if(!OperacaoRelacional.pilhaEstaVazia()){
                pararExecucao("PILHA OPERAÇÃO RELACIONAL NÃO ESTÁ VAZIA.");
            }else{
                
                String[] opRelacional = new String[OperacaoRelacional.operacaoRelacional.size()-1];
                for(int i=0;i<OperacaoRelacional.operacaoRelacional.size()-1;i++){
                    opRelacional[i] = OperacaoRelacional.operacaoRelacional.get(i).getLexema();
                }
                
                operacoesRelacionais.add(opRelacional);
                
                OperacaoRelacional.operacaoRelacional = new ArrayList<>();
                
                comando();
                
                ordemOperacoes.add("FIM RELACIONAL");
            }
                        
        } else {
            pararExecucao("ERRO AO ABRIR PARENTESES WHILE.");
        }
    }
    
    private void comando_basico(){
        //    <atribuição>
        //    <bloco>       
        
        if(CodigosToken.atribuicao.contains(token.getCodigo())){ // atribuicao
            atribuicao();
        }else if(CodigosToken.bloco.contains(token.getCodigo())){ //bloco
            bloco();
        } else {
            pararExecucao("ERRO NO CORPO DE COMANDO.");    
        }
    }
    
    private void atribuicao(){
        //<id> "=" <expr_arit> ";"
        String id = token.getLexema();
        
        if(token.getCodigo() == CodigosToken.ID){
            scan();
            String igual = token.getLexema();
            if(token.getCodigo() == CodigosToken.ATRIBUICAO){
                
                scan();
                OperacaoAritmetica.token = this.token;
                OperacaoAritmetica.operacao.add(token);
                OperacaoAritmetica operacao = new OperacaoAritmetica(this.scanner);
                operacao.estado0();
                token = OperacaoAritmetica.token;
                
                if(!OperacaoAritmetica.pilhaEstaVazia()){
                    pararExecucao("PILHA OPERAÇÃO RELACIONAL NÃO ESTÁ VAZIA."); 
                }else{ 
                    if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                        scan();
                        
                        String[] opAritmetica = new String[OperacaoAritmetica.operacao.size()+2];
                        opAritmetica[0] = id;opAritmetica[1] = igual;
                        for(int i=0;i<OperacaoAritmetica.operacao.size();i++){
                            opAritmetica[i+2] = OperacaoAritmetica.operacao.get(i).getLexema();
                        }

                        operacoesAritmetica.add(opAritmetica);
                        ordemOperacoes.add("ARITMETICA");
                        OperacaoAritmetica.operacao = new ArrayList<>();
                        
                    }else{
                        pararExecucao("ERRO FALTA DE ;");
                    }                 
                }
                                
                
            } else {
                pararExecucao("ERRO NA ATRIBUIÇÃO");
            }
        } else {
            pararExecucao("ERRO AO DECLARAR VARIAVEL.");
        }
        
    }
    
    boolean variavelFoiDeclarada(Simbolo declarei){
        
        for(int i=0;i<tabelaSimbolos.size();i++){
            if(tabelaSimbolos.get(i).getLexema().equals(declarei.getLexema())  && declarei.getEscopo() <= 1){
                return true;
            }
        }
        
        return false;
    }
        
    public static void pararExecucao(String erro){
        errou = true;
        System.out.println(erro);
        JOptionPane.showMessageDialog(null, erro);
        //System.exit(0);
    }
        
}