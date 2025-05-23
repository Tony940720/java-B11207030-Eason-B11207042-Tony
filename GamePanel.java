import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;


import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GamePanel extends JPanel{ 
    private static final int CELL_SIZE = 30;
    private static final int BLOCK_SIZE = 28;

    public enum GameState { MENU, PLAYING, PAUSED, GAME_OVER, HIGHSCORES }
    private GameState gameState = GameState.MENU;

    private GameBoard board;
    private InputHandler inputHandler;
    private javax.swing.Timer gravityTimer;
    SoundPlayer backgroundMusic;
    private boolean gameOverSoundPlayed = false;
    private long lastRowInsertTime = System.currentTimeMillis();

    // 設定選單選項
    private int selectedOption = 0;
    private int volumePercent = 100;
    private int speedLevel = 1; // 0: 慢, 1: 中, 2: 快
    private final int[] normalDelays = {500, 400, 300, 200, 100};
    private String mode = "Normal";

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard(mode);  
        this.inputHandler = new InputHandler(this);
        this.addKeyListener(inputHandler);
        SoundPlayer.setGlobalVolume(volumePercent);
    }

    public void startGame() {
        backgroundMusic = new SoundPlayer("java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav", volumePercent);

        javax.swing.Timer repaintTimer = new javax.swing.Timer(1, e -> repaint());
        repaintTimer.start();

        gravityTimer = new javax.swing.Timer(getAdjustedDelay(0), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameState.PLAYING) {
                    board.update();

                    if (mode.equals("Challenge")) {
                        long now = System.currentTimeMillis();
                        if (now - lastRowInsertTime >= 10000) {
                            board.insertBottomRow();
                            lastRowInsertTime = now;
                        }
                    }

                    if (board.isGameOver()) {
                        String name = promptForName();
                        HighScoreManager.addScore(name, board.getScore());
                        gameState = GameState.GAME_OVER;
                        stopBackgroundMusic();
                        if (!gameOverSoundPlayed) {
                            SoundPlayer.playSoundOnce("java-B11207030-Eason-B11207042-Tony/music/game-over.wav");
                            gameOverSoundPlayed = true;
                        }
                    }
                    adjustSpeed();
                }
            }
        });
        gravityTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (gameState) {
            case MENU -> drawMenu(g);
            case PLAYING, PAUSED -> drawGame(g);
            case GAME_OVER -> drawGameOver(g);
            case HIGHSCORES -> drawHighScores(g);
        }
        // 額外處理暫停畫面
        if (gameState == GameState.PAUSED) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(0, 0, 0, 150)); // 半透明黑色遮罩
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.drawString("PAUSED", getWidth() / 2 - 80, getHeight() / 2);
            g2d.dispose();

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press R to Restart", 140, 450);
            g.drawString("Press ESC to Menu", 133, 510);
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("TETRIS", 150, 150);

        g.setFont(new Font("Arial", Font.PLAIN, 24));

        String[] options = {
            "  High Scores",
            "Volume: " + volumePercent + "%",
            "Speed: " + (speedLevel == 0 ? "Slow" : speedLevel == 1 ? "Normal" : "Fast"),
            "Mode: " + mode,
            "  Start Game"
        };

        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) g.setColor(Color.YELLOW);
            else g.setColor(Color.WHITE);
            g.drawString(options[i], 150, 250 + i * 40);
        }
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 150, 250);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press R to Restart", 150, 310);
        g.drawString("Press ESC to Menu", 143, 370);
    }

    private void drawGame(Graphics g) {
        board.draw(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + board.getScore(), 320, 25);

        Block next = board.getNextBlock();
        if (next != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Next:", 330, 80);
            int minX = 0, minY = 0;
            for (Point p : next.getShape()) {
                if (p.x < minX) minX = p.x;
                if (p.y < minY) minY = p.y;
            }
            int offsetX = -minX * CELL_SIZE + 330;
            int offsetY = -minY * CELL_SIZE + 100;
            for (Point p : next.getShape()) {
                g.setColor(getBlockColor(next));
                g.fillRect(offsetX + p.x * CELL_SIZE, offsetY + p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(offsetX + p.x * CELL_SIZE, offsetY + p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }

        Block holdBlock = board.getHoldBlock();
        if (holdBlock != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Hold:", 330, 180);
            int minX = 0, minY = 0;
            for (Point p : holdBlock.getShape()) {
                if (p.x < minX) minX = p.x;
                if (p.y < minY) minY = p.y;
            }
            int offsetX = -minX * CELL_SIZE + 330;
            int offsetY = -minY * CELL_SIZE + 200;
            for (Point p : holdBlock.getShape()) {
                g.setColor(getBlockColor(holdBlock));
                g.fillRect(offsetX + p.x * CELL_SIZE, offsetY + p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(offsetX + p.x * CELL_SIZE, offsetY + p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    private void drawHighScores(Graphics g) {
        g.setColor(new Color(30, 30, 47));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 32));
        g.drawString("High Scores", 140, 100);

        List<HighScoreManager.ScoreEntry> scores = HighScoreManager.loadScores();
        g.setFont(new Font("Consolas", Font.PLAIN, 22));

        for (int i = 0; i < scores.size(); i++) {
            HighScoreManager.ScoreEntry entry = scores.get(i);
            String line = String.format("%2d. %-10s %6d", i + 1, entry.name, entry.score);

            // 設定前三名顏色
            if (i == 0) g.setColor(new Color(255, 215, 0));      // 金
            else if (i == 1) g.setColor(new Color(192, 192, 192)); // 銀
            else if (i == 2) g.setColor(new Color(205, 127, 50));  // 銅
            else g.setColor(Color.GRAY);

            g.drawString(line, 100, 150 + i * 30);
        }
    }
    
    private Color getBlockColor(Block block) {
        if (block instanceof IBlock) return Color.CYAN;
        if (block instanceof JBlock) return Color.BLUE;
        if (block instanceof LBlock) return Color.ORANGE;
        if (block instanceof OBlock) return Color.YELLOW;
        if (block instanceof SBlock) return Color.GREEN;
        if (block instanceof TBlock) return Color.MAGENTA;
        if (block instanceof ZBlock) return Color.RED;
        return Color.WHITE;
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    private int getAdjustedDelay(int clearline) {
        int index;
        if (clearline < 6) index = 0;
        else if (clearline < 12) index = 1;
        else if (clearline < 20) index = 2;
        else if (clearline < 30) index = 3;
        else index = 4;

        double multiplier = switch (speedLevel) {
            case 0 -> 1.0 * 2;      // Slow
            case 1 -> 1.0 ;         // Normal
            case 2 -> 1.0 / 2;      // Fast (Normal 的 1.5 倍)
            default -> 1.0;
        };
        return (int) (normalDelays[index] * multiplier);
    }

    private void adjustSpeed() {
        int clearline = board.getclearline();
        int newDelay = getAdjustedDelay(clearline);

        if (gravityTimer.getDelay() != newDelay) {
            gravityTimer.setDelay(newDelay);
        }
    }

    public String promptForName() {
        JTextField textField = new JTextField();

        // 限制輸入長度為 5
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 5) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() - length + text.length() <= 5) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        int result = JOptionPane.showConfirmDialog(
            null,
            textField,
            "Game Over! Enter your name (max 5 letters):",
            JOptionPane.OK_CANCEL_OPTION
        );

        String input = textField.getText().trim();
        if (result == JOptionPane.OK_OPTION && !input.isEmpty()) {
            return input;
        } else {
            return "nickname"; // 預設名字
        }
    }
    
    // Getter methods for InputHandler
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState newState) { gameState = newState; }

    public int getSelectedOption() { return selectedOption; }
    public void setSelectedOption(int opt) { selectedOption = opt; }

    public int getVolumePercent() { return volumePercent; }
    public void setVolumePercent(int v) { 
        volumePercent = v; 
        SoundPlayer.setGlobalVolume(volumePercent);
        if (backgroundMusic != null) backgroundMusic.setVolume(volumePercent);
    }

    public int getSpeedLevel() { return speedLevel; }
    public void setSpeedLevel(int level) { speedLevel = level; }

    public String getMode() { return mode; }
    public void setMode(String m) { mode = m; }

    public void resetGameBoard() { board = new GameBoard(mode); gameOverSoundPlayed = false; }

    public void playBackgroundMusic() {
        backgroundMusic = new SoundPlayer("java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav", volumePercent);
    }

    public GameBoard getBoard() { return board; }

    public void setlastRowInsertTime(long t){
        lastRowInsertTime = t;
    }

}
