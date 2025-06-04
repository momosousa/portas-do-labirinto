package labirinto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Representa a chave no mapa, agora usando um PNG em vez de um círculo amarelo.
 */
public class Chave extends PersonagemEstatico {
    private static final long serialVersionUID = 1L;

    // Campo para armazenar a imagem da chave
    private transient BufferedImage img;

    public Chave(int tileX, int tileY) {
        super(tileX, tileY);
        // Carrega a imagem no construtor
        try {
            // Supondo que você colocou key.png em src/labirinto/resources/key.png
            // Use getResource com caminho absoluto no classpath:
            img = ImageIO.read(getClass().getResource("/labirinto/resources/key.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            // Se falhar, img ficará nulo e podemos desenhar fallback (círculo amarelo) ou não desenhar nada.
            img = null;
        }
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;

        if (img != null) {
            // Desenha a imagem redimensionada para caber exatamente no tile
            g.drawImage(img, px, py, tileSize, tileSize, null);
        } else {
            // Fallback: se não carregou, manter o círculo amarelo
            g.setColor(java.awt.Color.YELLOW);
            g.fillOval(px + tileSize/4, py + tileSize/4, tileSize/2, tileSize/2);
        }
    }
}
