package dadm.scaffold.space;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.managers.LifeManager;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;
import dadm.scaffold.space.bullets.EnemyBullet;
import dadm.scaffold.space.bullets.TripleBullet;
import dadm.scaffold.space.upgrades.UpgradeFire;
import dadm.scaffold.space.upgrades.UpgradeHealth;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final int INITIAL_TRIPLEBULLET_POOL_AMOUNT = 36;
    private static final long TIME_BETWEEN_BULLETS = 250;

    List<Bullet> bullets = new ArrayList<Bullet>();
    List<TripleBullet> tripleBullets = new ArrayList<TripleBullet>();
    private long timeSinceLastFire;

    private int maxX;
    private int maxY;
    private double speedFactor;

    BulletType bulletType = BulletType.BasicBullet;
    boolean gainUpgrade = false;
    GameEngine theGameEngine;
    LifeManager lifeManager;

    int bulletDamage = 1;
    int bulletEffect; //0 asteroids, 1 enemies

    enum BulletType {
        BasicBullet,
        TripleBullet
    }

    public SpaceShipPlayer(GameEngine gameEngine, int spriteId) {
        super(gameEngine, spriteId);
        speedFactor = pixelFactor * 0.2d; // We want to move at 200px per second
        theGameEngine = gameEngine;
        maxX = theGameEngine.width - width;
        maxY = theGameEngine.height - height;
        initBulletPool(theGameEngine);
        lifeManager = theGameEngine.lifeManager;
        bulletEffect = 0;
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
        initTripleBulletPool(gameEngine, INITIAL_TRIPLEBULLET_POOL_AMOUNT);
    }

    public void initTripleBulletPool(GameEngine gameEngine, int numberOfBullets) {
        for (int i = 0; i < numberOfBullets; i++) {
            tripleBullets.add(new TripleBullet(gameEngine, 0));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        Bullet b = bullets.remove(0);
        return b;
    }

    private TripleBullet getTripleBullet() {
        if (tripleBullets.isEmpty()) {
            return null;
        }
        TripleBullet b = tripleBullets.remove(0);
        return b;
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
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
        bulletEffect = gameEngine.theInputController.isChanged ? 1 : 0;

        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            if (gainUpgrade) {
                initTripleBulletPool(gameEngine, 9);
            }
            if (tripleBullets.isEmpty()) {
                bulletType = BulletType.BasicBullet;
            }
            shootBullet(gameEngine);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        } else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            theGameEngine.getHurt(a.getDamage());
            a.removeObject(gameEngine);
        } else if (otherObject instanceof EnemyBullet) {
            //gameEngine.stopGame();
            EnemyBullet a = (EnemyBullet) otherObject;
            theGameEngine.getHurt(a.bulletDamage);
            a.removeObject(gameEngine);
        } else if (otherObject instanceof Enemy) {
            Enemy a = (Enemy) otherObject;
            theGameEngine.getHurt(2);
            a.removeObject(gameEngine);
            //gameEngine.onGameEvent(GameEvent.AsteroidHit);
        } else if (otherObject instanceof UpgradeFire) {
            UpgradeFire a = (UpgradeFire) otherObject;
            tripleBullets.clear();
            initTripleBulletPool(gameEngine, a.getNumberOfBullets());
            a.removeObject(gameEngine);
            bulletType = BulletType.TripleBullet;
            //gameEngine.onGameEvent(GameEvent.AsteroidHit);
        } else if (otherObject instanceof UpgradeHealth) {
            UpgradeHealth a = (UpgradeHealth) otherObject;
            gameEngine.addLife(a.getHealth());
            a.removeObject(gameEngine);
            //gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }

        if (lifeManager.getCurrentLife() <= 0) {
            gameEngine.removeGameObject(this);
            gameEngine.onGameEvent(GameEvent.SpaceshipDestroy);
        } else {
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }
    }

    private void shootBullet(GameEngine gameEngine) {
        switch (bulletType) {
            case TripleBullet:
                TripleBullet[] bullets = new TripleBullet[3];
                bullets[0] = getTripleBullet();
                bullets[1] = getTripleBullet();
                bullets[2] = getTripleBullet();

                //check if any of the three is null
                if (bullets[0] == null || bullets[1] == null || bullets[2] == null) {
                    return;
                }

                for (int i = 0; i < bullets.length; i++) {
                    bullets[i].setPosition(i);
                    switch (i) {
                        case 0:
                            bullets[i].init(this, positionX, positionY, bulletEffect);
                            break;
                        case 1:
                            bullets[i].init(this, positionX + width / 2, positionY, bulletEffect);
                            break;
                        case 2:
                            bullets[i].init(this, positionX + width, positionY, bulletEffect);
                            break;
                    }
                    gameEngine.addGameObject(bullets[i]);
                }
                break;

            case BasicBullet:
                Bullet bullet = getBullet();
                if (bullet == null) {
                    return;
                }
                bullet.init(this, positionX + width / 2, positionY, bulletEffect);
                gameEngine.addGameObject(bullet);
                break;
        }
    }

    public int getBulletDamage() {
        return bulletDamage;
    }
}
