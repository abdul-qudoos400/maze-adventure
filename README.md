# 🎮 Maze Adventure - Java Swing Game

A stunning, interactive maze game built with Java Swing featuring beautiful UI, database integration, enemy AI, and multiple game mechanics.

## ✨ Features

### 🎨 Beautiful UI
- Animated login screen with particle effects
- Glowing buttons with hover animations
- 3D depth effects and smooth transitions
- Main menu with floating hexagonal orbs
- Dynamic gradient backgrounds
- Leaderboard with twinkling stars

### 🎯 Gameplay
- Procedurally generated mazes using DFS (Depth-First Search) algorithm
- Player movement with 4-directional controls (WASD or Arrow Keys)
- Intelligent enemy AI with pathfinding (BFS)
- Multiple game mechanics:
  - **Keys**: Collect keys to unlock the exit
  - **Traps**: Avoid hazards or lose lives
  - **Treasures**: Find hidden treasures for bonus points
- Lives system (3 lives per game)
- Score tracking and leaderboard

### 🤖 Enemy AI
- Uses BFS (Breadth-First Search) pathfinding to track player
- Anti-stuck mechanism to detect and escape dead ends
- Weighted decision-making for challenging but fair gameplay
- Sprite rotation to face movement direction

### 💾 Database Integration
- MySQL integration for user accounts
- User registration and login system
- Score tracking and statistics
- Persistent leaderboard
- Games played counter

### 🎮 Game Controls
- **WASD / Arrow Keys**: Move player
- **R**: Generate new maze
- **ESC**: Pause menu (Resume/Main Menu/Logout)

## 📋 Requirements

### Software
- Java 11 or higher
- MySQL Server 5.7+
- MySQL JDBC Driver

### Java Packages (All included in standard Java library)
- `javax.swing.*` - GUI components
- `java.awt.*` - Graphics rendering
- `java.sql.*` - Database connectivity
- `java.util.*` - Data structures

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/abdul-qudoos400/maze-adventure.git
cd maze-adventure
```

### 2. Set Up Database

Create MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS project3;
USE project3;

CREATE TABLE IF NOT EXISTS game (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user VARCHAR(50) NOT NULL UNIQUE,
  scores INT DEFAULT 0,
  number_of_games_played INT DEFAULT 0
);

-- Insert test data (optional)
INSERT INTO game (user, scores, number_of_games_played) VALUES 
('yusra', 0, 0),
('mama', 0, 0),
('sadia', 0, 0);
```

### 3. Update Database Credentials

Edit `Game.java` and update the Connectivity class:
```java
private static final String URL = "jdbc:mysql://localhost:3306/project3";
private static final String USER = "root";
private static final String PASSWORD = "your_password";  // Change this!
```

### 4. Compile
```bash
javac Game.java
javac Maze.java
javac GameLogic.java
```

### 5. Run
```bash
java Game
```

## 📁 Project Structure

```
maze-adventure/
├── Game.java                 # Main entry point, enhanced UI, database integration
├── Maze.java                 # Original maze implementation and login system
├── GameLogic.java            # Game level logic
├── DATABASE_SETUP.md         # Database setup guide
├── project3.sql              # Database dump
├── Images/
│   ├── player_idel.png       # Player standing sprite
│   ├── player_move1.png      # Player walking frame 1
│   ├── player_move2.png      # Player walking frame 2
│   ├── enemy.png             # Enemy sprite
│   ├── key2.png              # Key collectible
│   ├── door.png              # Exit door
│   ├── trap.png              # Hazard sprite
│   ├── treasure.png          # Treasure sprite
│   ├── yusra1.jpeg           # Login background
│   └── yusra2.jpeg           # Menu background
└── Compiled Classes (auto-generated)
```

## 🎮 How to Play

### Login/Registration
1. Enter your username
2. Click **LOGIN** if you have an existing account
3. Click **REGISTER** to create a new account

### Main Menu
- **START GAME** - Begin a new maze game
- **LEADERBOARD** - View top players and scores
- **LOGOUT** - Return to login screen

### During Gameplay
1. Use **WASD** or **Arrow Keys** to move
2. **Collect the key** visible on the maze
3. **Unlock the door** at the exit (only possible with the key)
4. **Avoid enemies** - they patrol the maze
5. **Avoid traps** - you have 3 lives
6. **Collect treasures** - earn bonus points
7. **Reach the exit** with the key to win!

Press **ESC** to open the pause menu.

## 🎨 Maze Generation Algorithm

Uses **Depth-First Search (DFS)** backtracking:
1. Start at top-left (0,0)
2. Mark as visited
3. Randomly choose unvisited neighbors
4. Carve walls between current and next cell
5. Move to next cell
6. When stuck, backtrack
7. Continue until all cells visited

Result: A perfect maze with exactly one solution path!

## 🤖 Enemy AI Algorithm

Uses **Breadth-First Search (BFS)** pathfinding:
1. Find shortest path from enemy to player
2. 75% chance: Move along optimal path
3. 15% chance: Take suboptimal but valid path
4. 10% chance: Take worst available move
5. If stuck in small area for 3+ moves, increase randomness

Creates challenging but fair gameplay!

## 📊 Database Schema

```sql
CREATE TABLE game (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user VARCHAR(50) NOT NULL UNIQUE,
  scores INT DEFAULT 0,
  number_of_games_played INT DEFAULT 0
);
```

## 🐛 Troubleshooting

### "Database connection failed"
- Ensure MySQL is running: `mysql -u root -p`
- Check credentials in Connectivity class
- Verify database 'project3' exists
- See DATABASE_SETUP.md for detailed instructions

### "Cannot find images"
- Place image files in the same directory as .class files
- Alternatively, update file paths in code

### "Driver not found"
- Download MySQL JDBC driver from: https://dev.mysql.com/downloads/connector/j/
- Add to Java classpath

### Game window appears blank
- Ensure your screen resolution is at least 1024x768
- Try resizing the window

## 👨‍💻 Author

Abdul Qudoos
- GitHub: [@abdul-qudoos400](https://github.com/abdul-qudoos400)

## 📝 License

This project is open source and available under the MIT License.

## 🎓 Learning Resources

This project demonstrates:
- Object-Oriented Programming (OOP) in Java
- GUI Development with Swing
- Database connectivity with JDBC
- Pathfinding algorithms (BFS, DFS)
- Game loop and animation
- Collision detection
- State management
- Event handling

## 🤝 Contributing

Feel free to fork and submit pull requests for any improvements!

### Possible Improvements
- [ ] Difficulty levels (easy, medium, hard)
- [ ] Power-ups system
- [ ] Sound effects and background music
- [ ] Multiple enemy types
- [ ] Time-based challenges
- [ ] Bonus levels
- [ ] Achievements system
- [ ] Mobile version

## ⭐ Show Your Support

If you like this project, please give it a star! ⭐

---

**Enjoy the game! 🎮🎉**
