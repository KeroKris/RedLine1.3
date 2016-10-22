package se.keroprog.redline.steptwo;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Goodie that player can pick up to scoreGoodie extra points
 *
 * Created by Kristoffer on 2016-10-19.
 */
public class Goodie extends Entity{

    private int reward, lifeSpan;

    public Goodie(int x, int y, int reward, int lifeSpan){

        super(x,y);
        this.reward = reward;
        this.lifeSpan = lifeSpan;
        this.skin = '$';
        this.color = Terminal.Color.YELLOW;

    }


    public int getLifeSpan() {
        return lifeSpan;
    }

    public void expire(){

    }

    public int getReward() {
        return reward;
    }
}
