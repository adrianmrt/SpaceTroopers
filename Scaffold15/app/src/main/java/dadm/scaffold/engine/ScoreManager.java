package dadm.scaffold.engine;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import dadm.scaffold.R;

public class ScoreManager {
    private int currentScore;
    private int scoreToAdd;
    TextView scoreText;
    public ScoreManager(){
        this.currentScore=0;
    }

    public void setCurrentScore(int actualScore) {
        this.currentScore = actualScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setScoreToAdd(int scoreToAdd) {
        this.scoreToAdd = scoreToAdd;
    }

    public int getScoreToAdd() {
        return scoreToAdd;
    }

    public void setScoreText(TextView scoreText) {
        this.scoreText = scoreText;
        scoreText.setText(Integer.toString(currentScore));
    }

    public void resetScore(){
        currentScore=0;
    }

    //runnable for being able to change the ui from the main thread
    public Runnable AddScore= new Runnable() {
        @Override
        public void run() {
            currentScore += scoreToAdd;
            scoreText.setText(Integer.toString(currentScore));
        }
    };

}
