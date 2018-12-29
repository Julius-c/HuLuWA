package Field;

import Being.Being;

/**
 * 方块类, 战场类的基本组成单元
 * @author CC
 */
public class Square {

    private Being being;
    Square() {}
    public void setBeing(Being being) {
        if(isEmpty()) this.being = being;
    }
    public void setNull() {
        this.being = null;
    }
    public Being getBeing() {
        return this.being;
    }

    private boolean isEmpty() {
        return (this.being == null);
    }
}
