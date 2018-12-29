import org.junit.Test;

import Sort.SortCala;
import Being.Calabash;
import static org.junit.Assert.*;

public class SortTest {
    private SortCala sort = new SortCala();
    private Calabash[] brothers = new Calabash[7];
    @Test
    public void test() {
        for(int i = 0; i < 7; i ++)
            brothers[i] = new Calabash(i + 1);
        sort.De_sort(brothers);
        sort.BubbleSort_byName(brothers);
        for(int i = 0; i < 7; i ++)
            assertEquals(brothers[i].getRank(), i + 1);
        sort.De_sort(brothers);
        sort.BinarySort_byColor(brothers);
        for(int i = 0; i < 7; i ++)
            assertEquals(brothers[i].getRank(), i + 1);
    }
}
