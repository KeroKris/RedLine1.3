package se.keroprog.redline.steptwo;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by Kristoffer on 10/7/2016.
 *
 */
public class Highscore {

    private CopyOnWriteArrayList<Score> currentHighscore = new CopyOnWriteArrayList<>();



    public Highscore(){

        currentHighscore = loadFile();
    }

    public CopyOnWriteArrayList<Score> loadFile(){
        CopyOnWriteArrayList<Score> highscore = new CopyOnWriteArrayList<>();

        String scoreLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader("highscore.txt"));
            int i = 0;
            while((scoreLine = br.readLine()) != null){

                String[] scoreComponents = scoreLine.split(",");
                highscore.add(new Score(Integer.parseInt(scoreComponents[0]), scoreComponents[1], Long.parseLong(scoreComponents[2])));
                i++;

            }
//            for (int i = 0; i < 10; i++) {
//                highscore[i] = scoreLine;
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highscore;
    }

    public CopyOnWriteArrayList<Score> getCurrentHighscore() {
        return currentHighscore;
    }

    public void writeToFile(){

        String highscoreData = "";

        for (Score score :
                currentHighscore) {
            highscoreData += score.getRank() + "," + score.getName() +  "," + score.getScore() + "\n";
        }

        File file = new File("highscore.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(highscoreData);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addHighscore(int index, Score newScore){

        currentHighscore.add(index, newScore);
        currentHighscore.remove(10);

    }

    public String[] getHighScoreAsStringArray(){
       String[] highscore = new String[10];

        for (Score s :
                currentHighscore) {
            highscore[currentHighscore.indexOf(s)] = s.toString();
        }

        return highscore;
    }
}
