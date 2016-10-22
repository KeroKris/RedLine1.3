package se.keroprog.redline.steptwo;

/**
 * Class made to enable parsing and storing of the information in the highscore file to the highscore class.
 * Each individual score is treated as an object in a CopyOnWriteArrayList in the highscore class.
 *
 * Created by Kristoffer on 2016-10-20.
 */

public class Score {
    private int rank;
    private String name;
    private long score;

    public Score(int rank, String name, long score){

        this.rank = rank;
        this.name = name;
        this.score = score;
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString() {

        return String.format("%d.\t%s\t%d",rank, name, score);
    }
}
