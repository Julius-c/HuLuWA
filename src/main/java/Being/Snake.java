package Being;

import Position.Position;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Snake extends Being {

    public Snake(Position position) {
        this.name = "蛇精";
        this.position = position;
        this.alive = true;
        this.para = -2;
    }

    @Override
    public void setView() {
        this.view = new ImageView(new Image("sj.jpg", 50, 50, false, true));
        this.dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
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
