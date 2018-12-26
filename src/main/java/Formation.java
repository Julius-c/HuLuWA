import java.util.ArrayList;

/**
 * 阵型类
 * 共"鶴翼", "雁行", "長蛇", "鋒矢", "偃月", "方门", "鱼鳞", "衡轭"八种阵型
 * @author CC
 */

public class Formation {
    public static Integer max = 18;
    public String name;
    public Position leader;
    public ArrayList<Position> pos = new ArrayList<>();
    Formation(String name) {
        this.name = name;
        switch(name) {
            case "鶴翼":
                pos.add(new Position(3, 1));
                pos.add(new Position(2, 2));
                pos.add(new Position(4, 2));
                pos.add(new Position(1, 3));
                pos.add(new Position(5, 3));
                pos.add(new Position(0, 4));
                pos.add(new Position(6, 4));
                break;
            case "雁行":
                pos.add(new Position(0, 0));
                pos.add(new Position(1, 1));
                pos.add(new Position(2, 2));
                pos.add(new Position(3, 3));
                pos.add(new Position(4, 4));
                pos.add(new Position(5, 5));
                pos.add(new Position(6, 6));
                break;
            case "長蛇":
                pos.add(new Position(0,3));
                pos.add(new Position(1,3));
                pos.add(new Position(2,3));
                pos.add(new Position(3,3));
                pos.add(new Position(4,3));
                pos.add(new Position(5,3));
                pos.add(new Position(6,3));
                break;
            case "衡轭":
                pos.add(new Position(3, 1));
                pos.add(new Position(1, 2));
                pos.add(new Position(0, 3));
                pos.add(new Position(3, 2));
                pos.add(new Position(2, 3));
                pos.add(new Position(5, 2));
                pos.add(new Position(4, 3));
                pos.add(new Position(6, 3));
                break;
            case "偃月":
                pos.add(new Position(3, 0));
                pos.add(new Position(2, 0));
                pos.add(new Position(4, 0));
                pos.add(new Position(3, 1));
                pos.add(new Position(2, 1));
                pos.add(new Position(4, 1));
                pos.add(new Position(3, 2));
                pos.add(new Position(2, 2));
                pos.add(new Position(4, 2));
                pos.add(new Position(1, 2));
                pos.add(new Position(5, 2));
                pos.add(new Position(1, 3));
                pos.add(new Position(5, 3));
                pos.add(new Position(0, 3));
                pos.add(new Position(6, 3));
                pos.add(new Position(0, 4));
                pos.add(new Position(6, 4));
                pos.add(new Position(-1, 5));
                pos.add(new Position(7, 5));
                break;
            case "方门":
                pos.add(new Position(3, 1));
                pos.add(new Position(2, 2));
                pos.add(new Position(4, 2));
                pos.add(new Position(1, 3));
                pos.add(new Position(5, 3));
                pos.add(new Position(2, 4));
                pos.add(new Position(4, 4));
                pos.add(new Position(3, 5));
                break;
            case "鋒矢":
                pos.add(new Position(3, 1));
                pos.add(new Position(2, 2));
                pos.add(new Position(3, 2));
                pos.add(new Position(4, 2));
                pos.add(new Position(1, 3));
                pos.add(new Position(3, 3));
                pos.add(new Position(5, 3));
                pos.add(new Position(0, 4));
                pos.add(new Position(3, 4));
                pos.add(new Position(6, 4));
                break;
            case "鱼鳞":
                pos.add(new Position(3, 1));
                pos.add(new Position(2, 2));
                pos.add(new Position(1, 3));
                pos.add(new Position(3, 3));
                pos.add(new Position(5, 3));
                pos.add(new Position(0, 4));
                pos.add(new Position(2, 4));
                pos.add(new Position(4, 4));
                pos.add(new Position(6, 4));
                pos.add(new Position(3, 5));
                break;
            default: System.out.println("蝎子精还不会这种阵型");
        }
    }
}
