import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.Queue;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║          MAZE ADVENTURE - ENHANCED VERSION                                         ║
 * ╚══════════════════════════════════════════════════════════════╝
 * 
 * IMAGES TO ADD IN PROJECT FOLDER:
 * - bg1.png or bg1.jpg (First maze image - login background)
 * - bg2.png or bg2.jpg (Second maze image - menu background)
 * - All game sprites (key2.png, door.png, player_idel.png, etc.)
 * 
 * DATABASE STRUCTURE (project3):
 * CREATE TABLE game (
 *   user_id INT AUTO_INCREMENT PRIMARY KEY,
 *   user VARCHAR(50),
 *   scores INT,
 *   number_of_games_played INT
 * );
 */

enum Difficulty {
    EASY, MEDIUM, HARD
}

public class Game{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnhancedLogin());
    }
}

// ═══════════════════════════════════════════════════════════════
// DATABASE CONNECTIVITY
// ═══════════════════════════════════════════════════════════════
class Connectivity {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String URL = "jdbc:mysql://localhost:3306/project3";
    private static final String USER = "root";
    private static final String PASSWORD = "saiki$18";

    public static Connection connectingToDatabase() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// LOGIN FRAME 
// ═══════════════════════════════════════════════════════════════
class EnhancedLogin extends JFrame {
    private Image backgroundImage;
    private List<FloatingParticle> particles = new ArrayList<>();
    private Timer animationTimer;
    
    public EnhancedLogin() {
        setTitle("Maze Adventure - Login");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load background - USE IMAGE 1 (Green maze with tower)
        loadBackground("bg1.png", "bg1.jpg");
        
        // Create particles
        for (int i = 0; i < 60; i++) {
            particles.add(new FloatingParticle(1000, 700));
        }
        
        // Animation
        animationTimer = new Timer(30, e -> {
            for (FloatingParticle p : particles) p.update();
            repaint();
        });
        animationTimer.start();
        
        setContentPane(new LoginPanel());
        setVisible(true);
    }
    
    private void loadBackground(String png, String jpg) {
        try {
            // Try yusra1.jpg first, then yusra1.jpeg, then fallback names
            File f = new File("yusra1.jpg");
            if (!f.exists()) f = new File("yusra1.jpeg");
            if (!f.exists()) f = new File(png);
            if (!f.exists()) f = new File(jpg);
            
            if (f.exists()) {
                backgroundImage = ImageIO.read(f);
                System.out.println("Login background loaded: " + f.getName());
            } else {
                System.out.println("yusra1.jpg not found - using gradient");
            }
        } catch (Exception e) {
            System.out.println("Background not found - using gradient");
        }
    }
    
    class FloatingParticle {
        double x, y, vx, vy, size, alpha;
        Color color;
        
        FloatingParticle(int w, int h) {
            x = Math.random() * w;
            y = Math.random() * h;
            vx = -0.3 + Math.random() * 0.6;
            vy = -1 - Math.random() * 2;
            size = 2 + Math.random() * 4;
            alpha = 0.3 + Math.random() * 0.5;
            color = new Color(150, 255, 150); // Green particles
        }
        
        void update() {
            y += vy;
            x += vx;
            if (y < -10) { y = 710; x = Math.random() * 1000; }
            if (x < -10) x = 1010;
            if (x > 1010) x = -10;
        }
        
        void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            g2.setColor(color);
            g2.fillOval((int)x, (int)y, (int)size, (int)size);
            g2.setColor(new Color(255, 255, 255, 100));
            g2.fillOval((int)x, (int)y, (int)(size/2), (int)(size/2));
        }
    }
    
    class LoginPanel extends JPanel {
        private JTextField nameField;
        private GlowingButton loginButton, registerButton;
        private float titleWave = 0;
        
        LoginPanel() {
            setLayout(null);
            setOpaque(false);
            
            SwingUtilities.invokeLater(this::createComponents);
            
            Timer waveTimer = new Timer(50, e -> {
                titleWave += 0.1f;
                repaint();
            });
            waveTimer.start();
        }
        
        void createComponents() {
            // Username field with custom styling
            nameField = new JTextField();
            nameField.setBounds(350, 350, 300, 55);
            nameField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            nameField.setForeground(Color.WHITE);
            nameField.setCaretColor(new Color(150, 255, 150));
            nameField.setBackground(new Color(0, 0, 0, 150));
            nameField.setBorder(BorderFactory.createCompoundBorder(
                new GlowBorder(new Color(100, 255, 100)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            nameField.setText("Enter your name...");
            nameField.setForeground(new Color(180, 255, 180));
            
            nameField.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (nameField.getText().equals("Enter your name...")) {
                        nameField.setText("");
                        nameField.setForeground(Color.WHITE);
                    }
                }
                public void focusLost(FocusEvent e) {
                    if (nameField.getText().isEmpty()) {
                        nameField.setText("Enter your name...");
                        nameField.setForeground(new Color(180, 255, 180));
                    }
                }
            });
            
            add(nameField);
            
            // Login button
            loginButton = new GlowingButton("LOGIN", new Color(46, 204, 113));
            loginButton.setBounds(350, 435, 300, 65);
            loginButton.addActionListener(e -> handleLogin());
            add(loginButton);
            
            // Register button
            registerButton = new GlowingButton("REGISTER", new Color(52, 152, 219));
            registerButton.setBounds(350, 520, 300, 65);
            registerButton.addActionListener(e -> handleRegister());
            add(registerButton);
        }
        
        void handleLogin() {
            String user = nameField.getText().trim();
            if (user.isEmpty() || user.equals("Enter your name...")) {
                showMessage("Please enter your name!", Color.RED);
                return;
            }
            
            // Check if user exists
            try (Connection conn = Connectivity.connectingToDatabase()) {
                String query = "SELECT user_id FROM game WHERE user = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, user);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // User exists - login successful
                    showMessage("Welcome back, " + user + "!", new Color(46, 204, 113));
                   Timer  delay = new Timer(1000, ev -> {
                        EnhancedLogin.this.dispose();
                        new EnhancedMainMenu(user);
                    });
                    delay.setRepeats(false);
                    delay.start();
                } else {
                    showMessage("Account not found! Please register first.", Color.ORANGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showMessage("Database error!", Color.RED);
            }
        }
        
        void handleRegister() {
            String user = nameField.getText().trim();
            if (user.isEmpty() || user.equals("Enter your name...")) {
                showMessage("Please enter your name!", Color.RED);
                return;
            }
            
            // Create new account
            try (Connection conn = Connectivity.connectingToDatabase()) {
                String query = "INSERT INTO game (user, scores, number_of_games_played) VALUES (?, 0, 0)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, user);
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    showMessage("Account created! Welcome, " + user + "!", new Color(46, 204, 113));
                    Timer delay = new Timer(1500, ev -> {
                        EnhancedLogin.this.dispose();
                        new EnhancedMainMenu(user);
                    });
                    delay.setRepeats(false);
                    delay.start();
                }
            } catch (SQLException ex) {
                if (ex.getMessage().contains("Duplicate")) {
                    showMessage("Username already exists! Please login.", Color.ORANGE);
                } else {
                    ex.printStackTrace();
                    showMessage("Registration failed!", Color.RED);
                }
            }
        }
        
        void showMessage(String msg, Color color) {
            JDialog dialog = new JDialog(EnhancedLogin.this, false);
            dialog.setUndecorated(true);
            dialog.setSize(450, 120);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20, 20, 40, 250));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 27, 27);
                }
            };
            panel.setLayout(new BorderLayout());
            
            JLabel label = new JLabel(msg, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 18));
            label.setForeground(color);
            panel.add(label);
            
            dialog.setContentPane(panel);
            dialog.setVisible(true);
            
            Timer close = new Timer(2500, e -> dialog.dispose());
            close.setRepeats(false);
            close.start();
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Background
            if (((EnhancedLogin)SwingUtilities.getWindowAncestor(this)).backgroundImage != null) {
                g2.drawImage(((EnhancedLogin)SwingUtilities.getWindowAncestor(this)).backgroundImage, 
                           0, 0, getWidth(), getHeight(), null);
                g2.setColor(new Color(0, 20, 0, 180));
                g2.fillRect(0, 0, getWidth(), getHeight());
            } else {
                GradientPaint grad = new GradientPaint(0, 0, new Color(20, 40, 20),
                                                       0, getHeight(), new Color(10, 60, 30));
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            
            // Particles
            for (FloatingParticle p : ((EnhancedLogin)SwingUtilities.getWindowAncestor(this)).particles) {
                p.draw(g2);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            // Title with wave effect
            String title = "MAZE ADVENTURE";
            g2.setFont(new Font("Impact", Font.BOLD, 80));
            FontMetrics fm = g2.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int x = (getWidth() - titleWidth) / 2;
            int y = 180;
            
            // Wave shadow
            for (int i = 0; i < title.length(); i++) {
                char c = title.charAt(i);
                int charWidth = fm.charWidth(c);
                float offset = (float)(Math.sin(titleWave + i * 0.5) * 10);
                
                // Shadow
                g2.setColor(new Color(0, 100, 0, 150));
                g2.drawString(String.valueOf(c), x + 4, y + (int)offset + 4);
                
                // Main character
                g2.setColor(new Color(150, 255, 150));
                g2.drawString(String.valueOf(c), x, y + (int)offset);
                
                x += charWidth;
            }
            
            // Subtitle
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 24));
            g2.setColor(new Color(180, 255, 180, 200));
            String sub = "Enter the Ancient Labyrinth";
            int subWidth = g2.getFontMetrics().stringWidth(sub);
            g2.drawString(sub, (getWidth() - subWidth) / 2, 260);
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// MAIN MENU DASHBOARD
// ═══════════════════════════════════════════════════════════════
class EnhancedMainMenu extends JFrame {
    private Image backgroundImage;
    private List<HexOrb> orbs = new ArrayList<>();
    private Timer animationTimer;
    private String playerName;
    
