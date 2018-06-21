package Arvore;

public class No {
    public String valor;
    public No e = null;
    public No d = null;
    
    public No(String valor){
        this.valor = valor;
    }
    
    public void noE(String valor){
        this.e = new No(valor);
    }
    
    public void noD(String valor){
        this.d = new No(valor);
    }
}
