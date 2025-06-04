package labirinto;

import java.awt.Color;
import java.awt.Graphics;

public class Parede extends PersonagemEstatico {
    private static final long serialVersionUID = 1L;

    public Parede(int tileX, int tileY) {
        super(tileX, tileY);
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(px, py, tileSize, tileSize);
    }
}
