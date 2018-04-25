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


    public Cursor (PiirusMain g, float p) {
        game = g;
        penSize = p;
    }
    public static void joystickMoving(Rectangle rect) {

        game.xValueArray[arraySpot] = game.getAdjustedY();
        game.yValueArray[arraySpot] = game.getAdjustedZ();
        //Gdx.app.log("liikkuminen", "" + game.upYMultiplier);
        if (game.getAdjustedZ() > 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/2/game.upYMultiplier);
        } else if (game.getAdjustedZ() < 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/2/game.downYMultiplier);
        }
        if(game.getAdjustedY() > 0) {
            rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f/game.rightXMultiplier);
        } else {
            rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f/game.leftXMultiplier);
        }

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

        float maxYPercent = 0.99f;
        float minYPercent = 0.01f;
        float maxXPercent = 0.99f;
        float minXPercent = 0.01f;

        if(rect.y > game.WORLD_HEIGHT*maxYPercent - penSize){
            rect.setY(game.WORLD_HEIGHT*maxYPercent - penSize - 0.01f);
        }

        if(rect.y < game.WORLD_HEIGHT*minYPercent){
            rect.setY(game.WORLD_HEIGHT*minYPercent - 0.01f);
        }

        if(rect.x > game.WORLD_WIDTH*maxXPercent - penSize){
            rect.setX(game.WORLD_WIDTH*maxXPercent - penSize - 0.01f);
        }

        if(rect.x < game.WORLD_WIDTH*minXPercent){
            rect.setX(game.WORLD_WIDTH*minXPercent + 0.01f);
        }
    }

    public static boolean isPenMoved() {
        return true;
    }
}
