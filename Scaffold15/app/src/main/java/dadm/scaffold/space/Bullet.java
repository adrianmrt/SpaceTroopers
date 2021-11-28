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
    }

    public void setBulletType(SpaceShipPlayer.BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }


}
