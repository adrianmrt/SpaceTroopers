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
    private long currentMillis;
    private List<DestroyableItem> asteroidPool = new ArrayList<DestroyableItem>();
    private List<Enemy> enemyPool = new ArrayList<Enemy>();
    private int enemiesSpawned;
    private int asteroidsSpawned;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i=0; i<10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine, R.drawable.a10000));
        }
        for (int i=0; i<3; i++) {
            enemyPool.add(new Enemy(this, gameEngine, R.drawable.ship));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
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
            return;
        }
        long waveTimestampAsteroids = asteroidsSpawned*TIME_BETWEEN_ASTEROIDS;
        if (currentMillis > waveTimestampAsteroids) {
            // Spawn a new asteroid
            DestroyableItem a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            asteroidsSpawned++;
            return;
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
}
