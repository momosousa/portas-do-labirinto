package labirinto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;
import java.io.Serializable;

public class Fase implements Serializable {
    private static final long serialVersionUID = 1L;

    // ─────── ADIÇÕES PARA CORRIGIR O ERRO ───────
    private static final int VIEW_COLS = 10;
    private static final int VIEW_ROWS = 10;
    // ─────────────────────────────────────────────

    private int faseNumber;
    private int rows = 20, cols = 20;
    private char[][] mapa; // 20×20 de chars: '#' parede, '.' chão, 'K' chave, 'D' porta, 'C' caixa
    private List<PersonagemEstatico> estaticos;
    private List<PersonagemAnimado> animados;

    public Fase(int numero) {
        this.faseNumber = numero;
        estaticos = new ArrayList<>();
        animados = new ArrayList<>();
        carregarMapaEElementos(numero);
    }

    private void carregarMapaEElementos(int num) {
        mapa = new char[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                mapa[r][c] = '.';

        switch (num) {
            case 1:
                for (int i = 0; i < cols; i++) {
                    mapa[0][i] = '#';
                    mapa[rows - 1][i] = '#';
                }
                for (int i = 0; i < rows; i++) {
                    mapa[i][0] = '#';
                    mapa[i][cols - 1] = '#';
                }
                for (int i = 3; i < 17; i++) mapa[5][i] = '#';
                mapa[2][2] = 'K';
                mapa[18][18] = 'D';
                mapa[10][10] = 'C';

                Player p1 = new Player(1, 1);
                animados.add(p1);

                Point[] traj1 = { new Point(2, 15), new Point(8, 15) };
                Enemy e1 = new Enemy(2, 15, traj1);
                animados.add(e1);
                break;

            case 2:
                for (int i = 0; i < cols; i++) {
                    mapa[0][i] = '#';
                    mapa[rows - 1][i] = '#';
                }
                for (int i = 0; i < rows; i++) {
                    mapa[i][0] = '#';
                    mapa[i][cols - 1] = '#';
                }
                for (int i = 2; i < 18; i += 3) {
                    for (int j = 2; j < 18; j++) {
                        mapa[i][j] = '#';
                    }
                }
                mapa[17][2] = 'K';
                mapa[2][17] = 'D';
                mapa[5][5] = 'C';
                mapa[14][14] = 'C';

                Player p2 = new Player(1, 1);
                animados.add(p2);

                Point[] traj2 = { new Point(10, 3), new Point(10, 10) };
                Enemy e2 = new Enemy(10, 3, traj2);
                animados.add(e2);
                break;

            case 3:
                for (int i = 0; i < cols; i++) {
                    mapa[0][i] = '#';
                    mapa[rows - 1][i] = '#';
                }
                for (int i = 0; i < rows; i++) {
                    mapa[i][0] = '#';
                    mapa[i][cols - 1] = '#';
                }
                for (int i = 4; i < 16; i++) mapa[8][i] = '#';
                for (int i = 2; i < 8; i++) mapa[i][12] = '#';
                mapa[18][1] = 'K';
                mapa[1][18] = 'D';
                mapa[6][6] = 'C';
                mapa[6][13] = 'C';
                mapa[12][12] = 'C';

                Player p3 = new Player(1, 1);
                animados.add(p3);

                Point[] traj3a = { new Point(2, 16), new Point(16, 16) };
                Enemy e3a = new Enemy(2, 16, traj3a);
                animados.add(e3a);

                Enemy e3b = new Enemy(10, 5, null);
                e3b.setPersegue(true);
                animados.add(e3b);
                break;

            case 4:
                for (int i = 0; i < cols; i++) {
                    mapa[0][i] = '#';
                    mapa[rows - 1][i] = '#';
                }
                for (int i = 0; i < rows; i++) {
                    mapa[i][0] = '#';
                    mapa[i][cols - 1] = '#';
                }
                for (int i = 2; i < 18; i += 2) {
                    for (int j = 2; j < 18; j++) {
                        if (j % 3 == 0) mapa[i][j] = '#';
                    }
                }
                mapa[17][17] = 'K';
                mapa[2][17] = 'D';
                mapa[10][3] = 'C';
                mapa[3][10] = 'C';
                mapa[14][14] = 'C';

                Player p4 = new Player(1, 1);
                animados.add(p4);

                Point[] traj4a = { new Point(3, 3), new Point(3, 16) };
                Enemy e4a = new Enemy(3, 3, traj4a);
                animados.add(e4a);

                Enemy e4b = new Enemy(16, 3, null);
                e4b.setPersegue(true);
                animados.add(e4b);

                Enemy e4c = new Enemy(10, 10, null);
                e4c.setAleatorio(true);
                animados.add(e4c);
                break;

            case 5:
                for (int i = 0; i < cols; i++) {
                    mapa[0][i] = '#';
                    mapa[rows - 1][i] = '#';
                }
                for (int i = 0; i < rows; i++) {
                    mapa[i][0] = '#';
                    mapa[i][cols - 1] = '#';
                }
                for (int i = 2; i < 18; i++) {
                    mapa[2][i] = '#';
                    mapa[17][i] = '#';
                    mapa[i][2] = '#';
                    mapa[i][17] = '#';
                }
                mapa[18][2] = 'K';
                mapa[2][18] = 'D';
                mapa[5][5] = 'C';
                mapa[5][14] = 'C';
                mapa[14][5] = 'C';
                mapa[14][14] = 'C';

                Player p5 = new Player(1, 1);
                animados.add(p5);

                Point[] traj5a = { new Point(3, 3), new Point(3, 16) };
                Enemy e5a = new Enemy(3, 3, traj5a);
                animados.add(e5a);

                Enemy e5b = new Enemy(16, 3, null);
                e5b.setPersegue(true);
                animados.add(e5b);

                Enemy e5c = new Enemy(10, 10, null);
                e5c.setAleatorio(true);
                animados.add(e5c);

                Enemy e5d = new Enemy(16, 16, null);
                e5d.setPersegue(true);
                animados.add(e5d);
                break;

            default:
                break;
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = mapa[r][c];
                switch (ch) {
                    case '#':
                        estaticos.add(new Parede(c, r));
                        break;
                    case 'K':
                        estaticos.add(new Chave(c, r));
                        break;
                    case 'D':
                        estaticos.add(new Porta(c, r));
                        break;
                    case 'C':
                        estaticos.add(new Caixa(c, r));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void desenhar(Graphics g, int tileSize, int camX, int camY) {
        // Fundo branco (para limpar qualquer quadro anterior)
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, tileSize * VIEW_COLS, tileSize * VIEW_ROWS);

        // Desenha estáticos dentro do viewport
        for (PersonagemEstatico est : estaticos) {
            int tx = est.getTileX(), ty = est.getTileY();
            if (tx >= camX && tx < camX + VIEW_COLS && ty >= camY && ty < camY + VIEW_ROWS) {
                est.desenhar(g, tileSize, camX, camY);
            }
        }

        // Desenha animados dentro do viewport
        for (PersonagemAnimado pa : animados) {
            int tx = pa.getTileX(), ty = pa.getTileY();
            if (tx >= camX && tx < camX + VIEW_COLS && ty >= camY && ty < camY + VIEW_ROWS) {
                pa.desenhar(g, tileSize, camX, camY);
            }
        }

        // HUD: fase, vidas e chaves
        Player p = getPlayer();
        if (p != null) {
            g.setColor(Color.BLACK);
            g.drawString("Fase: " + faseNumber + "   Vidas: " + p.getVidas() + "   Chaves: " + p.getChaves(),
                         10, 20);
        }
    }

    public void atualizar() {
    Player p = getPlayer();
    if (p == null) return;

    // 1) Move player (agora via moveOnce em Player), etc...
    p.update(this);

    // 2) Atualiza inimigos e checa colisões com player...
    for (PersonagemAnimado pa : new ArrayList<>(animados)) {
        if (pa instanceof Enemy) {
            ((Enemy) pa).update(this);
            Enemy e = (Enemy) pa;
            if (e.getTileX() == p.getTileX() && e.getTileY() == p.getTileY()) {
                p.perderVida();
                if (p.getVidas() <= 0) {
                    System.out.println("Game Over");
                    System.exit(0);
                } else {
                    reiniciarFase();
                    return;
                }
            }
        }
    }

    // 3) Verifica se player está em um tile com Chave
    for (PersonagemEstatico est : new ArrayList<>(estaticos)) {
        if (est instanceof Chave) {
            if (est.getTileX() == p.getTileX() && est.getTileY() == p.getTileY()) {
                // coleta chave
                p.addChave();
                estaticos.remove(est);

                // ► assim que a chave é coletada, “desbloqueia” (isOpen=true) TODAS as Portas
                for (PersonagemEstatico est2 : estaticos) {
                    if (est2 instanceof Porta) {
                        ((Porta) est2).desbloquear();
                    }
                }
                break;
            }
        }
    }

    // 4) Verifica se player tocou a porta
    for (PersonagemEstatico est : estaticos) {
        if (est instanceof Porta) {
            if (est.getTileX() == p.getTileX() && est.getTileY() == p.getTileY()) {
                if (p.getChaves() > 0) {
                    // Ganha fase...
                    System.out.println("Fase " + faseNumber + " concluída!");
                    if (faseNumber >= 5) {
                        System.out.println("FIM DE JOGO – [Seu Nome Aqui]");
                        System.exit(0);
                    } else {
                        faseNumber++;
                        Fase proxima = new Fase(faseNumber);
                        // transfere vidas e zera chaves no novo player
                        Player novoP = proxima.getPlayer();
                        novoP.setVidas(p.getVidas());
                        proxima.getPlayer().setChaves(0);

                        this.mapa       = proxima.mapa;
                        this.estaticos  = proxima.estaticos;
                        this.animados   = proxima.animados;
                        this.faseNumber = proxima.faseNumber;
                        return;
                    }
                } else {
                    // Porta permanece fechada (não faz nada)
                }
            }
        }
    }
}


    private void reiniciarFase() {
        Fase copia = new Fase(this.faseNumber);
        Player pOld = this.getPlayer();
        Player pNew = copia.getPlayer();
        pNew.setVidas(pOld.getVidas());
        this.mapa      = copia.mapa;
        this.estaticos = copia.estaticos;
        this.animados  = copia.animados;
    }

    public Player getPlayer() {
        for (PersonagemAnimado pa : animados) {
            if (pa instanceof Player) return (Player) pa;
        }
        return null;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public List<PersonagemEstatico> getEstaticos() {
        return estaticos;
    }

    public List<PersonagemAnimado> getAnimados() {
        return animados;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public boolean tileLivre(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows) return false;
        for (PersonagemEstatico est : estaticos) {
            if (est.getTileX() == x && est.getTileY() == y) {
                if (est instanceof Parede) return false;
                if (est instanceof Porta) {
                    Player p = getPlayer();
                    if (p.getChaves() > 0) return true;
                    else return false;
                }
                if (est instanceof Caixa) return false;
            }
        }
        return true;
    }

    public boolean empurrarCaixa(int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 >= cols || y2 < 0 || y2 >= rows) return false;
        for (PersonagemEstatico est : estaticos) {
            if (est instanceof Caixa) {
                if (est.getTileX() == x1 && est.getTileY() == y1) {
                    boolean livre = true;
                    for (PersonagemEstatico est2 : estaticos) {
                        if (est2.getTileX() == x2 && est2.getTileY() == y2) {
                            livre = false;
                            break;
                        }
                    }
                    if (!livre) return false;
                    est.setTileX(x2);
                    est.setTileY(y2);
                    return true;
                }
            }
        }
        return false;
    }

    public void adicionarPersonagem(Personagem p, int tileX, int tileY) {
        p.setTileX(tileX);
        p.setTileY(tileY);
        if (p instanceof PersonagemEstatico) {
            estaticos.add((PersonagemEstatico) p);
        } else if (p instanceof PersonagemAnimado) {
            animados.add((PersonagemAnimado) p);
        }
    }
}
