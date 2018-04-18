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
    private boolean cleared;
    private float countdown;
    private float spriteWidth;
    private float spriteHeight;

    Rectangle dotRect;

    Sprite sprite ;

    public Dot(float x, float y, boolean visible){
        cleared = false;
        countdown = 0;
        Texture texture = new Texture(Gdx.files.internal("target.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
        //sprite.setTexture(texture);
        //sprite.setPosition(x - (sprite.getTexture().getWidth()/10), y - (sprite.getTexture().getHeight()/10));
        //sprite.setY(y);
        //sprite.setX(x);
        spriteWidth = sprite.getWidth()/100;
        spriteHeight = sprite.getHeight()/100;
        sprite.setSize(spriteWidth, spriteHeight);

        //sprite.setBounds(x, y, texture.getWidth()/20, texture.getHeight()/20);
       // positionX = x;
       // positionY = y;
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
        dotRect = new Rectangle(x, y, texture.getWidth()/100, texture.getHeight()/100);
        if (visible) {
            sprite.setColor(1,0,0,1);
        } else {
            sprite.setColor(0,0,0,0);
        }

        this.visible = visible;
       // Gdx.app.log("rectangle", "rectx=" + sprite.getBoundingRectangle().getHeight() + "recty: " + sprite.getBoundingRectangle().getWidth());
    }

    public void checkCollisions(Rectangle rect){

        if (sprite.getBoundingRectangle().overlaps(rect)){
            //Gdx.app.log("asd", "Osuuuuu");
            countdown += Gdx.graphics.getRawDeltaTime();
        } else {
            if (countdown > 0) {
                countdown -= Gdx.graphics.getRawDeltaTime();
            }
        }

        //Color
        sprite.setColor(1 - countdown/3, countdown/3, 0, 1);

        //if the cursor is held in the dot for long enough, you clear it and can move to the next dot
        if (countdown > 2) {
            sprite.setColor(0.8f,1f,0.8f,0.75f);
            Level.setDotsCleared();
            countdown = 0;
        }
        //Gdx.app.log("count", "down: " + countdown);
    }

    public void setVisible(){
        sprite.setColor(1,1,1,1);
    }

    public void setCleared(boolean b){
        cleared = b;
    }

    public void setSize(float amount){
        sprite.setSize(spriteWidth * amount, spriteHeight * amount);
    }
}
