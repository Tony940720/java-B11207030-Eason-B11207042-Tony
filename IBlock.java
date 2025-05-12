import java.awt.*;

public class IBlock extends Block {
    private boolean rotated = false;

    public IBlock() {
        shape = new Point[] {
            new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(2, 0)
        };
        position = new Point(4, -2);
    }

    public void rotate() {
        for (Point p : shape) {
            int temp = p.x;
            p.x = -p.y;
            p.y = temp;
        }
        rotated = !rotated;
    }

    public void rotateBack() {
        rotate(); // reverse same operation
    }
}