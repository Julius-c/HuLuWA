package Field;

import Being.*;
import Formation.Formation;
import Position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * 安置战场的逻辑
 * @author CC
 */
public class FieldControl {

    private Field field;
    private ArrayList<Calabash> brothers;
    private ArrayList<Soldiers> soldiers;
    private Grandpa grandpa;
    private Snake snake;
    private Scorpion scorpion;
    private ArrayList<Integer> randArray = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));

    public ArrayList<Soldiers> maxArmy = new ArrayList<>();

    public FieldControl(Field field, ArrayList<Calabash> brothers, ArrayList<Soldiers> soldiers, Grandpa grandpa, Snake snake, Scorpion scorpion) {
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
        for(Calabash c : brothers) c.live();
        for(Soldiers s : soldiers) s.live();
        scorpion.live();
    }

    private void setField() {
        setBrothers(brothers);
        setSoldiers(soldiers);
        setNPC();
    }

    private void setNPC() {
        field.square[grandpa.getPosition().getX()][grandpa.getPosition().getY()].setBeing(grandpa);
        field.square[snake.getPosition().getX()][snake.getPosition().getY()].setBeing(snake);
    }

    private void setBrothers(ArrayList<Calabash> brothers) { //依据長蛇阵型放置 葫芦兄弟
        Formation formation = new Formation("長蛇");
        formation.leader = new Position(2, 1);
        for(int i = 0; i < 7; i ++) {
            brothers.get(i).setPosition(new Position(formation.leader.getX() + formation.pos.get(i).getX(),
                    formation.leader.getY() + formation.pos.get(i).getY()));
            field.square[brothers.get(i).getPosition().getX()][brothers.get(i).getPosition().getY()].setBeing(brothers.get(i));
        }
    }

    private void setSoldiers(ArrayList<Soldiers> soldiers) { //依据蝎子精的安排放置 喽啰阵营
        Formation formation = scorpion.getFormation();
        formation.leader = new Position(2, 8);
        soldiers.clear();
        for (int i = 0; i < formation.pos.size() - 1; i++)
            soldiers.add(maxArmy.get(i));
        scorpion.setPosition(new Position(formation.leader.getX() + formation.pos.get(0).getX(), formation.leader.getY() + formation.pos.get(0).getY()));
        field.square[scorpion.getPosition().getX()][scorpion.getPosition().getY()].setBeing(scorpion); // 蝎子
        for (int i = 1; i < formation.pos.size(); i++)
            soldiers.get(i - 1).setPosition(new Position(formation.leader.getX() + formation.pos.get(i).getX(),
                    formation.leader.getY() + formation.pos.get(i).getY()));
        for (Soldiers s : soldiers) {
            field.square[s.getPosition().getX()][s.getPosition().getY()].setBeing(s);
        }
    }

    public synchronized Position chooseEnemy(Being en) {
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

    public synchronized boolean letsFight(Position a, Position b) {
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
}