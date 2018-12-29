package Field;

import Position.Position;
import Being.Being;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;
import java.util.Vector;

/**
 * 战场类, 由方块square类聚合而成
 * @author CC
 */
public class Field {

    public final static int Width = 16;
    public final static int Height = 11;
    public final Square[][] square;
    public boolean qualified = false;
    public ImageView tree;

    public Field() {
        square = new Square[Height][Width];
        for(int i = 0; i < Height; i ++)
            for(int j = 0; j < Width; j ++)
                square[i][j] = new Square();
    }
    public void setTree() {
        tree = new ImageView(new Image("tree.png", 1000, 650, true, true));
        tree.setX(0);
        tree.setY(0);
    }
    public Image getBackground() {
        return new Image("bg.jpg", 1000, 650, false, true);
    }

    synchronized void clearField() {
        for(int i = 0; i < Height; i ++)
            for(int j = 0; j < Width; j ++)
                square[i][j].setNull();
    }

    public synchronized boolean posQualified(Position p) {
        return (p.getX() >= 0 && p.getX() < Height && p.getY() >= 0 && p.getY() < Width);
    }

    public synchronized boolean emptySquare(Position p) {
        return posQualified(p) && square[p.getX()][p.getY()].getBeing() == null;
    }

    /**
     * @return 返回位置上活着的生物体
     */
    public synchronized Being getLiveBeing(Position p) {
        if(p == null) return null;
        Being b = square[p.getX()][p.getY()].getBeing();
        if(b != null && b.isAlive()) return b;
        else return null;
    }

    /**
     * @return 判断上下左右是否有敌人, 如果有, 返回敌人的位置
     */
    public synchronized Position isBeside(Position p) {
        if(isOpposite(p, p.up())) return p.up();
        else if(isOpposite(p, p.down())) return p.down();
        else if(isOpposite(p, p.left())) return p.left();
        else if(isOpposite(p, p.right())) return p.right();
        else return null;
    }

    /**
     * @return 判断位置a与b是否敌对
     */
    public synchronized boolean isOpposite(Position a, Position b) {
        if(posQualified(a) && posQualified(b))
            if(getLiveBeing(a) != null && getLiveBeing(b) != null)
                return ((getLiveBeing(a).getPara() == 1 && getLiveBeing(b).getPara() == -1) ||
                        (getLiveBeing(a).getPara() == -1 && getLiveBeing(b).getPara() == 1));
        return false;
    }

    /**
     * @return 返回一个随机位置
     */
    private synchronized Position getRandomPos(Position p) {
        Vector<Position> v = new Vector<>();
        if(emptySquare(p.up())) v.add(p.up());
        if(emptySquare(p.down())) v.add(p.down());
        if(emptySquare(p.right())) v.add(p.right());
        if(emptySquare(p.left())) v.add(p.left());
        Random rand = new Random();
        if(v.size() <= 0) return p;
        return v.get(rand.nextInt(v.size()));
    }

    /**
     * @return 返回从a到b所需要的下一个位置
     */
    public synchronized Position findNext(Position a, Position b) {
        Vector<Position> v = new Vector<>();
        if((emptySquare(a.right()) && b.getY()>a.getY())) v.add(a.right());
        else if((emptySquare(a.up()) && b.getX()<a.getX())) v.add(a.up());
        else if((emptySquare(a.left()) && b.getY()<a.getY())) v.add(a.left());
        else if((emptySquare(a.down()) && b.getX()>a.getX())) v.add(a.down());
        Random rand = new Random();
        if(v.size() <= 0 ) { return getRandomPos(a); }
        return v.get(rand.nextInt(v.size()));
    }
}