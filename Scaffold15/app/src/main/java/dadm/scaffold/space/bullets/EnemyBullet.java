package dadm.scaffold.space.bullets;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.space.Bullet;
import dadm.scaffold.space.Enemy;

public class EnemyBullet extends Bullet {

    protected Enemy parentEnemy;
    public EnemyBullet(GameEngine gameEngine, int type) {
        super(gameEngine, type);
    }

    public void init(Enemy parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY+height*2;
        parentEnemy = parentPlayer;
        bulletDamage= parentEnemy.getDamage();
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY -= speedFactor * elapsedMillis;
        if (positionY >=screenHeight) {
            parentEnemy.releaseBullet(this);
            gameEngine.removeGameObject(this);
        }
    }

    @Override
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parentEnemy.releaseBullet(this);
    }
}
