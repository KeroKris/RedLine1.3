package se.keroprog.redline.steptwo;


import com.googlecode.lanterna.terminal.Terminal;

/**
 * The basic monster, just chasing the player according to its super class' hunt method
 *
 * Created by Kristoffer on 9/15/2016.
 */
public class BasicMonster extends Monster {

    public BasicMonster(float x, float y) {
        super((int)x, (int)y);
        this.color = Terminal.Color.RED;
        this.skin = 'M';
    }


}
