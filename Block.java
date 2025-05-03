import java.awt.*;

public abstract class Block {
    protected Point[] shape;
    protected Point position;

    public abstract void rotate();
    public abstract void rotateBack();

    public void move(int dx, int dy) {
        position.translate(dx, dy);
    }

    public Point[] getAbsolutePoints() {
        Point[] result = new Point[shape.length];
        for (int i = 0; i < shape.length; i++) {
            result[i] = new Point(position.x + shape[i].x, position.y + shape[i].y);
        }
        return result;
    }
}