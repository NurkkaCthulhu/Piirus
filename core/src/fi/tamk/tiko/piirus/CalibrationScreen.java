package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anbu on 10.4.2018.
 */

public class CalibrationScreen extends GestureDetector.GestureAdapter implements Screen {

    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture backgroundTexture;
    private Texture crosshairTexture;
    private BitmapFont font;
    private Rectangle menuRect;
    private Rectangle upRect;
    private Rectangle downRect;
    private Rectangle leftRect;
    private Rectangle rightRect;
    //private Rectangle calibrationStartRect;
    private Rectangle crosshairRect;
    private float crosshairSize = 0.01f;
    private Vector3 crosshairVector;

    //for the average x&y value table
    private int arrayySpot = 0;

    private boolean calibrate = true;

    //what is printed on the board
    private static float up = 5;
    private static float down = 5;
    private static float left = 5;
    private static float right = 5;

    //saved calibration multipliers
    public static float leftXMultiplier;
    public static float upYMultiplier;
    public static float rightXMultiplier;
    public static float downYMultiplier;

    public float holdTime = 2;

    //used to count that the player is relatively still while calibrating
    private float movement = 0;
    private float newX;
    private float newY;
    private float oldX = 0;
    private float oldY = 0;
    private float stillnessCounter = 0;


    public CalibrationScreen(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        buttonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundTexture = new Texture(Gdx.files.internal("calibration_bg.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crosshairTexture = new Texture("crosshair.png");
        crosshairTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        upRect = new Rectangle(6.55f, game.WORLD_HEIGHT*0.75f, 1.1f, 0.5f);
        downRect = new Rectangle(6.55f, game.WORLD_HEIGHT*0.6f, 1.1f, 0.5f);
        leftRect = new Rectangle(6.55f, game.WORLD_HEIGHT*0.45f, 1.1f, 0.5f);
        rightRect = new Rectangle(6.55f, game.WORLD_HEIGHT*0.30f, 1.1f, 0.5f);
        crosshairRect = new Rectangle(game.WORLD_WIDTH/2-crosshairSize/2, game.WORLD_HEIGHT/2-crosshairSize/2, crosshairSize, crosshairSize);
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        //calibrationStartRect = new Rectangle(7,0.1f, 1, 1);
        crosshairVector = new Vector3(0,0,0);


        leftXMultiplier = left/10;
        upYMultiplier = up/10;
        rightXMultiplier = right/10;
        downYMultiplier = down/10;

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f,0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture,0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
        //batch.draw(buttonTexture, calibrationStartRect.x, calibrationStartRect.y, calibrationStartRect.width, calibrationStartRect.height);
        if(calibrate) {
            update();
        }
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        //font.draw(batch, "Aloita", 700, game.SCREEN_HEIGHT*0.15f);
        font.draw(batch, "" + up, fontSpot(up), game.SCREEN_HEIGHT*0.8125f);
        font.draw(batch, "" + down, fontSpot(down), game.SCREEN_HEIGHT*0.662f);
        font.draw(batch, "" + left, fontSpot(left), game.SCREEN_HEIGHT*0.5115f);
        font.draw(batch, "" + right, fontSpot(right), game.SCREEN_HEIGHT*0.361f);
        batch.end();

        //Gdx.app.log("Pure AdjustedZ", "" + Gdx.input.getAccelerometerZ());
        //Gdx.app.log("Pure AdjustedY", "" + Gdx.input.getAccelerometerY());

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.menuMusic.pause();
    }

    @Override
    public void resume() {
        game.menuMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        buttonTexture.dispose();
        backgroundTexture.dispose();
        crosshairTexture.dispose();
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y)){
            game.buttonSound.play(game.effectVolume);
            dispose();
            game.rightXMultiplier = rightXMultiplier;
            game.leftXMultiplier = leftXMultiplier;
            game.upYMultiplier = upYMultiplier;
            game.downYMultiplier = downYMultiplier;
            game.setScreen(new SettingsScreen(game, font));
        }
        if(upRect.contains(touchPos.x, touchPos.y)) {
            game.buttonSound.play(game.effectVolume);
            if(up > 1) {
                up--;
            } else {
                up = 10;
            }

        }
        if(downRect.contains(touchPos.x, touchPos.y)) {
            game.buttonSound.play(game.effectVolume);
            if(down > 1) {
                down--;
            } else {
                down = 10;
            }
        }
        if(leftRect.contains(touchPos.x, touchPos.y)) {
            game.buttonSound.play(game.effectVolume);
            if(left > 1) {
                left--;
            } else {
                left = 10;
            }
        }
        if(rightRect.contains(touchPos.x, touchPos.y)) {
            game.buttonSound.play(game.effectVolume);
            if(right > 1) {
                right--;
            } else {
                right = 10;
            }
        }
        /*if(calibrationStartRect.contains(touchPos.x, touchPos.y)) {
            calibrate = true;
        }*/
        return false;
    }

