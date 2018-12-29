package Being;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Soldiers extends Being {

    public Soldiers() {
        super("喽啰");
        this.alive = true;
        this.para = -1;//BAD FIGHTER
    }

    @Override
    public void setView() {
        this.view = new ImageView(new Image("ll.jpg", 50, 50, false, true));
        this.dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }

    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}