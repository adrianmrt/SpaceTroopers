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

    private static final int TIME_BETWEEN_ASTEROIDS = 500;
    private static final int TIME_BETWEEN_ENEMIES = 5000;
    private static final int TIME_BETWEEN_FIREUPGRADES = 500;
    private static final int TIME_BETWEEN_HEALTHUPGRADES = 500;

    private int nAsteroids = 10;
    private int nEnemies = 3;
    private int nUpgradesH = 5;
    private int nUpgradesF = 5;

    private long currentMillis;
    private List<DestroyableItem> asteroidPool = new ArrayList<DestroyableItem>();
    private List<Enemy> enemyPool = new ArrayList<Enemy>();
    private List<UpgradeFire> upgradeFiresPool = new ArrayList<UpgradeFire>();
    private List<UpgradeHealth> upgradeHealthPool = new ArrayList<UpgradeHealth>();

    private int enemiesSpawned;
    private int asteroidsSpawned;
    private int upgradesFireSpawned;
    private int upgradesHealthSpawned;

    GameEngine gameEngine;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i = 0; i < nAsteroids; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.uk_spitfire));
        }
        for (int i = 0; i < nEnemies; i++) {
            enemyPool.add(new Enemy(this, gameEngine, R.drawable.uk_lancaster));
        }
        for (int i = 0; i < nUpgradesF; i++) {
            upgradeFiresPool.add(new UpgradeFire(this, gameEngine, R.drawable.ammo_icon));
        }
        for (int i = 0; i < nUpgradesH; i++) {
            upgradeHealthPool.add(new UpgradeHealth(this, gameEngine, R.drawable.health_icon));
        }

        this.gameEngine = gameEngine;
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
        refreshPools();
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
        }

        long waveTimestampUpgradesHealth = upgradesHealthSpawned * TIME_BETWEEN_HEALTHUPGRADES;
        if (currentMillis > waveTimestampUpgradesHealth && !upgradeHealthPool.isEmpty()) {
            // Spawn a new upgrade
            UpgradeHealth a = upgradeHealthPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            upgradesHealthSpawned++;
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

    public void refreshPools() {
        if (asteroidPool.size() == nAsteroids / 2) {
            for (int i = 0; i < nAsteroids / 2; i++) {
                asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.uk_spitfire));
            }
        }
        if (enemyPool.size() == nEnemies / 2) {
            for (int i = 0; i < nEnemies / 2; i++) {
                enemyPool.add(new Enemy(this, gameEngine, R.drawable.uk_lancaster));
            }
        }
        if (upgradeFiresPool.size() == nUpgradesF / 2) {
            for (int i = 0; i < nAsteroids / 2; i++) {
                upgradeFiresPool.add(new UpgradeFire(this, gameEngine, R.drawable.ammo_icon));
            }
        }
        if (upgradeHealthPool.size() == nUpgradesH / 2) {
            for (int i = 0; i < nUpgradesH / 2; i++) {
                upgradeHealthPool.add(new UpgradeHealth(this, gameEngine, R.drawable.health_icon));
            }
        }
    }
}
