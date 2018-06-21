package Arvore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ArvoreCondicional {
    Scanner scan;
    List<String> Especiais_AND_OR = Arrays.asList("&&","||");
    List<String> Especiais_outros = Arrays.asList(
            "==", "!=",
            "<", "<=",
            ">", ">="
    );
    No node = new No(null);
    ArrayList<String> s = new ArrayList<>();
    ArrayList<No> nosPai = new ArrayList<>();
    
    ArrayList<String[]> expTresEnderecos = new ArrayList<>();
    
    public ArrayList<String[]> main(String expressao){
        String entrada = expressao;
        scan = new Scanner(entrada);
        
        while(scan.hasNext()){
            s.add(scan.next());
        }
        
        char_AND_OR();
        percorreArvore(node);
//        for(No n: nosPai){
//            System.out.println("Nó pai: '"+n.valor+"'  (esquerda: '"+n.e.valor+"')  (direita: '"+n.d.valor+"')");
//        }
        
        for(No n: nosPai){
            String[] expTres = new String[3];
            expTres[0] = n.valor;
            expTres[1] = n.e.valor;
            expTres[2] = n.d.valor;
    
            expTresEnderecos.add(expTres);
        }
                
        return expTresEnderecos;
        
    }
    
    public void char_AND_OR(){
        No no_raiz = node;
        //Percorre o arraylist, procura por "||" ou "&&"
        for(int i = 0 ; i < s.size(); i++){
            if(Especiais_AND_OR.contains(s.get(i))){
                noDisponivel();
                inserirArvore(i);
            }
            node = no_raiz;
        }
        
        //Percorre o arraylist de trás para frente, pega a última operação (Especiais_outros)
        for(int i = s.size()-1; i > -1; i--){
            if(Especiais_outros.contains(s.get(i))){
                noDisponivel();
                condicional(i);
                break;
            }
        }
        node = no_raiz;
    }
    
    public void noDisponivel(){
        //Verifica se a árvore já foi criada
        if (node.valor != null) {
            //Percorrer p/ a direita até encontrar um nó vazio
            while(node.d != null) node = node.d;
            node.noD(null);
            node = node.d;
        }
    }
    
    public void inserirArvore(int j) {
        //Insere o valor "&&" ou "||" no nó
        node.valor = s.get(j);
        //Direciona para o nó da esquerda
        node.noE(null);
        node = node.e;
        
        //Percorre o arraylist, ao contrário, a partir do 'posicao'
        for (int i = (j-1); i > -1; i--){
            //Quando alcançar um "||" ou "&&", o laço pára
            if(Especiais_AND_OR.contains(s.get(i))) break;
            
            if(Especiais_outros.contains(s.get(i))) condicional(i);
        }
        
    }
    
    public void condicional(int j){
        //Insere valor da operação no nó (Especiais_outros)
        node.valor = s.get(j);
        //Insere valor nos filhos da direita e esquerda
        node.noE(s.get(j-1));
        node.noD(s.get(j+1));    
    }
    
    public void percorreArvore(No anterior){
        No n_atual = node;
        //System.out.println(node.valor);
        
        if(node.e != null){
            node = node.e;
            percorreArvore(n_atual);
        }
        if(node.d != null){
            node = node.d;
            percorreArvore(n_atual);
            encontraPai(n_atual);
        }
        node = anterior;
        //encontraTrio(node);
    }
    
    public void encontraPai(No pai){
        if(pai.e.e == null || pai.e.d == null) nosPai.add(pai);
    }
    
}
