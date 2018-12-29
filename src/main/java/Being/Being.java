package Being;

import Position.Position;
import javafx.animation.*;
import javafx.scene.image.ImageView;

import java.util.Timer;

/**
 * 生物体类的基类
 * @author CC
 */
public abstract class Being {

    /**
     * para Good or Bad
     * alive Alive or Dead
     * power Fight Power
     * timer Thread
     */
    String name;
    protected Position position;

    public RotateTransition rotate;
    public ParallelTransition parallel;
    public Timer timer = new Timer();

    TranslateTransition translate;
    ScaleTransition scale;
    ImageView view;
    ImageView dead;
    Integer para;
    boolean alive;

    Being() { }
    Being(String name) {
        this.name = name;
    }
    public void setPosition (Position position) { this.position = position; }
    public ImageView getImage () {
        return alive ? view : dead;
    }
    public boolean isAlive() { return alive; }
    public Integer getPara() { return para; }
    public Position getPosition() { return position; }
    public void die() { alive = false; }
    public void live() { alive = true; }

    public abstract void setView();
}