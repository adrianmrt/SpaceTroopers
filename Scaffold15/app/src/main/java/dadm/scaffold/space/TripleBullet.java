package dadm.scaffold.space;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class TripleBullet extends Bullet {
    private int position; //0 left, 1 middle, 2 right

    public TripleBullet(GameEngine gameEngine, int type) {
        super(gameEngine,type);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            switch (position) {
                case 0:
                    positionX += speedFactor * elapsedMillis;
                    positionY += speedFactor * elapsedMillis;
                    if (positionY <=0||positionX>=screenWidth||positionX<=0) {
                        gameEngine.removeGameObject(this);
                        removeObject(gameEngine);
                    }
                    break;
                case 1:
                    positionY += speedFactor * elapsedMillis;
                    if (positionY <=0||positionX>=screenWidth||positionX<=0) {
                        gameEngine.removeGameObject(this);
                        removeObject(gameEngine);
                    }
                    break;
                case 2:
                    positionX -= speedFactor * elapsedMillis;
                    positionY += speedFactor * elapsedMillis;
                    if (positionY <=0||positionX>=screenWidth||positionX<=0) {
                        removeObject(gameEngine);
                    }
                    break;
            }
    }

    @Override
    protected void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    //changes the rotation of the bullet and sets which bullet is
    public void setPosition(int position) {
        this.position = position;
        switch (position) {
            case 0:
                rotation = -45;
                break;
            case 2:
                rotation = 45;
                break;
            default:
                rotation=0;
        }
    }
}
