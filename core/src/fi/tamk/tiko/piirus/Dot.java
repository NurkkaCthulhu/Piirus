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

    private boolean visible = true;
    private boolean cleared = false;
    int countdown = 0;

    Rectangle dotRect;

    Sprite sprite ;
    Texture texture = new Texture("target.png");

    public Dot(float x, float y, boolean visible){
        sprite = new Sprite(new Texture("target.png"));
        //sprite.setTexture(texture);
        //sprite.setPosition(x - (sprite.getTexture().getWidth()/10), y - (sprite.getTexture().getHeight()/10));
        //sprite.setY(y);
        //sprite.setX(x);
        sprite.setSize(sprite.getWidth()/100, sprite.getHeight()/100);

        //sprite.setBounds(x, y, texture.getWidth()/20, texture.getHeight()/20);
       // positionX = x;
       // positionY = y;
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
        dotRect = new Rectangle(x, y, texture.getWidth()/100, texture.getHeight()/100);
        if (visible) {
            sprite.setColor(1,1,1,1);
        } else {
            sprite.setColor(1,1,1,0);
        }
       // Gdx.app.log("rectangle", "rectx=" + sprite.getBoundingRectangle().getHeight() + "recty: " + sprite.getBoundingRectangle().getWidth());
    }

    public void checkCollisions(Rectangle rect){

        if(sprite.getBoundingRectangle().overlaps(rect)){
            Gdx.app.log("asd", "Osuuuuu");
            countdown++;
        } else {
            if(countdown > 0) {
                countdown--;
            }
        }

        if(countdown > 60) {
            LevelOne.setDotsCleared();
            countdown = 0;
        }
        Gdx.app.log("count", "down: " + countdown);
    }

    public void setVisible(){
        sprite.setColor(1,1,1,1);
    }

    public void setCleared(boolean b){
        cleared = b;
    }
}
