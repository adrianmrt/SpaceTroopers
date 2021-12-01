package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ASTEROIDS = 500;
    private static final int TIME_BETWEEN_ENEMIES = 5000;
    private static final int TIME_BETWEEN_UPGRADES = 500;

    private long currentMillis;
    private List<DestroyableItem> asteroidPool = new ArrayList<DestroyableItem>();
    private List<Enemy> enemyPool = new ArrayList<Enemy>();
    private List<UpgradeFire> upgradeFiresPool = new ArrayList<UpgradeFire>();

    private int enemiesSpawned;
    private int asteroidsSpawned;
    private int upgradesFireSpawned;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i=0; i<10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.a10000));
        }
        for (int i=0; i<3; i++) {
            enemyPool.add(new Enemy(this, gameEngine, R.drawable.ship));
        }
        for (int i=0; i<5; i++) {
            upgradeFiresPool.add(new UpgradeFire(this, gameEngine, R.drawable.robot));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        asteroidsSpawned=0;
        upgradesFireSpawned=0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestampEnemies = enemiesSpawned*TIME_BETWEEN_ENEMIES;
        if (currentMillis > waveTimestampEnemies && !enemyPool.isEmpty()) {
            // Spawn a new enemy
            DestroyableItem a = enemyPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemiesSpawned++;
        }
        long waveTimestampAsteroids = asteroidsSpawned*TIME_BETWEEN_ASTEROIDS;
        if (currentMillis > waveTimestampAsteroids) {
            // Spawn a new asteroid
            DestroyableItem a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            asteroidsSpawned++;
        }

        long waveTimestampUpgradesFire = upgradesFireSpawned*TIME_BETWEEN_UPGRADES;
        if (currentMillis > waveTimestampUpgradesFire&&!upgradeFiresPool.isEmpty()) {
            // Spawn a new upgrade
            UpgradeFire a = upgradeFiresPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            upgradesFireSpawned++;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(DestroyableItem asteroid) {
        asteroidPool.add(asteroid);
    }
    public void returnToPool(Enemy enemy) {
        enemyPool.add(enemy);
    }
    public void returnToPool(UpgradeFire upgradeFire) {
        upgradeFiresPool.add(upgradeFire);
    }
}
