package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.space.upgrades.UpgradeFire;
import dadm.scaffold.space.upgrades.UpgradeHealth;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ASTEROIDS = 3000;
    private static final int TIME_BETWEEN_ENEMIES = 14000;
    private static final int TIME_BETWEEN_FIREUPGRADES = 10000; //Final = 10000
    private static final int TIME_BETWEEN_HEALTHUPGRADES = 12000; //Final 12000

    private long currentMillis;
    private List<DestroyableItem> asteroidPool = new ArrayList<DestroyableItem>();
    private List<Enemy> enemyPool = new ArrayList<Enemy>();
    private List<UpgradeFire> upgradeFiresPool = new ArrayList<UpgradeFire>();
    private List<UpgradeHealth> upgradeHealthPool = new ArrayList<UpgradeHealth>();

    private int enemiesSpawned;
    private int asteroidsSpawned;
    private int upgradesFireSpawned;
    private int upgradesHealthSpawned;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.asteroid));
        }
        for (int i = 0; i < 3; i++) {
            enemyPool.add(new Enemy(this, gameEngine, R.drawable.enemyship));
        }
        for (int i = 0; i < 10; i++) {
            upgradeFiresPool.add(new UpgradeFire(this, gameEngine, R.drawable.fireupgrade));
        }
        for (int i = 0; i < 10; i++) {
            upgradeHealthPool.add(new UpgradeHealth(this, gameEngine, R.drawable.healthupgrade));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        asteroidsSpawned = 0;
        upgradesFireSpawned = 0;
        upgradesHealthSpawned = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestampEnemies = enemiesSpawned * TIME_BETWEEN_ENEMIES;
        if (currentMillis > waveTimestampEnemies && !enemyPool.isEmpty()) {
            // Spawn a new enemy
            DestroyableItem a = enemyPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemiesSpawned++;
        }
        long waveTimestampAsteroids = asteroidsSpawned * TIME_BETWEEN_ASTEROIDS;
        if (currentMillis > waveTimestampAsteroids) {
            // Spawn a new asteroid
            DestroyableItem a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            asteroidsSpawned++;
        }

        long waveTimestampUpgradesFire = upgradesFireSpawned * TIME_BETWEEN_FIREUPGRADES;
        if (currentMillis > waveTimestampUpgradesFire && !upgradeFiresPool.isEmpty()) {
            // Spawn a new upgrade
            UpgradeFire a = upgradeFiresPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            upgradesFireSpawned++;
            upgradeFiresPool.add(a);
        }

        long waveTimestampUpgradesHealth = upgradesHealthSpawned * TIME_BETWEEN_HEALTHUPGRADES;
        if (currentMillis > waveTimestampUpgradesHealth && !upgradeHealthPool.isEmpty()) {
            // Spawn a new upgrade
            UpgradeHealth a = upgradeHealthPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            upgradesHealthSpawned++;
            upgradeHealthPool.add(a);
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
