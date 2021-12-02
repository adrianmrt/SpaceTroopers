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

    private static final int TIME_BETWEEN_ENEMIES = 5000;
    private static final int TIME_BETWEEN_ASTEROIDS = 500;
    private static final int TIME_BETWEEN_FIREUPGRADES = 500;
    private static final int TIME_BETWEEN_HEALTHUPGRADES = 500;
    private static final int TIME_BETWEEN_ROUNDS = 2000;
    private static final int NUMBER_OF_ROUNDS = 3;

    private long currentMillis;
    private List<DestroyableItem> asteroidPool = new ArrayList<DestroyableItem>();
    private List<Enemy> enemyPool = new ArrayList<Enemy>();
    private List<UpgradeFire> upgradeFiresPool = new ArrayList<UpgradeFire>();
    private List<UpgradeHealth> upgradeHealthPool = new ArrayList<UpgradeHealth>();

    private int enemiesSpawned;
    private int[] enemiesToSpawn;
    private int asteroidsSpawned;
    private int[] asteroidsToSpawn;
    private int upgradesFireSpawned;
    private int[] upgradesFireToSpawn;
    private int upgradesHealthSpawned;
    private int[] upgradesHealthToSpawn;

    private boolean[] roundDone;
    private boolean changeOfRound;

    public int actualRound;

    public GameController(GameEngine gameEngine) {
        enemiesToSpawn = new int[]{1, 2, 5};
        asteroidsToSpawn = new int[]{1, 2, 5};
        upgradesFireToSpawn = new int[]{1, 2, 5};
        upgradesHealthToSpawn = new int[]{1, 2, 5};

        roundDone = new boolean[]{false, false, false, false};

        actualRound = 0;
        currentMillis = 1;

        initializeRound(gameEngine);
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        changeOfRound = true;
        for (int i = 0; i < roundDone.length; i++){
            if(!roundDone[i]) changeOfRound = false;
        }

        if(changeOfRound){
            initializeRound(gameEngine);
            actualRound++;
        } else{
            spawnEnemies(gameEngine);
        }
    }

    public void initializeRound(GameEngine gameEngine){
        enemiesSpawned = 0;
        asteroidsSpawned = 0;
        upgradesFireSpawned = 0;
        upgradesHealthSpawned = 0;

        if (actualRound < NUMBER_OF_ROUNDS) {
            initializePools(gameEngine, enemiesToSpawn[actualRound], asteroidsToSpawn[actualRound], upgradesFireToSpawn[actualRound], upgradesHealthToSpawn[actualRound]);

            for (int i = 0; i < roundDone.length; i++){
                roundDone[i] = false;
            }
        } else {
            gameEngine.EndGame(gameEngine.scoreManager.getCurrentScore());
        }
    }

    public void initializePools(GameEngine gameEngine, int enemyNumber, int asteroidNumber, int upgradeFiresNumber, int upgradeHealthNumber){
        for (int i = 0; i < enemyNumber; i++) {
            enemyPool.add(new Enemy(this, gameEngine, R.drawable.ger_he111));
        }
        for (int i = 0; i < asteroidNumber; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.a10000));
        }
        for (int i = 0; i < upgradeFiresNumber; i++) {
            upgradeFiresPool.add(new UpgradeFire(this, gameEngine, R.drawable.robot));
        }
        for (int i = 0; i < upgradeHealthNumber; i++) {
            upgradeHealthPool.add(new UpgradeHealth(this, gameEngine, R.drawable.robot));
        }
    }

    public void spawnEnemies(GameEngine gameEngine) {
        if(!roundDone[0]){
            if (enemiesSpawned < enemiesToSpawn[actualRound]) {
                long waveTimestampEnemies = enemiesSpawned * TIME_BETWEEN_ENEMIES;
                if (currentMillis > waveTimestampEnemies && !enemyPool.isEmpty()) {
                    // Spawn a new enemy
                    DestroyableItem a = enemyPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    enemiesSpawned++;
                }
            } else {
                roundDone[0] = true;
            }
        }

        if(!roundDone[1]){
            if (asteroidsSpawned < asteroidsToSpawn[actualRound]) {
                long waveTimestampAsteroids = asteroidsSpawned * TIME_BETWEEN_ASTEROIDS;
                if (currentMillis > waveTimestampAsteroids) {
                    // Spawn a new asteroid
                    DestroyableItem a = asteroidPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    asteroidsSpawned++;
                }
            } else {
                roundDone[1] = true;
            }
        }

        if(!roundDone[2]){
            if (upgradesFireSpawned < upgradesFireToSpawn[actualRound]) {
                long waveTimestampUpgradesFire = upgradesFireSpawned * TIME_BETWEEN_FIREUPGRADES;
                if (currentMillis > waveTimestampUpgradesFire && !upgradeFiresPool.isEmpty()) {
                    // Spawn a new upgrade
                    UpgradeFire a = upgradeFiresPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    upgradesFireSpawned++;
                }
            } else {
                roundDone[2] = true;
            }
        }

        if(!roundDone[3]){
            if (upgradesHealthSpawned < upgradesHealthToSpawn[actualRound]) {
                long waveTimestampUpgradesHealth = upgradesHealthSpawned * TIME_BETWEEN_HEALTHUPGRADES;
                if (currentMillis > waveTimestampUpgradesHealth && !upgradeHealthPool.isEmpty()) {
                    // Spawn a new upgrade
                    UpgradeHealth a = upgradeHealthPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    upgradesHealthSpawned++;
                }
            } else {
                roundDone[3] = true;
            }
        }
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

    @Override
    public void onDraw(Canvas canvas) {}
}