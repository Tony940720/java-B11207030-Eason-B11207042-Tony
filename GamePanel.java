import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int BLOCK_SIZE = 28;
    
    private GameBoard board;
    private javax.swing.Timer gravityTimer;
    private SoundPlayer backgroundMusic;
    private boolean gameOverSoundPlayed = false;

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard();
        this.addKeyListener(new InputHandler(board));
    }

    public void startGame() {
        backgroundMusic = new SoundPlayer("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/tetris_theme.wav");
        backgroundMusic.start();
        // 控制畫面刷新速度（每 1 毫秒）
        javax.swing.Timer repaintTimer = new javax.swing.Timer(1, e -> {
            repaint();
            if (board.isGameOver()) {
                if (!gameOverSoundPlayed) {
                    stopBackgroundMusic(); // 停止背景音樂
                    SoundPlayer.playSoundOnce("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/game-over.wav"); // 播放結束音效
                    gameOverSoundPlayed = true;
                } 
            }
        });
        repaintTimer.start();
    
        // 控制方塊下落速度（每 500 毫秒）
        gravityTimer = new javax.swing.Timer(500, e -> {
            board.update(); 
            adjustSpeed(); 
        });
        gravityTimer.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.draw(g);
        //score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + board.getScore(), 320, 25);
        //GameOver
        if (board.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER",321, 50);
        }
        //nextblock
        Block next = board.getNextBlock();
        if (next != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Next:", 330, 80);  

            
            for (Point p : next.getAbsolutePoints()) {
                // 偏移：將原始 x 向右、y 向下，畫在固定位置（靠右側）
                int drawX = (p.x - 3) * CELL_SIZE + 330;  // x 移到右側顯示區域
                int drawY = (p.y + 3) * CELL_SIZE;        // y 往下再移一格
                g.setColor(getBlockColor(next));
                g.fillRect(drawX, drawY, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, BLOCK_SIZE, BLOCK_SIZE);
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
}
