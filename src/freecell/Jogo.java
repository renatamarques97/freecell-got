package freecell;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class Jogo implements Iterable<PilhaCarta> {

    private final PilhaCarta[] freeCell;
    private final PilhaCarta[] tela;
    private final PilhaCarta[] auxiliar;
    
    private final ArrayList<PilhaCarta> pilhas;
    
    private final ArrayList<ChangeListener> changeListeners;
    private final ArrayDeque<PilhaCarta> desempilhar;

    public Jogo() {        
        this.desempilhar = new ArrayDeque<PilhaCarta>();
        pilhas = new ArrayList<PilhaCarta>();
        
        freeCell  = new PilhaCarta[4];
        tela    = new CartaPilhaAux[8];
        auxiliar = new PilhaCarta[4];

        for (int pile = 0; pile < auxiliar.length; pile++) {
            auxiliar[pile] = new CartaPilhaDefinitiva();
            pilhas.add(auxiliar[pile]);
        }
        
        //... Create empty piles of Free Cells.
        for (int pile = 0; pile < freeCell.length; pile++) {
            freeCell[pile] = new RegraVazia(); // não permite colocar mais cartas
            pilhas.add(freeCell[pile]);
        }
        
        //... Arrange the cards into piles.
        for (int pile = 0; pile < tela.length; pile++) {
            tela[pile] = new CartaPilhaAux();
            pilhas.add(tela[pile]);
        }
        
        changeListeners = new ArrayList<ChangeListener>();
        
        reset();
    }

    public void reset() {
        Deck deck = new Deck();
        deck.embaralhar();

        for (PilhaCarta p : pilhas) {
            p.limpar();
        }

        int whichPile = 0;
        for (Carta crd : deck) {
            tela[whichPile].empurraSemRegra(crd);
            whichPile = (whichPile + 1) % tela.length;
        }

        mudancas();
    }
    
    //TODO: This is a little messy right now, having methods that both 
    //      return a pile by number, and the array of all piles.
    //      Needs to be simplified.

    @Override
    public Iterator<PilhaCarta> iterator() {
        return pilhas.iterator();
    }

    public PilhaCarta getTelaPilha(int i) {
        return tela[i];
    }

    public PilhaCarta[] getTelaPilhas() {
        return tela;
    }

    public PilhaCarta[] getFreeCellPilhas() {
        return freeCell;
    }

    public PilhaCarta getFreeCellPilha(int cellNum) {
        return freeCell[cellNum];
    }
    
    public PilhaCarta[] getPilhaAuxiliar() {
        return auxiliar;
    }

    public PilhaCarta getFoundationPile(int cellNum) {
        return auxiliar[cellNum];
    }

    public boolean moverPilhaparaPilha(PilhaCarta source, PilhaCarta target) {
        boolean result = false;
        if (source.tamanho() > 0) {
            Carta crd = source.Top();
            if (target.regras(crd)) {
                target.empurra(crd);
                source.fixa();
                mudancas();
                //... Record on undo stack.
                desempilhar.push(source);
                desempilhar.push(target);
                result = true;
            }
        }
        return result;
    }
    
    private void moverpilha(PilhaCarta source, PilhaCarta target) {
        if (source.tamanho() > 0) {
            target.empurra(source.fixa());
            mudancas();
        }
    }

    public void fazerJogo() {
        
        boolean worthTrying;  // verdadeiro se houver movimento
        do {
            worthTrying = false;  // Assume nothing is going to be moved.
            //... Try moving each of the free cells to graveyard.
            for (PilhaCarta freePilha : freeCell) {
                for (PilhaCarta gravePilha : auxiliar) {
                    worthTrying |= moverPilhaparaPilha(freePilha, gravePilha);
                }
            }

            for (PilhaCarta cardPile : tela) {
                for (PilhaCarta gravePilha : auxiliar) {
                    worthTrying |= moverPilhaparaPilha(cardPile, gravePilha);
                }
            }
            
        }while (worthTrying);
    }
    
    //======================================================== addChangeListener
    public void ChangeListener(ChangeListener someoneWhoWantsToKnow) {
        changeListeners.add(someoneWhoWantsToKnow);
    }
    
    //================================================= _notifyEveryoneOfChanges
    private void mudancas() {
        for (ChangeListener interestedParty : changeListeners){
            interestedParty.stateChanged(new ChangeEvent("mudança no jogo"));
        }
    }
}