    public EnhancedMainMenu(String playerName) {
        this.playerName = playerName;
        
        setTitle("Maze Adventure - Main Menu");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load background - USE IMAGE 2 (Blue tech maze)
        loadBackground("bg2.png", "bg2.jpg");
        
        // Create orbs
        for (int i = 0; i < 25; i++) {
            orbs.add(new HexOrb());
        }
        
        animationTimer = new Timer(25, e -> {
            for (HexOrb orb : orbs) orb.update();
            repaint();
        });
        animationTimer.start();
        
        setContentPane(new MenuPanel());
        setVisible(true);
    }
    
    private void loadBackground(String png, String jpg) {
        try {
            // Try yusra2.jpg first, then yusra2.jpeg, then fallback names
            File f = new File("yusra2.jpg");
            if (!f.exists()) f = new File("yusra2.jpeg");
            if (!f.exists()) f = new File(png);
            if (!f.exists()) f = new File(jpg);
            
            if (f.exists()) {
                backgroundImage = ImageIO.read(f);
                System.out.println("✓ Menu background loaded: " + f.getName());
            } else {
                System.out.println("⚠ yusra2.jpg not found - using gradient");
            }
        } catch (Exception e) {
            System.out.println("⚠ Menu background not found");
        }
    }
    
    class HexOrb {
        double x, y, vx, vy, size, alpha, angle, rotSpeed;
        Color color;
        
        HexOrb() {
            x = Math.random() * 1200;
            y = Math.random() * 800;
            vx = -0.5 + Math.random();
            vy = -0.5 + Math.random();
            size = 25 + Math.random() * 45;
            alpha = 0.15 + Math.random() * 0.25;
            angle = Math.random() * 360;
            rotSpeed = -1 + Math.random() * 2;
            
            int choice = (int)(Math.random() * 4);
            switch(choice) {
                case 0: color = new Color(0, 191, 255); break;
                case 1: color = new Color(138, 43, 226); break;
                case 2: color = new Color(50, 205, 50); break;
                default: color = new Color(255, 140, 0); break;
            }
        }
        
        void update() {
            x += vx; y += vy; angle += rotSpeed;
            if (x < -60) x = 1260;
            if (x > 1260) x = -60;
            if (y < -60) y = 860;
            if (y > 860) y = -60;
        }
        
        void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(Math.toRadians(angle));
            
            Polygon hex = new Polygon();
            for (int i = 0; i < 6; i++) {
                double a = 2 * Math.PI * i / 6;
                hex.addPoint((int)(Math.cos(a) * size), (int)(Math.sin(a) * size));
            }
            
            RadialGradientPaint grad = new RadialGradientPaint(
                0, 0, (float)size,
                new float[]{0f, 0.6f, 1f},
                new Color[]{
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 220),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 110),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                }
            );
            g2.setPaint(grad);
            g2.fill(hex);
            
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillOval(-4, -4, 8, 8);
            
            g2.setTransform(old);
        }
    }
    
    class MenuPanel extends JPanel {
        private float gradOffset = 0;
        private GlowingButton startBtn, leaderBtn, logoutBtn;
        
        MenuPanel() {
            setLayout(null);
            setOpaque(false);
            
            SwingUtilities.invokeLater(this::createButtons);
            
            Timer gradTimer = new Timer(60, e -> {
                gradOffset += 0.02f;
                repaint();
            });
            gradTimer.start();
        }
        
        void createButtons() {
            startBtn = new GlowingButton("START GAME ⚔", new Color(46, 204, 113));
            startBtn.setBounds(400, 320, 400, 85);
            startBtn.addActionListener(e -> new DifficultyMenuFrame(11, 15, playerName).setVisible(true));
            add(startBtn);
            
            leaderBtn = new GlowingButton("LEADERBOARD ★", new Color(241, 196, 15));
            leaderBtn.setBounds(400, 425, 400, 85);
            leaderBtn.addActionListener(e -> showLeaderboard());
            add(leaderBtn);
            
            logoutBtn = new GlowingButton("LOGOUT ⏻", new Color(231, 76, 60));
            logoutBtn.setBounds(400, 530, 400, 85);
            logoutBtn.addActionListener(e -> logout());
            add(logoutBtn);
        }
        
        void startGame() {
            EnhancedMainMenu.this.dispose();
            SwingUtilities.invokeLater(() -> {
            new DifficultyMenuFrame(11, 15, playerName).setVisible(true);
        });
        }
        
        void showLeaderboard() {
            new EnhancedLeaderboard(playerName, EnhancedMainMenu.this);
        }
        
        void logout() {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                EnhancedMainMenu.this.dispose();
                new EnhancedLogin();
            }
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Background
            if (((EnhancedMainMenu)SwingUtilities.getWindowAncestor(this)).backgroundImage != null) {
                g2.drawImage(((EnhancedMainMenu)SwingUtilities.getWindowAncestor(this)).backgroundImage,
                           0, 0, getWidth(), getHeight(), null);
                g2.setColor(new Color(0, 10, 30, 170));
                g2.fillRect(0, 0, getWidth(), getHeight());
            } else {
                Color c1 = new Color(
                    (int)(20 + 15 * Math.sin(gradOffset * Math.PI)),
                    (int)(30 + 20 * Math.cos(gradOffset * Math.PI)),
                    (int)(60 + 30 * Math.sin(gradOffset * Math.PI * 2))
                );
                Color c2 = new Color(
                    (int)(10 + 10 * Math.cos(gradOffset * Math.PI)),
                    (int)(20 + 15 * Math.sin(gradOffset * Math.PI * 1.5)),
                    (int)(80 + 40 * Math.cos(gradOffset * Math.PI))
                );
                GradientPaint grad = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            
            // Orbs
            for (HexOrb orb : ((EnhancedMainMenu)SwingUtilities.getWindowAncestor(this)).orbs) {
                orb.draw(g2);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            // Title
            g2.setFont(new Font("Impact", Font.BOLD, 70));
            String title = "MAIN MENU";
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(title);
            int x = (getWidth() - w) / 2;
            int y = 130;
            
            for (int i = 10; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.drawString(title, x + i, y + i);
            }
            
            GradientPaint titleGrad = new GradientPaint(
                x, y - 30, new Color(100, 200, 255),
                x, y + 30, new Color(50, 150, 255)
            );
            g2.setPaint(titleGrad);
            g2.drawString(title, x, y);
            
            // Player name
            g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
            g2.setColor(new Color(255, 255, 255, 230));
            String welcome = "Welcome, " + playerName + "!";
            int ww = g2.getFontMetrics().stringWidth(welcome);
            g2.drawString(welcome, (getWidth() - ww) / 2, 210);
            
            // Frame decoration
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < 6; i++) {
                g2.setColor(new Color(100, 200, 255, 25 - i * 4));
                g2.drawRoundRect(20 + i, 20 + i, getWidth() - 40 - i*2, getHeight() - 40 - i*2, 45, 45);
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// LEADERBOARD
// ═══════════════════════════════════════════════════════════════
class EnhancedLeaderboard extends JFrame {
    private List<TwinkleStar> stars = new ArrayList<>();
    private Timer animationTimer;
    private String currentPlayer;
    
    public EnhancedLeaderboard(String currentPlayer, JFrame parent) {
        this.currentPlayer = currentPlayer;
        
        setTitle("Leaderboard - Hall of Champions");
        setSize(950, 750);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        for (int i = 0; i < 120; i++) {
            stars.add(new TwinkleStar());
        }
        
        animationTimer = new Timer(40, e -> {
            for (TwinkleStar s : stars) s.update();
            repaint();
        });
        animationTimer.start();
        
        setContentPane(new LeaderPanel());
        setVisible(true);
    }
    
    class TwinkleStar {
        double x, y, size, brightness;
        boolean increasing;
        
        TwinkleStar() {
            x = Math.random() * 950;
            y = Math.random() * 750;
            size = 1 + Math.random() * 2.5;
            brightness = Math.random();
            increasing = Math.random() > 0.5;
        }
        
        void update() {
            if (increasing) {
                brightness += 0.025;
                if (brightness >= 1) increasing = false;
            } else {
                brightness -= 0.025;
                if (brightness <= 0) increasing = true;
            }
        }
        
        void draw(Graphics2D g2) {
            g2.setColor(new Color(255, 255, 255, (int)(brightness * 255)));
            g2.fillOval((int)x, (int)y, (int)size, (int)size);
            
            if (brightness > 0.85) {
                g2.setColor(new Color(255, 255, 255, (int)((brightness - 0.85) * 1000)));
                g2.drawLine((int)x - 4, (int)y, (int)x + 4, (int)y);
                g2.drawLine((int)x, (int)y - 4, (int)x, (int)y + 4);
            }
        }
    }
    
    class LeaderPanel extends JPanel {
        private float titlePulse = 0;
        private String[][] leaderData;
        
        LeaderPanel() {
            setLayout(new BorderLayout());
            setOpaque(false);
            
            loadLeaderboard();
            
            GlowingButton backBtn = new GlowingButton("← BACK", new Color(52, 73, 94));
            backBtn.setPreferredSize(new Dimension(220, 65));
            backBtn.addActionListener(e -> EnhancedLeaderboard.this.dispose());
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setOpaque(false);
            bottomPanel.add(backBtn);
            add(bottomPanel, BorderLayout.SOUTH);
            
            Timer pulseTimer = new Timer(60, e -> {
                titlePulse += 0.12f;
                repaint();
            });
            pulseTimer.start();
        }
        
        void loadLeaderboard() {
            try (Connection conn = Connectivity.connectingToDatabase()) {
                String query = "SELECT user, scores, number_of_games_played FROM game ORDER BY scores DESC LIMIT 10";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                List<String[]> dataList = new ArrayList<>();
                int rank = 1;
                while (rs.next()) {
                    String[] row = new String[4];
                    row[0] = String.valueOf(rank++);
                    row[1] = rs.getString("user");
                    row[2] = String.valueOf(rs.getInt("scores"));
                    row[3] = (rank <= 4) ? "🏆" : "";
                    dataList.add(row);
                }
                
                leaderData = dataList.toArray(new String[0][]);
            } catch (Exception ex) {
                ex.printStackTrace();
                leaderData = new String[][]{{"1", "No data", "0", ""}};
            }
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Dark gradient
            GradientPaint bg = new GradientPaint(0, 0, new Color(15, 15, 40),
                                                 0, getHeight(), new Color(30, 30, 70));
            g2.setPaint(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Stars
            for (TwinkleStar s : ((EnhancedLeaderboard)SwingUtilities.getWindowAncestor(this)).stars) {
                s.draw(g2);
            }
            
            // Title
            g2.setFont(new Font("Impact", Font.BOLD, 52));
            String title = "HALL OF CHAMPIONS";
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(title);
            int x = (getWidth() - w) / 2;
            int y = 80;
            
            float glowSize = 18 + (float)(Math.sin(titlePulse) * 6);
            for (int i = (int)glowSize; i > 0; i--) {
                float alpha = (glowSize - i) / glowSize * 0.35f;
                g2.setColor(new Color(255, 215, 0, (int)(alpha * 255)));
                g2.drawString(title, x - i/2, y - i/2);
            }
            
            GradientPaint titleGrad = new GradientPaint(x, y - 25, new Color(255, 230, 0),
                                                        x, y + 25, new Color(255, 160, 0));
            g2.setPaint(titleGrad);
            g2.drawString(title, x, y);
            
            // Leaderboard table
            drawTable(g2);
        }
        
        void drawTable(Graphics2D g2) {
            int startY = 160;
            int rowHeight = 52;
            int tableWidth = 750;
            int tableX = (getWidth() - tableWidth) / 2;
            
            // Panel background
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(tableX - 25, startY - 25, tableWidth + 50, rowHeight * (leaderData.length + 1) + 50, 35, 35);
            
            // Border
            g2.setColor(new Color(255, 215, 0, 120));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(tableX - 25, startY - 25, tableWidth + 50, rowHeight * (leaderData.length + 1) + 50, 35, 35);
            
            // Headers
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("RANK", tableX + 10, startY);
            g2.drawString("PLAYER", tableX + 120, startY);
            g2.drawString("SCORE", tableX + 500, startY);
            g2.drawString("TROPHY", tableX + 650, startY);
            
            // Data rows
            for (int i = 0; i < leaderData.length; i++) {
                int y = startY + 30 + i * rowHeight;
                String[] row = leaderData[i];
                
                // Highlight current player
                if (row[1].equals(currentPlayer)) {
                    g2.setColor(new Color(46, 204, 113, 60));
                    g2.fillRoundRect(tableX - 15, y - 30, tableWidth + 30, rowHeight - 5, 18, 18);
                }
                
                // Alternate rows
                if (i % 2 == 0) {
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(tableX - 15, y - 30, tableWidth + 30, rowHeight - 5, 18, 18);
                }
                
                // Text color by rank
                Color textColor;
                if (i == 0) textColor = new Color(255, 215, 0);
                else if (i == 1) textColor = new Color(192, 192, 192);
                else if (i == 2) textColor = new Color(205, 127, 50);
                else textColor = new Color(200, 200, 255);
                
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                g2.setColor(textColor);
                
                g2.drawString(row[0], tableX + 20, y);
                g2.drawString(row[1], tableX + 120, y);
                g2.drawString(row[2], tableX + 500, y);
                
                if (!row[3].isEmpty()) {
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                    g2.drawString(row[3], tableX + 650, y);
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// CUSTOM UI COMPONENTS
// ═══════════════════════════════════════════════════════════════

class GlowingButton extends JButton {
    private Color baseColor;
    private float hoverProgress = 0f;
    private Timer hoverTimer;
    
    public GlowingButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        
        setFont(new Font("Segoe UI", Font.BOLD, 26));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(25, ev -> {
                    hoverProgress = Math.min(1f, hoverProgress + 0.12f);
                    repaint();
                });
                hoverTimer.start();
            }
            
            public void mouseExited(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(25, ev -> {
                    hoverProgress = Math.max(0f, hoverProgress - 0.12f);
                    if (hoverProgress <= 0) hoverTimer.stop();
                    repaint();
                });
                hoverTimer.start();
            }
            
            public void mousePressed(MouseEvent e) {
                hoverProgress = 0.6f;
                repaint();
            }
            
            public void mouseReleased(MouseEvent e) {
                hoverProgress = 1f;
                repaint();
            }
        });
    }
    
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int offset = (int)(6 * (1 - hoverProgress));
        
        // 3D shadow
        for (int i = offset; i > 0; i--) {
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(i, i, getWidth(), getHeight(), 45, 45);
        }
        
        // Glow
        if (hoverProgress > 0) {
            for (int i = 12; i > 0; i--) {
                float alpha = hoverProgress * (i / 12f) * 0.6f;
                g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(alpha * 255)));
                g2.fillRoundRect(-i, -i, getWidth() + i*2, getHeight() + i*2, 45 + i*2, 45 + i*2);
            }
        }
        
        // Button body
        Color btnColor = hoverProgress > 0 ? baseColor.brighter() : baseColor;
        GradientPaint gradient = new GradientPaint(0, 0, btnColor, 0, getHeight(), btnColor.darker());
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);
        
        // Shine
        g2.setColor(new Color(255, 255, 255, 90));
        g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() / 2, 40, 40);
        
        // Border
        g2.setColor(btnColor.brighter());
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 42, 42);
        
        // Text
        g2.setFont(getFont());
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textX = (getWidth() - textWidth) / 2;
        int textY = getHeight() / 2 + fm.getAscent() / 2 - 3;
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(getText(), textX + 2, textY + 2);
        
        // Main text
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), textX, textY);
    }
}