    private void moveCrosshair(Rectangle rect, Vector3 movement) {
        leftXMultiplier = left/10;
        upYMultiplier = up/10;
        rightXMultiplier = right/10;
        downYMultiplier = down/10;

        game.xValueArray[arrayySpot] = game.getAdjustedY();
        game.yValueArray[arrayySpot] = game.getAdjustedZ();

        if (game.getAdjustedZ() > 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/2/upYMultiplier);
        } else if (game.getAdjustedZ() < 0) {
            rect.y = game.WORLD_HEIGHT/2 + (game.getAverageY()/2/downYMultiplier);
        }
        if(game.getAdjustedY() > 0) {
            rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f/rightXMultiplier);
        } else if (game.getAdjustedY() < 0) {
            rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f/leftXMultiplier);
        }
        //rect.x = game.WORLD_WIDTH/2 + (game.getAverageX()/3.5f);

        //move one spot further in the array/reset the count
        if (arrayySpot == game.arrayLength-1) {
            arrayySpot = 0;
        } else {
            arrayySpot++;
        }

        stayWithinBounds(rect);
        //calibrateUp();
        /*if(yStayedStill()) {
            Gdx.app.log("Y liike", "Y PAIKOILLAAN!");
            stillnessCounter += Gdx.graphics.getDeltaTime();
        } else {
            stillnessCounter = 0;
        }
        if(xStayedStill()) {
            Gdx.app.log("X liike", "X PAIKOILLAAN!");
            stillnessCounter += Gdx.graphics.getDeltaTime();
        } else {
            stillnessCounter = 0;
        }*/
    }


    private Vector2 deadzoneInput() {
        float deadzone = 1.5f;

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

    //Check that the crosshair stays within bounds
    private void stayWithinBounds(Rectangle rect) {

        float maxYPercent = 0.945f;
        float minYPercent = 0.0625f;
        float maxXPercent = 0.775f;
        float minXPercent = 0.225f;

        if(rect.y > game.WORLD_HEIGHT*maxYPercent - crosshairSize){
            rect.setY(game.WORLD_HEIGHT*maxYPercent - crosshairSize - 0.01f);
        }

        if(rect.y < game.WORLD_HEIGHT*minYPercent){
            rect.setY(game.WORLD_HEIGHT*minYPercent - 0.01f);
        }

        if(rect.x > game.WORLD_WIDTH*maxXPercent - crosshairSize){
            rect.setX(game.WORLD_WIDTH*maxXPercent - crosshairSize - 0.01f);
        }

        if(rect.x < game.WORLD_WIDTH*minXPercent){
            rect.setX(game.WORLD_WIDTH*minXPercent + 0.01f);
        }

    }

    private boolean yStayedStill() {
        boolean still = false;
        newY = game.getAverageY();
        if(Math.abs(oldY-newY) < 0.005f) {
            still = true;
        } else {
            still = false;
        }
        oldY = game.getAverageY();
        return still;
    }
    private boolean xStayedStill() {
        boolean still = false;
        newX = game.getAverageX();
        if(Math.abs(oldX-newX) < 0.005f) {
            still = true;
        } else {
            still = false;
        }
        oldX = game.getAverageX();
        return still;
    }

    private void moveCross(Vector2 positionVector) {
        //check the cursor's position in Y&X axis and multiply the movement speed by the given calibration values
        if (positionVector.y > 0) {
            crosshairRect.y = game.WORLD_HEIGHT/2 + positionVector.y*(0.5f + upYMultiplier);
        } else {
            crosshairRect.y = game.WORLD_HEIGHT/2 + positionVector.y*(0.5f + downYMultiplier);
        }
        if(positionVector.x > 0) {
            crosshairRect.x = game.WORLD_WIDTH/2 + positionVector.x * (0.5f + rightXMultiplier);
        } else {
            crosshairRect.x = game.WORLD_WIDTH/2 + positionVector.x * (0.5f + leftXMultiplier);
        }
        //make the cursor be within the calibration bounds
        stayWithinBounds(crosshairRect);
    }

    public void update() {
        updateSensitivities();
        batch.draw(crosshairTexture, crosshairRect.x-crosshairRect.width*20, crosshairRect.y-crosshairRect.height*20, crosshairRect.width*40, crosshairRect.height*40);
        //moveCrosshair(crosshairRect, crosshairVector);
        moveCross(deadzoneInput());
        if (game.arraySpot == game.arrayLength-1) {
            game.arraySpot = 0;
        } else {
            game.arraySpot++;
        }
    }
    private void updateSensitivities(){
        leftXMultiplier = left/10;
        upYMultiplier = up/10;
        rightXMultiplier = right/10;
        downYMultiplier = down/10;
    }

    public void calibrateUp() {
        if(yStayedStill()&&crosshairRect.x > 2.73f) {
            Gdx.app.log("Y liike", "Y PAIKOILLAAN!");
            stillnessCounter += Gdx.graphics.getDeltaTime();
            if (stillnessCounter >= holdTime) {
                upYMultiplier = game.getAverageY()/1.6f;
                stillnessCounter = 0;
                calibrateDown();
            }
        } else {
            stillnessCounter = 0;
        }
    }
    public void calibrateDown() {
        if(yStayedStill()&&crosshairRect.x < 2.27f) {
            Gdx.app.log("Y liike", "Y PAIKOILLAAN!");
            stillnessCounter += Gdx.graphics.getDeltaTime();
            if (stillnessCounter >= holdTime) {
                upYMultiplier = game.getAverageY()/1.6f;
                stillnessCounter = 0;
                calibrateLeft();
            }
        } else {
            stillnessCounter = 0;
        }
    }
    public void calibrateLeft() {

    }
    public void calibrateRight() {

    }

    public int fontSpot(float i) {
        if (i >= 10) {
            return 659;
        } else {
            return 669;
        }
    }
}
