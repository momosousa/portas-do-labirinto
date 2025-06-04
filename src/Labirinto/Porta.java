package labirinto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

/**
 * Porta no mapa: carrega duas imagens (fechada e aberta) e
 * troca de acordo com o estado (isOpen).
 */
public class Porta extends PersonagemEstatico {
    private static final long serialVersionUID = 1L;

    // Se true, desenha a imagem “aberta”; senão, “fechada”.
    private boolean isOpen = false;

    // Imagens (transient para não serializar o BufferedImage inteiro)
    private transient BufferedImage imgClosed;
    private transient BufferedImage imgOpen;

    public Porta(int tileX, int tileY) {
        super(tileX, tileY);
        carregarImagens();
    }

    /** Chama ImageIO para carregar as duas imagens do classpath. */
    private void carregarImagens() {
        try {
            imgClosed = ImageIO.read(getClass().getResource("/labirinto/resources/door_closed.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            imgClosed = null;
        }
        try {
            imgOpen = ImageIO.read(getClass().getResource("/labirinto/resources/door_open.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            imgOpen = null;
        }
    }

    /** Marca esta porta como “aberta” (isOpen = true). */
    public void desbloquear() {
        isOpen = true;
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;

        BufferedImage img = (isOpen ? imgOpen : imgClosed);
        if (img != null) {
            // redesenha a imagem (fechada ou aberta) dentro do tile
            g.drawImage(img, px, py, tileSize, tileSize, null);
        } else {
            // fallback: retângulo azul / verde se não tiver as imagens
            if (isOpen) {
                // Porta aberta: desenhar de um jeito diferente (exemplo: verde claro)
                g.setColor(java.awt.Color.GREEN.brighter());
                g.fillRect(px, py, tileSize, tileSize);
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(px, py, tileSize, tileSize);
            } else {
                // Porta fechada: o antigo retângulo azul
                g.setColor(java.awt.Color.BLUE);
                g.fillRect(px, py, tileSize, tileSize);
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(px, py, tileSize, tileSize);
            }
        }
    }

    /**
     * Para que, ao desserializar (carregar fase salva), as imagens voltem a ser recarregadas.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // reconstrói os BufferedImages a partir dos PNGs
        carregarImagens();
    }

    /** Se True, esta porta já está “aberta” */
    public boolean estaAberta() {
        return isOpen;
    }
}
