package freecell;

public class RegraVazia extends PilhaCarta{

    @Override
    public boolean regras(Carta card) {
        return tamanho() == 0;
        // n�o permite colocar mais cartas, somente se estiver vazio
    }
}