class GlowBorder extends AbstractBorder {
    private Color color;
    
    GlowBorder(Color color) {
        this.color = color;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (int i = 3; i > 0; i--) {
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50 - i * 15));
            g2.setStroke(new BasicStroke(i * 2));
            g2.drawRoundRect(x + i, y + i, width - i * 2, height - i * 2, 28, 28);
        }
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width - 1, height - 1, 28, 28);
    }
    
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }
}

// ═══════════════════════════════════════════════════════════════
// LEVEL DASHBOARD
// ═══════════════════════════════════════════════════════════════

class DifficultyMenuFrame extends JFrame {
    private final int rows;
    private final int cols;
    private String playerName;
    private Image backgroundImage;
    private float glowIntensity = 0f;
    private Timer animationTimer;
    private List<Particle> particles = new ArrayList<>();
    private Random random = new Random();

    class Particle {
        float x, y, size, speed, alpha;
        
        Particle() {
            reset();
        }
        
        void reset() {
            x = random.nextFloat() * 2000;
            y = random.nextFloat() * 1200;
            size = random.nextFloat() * 3 + 1;
            speed = random.nextFloat() * 0.5f + 0.2f;
            alpha = random.nextFloat() * 0.5f + 0.3f;
        }
        
        void update() {
            y -= speed;
            alpha -= 0.001f;
            if (y < -10 || alpha <= 0) {
                reset();
            }
        }
    }

