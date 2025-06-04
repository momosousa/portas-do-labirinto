package labirinto;

import java.awt.Graphics;
import java.io.Serializable;

public abstract class Personagem implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int tileX, tileY;

    public Personagem(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
    public void setTileX(int x) { this.tileX = x; }
    public void setTileY(int y) { this.tileY = y; }

    // Desenho no mapa; parâmetros: g, tamanho do tile, offset de câmera em tiles
    public abstract void desenhar(Graphics g, int tileSize, int camX, int camY);
}
