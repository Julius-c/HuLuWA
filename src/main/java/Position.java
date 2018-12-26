/**
 * 位置类, 可得到当前位置的上下左右
 * @author CC
 */

public class Position {
    private int x;
    private int y;
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Position up() { return new Position(x - 1, y); }
    public Position down() { return new Position(x + 1, y); }
    public Position left() { return new Position(x, y - 1); }
    public Position right() { return new Position(x, y + 1); }
    public String toString() { return String.valueOf(x)+' '+String.valueOf(y); }
}
