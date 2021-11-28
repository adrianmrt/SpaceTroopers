package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;
    List<Bullet> bullets = new ArrayList<Bullet>();
    List<TripleBullet> tripleBullets = new ArrayList<TripleBullet>();
    private long timeSinceLastFire;

    private int maxX;
    private int maxY;
    private double speedFactor;

    public int lifes=3;
    BulletType bulletType= BulletType.BasicBullet;

    enum BulletType{
        BasicBullet,
        TripleBullet
    }


    public SpaceShipPlayer(GameEngine gameEngine){
        super(gameEngine, R.drawable.ship);
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
            tripleBullets.add(new TripleBullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    private TripleBullet getTripleBullet() {
        if (tripleBullets.isEmpty()) {
            return null;
        }
        return tripleBullets.remove(0);
    }


    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }
    void releaseBullet(TripleBullet bullet) {
        tripleBullets.add(bullet);
    }

    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            shootBullet(gameEngine);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);

        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            lifes--;
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            if(lifes==0) {
                gameEngine.removeGameObject(this);
                gameEngine.onGameEvent(GameEvent.SpaceshipDestroy);
            }else{
                gameEngine.onGameEvent(GameEvent.SpaceshipHit);
            }
        }
    }

    private void shootBullet(GameEngine gameEngine){
        switch (bulletType){
            case TripleBullet:
                TripleBullet[]bullets= new TripleBullet[3];
                bullets[0]=getTripleBullet();
                bullets[1]=getTripleBullet();
                bullets[2]=getTripleBullet();

                //check if any of the three is null
                if(bullets[0]==null||bullets[1]==null||bullets[2]==null){
                    return;
                }

                for (int i=0;i<bullets.length;i++){
                    bullets[i].setPosition(i);
                    switch (i){
                        case 0:
                            bullets[i].init(this, positionX, positionY);
                            break;
                        case 1:
                            bullets[i].init(this, positionX +width/2, positionY);
                            break;
                        case 2:
                            bullets[i].init(this, positionX + width, positionY);
                            break;
                    }
                    gameEngine.addGameObject(bullets[i]);
                }
                break;
            case BasicBullet:
                Bullet bullet = getBullet();
                if (bullet== null) {
                    return;
                }
                bullet.init(this, positionX + width/2, positionY);
                gameEngine.addGameObject(bullet);
                break;
        }
    }
}
