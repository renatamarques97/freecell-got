package freecell;

import java.awt.*;
import javax.swing.*;

public class Carta {
    
    //================================================================ constants
    public static final int CARD_WIDTH;  // Initialized in static initilizer.
    public static final int CARD_HEIGHT; // Initialized in static intializer..
    
    private static final String    IMAGEM_PASTA = "/cardimages/";
    private static final ImageIcon BACK_IMAGEM;  // Image of back of card.
    
    private static final Class CLASS = Carta.class;
    private static final String NOME_PACOTE;
    private static final ClassLoader CLSLDR;
    
    //======================================================= static initializer
    static {
        NOME_PACOTE = CLASS.getPackage().getName();
        CLSLDR = CLASS.getClassLoader();
        String pastaimagens;
        pastaimagens = NOME_PACOTE + IMAGEM_PASTA + "b.gif";
        java.net.URL imageURL = CLSLDR.getResource(pastaimagens);
        BACK_IMAGEM = new ImageIcon(imageURL);
        // largura e altura das cartas
        CARD_WIDTH  = BACK_IMAGEM.getIconWidth();
        CARD_HEIGHT = BACK_IMAGEM.getIconHeight();
    }

    private TipoCarta tipo;
    private CartaCor  cor;
    private ImageIcon faceImage;
    private int     x;
    private int     y;
    private boolean carta  = true;

    public Carta(TipoCarta face, CartaCor suit) {
        tipo = face;
        cor = suit;
        x = 0;
        y = 0;
        carta = false;
        
        //ordem das cartas
        char tipoChar = "a23456789tjqk".charAt(tipo.ordinal());
        char corChar = "shcd".charAt(cor.ordinal());
        // nome do arquivo é o tipo da carta + cor + extensão do arquivo
        String cardFilename = "" + tipoChar + corChar + ".gif";
        
        String path = NOME_PACOTE + IMAGEM_PASTA + cardFilename;
        java.net.URL imageURL = CLSLDR.getResource(path);
        
        faceImage = new ImageIcon(imageURL);
    }

    public TipoCarta getTipo() {
        return tipo; 
    }

    public CartaCor getCor() {
        return cor;
    }

    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void desenhar(Graphics g) {
            faceImage.paintIcon(null, g, this.x, this.y);
    }
    
    public boolean isInside(int x, int y) {
        return (x >= this.x && x < this.x+CARD_WIDTH) && (y >= this.y && y < this.y+CARD_HEIGHT);
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void turnFaceUp() {
        carta = true;
    }
}