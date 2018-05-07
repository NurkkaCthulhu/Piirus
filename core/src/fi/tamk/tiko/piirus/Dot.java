package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Anu on 8.3.2018.
 */

public class Dot {

    private boolean visible;
    private float countdown;
    private float spriteWidth;
    private float spriteHeight;

    Rectangle dotRect;

    Sprite sprite ;

    public Dot(float x, float y, boolean visible){
        countdown = 0;
        Texture texture = new Texture(Gdx.files.internal("target.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
        spriteWidth = sprite.getWidth()/100;
        spriteHeight = sprite.getHeight()/100;
        sprite.setSize(spriteWidth, spriteHeight);


        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
        dotRect = new Rectangle(x, y, texture.getWidth()/100, texture.getHeight()/100);
        if (visible) {
            sprite.setColor(1,0,0,1);
        } else {
            sprite.setColor(0,0,0,0);
        }

        this.visible = visible;
    }

    public void checkCollisions(Rectangle rect){

        if (sprite.getBoundingRectangle().overlaps(rect)){
            countdown += Gdx.graphics.getRawDeltaTime();
        } else {
            if (countdown > 0) {
                countdown -= Gdx.graphics.getRawDeltaTime();
            }
        }

        //Color
        if (countdown <= 0) {
            sprite.setColor(1,0,0,1);
        } else {
            sprite.setColor(1 - countdown / 3, countdown / 3, 0, 1);
        }
        //if the cursor is held in the dot for long enough, you clear it and can move to the next dot
        if (countdown > 2) {
            sprite.setColor(0.8f,1f,0.8f,0.75f);
            Level.setDotsCleared();
            countdown = 0;
        }
    }

    public void setVisible(){
        sprite.setColor(1,1,1,1);
    }

    public void setSize(float amount){
        sprite.setSize(spriteWidth * amount, spriteHeight * amount);
    }
}
