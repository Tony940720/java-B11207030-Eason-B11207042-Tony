import java.awt.*;
import java.util.Random;

public class GameBoard {
    private int[][] grid = new int[20][10];
    private Block currentBlock;
    private Random random = new Random();
    private int score = 0;

    public GameBoard() {
        spawnNewBlock();
    }

    public void update() {
        currentBlock.move(0, 1);
        if (!isValidPosition(currentBlock)) {
            currentBlock.move(0, -1);
            placeBlock(currentBlock);
            spawnNewBlock();
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x <= 10; x++) {
            g.drawLine(x * 30, 0, x * 30, 600);
        }
        for (int y = 0; y <= 20; y++) {
            g.drawLine(0, y * 30, 300, y * 30);
        }
        // Draw fixed blocks
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                if (grid[y][x] != 0) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x * 30, y * 30, 28, 28);
                }
            }
        }
        // Draw current block
        g.setColor(Color.RED);
        for (Point p : currentBlock.getAbsolutePoints()) {
            g.fillRect(p.x * 30, p.y * 30, 28, 28);
        }
    }

    public void spawnNewBlock() {
        int type = random.nextInt(7);
        switch (type) {
            case 0: currentBlock = new IBlock(); break;
            case 1: currentBlock = new JBlock(); break;
            case 2: currentBlock = new LBlock(); break;
            case 3: currentBlock = new OBlock(); break;
            case 4: currentBlock = new SBlock(); break;
            case 5: currentBlock = new TBlock(); break;
            case 6: currentBlock = new ZBlock(); break;
        }
        
    }

    public boolean isValidPosition(Block block) {
        for (Point p : block.getAbsolutePoints()) {
            if (p.x < 0 || p.x >= 10 || p.y < 0 || p.y >= 20) return false;
            if (grid[p.y][p.x] != 0) return false;
        }
        return true;
    }

    public void placeBlock(Block block) {
        for (Point p : block.getAbsolutePoints()) {
            grid[p.y][p.x] = 1;
        }
        clearFullLines();
    }

    public void moveBlock(int dx, int dy) {
        currentBlock.move(dx, dy);
        if (!isValidPosition(currentBlock)) {
            currentBlock.move(-dx, -dy);
        }
    }

    public void rotateBlock() {
        currentBlock.rotate();
        if (!isValidPosition(currentBlock)) {
            // basic wall kick not implemented
            currentBlock.rotateBack();
        }
    }

    private void clearFullLines() {
        int linesCleared = 0;
        for (int y = 0; y < 20; y++) {
            boolean full = true;
            for (int x = 0; x < 10; x++) {
                if (grid[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                for (int ty = y; ty > 0; ty--) {
                    System.arraycopy(grid[ty - 1], 0, grid[ty], 0, 10);
                }
                grid[0] = new int[10]; // 最上層清空
            }
        }

        switch (linesCleared) {
            case 1 -> score += 100;
            case 2 -> score += 300;
            case 3 -> score += 500;
            case 4 -> score += 800;
        }
    }

    public int getScore() {
        return score;
    }
}
