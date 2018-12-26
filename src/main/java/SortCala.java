import java.util.Random;

/**
 * 葫芦娃排序类, 继承之前作业而来
 * @author CC
 */
public class SortCala {
    private void RollCall_byName(Calabash[] brothers) {
        for(Calabash b : brothers)
            System.out.print(b.name + ' ');
        System.out.println();
    }

    private void RollCall_byColor(Calabash[] brothers) {
        for(Calabash b : brothers)
            System.out.print(b.getColor().toString() +  ' ');
        System.out.println();
    }

    public void De_sort(Calabash[] brothers) {
        Random rand = new Random();
        for(int i = 6; i > 0; i --) {
            int p = rand.nextInt(i);
            Calabash tmp = brothers[i];
            brothers[i] = brothers[p];
            brothers[p] = tmp;
        }
    }

    public void BubbleSort_byName(Calabash[] brothers) {
        for(int i = 0; i < brothers.length - 1; i ++) {
            for (int j = 0; j < brothers.length - 1 - i; j++)
                if (brothers[j].getRank() > brothers[j + 1].getRank()) {
                    Calabash temp = brothers[j];
                    brothers[j] = brothers[j + 1];
                    brothers[j + 1] = temp;
                }
        }
    }
}
