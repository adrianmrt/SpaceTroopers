package dadm.scaffold.space.upgrades;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.space.DestroyableItem;
import dadm.scaffold.space.GameController;

public class UpgradeHealth  extends DestroyableItem {
    private int health;

    public UpgradeHealth(GameController gameController, GameEngine gameEngine, int spriteId) {
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
        health=1;
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

    public int getHealth() {
        return health;
    }
}
