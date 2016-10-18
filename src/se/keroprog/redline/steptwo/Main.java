package se.keroprog.redline.steptwo;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {
    private static int boardSize = 20;
    private static ArrayList<Monster> monsterList = new ArrayList<>();
    private static int turn = 1;
    static boolean monsterSpawn = false;

    public static void main(String[] args) throws InterruptedException {

        // Sets up the terminal window
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        // Initializing starting values
        boolean gameOver = false;


        // Initializing the player and the starting enemies
        Player player1 = new Player(10,10);

        Monster enemy1 = new BasicMonster(0,0);
        Monster enemy2 = new ChargingMonster(0,20);
        Monster enemy3 = new ChargingMonster(20,0);
        Monster enemy4 = new ChargingMonster(20,20);
        Monster enemy5 = new ChargingMonster(10,20);


        // populating the monsterList
        monsterList.add(enemy1);
        monsterList.add(enemy2);
        monsterList.add(enemy3);
        monsterList.add(enemy4);
        monsterList.add(enemy5);

        // Main game loop
        while(true){

            if (!gameOver){  // Until game over, will update screen and let player move
                updateScreen(player1, monsterList, terminal);

                // Player movement
                movePlayer(player1, terminal);

                // Runs the AI for all the monsters in the monsterList
                // Sets gameOver to true if an enemy catches player
                gameOver = gameLogic(player1, monsterList);

                // updates the screen one last time to show the enemy catching the player
                // and prints the game over message
                if(gameOver) {
                    updateScreen(player1, monsterList, terminal);
                    printText(10, 10, "Game Over!", terminal);
                }

                // Adds a new BasicMonster every 20
                // except for every 100 turns where a ChargingMonster will be added-
                if(turn % 100 == 0){
                    monsterList.add(new ChargingMonster(20,20));
                    monsterSpawn = true;
                } else if(turn % 20 == 0){
                    BasicMonster tempMonster = new BasicMonster(0,0);
                    // Checks collision to make sure spawn location isn't occupied
                    if(!GamePhysics.checkCollision(0,0,monsterList,(tempMonster))){

                        monsterList.add(tempMonster);
                        monsterSpawn = true;
                    }
                }
            }
        }
    }

    /**
     * printText let's the game print a text at the specified starting position
     * @param x X-coordinate for the first character
     * @param y Y-coordinate for the first character
     * @param s String to be printed
     * @param terminal terminal to print the text
     */
    private static void printText(int x, int y, String s, Terminal terminal) {
        terminal.moveCursor(x, y);
        // Loops through the String and prints every character
        for (int i = 0; i < s.length(); i++) {
            terminal.moveCursor(x + i, y);
            terminal.putCharacter(s.charAt(i));
        }
        terminal.moveCursor(0,0);
    }

    /**
     * Activates the monsters and checks if any monsters caught the player
     * @param player1   the player
     * @param monsterList    the list of enemies
     * @return  returns true if the enemy catches the player
     */
    private static boolean gameLogic(Player player1, ArrayList<Monster> monsterList) {

        for (Monster monster: monsterList) {
            // calls the specific monster types' hunt method
            monster.hunt(player1);

            // returns true if the enemy has caught the player
            if (player1.x == (int) monster.x && player1.y == (int) monster.y){
                return true;
            }
        }
        return false; // otherwise returns false
    }


    /**
     * Updates the screen. Draws the player and the enemy to the terminal screen
     * @param player the player
     * @param monsterList the enemies
     * @param terminal the terminal
     */
    private static void updateScreen(Player player, ArrayList<Monster> monsterList, Terminal terminal) {
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
        // Also prints the amount of enemies on the screen
        printText(0,22, "Turn: " + turn + "   Enemies: " + monsterList.size(), terminal);

        // Prints a message if a monster spawned this turn
        if(monsterSpawn){
            printText(2, 23, "Monster Spawned!", terminal);
            monsterSpawn = false;
        }


        // Draws the player as an Ö
        terminal.moveCursor(player.x, player.y);
        terminal.putCharacter('Ö');

        // Draws all the monsters from the monsterList with their specific skin
        terminal.applyForegroundColor(Terminal.Color.RED); // Makes enemies red
        for (Monster monster: monsterList) {
            terminal.moveCursor((int) monster.x, (int) monster.y);
            terminal.putCharacter(monster.skin);
        }
        terminal.applyForegroundColor(Terminal.Color.WHITE); // Resets the color to white
        terminal.moveCursor(0,0); // resets cursor to default
    }

    /**
     * Method to handle player input and move the player accordingly
     * @param player the player
     * @param terminal the terminal
     * @throws InterruptedException
     */
    public static void movePlayer(Player player, Terminal terminal) throws InterruptedException {

        //wait for a key to be pressed
        Key key;
        do{
            Thread.sleep(5);
            key = terminal.readInput();

            // Moves the player according to key input as long as within board size
            if(key != null){
                switch (key.getKind()){
                    case ArrowUp:
                        if(!GamePhysics.checkBorderCollision(player.x, player.y-1)){    // checks hit
                            player.y--;                                                 // moves player
                        }
                        break;
                    case ArrowDown:
                        if(!GamePhysics.checkBorderCollision(player.x, player.y +1)){
                            player.y++;
                        }
                        break;
                    case ArrowLeft:
                        if(!GamePhysics.checkBorderCollision(player.x -1, player.y)){
                            player.x--;
                        }
                        break;
                    case ArrowRight:
                        if(!GamePhysics.checkBorderCollision(player.x +1, player.y)){
                            player.x++;
                        }
                        break;
                }
                turn++;
            }

        }
        while(key == null);
    }

    /**
     * Getter for the monsterList
     * @return returns the monsterList
     */
    public static ArrayList<Monster> getMonsterList(){

        return monsterList;
    }

    /**
     * Getter for the board size
     * @return the board size
     */
    public static int getBoardSize() {
        return boardSize;
    }
}
