import GameControl.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    private GameLogic control = new GameLogic();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("葫芦娃大战妖精");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("icon.jpg"));
        primaryStage.setScene(new Scene(control.guiControl.createContent()));
        primaryStage.setOnCloseRequest((WindowEvent e) -> System.exit(0));
        primaryStage.show();
        control.initGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}