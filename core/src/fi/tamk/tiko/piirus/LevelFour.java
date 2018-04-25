package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anu on 8.3.2018.
 */


public class LevelFour {
    private PiirusMain game;

    public static Texture finishPic;      //the beautified picture at the end

    public static int dots = 12;       //how many dots there are in the level

    //Dots
    public static Array<Dot> dotsArray;


    public LevelFour(PiirusMain g) {

        game = g;

        finishPic = new Texture(Gdx.files.internal("cat.png"));

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
                    x = game.WORLD_WIDTH * 0.35875f;
                    y = game.WORLD_HEIGHT * 0.05694f;
                    break;
                case 1:
                    x = game.WORLD_WIDTH * 0.3425f;
                    y = game.WORLD_HEIGHT * 0.29399997f;
                    break;
                case 2:
                    x = game.WORLD_WIDTH * 0.36375f;
                    y = game.WORLD_HEIGHT * 0.48199996f;
                    break;
                case 3:
                    x = game.WORLD_WIDTH * 0.27375f;
                    y = game.WORLD_HEIGHT * 0.65799993f;
                    break;
                case 4:
                    x = game.WORLD_WIDTH * 0.3025f;
                    y = game.WORLD_HEIGHT * 0.9859999f;
                    break;
                case 5:
                    x = game.WORLD_WIDTH * 0.39625f;
                    y = game.WORLD_HEIGHT * 0.83199996f;
                    break;
                case 6:
                    x = game.WORLD_WIDTH * 0.47f;
                    y = game.WORLD_HEIGHT * 0.80999994f;
                    break;
                case 7:
                    x = game.WORLD_WIDTH * 0.595f;
                    y = game.WORLD_HEIGHT * 0.9119999f;
                    break;
                case 8:
                    x = game.WORLD_WIDTH * 0.5575f;
                    y = game.WORLD_HEIGHT * 0.6639999f;
                    break;
                case 9:
                    x = game.WORLD_WIDTH * 0.5075f;
                    y = game.WORLD_HEIGHT * 0.52599996f;
                    break;
                case 10:
                    x = game.WORLD_WIDTH * 0.74f;
                    y = game.WORLD_HEIGHT * 0.27599996f;
                    break;
                case 11:
                    x = game.WORLD_WIDTH * 0.80625f;
                    y = game.WORLD_HEIGHT * 0.011999989f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(game.dotSize);
        }
    }
}