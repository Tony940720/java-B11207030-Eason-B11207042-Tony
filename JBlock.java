import java.awt.*;

public class JBlock extends Block {
    public JBlock() {
        shape = new Point[] {
            new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(1, 1)
        };
        position = new Point(4, 0);
    }

    public void rotate() {
        for (Point p : shape) {
            int temp = p.x;
            p.x = -p.y;
            p.y = temp;
        }
    }

    public void rotateBack() {
        rotate(); rotate(); rotate();
    }
}