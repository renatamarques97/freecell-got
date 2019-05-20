package freecell;

import java.util.*;

public class PilhaCarta implements Iterable<Carta> {
    private final ArrayList<Carta> cards = new ArrayList<Carta>(); // array de carta

    public void empurraSemRegra(Carta newCard) {
        cards.add(newCard);
    }

    public Carta fixaSemRegra() {
        int lastIndex = tamanho()-1;
        Carta nova = cards.get(lastIndex);
        cards.remove(lastIndex);
        return nova;
    }
   
    public boolean empurra(Carta newCard) {
        if (regras(newCard)) {
            cards.add(newCard);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean regras(Carta card) {
        return true;
    }

    public int tamanho() {
        return cards.size();
    }

    public Carta fixa() { //fixa a carta em outra pilha
        if (!remover()) {
            throw new UnsupportedOperationException("não pode remover");
        }
        return fixaSemRegra();
    }

    public void embaralhar() {
        Collections.shuffle(cards);
    }

    public Carta Top() { //topo da pilha
        return cards.get(cards.size() - 1);
    }

    @Override
    public Iterator<Carta> iterator() {
        return cards.iterator();
    }

    public ListIterator<Carta> reverseIterator() {
        return cards.listIterator(cards.size());
    }

    public void limpar() {
        cards.clear();
    }

    public boolean remover() {
        return true;
    }
}

