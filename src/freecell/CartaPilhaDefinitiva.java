package freecell;

public class CartaPilhaDefinitiva extends PilhaCarta {

    @Override
    public boolean regras(Carta card) {
        // a primeira carta deve ser A
        if (card.getTipo() == TipoCarta.A) {
            return true;
        }
        if (tamanho() > 0) {
            Carta top;
            top = Top();
            if ((top.getCor() == card.getCor() &&
                    (top.getTipo().ordinal() + 1 == card.getTipo().ordinal()))) {
                return true;
            }
        }
        return false;
    }
    //não remove na pilha definitiva
    @Override
    public boolean remover() {
        return false;
    }
}
