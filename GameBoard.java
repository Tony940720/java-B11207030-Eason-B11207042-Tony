import java.awt.*;
import java.util.Random;

public class GameBoard {
    private static final int CELL_SIZE = 30;
    private static final int BLOCK_SIZE = 28;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    private int[][] grid = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private Block currentBlock;
    private Block nextBlock;
    private Random random = new Random();
    private int score = 0;
    private boolean gameOver = false;

    public GameBoard() {
        nextBlock = generateRandomBlock();
        spawnNewBlock();
    }

    public void update() {
        if (gameOver) return;
        currentBlock.move(0, 1);
        if (!isValidPosition(currentBlock)) {
            currentBlock.move(0, -1);
            placeBlock(currentBlock);
            spawnNewBlock();
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            g.drawLine(0, y * CELL_SIZE, BOARD_WIDTH * CELL_SIZE, y * CELL_SIZE);
        }
        // Draw fixed blocks
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (grid[y][x] != 0) {
                    g.setColor(getBlockColorByType(grid[y][x]));
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        // Draw current block
        if (currentBlock != null) {  
            for (Point p : currentBlock.getAbsolutePoints()) {
                g.setColor(getBlockColor(currentBlock));
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(p.x * CELL_SIZE, p.y * CELL_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
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

    // 為固定方塊提供類型對應的顏色
    private Color getBlockColorByType(int type) {
        return switch (type) {
            case 1 -> Color.CYAN;    // IBlock
            case 2 -> Color.BLUE;    // JBlock
            case 3 -> Color.ORANGE;  // LBlock
            case 4 -> Color.YELLOW;  // OBlock
            case 5 -> Color.GREEN;   // SBlock
            case 6 -> Color.MAGENTA; // TBlock
            case 7 -> Color.RED;     // ZBlock
            default -> Color.WHITE;
        };
    }

    // 為固定方塊分配類型值
    private int getBlockType(Block block) {
        if (block instanceof IBlock) return 1;
        if (block instanceof JBlock) return 2;
        if (block instanceof LBlock) return 3;
        if (block instanceof OBlock) return 4;
        if (block instanceof SBlock) return 5;
        if (block instanceof TBlock) return 6;
        if (block instanceof ZBlock) return 7;
        return 0;
    }

    public void spawnNewBlock() {
        currentBlock = nextBlock;
        nextBlock = generateRandomBlock();
        if (currentBlock == null || !isValidPosition(currentBlock)) {
            gameOver = true;
        }
    }

    private Block generateRandomBlock() {
        int type = random.nextInt(7);
        return switch (type) {
            case 0 -> new IBlock();
            case 1 -> new JBlock();
            case 2 -> new LBlock();
            case 3 -> new OBlock();
            case 4 -> new SBlock();
            case 5 -> new TBlock();
            case 6 -> new ZBlock();
            default -> null;
        };
    }
    
    public Block getCurrentBlock() {
        return currentBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }
    

    public boolean isValidPosition(Block block) {
        if (block == null || block.getAbsolutePoints() == null) return false;
        for (Point p : block.getAbsolutePoints()) {
            if (p == null || p.x < 0 || p.x >= BOARD_WIDTH || p.y < 0 || p.y >= BOARD_HEIGHT) return false;
            if (grid[p.y][p.x] != 0) return false;
        }
        return true;
    }

    public void placeBlock(Block block) {
        if (block == null) return;
        int blockType = getBlockType(block); // 獲取方塊類型
        for (Point p : block.getAbsolutePoints()) {
            grid[p.y][p.x] = blockType;
            if (p.y <= 0) gameOver = true; // 方塊固定在頂部，遊戲結束
        }
        clearFullLines();
    }

    public void moveBlock(int dx, int dy) {
        if (currentBlock == null) return;
        currentBlock.move(dx, dy);
        if (!isValidPosition(currentBlock)) {
            currentBlock.move(-dx, -dy);
        }
    }

    public void rotateBlock() {
        if (currentBlock == null) return;
        currentBlock.rotate();
        if (!isValidPosition(currentBlock)) {
            // Simple wall kick
            Point[] kickOffsets = new Point[]{
                new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(-2, 0), new Point(2, 0)
            };
            for (Point offset : kickOffsets) {
                currentBlock.move(offset.x, offset.y);
                if (isValidPosition(currentBlock)) return;
                currentBlock.move(-offset.x, -offset.y);
            }
            currentBlock.rotateBack();
        }
    }

    public void rotateBlockBack() {
        if (currentBlock == null) return;
        currentBlock.rotateBack();
        if (!isValidPosition(currentBlock)) {
            // 嘗試 wall kick
            Point[] kickOffsets = new Point[]{
                new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(-2, 0), new Point(2, 0)
            };
            for (Point offset : kickOffsets) {
                currentBlock.move(offset.x, offset.y);
                if (isValidPosition(currentBlock)) return;
                currentBlock.move(-offset.x, -offset.y);
            }
            currentBlock.rotate(); // 恢復原狀
        }
    }

    public void dropBlock() {
        if (gameOver || currentBlock == null) return;
    
        while (true) {
            currentBlock.move(0, 1);
            if (!isValidPosition(currentBlock)) {
                currentBlock.move(0, -1);
                placeBlock(currentBlock);
                spawnNewBlock();
                break;
            }
        }
    }

    private void clearFullLines() {
        int linesCleared = 0;
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (grid[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                for (int ty = y; ty > 0; ty--) {
                    System.arraycopy(grid[ty - 1], 0, grid[ty], 0, BOARD_WIDTH);
                }
                grid[0] = new int[BOARD_WIDTH];
            }
        }
        
        if (linesCleared > 0) {
            SoundPlayer.playSoundOnce("C:/d槽/java/java-B11207030-Eason-B11207042-Tony/music/clear_line.wav");
        }
        score += switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}