package freecell;

public class Deck extends PilhaCarta {

    public Deck() {
        for (CartaCor s : CartaCor.values()) {
            for (TipoCarta f : TipoCarta.values()) {
                Carta c = new Carta(f, s);
                c.turnFaceUp();
                this.empurra(c);
            }
        }
        embaralhar();
    }
}
