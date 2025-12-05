import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.GlyphVector;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;



public class Maze{
    public static void main(String[] args) {
        //System.out.println("Welcome to the Maze Game!");
        // Additional maze game logic would go here
        SwingUtilities.invokeLater(() -> new Login());
    }
}

class Connectivity{


static {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace(); 
    }
}



private static final String URL = "jdbc:mysql://localhost:3306/project3"; // replace with your actual database URL
private static final String USER = "root";
private static final String PASSWORD = "password"; // replace with your actual password

public static Connection connectingToDatabase() {
    try {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
        e.printStackTrace(); // to get detailed message
        return null;
    }
}


}



class Login {
    //static Connection conn;
    String r;
    int id;




	//login method
    int ToLogin(String x) {
        try {
	Connection conn = Connectivity.connectingToDatabase();

            String query = "SELECT id FROM users WHERE user = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, x);
                //pstmt.setString(2, y);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                       
                       // r = rs.getString("role");
                        id = rs.getInt("id");
                    } else {
                        System.out.println("NO record found");
                    }
                }
            } catch (SQLException e) {
                System.out.println("error : " + e.getMessage());
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return id;
    }


    Login() {
        JFrame f = new JFrame("Login Portal");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Assignment Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginButton);

	JButton CreateAccountButton = new JButton("Create Account");
	CreateAccountButton.setBounds(0,0,200,60);
	CreateAccountButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        CreateAccountButton.setBackground(Color.GREEN);
	loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	f.add(CreateAccountButton);

    loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText();
                //String pass = new String(passwordField.getPassword());

                if (user.equals("")) {
                    JOptionPane.showMessageDialog(f, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    //String role = ToLogin(user);
                    // Further actions based on role can be implemented here
                    int id = ToLogin(user);
                    JOptionPane.showMessageDialog(f, "Login successful! User ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                    f.dispose();
                    SwingUtilities.invokeLater(() -> new MainMenuFrame("PlayerOne"));



                }

            }
        });

        f.add(mainPanel, BorderLayout.CENTER);
        f.setVisible(true);
    }
}


//class to create account
class CreateAccount{

public static void CreateAcc(String user,JFrame f){
try (Connection conn = Connectivity.connectingToDatabase()){
String query = "Insert into game (user,score,number_of_games_played) values(?,0,0)";
try(PreparedStatement pstmt = conn.prepareStatement(query)){
pstmt.setString(1, user);
int rowsAffected = pstmt.executeUpdate();
if(rowsAffected == 1){
JOptionPane.showMessageDialog(f, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
f.dispose();
new CreateAccount();
}
}catch (SQLException ex) { System.out.println("error : " + ex.getMessage());}
}catch (SQLException ex) {System.out.println("Connection error: " + ex.getMessage());}


}

CreateAccount(){

JFrame f = new JFrame("Create Account");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(10, 10));
	
	JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Create new Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(15));

	JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createVerticalStrut(15));
	
        JPanel pwdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JPasswordField pwdField = new JPasswordField(20);
        pwdField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        pwdPanel.add(pwdLabel);
        pwdPanel.add(pwdField);
        mainPanel.add(pwdPanel);
        mainPanel.add(Box.createVerticalStrut(15));

	JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel roleLabel = new JLabel("Choose your role:");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JRadioButton Teacher = new JRadioButton("Teacher");
        JRadioButton Student = new JRadioButton("Student");
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(Teacher);
        radioGroup.add(Student);
	rolePanel.add(Teacher);
	rolePanel.add(Student);

	mainPanel.add(rolePanel);
        mainPanel.add(Box.createVerticalStrut(15));

	JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.WHITE);
	backButton.setBounds(0,0,150,70);
	f.add(backButton);

	JButton CreateButton = new JButton("Create");
        CreateButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        CreateButton.setBackground(Color.BLUE);
        CreateButton.setForeground(Color.WHITE);
        CreateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(CreateButton);

	 CreateButton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
	String user = null;
	int permit = 0;
        user = nameField.getText();
	if (user.equals("")) {
	JOptionPane.showMessageDialog(f, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE); //permit--;
       	}


	if (permit > 0)
	CreateAccount.CreateAcc(user,f);

	}});
        


	backButton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
            new Login();
        }});

	f.add(mainPanel, BorderLayout.CENTER);
	f.setVisible(true);
}



}



















//dashboard

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║          MAZE ADVENTURE - MAIN MENU                          ║
 * ║  Stunning menu system with 3D effects and animations        ║
 * ╚══════════════════════════════════════════════════════════════╝
 */

class MainMenuFrame extends JFrame {
    private Image backgroundImage; // Place "menu_bg.png" or "menu_bg.jpg" in folder
    private List<FloatingOrb> orbs = new ArrayList<>();
    private javax.swing.Timer animationTimer;
    private String playerName;
    
    public MainMenuFrame(String playerName) {
        this.playerName = playerName;
        
        setTitle("Maze Adventure - Main Menu");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load background - PLACE "menu_bg.png" or "menu_bg.jpg" IN PROJECT FOLDER
        try {
            File bgFile = new File("menu_bg.png");
            if (!bgFile.exists()) {
                bgFile = new File("menu_bg.jpg");
            }
            if (bgFile.exists()) {
                backgroundImage = ImageIO.read(bgFile);
                System.out.println("✓ Menu background loaded");
            } else {
                System.out.println("⚠ Menu background not found - using animated gradient");
            }
        } catch (Exception e) {
            System.out.println("⚠ Could not load menu background");
        }
        
        // Create main menu panel
        MainMenuPanel menuPanel = new MainMenuPanel();
        setContentPane(menuPanel);
        
        // Create floating orbs
        for (int i = 0; i < 20; i++) {
            orbs.add(new FloatingOrb());
        }
        
        // Animation timer
        animationTimer = new javax.swing.Timer(20, e -> {
            for (FloatingOrb orb : orbs) {
                orb.update();
            }
            repaint();
        });
        animationTimer.start();
        
        setVisible(true);
    }
    
    // Floating orb particles
    class FloatingOrb {
        double x, y, vx, vy, size, alpha;
        Color color;
        double angle;
        double rotSpeed;
        
        FloatingOrb() {
            reset();
        }
        
        void reset() {
            x = Math.random() * 1200;
            y = Math.random() * 800;
            vx = -0.5 + Math.random() * 1;
            vy = -0.5 + Math.random() * 1;
            size = 20 + Math.random() * 40;
            alpha = 0.2 + Math.random() * 0.3;
            angle = Math.random() * 360;
            rotSpeed = -1 + Math.random() * 2;
            
            // Mystical colors
            int colorChoice = (int)(Math.random() * 5);
            switch(colorChoice) {
                case 0: color = new Color(138, 43, 226); break; // Purple
                case 1: color = new Color(0, 191, 255); break;  // Deep Sky Blue
                case 2: color = new Color(255, 20, 147); break; // Deep Pink
                case 3: color = new Color(50, 205, 50); break;  // Lime Green
                default: color = new Color(255, 215, 0); break;  // Gold
            }
        }
        
