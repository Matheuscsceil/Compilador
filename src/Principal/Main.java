package Principal;

import java.io.BufferedReader;
import scanner.ScannerLexico;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import parser.ParserSintatico;

public class Main {
        
    public static String codigo = "";
    
    public static void main(String[] args){

        String enderecoArquivo = "compiler.txt";
        
        try{
            
            BufferedReader entrada = new BufferedReader(new FileReader(enderecoArquivo));
            String line = "";
            String texto = "";
            while((line = entrada.readLine()) != null){ 
                texto = texto + line.trim();
            }
            
            System.out.println(texto);
                       
            Scanner scan = new Scanner(/*codigo*/texto);
            ScannerLexico scanner = new ScannerLexico(scan); 

            ParserSintatico parser = new ParserSintatico(scanner);
            parser.run();

        }catch(FileNotFoundException e){
            System.out.println("Nenhum arquivo encontrado: "+e);
        }catch(IOException e){
            System.out.println("Nenhum arquivo encontrado: "+e);
        }
               
    }
    
    public void executar(){
        System.out.println(codigo.trim());
        Scanner scan = new Scanner(codigo.trim());
        ScannerLexico scanner = new ScannerLexico(scan); 

        ParserSintatico parser = new ParserSintatico(scanner);
        parser.run();
    }
    
}