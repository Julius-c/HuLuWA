package GameControl;

import Field.*;
import Position.Position;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.*;
/**
 * 游戏逻辑的GUI实现
 * @author CC
 */
public class GuiControl{

    public Pane pane = new Pane();
    public Timeline timeline = new Timeline();

    private Text title;
    private VBox time;
    private Label author;

    private ArrayList<ImageView> crossArray = new ArrayList<>();
    private final ImageView cross = new ImageView(new Image("fight.png", 50, 50, false, true));

    Timer refreshTimer = new Timer(); //刷新屏幕的线程

    private Field field;
    private VBox navigation;
    private VBox reform;
    GuiControl(Field field, VBox navigation, VBox reform) {
        this.field = field;
        this.navigation = navigation;
        this.reform = reform;
    }

    private Timeline createTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        KeyFrame begin = new KeyFrame(Duration.ZERO);
        KeyFrame end = new KeyFrame(Duration.minutes(4));
        timeline.getKeyFrames().addAll(begin, end);
        return timeline;
    }

    private Label createLabel() {
        Label label = new Label("@author CC");
        label.setLayoutX(50);
        label.setLayoutY(100);
        label.setBackground(Background.EMPTY);
        label.setFont(Font.font(null, FontWeight.LIGHT, 25));
        label.setTextFill(Color.WHITESMOKE);
        label.setRotate(340);
        return label;
    }

    private Text createTitle() {
        Text title = new Text("Welcome to Battlefield of Calabash!");
        title.setX(140.0);
        title.setY(50.0);
        title.setCache(true);
        title.setFont(Font.font(null, FontWeight.BOLD, 40));
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(3);
        title.setFill(Color.SNOW);
        return title;
    }

    private VBox createTime() {
        final TextFlow flow = new TextFlow();
        final Text current = new Text("Current time: ");
        final Text rate = new Text();
        final Text ms = new Text(" ms");
        current.setBoundsType(TextBoundsType.VISUAL);
        current.setFill(Color.SNOW);
        current.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        ms.setBoundsType(TextBoundsType.VISUAL);
        ms.setFill(Color.SNOW);
        ms.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        rate.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        rate.setText(String.format("%4d", 0));
        rate.setFill(Color.SNOW);

        timeline.currentTimeProperty().addListener((Observable ov) -> {
            rate.setText(String.format("%4.0f", timeline.getCurrentTime().toMillis()));
            flow.requestLayout();
        });
        flow.getChildren().addAll(current, rate, ms);

        VBox controls = new VBox(10);
        controls.getChildren().addAll(flow);
        controls.setLayoutX(400);
        controls.setLayoutY(65);
        return controls;
    }

    public Parent createContent() {
        timeline = createTimeline();
        title = createTitle();
        time = createTime();
        author = createLabel();
        pane.setPrefSize(1000, 650);
        pane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        pane.getChildren().addAll(title, time, navigation, reform, author);
        pane.setBackground(new Background(new BackgroundImage(field.getBackground(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        pane.getStylesheets().add("Style.css");
        return pane;
    }

    public synchronized void refreshPane() {
        pane.getChildren().clear();
        pane.getChildren().addAll(field.tree, title, time, navigation, reform, author);
        for(int i = 0; i < Field.Height; i++) {
            for(int j = 0; j < Field.Width; j++) {
                if(field.square[i][j].getBeing() != null) {
                    ImageView view = field.square[i][j].getBeing().getImage();
                    view.setX(j * 50 + 100);
                    view.setY(i * 50 + 100);
                    pane.getChildren().add(view);
                }
            }
        }
        for(ImageView view : crossArray)
            pane.getChildren().add(view);
        crossArray.clear();
    }

    void addCross(Position start, Position neighbor) {
        if (neighbor.getX() == start.getX()) {
            cross.setY(start.getX() * 50 + 100);
            cross.setX((neighbor.getY() + start.getY()) * 25 + 100);
        } else {
            cross.setX(start.getY() * 50 + 100);
            cross.setY((neighbor.getX() + start.getX()) * 25 + 100);
        }
        crossArray.add(cross);
    }
}