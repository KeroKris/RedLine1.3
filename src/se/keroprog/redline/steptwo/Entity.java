package se.keroprog.redline.steptwo;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Created by Kristoffer on 2016-10-19.
 */
public class Entity {

    protected int x, y;
    protected char skin;
    protected Terminal.Color color;

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

    public void draw(){

        Main.getTerminal().applyForegroundColor(color);
        Main.getTerminal().moveCursor(x, y);
        Main.getTerminal().putCharacter(skin);
    }
}
