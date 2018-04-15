package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anu on 8.3.2018.
 */


public class LevelOne {
    private PiirusMain game;

    public static Texture finishPic;      //the beautified picture at the end

    public static int dots = 4;       //how many dots there are in the level

    //Dots
    public static Array<Dot> dotsArray;


    public LevelOne(PiirusMain g) {

        game = g;

        finishPic = new Texture(Gdx.files.internal("bread.png"));

        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            if (i == 0) {
                visible = true;
            } else {
                visible = false;
            }
            switch (i) {
                case 0:
                    x = game.WORLD_WIDTH * 0.6875f;
                    y = game.WORLD_HEIGHT * 0.75f;
                    break;
                case 1:
                    x = game.WORLD_WIDTH * 0.325f;
                    y = game.WORLD_HEIGHT * 0.775f;
                    break;
                case 2:
                    x = game.WORLD_WIDTH * 0.225f;
                    y = game.WORLD_HEIGHT * 0.375f;
                    break;
                case 3:
                    x = game.WORLD_WIDTH * 0.7f;
                    y = game.WORLD_HEIGHT * 0.325f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(game.dotSize);
        }
    }
}