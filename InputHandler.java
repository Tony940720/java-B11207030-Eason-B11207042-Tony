import java.awt.event.*;

public class InputHandler implements KeyListener {
    private final GamePanel gamePanel;

    public InputHandler(GamePanel panel) {
        this.gamePanel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (gamePanel.getGameState()) {
            case MENU -> handleMenuInput(e);
            case PLAYING -> handlePlayingInput(e);
            case PAUSED -> handlePauseInput(e);
            case GAME_OVER -> handleGameOverInput(e);
        }
    }

    private void handleMenuInput(KeyEvent e) {
        int selected = gamePanel.getSelectedOption();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> gamePanel.setSelectedOption((selected + 3) % 4);
            case KeyEvent.VK_DOWN -> gamePanel.setSelectedOption((selected + 1) % 4);
            case KeyEvent.VK_LEFT -> {
                switch (selected) {
                    case 0 -> {
                        int v = Math.max(0, gamePanel.getVolumePercent() - 10);
                        gamePanel.setVolumePercent(v);
                    }
                    case 1 -> gamePanel.setSpeedLevel(Math.max(0, gamePanel.getSpeedLevel() - 1));
                    case 2 -> gamePanel.setMode(gamePanel.getMode().equals("Normal") ? "Challenge" : "Normal");
                }
            }
            case KeyEvent.VK_RIGHT -> {
                switch (selected) {
                    case 0 -> {
                        int v = Math.min(100, gamePanel.getVolumePercent() + 10);
                        gamePanel.setVolumePercent(v);
                        SoundPlayer.setGlobalVolume(v);
                    }
                    case 1 -> gamePanel.setSpeedLevel(Math.min(2, gamePanel.getSpeedLevel() + 1));
                    case 2 -> gamePanel.setMode(gamePanel.getMode().equals("Normal") ? "Challenge" : "Normal");
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (selected == 3) {
                    gamePanel.resetGameBoard();
                    gamePanel.setGameState(GamePanel.GameState.PLAYING);
                }
            }
        }
    }

    private void handlePlayingInput(KeyEvent e) {
        GameBoard board = gamePanel.getBoard();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P -> {
                gamePanel.setGameState(GamePanel.GameState.PAUSED);
            }
            case KeyEvent.VK_A -> board.moveBlock(-1, 0);
            case KeyEvent.VK_D -> board.moveBlock(1, 0);
            case KeyEvent.VK_S -> board.moveBlock(0, 1);
            case KeyEvent.VK_W -> board.rotateBlock();
            case KeyEvent.VK_Q -> board.rotateBlockBack();
            case KeyEvent.VK_SPACE -> board.dropBlock();
            case KeyEvent.VK_E -> board.holdCurrentBlock();
        }
    }

    private void handlePauseInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gamePanel.setGameState(GamePanel.GameState.PLAYING);
        }
    }

    private void handleGameOverInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R -> {
                gamePanel.resetGameBoard();
                gamePanel.setGameState(GamePanel.GameState.PLAYING);
                gamePanel.playBackgroundMusic();
            }
            case KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE -> {
                gamePanel.setGameState(GamePanel.GameState.MENU);
                gamePanel.playBackgroundMusic();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
