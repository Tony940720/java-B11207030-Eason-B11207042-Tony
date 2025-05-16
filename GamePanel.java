import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener {
    private static final int CELL_SIZE = 30;
    private static final int BLOCK_SIZE = 28;

    private enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState gameState = GameState.MENU;

    private GameBoard board;
    private javax.swing.Timer gravityTimer;
    private SoundPlayer backgroundMusic;
    private boolean gameOverSoundPlayed = false;

    // 設定選單選項
    private int selectedOption = 0;
    private int volumePercent = 100;
    private int speedLevel = 1; // 0: 慢, 1: 中, 2: 快
    private String mode = "Normal";

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard(mode);
        this.addKeyListener(this);
        SoundPlayer.setGlobalVolume(volumePercent);
    }

    public void startGame() {
        backgroundMusic = new SoundPlayer("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav", volumePercent);

        javax.swing.Timer repaintTimer = new javax.swing.Timer(1, e -> repaint());
        repaintTimer.start();

        gravityTimer = new javax.swing.Timer(getInitialSpeed(), new ActionListener() {
            private long lastRowInsertTime = System.currentTimeMillis();

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
                        gameState = GameState.GAME_OVER;
                        stopBackgroundMusic();
                        if (!gameOverSoundPlayed) {
                            SoundPlayer.playSoundOnce("../java-B11207030-Eason-B11207042-Tony/music/game-over.wav");
                            gameOverSoundPlayed = true;
                        }
                    }
                    adjustSpeed();
                }
            }
        });
        gravityTimer.start();
    }

    private int getInitialSpeed() {
        return switch (speedLevel) {
            case 0 -> 600;
            case 1 -> 400;
            case 2 -> 200;
            default -> 400;
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (gameState) {
            case MENU -> drawMenu(g);
            case PLAYING -> drawGame(g);
            case GAME_OVER -> drawGameOver(g);
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

    private void adjustSpeed() {
        int score = board.getScore();
        int newDelay;
        if (score < 200) newDelay = 500;
        else if (score < 500) newDelay = 400;
        else if (score < 1000) newDelay = 300;
        else if (score < 1500) newDelay = 200;
        else newDelay = 100;

        if (gravityTimer.getDelay() != newDelay) {
            gravityTimer.setDelay(newDelay);
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

    @Override
    public void keyPressed(KeyEvent e) {
        switch (gameState) {
            case MENU -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> selectedOption = (selectedOption + 3) % 4;
                    case KeyEvent.VK_DOWN -> selectedOption = (selectedOption + 1) % 4;
                    case KeyEvent.VK_LEFT -> {
                        if (selectedOption == 0) {
                            volumePercent = Math.max(0, volumePercent - 10);
                            SoundPlayer.setGlobalVolume(volumePercent);
                            if (backgroundMusic != null) backgroundMusic.setVolume(volumePercent);
                        }
                        if (selectedOption == 1) speedLevel = Math.max(0, speedLevel - 1);
                        if (selectedOption == 2) mode = mode.equals("Normal") ? "Challenge" : "Normal";
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (selectedOption == 0) {
                            volumePercent = Math.min(100, volumePercent + 10);
                            SoundPlayer.setGlobalVolume(volumePercent);
                            if (backgroundMusic != null) backgroundMusic.setVolume(volumePercent);
                        }
                        if (selectedOption == 1) speedLevel = Math.min(2, speedLevel + 1);
                        if (selectedOption == 2) mode = mode.equals("Normal") ? "Challenge" : "Normal";
                    }
                    case KeyEvent.VK_ENTER -> {
                        if (selectedOption == 3) {
                            board = new GameBoard(mode);
                            gameOverSoundPlayed = false;
                            gameState = GameState.PLAYING;
                            backgroundMusic = new SoundPlayer("../java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav", volumePercent);
                        }
                    }
                }
            }
            case GAME_OVER -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R -> {
                        board = new GameBoard(mode);
                        gameOverSoundPlayed = false;
                        gameState = GameState.PLAYING;
                        backgroundMusic = new SoundPlayer("../java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav", volumePercent);
                    }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE -> {
                        gameState = GameState.MENU;
                    }
                }
            }
            case PLAYING -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> board.moveBlock(-1, 0);
                    case KeyEvent.VK_D -> board.moveBlock(1, 0);
                    case KeyEvent.VK_S -> board.moveBlock(0, 1);
                    case KeyEvent.VK_Q -> board.rotateBlock();
                    case KeyEvent.VK_E -> board.rotateBlockBack();
                    case KeyEvent.VK_SPACE -> board.dropBlock();
                    case KeyEvent.VK_W -> board.holdCurrentBlock();
                }
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