    DifficultyMenuFrame(int rows, int cols, String playerName) {  // ADD playerName parameter
    super("Maze Adventure");
    this.rows = rows;
    this.cols = cols;
    this.playerName = playerName;

        // Initialize particles
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle());
        }

        // Load background image (level.jpeg)
        try {
            File bgFile = new File("level.jpeg");
            if (bgFile.exists()) {
                backgroundImage = ImageIO.read(bgFile);
            }
        } catch (IOException e) {
            System.err.println("Background image not found, using gradient: " + e.getMessage());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Main panel with animations
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    // Dark overlay
                    g2.setColor(new Color(0, 0, 0, 120));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 20, 15),
                        0, getHeight(), new Color(20, 40, 30)
                    );
                    g2.setPaint(gradient);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }

                // Draw animated particles
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                for (Particle p : particles) {
                    g2.setColor(new Color(100, 255, 150, (int)(p.alpha * 255)));
                    g2.fillOval((int)p.x, (int)p.y, (int)p.size, (int)p.size);
                    
                    // Glow effect
                    float glowSize = p.size * 3;
                    RadialGradientPaint glow = new RadialGradientPaint(
                        p.x + p.size/2, p.y + p.size/2, glowSize,
                        new float[]{0f, 1f},
                        new Color[]{
                            new Color(100, 255, 150, (int)(p.alpha * 100)),
                            new Color(100, 255, 150, 0)
                        }
                    );
                    g2.setPaint(glow);
                    g2.fillOval((int)(p.x - glowSize), (int)(p.y - glowSize), 
                               (int)(glowSize * 2), (int)(glowSize * 2));
                }
            }
        };
        mainPanel.setOpaque(false);

        // Title panel with glow effect
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(80, 20, 60, 20));

        JLabel mainTitle = new JLabel("MAZE ADVENTURE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Text glow
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Multiple glow layers
                for (int i = 8; i > 0; i--) {
                    int alpha = (int)(50 * glowIntensity * (i / 8f));
                    g2.setColor(new Color(100, 255, 150, alpha));
                    g2.drawString(getText(), x - i/2, y - i/2);
                    g2.drawString(getText(), x + i/2, y + i/2);
                }
                
                // Main text
                g2.setColor(new Color(180, 255, 200));
                g2.drawString(getText(), x, y);
            }
        };
        mainTitle.setFont(new Font("Impact", Font.BOLD, 86));
        mainTitle.setForeground(new Color(180, 255, 200));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainTitle.setOpaque(false);
        
        JLabel subtitle = new JLabel("The Gate to the Maze Awaits") ;
        subtitle.setFont(new Font("Arial", Font.ITALIC, 28));
        subtitle.setForeground(new Color(150, 220, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(mainTitle);
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(subtitle);

        // Center panel for buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 120, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(25, 0, 25, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Create animated buttons
        AnimatedButton easyBtn = new AnimatedButton(" EASY ", new Color(100, 255, 150), " ");
        AnimatedButton medBtn = new AnimatedButton(" MEDIUM ", new Color(255, 200, 50), " ");
        AnimatedButton hardBtn = new AnimatedButton(" HARD ", new Color(255, 80, 80), " ");

        centerPanel.add(easyBtn, gbc);
        centerPanel.add(medBtn, gbc);
        centerPanel.add(hardBtn, gbc);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Animation timer
        animationTimer = new Timer(30, e -> {
            glowIntensity = (float)(Math.sin(System.currentTimeMillis() / 300.0) * 0.5 + 0.5);
            for (Particle p : particles) {
                p.update();
            }
            repaint();
        });
        animationTimer.start();

        // Action listeners
        easyBtn.addActionListener(e -> openGame(Difficulty.EASY));
        medBtn.addActionListener(e -> openGame(Difficulty.MEDIUM));
        hardBtn.addActionListener(e -> openGame(Difficulty.HARD));

        setLocationRelativeTo(null);
    }

    class AnimatedButton extends JButton {
        private float hoverProgress = 0f;
        private Timer hoverTimer;
        private Color baseColor;
        private String description;
        private float pulsePhase = (float)(Math.random() * Math.PI * 2);

        AnimatedButton(String text, Color color, String desc) {
            super(text);
            this.baseColor = color;
            this.description = desc;
            
            setPreferredSize(new Dimension(700, 120));
            setFont(new Font("Impact", Font.BOLD, 48));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            hoverTimer = new Timer(20, e -> {
                repaint();
            });
            hoverTimer.start();

            addMouseListener(new MouseAdapter() {
                Timer fadeTimer;
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (fadeTimer != null) fadeTimer.stop();
                    fadeTimer = new Timer(10, evt -> {
                        hoverProgress = Math.min(1f, hoverProgress + 0.05f);
                        if (hoverProgress >= 1f) {
                            fadeTimer.stop();
                        }
                    });
                    fadeTimer.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (fadeTimer != null) fadeTimer.stop();
                    fadeTimer = new Timer(10, evt -> {
                        hoverProgress = Math.max(0f, hoverProgress - 0.05f);
                        if (hoverProgress <= 0f) {
                            fadeTimer.stop();
                        }
                    });
                    fadeTimer.start();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                               RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            pulsePhase += 0.05f;
            float pulse = (float)(Math.sin(pulsePhase) * 0.5 + 0.5);

            // Outer glow
            int glowSize = (int)(30 + hoverProgress * 20);
            for (int i = glowSize; i > 0; i -= 3) {
                int alpha = (int)((50 + hoverProgress * 100) * (1 - i / (float)glowSize));
                g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), 
                                     baseColor.getBlue(), alpha));
                g2.fillRoundRect(-i/2, -i/2, w + i, h + i, 35 + i/2, 35 + i/2);
            }

            // Button background with gradient
            Color brightColor = new Color(
                Math.min(255, baseColor.getRed() + (int)(50 * hoverProgress)),
                Math.min(255, baseColor.getGreen() + (int)(50 * hoverProgress)),
                Math.min(255, baseColor.getBlue() + (int)(50 * hoverProgress))
            );
            
            GradientPaint gradient = new GradientPaint(
                0, 0, brightColor,
                0, h, baseColor.darker()
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(5, 5, w - 10, h - 10, 30, 30);

            // Animated border
            g2.setStroke(new BasicStroke(4 + hoverProgress * 3));
            g2.setColor(new Color(255, 255, 255, (int)(150 + pulse * 105)));
            g2.drawRoundRect(5, 5, w - 10, h - 10, 30, 30);

            // Inner glow
            g2.setColor(new Color(255, 255, 255, (int)(50 + hoverProgress * 100)));
            g2.fillRoundRect(10, 10, w - 20, h/3, 25, 25);

            // Draw text with glow
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textX = (w - textWidth) / 2;
            int textY = h/2 + fm.getAscent()/2 - 10;

            // Text shadow/glow
            for (int i = 4; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawString(getText(), textX + i, textY + i);
            }

            // Main text
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), textX, textY);

            // Description text
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(255, 255, 255, 200));
            int descWidth = g2.getFontMetrics().stringWidth(description);
            g2.drawString(description, (w - descWidth) / 2, h - 20);

            g2.dispose();
        }
    }

    private void openGame(Difficulty dif) {
        animationTimer.stop();
        MazeFrame frame = new MazeFrame(rows, cols, dif, playerName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.dispose();
    }
}



// ═══════════════════════════════════════════════════════════════
// GAME CODE 
// ═══════════════════════════════════════════════════════════════

//creation of single cell of grid
class Cell {
    int r;//row
    int c ; // column 
    boolean[] wall = { true, true, true, true }; // top, right, bottom, left
    boolean visited = false;//for maze generation(DFS tracker)
    boolean trap = false; 
    boolean treasure = false; 

    Cell(int r, int c){
         this.r = r; 
         this.c = c; 
    }
}

class Maze {
     int rows;
     int cols;
    Cell[][] grid;//2D array holding all cells 
    Random rand;

