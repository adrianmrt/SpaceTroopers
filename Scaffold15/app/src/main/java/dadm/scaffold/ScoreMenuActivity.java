package dadm.scaffold;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import dadm.scaffold.counter.MainMenuFragment;

public class ScoreMenuActivity extends AppCompatActivity {
    ImageButton exitButton;
    TextView scoreText;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_menu);
        Intent intent= getIntent();
        score=intent.getIntExtra("score",0);
        scoreText= findViewById(R.id.scoreText);
        scoreText.setText(Integer.toString(score));
        exitButton= findViewById(R.id.ExitButtonScore);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMainMenu();
            }
        });
    }
    public void GoToMainMenu(){
        Intent mainMenuIntent= new Intent(this,ScaffoldActivity.class);
        startActivity(mainMenuIntent);
        this.finish();
    }

}