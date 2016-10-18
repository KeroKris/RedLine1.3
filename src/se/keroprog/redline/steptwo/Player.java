package se.keroprog.redline.steptwo;

/**
 * Basic player class, only contains its coordinates for now.
 * Will eventually probably create a move method to handle player movement
 * and make its coordinates private and create Getters and Setters for them
 *
 * Created by Kristoffer on 9/15/2016.
 */
public class Player {
    public int x, y;

    public Player(int x, int y){

        this.x = x;
        this.y = y;
    }
}