  Maze(int rows, int cols) {
   
    this.rows = rows;
    this.cols = cols;
    this.rand = new Random(System.nanoTime());// produces maze different every time
    grid = new Cell[rows][cols];

    for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++)
            grid[r][c] = new Cell(r, c);
  }

    //generation of random maze with internal DFS backtracker
    void generate() {
        // fresh grid
        for (int r = 0; r < rows; r++){
         for (int c = 0; c < cols; c++) {
            Cell cell = grid[r][c];    //Local reference to the Cell object

            cell.wall[0] = cell.wall[1] = cell.wall[2] = cell.wall[3] = true;   //walls visible (cell closed in all direction )
            cell.visited = false;           //Clears the visited flag used by DFS/backtracker
            cell.trap = false;
            cell.treasure = false;
        }
    }

    //to implement iterative DFS (backtracking)
        Stack <Cell> stack = new Stack<>();
        Cell start = grid[0][0];    //generation begins here 
        start.visited = true;
        stack.push(start);          //pushing starting cell onto stack

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List <Cell> unvisited = new ArrayList<>();
            int r = current.r ;
            int c = current.c;

            if (r > 0 && !grid[r-1][c].visited) //move up 
            unvisited.add(grid[r-1][c]);

            if (c < cols-1 && !grid[r][c+1].visited)//move right 
             unvisited.add(grid[r][c+1]);

            if (r < rows-1 && !grid[r+1][c].visited) //move down 
            unvisited.add(grid[r+1][c]);

            if (c > 0 && !grid[r][c-1].visited) //move left 
            unvisited.add(grid[r][c-1]);

            //choose random unvisited neighbor if any
            if (!unvisited.isEmpty()) {
                Cell next = unvisited.get(rand.nextInt(unvisited.size()));
                removeWall(current, next);//remove wall (carve path )between current and next cell
                next.visited = true;
                stack.push(next);//next becomes the current cell(moves forward )
            } else {
                stack.pop(); // backtrack
            }
        }
    }

    //crving path between two cells by removing walls
     void removeWall(Cell a, Cell b) {
        if (a.r == b.r) {//if both cells are in the same row
            if (a.c + 1 == b.c) {
                 a.wall[1] = false; // a right
                 b.wall[3] = false; //b left
                } 
            else if (a.c - 1 == b.c) {
                 a.wall[3] = false;// a left
                  b.wall[1] = false; // b right
                } 
        } else if (a.c == b.c) {//if both cells are in the same column
            if (a.r + 1 == b.r) 
            {
                 a.wall[2] = false; // a bottom
                b.wall[0] = false;// b top
             } 
            else if (a.r - 1 == b.r) { 
                a.wall[0] = false;// a top
                 b.wall[2] = false;// b bottom
                 } 
        }
    }

    // BFS pathfinder (returns empty list if no path)
    List<Cell> findPath(int sr, int sc, int lr, int lc) {
        Queue<Cell> q = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        Cell[][] parent = new Cell[rows][cols];

        q.add(grid[sr][sc]);//starting cell added to queue
        visited[sr][sc] = true;

        while (!q.isEmpty()) {
            Cell current = q.poll();
            int r = current.r;
            int c = current.c;

            if (r == lr && c == lc) break;

            // explore neighbors if passage exists
            if (!current.wall[0] && r > 0 && !visited[r-1][c]) {//up
                visited[r-1][c] = true;
                parent[r-1][c] = current;
                q.add(grid[r-1][c]);
            }
            if (!current.wall[1] && c < cols-1 && !visited[r][c+1]) {//right
                visited[r][c+1] = true;
                parent[r][c+1] = current;
                q.add(grid[r][c+1]);
            }
            if (!current.wall[2] && r < rows-1 && !visited[r+1][c]) {//down
                visited[r+1][c] = true;
                parent[r+1][c] = current;
                q.add(grid[r+1][c]);
            }
            if (!current.wall[3] && c > 0 && !visited[r][c-1]) {//left
                visited[r][c-1] = true;
                parent[r][c-1] = current;
                q.add(grid[r][c-1]);
            }
        }

        // if goal wasn't reached, return empty path
        if (! (lr >= 0 && lr < rows && lc >= 0 && lc < cols) ) return new ArrayList<>();
        if (!visited[lr][lc]) return new ArrayList<>();

        // reconstruct path
        List<Cell> path = new ArrayList<>();
        Cell cur = grid[lr][lc];
        while (cur != null && cur != grid[sr][sc]) {
            path.add(cur);
            cur = parent[cur.r][cur.c];
        }
        path.add(grid[sr][sc]);
        Collections.reverse(path);
        return path;
    }

    // Count number of shortest paths from start to target (ADDED helper)
    // Uses BFS to compute distances, then DP to count the number of shortest paths.
    int countShortestPaths(int sr, int sc, int tr, int tc) {
        // First, BFS to get distances
        int[][] dist = new int[rows][cols];
        for (int i=0;i<rows;i++) Arrays.fill(dist[i], Integer.MAX_VALUE);
        Queue<Cell> q = new LinkedList<>();
        dist[sr][sc] = 0;
        q.add(grid[sr][sc]);
        while (!q.isEmpty()) {
            Cell cur = q.poll();
            int r = cur.r, c = cur.c;
            int d = dist[r][c];
            // neighbors
            if (!cur.wall[0] && r > 0 && dist[r-1][c] == Integer.MAX_VALUE) {
                dist[r-1][c] = d + 1;
                q.add(grid[r-1][c]);
            }
            if (!cur.wall[1] && c < cols-1 && dist[r][c+1] == Integer.MAX_VALUE) {
                dist[r][c+1] = d + 1;
                q.add(grid[r][c+1]);
            }
            if (!cur.wall[2] && r < rows-1 && dist[r+1][c] == Integer.MAX_VALUE) {
                dist[r+1][c] = d + 1;
                q.add(grid[r+1][c]);
            }
            if (!cur.wall[3] && c > 0 && dist[r][c-1] == Integer.MAX_VALUE) {
                dist[r][c-1] = d + 1;
                q.add(grid[r][c-1]);
            }
        }
        if (dist[tr][tc] == Integer.MAX_VALUE) return 0;

        // DP: count shortest paths
        // Collect all cells by distance ascending
        Map<Integer, List<Cell>> byDist = new HashMap<>();
        int maxd = 0;
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) {
            if (dist[r][c] < Integer.MAX_VALUE) {
                byDist.computeIfAbsent(dist[r][c], k -> new ArrayList<>()).add(grid[r][c]);
                maxd = Math.max(maxd, dist[r][c]);
            }
        }
        long[][] paths = new long[rows][cols];
        paths[sr][sc] = 1;
        for (int d=0; d<=maxd; d++) {
            List<Cell> list = byDist.get(d);
            if (list == null) continue;
            for (Cell cur : list) {
                int r = cur.r, c = cur.c;
                // propagate to neighbors with dist +1
                if (!cur.wall[0] && r > 0 && dist[r-1][c] == dist[r][c] + 1) {
                    paths[r-1][c] += paths[r][c];
                }
                if (!cur.wall[1] && c < cols-1 && dist[r][c+1] == dist[r][c] + 1) {
                    paths[r][c+1] += paths[r][c];
                }
                if (!cur.wall[2] && r < rows-1 && dist[r+1][c] == dist[r][c] + 1) {
                    paths[r+1][c] += paths[r][c];
                }
                if (!cur.wall[3] && c > 0 && dist[r][c-1] == dist[r][c] + 1) {
                    paths[r][c-1] += paths[r][c];
                }
            }
        }
        // clamp to int safely
        long result = paths[tr][tc];
        if (result > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int)result;
    }
}


//GUI components 
class MazeFrame extends JFrame {
    private final MazePanel mazePanel;
    private String playerName;  // ADD THIS
    private boolean isPaused = false;  // ADD THIS
    
    // MODIFIED CONSTRUCTOR - Add playerName parameter
    MazeFrame(int rows, int cols, Difficulty difficulty, String playerName) {
        this.playerName = playerName;  // ADD THIS
        
        setTitle("Maze Adventure - " + difficulty.name());
        mazePanel = new MazePanel(rows, cols, difficulty, playerName, this); // Pass playerName & this
        
        setLayout(new BorderLayout());
        add(new JScrollPane(mazePanel), BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        // Key binding: R to randomize
        mazePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "REFRESH");
        mazePanel.getActionMap().put("REFRESH", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { 
                mazePanel.randomize(); 
            }
        });
        
        // ADD THIS: Key binding for ESC to pause
        mazePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "PAUSE");
        mazePanel.getActionMap().put("PAUSE", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!mazePanel.gameOver && mazePanel.lives > 0) {
                    showPauseMenu();
                }
            }
        });
    }
    
    // ADD THIS METHOD
    void showPauseMenu() {
        isPaused = true;
        mazePanel.setEnabled(false);
        
        PauseMenuDialog pauseDialog = new PauseMenuDialog(this, playerName);
        pauseDialog.setVisible(true);
        
        isPaused = false;
        mazePanel.setEnabled(true);
        mazePanel.requestFocusInWindow();
    }
    
    // ADD THIS METHOD
    boolean isPaused() {
        return isPaused;
    }
}

/**
 * Main panel class that handles all game rendering and image management.
 * Extends JPanel to provide custom painting and image handling capabilities.
 */
class MazePanel extends JPanel {
    String playerName;           // ADD THIS
    MazeFrame parentFrame;
    // Game sprite images
    private Image keyImage;         // Key pickup sprite
    private Image doorImage;        // Exit door sprite
    private Image playerIdle;       // Player standing still
    private Image playerMove1;      // Player walking animation frame 1
    private Image playerMove2;      // Player walking animation frame 2
    private Image enemyImage;       // Enemy character sprite
    private Image trapImage;        // Hazard/trap sprite
    private Image treasureImage;    // Treasure chest sprite
    
    private boolean isWalking = false;
    private boolean useFirstFrame = true;
    
    Maze maze;
     int rows;
     int  cols;
     int margin = 10;
     int playerRow = 0;
     int playerCol = 0;
    int score = 0;
    double playerRotation = 0;
    int lives = 3;
    int keyRow = -1;
    int keyCol = -1;
    boolean hasKey = false;
    int doorRow = -1;
    int doorCol = -1;
    boolean gameOver = false;

    // New field: difficulty (ADDED)
    private final Difficulty difficulty;

    // Enemy class definition (kept same)
    static class Enemy {
        int r;
        int c;
        double rotation;
        Cell lastPosition;
        int stuckCounter;

        Enemy(int r, int c) {
            this.r = r;
            this.c = c;
            this.rotation = 0;
            this.lastPosition = null;
            this.stuckCounter = 0;
        }
        
        void updateRotation(int newR, int newC) {
            if (newR < r) rotation = 270;
            else if (newR > r) rotation = 90;
            else if (newC < c) rotation = 180;
            else if (newC > c) rotation = 0;
        }

        //updating score
        

        boolean isStuck(Cell currentCell) {
            if (lastPosition != null) {
                int manhattanDistance = Math.abs(lastPosition.r - currentCell.r) + 
                                      Math.abs(lastPosition.c - currentCell.c);
                if (manhattanDistance <= 1) {
                    stuckCounter++;
                } else {
                    stuckCounter = 0;
                }
            }
            lastPosition = currentCell;
            return stuckCounter > 3;
        }
    }
    Enemy enemy = null;                 
    Random enemyRand = new Random();

