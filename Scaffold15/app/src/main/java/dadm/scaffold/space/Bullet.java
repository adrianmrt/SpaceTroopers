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

    public int effect; //0 asteroids, 1 enemy, 2 player

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.bullet);
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
        screenWidth= gameEngine.width;
        screenHeight= gameEngine.height;
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

    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY, int effect) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
        bulletDamage= parent.getBulletDamage();
        this.effect =effect;
    }

   protected void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid && effect ==0) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            DestroyableItem a = (DestroyableItem) otherObject;
            a.addPoints(a.getPoints());// Add  score
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }
        else if (otherObject instanceof Enemy && effect ==1) {
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
