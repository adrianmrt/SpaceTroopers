package dadm.scaffold.counter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import dadm.scaffold.R;


public class ScoreMenuFragment extends Fragment {
    ImageButton exitButton;
    FragmentManager fragmentManager;
    TextView scoreText;
    int score;

    public ScoreMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scoreText= view.findViewById(R.id.scoreText);
        exitButton=view.findViewById(R.id.ExitButtonScore);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMainMenu();
            }
        });
    }

    public void GoToMainMenu(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, MainMenuFragment.class,null);
        transaction.remove(this);
        transaction.commit();
    }
}