package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class PiirusMain extends Game {
    //world camera specs
    public int WORLD_WIDTH;
    public int WORLD_HEIGHT;
    //font camera specs
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public float savedX, savedY, savedZ;

    public int arrayLength;
    public float[] xValueArray;
    public float[] yValueArray;

    public float dotSize;

    public float leftXMultiplier;
    public float upYMultiplier;
    public float rightXMultiplier;
    public float downYMultiplier;

    private float maxDifference;

    SpriteBatch batch;

    public boolean sounds;
    public boolean music;

    public Locale locale;
    public I18NBundle myBundle;

    @Override
    public void create() {
        batch = new SpriteBatch();

        arrayLength = 30;

        leftXMultiplier = 1;
        upYMultiplier = 1;
        rightXMultiplier = 1;
        downYMultiplier = 1;

        dotSize = 1;

        xValueArray = new float[arrayLength];
        yValueArray = new float[arrayLength];

        WORLD_WIDTH = 8;
        WORLD_HEIGHT = 5;

        SCREEN_WIDTH = WORLD_WIDTH * 100;
        SCREEN_HEIGHT = WORLD_HEIGHT * 100;

        maxDifference = 2;

        sounds = false;
        music = false;

        locale = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);

        setScreen(new SplashScreen(this));
    }

    public String getLanguage() {
        String l = myBundle.get("language");
        if (l.equalsIgnoreCase("en")) {
            return "en";
        } else {
            return "fi";
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void letsFigurePositionForMePlease(Rectangle rect, float speed) {
        //float speed = 20f;

        //Moving
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            rect.setX(rect.x + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            rect.setX(rect.x - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            rect.setY(rect.y + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            rect.setY(rect.y - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        //Size
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rect.setWidth(rect.width - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rect.setWidth(rect.width + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            rect.setHeight(rect.height + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            rect.setHeight(rect.height - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }
    }

    public float getAdjustedY() {
        //adjustedY = 0;
        if(savedY < 0) {
            //Gdx.app.log("AdjustedY FIRST", "" + (Gdx.input.getAccelerometerY() + Math.abs(savedY)));
            if(checkAcceloX(Gdx.input.getAccelerometerY() + Math.abs(savedY)))
                return (Gdx.input.getAccelerometerY() + Math.abs(savedY));
            else
                return (getAverageX());
        } else {
            //Gdx.app.log("AdjustedY SECOND", "" + (Gdx.input.getAccelerometerY() - savedY));
            if(checkAcceloX(Gdx.input.getAccelerometerY() - savedY))
                return (Gdx.input.getAccelerometerY() - savedY);
            else
                return (getAverageX());
            //return (Gdx.input.getAccelerometerY() - savedY);
        }
    }

    public float getAdjustedZ() {
        //adjustedZ = 0;
        if(savedZ < 0) {
            //Gdx.app.log("AdjustedZ FIRST", "" + (Gdx.input.getAccelerometerZ() + Math.abs(savedZ)));
            if(checkAcceloY(Gdx.input.getAccelerometerZ() + Math.abs(savedZ)))
                return (Gdx.input.getAccelerometerZ() + Math.abs(savedZ));
            else
                return (getAverageY());
        } else {
            //Gdx.app.log("AdjustedZ SECOND", "" + (Gdx.input.getAccelerometerZ() - savedZ));
            if(checkAcceloY(Gdx.input.getAccelerometerZ() - savedZ))
                return (Gdx.input.getAccelerometerZ() - savedZ);
            else
                return (getAverageY());
        }
    }

    public float getAverageY() {
        float helper = 0;
        for (int i = 0; i < arrayLength; i++) {
            helper += yValueArray[i];
        }
        helper = helper/arrayLength;
        return helper;
    }
    public float getAverageX() {
        float helper = 0;
        for (int i = 0; i < arrayLength; i++) {
            helper += xValueArray[i];
        }
        helper = helper/arrayLength;
        return helper;
    }

    public void calibrate(){
        savedX = Gdx.input.getAccelerometerX();
        savedY = Gdx.input.getAccelerometerY();
        savedZ = Gdx.input.getAccelerometerZ();

        for (int i = 0; i < arrayLength; i++) {
            xValueArray[i] = arrayFillX();
            yValueArray[i] = WORLD_HEIGHT / 2;
        }
    }

    private boolean checkAcceloX(float acceloValueX){
        float helper = Math.abs(acceloValueX - getAverageX());
        if(acceloValueX > savedY){ //Liikutaan oikealle?
            return (helper <= maxDifference * rightXMultiplier);
        } else if (acceloValueX < savedY){ //liikutaan vasemmalle?
            return (helper <= maxDifference * leftXMultiplier);
        } else {
            return false;
        }
        /*if(helperX <= maxDifference){
            return true;
        } else{
            return false;
        }*/
    }

    private boolean checkAcceloY(float acceloValueY){
        float helper = Math.abs(acceloValueY - getAverageY());
        if(acceloValueY > savedZ){ //Liikutaan Ylös?
            return (helper <= maxDifference * upYMultiplier);
        } else if (acceloValueY < savedZ){ //liikutaan alas?
            return (helper <= maxDifference * downYMultiplier);
        } else {
            return false;
        }
        /*if(helper <= maxDifference){
            return true;
        } else {
            return false;
        }*/
    }

    private float arrayFillX(){
        if(savedY > 0)
            return (Gdx.input.getAccelerometerY() + Math.abs(savedY));
        else
            return (Gdx.input.getAccelerometerY() - savedY);
    }
    private float arrayFillY(){
        if(savedZ > 0)
            return (Gdx.input.getAccelerometerZ() + Math.abs(savedZ));
        else
            return (Gdx.input.getAccelerometerZ() - savedZ);
    }
}