    void updateScore() {
    try (Connection conn = Connectivity.connectingToDatabase()) {
        // Update score and games played
        String query = "UPDATE game SET scores = scores + ?, number_of_games_played = number_of_games_played + 1 WHERE user = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, score);
        pstmt.setString(2, playerName);
        pstmt.executeUpdate();
        System.out.println("Score updated for " + playerName);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // MazePanel constructor now receives difficulty (MODIFIED)
    MazePanel(int rows, int cols, Difficulty difficulty, String playerName, MazeFrame parentFrame) {
    this.rows = rows; 
    this.cols = cols;
    this.difficulty = difficulty;
    this.playerName = playerName;      // ADD THIS
    this.parentFrame = parentFrame;    // ADD THIS
        

        // Load images (unchanged)
        try {
            File keyFile = new File("key2.png");
            File doorFile = new File("door.png");
            File idleFile = new File("player_idel.png");
            File move1File = new File("player_move1.png");
            File move2File = new File("player_move2.png");
            File enemyFile = new File("enemy.png");
            File trapFile = new File("trap.png");
            File treasureFile = new File("treasure.png");
            
            if (keyFile.exists()) {
                keyImage = ImageIO.read(keyFile);
            }
            if (doorFile.exists()) {
                doorImage = ImageIO.read(doorFile);
            }
            if (idleFile.exists() && move1File.exists() && move2File.exists()) {
                playerIdle = ImageIO.read(idleFile);
                playerMove1 = ImageIO.read(move1File);
                playerMove2 = ImageIO.read(move2File);
            }
            if (enemyFile.exists()) {
                enemyImage = ImageIO.read(enemyFile);
            }
            if (trapFile.exists()) {
                trapImage = ImageIO.read(trapFile);
            }
            if (treasureFile.exists()) {
                treasureImage = ImageIO.read(treasureFile);
            }
        } catch (IOException e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
        
        randomize(); // generate initial maze according to difficulty
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Math.max(400, cols * 24), Math.max(300, rows * 24 + 30)));
    
      // Player movement (arrows + WASD)
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (lives <= 0 || gameOver) return;

                switch (key) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        movePlayer(-1, 0);
                        break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        movePlayer(1, 0);
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        movePlayer(0, -1);
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        movePlayer(0, 1);
                        break;

                    default:
                        break;
                }
            }
        });

    }
     
    // helper: place key at generation time (ensure not on trap/start/exit)
    void placeInitialKey() {
        keyRow = -1;
        keyCol = -1;
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            int rr = r.nextInt(rows);
            int cc = r.nextInt(cols);
            if ((rr == 0 && cc == 0) || (rr == doorRow && cc == doorCol)) continue;
            Cell cell = maze.grid[rr][cc];
            if (!cell.trap && !cell.treasure) {
                keyRow = rr;
                keyCol = cc;
                break;
            }
        }
    }

    // Spawn enemy at the bottom-right corner
    void spawnEnemy() {
        int rr = rows - 1;
        int cc = cols - 1;

        Cell cell = maze.grid[rr][cc];
        if (cell.wall[0] && cell.wall[1] && cell.wall[2] && cell.wall[3]) {
            cell.wall[0] = false;
        }

        enemy = new Enemy(rr, cc);
    }

    // get accessible neighbors of a cell (N,E,S,W)
    List<int[]> accessibleNeighbors(int r, int c) {
        List<int[]> list = new ArrayList<>();
        Cell cur = maze.grid[r][c];

        if (!cur.wall[0] && r > 0) //up
        list.add(new int[]{r-1, c});

        if (!cur.wall[1] && c < cols-1) //right
        list.add(new int[]{r, c+1});

        if (!cur.wall[2] && r < rows-1)//down
         list.add(new int[]{r+1, c});

        if (!cur.wall[3] && c > 0)//left
         list.add(new int[]{r, c-1});

        return list;
    }

    /**
     * Handles enemy movement with intelligence adjusted by difficulty (MODIFIED).
     * EASY: deprioritize optimal move and favor randomness (enemy avoids player's path).
     * MEDIUM: ~50% optimal, 50% random/suboptimal.
     * HARD: mostly optimal (high chance of best move), small randomness.
     */
    void enemyMoveTick() {
        if (enemy == null) return;

        Cell currentCell = maze.grid[enemy.r][enemy.c];
        boolean isStuck = enemy.isStuck(currentCell);

        // Collect all valid moves from current position
        List<Cell> possibleMoves = new ArrayList<>();
        if (!currentCell.wall[0] && enemy.r > 0)
            possibleMoves.add(maze.grid[enemy.r - 1][enemy.c]);
        if (!currentCell.wall[1] && enemy.c < cols - 1)
            possibleMoves.add(maze.grid[enemy.r][enemy.c + 1]);
        if (!currentCell.wall[2] && enemy.r < rows - 1)
            possibleMoves.add(maze.grid[enemy.r + 1][enemy.c]);
        if (!currentCell.wall[3] && enemy.c > 0)
            possibleMoves.add(maze.grid[enemy.r][enemy.c - 1]);

        if (possibleMoves.isEmpty()) return;

        // Get optimal path to player
        List<Cell> bestPath = maze.findPath(enemy.r, enemy.c, playerRow, playerCol);
        if (bestPath.size() <= 1) {
            // No path to player exists; just pick random move
            Cell chosen = possibleMoves.get(enemyRand.nextInt(possibleMoves.size()));
            enemy.updateRotation(chosen.r, chosen.c);
            enemy.r = chosen.r; enemy.c = chosen.c;
            repaint();
            return;
        }

        // Remove last position to avoid immediate backtracking if present
        if (enemy.lastPosition != null) {
            possibleMoves.remove(enemy.lastPosition);
        }

        Cell optimalNext = bestPath.get(1); // next step toward player
        List<Cell> ratedMoves = new ArrayList<>();

        // Add move ordering: put optimal first if available
        if (possibleMoves.contains(optimalNext)) ratedMoves.add(optimalNext);
        // add other moves that still lead to player (suboptimal)
        for (Cell m : possibleMoves) {
            if (m != optimalNext) {
                List<Cell> alt = maze.findPath(m.r, m.c, playerRow, playerCol);
                if (!alt.isEmpty()) ratedMoves.add(m);
            }
        }
        // remaining moves last
        for (Cell m : possibleMoves) if (!ratedMoves.contains(m)) ratedMoves.add(m);

        if (ratedMoves.isEmpty()) return;

        Cell chosenMove;
        double chance = enemyRand.nextDouble();

        // Difficulty-adjusted probabilities (MODIFIED)
        double optProb; // probability to select optimal move
        double subProb; // probability to select second-best/suboptimal
        switch (difficulty) {
            case EASY:
                // Enemy avoids player's path: very low optimal probability
                optProb = 0.20;
                subProb = 0.30;
                break;
            case MEDIUM:
                // Balanced 50/50
                optProb = 0.50;
                subProb = 0.30;
                break;
            case HARD:
            default:
                // Hard is smart: high optimal probability
                optProb = 0.85;
                subProb = 0.10;
                break;
        }

        if (isStuck) {
            // stuck => increase randomness a bit to break loops
            if (chance < 0.5) chosenMove = ratedMoves.get(enemyRand.nextInt(ratedMoves.size()));
            else chosenMove = ratedMoves.get(0);
        } else {
            if (chance < optProb && ratedMoves.contains(optimalNext)) {
                chosenMove = optimalNext;
            } else if (chance < optProb + subProb && ratedMoves.size() > 1) {
                int idx = Math.min(1, ratedMoves.size()-1);
                chosenMove = ratedMoves.get(idx);
            } else {
                chosenMove = ratedMoves.get(ratedMoves.size()-1);
            }
        }

        // For EASY mode: extra rule "enemy should not choose the same path as the user"
        // We interpret this as: avoid moving into the cell the player *just moved into* if possible.
        // We attempt to avoid player's current cell only when difficulty is EASY and there is an alternative.
        if (difficulty == Difficulty.EASY) {
            // If chosenMove equals player's position and an alternative exists, pick alternative
            if (chosenMove.r == playerRow && chosenMove.c == playerCol) {
                for (Cell alt : ratedMoves) {
                    if (alt.r != playerRow || alt.c != playerCol) {
                        chosenMove = alt;
                        break;
                    }
                }
            }
        }

        // Execute chosen move
        enemy.updateRotation(chosenMove.r, chosenMove.c);
        enemy.r = chosenMove.r; enemy.c = chosenMove.c;

        // If enemy catches the player
        if (enemy.r == playerRow && enemy.c == playerCol) {
            JOptionPane.showMessageDialog(this, "💀 Enemy caught you!");
            lives--;
            if (lives <= 0) {
                gameOver = true;
                int opt = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over! Play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
                );
                if (opt == JOptionPane.YES_OPTION) {
                    randomize();
                }
            } else {
                playerRow = 0;
                playerCol = 0;
            }
        }

        repaint();
    }
   

    // candidate helper for enemy decisions
    static class Candidate {
        int r;
        int c;
        int len;

        Candidate(int r,int c,int len){
             this.r=r; 
             this.c=c; 
             this.len=len; 
        }
    }

    // regenerate with a fresh RNG and ensure door/path/traps per your rules (MODIFIED)
    void randomize() {
        this.maze = new Maze(rows, cols);
        this.maze.generate();

        // pick a random door location ensuring there is at least some path
        Random r = new Random();
        List<Cell> path = new ArrayList<>();
        int i = 0;
        do {
            doorRow = r.nextInt(rows);
            doorCol = r.nextInt(cols);
            if (doorRow == 0 && doorCol == 0) continue;
            path = maze.findPath(0, 0, doorRow, doorCol);
            i++;
            if (i > 1000) break; // fallback
        } while (path.isEmpty());

        // clear previous traps/treasures
        for (int rr = 0; rr < rows; rr++) {
            for (int cc = 0; cc < cols; cc++) {
                maze.grid[rr][cc].trap = false;
                maze.grid[rr][cc].treasure = false;
            }
        }

        // place traps/treasures OFF the chosen safe path
        Set<Cell> pathSet = new HashSet<>(path);
        for (int rr = 0; rr < rows; rr++) {
            for (int cc = 0; cc < cols; cc++) {
                Cell cell = maze.grid[rr][cc];
                if (rr == 0 && cc == 0) continue;
                if (rr == doorRow && cc == doorCol) continue;
                if (pathSet.contains(cell)) continue;
                double p = r.nextDouble();
                if (p < 0.15) cell.trap = true;
                else if (p < 0.25) cell.treasure = true;
            }
        }

        // allow exactly one trap on the path to make it non-obvious (kept)
        if (path.size() > 2) {
            List<Cell> innerPath = new ArrayList<>(path);
            innerPath.remove(0);
            innerPath.remove(innerPath.size()-1);
            if (!innerPath.isEmpty()) {
                Cell chosen = innerPath.get(r.nextInt(innerPath.size()));
                chosen.trap = true;
            }
        }

        // === NEW: Create loops to increase the number of shortest paths ===
        // targetPaths depends on difficulty:
        int targetPaths;
        switch (difficulty) {
            case EASY: targetPaths = 5; break;
            case MEDIUM: targetPaths = 3; break;
            case HARD: default: targetPaths = 2; break;
        }

        // Count current number of shortest paths
        int currentPaths = maze.countShortestPaths(0, 0, doorRow, doorCol);
        // We'll attempt to remove some walls (create "passages") that increase the
        // number of shortest paths. We try up to a limited number of attempts.
        int attempts = 0;
        int maxAttempts = 600; // safety cap
        while (currentPaths < targetPaths && attempts < maxAttempts) {
            attempts++;
            // pick a random cell not equal to start or goal
            int ar = r.nextInt(rows);
            int ac = r.nextInt(cols);
            if (ar == 0 && ac == 0) continue;
            if (ar == doorRow && ac == doorCol) continue;
            Cell A = maze.grid[ar][ac];

            // choose a neighbor direction randomly
            List<int[]> neigh = new ArrayList<>();
            if (ar > 0) neigh.add(new int[]{ar-1, ac});
            if (ac < cols-1) neigh.add(new int[]{ar, ac+1});
            if (ar < rows-1) neigh.add(new int[]{ar+1, ac});
            if (ac > 0) neigh.add(new int[]{ar, ac-1});
            if (neigh.isEmpty()) continue;
            int[] pick = neigh.get(r.nextInt(neigh.size()));
            Cell B = maze.grid[pick[0]][pick[1]];

            // If they are already connected, skip
            boolean alreadyConnected = false;
            if (A.r == B.r) {
                if (A.c + 1 == B.c) alreadyConnected = !A.wall[1];
                else if (A.c - 1 == B.c) alreadyConnected = !A.wall[3];
            } else if (A.c == B.c) {
                if (A.r + 1 == B.r) alreadyConnected = !A.wall[2];
                else if (A.r - 1 == B.r) alreadyConnected = !A.wall[0];
            }
            if (alreadyConnected) continue;

            // Create a loop by removing the wall between A and B
            maze.removeWall(A, B);

            // Recompute count
            int newCount = maze.countShortestPaths(0, 0, doorRow, doorCol);
            if (newCount >= currentPaths) {
                // Accept the change (it may or may not have increased short paths)
                currentPaths = newCount;
            } else {
                // If the change somehow decreased count (unlikely), revert (re-add wall)
                // Re-adding wall: set walls as if A and B not connected.
                // Determine relation and re-block
                if (A.r == B.r) {
                    if (A.c + 1 == B.c) { A.wall[1] = true; B.wall[3] = true; }
                    else if (A.c - 1 == B.c) { A.wall[3] = true; B.wall[1] = true; }
                } else if (A.c == B.c) {
                    if (A.r + 1 == B.r) { A.wall[2] = true; B.wall[0] = true; }
                    else if (A.r - 1 == B.r) { A.wall[0] = true; B.wall[2] = true; }
                }
            }
        }
        // Note: it's possible we cannot reach exact targetPaths; this loop attempts to
        // increase the number of shortest paths to at least the target by creating
        // extra connections (loops). This approach is conservative and tries not to
        // break your core maze generation logic.

        // place the key (visible from start) while ensuring it's not on trap/door/start
        placeInitialKey();

        // reset player state
        playerRow = 0;
        playerCol = 0;
        score = 0;
        lives = 3;
        hasKey = false;
        gameOver = false;
        spawnEnemy();

        repaint();
    }

     void movePlayer(int dr, int dc) {
        // Update player rotation based on input direction
        if (dr == -1) playerRotation = 0;
        else if (dr == 1) playerRotation = 180;
        else if (dc == -1) playerRotation = 270;
        else if (dc == 1) playerRotation = 90;
        
        repaint();

        int newR = playerRow + dr;
        int newC = playerCol + dc;
        if (newR < 0 || newR >= rows || newC < 0 || newC >= cols) return;

        Cell cur = maze.grid[playerRow][playerCol];
        Cell next = maze.grid[newR][newC];

        // Check walls before moving
        if (dr == -1 && cur.wall[0]) return; // top blocked
        if (dr == 1 && cur.wall[2]) return; // bottom blocked
        if (dc == -1 && cur.wall[3]) return; // left blocked
        if (dc == 1 && cur.wall[1]) return; // right blocked

        playerRow = newR;
        playerCol = newC;
        
        isWalking = true;
        useFirstFrame = !useFirstFrame;
        repaint();
        
        javax.swing.Timer walkTimer = new javax.swing.Timer(200, e -> {
            useFirstFrame = !useFirstFrame;
            repaint();
        });
        walkTimer.start();
        
        javax.swing.Timer stopTimer = new javax.swing.Timer(400, e -> {
            isWalking = false;
            walkTimer.stop();
            ((javax.swing.Timer)e.getSource()).stop();
            repaint();
        });
        stopTimer.setRepeats(false);
        stopTimer.start();
    
        // check traps/treasures
        if (next.trap) {
    lives--;
    next.trap = false;
    if (lives <= 0) {
        gameOver = true;
        JOptionPane.showMessageDialog(this, "Game Over! You ran out of lives.\nFinal Score: " + score);
        
        int opt = JOptionPane.showConfirmDialog(this,
            "Play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION);
        
        if (opt == JOptionPane.YES_OPTION) {
            randomize();
        } else {
            parentFrame.dispose();
            new EnhancedMainMenu(playerName);
        }
        return;
    }
    showQuickMessage(" Trap! Life lost. Lives: " + lives, new Color(231, 76, 60));
} else if (next.treasure) {
    score += 10;
    next.treasure = false;
    showQuickMessage(" Treasure! +10 points", new Color(241, 196, 15));
}
        // Enemy moves after player moves
        enemyMoveTick();

        // check key pickup (visible from start)
        if (keyRow >= 0 && keyCol >= 0 && playerRow == keyRow && playerCol == keyCol) {
            hasKey = true;
            keyRow = -1; 
            keyCol = -1;
            JOptionPane.showMessageDialog(this, "You found the Key! The Door will now appear at the exit.");
        }

        // check door only when player has the key
        if (hasKey && doorRow >= 0 && doorCol >= 0 && playerRow == doorRow && playerCol == doorCol) {
    gameOver = true;
    updateScore();  // ADD THIS LINE
    
    int opt = JOptionPane.showConfirmDialog(
        this,
        "🎉 VICTORY!\nFinal Score: " + score + "\n\nPlay again?",
        "You Win!",
        JOptionPane.YES_NO_OPTION
    );
    
    if (opt == JOptionPane.YES_OPTION) {
        randomize();
    } else {
        // Return to main menu
        parentFrame.dispose();
        new EnhancedMainMenu(playerName);
    }
    return;
}


        repaint();
    }

    /**
 * Shows a quick auto-disappearing message overlay
 * @param message Text to display
 * @param color Border and text color
 */
void showQuickMessage(String message, Color color) {
    JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), false);
    dialog.setUndecorated(true);
    dialog.setSize(400, 80);
    dialog.setLocationRelativeTo(this);
    
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Semi-transparent dark background
            g2.setColor(new Color(20, 20, 40, 240));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            
            // Colored border
            g2.setColor(color);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 22, 22);
        }
    };
    panel.setLayout(new BorderLayout());
    
    JLabel label = new JLabel(message, SwingConstants.CENTER);
    label.setFont(new Font("Segoe UI", Font.BOLD, 18));
    label.setForeground(color);
    panel.add(label);
    
    dialog.setContentPane(panel);
    dialog.setVisible(true);
    
    // Auto-close after 1 second (change to 500 for 0.5 seconds)
    Timer closeTimer = new Timer(1000, e -> dialog.dispose());
    closeTimer.setRepeats(false);
    closeTimer.start();
    
    // Allow ENTER key to dismiss immediately
    dialog.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                closeTimer.stop();
                dialog.dispose();
            }
        }
    });
    
    // Make dialog focusable
    dialog.setFocusable(true);
    dialog.requestFocus();
}

 /**
  * Main rendering method that draws the entire game state.
  */
 @Override protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    int w = getWidth() - 2 * margin;
    int h = getHeight() - 2 * margin - 30;
    int cellSize = Math.max(6, Math.min(w / Math.max(1, cols), h / Math.max(1, rows)));

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int offsetX = margin + (w - cellSize * cols) / 2;
    int offsetY = margin + (h - cellSize * rows) / 2;

    // draw cells walls
    g2.setColor(Color.WHITE);
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            Cell cell = maze.grid[r][c];
            int x = offsetX + c * cellSize;
            int y = offsetY + r * cellSize;
            if (cell.wall[0]) g2.drawLine(x, y, x + cellSize, y); // top
            if (cell.wall[1]) g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize); // right
            if (cell.wall[2]) g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize); // bottom
            if (cell.wall[3]) g2.drawLine(x, y, x, y + cellSize); // left

            // draw traps and treasures
            if (cell.trap) {
                if (trapImage != null) {
                    int imgSize = (int)(cellSize * 0.9);
                    int imgX = x + (cellSize - imgSize) / 2;
                    int imgY = y + (cellSize - imgSize) / 2;
                    g2.drawImage(trapImage, imgX, imgY, imgSize, imgSize, null);
                } else {
                    g2.setColor(new Color(255, 0, 0, 150));
                    g2.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
                    g2.setColor(Color.WHITE);
                }
            } else if (cell.treasure) {
                if (treasureImage != null) {
                    int imgSize = (int)(cellSize * 0.9);
                    int imgX = x + (cellSize - imgSize) / 2;
                    int imgY = y + (cellSize - imgSize) / 2;
                    g2.drawImage(treasureImage, imgX, imgY, imgSize, imgSize, null);
                } else {
                    g2.setColor(new Color(255, 215, 0, 150));
                    g2.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
                    g2.setColor(Color.WHITE);
                }
            }
        }
    }

    // draw door (after key)
    if (hasKey && doorRow >= 0 && doorCol >= 0) {
        int dx = offsetX + doorCol * cellSize;
        int dy = offsetY + doorRow * cellSize;
        
        if (doorImage != null) {
            int imgSize = (int)(cellSize * 0.9);
            int imgX = dx + (cellSize - imgSize) / 2;
            int imgY = dy + (cellSize - imgSize) / 2;
            g2.drawImage(doorImage, imgX, imgY, imgSize, imgSize, null);
        } else {
            g2.setColor(new Color(218, 165, 32));
            g2.fillRect(dx + cellSize/4, dy + cellSize/4, cellSize/2, cellSize/2);
            g2.setColor(Color.WHITE);
        }
    }

    // draw key (visible from the start)
    if (keyRow >= 0 && keyCol >= 0) {
        int kx = offsetX + keyCol * cellSize;
        int ky = offsetY + keyRow * cellSize;
        if (keyImage != null) {
            int imgSize = (int)(cellSize * 0.9);
            int imgX = kx + (cellSize - imgSize) / 2;
            int imgY = ky + (cellSize - imgSize) / 2;
            g2.drawImage(keyImage, imgX, imgY, imgSize, imgSize, null);
        } else {
            g2.setColor(new Color(255, 215, 0));
            g2.fillOval(kx + cellSize/4, ky + cellSize/4, cellSize/2, cellSize/2);
            g2.setColor(Color.WHITE);
        }
    }

    // draw player
    int px = offsetX + playerCol * cellSize;
    int py = offsetY + playerRow * cellSize;
    
    if (playerIdle != null && playerMove1 != null && playerMove2 != null) {
        int imgSize = (int)(cellSize * 0.9);
        int imgX = px + (cellSize - imgSize) / 2;
        int imgY = py + (cellSize - imgSize) / 2;
        
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(imgX + imgSize/2, imgY + imgSize/2);
        g2.rotate(Math.toRadians(playerRotation));
        
        Image currentFrame;
        if (isWalking) {
            currentFrame = useFirstFrame ? playerMove1 : playerMove2;
        } else {
            currentFrame = playerIdle;
        }
        
        g2.drawImage(currentFrame, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
        g2.setTransform(oldTransform);
    } else {
        g2.setColor(new Color(0, 255, 255));
        g2.fillOval(px + cellSize/4, py + cellSize/4, cellSize/2, cellSize/2);
    }

    // draw enemy
    if (enemy != null) {
        int ex = offsetX + enemy.c * cellSize;
        int ey = offsetY + enemy.r * cellSize;
        if (enemyImage != null) {
            int imgSize = (int)(cellSize * 0.9);
            int imgX = ex + (cellSize - imgSize) / 2;
            int imgY = ey + (cellSize - imgSize) / 2;
            
            AffineTransform oldTransform = g2.getTransform();
            g2.translate(imgX + imgSize/2, imgY + imgSize/2);
            g2.rotate(Math.toRadians(enemy.rotation));
            g2.drawImage(enemyImage, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
            g2.setTransform(oldTransform);
        } else {
            g2.setColor(new Color(70, 0, 0));
            g2.fillOval(ex + cellSize/4, ey + cellSize/4, cellSize/2, cellSize/2);
            g2.setColor(Color.WHITE);
        }
    }

    // HUD (scoreboard area)
    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(0, getHeight() - 30, getWidth(), 30);

    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Consolas", Font.BOLD, 16));
    String hud = String.format("Score: %d   Lives: %d   Difficulty: %s", score, lives, difficulty.name());
    FontMetrics fm = g2.getFontMetrics();

    int textWidth = fm.stringWidth(hud);
    g2.drawString(hud, (getWidth() - textWidth) / 2, getHeight() - 10);

    g2.dispose();
}
}



