package se.keroprog.redline.steptwo;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * Basic player class, only contains its coordinates for now.
 * Will eventually probably create a move method to handle player movement
 * and make its coordinates private and create Getters and Setters for them
 *
 * Created by Kristoffer on 9/15/2016.
 */
public class Player extends Entity{

    private int score, scorePerTurn;

    public Player(int x, int y){
        super(x,y);

        this.x = x;
        this.y = y;
        this.skin = 'X';
        this.score = 0;
        this.scorePerTurn = 10;
        this.color = Terminal.Color.WHITE;
    }


    public int getScore() {
        return score;
    }

    public void turnScore(){
        increaseScore(scorePerTurn);
    }

    public void increaseScore(int scoreIncrease){
        score += scoreIncrease;
    }

    public void scoreReward(int reward) {
        increaseScore(reward);
    }

    /**
     * Method to handle player input and move the player accordingly
     * @throws InterruptedException
     */
    public void move() throws InterruptedException {

        //wait for a key to be pressed
        Key key;
        do{
            Thread.sleep(5);
            key = Main.getTerminal().readInput();

            // Moves the player according to key input as long as within board size
            if(key != null){
                switch (key.getKind()){
                    case ArrowUp:
                        if(!GamePhysics.checkBorderCollision(x, y-1)){    // checks hit
                            y--;                                                 // moves player
                        }
                        break;
                    case ArrowDown:
                        if(!GamePhysics.checkBorderCollision(x, y +1)){
                            y++;
                        }
                        break;
                    case ArrowLeft:
                        if(!GamePhysics.checkBorderCollision(x -1, y)){
                            x--;
                        }
                        break;
                    case ArrowRight:
                        if(!GamePhysics.checkBorderCollision(x +1, y)){
                            x++;
                        }
                        break;
                }
                turnScore();
                Main.endTurn();
            }

        }
        while(key == null);
    }
}
