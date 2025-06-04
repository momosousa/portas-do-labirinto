package labirinto;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Labirinto de Portas");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setResizable(false);

            GamePanel painel = new GamePanel();
            janela.setContentPane(painel);
            janela.pack();
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
            painel.startGameLoop();
        });
    }
}
