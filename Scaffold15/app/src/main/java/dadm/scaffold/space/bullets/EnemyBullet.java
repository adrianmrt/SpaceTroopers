package dadm.scaffold.space.bullets;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.space.Bullet;
import dadm.scaffold.space.Enemy;

public class EnemyBullet extends Bullet {

    protected Enemy parentEnemy;
    public EnemyBullet(GameEngine gameEngine, int type) {
        super(gameEngine);
        setBitmap( ((BitmapDrawable) gameEngine.getContext().getResources().getDrawable(R.drawable.enemybullet)));
    }

    public void init(Enemy parentPlayer, double initPositionX, double initPositionY,int effect) {
        positionX = 10+initPositionX - width/2;
        positionY = initPositionY+height*1.25;
        parentEnemy = parentPlayer;
        bulletDamage= parentEnemy.getDamage();
        this.effect=effect;
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