// ═══════════════════════════════════════════════════════════════
// BEAUTIFUL PAUSE MENU DIALOG
// ═══════════════════════════════════════════════════════════════
class PauseMenuDialog extends JDialog {
    private List<FloatingParticle> particles = new ArrayList<>();
    private javax.swing.Timer animationTimer;
    
    public PauseMenuDialog(MazeFrame parent, String playerName) {
        super(parent, "Game Paused", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setResizable(false);
        
        // Create particles
        for (int i = 0; i < 40; i++) {
            particles.add(new FloatingParticle(600, 500));
        }
        
        PausePanel panel = new PausePanel(parent, playerName, this);
        setContentPane(panel);
        
        // Animation
        animationTimer = new javax.swing.Timer(40, e -> {
            for (FloatingParticle p : particles) p.update();
            repaint();
        });
        animationTimer.start();
    }
    
    class FloatingParticle {
        double x, y, vx, vy, size, alpha;
        Color color;
        
        FloatingParticle(int w, int h) {
            x = Math.random() * w;
            y = Math.random() * h;
            vx = -0.5 + Math.random();
            vy = -0.5 + Math.random();
            size = 2 + Math.random() * 4;
            alpha = 0.3 + Math.random() * 0.4;
            
            int choice = (int)(Math.random() * 3);
            switch(choice) {
                case 0: color = new Color(100, 200, 255); break;
                case 1: color = new Color(150, 100, 255); break;
                default: color = new Color(255, 200, 100); break;
            }
        }
        
        void update() {
            x += vx;
            y += vy;
            if (x < 0) x = 600;
            if (x > 600) x = 0;
            if (y < 0) y = 500;
            if (y > 500) y = 0;
        }
        
        void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            g2.setColor(color);
            g2.fillOval((int)x, (int)y, (int)size, (int)size);
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval((int)x, (int)y, (int)(size/2), (int)(size/2));
        }
    }
    
