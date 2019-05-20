package freecell;

public class CartaPilhaAux extends PilhaCarta {

    @Override
    public boolean regras(Carta card) {
        return (this.Top().getTipo().ordinal() - 1 == card.getTipo().ordinal() &&
                this.Top().getCor().getCor() != card.getCor().getCor());
    }
}
