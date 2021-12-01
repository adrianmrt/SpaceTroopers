package dadm.scaffold.space;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class UpgradeFire extends DestroyableItem{

    private int numberOfBullets;

    public UpgradeFire(GameController gameController, GameEngine gameEngine, int spriteId) {
        super(gameController, gameEngine, spriteId);
    }

    @Override
    public void init(GameEngine gameEngine) {
        speedX = 0;
        speedY = speed;
        positionX = gameEngine.random.nextInt(screenWidth-width);
        // They initialize outside of the screen vertically
        positionY = 0;
        rotation=0;
        numberOfBullets=30;
    }


    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedY * elapsedMillis;
        if (positionY >=screenHeight) {
            gameEngine.removeGameObject(this);
        }
    }

    @Override
    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        //gameController.returnToPool(this);
    }

    public int getNumberOfBullets() {
        return numberOfBullets;
    }
}
