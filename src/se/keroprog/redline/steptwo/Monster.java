package se.keroprog.redline.steptwo;


/**
 * The abstract Monster super class that all Monsters will inherit from
 *
 * Created by Kristoffer on 9/15/2016.
 */
public abstract class Monster extends Entity{
    //starting position and skin
    public float xPos, yPos;

    // variables handling movement
    public float xVelocity, yVelocity, speed;

    /**
     * Constructor for the Monster superclass,
     * for now sets the speed and skin in constructor
     * @param x takes a starting X-coordinate
     * @param y and a starting Y-coordinate
     */
    public Monster(int x, int y){

        super(x, y);
        this.xPos = x;
        this.yPos = y;
        this.speed = 0.7f;
        this.skin = 'A';

    }

    /**
     * The hunt method where all the fun happens.
     * The standard checks the position of the player and moves toward it
     * Expected to be overridden by subclasses
     * @param player the target player
     */
    public void hunt(Player player){
        // calculates the direction to move first
        calcDirection(player.x, player.y);
        // checks collisions
        if(!GamePhysics.checkCollision((int)(xPos + xVelocity),(int) (yPos + yVelocity),
                Main.getMonsterList(), this)){
            // if everything looks ok, calls the move method
            move(xVelocity, yVelocity);
        }
    }

    /**
     * Calculates the amount to move in either direction and
     * sets the xVelocity and yVelocity according to the angle and speed
     *
     * @param targetX The X-coordinate to calculate the direction to
     * @param targetY The Y-coordinate to calculate the direction to
     */
    public void calcDirection(int targetX, int targetY){
        float xDist, yDist;
        double totalDist;
        float totalAllowedMovement = speed;

        // Calculating the distances in absolute terms
        xDist = Math.abs(targetX - xPos);
        yDist = Math.abs(targetY - yPos);

        // Calculates the real distance in a vector and derives the xVelocity and the yVelocity from the vector
        totalDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        xVelocity = (float)  (speed * xDist / totalDist);
        yVelocity = totalAllowedMovement - xVelocity;

        // Negates the values if target is to the left of, and/or above the monster
        if(targetX < xPos){
            xVelocity *= -1;
        }
        if(targetY < yPos){
            yVelocity *= -1;
        }
    }

    /**
     * The basic move function that updates the position for monsters according to its input
     * @param xMove the X amount wanted to move
     * @param yMove the Y amount wanted to move
     */
    public void move(float xMove, float yMove){
        xPos += xMove;
        yPos += yMove;
    }



}
