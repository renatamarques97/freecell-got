package freecell;

import java.awt.Color;

enum CartaCor {
    ESPADAS(Color.BLACK),
    COPAS(Color.RED),
    PAUS(Color.BLACK),
    OUROS(Color.RED);

    private final Color cor; // cor bg

    CartaCor(Color color) {
        this.cor = color;
    }
    public Color getCor() {
        return cor;
    }
}