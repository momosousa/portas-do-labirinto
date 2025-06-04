package labirinto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

public class Enemy extends PersonagemAnimado {
    private static final long serialVersionUID = 1L;

    private Point[] path;       // pontos para patrulha (x,y) em tiles
    private int pathIndex = 0;  // índice atual no path
    private boolean vaiAvancar = true; // direção no path (vai/volta)
    private boolean persegue = false;
    private boolean aleatorio = false;

    private static final Random rand = new Random();

    public Enemy(int tileX, int tileY, Point[] trajeto) {
        super(tileX, tileY);
        this.path = trajeto;
    }

    public void setPersegue(boolean persegue) {
        this.persegue = persegue;
    }

    public void setAleatorio(boolean aleatorio) {
        this.aleatorio = aleatorio;
    }

    @Override
    public void update(Fase fase) {
        if (persegue) {
            // Move-se na direção do player (simples)
            Player p = fase.getPlayer();
            if (p == null) return;
            int dx = p.getTileX() - getTileX();
            int dy = p.getTileY() - getTileY();
            int movX = 0, movY = 0;
            if (Math.abs(dx) > Math.abs(dy)) {
                movX = (dx > 0) ? 1 : -1;
            } else {
                movY = (dy > 0) ? 1 : -1;
            }
            int nx = getTileX() + movX;
            int ny = getTileY() + movY;
            if (fase.tileLivre(nx, ny)) {
                setTileX(nx);
                setTileY(ny);
            }
        } else if (aleatorio) {
            // Move-se aleatoriamente
            int direcao = rand.nextInt(4);
            int movX = 0, movY = 0;
            switch (direcao) {
                case 0: movY = -1; break;
                case 1: movY = 1; break;
                case 2: movX = -1; break;
                case 3: movX = 1; break;
            }
            int nx = getTileX() + movX;
            int ny = getTileY() + movY;
            if (fase.tileLivre(nx, ny)) {
                setTileX(nx);
                setTileY(ny);
            }
        } else if (path != null && path.length >= 2) {
            // Patrulha entre pontos de path em sequência
            Point alvo = path[pathIndex];
            int dx = alvo.x - getTileX();
            int dy = alvo.y - getTileY();
            int movX = 0, movY = 0;
            if (dx != 0) movX = (dx > 0) ? 1 : -1;
            else if (dy != 0) movY = (dy > 0) ? 1 : -1;

            int nx = getTileX() + movX;
            int ny = getTileY() + movY;
            if (fase.tileLivre(nx, ny)) {
                setTileX(nx);
                setTileY(ny);
            }

            // Se alcançou o alvo, troque de índice
            if (getTileX() == alvo.x && getTileY() == alvo.y) {
                if (vaiAvancar) {
                    pathIndex++;
                    if (pathIndex >= path.length) {
                        pathIndex = path.length - 2;
                        vaiAvancar = false;
                    }
                } else {
                    pathIndex--;
                    if (pathIndex < 0) {
                        pathIndex = 1;
                        vaiAvancar = true;
                    }
                }
            }
        }
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;
        g.setColor(Color.RED);
        g.fillRect(px + 4, py + 4, tileSize - 8, tileSize - 8);
    }
}