        void update() {
            x += vx;
            y += vy;
            angle += rotSpeed;
            
            // Wrap around
            if (x < -50) x = 1250;
            if (x > 1250) x = -50;
            if (y < -50) y = 850;
            if (y > 850) y = -50;
        }
        
        void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(Math.toRadians(angle));
            
            // Hexagonal orb
            int sides = 6;
            Polygon hex = new Polygon();
            for (int i = 0; i < sides; i++) {
                double angle = 2 * Math.PI * i / sides;
                hex.addPoint(
                    (int)(Math.cos(angle) * size),
                    (int)(Math.sin(angle) * size)
                );
            }
            
            // Gradient fill
            RadialGradientPaint gradient = new RadialGradientPaint(
                0, 0, (float)size,
                new float[]{0f, 0.7f, 1f},
                new Color[]{
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 200),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 100),
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                }
            );
            g2.setPaint(gradient);
            g2.fill(hex);
            
            // Bright center
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval(-3, -3, 6, 6);
            
            g2.setTransform(old);
        }
    }
    
    // Main menu panel
    class MainMenuPanel extends JPanel {
        private float gradientOffset = 0;
        private MenuButton startButton;
        private MenuButton leaderboardButton;
        private MenuButton exitButton;
        
        public MainMenuPanel() {
            setLayout(null);
            setOpaque(false);
            
            SwingUtilities.invokeLater(() -> createComponents());
            
            // Animated gradient
            javax.swing.Timer gradientTimer = new javax.swing.Timer(50, e -> {
                gradientOffset += 0.01f;
                if (gradientOffset > 1) gradientOffset = 0;
                repaint();
            });
            gradientTimer.start();
        }
        
        private void createComponents() {
            // Start Game Button
            startButton = new MenuButton(
                "START GAME",
                new Color(46, 204, 113),
                "⚔"
            );
            startButton.setBounds(400, 300, 400, 80);
            startButton.addActionListener(e -> startGame());
            add(startButton);
            
            // Leaderboard Button
            leaderboardButton = new MenuButton(
                "LEADERBOARD",
                new Color(52, 152, 219),
                "★"
            );
            leaderboardButton.setBounds(400, 400, 400, 80);
            leaderboardButton.addActionListener(e -> showLeaderboard());
            add(leaderboardButton);
            
            // Exit Button
            exitButton = new MenuButton(
                "LOGOUT",
                new Color(231, 76, 60),
                "⏻"
            );
            exitButton.setBounds(400, 500, 400, 80);
            exitButton.addActionListener(e -> logout());
            add(exitButton);
        }
        
        private void startGame() {
            // INTEGRATE YOUR MAZE GAME HERE
            SwingUtilities.invokeLater(() -> { // ensure GUI updates on Event Dispatch Thread
                                             //(It takes a lambda expression  that executes your GUI startup code asynchronously.)
            MazeFrame frame = new MazeFrame(11, 15); //makign grid (instance of game window)
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
            JOptionPane.showMessageDialog(this,
                "Game will start here!\n\nIntegrate:\nnew MazeFrame(11, 15).setVisible(true);",
                "Start Game",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Example: MainMenuFrame.this.dispose(); new MazeFrame(11, 15);
        }
        
        private void showLeaderboard() {
            new LeaderboardFrame(playerName, MainMenuFrame.this);
        }
        
        private void logout() {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                MainMenuFrame.this.dispose();
                new Login();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                g2.setColor(new Color(0, 0, 20, 150));
                g2.fillRect(0, 0, getWidth(), getHeight());
            } else {
                // Animated gradient background
                Color c1 = new Color(
                    (int)(30 + 20 * Math.sin(gradientOffset * Math.PI)),
                    (int)(10 + 10 * Math.cos(gradientOffset * Math.PI)),
                    (int)(60 + 30 * Math.sin(gradientOffset * Math.PI * 2))
                );
                Color c2 = new Color(
                    (int)(80 + 30 * Math.cos(gradientOffset * Math.PI)),
                    (int)(30 + 20 * Math.sin(gradientOffset * Math.PI * 1.5)),
                    (int)(120 + 40 * Math.cos(gradientOffset * Math.PI))
                );
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, c1,
                    getWidth(), getHeight(), c2
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            
            // Draw orbs
            for (FloatingOrb orb : orbs) {
                orb.draw(g2);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            // Draw title
            drawTitle(g2);
            
            // Draw player name
            drawPlayerName(g2);
            
            // Draw decorative frame
            drawFrame(g2);
        }
        
        private void drawTitle(Graphics2D g2) {
            String title = "MAIN MENU";
            Font font = new Font("Impact", Font.BOLD, 60);
            g2.setFont(font);
            
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(title);
            int x = (getWidth() - width) / 2;
            int y = 120;
            
            // 3D shadow effect
            for (int i = 8; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawString(title, x + i, y + i);
            }
            
            // Gradient text
            GradientPaint textGradient = new GradientPaint(
                x, y - 30, new Color(255, 215, 0),
                x, y + 30, new Color(255, 140, 0)
            );
            g2.setPaint(textGradient);
            g2.drawString(title, x, y);
            
            // Outline
            g2.setColor(new Color(139, 69, 19));
            g2.setStroke(new BasicStroke(3));
            Font outlineFont = font.deriveFont(Font.BOLD, 60);
            GlyphVector gv = outlineFont.createGlyphVector(g2.getFontRenderContext(), title);
            Shape outline = gv.getOutline(x, y);
            g2.draw(outline);
        }
        
        private void drawPlayerName(Graphics2D g2) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.setColor(new Color(255, 255, 255, 200));
            String welcome = "Welcome, " + playerName + "!";
            int width = g2.getFontMetrics().stringWidth(welcome);
            g2.drawString(welcome, (getWidth() - width) / 2, 200);
        }
        
        private void drawFrame(Graphics2D g2) {
            g2.setStroke(new BasicStroke(3));
            
            // Outer glow
            for (int i = 0; i < 5; i++) {
                g2.setColor(new Color(100, 200, 255, 30 - i * 5));
                g2.drawRoundRect(20 + i, 20 + i, getWidth() - 40 - i*2, getHeight() - 40 - i*2, 40, 40);
            }
            
            // Main frame
            g2.setColor(new Color(100, 200, 255, 100));
            g2.drawRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 40, 40);
            
            // Corner decorations
            drawCornerDecoration(g2, 40, 40);
            drawCornerDecoration(g2, getWidth() - 40, 40);
            drawCornerDecoration(g2, 40, getHeight() - 40);
            drawCornerDecoration(g2, getWidth() - 40, getHeight() - 40);
        }
        
        private void drawCornerDecoration(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(255, 215, 0, 150));
            for (int i = 0; i < 3; i++) {
                g2.drawOval(x - 10 - i*3, y - 10 - i*3, 20 + i*6, 20 + i*6);
            }
        }
    }
    
    // Custom 3D menu button
    static class MenuButton extends JButton {
        private Color baseColor;
        private String icon;
        private float hoverProgress = 0f;
        private javax.swing.Timer hoverTimer;
        
        public MenuButton(String text, Color baseColor, String icon) {
            super(text);
            this.baseColor = baseColor;
            this.icon = icon;
            
            setFont(new Font("Segoe UI", Font.BOLD, 28));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hoverTimer = new javax.swing.Timer(20, ev -> {
                        hoverProgress = Math.min(1f, hoverProgress + 0.1f);
                        repaint();
                    });
                    hoverTimer.start();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new javax.swing.Timer(20, ev -> {
                        hoverProgress = Math.max(0f, hoverProgress - 0.1f);
                        if (hoverProgress <= 0) {
                            hoverTimer.stop();
                        }
                        repaint();
                    });
                    hoverTimer.start();
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    hoverProgress = 0.5f;
                    repaint();
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    hoverProgress = 1f;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int offset = (int)(5 * (1 - hoverProgress));
            
            // 3D shadow layers
            for (int i = offset; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(i, i, getWidth(), getHeight(), 40, 40);
            }
            
            // Glow effect
            if (hoverProgress > 0) {
                for (int i = 10; i > 0; i--) {
                    float alpha = hoverProgress * (i / 10f) * 0.5f;
                    g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(alpha * 255)));
                    g2.fillRoundRect(-i, -i, getWidth() + i*2, getHeight() + i*2, 40 + i*2, 40 + i*2);
                }
            }
            
            // Main button
            Color buttonColor = hoverProgress > 0 ? baseColor.brighter() : baseColor;
            GradientPaint gradient = new GradientPaint(
                0, 0, buttonColor,
                0, getHeight(), buttonColor.darker()
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            
            // Shine effect
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() / 2, 40, 40);
            
            // Border
            g2.setColor(buttonColor.brighter());
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 40, 40);
            
            // Icon
            g2.setFont(new Font("Segoe UI Symbol", Font.BOLD, 40));
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawString(icon, 30, getHeight() / 2 + 15);
            
            // Text
            g2.setFont(getFont());
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textX = (getWidth() - textWidth) / 2 + 20;
            int textY = getHeight() / 2 + fm.getAscent() / 2 - 2;
            
            // Text shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(getText(), textX + 2, textY + 2);
            
            // Main text
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), textX, textY);
        }
    }
}













