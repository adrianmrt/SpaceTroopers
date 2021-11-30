package dadm.scaffold.space;

import dadm.scaffold.engine.GameEngine;

public class Enemy extends DestroyableItem{

    private int life;

    public Enemy(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine);
        setPoints(100);
        setDamage(2);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;

        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void getHurt(int damage){
        life-=damage;
        if(life==0){
            gameEngine.removeGameObject(this);
        }
    }
}
