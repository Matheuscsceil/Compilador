package Arvore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ArvoreAritmetica {
    
    Scanner scan;
    List<String> Especiais = Arrays.asList("/","*","+", "-");
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
        
        encontraEspeciais();
        percorreArvore(node);
                
        for(No n: nosPai){
            String[] expTres = new String[3];
            //System.out.print("Nó pai: '"+n.valor+"'  (esquerda: '"+n.e.valor+"')  ");
            expTres[0] = n.valor;
            expTres[1] = n.e.valor;
            if(Especiais.contains(n.d.valor)){
                //System.out.println("(direita: '"+n.d.e.valor+"')");
                expTres[2] = n.d.e.valor;
            }else {
                //System.out.println("(direita: '"+n.d.valor+"')");
                expTres[2] = n.d.valor;
            }
            
            expTresEnderecos.add(expTres);
        }
                
        return expTresEnderecos;
    }
    
    public void encontraEspeciais(){
        No no_raiz = node;
        //Percorre o arraylist, procura por 'Especiais'
        for(int i = 0 ; i < s.size(); i++){
            if(Especiais.contains(s.get(i))){
                noDisponivel();
                inserirArvore(i);
                nosPai.add(node);
            }
        }
        
        //Insere o último valor da string
        noDisponivel();
        node.valor = s.get(s.size()-1);
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
        //Insere o valor atual e a valor à esquerda
        node.valor = s.get(j);
        node.noE(s.get(j-1));
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
        }
        node = anterior;
    }
    
}