/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║          LEADERBOARD FRAME                                   ║
 * ║  Exquisite leaderboard with trophy animations               ║
 * ╚══════════════════════════════════════════════════════════════╝
 */

class LeaderboardFrame extends JFrame {
    private Image trophyImage; // Place "trophy.png" in folder (optional)
    private List<Star> stars = new ArrayList<>();
    private javax.swing.Timer animationTimer;
    
    public LeaderboardFrame(String currentPlayer, JFrame parent) {
        setTitle("Leaderboard - Hall of Champions");
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Load trophy image - PLACE "trophy.png" IN FOLDER (optional)
        try {
            File trophyFile = new File("trophy.png");
            if (trophyFile.exists()) {
                trophyImage = ImageIO.read(trophyFile);
                System.out.println("✓ Trophy image loaded");
            }
        } catch (Exception e) {
            System.out.println("⚠ Trophy image not found - using default");
        }
        
        LeaderboardPanel panel = new LeaderboardPanel(currentPlayer);
        setContentPane(panel);
        
        // Create stars
        for (int i = 0; i < 100; i++) {
            stars.add(new Star());
        }
        
        animationTimer = new javax.swing.Timer(30, e -> {
            for (Star star : stars) {
                star.update();
            }
            repaint();
        });
        animationTimer.start();
        
        setVisible(true);
    }
    
    // Twinkling stars
    class Star {
        double x, y, size, brightness;
        boolean increasing;
        
        Star() {
            x = Math.random() * 900;
            y = Math.random() * 700;
            size = 1 + Math.random() * 2;
            brightness = Math.random();
            increasing = Math.random() > 0.5;
        }
        
        void update() {
            if (increasing) {
                brightness += 0.02;
                if (brightness >= 1) increasing = false;
            } else {
                brightness -= 0.02;
                if (brightness <= 0) increasing = true;
            }
        }
        
        void draw(Graphics2D g2) {
            g2.setColor(new Color(255, 255, 255, (int)(brightness * 255)));
            g2.fillOval((int)x, (int)y, (int)size, (int)size);
            
            // Twinkle effect
            if (brightness > 0.8) {
                g2.setColor(new Color(255, 255, 255, (int)((brightness - 0.8) * 500)));
                g2.drawLine((int)x - 3, (int)y, (int)x + 3, (int)y);
                g2.drawLine((int)x, (int)y - 3, (int)x, (int)y + 3);
            }
        }
    }
    
    class LeaderboardPanel extends JPanel {
        private String currentPlayer;
        private float titlePulse = 0f;
        
        // DUMMY DATA - Replace with actual leaderboard data
        private String[][] leaderboardData = {
            {"1", "Shadow Master", "9999", "🥇"},
            {"2", "Maze Runner", "8754", "🥈"},
            {"3", "Path Finder", "7421", "🥉"},
            {"4", "Labyrinth Lord", "6892", ""},
            {"5", "Dungeon Delver", "6543", ""},
            {"6", "Quest Seeker", "5987", ""},
            {"7", "Adventure King", "5432", ""},
            {"8", "Trail Blazer", "5100", ""},
            {"9", "Explorer Elite", "4876", ""},
            {"10", "Maze Novice", "4321", ""}
        };
        
