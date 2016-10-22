package se.keroprog.redline.steptwo;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import com.sun.org.apache.regexp.internal.RE;

import javax.smartcardio.TerminalFactory;
import javax.swing.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {
    private static int boardSize = 20;
    private static ArrayList<Monster> monsterList = new ArrayList<>();
    private static int turn = 1, nextGoodieTurn;
    private static boolean monsterSpawn = false;
    private static ArrayList<Goodie> goodieList;
    private static double maxTurnsUntilGoodie = 100;
    private static Player player1;
    private static Terminal terminal;
    private static Highscore highscore;
    private static boolean newHighscore;
    private static int newHighscoreRank;

    public static void main(String[] args) throws InterruptedException {

        // Sets up the terminal window
        terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        // Initializing starting values
        boolean gameOver = false;

//        Terminal

        highscore = new Highscore();

        // Initializing the player and the starting enemies
        player1 = new Player(10,10);

        Monster enemy1 = new BasicMonster(0,0);
        Monster enemy2 = new ChargingMonster(0,20);
        Monster enemy3 = new ChargingMonster(20,0);
        Monster enemy4 = new ChargingMonster(20,20);
        Monster enemy5 = new ChargingMonster(10,20);

        // sets the turn for the first goodie to spawn
        nextGoodieTurn = (int) (Math.random()*maxTurnsUntilGoodie) + 2;
        System.out.println("next goodie turn is: " + nextGoodieTurn);
        goodieList = new ArrayList<>();

        // populating the monsterList
        monsterList.add(enemy1);
        monsterList.add(enemy2);
        monsterList.add(enemy3);
        monsterList.add(enemy4);
        monsterList.add(enemy5);

        // Main game loop
        while(true){

            if (!gameOver){  // Until game over, will update screen and let player move
                updateScreen();

                // Player movement
                player1.move();

                // Runs the AI for all the monsters in the monsterList
                // Sets gameOver to true if a monster catches player
                gameOver = gameLogic();

                //runs the game over event if a monster caught player
                if(gameOver) {
                    gameOverEvent();
                }

                // Adds a new BasicMonster every 20 turns,
                // except for every 100 turns where a ChargingMonster will be added

                if(turn % 100 == 0){
                    spawnChargingMonster();
                } else if(turn % 20 == 0){
                    spawnBasicMonster();
                }


                // spawns a new goodie if the time has come
                if (turn == nextGoodieTurn){
                    spawnGoodie();
                }
            }
        }
    }

    private static void spawnBasicMonster() {

        BasicMonster tempMonster = new BasicMonster(0,0);
        // Checks collision to make sure spawn location isn't occupied
        if(!GamePhysics.checkCollision(0,0,monsterList,(tempMonster))){

            monsterList.add(tempMonster);
            monsterSpawn = true;
        }
    }

    private static void spawnChargingMonster() {

        ChargingMonster tempMonster = new ChargingMonster(20,20);
        if(!GamePhysics.checkCollision(20,20,monsterList,tempMonster)){

            monsterList.add(tempMonster);
        }
        monsterSpawn = true;

    }


    /**
     * prompts the player for a name if player made the highscore list
     *  updates the screen one last time to show the enemy catching the player
     * and prints the game over message as well as the updated highscore list
     */
    private static void gameOverEvent() {

        newHighscore = false;
        Score tempScore = null;
        // loops through all the highscores to see if the new score qualifies
        for (Score s :
                highscore.getCurrentHighscore()) {

            int highscoreIndex = highscore.getCurrentHighscore().indexOf(s);

            if(player1.getScore() > s.getScore() && !newHighscore){

                // prompts the player for his name
                String name = JOptionPane.showInputDialog("Please enter your name: ");

                // safeguards against names that would mess up the highscore board or file
                if(name.length() > 12 || name.contains(",")){
                    name = "default";
                }
                newHighscoreRank = highscoreIndex;
                newHighscore = true;
                // saves the tempScore to be placed in the highscore list.
                tempScore = new Score(highscoreIndex + 1, name, player1.getScore());
            }
            if(newHighscore){
                // changes the rank on the remaining scores to their proper number
                s.setRank(s.getRank()+1);
            }
        }

        if(tempScore != null){
            // adds the tempScore to the highscore CopyOnWriteArrayList
            highscore.addHighscore(tempScore.getRank()-1, tempScore);
        }

        // updates the screen one last time with the new highscore
        // saves the highscore to the txt file and writes the game over message
        updateScreen();
        highscore.writeToFile();
        printText(10, 10, "Game Over!", Terminal.Color.WHITE);
    }

    /**
     * printText lets the game print a text at the specified starting position
     * @param x X-coordinate for the first character
     * @param y Y-coordinate for the first character
     * @param s String to be printed
     */
    private static void printText(int x, int y, String s, Terminal.Color color) {

        Main.getTerminal().applyForegroundColor(color);
        // Loops through the String and prints every character
        for (int i = 0; i < s.length(); i++) {
            Main.getTerminal().moveCursor(x + i, y);
            Main.getTerminal().putCharacter(s.charAt(i));
        }
        Main.getTerminal().moveCursor(0,0);
        Main.getTerminal().applyForegroundColor(Terminal.Color.WHITE);
    }


    /**
     * Prints a list of Strings to the screen
     * @param startX X-coordinate for the first character of the first String
     * @param startY Y-coordinate for the first character of the first String
     * @param stringArray The String Array to be printed
     */
    public static void printTextList(int startX, int startY, String[] stringArray, Terminal.Color color){

        for (int i = 0; i < stringArray.length; i++) {
            printText(startX, startY + i, stringArray[i], color);
        }
        Main.getTerminal().moveCursor(0,0);
    }

    /**
     * Activates the monsters and checks if any monsters caught the player
     * @return  returns true if the enemy catches the player
     */
    private static boolean gameLogic() {

        for (Monster monster: monsterList) {
            // calls the specific monster types' hunt method
            monster.hunt(player1);

            // returns true if the enemy has caught the player
            if (player1.x == (int) monster.xPos && player1.y == (int) monster.yPos){
                return true;
            }
        }
        return false; // otherwise returns false
    }


    /**
     * Updates the screen. Draws the player and the enemy to the terminal screen
     */
    private static void updateScreen() {

        Terminal terminal = Main.getTerminal();

        terminal.clearScreen();  // clears the screen before drawing anything

        // Draws the game border in red to establish a more hostile environment
        int gameBorder = boardSize +1;
        for (int i = 0; i < gameBorder +1; i++) {
            terminal.applyForegroundColor(Terminal.Color.RED);
            terminal.moveCursor(gameBorder, i);
            terminal.putCharacter('X');
            terminal.moveCursor(i, gameBorder);
            terminal.putCharacter('X');
            terminal.applyForegroundColor(Terminal.Color.WHITE);
        }

        // Prints the turn tracker. Can you beat your personal record? ;)
        // Also prints the amount of enemies, current score and the highscore list.
        printText(0,22, "Turn: " + turn + "   Enemies: " + monsterList.size(), Terminal.Color.WHITE);
        printText(0, 24,"your Current score is: " + player1.getScore(), Terminal.Color.WHITE);
        printText(29, 2, "HIGHSCORE", Terminal.Color.WHITE);
        printTextList(25, 4, highscore.getHighScoreAsStringArray(), Terminal.Color.WHITE);

        // highlights the new highscore on the list
        if (newHighscore){
            printText(47,4 + newHighscoreRank, "<--- New Highscore!!", Terminal.Color.WHITE);
        }

        // Prints a message if a monster spawned this turn
        if(monsterSpawn){
            printText(2, 23, "Monster Spawned!", Terminal.Color.RED);
            monsterSpawn = false;
        }

        //Updates and draws the goodies on the map
        updateGoodies();

        // Draws the player as an Ö
        player1.draw();

        // Draws all the monsters from the monsterList with their specific skin
        monsterList.forEach(Monster::draw);

    }


    /**
     * Spawns a new Goodie that the player can pick up for extra points
     */
    public static void spawnGoodie(){

        // randomizes a position on the board for the goodie to spawn
        int randomX = (int) (Math.random()*(boardSize-1));
        int randomY = (int) (Math.random()*(boardSize-1));

        // adds the goodie to the goodieList if it doesn't collide with a monster or the player
        if(!GamePhysics.checkCollision(0,0,monsterList, player1)){

            goodieList.add(new Goodie(randomX, randomY, 1000 + (turn /100)*250, turn + 25));
            nextGoodieTurn += Math.random()*maxTurnsUntilGoodie + 1;
        }else{
            // otherwise tries again next turn
            nextGoodieTurn++;
        }
        System.out.println(nextGoodieTurn);
    }

    /**
     * Updates all the goodies, deletes them if they are picked up or expires.
     */
    public static void updateGoodies(){

        Goodie expireGoodie = null;
        Goodie scoreGoodie = null;
        for (Goodie g :
                goodieList) {
            if(g.getLifeSpan() == turn){
                expireGoodie = g;
            }
            if(g.getX()== player1.getX() && g.getY() == player1.getY() ){
                scoreGoodie = g;
            }
            g.draw();
        }
        if(expireGoodie != null){
            goodieList.remove(expireGoodie);
        }
        if(scoreGoodie != null){
            player1.scoreReward(scoreGoodie.getReward());
            printText(25, 20, "you scored a goodie for: " + scoreGoodie.getReward() + " points!", Terminal.Color.YELLOW);
            goodieList.remove(scoreGoodie);
        }
    }


    public static Terminal getTerminal() {
        return terminal;
    }

    /**
     * Getter for the monsterList
     * @return returns the monsterList
     */
    public static ArrayList<Monster> getMonsterList(){

        return monsterList;
    }

    public static void endTurn(){
        turn++;
    }

    /**
     * Getter for the board size
     * @return the board size
     */
    public static int getBoardSize() {
        return boardSize;
    }
}
