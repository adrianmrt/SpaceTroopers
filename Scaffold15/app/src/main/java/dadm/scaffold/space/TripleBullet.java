package dadm.scaffold.space;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class TripleBullet extends Bullet {
    private int position; //0 left, 1 middle, 2 right

    public TripleBullet(GameEngine gameEngine) {
        super(gameEngine);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            switch (position) {
                case 0:
                    positionX += speedFactor * elapsedMillis;
                    positionY += speedFactor * elapsedMillis;
                    if (positionY < -height) {
                        gameEngine.removeGameObject(this);
                    }
                    break;
                case 1:
                    positionY += speedFactor * elapsedMillis;
                    if (positionY < -height) {
                        gameEngine.removeGameObject(this);
                    }
                    break;
                case 2:
                    positionX -= speedFactor * elapsedMillis;
                    positionY += speedFactor * elapsedMillis;
                    if (positionY < -height) {
                        gameEngine.removeGameObject(this);
                    }
                    break;
            }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            DestroyableItem a = (DestroyableItem) otherObject;
            a.addPoints(a.getPoints());// Add  score
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }
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
