package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;

public class Asteroid extends DestroyableItem {

    private final GameController gameController;

    private double speed;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    public Asteroid(GameController gameController, GameEngine gameEngine, int spriteId) {
        super(gameController, gameEngine, spriteId);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;
        this.rotationSpeed = 0;
        setPoints(50);
        setDamage(1);
    }
}
