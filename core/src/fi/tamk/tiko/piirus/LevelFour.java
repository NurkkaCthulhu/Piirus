package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;
/**
 * Level Four's dot stats.
 *
 * Holds the information of the level: how many dots there are and where they are. Easy to modify without breaking the base Level class.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */
class LevelFour {
    //how many dots there are in the level
    static int dots = 12;
    //Dots
    static Array<Dot> dotsArray;

    /**
     * Constructor fot level four.
     * @param g the main game object(can be used to call all sorts of things)
     */
    LevelFour(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.35875f;
                    y = g.WORLD_HEIGHT * 0.05694f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.3425f;
                    y = g.WORLD_HEIGHT * 0.29399997f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.36375f;
                    y = g.WORLD_HEIGHT * 0.48199996f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.27375f;
                    y = g.WORLD_HEIGHT * 0.65799993f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.3025f;
                    y = g.WORLD_HEIGHT * 0.9859999f;
                    break;
                case 5:
                    x = g.WORLD_WIDTH * 0.39625f;
                    y = g.WORLD_HEIGHT * 0.83199996f;
                    break;
                case 6:
                    x = g.WORLD_WIDTH * 0.47f;
                    y = g.WORLD_HEIGHT * 0.80999994f;
                    break;
                case 7:
                    x = g.WORLD_WIDTH * 0.595f;
                    y = g.WORLD_HEIGHT * 0.9119999f;
                    break;
                case 8:
                    x = g.WORLD_WIDTH * 0.5575f;
                    y = g.WORLD_HEIGHT * 0.6639999f;
                    break;
                case 9:
                    x = g.WORLD_WIDTH * 0.5075f;
                    y = g.WORLD_HEIGHT * 0.52599996f;
                    break;
                case 10:
                    x = g.WORLD_WIDTH * 0.74f;
                    y = g.WORLD_HEIGHT * 0.27599996f;
                    break;
                case 11:
                    x = g.WORLD_WIDTH * 0.80625f;
                    y = g.WORLD_HEIGHT * 0.011999989f;
                    break;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            //dot's size is the one that the user inputted in settings
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}