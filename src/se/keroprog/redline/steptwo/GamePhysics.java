package se.keroprog.redline.steptwo;

import java.util.ArrayList;

/**
 * Class created to handle the physics of the game, like collisions
 *
 * Created by Kristoffer on 9/15/2016.
 */
public class GamePhysics {

    /**
     * Collision check for monsters.
     * @param targetX the X-Coordinate monster wants to move to
     * @param targetY the Y-Coordinate monster wants to move to
     * @param others the list of other monsters to check collision with
     * @param thisMonster the actual monster checking, specified to ignore himself in collision checks
     * @return returns true if a collision occurs and false if monster is all clear
     */
    public static boolean checkCollision(float targetX, float targetY, ArrayList<Monster> others, Entity thisMonster){

        // Checks collision with border first
        if(checkBorderCollision(targetX, targetY)){
            return true;
        }else{
            // Runs through the monsterList to stop collision with other monsters,
            // ignores himself in case of low speeds
            for (Monster otherMonster : others) {
                if(otherMonster != thisMonster){
                    if ((int)targetX == (int)otherMonster.xPos && (int)targetY == (int)otherMonster.yPos){
                        return true; // if collision occurs
                    }
                }
            }
        }
        return false; // if no collision occurs
    }

    /**
     * General Check for border collision, usable by anyone that has coordinates it wants to move to
     * @param targetX the X-coordinate wanted to check
     * @param targetY the Y-coordinate wanted to check
     * @return returns true if a collision occurs and false if path is clear
     */
    public static boolean checkBorderCollision(float targetX, float targetY){

        if((int)targetX < 0 || (int)targetX > Main.getBoardSize() ||
                (int)targetY < 0 || (int)targetY > Main.getBoardSize()){
            return true;
        }
        return false;
    }

}
