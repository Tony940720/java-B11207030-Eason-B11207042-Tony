import java.awt.*;

public class OBlock extends Block {
    public OBlock() {
        shape = new Point[] {
            new Point(0, 0), new Point(1, 0),
            new Point(0, 1), new Point(1, 1)
        };
        position = new Point(4, 0);
    }

    public void rotate() {}
    public void rotateBack() {}
}