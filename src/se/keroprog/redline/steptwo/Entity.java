package se.keroprog.redline.steptwo;

/**
 * Created by Kristoffer on 2016-10-19.
 */
public class Entity {

    protected int x, y;
    protected char skin;

    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSkin() {
        return skin;
    }
}
