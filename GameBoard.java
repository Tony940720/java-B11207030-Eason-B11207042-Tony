import java.awt.*;
import java.util.ArrayList;
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
    private int clearline = 0;
    private boolean gameOver = false;
    //消除動畫用參數
    private java.util.List<Integer> linesToClear = new ArrayList<>();
    private long lineClearStartTime = 0;
    private static final int LINE_CLEAR_DELAY = 500; 
    private boolean isClearingLines = false;    
    private Block holdBlock = null;
    private boolean holdUsed = false;

    public GameBoard() {
        nextBlock = generateRandomBlock();
        spawnNewBlock();
    }

    public void update() {
        if (gameOver) return;

        if (isClearingLines) {
            if (System.currentTimeMillis() - lineClearStartTime >= LINE_CLEAR_DELAY) {
                clearMarkedLines(); // 真正清除
                isClearingLines = false;
            }
            return;
        }

        currentBlock.move(0, 1);
        if (!isValidPosition(currentBlock)) {
            currentBlock.move(0, -1);
            placeBlock(currentBlock);
            spawnNewBlock();
        }
    }

    public void draw(Graphics g) {
        int offsetY = 60;

        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            g.drawLine(x * CELL_SIZE, offsetY, x * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE + offsetY);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            g.drawLine(0, y * CELL_SIZE + offsetY, BOARD_WIDTH * CELL_SIZE, y * CELL_SIZE + offsetY);
        }
        // Draw fixed blocks
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (grid[y][x] != 0) {
                    if (linesToClear.contains(y) && (System.currentTimeMillis() / 100) % 2 == 0) {
                        g.setColor(Color.WHITE); // 閃爍效果
                    } else {
                        g.setColor(getBlockColorByType(grid[y][x]));
                    }
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw ghost block (drop preview)
        Block ghostBlock = getGhostBlock();
        if (ghostBlock != null) {
            for (Point p : ghostBlock.getAbsolutePoints()) {
                g.setColor(getBlockColor(currentBlock)); // 與目前方塊同色
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 半透明
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(p.x * CELL_SIZE, p.y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
            }
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // 恢復不透明
        }

        // Draw current block
        if (currentBlock != null) {  
            for (Point p : currentBlock.getAbsolutePoints()) {
                g.setColor(getBlockColor(currentBlock));
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(p.x * CELL_SIZE, p.y * CELL_SIZE + offsetY, BLOCK_SIZE, BLOCK_SIZE);
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
            case 8 -> Color.DARK_GRAY;//newRow
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
        holdUsed = false;
        checkGameOver();
    }
    
    public void holdCurrentBlock() {
        if (holdUsed) return;
    
        if (holdBlock == null) {
            holdBlock = currentBlock;
            spawnNewBlock();
        } 
        else {
            Block temp = currentBlock;
            currentBlock = holdBlock;
            holdBlock = temp;
            currentBlock.resetPosition(); // 重設位置，確保方塊從頂部開始
        }
       holdUsed = true;
    }

private Block getGhostBlock() {
    if (currentBlock == null) return null;

    Block ghost = currentBlock.clone(); // 假設 Block 有正確實作 clone()
    while (true) {
        ghost.move(0, 1);
        if (!isValidPosition(ghost)) {
            ghost.move(0, -1); // 回到最後合法位置
            break;
        }
    }
    return ghost;
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

    public boolean isValidPosition(Block block) {
        if (block == null || block.getAbsolutePoints() == null) return false;
        for (Point p : block.getAbsolutePoints()) {
            if (p == null || p.x < 0 || p.x >= BOARD_WIDTH || p.y >= BOARD_HEIGHT) return false;
            if (p.y >= 0 && grid[p.y][p.x] != 0) return false;
        }
        return true;
    }

    public void placeBlock(Block block) {
        if (block == null) return;
        int blockType = getBlockType(block); // 獲取方塊類型
        for (Point p : block.getAbsolutePoints()) {
            if (p.y >= 0) grid[p.y][p.x] = blockType;
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
        linesToClear.clear();
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (grid[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) linesToClear.add(y);
        }

        if (!linesToClear.isEmpty()) {
            lineClearStartTime = System.currentTimeMillis();
            isClearingLines = true;
            SoundPlayer.playSoundOnce("java-B11207030-Eason-B11207042-Tony/music/clear_line.wav");
        }
    }

    private void clearMarkedLines() {
        int linesCleared = linesToClear.size();
        for (int y : linesToClear) {
            for (int ty = y; ty > 0; ty--) {
                grid[ty] = grid[ty - 1].clone();
            }
            grid[0] = new int[BOARD_WIDTH];
        }
    
        score += switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        clearline += linesCleared;

        linesToClear.clear();
    }
    
    public void insertBottomRow() {
        checkGameOver(); 
        if (gameOver) return;

        // 將所有行上移
        for (int y = 0; y < grid.length - 1; y++) {
            grid[y] = grid[y + 1].clone();
        }
    
        // 新增一行滿行但有一格是空的（用 8 代表 Challenge block）
        int[] newRow = new int[grid[0].length];
        int hole = new Random().nextInt(newRow.length);
        for (int x = 0; x < newRow.length; x++) {
            newRow[x] = (x == hole) ? 0 : 8;
        }
        grid[grid.length - 1] = newRow;
    }

    private void checkGameOver() {
        if ("Challenge".equals(mode)) {
            // 若最上面一行有方塊，代表被擠到最上方 → Game Over
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (grid[0][x] != 0) {
                    gameOver = true;
                    return;
                }
            }
        } else {
            // 普通模式：如果新方塊無法放置 → Game Over
            if (currentBlock != null && !isValidPosition(currentBlock)) {
                gameOver = true;
            }
        }
    }

    public Block getHoldBlock() {
        return holdBlock;
    }
    
    public Block getCurrentBlock() {
        return currentBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public int getScore() {
        return score;
    }

    public int getclearline() {
        return clearline;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    
    //mode
    private String mode = "Normal"; // 儲存模式
   
    public GameBoard(String mode) {
        this(); // 呼叫原本的無參數建構子
        this.mode = mode;
    }
   
    public String getMode() {
        return mode;
    }

}