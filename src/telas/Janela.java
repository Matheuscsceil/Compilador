package telas;

import Principal.Main;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import parser.OperacaoAritmetica;
import parser.OperacaoRelacional;
import parser.ParserSintatico;

public class Janela extends JFrame implements ActionListener {
    
    private JComboBox fontes = new JComboBox();
    private JComboBox tamanho = new JComboBox();
    private JTextPane areaDeTexto = new JTextPane();
    private JPanel painelEstilo = new JPanel();
    private JScrollPane scroll = new JScrollPane();
    private JColorChooser escolherCor = new JColorChooser();
    private JButton botao = new JButton("COMPILAR");
    
    public Janela() {
        configuraJanela();
        scroll.add(areaDeTexto);
        scroll.setViewportView(areaDeTexto);
        this.add(scroll, BorderLayout.CENTER);
        botao.addActionListener(this);
        fontes.addActionListener(this);
        tamanho.addActionListener(this);
        painelEstilo.add(botao);
        adicionaTamanhosNaCombo(tamanho);
        adicionaFontesNaCombo(fontes);
        tamanho.setSelectedIndex(3);
        fontes.setSelectedIndex(2);
        painelEstilo.add(fontes);
        painelEstilo.add(tamanho);
        this.add(painelEstilo, BorderLayout.SOUTH);
        this.setVisible(true);
    }
    private void adicionaFontesNaCombo(JComboBox combo) {
        String[] fontes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fonte : fontes) {
            combo.addItem(fonte);
        }
    }
    private void adicionaTamanhosNaCombo(JComboBox combo) {
        for (int i = 8; i <= 72; i += 2) {
            combo.addItem(i);
        }
    }
    private void configuraJanela() {
        this.setSize(700, 400);
        this.setLocationRelativeTo(null);
        this.setTitle("COMPILADOR");
        this.setLayout(new GridLayout(2, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "COMPILAR") {
            for (int i = 0; i < 50; ++i)System.out.println ();

            OperacaoRelacional.pilha = new ArrayDeque<>();
            OperacaoAritmetica.pilha = new ArrayDeque<>();
            OperacaoAritmetica.operacao = new ArrayList<>();
            ParserSintatico.tabelaSimbolos = new ArrayList<>();
            
            Main main = new Main();
            try{
                Main.codigo = areaDeTexto.getText().replaceAll("\\s+"," ");
                main.executar();
            }catch(Exception ex){
                System.out.println("NAO FOI POSSIVEL ANALISAR CODIGO");
            }
            
            if(!ParserSintatico.errou){
                JOptionPane.showMessageDialog(null, "COMPILADO COM SUCESSO");  
            }
            
            ParserSintatico.errou = false;
            
        }
        modificaEstilo();
    }
    public static void main(String[] args) {
        new Janela();
    }
    public void modificaEstilo() {
        StyledDocument documento = (StyledDocument) areaDeTexto.getDocument();
        Style estilo = documento.getStyle(documento.addStyle("StyleAdd", null).getName());

        // fonte
        String fonte = (String) fontes.getSelectedItem();
        if (fonte != null) {
            StyleConstants.setFontFamily(estilo, fonte);
        }
        // tamanho
        int tamanhoFonte = 8;
        tamanhoFonte = (Integer) tamanho.getSelectedItem();
        StyleConstants.setFontSize(estilo, tamanhoFonte);
        String textoFormatado = areaDeTexto.getText();
        areaDeTexto.setText("");
        documento.addStyle("Style", estilo);
        try {
            documento.insertString(documento.getLength(), textoFormatado,estilo);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

