package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * The main method that holds core functionality of the game.
 *
 * Useful variables are also included in the main file.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class PiirusMain extends Game {
    //world camera specifications
    public float WORLD_WIDTH;
    public float WORLD_HEIGHT;

    //font camera specifications
    float SCREEN_WIDTH;
    float SCREEN_HEIGHT;

    //Saved accelometer values
    private float savedY, savedZ;

    //Array for calculating averages to reduce shaking of the "cursor"
    int arrayLength;
    private float[] xValueArray;
    private float[] yValueArray;
    int arraySpot;

    //The preferred size of the "dot" and "pen"
    float dotSize;
    float penSize;

    //Calibration multipliers that comes from the calibrationScreen
    float leftXMultiplier;
    float upYMultiplier;
    float rightXMultiplier;
    float downYMultiplier;

    //General sound volumes
    float effectVolume;
    float musicVolume;
    float menuMusicVolume;
    float gameMusicVolume;

    //SpriteBatch to pass around
    private SpriteBatch batch;

    //Indicates if the user wants to hear sound effects
    boolean sounds;
    //Indicates if the user wants to hear music
    boolean music;
    //Indicates if the user wants to track the scores or not
    boolean scoreTracking;

    //Locale and bundles are used for localisation
    Locale locale;
    private I18NBundle myBundle;

    //Sound that plays when the user taps a button
    Sound buttonSound;
    //Sound that plays when the user "clears a dot"
    Sound doneSound;

    //Music heard in all other screens than freeDraw and Level
    Music menuMusic;
    //Music that is heard in FreeDraw and level
    Music gameMusic;

    //User defined preferences, if they do not exist, default preferences will be generated
    Preferences settings;

    @Override
    public void create() {
        batch = new SpriteBatch();

        arrayLength = 30;
        arraySpot = 0;

        leftXMultiplier = 1;
        upYMultiplier = 1;
        rightXMultiplier = 1;
        downYMultiplier = 1;

        dotSize = 1;
        penSize = 1;

        effectVolume = 0.5f;
        musicVolume = 0.25f;
        gameMusicVolume = 0;
        menuMusicVolume = 0;

        xValueArray = new float[arrayLength];
        yValueArray = new float[arrayLength];

        WORLD_WIDTH = 8;
        WORLD_HEIGHT = 5;

        SCREEN_WIDTH = WORLD_WIDTH * 100;
        SCREEN_HEIGHT = WORLD_HEIGHT * 100;

        sounds = true;
        music = true;
        scoreTracking = true;

        locale = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale, "UTF-8");

        setScreen(new SplashScreen(this));
    }

    /**
     * Returns the language from myBundle
     * @return the language defined in current bundle
     */
    String getLanguage() {
        return myBundle.get("language");
    }

    /**
     * Returns the current bundle
     * @return bundle currently in use
     */
    I18NBundle getMyBundle() {
        return myBundle;
    }

    /**
     * Sets the locale accordingly from the given integer
     *
     * @param l 1 = Finnish, 2 = English
     */
    void setLocale(int l) {
        if (l == 0) {
            locale = new Locale("fi", "FI");
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale, "UTF-8");
        } else {
            locale = new Locale("en", "GB");
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale, "UTF-8");
        }
    }

    /**
     * Returns the sprite batch.
     * @return SpriteBatch
     */
    SpriteBatch getBatch() {
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


    /**
     * Returns the adjusted accelometer Y value relative to the saved position
     *
     * @return adjusted value for Y
     */
    float getAdjustedY() {
        //adjustedY = 0;
        if(savedY < 0) {
            //Gdx.app.log("AdjustedY FIRST", "" + (Gdx.input.getAccelerometerY() + Math.abs(savedY)));
                return (Gdx.input.getAccelerometerY() + Math.abs(savedY));
        } else {
            //Gdx.app.log("AdjustedY SECOND", "" + (Gdx.input.getAccelerometerY() - savedY));
                return (Gdx.input.getAccelerometerY() - savedY);
            //return (Gdx.input.getAccelerometerY() - savedY);
        }
    }

    /**
     * Returns the adjusted accelometer Z value relative to the saved position
     *
     * @return adjusted value for Z
     */
    float getAdjustedZ() {
        //adjustedZ = 0;
        if(savedZ < 0) {
            //Gdx.app.log("AdjustedZ FIRST", "" + (Gdx.input.getAccelerometerZ() + Math.abs(savedZ)));
                return (Gdx.input.getAccelerometerZ() + Math.abs(savedZ));
        } else {
            //Gdx.app.log("AdjustedZ SECOND", "" + (Gdx.input.getAccelerometerZ() - savedZ));
                return (Gdx.input.getAccelerometerZ() - savedZ);
        }
    }

    /**
     * Calculates the average value of the yValueArray
     *
     * @return average value of the yValueArray
     */
    float getAverageY() {
        yValueArray[arraySpot] = getAdjustedZ();
        float helper = 0;
        for (int i = 0; i < arrayLength; i++) {
            helper += yValueArray[i];
        }
        helper = helper/arrayLength;
        return helper;
    }
    /**
     * Calculates the average value of the xValueArray
     *
     * @return average value of the xValueArray
     */
    float getAverageX() {
        xValueArray[arraySpot] = getAdjustedY();
        float helper = 0;
        for (int i = 0; i < arrayLength; i++) {
            helper += xValueArray[i];
        }
        helper = helper/arrayLength;
        return helper;
    }

    /**
     * Saves the current accelometer values
     *
     * Also fills the xValueArray and yValueArray with relevant numbers.
     */
    void calibrate(){
        savedY = Gdx.input.getAccelerometerY();
        savedZ = Gdx.input.getAccelerometerZ();

        for (int i = 0; i < arrayLength; i++) {
            xValueArray[i] = arrayFillX();
            yValueArray[i] = WORLD_HEIGHT / 2;
        }
    }

    /**
     * Method used to fill xValueArray with relevant information
     */
    private float arrayFillX(){
        if(savedY > 0)
            return (Gdx.input.getAccelerometerY() + Math.abs(savedY));
        else
            return (Gdx.input.getAccelerometerY() - savedY);
    }
}