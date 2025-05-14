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
            case KeyEvent.VK_A -> board.moveBlock(-1, 0);
            case KeyEvent.VK_D -> board.moveBlock(1, 0);
            case KeyEvent.VK_S -> board.moveBlock(0, 1);
            case KeyEvent.VK_Q -> board.rotateBlockBack();
            case KeyEvent.VK_E -> board.rotateBlock();
            case KeyEvent.VK_W -> board.holdCurrentBlock();
            case KeyEvent.VK_SPACE -> {
            // 快速下落到底
                board.dropBlock();
                break;
            }    
        }   
    }
}
