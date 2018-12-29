package GameControl;

import Being.*;
import Field.*;
import FileControl.FileReplay;
import Formation.Formation;
import Position.Position;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.*;

public class GameLogic {

    public static boolean isGaming = false;
    public static boolean isReplaying = false;
    private Timer clearTimer = new Timer(); //清除墓碑的线程
    private String combatLog; //记录战斗的字符串
    private ArrayList<Position> tomb = new ArrayList<>(); //墓碑列表
    private final String combatFile = System.getProperty("user.dir") + "\\Combat.log"; //存档文件

    private Field field = new Field();
    private ArrayList<Calabash> brothers = new ArrayList<>();
    private ArrayList<Soldiers> soldiers = new ArrayList<>();
    private Grandpa grandpa = new Grandpa(new Position(5,1));
    private Snake snake = new Snake(new Position(5, 14));
    private Scorpion scorpion = new Scorpion();
    private ArrayList<Integer> randArray = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));

    //获取键盘输入
    class KeyParse implements EventHandler<KeyEvent> {
        public void handle(KeyEvent event) {
            if (!GameLogic.isGaming || !GameLogic.isReplaying) {
                if (event.getCode() == KeyCode.L)
                    fileReplay.replayGame();
            }
        }
    }

    public GuiControl guiControl = new GuiControl(field, createNavigation(), createFormation());

    private FieldControl fieldControl = new FieldControl(field, brothers, soldiers, grandpa, snake, scorpion);
    private FileReplay fileReplay = new FileReplay(field, fieldControl, guiControl);

    //非NPC的生物体线程
    private synchronized TimerTask getTask(Being en) {
        return new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Position start = en.getPosition();
                    if (!isGaming) {
                        if(en.isAlive()) en.rotate.playFromStart();
                        System.gc(); cancel();
                    }
                    if (!en.isAlive()) {
                        tomb.add(en.getPosition());
                        System.gc(); cancel();
                    }
                    Position neighbor = field.isBeside(start);
                    if (neighbor != null && field.getLiveBeing(neighbor).isAlive() && en.isAlive())
                        fightRecord(start, neighbor);
                    else {
                        Position end = chooseEnemy(en);
                        if (end == null) {
                            en.rotate.playFromStart();
                            if(isGaming) endGame(en);
                            System.gc(); cancel();
                        } else if (en.isAlive())
                            forwardGame(start, end);
                    }
                });
            }
        };
    }
    //清理墓碑线程
    private void initClearThread() {
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

    private synchronized Position chooseEnemy(Being en) {
        if (en.getPara() == 1) {
            Collections.shuffle(soldiers);
            for (Soldiers s : soldiers)
                if (s.isAlive())  return s.getPosition();
            if (scorpion.isAlive())
                return scorpion.getPosition();
        } else {
            Collections.shuffle(randArray);
            for (Integer i : randArray)
                if (brothers.get(i).isAlive())
                    return brothers.get(i).getPosition();
        }
        return null;
    }
    private synchronized boolean letsFight(Position a, Position b) {
        assert(field.getLiveBeing(a) != null && field.getLiveBeing(b) != null);
        Being er = field.getLiveBeing(a);
        Being ee = field.getLiveBeing(b);
        assert (er.isAlive() && ee.isAlive());
        int prob_a, prob_b;
        if(er.getPara() == 1) {
            prob_a = soldiers.size() + 1;
            prob_b = brothers.size();
        } else {
            prob_a = brothers.size();
            prob_b = soldiers.size() + 1;
        }
        Random rand = new Random();
        int live = rand.nextInt(prob_a + prob_b);
        if(live < prob_a) {
            ee.die(); return true;
        } else {
            er.die(); return false;
        }
    }
    private synchronized void fightRecord(Position start, Position neighbor) {
        guiControl.addCross(start, neighbor);
        guiControl.refreshPane();
        combatLog += start.toString() + " and " + neighbor.toString() + " FIGHT\n";
        if (letsFight(start, neighbor))
            combatLog += neighbor.toString() + " DIE\n";
        else combatLog += start.toString() + " DIE\n";
    }
    private synchronized void forwardGame(Position start, Position end) {
        Position next = field.findNext(start, end);
        combatLog += (start.toString() + " to " + next.toString() + '\n');
        Being en = field.getLiveBeing(start);
        en.setPosition(next);
        field.square[start.getX()][start.getY()].setNull();
        field.square[next.getX()][next.getY()].setBeing(en);
    }
    private synchronized void endGame(Being en) {
        if (en.getPara() == 1)
            combatLog += "Calabash win\n";
        else
            combatLog += "Soldiers win\n";
        fileReplay.writeFile(combatFile, combatLog);
        isGaming = false;
        field.qualified = false;
        guiControl.timeline.pause();
    }

    private void initAnimation() {
        grandpa.setParallel();
        grandpa.parallel.play();
        snake.setParallel();
        snake.parallel.play();
        for(Calabash c : brothers) c.setRotate();
        for(Soldiers s : fieldControl.maxArmy) s.setRotate();
        scorpion.setRotate();
    }
    private void initView() {
        for(Calabash c : brothers) c.setView();
        for(Soldiers s : fieldControl.maxArmy) s.setView();
        scorpion.setView();
        snake.setView();
        grandpa.setView();
    }
    public void initGame() {
        field.setTree();
        for(int i = 0; i < 7; i ++) brothers.add(new Calabash(i + 1));
        for(int i = 0; i < Formation.max; i ++) fieldControl.maxArmy.add(new Soldiers());
        Collections.shuffle(brothers);
        fieldControl.initField("鶴翼");
        this.initView();
        guiControl.refreshPane();
        this.initAnimation();
        guiControl.timeline.stop();
        guiControl.pane.setOnKeyPressed(new KeyParse());
    }

    private void playGame() {
        if(isGaming || isReplaying || !field.qualified) return;
        GameLogic.isGaming = true;
        this.tomb.clear();
        combatLog = "New Log\n" + "Game Start\n" + scorpion.getFormation().name + '\n';
        guiControl.timeline.playFromStart();
        guiControl.refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!GameLogic.isGaming) { System.gc(); cancel(); }
                Platform.runLater(() -> guiControl.refreshPane());
            }
        }, 100, 500);
        this.initClearThread();
        for (Calabash c : brothers)
            c.timer.scheduleAtFixedRate(getTask(c), 1000, 1000);
        scorpion.timer.scheduleAtFixedRate(getTask(scorpion), 1000, 1000);
        for (Soldiers s : soldiers)
            s.timer.scheduleAtFixedRate(getTask(s), 1000, 1000);
    }
    private VBox createNavigation() {
        Button buttonStart = new Button("Start");
        buttonStart.setOnAction((ActionEvent t) -> playGame());
        Button buttonLoad = new Button("Load");
        buttonLoad.setOnAction((ActionEvent t) -> fileReplay.replayGame());

        VBox navigate = new VBox(10);
        navigate.getChildren().addAll(buttonStart, buttonLoad);
        navigate.setLayoutX(30);
        navigate.setLayoutY(295);
        return navigate;
    }
    private synchronized void forceRestart(String formation) {
        if(isReplaying || isGaming) return;
        fieldControl.initField(formation);
        guiControl.timeline.stop();
        guiControl.refreshPane();
    }
    private VBox createFormation() {
        Button button1 = new Button("鶴翼");
        button1.setOnAction((ActionEvent t) -> forceRestart("鶴翼"));
        Button button2 = new Button("雁行");
        button2.setOnAction((ActionEvent t) -> forceRestart("雁行"));
        Button button3 = new Button("長蛇");
        button3.setOnAction((ActionEvent t) -> forceRestart("長蛇"));
        Button button4 = new Button("鋒矢");
        button4.setOnAction((ActionEvent t) -> forceRestart("鋒矢"));
        Button button5 = new Button("偃月");
        button5.setOnAction((ActionEvent t) -> forceRestart("偃月"));
        Button button6 = new Button("方门");
        button6.setOnAction((ActionEvent t) -> forceRestart("方门"));
        Button button7 = new Button("鱼鳞");
        button7.setOnAction((ActionEvent t) -> forceRestart("鱼鳞"));
        Button button8 = new Button("衡轭");
        button8.setOnAction((ActionEvent t) -> forceRestart("衡轭"));
        VBox formations = new VBox(10);
        formations.getChildren().addAll(
                button1, button2, button3, button4,
                button5, button6, button7, button8);
        formations.setLayoutX(900);
        formations.setLayoutY(198);
        return formations;
    }
}