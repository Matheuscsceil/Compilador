package Gerador;

import Arvore.ArvoreAritmetica;
import Arvore.ArvoreCondicional;
import GeradorAlvo.ConverterPHP;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static parser.ParserSintatico.operacoesAritmetica;
import static parser.ParserSintatico.operacoesRelacionais;
import static parser.ParserSintatico.ordemOperacoes;

public class Gerador {
    
    private int indiceAritmetico = 0;
    private int indiceRelacional = 0;
    int indice = 0;
    ArrayList<String> codigoTresEnderecos = new ArrayList<>();
    ArrayList<String> codigoTresEnderecosRelacional = new ArrayList<>();
    ArrayList<String> ordemDeVariaveis = new ArrayList<>();
    final String regexParenteses = "\\((.*?)\\)";
    final String regexVar = "(.*?)\\s*=";
    
    ArrayList<String> codigoTresEnderecosFinal = new ArrayList<>();
    ArrayList<String> ordemDeCondicoes = new ArrayList<>();
    int indiceBloco = 0;

    
    public void gerarCodigo(){
                        
        for(int i=0;i<ordemOperacoes.size();i++){
            if(ordemOperacoes.get(i).equals("ARITMETICA")){

                String[] aritmetico = operacoesAritmetica.get(indiceAritmetico);
                String op = "";
                for(int j=0;j<aritmetico.length;j++){
                    op = op + aritmetico[j] + " ";
                }
                
                Pattern pattern = Pattern.compile(regexParenteses, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(op);
                ArrayList<String> operacoesComParenteses = new ArrayList<>();
                
                while (matcher.find()) {
                    for (int k = 1; k <= matcher.groupCount(); k++) {
                        operacoesComParenteses.add(matcher.group(k));
                    }
                }
                                
                for (int k = 0; k < operacoesComParenteses.size(); k++) {
                    gerarArvoreAritmetica(operacoesComParenteses.get(k),true);
                    op = op.replace("("+operacoesComParenteses.get(k)+")", "ALTER");
                }
                
                pattern = Pattern.compile(regexVar, Pattern.MULTILINE);
                matcher = pattern.matcher(op);
                
                while (matcher.find()) {
                    for (int k = 1; k <= matcher.groupCount(); k++) {
                        ordemDeVariaveis.add(matcher.group(k));
                    }
                }
                
                gerarArvoreAritmetica(op,false);

                indiceAritmetico++;
            }else if(ordemOperacoes.get(i).equals("INICIO RELACIONAL")){
                String[] relacional = operacoesRelacionais.get(indiceRelacional);
                String op = "";
                for(int j=0;j<relacional.length;j++){
                   op = op + relacional[j] + " ";
                }
                
                ordemDeCondicoes.add(op);
                codigoTresEnderecos.add("Inicio Relacional");
                gerarArvoreRelacional(op);
            }else{
                codigoTresEnderecos.add("Fim Relacional");
                indiceRelacional++;
            }
        }
           
        exibirCodigo();
        ConverterPHP converter = new ConverterPHP();
        converter.gerarAlvo(codigoTresEnderecosFinal);
    }
    
    public void gerarArvoreRelacional(String operacao){
        ArvoreCondicional arvore = new ArvoreCondicional();
        ArrayList<String[]> miniArvores = arvore.main(operacao);
    }
    
    public void gerarArvoreAritmetica(String operacao,boolean parenteses){
        ArvoreAritmetica arvore = new ArvoreAritmetica();
        ArrayList<String[]> miniArvores = arvore.main(operacao);
                
        ArrayList<String> multiplicacoesAndDivisoes = new ArrayList<>(); 
        String multiplicacaoAndDivisao = "";
        int cont = 0;
                
        for(int i = miniArvores.size()-1; i >= 0; i--) {
            String[] expMiniArvore = miniArvores.get(i);
            if(expMiniArvore[0].equals("/") || expMiniArvore[0].equals("*")){
                if(cont == 0){
                    multiplicacaoAndDivisao = expMiniArvore[1]+ " " + expMiniArvore[0] + " " + expMiniArvore[2];
                    codigoTresEnderecos.add(multiplicacaoAndDivisao);
                }else{
                    multiplicacaoAndDivisao = expMiniArvore[1]+ " " + expMiniArvore[0] + " TX";
                    codigoTresEnderecos.add(multiplicacaoAndDivisao);
                }  
                cont++;
            }else{
                if(!multiplicacaoAndDivisao.equals("")) {
                    multiplicacoesAndDivisoes.add(multiplicacaoAndDivisao);
                }
                cont = 0;
                multiplicacaoAndDivisao = "";
            }
        }
        
        if(!multiplicacaoAndDivisao.equals("")) {
            multiplicacoesAndDivisoes.add(multiplicacaoAndDivisao);
            cont = 0;
            multiplicacaoAndDivisao = "";
        }
        
        int indiceMultiplicacoesAndDivisoes = 0;
                
        for(int i = miniArvores.size()-1; i >= 0; i--) {
            if(i != 0){
                String[] expMiniArvore = miniArvores.get(i);
                String[] expMiniArvoreAnterior = miniArvores.get(i-1);
                String[] expMiniArvorePosterior = {""};
                if((i+1) != miniArvores.size()) {
                    expMiniArvorePosterior = miniArvores.get(i+1);
                }
                if(expMiniArvore[0].equals("+") || expMiniArvore[0].equals("-")){
                    
                    if(expMiniArvorePosterior[0].equals("/") || expMiniArvorePosterior[0].equals("*")){
                        //codigoTresEnderecos.add("transicao aqui");
                    }
                    
                    if(expMiniArvoreAnterior[0].equals("/") || expMiniArvoreAnterior[0].equals("*")){
                        if(indiceMultiplicacoesAndDivisoes == 0){
                            String valor = "TX " + expMiniArvore[0] + " " + expMiniArvore[2];
                            codigoTresEnderecos.add(valor);
                        }else{
                            String valorAnterior = expMiniArvoreAnterior[1] + " " + expMiniArvoreAnterior[0] + " " + expMiniArvoreAnterior[2];
                            codigoTresEnderecos.add(valorAnterior);
                            
                            String valor = "TX " + expMiniArvore[0] + " TZ";
                            codigoTresEnderecos.add(valor);
                        }
                        indiceMultiplicacoesAndDivisoes++;
                    }else{
                        if(i == miniArvores.size()-1){
                            String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " " + expMiniArvore[2];
                            codigoTresEnderecos.add(valor);
                        }else{
                            String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " TX";
                            codigoTresEnderecos.add(valor);
                        }
                    }
                }
            }else if(miniArvores.size() > 1){
                String[] expMiniArvore = miniArvores.get(i);
                String[] expMiniArvorePosterior = miniArvores.get(i+1);
                String[] expMiniArvoreAposPosterior = miniArvores.get(i+2);
                if(expMiniArvore[0].equals("+") || expMiniArvore[0].equals("-")){
                    if(expMiniArvorePosterior[0].equals("+") || expMiniArvorePosterior[0].equals("-")){
                        if(expMiniArvoreAposPosterior[0].equals("+") || expMiniArvoreAposPosterior[0].equals("-")){
                            String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " TX";
                            codigoTresEnderecos.add(valor);
                        }else{
                            String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " " + expMiniArvore[2];
                            codigoTresEnderecos.add(valor);

                            String valor2 = "TX " + expMiniArvorePosterior[0] + " TZ";
                            codigoTresEnderecos.add(valor2);
                        }
                    }else{
                        String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " TX";
                        codigoTresEnderecos.add(valor);
                    }
                }
            }else{
                String[] expMiniArvore = miniArvores.get(i);
                String valor = expMiniArvore[1] + " " + expMiniArvore[0] + " " + expMiniArvore[2];
                codigoTresEnderecos.add(valor);
            }
        }
        
        if(miniArvores.isEmpty()){
            String[] value = operacao.split("=");
            codigoTresEnderecos.add(value[1]);
            codigoTresEnderecos.add("FIM");
        }else{
            if(!parenteses){
                codigoTresEnderecos.add("FIM");
            }       
        }
                        
    }
    
    public void exibirCodigo(){
        int indiceOrdemVariaveis = 0;
        int indiceOrdemCondicoes = 0;
        
        System.out.println("\n------------------------------------------------------\n");
        
        for(int i=0;i<codigoTresEnderecos.size();i++){
            
            if(!codigoTresEnderecos.get(i).equals("Inicio Relacional") && !codigoTresEnderecos.get(i).equals("Fim Relacional")){
            
                if(!codigoTresEnderecos.get(i).equals("FIM")){
                    String resultado;
                    resultado = codigoTresEnderecos.get(i).replace("TX", "TX"+(indice));
                    resultado = resultado.replace("TZ", "TX"+(indice-1));
                    System.out.println("    TX"+(indice+1)+" = "+resultado);
                    codigoTresEnderecosFinal.add("    TX"+(indice+1)+" = "+resultado);
                    indice++;
                }else{
                    if(i!=0){
                        System.out.println("    "+ordemDeVariaveis.get(indiceOrdemVariaveis)+" = TX"+(indice));
                        codigoTresEnderecosFinal.add("    "+ordemDeVariaveis.get(indiceOrdemVariaveis)+" = TX"+(indice));
                        indiceOrdemVariaveis++;
                    }
                }
            
            }else if(codigoTresEnderecos.get(i).equals("Fim Relacional")){
                indiceBloco++;
                //System.out.println("Fim Relacional");
                System.out.println("L"+indiceBloco/*+" - Fim Relacional"*/);
                codigoTresEnderecosFinal.add("L"+indiceBloco/*+" - Fim Relacional"*/);
            }else{
                indiceBloco++;
                //System.out.println("Inicio Relacional");
                System.out.println("L"+indiceBloco/*+" - Inicio Relacional"*/);
                codigoTresEnderecosFinal.add("L"+indiceBloco/*+" - Inicio Relacional"*/);
                System.out.println("    TX"+(indice+1)+" = "+ordemDeCondicoes.get(indiceOrdemCondicoes));
                codigoTresEnderecosFinal.add("    TX"+(indice+1)+" = "+ordemDeCondicoes.get(indiceOrdemCondicoes));
                indice++;
                System.out.println("    if TX"+(indice)+" == 0 goto L"+(indiceBloco+1));
                codigoTresEnderecosFinal.add("    if TX"+(indice)+" == 0 goto L"+(indiceBloco+1));
                indiceOrdemCondicoes++;
            }
        }          
    }
    
    
}
