import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameBoard board;
    private javax.swing.Timer gravityTimer;

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard();
        this.addKeyListener(new InputHandler(board));
    }

    public void startGame() {
        // 控制畫面刷新速度（每 16 毫秒）
        javax.swing.Timer repaintTimer = new javax.swing.Timer(16, e -> {
            repaint(); 
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

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + board.getScore(), 320, 25);
        //GameOver
        if (board.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER",321, 50);
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
    
}
