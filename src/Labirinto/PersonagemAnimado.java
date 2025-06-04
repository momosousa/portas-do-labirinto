package labirinto;

import java.awt.Graphics;

public abstract class PersonagemAnimado extends Personagem {
    private static final long serialVersionUID = 1L;

    public PersonagemAnimado(int tileX, int tileY) {
        super(tileX, tileY);
    }

    // Atualiza lógica interna (mover, checar colisões etc)
    public abstract void update(Fase fase);

    // Desenha no mapa
    @Override
    public abstract void desenhar(Graphics g, int tileSize, int camX, int camY);
}