        public LeaderboardPanel(String currentPlayer) {
            this.currentPlayer = currentPlayer;
            setLayout(new BorderLayout());
            setOpaque(false);
            
            // Back button
            JButton backButton = new MainMenuFrame.MenuButton(
                "BACK",
                new Color(52, 73, 94),
                "←"
            );
            backButton.setPreferredSize(new Dimension(200, 60));
            backButton.addActionListener(e -> {
                LeaderboardFrame.this.dispose();
            });
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setOpaque(false);
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
            
            // Title pulse
            javax.swing.Timer pulseTimer = new javax.swing.Timer(50, e -> {
                titlePulse += 0.1f;
                if (titlePulse > Math.PI * 2) titlePulse = 0;
                repaint();
            });
            pulseTimer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Dark gradient background
            GradientPaint bg = new GradientPaint(
                0, 0, new Color(15, 15, 35),
                0, getHeight(), new Color(30, 30, 60)
            );
            g2.setPaint(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw stars
            for (Star star : ((LeaderboardFrame)SwingUtilities.getWindowAncestor(this)).stars) {
                star.draw(g2);
            }
            
            // Title
            drawTitle(g2);
            
            // Leaderboard table
            drawLeaderboard(g2);
            
            // Decorative elements
            drawDecorations(g2);
        }
        
        private void drawTitle(Graphics2D g2) {
            String title = "HALL OF CHAMPIONS";
            g2.setFont(new Font("Impact", Font.BOLD, 48));
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.stringWidth(title);
            int x = (getWidth() - width) / 2;
            int y = 70;
            
            // Pulsing glow
            float glowSize = 15 + (float)(Math.sin(titlePulse) * 5);
            for (int i = (int)glowSize; i > 0; i--) {
                float alpha = (glowSize - i) / glowSize * 0.3f;
                g2.setColor(new Color(255, 215, 0, (int)(alpha * 255)));
                g2.drawString(title, x - i/2, y - i/2);
            }
            
            // Gradient text
            GradientPaint titleGrad = new GradientPaint(
                x, y - 20, new Color(255, 215, 0),
                x, y + 20, new Color(255, 140, 0)
            );
            g2.setPaint(titleGrad);
            g2.drawString(title, x, y);
        }
        
        private void drawLeaderboard(Graphics2D g2) {
            int startY = 150;
            int rowHeight = 50;
            int tableWidth = 700;
            int tableX = (getWidth() - tableWidth) / 2;
            
            // Semi-transparent panel
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(tableX - 20, startY - 20, tableWidth + 40, rowHeight * 10 + 40, 30, 30);
            
            // Border
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(tableX - 20, startY - 20, tableWidth + 40, rowHeight * 10 + 40, 30, 30);
            
            // Header
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("RANK", tableX, startY - 5);
            g2.drawString("PLAYER", tableX + 100, startY - 5);
            g2.drawString("SCORE", tableX + 450, startY - 5);
            g2.drawString("TROPHY", tableX + 600, startY - 5);
            
            // Rows
            for (int i = 0; i < leaderboardData.length; i++) {
                int y = startY + 20 + i * rowHeight;
                String[] row = leaderboardData[i];
                
                // Highlight current player
                boolean isCurrentPlayer = row[1].equals(currentPlayer);
                if (isCurrentPlayer) {
                    g2.setColor(new Color(46, 204, 113, 50));
                    g2.fillRoundRect(tableX - 10, y - 25, tableWidth + 20, rowHeight - 5, 15, 15);
                }
                
                // Alternate row colors
                if (i % 2 == 0) {
                    g2.setColor(new Color(255, 255, 255, 10));
                    g2.fillRoundRect(tableX - 10, y - 25, tableWidth + 20, rowHeight - 5, 15, 15);
                }
                
                // Text color based on rank
                Color textColor;
                if (i == 0) textColor = new Color(255, 215, 0); // Gold
                else if (i == 1) textColor = new Color(192, 192, 192); // Silver
                else if (i == 2) textColor = new Color(205, 127, 50); // Bronze
                else textColor = new Color(200, 200, 255);
                
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g2.setColor(textColor);
                
                // Rank
                g2.drawString(row[0], tableX + 15, y);
                
                // Player name
                g2.drawString(row[1], tableX + 100, y);
                
                // Score
                g2.drawString(row[2], tableX + 450, y);
                
                // Trophy emoji
                if (!row[3].isEmpty()) {
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
                    g2.drawString(row[3], tableX + 600, y);
                }
            }
        }
        
        private void drawDecorations(Graphics2D g2) {
            // Corner trophy emblems
            drawTrophyEmblem(g2, 50, 150);
            drawTrophyEmblem(g2, getWidth() - 50, 150);
            
            // Laurel wreaths
            g2.setColor(new Color(34, 139, 34, 150));
            drawLaurelWreath(g2, getWidth() / 2, 120, 80);
        }
        
        private void drawTrophyEmblem(Graphics2D g2, int x, int y) {
            // Circular background
            RadialGradientPaint circleGrad = new RadialGradientPaint(
                x, y, 40,
                new float[]{0f, 0.7f, 1f},
                new Color[]{
                    new Color(255, 215, 0, 150),
                    new Color(255, 165, 0, 100),
                    new Color(255, 140, 0, 50)
                }
            );
            g2.setPaint(circleGrad);
            g2.fillOval(x - 40, y - 40, 80, 80);
            
            // Trophy icon (simplified)
            g2.setColor(new Color(255, 215, 0));
            
            // Cup
            int[] cupX = {x - 20, x - 15, x - 15, x + 15, x + 15, x + 20};
            int[] cupY = {y - 15, y - 15, y + 10, y + 10, y - 15, y - 15};
            g2.fillPolygon(cupX, cupY, 6);
            
            // Base
            g2.fillRect(x - 25, y + 10, 50, 5);
            g2.fillRect(x - 15, y + 15, 30, 5);
            
            // Handles
            g2.setStroke(new BasicStroke(3));
            g2.drawArc(x - 30, y - 10, 15, 20, 90, 180);
            g2.drawArc(x + 15, y - 10, 15, 20, 270, 180);
            
            // Shine
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillOval(x - 8, y - 8, 8, 12);
        }
        
        private void drawLaurelWreath(Graphics2D g2, int cx, int cy, int radius) {
            g2.setStroke(new BasicStroke(2));
            
            // Left branch
            for (int i = 0; i < 8; i++) {
                double angle = Math.PI * 0.4 + i * 0.15;
                int x = cx + (int)(Math.cos(angle) * radius);
                int y = cy + (int)(Math.sin(angle) * radius);
                
                g2.setColor(new Color(34, 139, 34, 150 - i * 15));
                g2.fillOval(x - 8, y - 5, 16, 10);
            }
            
            // Right branch
            for (int i = 0; i < 8; i++) {
                double angle = Math.PI * 0.6 - i * 0.15;
                int x = cx + (int)(Math.cos(angle) * radius);
                int y = cy + (int)(Math.sin(angle) * radius);
                
                g2.setColor(new Color(34, 139, 34, 150 - i * 15));
                g2.fillOval(x - 8, y - 5, 16, 10);
            }
        }
    }
}

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║          QUICK INTEGRATION GUIDE                             ║
 * ╚══════════════════════════════════════════════════════════════╝
 * 
 * IMAGES TO ADD (place in project folder):
 * 1. yusra1.png or yusra1.jpg - Login background
 * 2. menu_bg.png or menu_bg.jpg - Main menu background (optional)
 * 3. trophy.png - Trophy icon for leaderboard (optional)
 * 
 * If images not found, system uses beautiful animated gradients!
 * 
 * ═══════════════════════════════════════════════════════════════
 * 
 * HOW TO INTEGRATE WITH YOUR MAZE GAME:
 * 
 * 1. In MainMenuFrame.java, find the startGame() method (line ~201)
 * 
 * 2. Replace the JOptionPane with:
 *    
 *    MainMenuFrame.this.dispose();  // Close menu
 *    SwingUtilities.invokeLater(() -> {
 *        MazeFrame mazeFrame = new MazeFrame(11, 15);
 *        mazeFrame.setVisible(true);
 *    });
 * 
 * 3. In your MazeAdventureS.java main() method, change to:
 * 
 *    public static void main(String[] args) {
 *        SwingUtilities.invokeLater(() -> new LoginFrame());
 *    }
 * 
 * 4. When game ends (player wins/loses), return to menu:
 * 
 *    MazeFrame.this.dispose();
 *    new MainMenuFrame(playerName);
 * 
 * ═══════════════════════════════════════════════════════════════
 * 
 * LEADERBOARD INTEGRATION:
 * 
 * In LeaderboardPanel, replace leaderboardData[][] with actual data:
 * 
 * - Read from file/database
 * - Sort by score
 * - Update after each game
 * 
 * Example structure:
 * 
 * class LeaderboardManager {
 *     static void saveScore(String name, int score) { ... }
 *     static String[][] getTopScores(int count) { ... }
 * }
 * 
 * ═══════════════════════════════════════════════════════════════
 * 
 * FEATURES INCLUDED:
 * 
 * ✓ Animated particle systems
 * ✓ Glowing buttons with hover effects
 * ✓ 3D depth effects
 * ✓ Smooth transitions
 * ✓ Pulsing animations
 * ✓ Custom rounded borders
 * ✓ Gradient backgrounds
 * ✓ Floating orbs
 * ✓ Twinkling stars
 * ✓ Trophy emblems
 * ✓ Responsive design
 * ✓ Professional color schemes
 * ✓ Mystical atmosphere
 * 
 * ═══════════════════════════════════════════════════════════════
 * 
 * CUSTOMIZATION IDEAS:
 * 
 * 1. Change color schemes in baseColor parameters
 * 2. Adjust particle count for performance
 * 3. Add sound effects on button clicks
 * 4. Add background music
 * 5. Implement user registration system
 * 6. Add profile pictures
 * 7. Create achievement badges
 * 8. Add animated GIFs as backgrounds
 * 9. Implement fade transitions between screens
 * 10. Add difficulty selection before game start
 * 
 * ═══════════════════════════════════════════════════════════════
 * 
 * PERFORMANCE TIPS:
 * 
 * - Reduce particle count on slower systems
 * - Use buffered images for static elements
 * - Disable animations if FPS drops
 * - Cache gradient paints
 * 
 * ═══════════════════════════════════════════════════════════════
 */




