    class PausePanel extends JPanel {
        private GlowingButton resumeBtn, menuBtn, logoutBtn;
        private MazeFrame parent;
        private String playerName;
        private PauseMenuDialog dialog;
        private float titlePulse = 0;
        
        PausePanel(MazeFrame parent, String playerName, PauseMenuDialog dialog) {
            this.parent = parent;
            this.playerName = playerName;
            this.dialog = dialog;
            
            setLayout(null);
            setOpaque(false);
            
            SwingUtilities.invokeLater(this::createButtons);
            
           javax.swing.Timer  pulseTimer = new javax.swing.Timer(60, e -> {
                titlePulse += 0.15f;
                repaint();
            });
            pulseTimer.start();
        }
        
        void createButtons() {
            // Resume button
            resumeBtn = new GlowingButton("▶ RESUME", new Color(46, 204, 113));
            resumeBtn.setBounds(150, 180, 300, 70);
            resumeBtn.addActionListener(e -> {
                dialog.animationTimer.stop();
                dialog.dispose();
            });
            add(resumeBtn);
            
            // Main Menu button
            menuBtn = new GlowingButton("🏠 MAIN MENU", new Color(52, 152, 219));
            menuBtn.setBounds(150, 270, 300, 70);
            menuBtn.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Return to main menu?\n(Current game will be lost)",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    dialog.animationTimer.stop();
                    dialog.dispose();
                    parent.dispose();
                    new EnhancedMainMenu(playerName);
                }
            });
            add(menuBtn);
            
            // Logout button
            logoutBtn = new GlowingButton("⏻ LOGOUT", new Color(231, 76, 60));
            logoutBtn.setBounds(150, 360, 300, 70);
            logoutBtn.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Logout and return to login screen?\n(Current game will be lost)",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    dialog.animationTimer.stop();
                    dialog.dispose();
                    parent.dispose();
                    new EnhancedLogin();
                }
            });
            add(logoutBtn);
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dark glass background
            GradientPaint bg = new GradientPaint(0, 0, new Color(20, 20, 40, 250),
                                                 0, getHeight(), new Color(40, 40, 80, 250));
            g2.setPaint(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            
            // Particles
            for (FloatingParticle p : ((PauseMenuDialog)SwingUtilities.getWindowAncestor(this)).particles) {
                p.draw(g2);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            // Border glow
            for (int i = 5; i > 0; i--) {
                g2.setColor(new Color(100, 200, 255, 40 - i * 7));
                g2.setStroke(new BasicStroke(i * 2));
                g2.drawRoundRect(i, i, getWidth() - i * 2, getHeight() - i * 2, 30, 30);
            }
            
            // Title with pulse
            g2.setFont(new Font("Impact", Font.BOLD, 60));
            String title = "PAUSED";
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(title);
            int x = (getWidth() - w) / 2;
            int y = 100;
            
            float glowSize = 12 + (float)(Math.sin(titlePulse) * 5);
            for (int i = (int)glowSize; i > 0; i--) {
                float alpha = (glowSize - i) / glowSize * 0.4f;
                g2.setColor(new Color(100, 200, 255, (int)(alpha * 255)));
                g2.drawString(title, x - i/2, y - i/2);
            }
            
            GradientPaint titleGrad = new GradientPaint(x, y - 25, new Color(150, 220, 255),
                                                        x, y + 25, new Color(100, 180, 255));
            g2.setPaint(titleGrad);
            g2.drawString(title, x, y);
            
            // Instruction text
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2.setColor(new Color(200, 200, 255, 180));
            String instruction = "Press ESC to resume or choose an option below";
            int instrWidth = g2.getFontMetrics().stringWidth(instruction);
            g2.drawString(instruction, (getWidth() - instrWidth) / 2, 140);
        }
    }
}
