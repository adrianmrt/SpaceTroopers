package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;
import dadm.scaffold.space.bullets.EnemyBullet;

public class Bullet extends Sprite {

    protected double speedFactor;
    protected SpaceShipPlayer.BulletType bulletType;
    protected SpaceShipPlayer parent;

    protected int bulletDamage;
    protected int screenWidth;
    protected int screenHeight;

    public int whoIsFiring; //0 ship, 1 enemy

    public Bullet(GameEngine gameEngine, int type){
        super(gameEngine, R.drawable.bullet);
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
        screenWidth= gameEngine.width;
        screenHeight= gameEngine.height;
        whoIsFiring =type;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedFactor * elapsedMillis;
        if (positionY < -height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }

    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
        bulletDamage= parent.getBulletDamage();
    }

   protected void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid && whoIsFiring ==0) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            DestroyableItem a = (DestroyableItem) otherObject;
            a.addPoints(a.getPoints());// Add  score
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }
        else if (otherObject instanceof Enemy && whoIsFiring ==0) {
            Enemy a = (Enemy) otherObject;
            a.getHurt(bulletDamage);//damage enemy
            removeObject(gameEngine);
            //gameEngine.onGameEvent(GameEvent.SpaceshipDestroy);
        }
        else if (otherObject instanceof EnemyBullet) {
            EnemyBullet a = (EnemyBullet) otherObject;
            a.removeObject(gameEngine);
            removeObject(gameEngine);
        }
    }

    public void setBulletType(SpaceShipPlayer.BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }


}
