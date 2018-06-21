package parser;

import scanner.Token;

public class Simbolo {
    //tipos possiveis: int:2 / float:3
        
    private int tipo;
    private String lexema;
    private int escopo;

    public Simbolo(Token token, int escopo, int tipo) {
        this.escopo = escopo;
        lexema = token.getLexema();
        this.tipo = tipo;
    }

    public int getEscopo() {
        return escopo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getTipo() {
        return tipo;
    }
        
}
