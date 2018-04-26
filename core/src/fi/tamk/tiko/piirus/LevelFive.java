package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anu on 8.3.2018.
 */


public class LevelFive {
    private PiirusMain game;

    public static Texture finishPic;      //the beautified picture at the end

    public static int dots = 13;       //how many dots there are in the level

    //Dots
    public static Array<Dot> dotsArray;


    public LevelFive(PiirusMain g) {

        game = g;

        finishPic = new Texture(Gdx.files.internal("snowflake.png"));

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
                    x = game.WORLD_WIDTH * 0.475f;
                    y = game.WORLD_HEIGHT * 0.64f;
                    break;
                case 1:
                    x = game.WORLD_WIDTH * 0.46375f;
                    y = game.WORLD_HEIGHT * 0.966f;
                    break;
                case 2:
                    x = game.WORLD_WIDTH * 0.56f;
                    y = game.WORLD_HEIGHT * 0.67f;
                    break;
                case 3:
                    x = game.WORLD_WIDTH * 0.71f;
                    y = game.WORLD_HEIGHT * 0.838f;
                    break;
                case 4:
                    x = game.WORLD_WIDTH * 0.625f;
                    y = game.WORLD_HEIGHT * 0.59f;
                    break;
                case 5:
                    x = game.WORLD_WIDTH * 0.795f;
                    y = game.WORLD_HEIGHT * 0.506f;
                    break;
                case 6:
                    x = game.WORLD_WIDTH * 0.613f;
                    y = game.WORLD_HEIGHT * 0.502f;
                    break;
                case 7:
                    x = game.WORLD_WIDTH * 0.651f;
                    y = game.WORLD_HEIGHT * 0.216f;
                    break;
                case 8:
                    x = game.WORLD_WIDTH * 0.5375f;
                    y = game.WORLD_HEIGHT * 0.424f;
                    break;
                case 9:
                    x = game.WORLD_WIDTH * 0.405f;
                    y = game.WORLD_HEIGHT * 0.208f;
                    break;
                case 10:
                    x = game.WORLD_WIDTH * 0.452f;
                    y = game.WORLD_HEIGHT * 0.516f;
                    break;
                case 11:
                    x = game.WORLD_WIDTH * 0.278f;
                    y = game.WORLD_HEIGHT * 0.666f;
                    break;
                case 12:
                    x = game.WORLD_WIDTH * 0.475f;
                    y = game.WORLD_HEIGHT * 0.64f;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            dotsArray.get(i).setSize(game.dotSize);
        }
    }
}