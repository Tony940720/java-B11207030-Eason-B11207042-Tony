import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel{ 
    private static final int CELL_SIZE = 30;
    private static final int BLOCK_SIZE = 28;

    public enum GameState { MENU, PLAYING, PAUSED, GAME_OVER }
    private GameState gameState = GameState.MENU;

    private GameBoard board;
    private InputHandler inputHandler;
    private javax.swing.Timer gravityTimer;
    SoundPlayer backgroundMusic;
    private boolean gameOverSoundPlayed = false;

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
            private long lastRowInsertTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameState.PLAYING) {
                    board.update();

                    if (mode.equals("Challenge")) {
                        long now = System.currentTimeMillis();
                        if (now - lastRowInsertTime >= 7000) {
                            board.insertBottomRow();
                            lastRowInsertTime = now;
                        }
                    }

                    if (board.isGameOver()) {
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
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("TETRIS", 80, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 24));

        String[] options = {
            "Volume: " + volumePercent + "%",
            "Speed: " + (speedLevel == 0 ? "Slow" : speedLevel == 1 ? "Normal" : "Fast"),
            "Mode: " + mode,
            "Start Game"
        };

        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) g.setColor(Color.YELLOW);
            else g.setColor(Color.WHITE);
            g.drawString(options[i], 60, 180 + i * 40);
        }
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 80, 250);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press R to Restart", 70, 310);
        g.drawString("Press ESC to Menu", 70, 370);
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

}
