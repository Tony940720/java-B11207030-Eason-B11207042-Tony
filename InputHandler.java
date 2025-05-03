import java.awt.event.*;

public class InputHandler extends KeyAdapter {
    private GameBoard board;

    public InputHandler(GameBoard board) {
        this.board = board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> board.moveBlock(-1, 0);
            case KeyEvent.VK_RIGHT -> board.moveBlock(1, 0);
            case KeyEvent.VK_DOWN -> board.moveBlock(0, 1);
            case KeyEvent.VK_UP -> board.rotateBlock();
        }
    }
}