 //game
/* 
class MazeAdventureS {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // ensure GUI updates on Event Dispatch Thread
                                             //(It takes a lambda expression  that executes your GUI startup code asynchronously.)
            MazeFrame frame = new MazeFrame(11, 15); //makign grid (instance of game window)
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
*/
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
                                             // so the generation algorithm starts with a clean visitation state bcz 
                                            //No cell is marked visited so the generator can visit/mark them properly while generating maze 
            cell.trap = false;
            cell.treasure = false;
        }
    }

    //to implement iterative DFS (backtracking)(so if stuck (wall closed )we could track back )
        Stack <Cell> stack = new Stack<>();
        Cell start = grid[0][0];    //generation begins here 
        start.visited = true;
        stack.push(start);          //pushing starting cell onto stack

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List <Cell> unvisited = new ArrayList<>();//Create a list to store unvisited neighboring cells..(to choose which neighbour cell to visit next )
            int r = current.r ;
            int c = current.c;

            if (r > 0 && !grid[r-1][c].visited) //move up 
            unvisited.add(grid[r-1][c]);//mark unvisited so dont create a cycle 

            if (c < cols-1 && !grid[r][c+1].visited)//move right 
             unvisited.add(grid[r][c+1]);

            if (r < rows-1 && !grid[r+1][c].visited) //move down 
            unvisited.add(grid[r+1][c]);

            if (c > 0 && !grid[r][c-1].visited) //mve left 
            unvisited.add(grid[r][c-1]);

            //choose random unvisited neighbor if any(to move forward in mae making)
            if (!unvisited.isEmpty()) {
                Cell next = unvisited.get(rand.nextInt(unvisited.size()));
                removeWall(current, next);//remove wall (carve path )between current and next cell
                next.visited = true;
                stack.push(next);//next becomes the current cell(moves forward )
            } else {
                stack.pop(); // backtrack (real backtracking step shwoing DFS nature )
                             //(tells that all neighbors visited (stuck )so go back to previous cell )
            }
        }
    }

//function generate ending here 
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
//crving path between two cells by removing walls function ending

    // BFS pathfinder (returns empty list if no path)
    List<Cell> findPath(int sr, int sc, int lr, int lc) {//Defines a method that finds the shortest path between the Start Row/Col and last Row/Col.
        Queue<Cell> q = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        Cell[][] parent = new Cell[rows][cols];//necessary for baktracking the path once goal is reached

        q.add(grid[sr][sc]);//starting cell added to queue
        visited[sr][sc] = true;

        while (!q.isEmpty()) {
            Cell current = q.poll();    //poll function retrieves and removes the head of the queue
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
        if (!visited[lr][lc]) return new ArrayList<>();

        // reconstruct path(backtracking from goal to start using parent array)
        List<Cell> path = new ArrayList<>();
        Cell cur = grid[lr][lc];
        while (cur != null && cur != grid[sr][sc]) {//Walks backward from the goal to the start using the parent matrix.
            path.add(cur);
            cur = parent[cur.r][cur.c];
        }
        path.add(grid[sr][sc]);
        Collections.reverse(path);//Backtracking built the path from goal → start, so this reverses it to start → goal.
        return path;
    }
}


//GUI components 
class MazeFrame extends JFrame {

    private final MazePanel mazePanel;          //the maze drawing
   

    MazeFrame(int rows, int cols, String playerName, DifficultyLevel level) {
        setTitle("Maze Adventure - " + playerName + " - " + level);
        mazePanel = new MazePanel(rows, cols);
        
        setLayout(new BorderLayout());//how components are arranged in the frame
        add(new JScrollPane(mazePanel), BorderLayout.CENTER);

        pack();//Automatically sizes the window just big enough to fit all components neatly.
        setLocationRelativeTo(null);//Centers the window

        // start maximized so it fills screen if user maximizes
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // key binding: R to randomize
        mazePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "REFRESH");
        mazePanel.getActionMap().put("REFRESH", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { mazePanel.randomize(); }
        });
    }
}


/**
 * Main panel class that handles all game rendering and image management.
 * Extends JPanel to provide custom painting and image handling capabilities.
 */
class MazePanel extends JPanel {
    // Game sprite images
    private Image keyImage;         // Key pickup sprite
    private Image doorImage;        // Exit door sprite
    private Image playerIdle;       // Player standing still
    private Image playerMove1;      // Player walking animation frame 1
    private Image playerMove2;      // Player walking animation frame 2
    private Image enemyImage;       // Enemy character sprite
    private Image trapImage;        // Hazard/trap sprite
    private Image treasureImage;    // Treasure chest sprite
    
    /**
     * Animation state control
     * isWalking: Tracks if player is in motion for animation
     * useFirstFrame: Toggles between first/second walking frames
     */
    private boolean isWalking = false;
    private boolean useFirstFrame = true;
    
