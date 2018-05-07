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
    private static Rectangle rect;


    public Cursor (PiirusMain g, float p, Rectangle rectangle) {
        game = g;
        penSize = p;
        rect = rectangle;
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
    public static void move() {

        //check the cursor's position in Y&X axis and multiply the movement speed by the given calibration values
        if (deadzoneInput().y > 0) {
            rect.y = game.WORLD_HEIGHT/2 + deadzoneInput().y*(0.6f + game.upYMultiplier);
        } else {
            rect.y = game.WORLD_HEIGHT/2 + deadzoneInput().y*(0.6f + game.downYMultiplier);
        }
        if(deadzoneInput().x > 0) {
            rect.x = game.WORLD_WIDTH/2 + deadzoneInput().x * (1 + game.rightXMultiplier);
        } else {
            rect.x = game.WORLD_WIDTH/2 + deadzoneInput().x * (1 + game.leftXMultiplier);
        }
        //make the cursor be within the calibration bounds
        stayWithinBounds(rect);
        if (game.arraySpot == game.arrayLength-1) {
            game.arraySpot = 0;
        } else {
            game.arraySpot++;
        }
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

    public static Vector2 deadzoneInput() {
        float deadzone = 0.1f;

        //averaged input vector
        Vector2 stickInput = new Vector2(game.getAverageX(),game.getAverageY());

        //length of the deadzone vector
        Vector2 deadzoneVector = new Vector2(game.getAverageX(),game.getAverageY());
        deadzoneVector.nor().scl(deadzone, deadzone);

        //the position of the cursor
        Vector2 positionVector = new Vector2(game.getAverageX(),game.getAverageY());
        positionVector.sub(deadzoneVector);

        if(stickInput.len() < deadzone) {
            positionVector = new Vector2(0,0);
        }

        return positionVector;
    }


    public static boolean isPenMoved() {
        return true;
    }
}
