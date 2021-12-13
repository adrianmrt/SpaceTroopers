package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.sound.GameEvent;
import dadm.scaffold.space.bullets.EnemyBullet;
import dadm.scaffold.space.bullets.TripleBullet;

public class Enemy extends DestroyableItem {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final int INITIAL_TRIPLEBULLET_POOL_AMOUNT = 0;
    private static final long TIME_BETWEEN_BULLETS = 1000;

    List<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();
    List<TripleBullet> tripleBullets = new ArrayList<TripleBullet>();
    private long timeSinceLastFire;

    private int life;
    SpaceShipPlayer.BulletType bulletType = SpaceShipPlayer.BulletType.BasicBullet;
    double xMov;
    double yMov;

    enum BulletType {
        BasicBullet,
        TripleBullet
    }

    public Enemy(GameController gameController, GameEngine gameEngine, int spriteId) {
        super(gameController, gameEngine, spriteId);

    }

    @Override
    public void init(GameEngine gameEngine) {
        speedX = speed;
        speedY = speed;
        // Items initialize in the central 50% of the screen horizontally
        positionX = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4;
        // They initialize outside of the screen vertically
        positionY = -height;
        rotation = 180;
        xMov = speedX;
        yMov = speedY / 10;
        setPoints(150);
        setDamage(2);
        initBulletPool(gameEngine);
        setLife(3);
        timeSinceLastFire=0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (positionX >= screenWidth - width) {
            xMov = -speedX;
        } else if (positionX <= 0) {
            xMov = speedX;
        }
        positionX += xMov * elapsedMillis;
        positionY += yMov * elapsedMillis;
        checkIfCanFire(elapsedMillis, gameEngine);
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }
    }

    @Override
    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void getHurt(int damage) {
        life -= damage;
        if (life == 0) {
            addPoints(getPoints());
            gameEngine.removeGameObject(this);
        }
    }

    //bullet management
    private void checkIfCanFire(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            if (tripleBullets.isEmpty()) {
                bulletType = SpaceShipPlayer.BulletType.BasicBullet;
            }
            shootBullet(gameEngine);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);

        } else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
            enemyBullets.add(new EnemyBullet(gameEngine, 1));
        }
        initTripleBulletPool(gameEngine, INITIAL_TRIPLEBULLET_POOL_AMOUNT);
    }

    public void initTripleBulletPool(GameEngine gameEngine, int numberOfBullets) {
        for (int i = 0; i < numberOfBullets; i++) {
            tripleBullets.add(new TripleBullet(gameEngine, 1));
        }
    }

    private EnemyBullet getBullet() {
        if (enemyBullets.isEmpty()) {
            return null;
        }
        EnemyBullet b = enemyBullets.remove(0);
        return b;
    }

    private TripleBullet getTripleBullet() {
        if (tripleBullets.isEmpty()) {
            return null;
        }
        TripleBullet b = tripleBullets.remove(0);
        return b;
    }

    public void releaseBullet(EnemyBullet bullet) {
        enemyBullets.add(bullet);
    }

    private void shootBullet(GameEngine gameEngine) {
        EnemyBullet bullet = getBullet();
        if (bullet == null) {
            return;
        }
        bullet.init(this, positionX + width / 2, positionY,2);
        gameEngine.addGameObject(bullet);
     }

}
