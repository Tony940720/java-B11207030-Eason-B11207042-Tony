import java.awt.event.*;

public class InputHandler extends KeyAdapter {
    private GameBoard board;

    public InputHandler(GameBoard board) {
        this.board = board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (board.isGameOver()) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> board.moveBlock(-1, 0);
            case KeyEvent.VK_RIGHT -> board.moveBlock(1, 0);
            case KeyEvent.VK_DOWN -> board.rotateBlockBack();
            case KeyEvent.VK_UP -> board.rotateBlock();
            case KeyEvent.VK_SPACE -> {
            // 快速下落到底
                board.dropBlock();
                break;
            }    
        }   
    }
}
