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

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard();
        this.addKeyListener(this);
    }

    public void startGame() {
        backgroundMusic = new SoundPlayer("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav");
        backgroundMusic.start();

        javax.swing.Timer repaintTimer = new javax.swing.Timer(1, e -> repaint());
        repaintTimer.start();

        gravityTimer = new javax.swing.Timer(500, e -> {
            if (gameState == GameState.PLAYING) {
                board.update();
                if (board.isGameOver()) {
                    gameState = GameState.GAME_OVER;
                    stopBackgroundMusic();
                    if (!gameOverSoundPlayed) {
                        SoundPlayer.playSoundOnce("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/game-over.wav");
                        gameOverSoundPlayed = true;
                    }
                }
                adjustSpeed();
            }
        });
        gravityTimer.start();
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
        g.drawString("TETRIS", 80, 200);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press ENTER to Start", 60, 300);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 80, 250);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press R to Restart", 70, 310);
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
        if (score < 200) {
            newDelay = 500;
        } else if (score < 500) {
            newDelay = 400;
        } else if (score < 1000) {
            newDelay = 300;
        } else if (score < 1500) {
            newDelay = 200;
        } else {
            newDelay = 100;
        }

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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    board = new GameBoard();
                    gameOverSoundPlayed = false;
                    gameState = GameState.PLAYING;
                }
            }
            case GAME_OVER -> {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    board = new GameBoard();
                    gameOverSoundPlayed = false;
                    gameState = GameState.PLAYING;
                    backgroundMusic = new SoundPlayer("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav");
                    backgroundMusic.start();
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