import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.*;
import java.util.Timer;

/**
 * 主函数，游戏逻辑的GUI实现
 * @author CC
 */
public class Main extends Application {
    private static boolean isGaming = false;
    private static boolean isReplaying = false;
    private static Timer clearTimer = new Timer(); //清除墓碑的线程
    private static Timer replayTimer = new Timer(); //回放战斗的线程
    private static Timer refreshTimer = new Timer(); //刷新屏幕的线程
    private static String combatLog; //记录战斗的字符串
    private static ArrayList<Position> tomb = new ArrayList<>(); //墓碑列表
    private static final String combatFile = System.getProperty("user.dir") + "\\NEWLOG.log"; //存档文件

    private Field field = new Field();
    private ArrayList<Calabash> brothers = new ArrayList<>();
    private ArrayList<Soldiers> soldiers = new ArrayList<>();
    private Grandpa grandpa = new Grandpa(new Position(5,1));
    private Snake snake = new Snake(new Position(5, 14));
    private Scorpion scorpion = new Scorpion();
    private final Image crossimage = new Image("fight.png", 50, 50, false, true);
    private final ImageView cross = new ImageView(crossimage);
    private FileReplay controller = new FileReplay(pane);
    private ArrayList<Integer> randArray = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
    private AboveAll God = new AboveAll(field, brothers, soldiers, grandpa, snake, scorpion);

    private static Pane pane = new Pane();
    private static Timeline timeline;
    private static Text title;
    private static Label author;
    private static VBox time;
    private static VBox navigation;
    private static VBox reform;
    //获取键盘输入
    class KeyParse implements EventHandler<KeyEvent> {
        public void handle(KeyEvent event) {
            if (!isGaming || !isReplaying) {
                if (event.getCode() == KeyCode.L)
                    replayGame(controller.parseFile(controller.loadFile()));
            }
        }
    }

