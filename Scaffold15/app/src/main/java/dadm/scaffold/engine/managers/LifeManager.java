package dadm.scaffold.engine.managers;

import android.widget.TextView;

public class LifeManager {
    TextView lifesT;
    int currentLife;
    int initialLifes=3;

    public LifeManager(){
        currentLife=initialLifes;
    }

    public Runnable updateLifeText= new Runnable() {
        @Override
        public void run() {
            if (currentLife>0)
                lifesT.setText(Integer.toString(currentLife));
        }
    };

    public void setLifesT(TextView lifesT) {
        this.lifesT = lifesT;
        lifesT.setText(Integer.toString(currentLife));
    }

    public void resetLifes(){
        currentLife=initialLifes;
    }

    public void getHurt(int damage){
        currentLife-=damage;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public void addLife(int lifesToAdd){
        currentLife+=lifesToAdd;
    }
}
