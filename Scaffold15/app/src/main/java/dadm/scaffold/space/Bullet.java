package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    protected double speedFactor;
    protected SpaceShipPlayer.BulletType bulletType;
    protected SpaceShipPlayer parent;

    protected Enemy parentEnemy;
    protected int bulletDamage;
    int screenWidth;
    int screenHeight;

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


    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    public void init(Enemy parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parentEnemy = parentPlayer;
        bulletDamage= parentEnemy.getDamage();
    }

   protected void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
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
        if (otherObject instanceof SpaceShipPlayer) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            SpaceShipPlayer a = (SpaceShipPlayer) otherObject;
            a.lifeManager.getHurt(bulletDamage);// Add  score
            gameEngine.removeGameObject(a);
            gameEngine.onGameEvent(GameEvent.SpaceshipDestroy);
        }
        if (otherObject instanceof Enemy) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Enemy a = (Enemy) otherObject;
            a.getHurt(bulletDamage);// Add  score
            //gameEngine.onGameEvent(GameEvent.SpaceshipDestroy);
        }
    }

    public void setBulletType(SpaceShipPlayer.BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }


}
