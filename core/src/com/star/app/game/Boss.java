package com.star.app.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Boss extends Ship implements Poolable {
    private boolean active;
    private Vector2 tempVec;
    private Texture healthTexture;

    @Override
    public boolean isActive() {
        return active;
    }

    public Boss(GameController gc) {
        super(gc, 200, 100 + gc.getLevel() * 20);
        this.texture = Assets.getInstance().getAtlas().findRegion("boss");
        this.position = new Vector2(100, 100);
        this.hitArea = new Circle(position, 130);
        this.active = false;
        this.tempVec = new Vector2();
        this.ownerType = OwnerType.BOSS;
        this.weaponNum = MathUtils.random(gc.getLevel() + 1);
        this.currentWeapon = weapons[weaponNum];
    }

    public void activate(float x, float y) {
        position.set(x, y);
        hpMax = 100 + gc.getLevel() * 200;
        hp = hpMax;
        active = true;
        callBots(gc.getLevel()/2);
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 1, 1, angle);
    }

    public void update(float dt) {
        super.update(dt);

        if (!isAlive()) {
            active = false;
        }

        tempVec.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVec.angleDeg();

        if (gc.getHero().getPosition().dst(position) > 100) {
            accelerate(dt);

            float bx;
            float by;

            for (int i = 0; i < 20; i +=5) {
                bx = position.x + MathUtils.cosDeg(angle + (155 + i)) * (110-i);
                by = position.y + MathUtils.sinDeg(angle + (155 + i)) * (110-i);

                for (int j = 0; j < 3; j++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                            0.1f,
                            1.2f, 0.2f,
                            0.0f, 0.5f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 0.0f);
                }

                bx = position.x + MathUtils.cosDeg(angle - (155 + i)) * (110-i);
                by = position.y + MathUtils.sinDeg(angle - (155 + i)) * (110-i);

                for (int j = 0; j < 3; j++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                            0.1f,
                            1.2f, 0.2f,
                            0.0f, 0.5f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 0.0f);
                }
            }


        }

        if (gc.getHero().getPosition().dst(position) < 500) {
            tryToFire();
        }
    }
    public void renderBossHealth(SpriteBatch batch) {
        Pixmap pixmap = new Pixmap(10,10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        this.healthTexture = new Texture(pixmap);
        batch.draw(healthTexture,100,ScreenManager.SCREEN_HEIGHT-10,(float) hp/hpMax * 1000,10);
        pixmap.dispose();
    }

    public void callBots(int count) {
       for (int i = 0; i < count; i++) {
           gc.getBotController().setup(position.x+150, position.y+150);
       }
    }
}
