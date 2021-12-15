package dadm.scaffold.counter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;

public class PauseMenuFragment extends BaseFragment {

    GameEngine gameEngine;
    Button resumeButton;
    Button exitButton;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pause_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager= getParentFragmentManager();
        resumeButton=view.findViewById(R.id.ResumeButton);
        exitButton=view.findViewById(R.id.ExitButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResumeGame();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitGame();
            }
        });
    }

    public void ResumeGame(){
        SetNextState("resume");
        DestroyPauseMenuFragment();
    }

    public void ExitGame(){
        SetNextState("exit");
        DestroyPauseMenuFragment();

    }

    private void DestroyPauseMenuFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(PauseMenuFragment.this);
        transaction.commit();
    }

    private void SetNextState(String nextState){
        Bundle bundle= new Bundle();
        bundle.putString("pauseMenuState",nextState);
        getParentFragmentManager().setFragmentResult("handlePauseMenu",bundle);
    }
}