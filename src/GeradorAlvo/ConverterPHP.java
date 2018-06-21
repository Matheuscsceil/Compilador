package GeradorAlvo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import parser.ParserSintatico;

public class ConverterPHP {
    
    final String regexDeclaracaoVariaveis = "(int|float)+\\s*([A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇ<>Ñ_,0-9]+)\\;";;
    final String regexVar = "([A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ_0-9]+)";
    
    private ArrayList<String> variaveis = new ArrayList<>();
    
    public void gerarAlvo(ArrayList<String> codigoTresEnderecosFinal){
        System.out.println("\n------------------------------------------------------\n");
        System.out.println("<?php");
        
        String enderecoArquivo = "compiler.txt";
        
        try{
            
            BufferedReader entrada = new BufferedReader(new FileReader(enderecoArquivo));
            String line = "";
            String texto = "";
            while((line = entrada.readLine()) != null){ 
                texto = texto + line + "\n";
            }
            
            Pattern pattern = Pattern.compile(regexDeclaracaoVariaveis, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(texto);

            while (matcher.find()) {
                texto = texto.replace(matcher.group(0), "");
            }
                        
            texto = texto.substring(7, texto.length()-2).replaceAll("\n{2,}", "\n");
            texto = texto.replaceFirst("\\s*", "\n   ");
            
            pattern = Pattern.compile(regexVar, Pattern.MULTILINE);
            matcher = pattern.matcher(texto);
              
            ArrayList<String> variaveisDeclaradas = new ArrayList<String>();
            for(int i=0;i<ParserSintatico.tabelaSimbolos.size();i++){
                variaveisDeclaradas.add(ParserSintatico.tabelaSimbolos.get(i).getLexema());
            }
            
            texto = texto.replace("while", "§");
            while (matcher.find()) {
                if(!eNaoVar(matcher.group(0))){
                    if(!variaveis.contains(matcher.group(0)) && variaveisDeclaradas.contains(matcher.group(0))){
                        System.out.println(matcher.group(0));
                        texto = texto.replace(matcher.group(0), "$"+matcher.group(0));
                        variaveis.add(matcher.group(0));
                    }
                }
            }
            texto = texto.replace("§", "while");
            
            System.out.println(texto);
            
        }catch(FileNotFoundException e){
            System.out.println("Nenhum arquivo encontrado: "+e);
        }catch(IOException e){
            System.out.println("Nenhum arquivo encontrado: "+e);
        }
        
    }
    
    public boolean eNaoVar(String codigo){
        Pattern pattern = Pattern.compile(regexVar);
        Matcher matcher = pattern.matcher(codigo);
        
        if (matcher.find()) {
            if(matcher.group(0).equals("while")){
                return true;
            }
        }
        
        return false;
    }
    
}
