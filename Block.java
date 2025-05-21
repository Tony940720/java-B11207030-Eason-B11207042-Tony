import java.awt.*;

public abstract class Block implements Cloneable {
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

    @Override
    public Block clone() {
        try {
            Block copy = (Block) super.clone(); // 淺複製
            copy.position = new Point(this.position); // 複製位置
            copy.shape = new Point[this.shape.length]; // 深複製形狀
            for (int i = 0; i < this.shape.length; i++) {
                copy.shape[i] = new Point(this.shape[i]);
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void resetPosition() {
        this.position.x = 4;
        this.position.y = -2;
    }
    
    public Point[] getShape() {
       return shape;
    }
    
}