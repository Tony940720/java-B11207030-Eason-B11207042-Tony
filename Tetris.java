public class Tetris {
    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Tetris");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);

        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setVisible(true);

        panel.startGame();
    }
}