package labirinto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

/**
 * Representa o jogador no mapa, agora usando um PNG em vez de um círculo verde.
 */
public class Player extends PersonagemAnimado {
    private static final long serialVersionUID = 1L;

    private int vidas = 3;
    private int chaves = 0;

    // Imagem do jogador (não é serializada diretamente)
    private transient BufferedImage img;

    /**
     * Construtor: carrega o PNG do jogador.
     * @param tileX coluna inicial em tiles
     * @param tileY linha inicial em tiles
     */
    public Player(int tileX, int tileY) {
        super(tileX, tileY);
        carregarImagem();
    }

    /** Tenta ler player.png do classpath ("/labirinto/resources/player.png"). */
    private void carregarImagem() {
        try {
            img = ImageIO.read(getClass().getResource("/labirinto/resources/player.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            img = null;
        }
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int v) {
        this.vidas = v;
    }

    public void perderVida() {
        vidas--;
    }

    public int getChaves() {
        return chaves;
    }

    public void setChaves(int c) {
        this.chaves = c;
    }

    public void addChave() {
        chaves++;
    }

    /**
     * Move o player exatamente UMA vez, na direção (dx, dy).
     * Se houver caixa, tenta empurrar. Se caixa não puder ser empurrada, o player para.
     * Se tile estiver livre, move 1 tile. Após mover, chama fase.atualizar() para
     * checar colisões, coletar chave, abrir porta etc.
     */
    public void moveOnce(int dx, int dy, Fase fase) {
        int nx = getTileX() + dx;
        int ny = getTileY() + dy;

        // Verifica se existe Caixa em (nx,ny)
        boolean haCaixa = false;
        for (PersonagemEstatico est : fase.getEstaticos()) {
            if (est instanceof Caixa) {
                if (est.getTileX() == nx && est.getTileY() == ny) {
                    haCaixa = true;
                    break;
                }
            }
        }
        if (haCaixa) {
            int tx = nx + dx;
            int ty = ny + dy;
            if (fase.empurrarCaixa(nx, ny, tx, ty)) {
                // Caixa foi empurrada, então o player ocupa (nx,ny)
                setTileX(nx);
                setTileY(ny);
            }
            // se não conseguiu empurrar, player fica parado
        } else {
            // Sem caixa, testa se tileLivre
            if (fase.tileLivre(nx, ny)) {
                setTileX(nx);
                setTileY(ny);
            }
        }

        // Após qualquer tentativa de movimento, atualizar a fase
        fase.atualizar();
    }

    @Override
    public void update(Fase fase) {
        // não usado—movimentação é feita via moveOnce()
    }

    @Override
    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        int px = (getTileX() - camX) * tileSize;
        int py = (getTileY() - camY) * tileSize;

        if (img != null) {
            // Desenha o PNG escalonado para caber no tile
            g.drawImage(img, px, py, tileSize, tileSize, null);
        } else {
            // Fallback: se img==null, desenha o círculo verde
            g.setColor(java.awt.Color.GREEN);
            g.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
        }
    }

    /**
     * Invocado durante a desserialização para recarregar o BufferedImage.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        carregarImagem();
    }
}
