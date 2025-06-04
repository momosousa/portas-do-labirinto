package labirinto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

/**
 * Representa uma Caixa empurrável no mapa, agora usando um PNG em vez de um retângulo marrom.
 */
public class Caixa extends PersonagemEstatico {
    private static final long serialVersionUID = 1L;

    // Campo que guarda a imagem carregada (não será serializado)
    private transient BufferedImage img;

    public Caixa(int tileX, int tileY) {
        super(tileX, tileY);
        // Carrega a imagem no construtor
        try {
            // Supondo que você colocou box.png em src/labirinto/resources/box.png
            img = ImageIO.read(getClass().getResource("/labirinto/resources/box.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            img = null; // fallback caso não consiga carregar
        }
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;

        if (img != null) {
            // Redimensiona a imagem para ocupar exatamente o tile de bordo a bordo
            g.drawImage(img, px, py, tileSize, tileSize, null);
        } else {
            // Fallback: desenha um quadrado marrom (para o caso de não carregar o PNG)
            g.setColor(new java.awt.Color(139, 69, 19)); // marrom
            g.fillRect(px + 4, py + 4, tileSize - 8, tileSize - 8);
        }
    }

    /**
     * Este método é invocado automaticamente durante a desserialização.
     * Ele recarrega o BufferedImage que ficou como null após o ObjectInputStream.
     */
    private void readObject(ObjectInputStream in) 
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Recarrega a imagem após a desserialização
        try {
            img = ImageIO.read(getClass().getResource("/labirinto/resources/box.png"));
        } catch (Exception e) {
            img = null;
        }
    }
}
