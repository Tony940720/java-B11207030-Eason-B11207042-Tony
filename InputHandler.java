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
            case HIGHSCORES -> handleHighScoresInput(e);
        }
    }

    private void handleMenuInput(KeyEvent e) {
        int selected = gamePanel.getSelectedOption();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> gamePanel.setSelectedOption((selected + 4) % 5);
            case KeyEvent.VK_DOWN -> gamePanel.setSelectedOption((selected + 1) % 5);
            case KeyEvent.VK_LEFT -> {
                switch (selected) {
                    case 1 -> {
                        int v = Math.max(0, gamePanel.getVolumePercent() - 10);
                        gamePanel.setVolumePercent(v);
                    }
                    case 2 -> gamePanel.setSpeedLevel(Math.max(0, gamePanel.getSpeedLevel() - 1));
                    case 3 -> gamePanel.setMode(gamePanel.getMode().equals("Normal") ? "Challenge" : "Normal");
                }
            }
            case KeyEvent.VK_RIGHT -> {
                switch (selected) {
                    case 1 -> {
                        int v = Math.min(100, gamePanel.getVolumePercent() + 10);
                        gamePanel.setVolumePercent(v);
                        SoundPlayer.setGlobalVolume(v);
                    }
                    case 2 -> gamePanel.setSpeedLevel(Math.min(2, gamePanel.getSpeedLevel() + 1));
                    case 3 -> gamePanel.setMode(gamePanel.getMode().equals("Normal") ? "Challenge" : "Normal");
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (selected == 0) {
                    gamePanel.setGameState(GamePanel.GameState.HIGHSCORES);
                }
                else if (selected == 4) {
                    gamePanel.resetGameBoard();
                    gamePanel.setGameState(GamePanel.GameState.PLAYING);
                }
            }
            case KeyEvent.VK_H -> gamePanel.setGameState(GamePanel.GameState.HIGHSCORES);
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
            gamePanel.setlastRowInsertTime(System.currentTimeMillis());
        }
        else if (e.getKeyCode()== KeyEvent.VK_ESCAPE) {
            gamePanel.setGameState(GamePanel.GameState.MENU);
        }
        else if (e.getKeyCode()== KeyEvent.VK_R) {
            gamePanel.resetGameBoard();
            gamePanel.setGameState(GamePanel.GameState.PLAYING);
            gamePanel.playBackgroundMusic();
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

    private void handleHighScoresInput(KeyEvent e){
         if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gamePanel.setGameState(GamePanel.GameState.MENU);
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
