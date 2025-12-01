import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameLogic {

    private MazeFrame frame;
    private String playerName;
    public GameLogic(String playerName) {
        this.playerName = playerName;
        showLevelDashboard();
    }

    private void showLevelDashboard() {
        JFrame dashboard = new JFrame("Level Dashboard");
        dashboard.setSize(400, 300);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + playerName + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton level1Btn = new JButton("Level 1");
        JButton level2Btn = new JButton("Level 2");
        JButton level3Btn = new JButton("Level 3");

        level1Btn.addActionListener(e -> startLevel(1));
        level2Btn.addActionListener(e -> startLevel(2));
        level3Btn.addActionListener(e -> startLevel(3));

        dashboard.add(welcomeLabel);
        dashboard.add(level1Btn);
        dashboard.add(level2Btn);
        dashboard.add(level3Btn);

        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }

    private void startLevel(int level) {
        int[][] mazeSize = getMazeSizeForLevel(level);
        int rows = mazeSize[0][0];
        int cols = mazeSize[0][1];

        if (frame != null) {
            frame.dispose();
        }
        frame = new MazeFrame(rows, cols, playerName, level);
    }

    private int[][] getMazeSizeForLevel(int level) {
        switch (level) {
            case 1:
                return new int[][]{{11, 15}};
            case 2:
                return new int[][]{{13, 17}};
            case 3:
                return new int[][]{{15, 19}};
            default:
                return new int[][]{{11, 15}};
        }
    }

    // ---------------- MazeFrame Inner Class ----------------
    public static class MazeFrame extends JFrame {
        private int rows, cols;
        private String playerName;
        private int level;
        private JPanel mazePanel;
        private JButton[][] cells;
        private int playerRow, playerCol;
        private Random rand = new Random();

        public MazeFrame(int rows, int cols, String playerName, int level) {
            this.rows = rows;
            this.cols = cols;
            this.playerName = playerName;
            this.level = level;
            initUI();
        }

        private void initUI() {
            setTitle("Maze Game - Level " + level + " (" + playerName + ")");
            setSize(cols * 40, rows * 40 + 50);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            mazePanel = new JPanel();
            mazePanel.setLayout(new GridLayout(rows, cols));
            cells = new JButton[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    JButton btn = new JButton();
                    btn.setBackground(Color.WHITE);
                    btn.setEnabled(false);
                    cells[r][c] = btn;
                    mazePanel.add(btn);
                }
            }

            add(mazePanel, BorderLayout.CENTER);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    movePlayer(e.getKeyCode());
                }
            });
            setFocusable(true);
            generateMaze();
            placePlayer();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void generateMaze() {
            // Random walls
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (rand.nextDouble() < 0.2) { // 20% chance wall
                        cells[r][c].setBackground(Color.BLACK);
                    }
                }
            }
        }

        private void placePlayer() {
            playerRow = rows - 1;
            playerCol = 0;
            cells[playerRow][playerCol].setBackground(Color.BLUE);
            // Goal
            cells[0][cols - 1].setBackground(Color.GREEN);
        }

        private void movePlayer(int keyCode) {
            int newRow = playerRow;
            int newCol = playerCol;
            switch (keyCode) {
                case KeyEvent.VK_UP -> newRow--;
                case KeyEvent.VK_DOWN -> newRow++;
                case KeyEvent.VK_LEFT -> newCol--;
                case KeyEvent.VK_RIGHT -> newCol++;
                default -> {}
            }

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                cells[newRow][newCol].getBackground() != Color.BLACK) {
                cells[playerRow][playerCol].setBackground(Color.WHITE);
                playerRow = newRow;
                playerCol = newCol;
                cells[playerRow][playerCol].setBackground(Color.BLUE);

                if (playerRow == 0 && playerCol == cols - 1) {
                    JOptionPane.showMessageDialog(this, "Level " + level + " Completed!");
                    dispose();
                }
            }
        }
    }

    // ---------------- Main for testing ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameLogic("Player1"));
    }
}
