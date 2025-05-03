import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameBoard board;

    public GamePanel() {
        this.setFocusable(true);
        this.board = new GameBoard();
        this.addKeyListener(new InputHandler(board));
    }

    public void startGame() {
        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            board.update();
            repaint();
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.draw(g);
    }
}
