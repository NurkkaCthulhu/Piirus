package fi.tamk.tiko.piirus;

import com.badlogic.gdx.utils.Array;
/**
 * Level Five's dot stats.
 *
 * Holds the information of the level: how many dots there are and where they are. Easy to modify without breaking the base Level class.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */
class LevelFive {
    //how many dots there are in the level
    static int dots = 13;
    //Dots
    static Array<Dot> dotsArray;


    /**
     * Constructor fot level five.
     * @param g the main game object(can be used to call all sorts of things)
     */
    LevelFive(PiirusMain g) {
        //dots are in an array. Dot coordinates are inputted manually.
        dotsArray = new Array<Dot>(dots);

        for (int i = 0; i < dots; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            visible = i == 0;
            switch (i) {
                case 0:
                    x = g.WORLD_WIDTH * 0.475f;
                    y = g.WORLD_HEIGHT * 0.64f;
                    break;
                case 1:
                    x = g.WORLD_WIDTH * 0.46375f;
                    y = g.WORLD_HEIGHT * 0.966f;
                    break;
                case 2:
                    x = g.WORLD_WIDTH * 0.56f;
                    y = g.WORLD_HEIGHT * 0.67f;
                    break;
                case 3:
                    x = g.WORLD_WIDTH * 0.71f;
                    y = g.WORLD_HEIGHT * 0.838f;
                    break;
                case 4:
                    x = g.WORLD_WIDTH * 0.625f;
                    y = g.WORLD_HEIGHT * 0.59f;
                    break;
                case 5:
                    x = g.WORLD_WIDTH * 0.795f;
                    y = g.WORLD_HEIGHT * 0.506f;
                    break;
                case 6:
                    x = g.WORLD_WIDTH * 0.613f;
                    y = g.WORLD_HEIGHT * 0.502f;
                    break;
                case 7:
                    x = g.WORLD_WIDTH * 0.651f;
                    y = g.WORLD_HEIGHT * 0.216f;
                    break;
                case 8:
                    x = g.WORLD_WIDTH * 0.5375f;
                    y = g.WORLD_HEIGHT * 0.424f;
                    break;
                case 9:
                    x = g.WORLD_WIDTH * 0.405f;
                    y = g.WORLD_HEIGHT * 0.208f;
                    break;
                case 10:
                    x = g.WORLD_WIDTH * 0.452f;
                    y = g.WORLD_HEIGHT * 0.516f;
                    break;
                case 11:
                    x = g.WORLD_WIDTH * 0.278f;
                    y = g.WORLD_HEIGHT * 0.666f;
                    break;
                case 12:
                    x = g.WORLD_WIDTH * 0.475f;
                    y = g.WORLD_HEIGHT * 0.64f;
            }
            dotsArray.insert(i, new Dot(x, y, visible));
            //dot's size is the one that the user inputted in settings
            dotsArray.get(i).setSize(g.dotSize);
        }
    }
}