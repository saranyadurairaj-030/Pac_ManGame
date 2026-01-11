package PacmanPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    final int TILE = 25;
    int level = 1;
    int score = 0;
    boolean gameOver = false;

    int pacRow, pacCol;

    int[] ghostRow = new int[4];
    int[] ghostCol = new int[4];
    int[] ghostDir = new int[4];

    Color[] ghostColor = {
            Color.RED, Color.PINK, Color.CYAN, Color.ORANGE
    };

    Random rand = new Random();
    Timer timer = new Timer(180, this);

    // ===== LEVEL MAPS =====
    int[][][] levels = {

            // LEVEL 1
            {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },

            // LEVEL 2
            {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,1},
                    {1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,2,2,1},
                    {1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,2,2,1},
                    {1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,2,2,1},
                    {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
                    {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            }
    };

    int[][] map;

    public PacmanGame() {
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(600, 350));
        loadLevel();
        timer.start();
    }

    void loadLevel() {
        map = new int[levels[level - 1].length][levels[level - 1][0].length];
        for (int i = 0; i < map.length; i++)
            map[i] = levels[level - 1][i].clone();

        pacRow = map.length / 2;
        pacCol = map[0].length / 2;

        ghostRow[0] = 1; ghostCol[0] = 1;
        ghostRow[1] = 1; ghostCol[1] = map[0].length - 2;
        ghostRow[2] = map.length - 2; ghostCol[2] = 1;
        ghostRow[3] = map.length - 2; ghostCol[3] = map[0].length - 2;

        for (int i = 0; i < 4; i++)
            ghostDir[i] = rand.nextInt(4);

        gameOver = false;
        timer.start();
    }

    void restartGame() {
        timer.stop();
        score = 0;
        level = 1;
        loadLevel();
    }

    // 🔥 ESC KEY MENU
    void showPauseDialog() {
        timer.stop();

        int option = JOptionPane.showOptionDialog(
                this,
                "Game Paused",
                "Pause Menu",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Continue", "Exit"},
                "Continue"
        );

        if (option == 0) {
            timer.start(); // Continue
        } else {
            System.exit(0); // Exit
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                int x = c * TILE;
                int y = r * TILE;
                if (map[r][c] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, TILE, TILE);
                }
                if (map[r][c] == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x + 10, y + 10, 5, 5);
                }
            }
        }

        g.setColor(Color.YELLOW);
        g.fillArc(pacCol * TILE, pacRow * TILE, TILE, TILE, 30, 300);

        for (int i = 0; i < 4; i++) {
            g.setColor(ghostColor[i]);
            g.fillOval(ghostCol[i] * TILE, ghostRow[i] * TILE, TILE, TILE);
        }

        g.setColor(Color.GREEN);
        g.drawString("Score: " + score + "   Level: " + level + "   (ESC to Pause)", 10, 20);
    }

    void movePacman(int r, int c) {
        if (!gameOver && map[r][c] != 1) {
            pacRow = r;
            pacCol = c;
            if (map[r][c] == 2) {
                map[r][c] = 0;
                score += 10;
                checkNextLevel();
            }
        }
    }

    void moveGhosts() {
        for (int i = 0; i < 4; i++) {
            int r = ghostRow[i];
            int c = ghostCol[i];
            int d = ghostDir[i];

            int nr = r, nc = c;
            if (d == 0) nr--;
            if (d == 1) nr++;
            if (d == 2) nc--;
            if (d == 3) nc++;

            if (map[nr][nc] != 1) {
                ghostRow[i] = nr;
                ghostCol[i] = nc;
            } else {
                ghostDir[i] = rand.nextInt(4);
            }

            if (ghostRow[i] == pacRow && ghostCol[i] == pacCol) {
                gameOver = true;
                timer.stop();
                showRetryDialog();
                return;
            }
        }
    }

    void checkNextLevel() {
        for (int[] row : map)
            for (int cell : row)
                if (cell == 2) return;

        level++;
        if (level > levels.length) {
            JOptionPane.showMessageDialog(this, "YOU COMPLETED ALL LEVELS!");
            restartGame();
        } else {
            loadLevel();
        }
    }

    void showRetryDialog() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Game Over!\nStart from Level 1?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION)
            restartGame();
        else
            System.exit(0);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            showPauseDialog();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) movePacman(pacRow - 1, pacCol);
        if (e.getKeyCode() == KeyEvent.VK_DOWN) movePacman(pacRow + 1, pacCol);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) movePacman(pacRow, pacCol - 1);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) movePacman(pacRow, pacCol + 1);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        moveGhosts();
        repaint();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac-Man Game");
        frame.add(new PacmanGame());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}