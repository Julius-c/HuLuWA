import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import static org.junit.Assert.*;

public class FieldTest {
    private Field field = new Field();
    private ArrayList<Calabash> brothers = new ArrayList<>();
    private ArrayList<Soldiers> soldiers = new ArrayList<>();
    private Grandpa grandpa = new Grandpa(new Position(5,1));
    private Snake snake = new Snake(new Position(5, 14));
    private Scorpion scorpion = new Scorpion();
    @Test
    public void test() {
        AboveAll God = new AboveAll(field, brothers, soldiers, grandpa, snake, scorpion);
        for(int i = 0; i < 7; i ++) brothers.add(new Calabash(i + 1));
        for(int i = 0; i < Formation.max; i ++) AboveAll.maxArmy.add(new Soldiers());
        Collections.shuffle(brothers);
        God.initField("鶴翼");
        assertTrue(field.qualified);
        assertFalse(field.posQualified(new Position(-1, -1)));
        assertFalse(field.posQualified(new Position(11, 16)));
        assertTrue(field.emptySquare(brothers.get(0).position.left()));
        assertTrue(field.isOpposite(brothers.get(0).position, scorpion.position));
        assertFalse(field.isOpposite(brothers.get(0).position, brothers.get(1).position));
        System.out.println("Test Pass");
    }
}