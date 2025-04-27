package java-B11207030-Eason-B11207042-Tony;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends JPanel implements ActionListener {
    private static final int PANEL_WIDTH = 200;
    private static final int PANEL_HEIGHT = 400;
    private static final int BLOCK_SIZE = 30;
    private int blockX = (PANEL_WIDTH - BLOCK_SIZE) / 2;
    private int blockY = 0;
    private Timer timer;

    public Tetris() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        // 每 500ms 更新一次位置
        timer = new Timer(500, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 畫出一個白色方塊
        g.setColor(Color.WHITE);
        g.fillRect(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 方塊往下移一格
        blockY += BLOCK_SIZE;
        // 掉到底部就回到最上面
        if (blockY > PANEL_HEIGHT - BLOCK_SIZE) {
            blockY = 0;
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Tetris());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

