package freecell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;

class PaineldeCarta extends JComponent implements
        MouseListener,
        MouseMotionListener,
        ChangeListener {
    private static final int NUMERO_DE_PILHAS = 8;
    
    //... Constants specifying position of display elements
    private static final int GAP = 10;
    private static final int FOUNDATION_TOP = GAP;
    private static final int FOUNDATION_BOTTOM = FOUNDATION_TOP + Carta.CARD_HEIGHT;
    
    private static final int FREE_CELL_TOP = GAP + FOUNDATION_TOP;
    private static final int FREE_CELL_BOTTOM = FREE_CELL_TOP + Carta.CARD_HEIGHT;
    
    private static final int TELA_TOP = 2 * GAP +
            Math.max(FOUNDATION_BOTTOM, FREE_CELL_BOTTOM);
    private static final int TELA_INCR_Y  = 15;
    private static final int TELA_START_X = GAP;
    private static final int TELA_INCR_X  = Carta.CARD_WIDTH + GAP;
    
    private static final int DISPLAY_WIDTH = GAP + NUMERO_DE_PILHAS * TELA_INCR_X;
    private static final int DISPLAY_HEIGHT = TELA_TOP + 3 * Carta.CARD_HEIGHT + GAP;
    
    private static final Color BACKGROUND_COLOR = new Color(146, 143, 143);
    
    //=================================================================== fields
    //coordenadas das inagens
    private final int _initX     = 0;   // x coord - set from drag
    private final int _initY     = 0;   // y coord - set from drag
    
    /** Position in image of mouse press to make dragging look better. */
    private int arrastaX = 0;  // Displacement inside image of mouse press.
    private int arrastaY = 0;
    
    //... Selected card and its pile for dragging purposes.
    private Carta     arrastacarta = null;  // Current draggable card.
    private PilhaCarta arrastaparapilha = null;  // Which pile it came from.
    
    //... Remember where each pile is located.
    private final IdentityHashMap<PilhaCarta, Rectangle> _whereIs =
            new IdentityHashMap<PilhaCarta, Rectangle>();
    
    private final Jogo modelo;

    PaineldeCarta(Jogo modelo) {
        //... Save the model.
        this.modelo = modelo;
        
        //... Initialize graphics
        setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        setBackground(Color.gray);
        
        //... Add mouse listeners.
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        //... Set location of all piles in model.
        int x = TELA_START_X;   // Initial x position.
        for (int pileNum = 0; pileNum < NUMERO_DE_PILHAS; pileNum++) {
            PilhaCarta p;
            if (pileNum < 4){
                p = this.modelo.getFreeCellPilha(pileNum);
                _whereIs.put(p, new Rectangle(x, FREE_CELL_TOP, Carta.CARD_WIDTH,
                        Carta.CARD_HEIGHT));
            }else{
                p = modelo.getFoundationPile(pileNum - 4);
                _whereIs.put(p, new Rectangle(x, FOUNDATION_TOP, Carta.CARD_WIDTH,
                        Carta.CARD_HEIGHT));
            }
            
            p = modelo.getTelaPilha(pileNum);
            _whereIs.put(p, new Rectangle(x, TELA_TOP, Carta.CARD_WIDTH,
                    3 * Carta.CARD_HEIGHT));
            
            x += TELA_INCR_X;
        }
        
        modelo.ChangeListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        //... Paint background.
        int width  = getWidth();
        int height = getHeight();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);   // Restore pen color.
        
        //mostra cada carta
        for (PilhaCarta pile : this.modelo.getFreeCellPilhas()) {
            desenhaPilha(g, pile, true);
        }
        for (PilhaCarta pile : this.modelo.getPilhaAuxiliar()) {
            desenhaPilha(g, pile, true);
        }
        for (PilhaCarta pile : this.modelo.getTelaPilhas()) {
            desenhaPilha(g, pile, false);
        }
        if (arrastacarta != null) {
            arrastacarta.desenhar(g);
        }
    }
    private void desenhaPilha(Graphics g, PilhaCarta pilha, boolean topOnly) {
        Rectangle loc = _whereIs.get(pilha);
        g.drawRect(loc.x, loc.y, loc.width, loc.height);
        int y = loc.y;
        if (pilha.tamanho() > 0) {
            if (topOnly) {
                Carta card = pilha.Top();
                if (card != arrastacarta) {
                    card.setPosicao(loc.x, y);
                    card.desenhar(g);
                }
            } else {
                //... Draw all cards.
                //    Another possibility is that instead of only associating
                //    a Rectangle with a pile, other rendering hints should
                //    be recorded, such as whether to show only the top card.
                for (Carta card : pilha) {
                    if (card != arrastacarta) {
                        //... Draw only non-dragged card.
                        card.setPosicao(loc.x, y);
                        card.desenhar(g);
                        y += TELA_INCR_Y;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();   // Save the x coord of the click
        int y = e.getY();   // Save the y coord of the click
        
        //... Find card image this is in.  Check top of every pile.
        arrastacarta = null;  // Assume not in any image.
        for (PilhaCarta pile : this.modelo) {
            if (pile.remover() && pile.tamanho() > 0) {
                Carta testCard = pile.Top();
                if (testCard.isInside(x, y)) {
                    arrastaX = x - testCard.getX();  // how far from left
                    arrastaY = y - testCard.getY();  // how far from top
                    arrastacarta = testCard;  // Remember what we're dragging.
                    arrastaparapilha = pile;
                    break;   // Stop when we find the first match.
                }
            }
        }
    }
    
    //============================================================= stateChanged
    // Implementing ChangeListener means we had to define this.
    // Because we added ourselves as a change listener in the model,
    // This method will be called whenever anything changes in the model.
    // All we have to do is repaint.
    @Override
    public void stateChanged(ChangeEvent e) {
        limpar();     // Perhaps not needed, but this makes sure.
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (arrastacarta == null) {
            return;
        }
        int newX;
        int newY;
        
        newX = e.getX() - arrastaX;
        newY = e.getY() - arrastaY;
        
        //... Don't move the image off the screen sides
        newX = Math.max(newX, 0);
        newX = Math.min(newX, getWidth() - Carta.CARD_WIDTH);
        
        //... Don't move the image off top or bottom
        newY = Math.max(newY, 0);
        newY = Math.min(newY, getHeight() - Carta.CARD_HEIGHT);
        
        arrastacarta.setPosicao(newX, newY);
        //redesenha a carta, pois seu estado mudou
        this.repaint();
    }
    
    //============================================================ mouseReleased
    @Override
    public void mouseReleased(MouseEvent e) {
        if (arrastaparapilha != null) {
            int x = e.getX();
            int y = e.getY();
            PilhaCarta targetPile = _findPileAt(x, y);
            if (targetPile != null) {
                //... Move card.  This may not actually move if illegal.
                this.modelo.moverPilhaparaPilha(arrastaparapilha, targetPile);
            }
            limpar();
            this.repaint();
        }
    }
    //=============================================================== limpa carta
    // After mouse button is released, clear the drag info, otherwise
    //    paintComponent will still try to display a dragged card.
    //    Perhaps this is overly cautious.
    private void limpar() {
        arrastacarta = null;
        arrastaparapilha = null;
    }

    private PilhaCarta _findPileAt(int x, int y) {
        for (PilhaCarta pilha : this.modelo) {
            Rectangle loc = _whereIs.get(pilha);
            if (loc.contains(x, y)) {
                return pilha;
            }
        }
        
        return null;
    }

    @Override
    public void mouseMoved  (MouseEvent e) {} 
    @Override
    public void mouseEntered(MouseEvent e) {}  
    @Override
    public void mouseClicked(MouseEvent e) {} 
    @Override
    public void mouseExited(MouseEvent e) {}

}
