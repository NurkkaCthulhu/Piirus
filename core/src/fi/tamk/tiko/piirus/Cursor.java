package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anu on 8.3.2018.
 */

public class Cursor {

    private static PiirusMain game;
    private static int arraySpot = 0;
    private static float penSize;

    public static void joystickMoving(PiirusMain g, Rectangle rect, float p) {
        penSize = p;
        game.xValueArray[arraySpot] = game.getAdjustedY();
        game.yValueArray[arraySpot] = game.getAdjustedZ();

        if (game.getAdjustedZ() > 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/2);
        } else if (game.getAdjustedZ() < 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/1.6f);
        }
        rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f);

        //move one spot further in the array/reset the count
        if (arraySpot == game.arrayLength-1) {
            arraySpot = 0;
        } else {
            arraySpot++;
        }

        stayWithinBounds(rect);

    }
    //Check that the crosshair stays within bounds
    private static void stayWithinBounds(Rectangle rect) {

        if(rect.y > 3.78f - penSize){
            rect.setY(3.78f - penSize - 0.01f);
        }

        if(rect.y < 0.25f){
            rect.setY(0.25f-0.01f);
        }

        if(rect.x > 6.2f - penSize){
            rect.setX(6.2f - penSize - 0.01f);
        }

        if(rect.x < 1.8f){
            rect.setX(1.8f+0.01f);
        }

    }
}
