package labirinto;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 32;
    private static final int VIEW_COLS = 10;
    private static final int VIEW_ROWS = 10;
    private static final int PANEL_WIDTH = TILE_SIZE * VIEW_COLS;
    private static final int PANEL_HEIGHT = TILE_SIZE * VIEW_ROWS;

    private Fase faseAtual;
    private Timer timer;
    private int cameraX = 0, cameraY = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        // Inicia na fase 1
        faseAtual = new Fase(1);

        // Habilita drag & drop (arquivos .zip) com ação de cópia
        new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable t = dtde.getTransferable();
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File f : files) {
                            if (f.getName().toLowerCase().endsWith(".zip")) {
                                Point dropPoint = dtde.getLocation();
                                int tileX = (cameraX + dropPoint.x) / TILE_SIZE;
                                int tileY = (cameraY + dropPoint.y) / TILE_SIZE;
                                Personagem novo = desserializarPersonagemDeZip(f);
                                if (novo != null) {
                                    faseAtual.adicionarPersonagem(novo, tileX, tileY);
                                }
                            }
                        }
                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dtde.rejectDrop();
                }
            }
        }, true);

        // Timer para game loop (~60 FPS)
        timer = new Timer(1000 / 60, e -> {
            faseAtual.atualizar();
            atualizarCamera();
            repaint();
        });
    }

    public void startGameLoop() {
        timer.start();
    }

    private void atualizarCamera() {
        Player p = faseAtual.getPlayer();
        if (p != null) {
            int px = p.getTileX();
            int py = p.getTileY();
            cameraX = px - VIEW_COLS / 2;
            cameraY = py - VIEW_ROWS / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            int maxCamX = faseAtual.getCols() - VIEW_COLS;
            int maxCamY = faseAtual.getRows() - VIEW_ROWS;
            if (cameraX > maxCamX) cameraX = maxCamX;
            if (cameraY > maxCamY) cameraY = maxCamY;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        faseAtual.desenhar(g, TILE_SIZE, cameraX, cameraY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Player p = faseAtual.getPlayer();
        if (p == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                p.moveOnce(0, -1, faseAtual);
                break;
            case KeyEvent.VK_DOWN:
                p.moveOnce(0, 1, faseAtual);
                break;
            case KeyEvent.VK_LEFT:
                p.moveOnce(-1, 0, faseAtual);
                break;
            case KeyEvent.VK_RIGHT:
                p.moveOnce(1, 0, faseAtual);
                break;
            case KeyEvent.VK_S:
                salvarFase();
                break;
            case KeyEvent.VK_L:
                carregarFase();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Não utilizado, pois a movimentação ocorre em moveOnce()
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não utilizado
    }

    private void salvarFase() {
        try {
            File out = new File("salvo_fase.zip");
            try (FileOutputStream fos = new FileOutputStream(out);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                zos.putNextEntry(new ZipEntry("fase.dat"));
                ObjectOutputStream oos = new ObjectOutputStream(zos);
                oos.writeObject(faseAtual);
                oos.flush();
                zos.closeEntry();
                oos.close();
            }
            System.out.println("Fase salva em: " + out.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void carregarFase() {
        try {
            File in = new File("salvo_fase.zip");
            if (!in.exists()) {
                System.out.println("Arquivo salvo não encontrado.");
                return;
            }
            try (FileInputStream fis = new FileInputStream(in);
                 ZipInputStream zis = new ZipInputStream(fis)) {
                ZipEntry entry;
                Fase f = null;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("fase.dat")) {
                        ObjectInputStream ois = new ObjectInputStream(zis);
                        f = (Fase) ois.readObject();
                        ois.close();
                        break;
                    }
                    zis.closeEntry();
                }
                if (f != null) {
                    faseAtual = f;
                    atualizarCamera();
                    System.out.println("Fase carregada com sucesso.");
                } else {
                    System.out.println("Não encontrou entrada fase.dat no zip.");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private Personagem desserializarPersonagemDeZip(File zipFile) {
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ObjectInputStream ois = new ObjectInputStream(zis);
                Object obj = ois.readObject();
                ois.close();
                if (obj instanceof Personagem) {
                    return (Personagem) obj;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
