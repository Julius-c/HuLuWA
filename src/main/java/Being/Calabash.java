package Being;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Calabash extends Being{

    private int rank;
    private COLOR color;

    public Calabash(int rank) {
        this.para = 1;
        this.alive = true;
        this.rank = rank;
        switch(rank) {
            case 1: this.color = COLOR.RED; this.name = "老大"; break;
            case 2: this.color = COLOR.ORA; this.name = "老二"; break;
            case 3: this.color = COLOR.YEL; this.name = "老三"; break;
            case 4: this.color = COLOR.GRE; this.name = "老四"; break;
            case 5: this.color = COLOR.CYA; this.name = "老五"; break;
            case 6: this.color = COLOR.BLU; this.name = "老六"; break;
            case 7: this.color = COLOR.PUR; this.name = "老七"; break;
            default: System.out.println("WRONG RANK"); break;
        }
    }

    public COLOR getColor() { return this.color; }
    public int getRank() { return this.rank; }

    @Override
    public void setView() {
        this.view = new ImageView(new Image(String.format("%d.jpg", rank), 50, 50, false, true));
        this.dead = new ImageView(new Image("dead.jpg", 50, 50, false, false));
    }

    public void setRotate() {
        this.rotate = new RotateTransition(Duration.seconds(0.5), view);
        this.rotate.setByAngle(1080);
        this.rotate.setCycleCount(2);
        this.rotate.setAutoReverse(true);
    }
}