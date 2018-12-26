import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Timer;

/**
 * 生物体类的基类及其衍生类
 * @author CC
 */
public class Being {
    protected String name;
    protected Position position;
    /**
     * 生物属性好坏
     * >0 means Good, <0 means Bad
     * 1 means Fighter, 2 means NPC
     */
    protected int para;
    protected boolean alive; //生物是否存活
    //生物的动画
    protected RotateTransition rotate;
    protected TranslateTransition translate;
    protected ScaleTransition scale;
    protected ParallelTransition parallel;
    protected ImageView view;
    protected ImageView dead;
    protected Integer power; //生物战斗概率权重
    protected Timer timer = new Timer(); //生物的线程Timer

    Being() { }
    Being(String name) {
        this.name = name;
    }
    public void setPosition (Position position) { this.position = position; }
    public ImageView getImage () {
        return alive ? view : dead;
    }
}

class Calabash extends Being{
    private int rank;
    private Color color;

    Calabash(int rank) {
        this.para = 1;
        this.alive = true;
        this.rank = rank;
        switch(rank) {
            case 1: color = Color.RED; this.name = "老大"; break;
            case 2: color = Color.ORANGE; this.name = "老二"; break;
            case 3: color = Color.YELLOW; this.name = "老三"; break;
            case 4: color = Color.GREEN; this.name = "老四"; break;
            case 5: color = Color.CYAN; this.name = "老五"; break;
            case 6: color = Color.BLUE; this.name = "老六"; break;
            case 7: color = Color.PURPLE; this.name = "老七"; break;
            default: System.out.println("WRONG RANK"); break;
        }
    }
    public Color getColor() { return color; }
    public int getRank() { return rank; }
    public void setView() {
        view = new ImageView(new Image(String.format("%d.jpg", rank), 50, 50, false, true));
        dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }
    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}

class Grandpa extends Being {
    Grandpa(Position position) {
        this.name = "爷爷";
        this.position = position;
        this.alive = true;
        this.para = 2;
    }
    public void setView() {
        view = new ImageView(new Image("lyy.jpg", 50, 50, false, true));
        dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }
    public void setParallel() {
        this.rotate = new RotateTransition(Duration.seconds(2));
        this.rotate.setByAngle(360);
        this.rotate.setCycleCount(1);
        this.rotate.setAutoReverse(true);
        this.translate = new TranslateTransition(Duration.seconds(2));
        this.translate.setFromY(-200);
        this.translate.setToY(200);
        this.translate.setCycleCount(1);
        this.translate.setAutoReverse(true);
        this.scale = new ScaleTransition(Duration.seconds(1));
        this.scale.setToX(2);
        this.scale.setToY(2);
        this.scale.setAutoReverse(true);
        this.scale.setCycleCount(2);
        this.parallel = new ParallelTransition(view, rotate, translate, scale);
        this.parallel.setCycleCount(Timeline.INDEFINITE);
        this.parallel.setAutoReverse(true);
    }
}

class Snake extends Being {
    Snake(Position position) {
        this.name = "蛇精";
        this.position = position;
        this.alive = true;
        this.para = -2;//BAD NPC
    }
    public void setView() {
        view = new ImageView(new Image("sj.jpg", 50, 50, false, true));
        dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }
    public void setParallel() {
        this.rotate = new RotateTransition(Duration.seconds(2));
        this.rotate.setByAngle(360);
        this.rotate.setCycleCount(1);
        this.rotate.setAutoReverse(true);
        this.translate = new TranslateTransition(Duration.seconds(2));
        this.translate.setFromY(200);
        this.translate.setToY(-200);
        this.translate.setCycleCount(1);
        this.translate.setAutoReverse(true);
        this.scale = new ScaleTransition(Duration.seconds(1));
        this.scale.setToX(2);
        this.scale.setToY(2);
        this.scale.setAutoReverse(true);
        this.scale.setCycleCount(2);
        this.parallel = new ParallelTransition(view, rotate, translate, scale);
        this.parallel.setCycleCount(Timeline.INDEFINITE);
        this.parallel.setAutoReverse(true);
    }
}

class Scorpion extends Being {
    private Formation formation;
    Scorpion() {
        super("蝎子");
        this.alive = true;
        this.para = -1;
    }
    public void setFormation(String name) {
        formation = new Formation(name);
    }
    public Formation getFormation() {
        return this.formation;
    }
    public void setView() {
        view = new ImageView(new Image("xzj.jpg", 50, 50, false, true));
        dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }
    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}

class Soldiers extends Being {
    Soldiers() {
        super("喽啰");
        this.alive = true;
        this.para = -1;//BAD FIGHTER
    }
    public void setView() {
        view = new ImageView(new Image("ll.jpg", 50, 50, false, true));
        dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }
    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}