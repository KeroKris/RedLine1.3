package se.keroprog.redline.steptwo;


/**
 * A charging monster, designed to acquire a target once at the players position and then charge toward that position
 * until it collides with a border or another monster. Then acquire a new target position and repeat.
 *
 * Created by Kristoffer on 9/15/2016.
 */
public class ChargingMonster extends Monster {

    private boolean hasTarget = false;
    private float startSpeed = 0.8f;

    /**
     * Constructor sets the starting position, the initial speed and the skin character
     * @param x Starting X-coordinate
     * @param y Starting Y-coordinate
     */
    public ChargingMonster(float x, float y) {
        super(x, y);
        this.speed = startSpeed;
        this.skin = '\u06DD';
    }

    @Override
    /**
     * The ChargingMonster's hunt method acquires a target if it has none and charges towards it.
     */
    public void hunt(Player player) {

        if(!hasTarget){
            // resets the speed and acquires new target
            speed = startSpeed;
            calcDirection(player.x, player.y);
            hasTarget = true;
        } else{
            charge();
        }
    }

    /**
     * Charges the target by moving in its set direction unless it collides with something,
     * picks up speed with every consecutive charge call for a target
     */
    public void charge(){

        if(!GamePhysics.checkCollision(x + xVelocity, y + yVelocity, Main.getMonsterList(), this)){
            move(xVelocity, yVelocity);
        } else {
            hasTarget = false;
        }
        // picks up speed
        speed += 0.2f;
    }
}
