package dadm.scaffold.counter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.ScoreMenuActivity;
import dadm.scaffold.engine.FramesPerSecondCounter;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameView;
import dadm.scaffold.engine.ScoreManager;
import dadm.scaffold.input.JoystickInputController;
import dadm.scaffold.space.GameController;
import dadm.scaffold.space.SpaceShipPlayer;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    private GameEngine theGameEngine;
    FragmentManager fragmentManager;
    public GameFragment instance;

    public GameFragment() {
        if(instance==null){
            instance=this;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getParentFragmentManager();

        //setListeners for PauseMenu
        fragmentManager.setFragmentResultListener("handlePauseMenu", this, ((requestKey, result) -> {
            String nextState = result.getString("pauseMenuState");
            if (nextState.equals("resume")) {
                theGameEngine.resumeGame();
            } else if (nextState.equals("exit")) {
                theGameEngine.stopGame();
                ((ScaffoldActivity) getActivity()).navigateBack();
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                theGameEngine = new GameEngine((AppCompatActivity) getActivity(), gameView);
                theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
                theGameEngine.setTheInputController(new JoystickInputController(getView()));
                //Initialize elements of score manager
                ScoreManager scoreManager = getScaffoldActivity().getScoreManager();
                scoreManager.setScoreText(view.findViewById(R.id.score));
                theGameEngine.setScoreManager(scoreManager);
                theGameEngine.setScoreManager(getScaffoldActivity().getScoreManager());
                theGameEngine.addGameObject(new SpaceShipPlayer(theGameEngine));
                theGameEngine.addGameObject(new FramesPerSecondCounter(theGameEngine));
                theGameEngine.addGameObject(new GameController(theGameEngine));
                theGameEngine.startGame();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (theGameEngine.isRunning()) {
            //pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        theGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    public void pauseGameAndShowPauseDialog() {
        theGameEngine.pauseGame();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PauseMenuFragment pauseMenuFragment = new PauseMenuFragment();
        transaction.replace(R.id.pauseMenuContainer, pauseMenuFragment, null);
        transaction.commit();
    }

    private void playOrPause() {
        Button button = (Button) getView().findViewById(R.id.btn_play_pause);
        if (theGameEngine.isPaused()) {
            theGameEngine.resumeGame();
            button.setText(R.string.pause);
        } else {
            theGameEngine.pauseGame();
            button.setText(R.string.resume);
        }
    }
}
