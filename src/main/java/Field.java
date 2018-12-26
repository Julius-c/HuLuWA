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
    public ImageView huluteng;
    Field() {
        square = new Square[Height][Width];
        for(int i = 0; i < Height; i ++)
            for(int j = 0; j < Width; j ++)
                square[i][j] = new Square();
    }
    public void setHulujia() {
        huluteng = new ImageView(new Image("HuluTeng.png", 1000, 650, true, true));
    }
    public Image getBackground() {
        return new Image("bg.jpg", 1000, 650, false, true);
    }
    //清空战场
    public synchronized void clearField() {
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
    public synchronized Being getLiveBeing(Position p) {
        if(p == null) return null;
        Being b = square[p.getX()][p.getY()].getBeing();
        if(b != null && b.alive) return b;
        else return null;
    }
    //判断上下左右是否有敌人, 如果有, 返回敌人的位置
    public synchronized Position isBeside(Position p) {
        if(isOpposite(p, p.up())) return p.up();
        else if(isOpposite(p, p.down())) return p.down();
        else if(isOpposite(p, p.left())) return p.left();
        else if(isOpposite(p, p.right())) return p.right();
        else return null;
    }
    //判断位置a与b是否敌对
    public synchronized boolean isOpposite(Position a, Position b) {
        if(posQualified(a) && posQualified(b))
            if(getLiveBeing(a) != null && getLiveBeing(b) != null)
                return ((getLiveBeing(a).para == 1 && getLiveBeing(b).para == -1) ||
                        (getLiveBeing(a).para == -1 && getLiveBeing(b).para == 1));
        return false;
    }
    //返回一个随机位置
    public synchronized Position getRandomPos(Position p) {
        Vector<Position> v = new Vector<>();
        if(emptySquare(p.up())) v.add(p.up());
        if(emptySquare(p.down())) v.add(p.down());
        if(emptySquare(p.right())) v.add(p.right());
        if(emptySquare(p.left())) v.add(p.left());
        Random rand = new Random();
        if(v.size() <= 0) return p;
        return v.get(rand.nextInt(v.size()));
    }
    //返回从a到b所需要的下一个位置
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

class Square {
    private Being being;
    Square() {

    }
    public boolean isEmpty() {
        return (this.being == null);
    }
    public void setBeing(Being being) {
        if(isEmpty()) this.being = being;
    }
    public void setNull() {
        this.being = null;
    }
    public Being getBeing() {
        return this.being;
    }
}