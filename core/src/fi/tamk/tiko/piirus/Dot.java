package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Singular dot's statistics can be found here.
 *
 * When a dot is created it's told it's visibility. Then, when the player moves the pencil rectangle on the dot the dot starts to change color.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class Dot {

    private boolean visible;
    //countdown is used to calculate how long you need to be on the dot to clear it
    private float countdown;

    private float spriteWidth;
    private float spriteHeight;

    Rectangle dotRect;

    Sprite sprite ;

    /**
     * Constructor for the dot.
     * @param x the dot's x coordinate
     * @param y the dot's y coordinate
     * @param visible is the dot visible upon it's creation (hint: only the first dot is visible, others are not)
     */
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
        //The dot is red if it's set to visible, invisible if not
        if (visible) {
            sprite.setColor(1,0,0,1);
        } else {
            sprite.setColor(0,0,0,0);
        }

        this.visible = visible;
    }

    /**
     * Checks the collision of the pencil and the dot.
     * @param rect the pencil's rectangle
     */
    public void checkCollisions(Rectangle rect){

        if (sprite.getBoundingRectangle().overlaps(rect)){
            countdown += Gdx.graphics.getRawDeltaTime();
        } else {
            if (countdown > 0) {
                countdown -= Gdx.graphics.getRawDeltaTime();
            }
        }

        //Color slowly changes to green
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

    /**
     * Dot becomes visible
     */
    public void setVisible(){
        sprite.setColor(1,1,1,1);
    }

    /**
     * Set the dot's size to be what user selected in the settings menu
     * @param amount the dot's size multiplier
     */
    public void setSize(float amount){
        sprite.setSize(spriteWidth * amount, spriteHeight * amount);
    }
}
