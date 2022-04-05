package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class BossController extends ObjectPool<Boss> {
    private GameController gc;

    @Override
    protected Boss newObject() {
        return new Boss(gc);
    }

    public BossController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Boss b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup(float x, float y){
        getActiveElement().activate(x, y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
