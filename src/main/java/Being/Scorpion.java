package Being;

import Formation.Formation;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Scorpion extends Being {

    private Formation formation;

    public Scorpion() {
        super("蝎子");
        this.alive = true;
        this.para = -1;
    }

    public void setFormation(String name) { this.formation = new Formation(name); }
    public Formation getFormation() {
        return this.formation;
    }

    @Override
    public void setView() {
        this.view = new ImageView(new Image("xzj.jpg", 50, 50, false, true));
        this.dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }

    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}