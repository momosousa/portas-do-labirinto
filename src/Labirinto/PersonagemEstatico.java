package labirinto;

import java.awt.Graphics;

public abstract class PersonagemEstatico extends Personagem {
    private static final long serialVersionUID = 1L;

    public PersonagemEstatico(int tileX, int tileY) {
        super(tileX, tileY);
    }

    // O desenho específico é implementado nas subclasses
}
