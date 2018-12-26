import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * 安置战场的逻辑
 * @author CC
 */
public class AboveAll {
    private Field field;
    private ArrayList<Calabash> brothers;
    private ArrayList<Soldiers> soldiers;
    private Grandpa grandpa;
    private Snake snake;
    private Scorpion scorpion;
    public static ArrayList<Soldiers> maxArmy = new ArrayList<>();

    AboveAll(Field field, ArrayList<Calabash> brothers, ArrayList<Soldiers> soldiers, Grandpa grandpa, Snake snake, Scorpion scorpion) {
        this.field = field;
        this.brothers = brothers;
        this.soldiers = soldiers;
        this.grandpa =grandpa;
        this.snake = snake;
        this.scorpion = scorpion;
    }
    public void initField(String formation) {
        field.clearField();
        scorpion.setFormation(formation);
        this.setAlive();
        this.setField();
        field.qualified = true;
    }
    private void setAlive() {
        for(Calabash c : brothers) c.alive = true;
        for(Soldiers s : soldiers) s.alive = true;
        scorpion.alive = true;
    }
    private void setField() {
        setBrothers(brothers);
        setSoldiers(soldiers);
        setNPC();
    }
    private void setNPC() { //放置NPC人物 爷爷和蛇精
        field.square[grandpa.position.getX()][grandpa.position.getY()].setBeing(grandpa);
        field.square[snake.position.getX()][snake.position.getY()].setBeing(snake);
    }
    private void setBrothers(ArrayList<Calabash> brothers) { //依据長蛇阵型放置 葫芦兄弟
        Formation formation = new Formation("長蛇");
        formation.leader = new Position(2, 1);
        for(int i = 0; i < 7; i ++) {
            brothers.get(i).setPosition(new Position(formation.leader.getX() + formation.pos.get(i).getX(),
                    formation.leader.getY() + formation.pos.get(i).getY()));
            field.square[brothers.get(i).position.getX()][brothers.get(i).position.getY()].setBeing(brothers.get(i));
        }
    }
    private void setSoldiers(ArrayList<Soldiers> soldiers) { //依据蝎子精的安排放置 喽啰阵营
        Formation formation = scorpion.getFormation();
        formation.leader = new Position(2, 8);
        soldiers.clear();
        for (int i = 0; i < formation.pos.size() - 1; i++)
            soldiers.add(maxArmy.get(i));
        scorpion.position = new Position(formation.leader.getX() + formation.pos.get(0).getX(), formation.leader.getY() + formation.pos.get(0).getY());
        field.square[scorpion.position.getX()][scorpion.position.getY()].setBeing(scorpion); // 蝎子
        for (int i = 1; i < formation.pos.size(); i++)
            soldiers.get(i - 1).setPosition(new Position(formation.leader.getX() + formation.pos.get(i).getX(),
                    formation.leader.getY() + formation.pos.get(i).getY()));
        for (Soldiers s : soldiers) {
            field.square[s.position.getX()][s.position.getY()].setBeing(s);
        }
    }
}