    Maze maze;
     int rows;
     int  cols;
     int margin = 10;//Helps prevent visuals from touching the panel edges.
     int playerRow = 0;
     int playerCol = 0;
    int score = 0;
    double playerRotation = 0; // Direction player is facing in degrees (0 = right, 90 = down, 180 = left, 270 = up)
    int lives = 3;
    int keyRow = -1;
    int keyCol = -1; // key position (visible from start)
    boolean hasKey = false; // whether player picked key
    int doorRow = -1;
    int doorCol = -1; // exit location (can be anywhere)
    boolean gameOver = false;

    
    // Enemy class definition
    /**
     * Enemy class that implements intelligent pathfinding with anti-stuck mechanisms
     * The enemy can track its position, remember previous locations, and detect when
     * it's stuck in circular patterns.
     */
    static class Enemy {
        int r;              // Current row in maze
        int c;              // Current column in maze
        double rotation;    // Direction enemy is facing in degrees
        Cell lastPosition;  // Previous cell position to prevent backtracking
        int stuckCounter;   // Tracks repeated movements in small area

        /**
         * Creates a new enemy at the specified position
         *  r Starting row
         *  c Starting column
         */
        Enemy(int r, int c) {
            this.r = r;
            this.c = c;
            this.rotation = 0;
            this.lastPosition = null;
            this.stuckCounter = 0;
        }
        
        /**
         * Updates the enemy's rotation based on its movement direction
         * Used to make the enemy sprite face the direction it's moving
         *  newR New row position
         *  newC New column position
         */
        void updateRotation(int newR, int newC) {
            if (newR < r) rotation = 270;      // moving up
            else if (newR > r) rotation = 90;   // moving down
            else if (newC < c) rotation = 180;  // moving left
            else if (newC > c) rotation = 0;    // moving right
        }

        /**
         * Detects if the enemy is stuck in a small area by tracking movement patterns
         * Uses Manhattan distance to detect small-area movement and increases
         * stuckCounter when moving in confined space
         *  currentCell The cell the enemy is currently in
         *  true if stuck in a small area for several moves
         */
        boolean isStuck(Cell currentCell) {
            if (lastPosition != null) {
                // Calculate Manhattan distance between current and last position
                int manhattanDistance = Math.abs(lastPosition.r - currentCell.r) + 
                                      Math.abs(lastPosition.c - currentCell.c);
                
                // If moving in small area (distance <= 1), increment stuck counter
                if (manhattanDistance <= 1) {
                    stuckCounter++;
                } else {
                    // Reset counter if we've moved to a new area
                    stuckCounter = 0;
                }
            }
            // Update last position for next check
            lastPosition = currentCell;
            // Consider stuck if moving in small area for more than 3 moves
            return stuckCounter > 3;
        }
    }
    Enemy enemy = null;                 
    Random enemyRand = new Random();    // random for enemy behavior