    private ArrayList<ImageView> crossArray = new ArrayList<>();
    private void createTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        KeyFrame begin = new KeyFrame(Duration.ZERO);
        KeyFrame end = new KeyFrame(Duration.minutes(4));
        timeline.getKeyFrames().addAll(begin, end);
    }
    private Label createlabel() {
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
    private VBox createNavigation() {
        Button buttonStart = new Button("Start");
        buttonStart.setOnAction((ActionEvent t) -> {
            if(!isGaming && !isReplaying && field.qualified)
                playGame();
        });
        Button buttonLoad = new Button("Load");
        buttonLoad.setOnAction((ActionEvent t) -> {
            if(!isGaming && !isReplaying)
                replayGame(controller.parseFile(controller.loadFile()));
        });

        VBox navigate = new VBox(10);
        navigate.getChildren().addAll(buttonStart, buttonLoad);
        navigate.setLayoutX(30);
        navigate.setLayoutY(295);
        return navigate;
    }
    private VBox createFormation() {
        Button buttonheyi = new Button("鶴翼");
        buttonheyi.setOnAction((ActionEvent t) -> forceRestart("鶴翼"));
        Button buttonyanxing = new Button("雁行");
        buttonyanxing.setOnAction((ActionEvent t) -> forceRestart("雁行"));
        Button buttonchangshe = new Button("長蛇");
        buttonchangshe.setOnAction((ActionEvent t) -> forceRestart("長蛇"));
        Button buttonfengshi = new Button("鋒矢");
        buttonfengshi.setOnAction((ActionEvent t) -> forceRestart("鋒矢"));
        Button buttonyanyue = new Button("偃月");
        buttonyanyue.setOnAction((ActionEvent t) -> forceRestart("偃月"));
        Button buttonfangmen = new Button("方门");
        buttonfangmen.setOnAction((ActionEvent t) -> forceRestart("方门"));
        Button buttonyulin = new Button("鱼鳞");
        buttonyulin.setOnAction((ActionEvent t) -> forceRestart("鱼鳞"));
        Button buttonhenge = new Button("衡轭");
        buttonhenge.setOnAction((ActionEvent t) -> forceRestart("衡轭"));
        VBox formations = new VBox(10);
        formations.getChildren().addAll(buttonyulin, buttonchangshe, buttonfangmen, buttonfengshi,
                buttonhenge, buttonyanyue, buttonyanxing, buttonheyi);
        formations.setLayoutX(900);
        formations.setLayoutY(198);
        return formations;
    }

    //非NPC生物体线程
    private synchronized TimerTask getTask(Being en) {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Position start = en.position;
                    if (!isGaming) {
                        if(en.alive) en.rotate.playFromStart();
                        System.gc(); cancel();
                    }
                    if (!en.alive) {
                        tomb.add(en.position);
                        System.gc(); cancel();
                    }
                    Position neighbor = field.isBeside(start);
                    if (neighbor != null && field.getLiveBeing(neighbor).alive && en.alive)
                        fightGame(start, neighbor);
                    else {
                        Position end = chooseEnemy(en);
                        if (end == null) {
                            en.rotate.playFromStart();
                            if(isGaming) endGame(en);
                            System.gc(); cancel();
                        } else if (en.alive)
                            forwardGame(en,start, end);
                    }
                });
            }
        };
    }
    //清理墓碑线程
    private void tombClearThread() {
        clearTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    if(!isGaming) { System.gc();  cancel(); }
                    else if (!tomb.isEmpty()) {
                        Collections.shuffle(tomb);
                        combatLog += tomb.get(0).toString() + " DISAPPEAR\n";
                        if(isGaming) field.square[tomb.get(0).getX()][tomb.get(0).getY()].setNull();
                        tomb.remove(0);
                    }
                }
            }
        },6000, 3000);//6s后每3s清空一个墓碑
    }

    private synchronized boolean LetsFight(Position a, Position b) {
        assert(field.getLiveBeing(a) != null && field.getLiveBeing(b) != null);
        Being er = field.getLiveBeing(a);
        Being ee = field.getLiveBeing(b);
        assert (er.alive && ee.alive);
        if(er.para == 1) {
            er.power = soldiers.size() + 1;
            ee.power = brothers.size();
        } else {
            er.power = brothers.size();
            ee.power = soldiers.size() + 1;
        }
        Random rand = new Random();
        int live = rand.nextInt(er.power + ee.power);
        if(live < er.power) {
            er.alive = true;
            ee.alive = false;
            return true;
        } else {
            er.alive = false;
            ee.alive = true;
            return false;
        }
    }
    private synchronized Position chooseEnemy(Being en) {
        if (en.para == 1) {
            Collections.shuffle(soldiers);
            for (Soldiers s : soldiers)
                if (s.alive)  return s.position;
            if (scorpion.alive)
                return scorpion.position;
        } else {
            Collections.shuffle(randArray);
            for (Integer i : randArray)
                if (brothers.get(i).alive)
                    return brothers.get(i).position;
        }
        return null;
    }
    private synchronized void fightGame(Position start, Position neighbor) {
        ImageView fight = new ImageView(crossimage);
        if (neighbor.getX() == start.getX()) {
            fight.setY(start.getX() * 50 + 100);
            fight.setX((neighbor.getY() + start.getY()) * 25 + 100);
        } else {
            fight.setX(start.getY() * 50 + 100);
            fight.setY((neighbor.getX() + start.getX()) * 25 + 100);
        }
        crossArray.add(fight);
        refreshPane();
        combatLog += start.toString() + " and " + neighbor.toString() + " FIGHT\n";
        if (LetsFight(start, neighbor)) combatLog += neighbor.toString() + " DIE\n";
        else combatLog += start.toString() + " DIE\n";
    }
    private synchronized void forwardGame(Being en, Position start, Position end) {
        Position next = field.findNext(start, end);
        combatLog += (start.toString() + " to " + next.toString() + '\n');
        en.position = next;
        field.square[start.getX()][start.getY()].setNull();
        field.square[next.getX()][next.getY()].setBeing(en);
    }
    private synchronized void endGame(Being en) {
        if (en.para == 1) combatLog += "Calabash win\n";
        else combatLog += "Soldiers win\n";
        controller.writeFile(combatFile, combatLog);
        isGaming = false;
        field.qualified = false;
        timeline.pause();
    }
    private synchronized void forceRestart(String formation) {
        if(isReplaying || isGaming) return;
        God.initField(formation);
        timeline.stop();
        refreshPane();
    }

    private void initAnimation() {
        grandpa.setParallel();
        grandpa.parallel.play();
        snake.setParallel();
        snake.parallel.play();
        for(Calabash c : brothers) c.setRotate();
        for(Soldiers s : AboveAll.maxArmy) s.setRotate();
        scorpion.setRotate();
    }
    private void setView() {
        for(Calabash c : brothers) c.setView();
        for(Soldiers s : AboveAll.maxArmy) s.setView();
        scorpion.setView();
        snake.setView();
        grandpa.setView();
    }
    //初始化游戏, 默认为長蛇对阵鶴翼
    private void initGame() {
        field.setHulujia();
        for(int i = 0; i < 7; i ++) brothers.add(new Calabash(i + 1));
        for(int i = 0; i < Formation.max; i ++) AboveAll.maxArmy.add(new Soldiers());
        Collections.shuffle(brothers);
        God.initField("鶴翼");
        this.setView();
        this.refreshPane();
        this.initAnimation();
        pane.setOnKeyPressed(new KeyParse());
    }
    private void playGame() {
        isGaming = true;
        tomb.clear();
        timeline.playFromStart();
        combatLog = "New Log\n" + "Game Start\n" + scorpion.getFormation().name + '\n';
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!isGaming) { System.gc(); cancel(); }
                Platform.runLater(() -> refreshPane());
            }
        }, 100, 500);
        tombClearThread();
        for (Calabash c : brothers)
            c.timer.scheduleAtFixedRate(getTask(c), 1000, 1000);
        scorpion.timer.scheduleAtFixedRate(getTask(scorpion), 1000, 1000);
        for (Soldiers s : soldiers)
            s.timer.scheduleAtFixedRate(getTask(s), 1000, 1000);
    }
    /**
     * @param buffer 从文件读取得到的字符串
     */
    private void replayGame(ArrayList<String> buffer) {
        if(buffer.isEmpty()) return;
        isReplaying = true;
        timeline.playFromStart();
        replayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String text = buffer.get(0);
                    buffer.remove(0);
                    if(text.equals("New Log")) return;
                    if(text.equals("Game Start")) return;
                    String[] s = text.split(" ");
                    if(s.length == 1) { God.initField(text); refreshPane(); return; }
                    if(s.length == 2) {
                        isReplaying = false;
                        timeline.pause();
                        System.gc(); cancel();
                    } else if(s.length == 6) {
                        int fx = Integer.valueOf(s[0]);
                        int fy = Integer.valueOf(s[1]);
                        int tx = Integer.valueOf(s[3]);
                        int ty = Integer.valueOf(s[4]);
                        if(fx == tx) {
                            cross.setY(fx * 50 + 100);
                            cross.setX((fy+ty) * 25 + 100);
                        } else {
                            cross.setX(fy * 50 + 100);
                            cross.setY((fx+tx) * 25 + 100);
                        }
                        pane.getChildren().remove(cross);
                        pane.getChildren().add(cross);
                    } else if(s.length == 5 && s[2].equals("to")) {
                        int fx = Integer.valueOf(s[0]);
                        int fy = Integer.valueOf(s[1]);
                        int tx = Integer.valueOf(s[3]);
                        int ty = Integer.valueOf(s[4]);
                        Position start = new Position(fx, fy);
                        Position next = new Position(tx, ty);
                        Being en = field.getLiveBeing(start);
                        en.position = next;
                        field.square[start.getX()][start.getY()].setNull();
                        field.square[next.getX()][next.getY()].setBeing(en);
                        refreshPane();
                    } else if(s.length == 3 && s[2].equals("DIE")) {
                        int dx = Integer.valueOf(s[0]);
                        int dy = Integer.valueOf(s[1]);
                        Position p = new Position(dx, dy);
                        Being ee = field.getLiveBeing(p);
                        ee.alive = false;
                        refreshPane();
                    } else if(s.length == 3 && s[2].equals("DISAPPEAR")) {
                        int dx = Integer.valueOf(s[0]);
                        int dy = Integer.valueOf(s[1]);
                        Being ee = field.square[dx][dy].getBeing();
                        assert (!ee.alive);
                        field.square[dx][dy].setNull();
                        refreshPane();
                    }
                });
            }
        }, 0, 250);
    }

    private Parent createContent() {
        createTimeline();
        title = createTitle();
        navigation = createNavigation();
        reform = createFormation();
        time = createTime();
        author = createlabel();
        pane.setPrefSize(1000, 650);
        pane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        pane.getChildren().addAll(title, time, navigation, reform, author);
        pane.setBackground(new Background(new BackgroundImage(field.getBackground(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        pane.getStylesheets().add("Style.css");
        return pane;
    }
    private synchronized void refreshPane() {
        pane.getChildren().clear();
        field.huluteng.setX(0);
        field.huluteng.setY(0);
        pane.getChildren().addAll(field.huluteng, title, time, navigation, reform, author);
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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CALABASH");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("icon.jpg"));
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setOnCloseRequest((WindowEvent e) -> System.exit(0));
        primaryStage.show();
        timeline.stop();
        initGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}