    MazePanel(int rows, int cols) {
        this.rows = rows; 
        this.cols = cols;
        
        /**
         * Load all game sprites and images.
         * Each image is loaded with error handling and validation:
         * 1. Check if file exists
         * 2. Attempt to load image
         * 3. Verify loaded image is valid
         * 4. Print status message
         * 
         * If any image fails to load, the game will fall back to simple shape rendering
         */
        try {
            // Initialize file objects for all game sprites
            File keyFile = new File("key2.png");           // Key collectible
            File doorFile = new File("door.png");          // Exit door
            File idleFile = new File("player_idel.png");   // Player standing
            File move1File = new File("player_move1.png"); // Player walk frame 1
            File move2File = new File("player_move2.png"); // Player walk frame 2
            File enemyFile = new File("enemy.png");        // Enemy sprite
            File trapFile = new File("trap.png");          // Hazard sprite
            File treasureFile = new File("treasure.png");   // Treasure sprite
            
            if (keyFile.exists()) {
                keyImage = ImageIO.read(keyFile);
                if (keyImage != null) {
                    System.out.println("Successfully loaded key2.png");
                } else {
                    System.err.println("Failed to load key2.png - image is null");
                }
            } else {
                System.err.println("key2.png file not found at: " + keyFile.getAbsolutePath());
            }

            if (doorFile.exists()) {
                doorImage = ImageIO.read(doorFile);
                if (doorImage != null) {
                    System.out.println("Successfully loaded door.png");
                } else {
                    System.err.println("Failed to load door.png - image is null");
                }
            } else {
                System.err.println("door.png file not found at: " + doorFile.getAbsolutePath());
            }

            // Load all player-related images
            if (idleFile.exists() && move1File.exists() && move2File.exists()) {
                playerIdle = ImageIO.read(idleFile);
                playerMove1 = ImageIO.read(move1File);
                playerMove2 = ImageIO.read(move2File);
                if (playerIdle != null && playerMove1 != null && playerMove2 != null) {
                    System.out.println("Successfully loaded all player images");
                } else {
                    System.err.println("Failed to load player images - one or more images are null");
                }
            } else {
                System.err.println("One or more player images not found in the directory");
            }

            // Load enemy image
            if (enemyFile.exists()) {
                enemyImage = ImageIO.read(enemyFile);
                if (enemyImage != null) {
                    System.out.println("Successfully loaded enemy.png");
                } else {
                    System.err.println("Failed to load enemy.png - image is null");
                }
            } else {
                System.err.println("enemy.png not found at: " + enemyFile.getAbsolutePath());
            }

            // Load trap image
            if (trapFile.exists()) {
                trapImage = ImageIO.read(trapFile);
                if (trapImage != null) {
                    System.out.println("Successfully loaded trap.png");
                } else {
                    System.err.println("Failed to load trap.png - image is null");
                }
            } else {
                System.err.println("trap.png not found at: " + trapFile.getAbsolutePath());
            }

            // Load treasure image
            if (treasureFile.exists()) {
                treasureImage = ImageIO.read(treasureFile);
                if (treasureImage != null) {
                    System.out.println("Successfully loaded treasure.png");
                } else {
                    System.err.println("Failed to load treasure.png - image is null");
                }
            } else {
                System.err.println("treasure.png not found at: " + treasureFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
        
        // call randomize to generate maze + place door/key/traps 
        randomize();
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
        // always place enemy at bottom-right corner
        int rr = rows - 1;
        int cc = cols - 1;

        // making sure it's not a wall-locked cell (can move somewhere)
        Cell cell = maze.grid[rr][cc];
        if (cell.wall[0] && cell.wall[1] && cell.wall[2] && cell.wall[3]) {
            cell.wall[0] = false; // if all four walls are closed, open one for movement
        }

        // create the enemy at that location
        enemy = new Enemy(rr, cc);
    }

    // get accessible neighbors of a cell (N,E,S,W) — returns list of [r,c]
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
     * Handles enemy movement with intelligent pathfinding and anti-stuck mechanisms.
     * The enemy uses a combination of optimal pathfinding and controlled randomness
     * to create challenging but beatable behavior.
     */
    void enemyMoveTick() {
        if (enemy == null) return;

        // Get current cell and check if enemy is stuck in a pattern
        Cell currentCell = maze.grid[enemy.r][enemy.c];
        boolean isStuck = enemy.isStuck(currentCell);

        // Collect all valid moves from current position
        List<Cell> possibleMoves = new ArrayList<>();
        
        // Check all four directions for valid moves (no walls)
        if (!currentCell.wall[0] && enemy.r > 0)           // Up: Check top wall and maze boundary
            possibleMoves.add(maze.grid[enemy.r - 1][enemy.c]);
        if (!currentCell.wall[1] && enemy.c < cols - 1)    // Right: Check right wall and maze boundary
            possibleMoves.add(maze.grid[enemy.r][enemy.c + 1]);
        if (!currentCell.wall[2] && enemy.r < rows - 1)    // Down: Check bottom wall and maze boundary
            possibleMoves.add(maze.grid[enemy.r + 1][enemy.c]);
        if (!currentCell.wall[3] && enemy.c > 0)           // Left: Check left wall and maze boundary
            possibleMoves.add(maze.grid[enemy.r][enemy.c - 1]);

        if (possibleMoves.isEmpty()) return;  // No valid moves available

        // Calculate optimal path to player using BFS
        List<Cell> bestPath = maze.findPath(enemy.r, enemy.c, playerRow, playerCol);
        if (bestPath.size() <= 1) return;  // No path to player exists

        // Prevent immediate backtracking by removing the last position
        if (enemy.lastPosition != null) {
            possibleMoves.remove(enemy.lastPosition);
        }

        // Create prioritized list of moves
        List<Cell> ratedMoves = new ArrayList<>();
        Cell bestMove = bestPath.get(1);  // Next cell in optimal path
        
        // Add optimal move if it's available
        if (possibleMoves.contains(bestMove)) {
            ratedMoves.add(bestMove);
        }
        
        // Find and add suboptimal moves that eventually lead to player
        for (Cell move : possibleMoves) {
            if (move != bestMove) {
                List<Cell> altPath = maze.findPath(move.r, move.c, playerRow, playerCol);
                if (!altPath.isEmpty()) {
                    ratedMoves.add(move);  // Move leads to player, but not optimally
                }
            }
        }
        
        // Add remaining moves as worst options
        for (Cell move : possibleMoves) {
            if (!ratedMoves.contains(move)) {
                ratedMoves.add(move);  // Moves that don't lead to player or are blocked
            }
        }

        if (ratedMoves.isEmpty()) return;  // No valid rated moves available

        // Choose next move based on situation and randomness
        Cell chosenMove;
        double chance = enemyRand.nextDouble();
        
        if (isStuck) {
            // Stuck behavior: Increase randomness to break out of patterns
            if (chance < 0.4) {
                // 40% chance: Make random move to break pattern
                chosenMove = ratedMoves.get(enemyRand.nextInt(ratedMoves.size()));
            } else {
                // 60% chance: Try to move toward player
                chosenMove = ratedMoves.get(0);
            }
        } else {
            // Normal behavior: Weighted random choice
            if (chance < 0.75) {
                // 75% chance: Make optimal move toward player
                chosenMove = ratedMoves.get(0);
            } else if (chance < 0.9 && ratedMoves.size() > 1) {
                // 15% chance: Make suboptimal move
                int midIndex = Math.min(1, ratedMoves.size() - 1);
                chosenMove = ratedMoves.get(midIndex);
            } else {
                // 10% chance: Make worst available move
                chosenMove = ratedMoves.get(ratedMoves.size() - 1);
            }
        }

        // Execute the chosen move
        enemy.updateRotation(chosenMove.r, chosenMove.c);  // Update sprite rotation
        enemy.r = chosenMove.r;                           // Update position
        enemy.c = chosenMove.c;

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
                // Reset player to start
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

    // regenerate with a fresh RNG and ensure door/path/traps per your rules
    void randomize() {
        this.maze = new Maze(rows, cols);
        this.maze.generate();

        // pick a random door location 
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

        // place traps/treasures OFF the chosen safe path(THIS PART WILL BE CHANGED AS CURSES / REWARDS GONNA BE ADDED SEPERATELY LATER)
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

        // allow exactly one trap on the path to make it non-obvious (if path large enough)
        if (path.size() > 2) {
            List<Cell> innerPath = new ArrayList<>(path);
            // remove start and goal from candidates
            innerPath.remove(0);
            innerPath.remove(innerPath.size()-1);
            if (!innerPath.isEmpty()) {
                Cell chosen = innerPath.get(r.nextInt(innerPath.size()));
                chosen.trap = true;
            }
        }
        //TILL HERE (aagr nhi pta kia baat kr rahe hoon to comments parhte aooo 😐)

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
        // Update player rotation based on input direction, before movement checks
        if (dr == -1) playerRotation = 0;        // facing up (rotated 90° from original)
        else if (dr == 1) playerRotation = 180;  // facing down (rotated 90° from original)
        else if (dc == -1) playerRotation = 270; // facing left (rotated 90° from original)
        else if (dc == 1) playerRotation = 90;   // facing right (rotated 90° from original)
        
        // Always redraw to show new rotation, even if we can't move
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
        
        // Update animation state - only when actually moving
        isWalking = true;
        useFirstFrame = !useFirstFrame; // Switch animation frame
        
        // Schedule a repaint to show the walking animation
        repaint();
        
        // Start walking animation
        isWalking = true;
        repaint();
        
        // Use a Timer to animate the walking frames
        javax.swing.Timer walkTimer = new javax.swing.Timer(200, e -> { // 200ms between frame switches
            useFirstFrame = !useFirstFrame;
            repaint();
        });
        walkTimer.start();
        
        // Use another Timer to stop the animation after a longer duration
        javax.swing.Timer stopTimer = new javax.swing.Timer(400, e -> { // 400ms total animation time
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
                JOptionPane.showMessageDialog(this, "Game Over! Out of lives.");
                int opt = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    randomize();
                }
                return;
            }
            JOptionPane.showMessageDialog(this, "You hit a trap! Life lost.");
        } else if (next.treasure) {
            score += 10;
            next.treasure = false;
            JOptionPane.showMessageDialog(this, "You found a treasure! +10 points!");
        }
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
    // Game-over behavior temporarily disabled: show dialog but do not set game state
    int opt = JOptionPane.showConfirmDialog(
        this,
        "You escaped! Final Score: " + score + "\nPlay again?",
        "You Win!",
        JOptionPane.YES_NO_OPTION
    );

    if (opt == JOptionPane.YES_OPTION) {
        randomize();                  // restart the game immediately
    }

    return; // end this move
}
    // check win/loss
        //if (lives <= 0) { // Lives functionality temporarily disabled
        //    JOptionPane.showMessageDialog(this, "Game Over! Out of lives.");
        //}
    // gameOver check temporarily disabled

        repaint();
    }

 /**
  * Main rendering method that draws the entire game state.
  * Handles all sprite rendering, maze drawing, and HUD display.
  * Uses Graphics2D for advanced rendering features like rotation and alpha compositing.
  *
  * Rendering order:
  * 1. Calculate dimensions and scaling
  * 2. Draw maze walls and cells
  * 3. Draw traps and treasures
  * 4. Draw door (if key collected)
  * 5. Draw key (if not collected)
  * 6. Draw player with animation
  * 7. Draw enemy
  * 8. Draw HUD overlay
  *
  *  g The Graphics context provided by Swing
  */
 @Override protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // Calculate drawing dimensions
    int w = getWidth() - 2 * margin;        // Available width minus margins
    int h = getHeight() - 2 * margin - 30;  // Available height minus margins and HUD space
    // Calculate cell size to fit maze while maintaining aspect ratio
    int cellSize = Math.max(6, Math.min(w / Math.max(1, cols), h / Math.max(1, rows)));

    // Create Graphics2D for advanced rendering
    Graphics2D g2 = (Graphics2D) g.create();
    // Enable anti-aliasing for smoother rendering
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // origin for maze drawing
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
                    // Draw trap image scaled to cell size
                    int imgSize = (int)(cellSize * 0.9); // Make image 90% of cell size
                    int imgX = x + (cellSize - imgSize) / 2;
                    int imgY = y + (cellSize - imgSize) / 2;
                    g2.drawImage(trapImage, imgX, imgY, imgSize, imgSize, null);
                } else {
                    // Fallback to red box if image loading failed
                    g2.setColor(new Color(255, 0, 0, 150));
                    g2.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
                    g2.setColor(Color.WHITE);
                }
            } else if (cell.treasure) {
                if (treasureImage != null) {
                    // Draw treasure chest image scaled to cell size
                    int imgSize = (int)(cellSize * 0.9); // Make image 90% of cell size
                    int imgX = x + (cellSize - imgSize) / 2;
                    int imgY = y + (cellSize - imgSize) / 2;
                    g2.drawImage(treasureImage, imgX, imgY, imgSize, imgSize, null);
                } else {
                    // Fallback to gold box if image loading failed
                    g2.setColor(new Color(255, 215, 0, 150));
                    g2.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
                    g2.setColor(Color.WHITE);
                }
            }
        }
    }

   
    /**
     * Draw the exit door sprite
     * Only visible after player collects the key
     * Uses sprite scaling and centering for consistent appearance
     */
    if (hasKey && doorRow >= 0 && doorCol >= 0) {
        // Calculate door position in pixels
        int dx = offsetX + doorCol * cellSize;
        int dy = offsetY + doorRow * cellSize;
        
        if (doorImage != null) {
            // Scale door image to 90% of cell size for better visibility
            int imgSize = (int)(cellSize * 0.9);
            // Center the image within the cell
            int imgX = dx + (cellSize - imgSize) / 2;
            int imgY = dy + (cellSize - imgSize) / 2;
            
            // Draw door sprite without background effects
            g2.drawImage(doorImage, imgX, imgY, imgSize, imgSize, null);
        } else {
            // Fallback rendering if image loading failed
            g2.setColor(new Color(218, 165, 32)); // Golden color
            g2.fillRect(dx + cellSize/4, dy + cellSize/4, cellSize/2, cellSize/2);
            g2.setColor(Color.WHITE);
        }
    }

    // draw key (visible from the start)
    if (keyRow >= 0 && keyCol >= 0) {
        int kx = offsetX + keyCol * cellSize;
        int ky = offsetY + keyRow * cellSize;
        if (keyImage != null) {
            // Draw the key image scaled larger for better visibility
            int imgSize = (int)(cellSize * 0.9); // Make image large (90% of cell)
            int imgX = kx + (cellSize - imgSize) / 2;
            int imgY = ky + (cellSize - imgSize) / 2;
            
            // Draw image without background effects
            g2.drawImage(keyImage, imgX, imgY, imgSize, imgSize, null);
        } else {
            // Fallback to colored oval if image loading failed
            g2.setColor(new Color(255, 215, 0)); // Bright gold color
            g2.fillOval(kx + cellSize/4, ky + cellSize/4, cellSize/2, cellSize/2);
            g2.setColor(Color.WHITE);
        }
    }

    /**
     * Draw the player character with animation and rotation
     * Handles:
     * - Sprite selection (idle vs walking animation)
     * - Rotation based on movement direction
     * - Proper scaling and centering
     * - Transform management for rotation
     */
    int px = offsetX + playerCol * cellSize;  // Player X position in pixels
    int py = offsetY + playerRow * cellSize;  // Player Y position in pixels
    
    if (playerIdle != null && playerMove1 != null && playerMove2 != null) {
        // Calculate consistent sprite size (90% of cell)
        int imgSize = (int)(cellSize * 0.9);
        // Center position calculations
        int imgX = px + (cellSize - imgSize) / 2;
        int imgY = py + (cellSize - imgSize) / 2;
        
        // Store current graphics transform for restoration later
        AffineTransform oldTransform = g2.getTransform();
        
        // Setup rotation transform:
        // 1. Move to sprite center
        g2.translate(imgX + imgSize/2, imgY + imgSize/2);
        // 2. Apply rotation around center point
        g2.rotate(Math.toRadians(playerRotation));
        
        // Select appropriate animation frame
        Image currentFrame;
        if (isWalking) {
            // Alternate between walk frames during movement
            currentFrame = useFirstFrame ? playerMove1 : playerMove2;
        } else {
            // Use standing still frame when not moving
            currentFrame = playerIdle;
        }
        
        // Draw sprite centered at origin (due to transform)
        // Offset by half size to maintain center point
        g2.drawImage(currentFrame, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
        
        // Restore original transform to prevent affecting other rendering
        g2.setTransform(oldTransform);
    } else {
        // Fallback to cyan circle if image loading failed
        g2.setColor(new Color(0, 255, 255));
        g2.fillOval(px + cellSize/4, py + cellSize/4, cellSize/2, cellSize/2);
    }

    // Draw enemy
    if (enemy != null) {
        int ex = offsetX + enemy.c * cellSize;
        int ey = offsetY + enemy.r * cellSize;
        if (enemyImage != null) {
            // Draw the enemy image scaled to cell size
            int imgSize = (int)(cellSize * 0.9); // Make image 90% of cell size
            int imgX = ex + (cellSize - imgSize) / 2;
            int imgY = ey + (cellSize - imgSize) / 2;
            
            // Save the current transform
            AffineTransform oldTransform = g2.getTransform();
            
            // Translate to the center of where we want to draw the enemy
            g2.translate(imgX + imgSize/2, imgY + imgSize/2);
            
            // Rotate around the center
            g2.rotate(Math.toRadians(enemy.rotation));
            
            // Draw the enemy image centered at origin
            g2.drawImage(enemyImage, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
            
            // Restore the original transform
            g2.setTransform(oldTransform);
        } else {
            // Fallback to red circle if image loading failed
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
    String hud = String.format("Score: %d   Lives: %d", score, lives);
    FontMetrics fm = g2.getFontMetrics();

    int textWidth = fm.stringWidth(hud);
    g2.drawString(hud, (getWidth() - textWidth) / 2, getHeight() - 10);

    g2.dispose();
}
}








