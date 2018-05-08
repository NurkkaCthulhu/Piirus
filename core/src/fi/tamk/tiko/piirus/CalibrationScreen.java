package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Calibration screen holds the screen that's used for calibration.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class CalibrationScreen extends GestureDetector.GestureAdapter implements Screen {

    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture buttonTexture;
    private Texture backgroundTexture;
    private Texture crosshairTexture;
    private Texture upArrowTexture;
    private Texture downArrowTexture;
    private BitmapFont font;
    private BitmapFont font2;
    private Rectangle menuRect;
    private Rectangle upUpRect;
    private Rectangle downUpRect;
    private Rectangle leftUpRect;
    private Rectangle rightUpRect;
    private Rectangle upDownRect;
    private Rectangle downDownRect;
    private Rectangle leftDownRect;
    private Rectangle rightDownRect;

    private Rectangle crosshairRect;
    private float crosshairSize = 0.01f;
    private boolean help = true;
    private Rectangle helpRect;

    //what is printed on the board
    private static float up = 5;
    private static float down = 5;
    private static float left = 5;
    private static float right = 5;

    //saved calibration multipliers
    private static float leftXMultiplier;
    private static float upYMultiplier;
    private static float rightXMultiplier;
    private static float downYMultiplier;

    //localization
    private String textSensitivity;
    private String textUp;
    private String textDown;
    private String textLeft;
    private String textRight;
    private String textHelp;

    CalibrationScreen(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundTexture = new Texture(Gdx.files.internal("calibration_bg.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        crosshairTexture = new Texture("crosshair.png");
        crosshairTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        downArrowTexture = new Texture("calibration_down.png");
        downArrowTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        upArrowTexture = new Texture("calibration_up.png");
        upArrowTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        upUpRect = new Rectangle(6.3f, game.WORLD_HEIGHT*0.68f, 0.3f, 0.3f);
        downUpRect = new Rectangle(6.3f, game.WORLD_HEIGHT*0.48f, 0.3f, 0.3f);
        leftUpRect = new Rectangle(6.3f, game.WORLD_HEIGHT*0.28f, 0.3f, 0.3f);
        rightUpRect = new Rectangle(6.3f, game.WORLD_HEIGHT*0.08f, 0.3f, 0.3f);
        upDownRect = new Rectangle(7.4f, game.WORLD_HEIGHT*0.68f, 0.3f, 0.3f);
        downDownRect = new Rectangle(7.4f, game.WORLD_HEIGHT*0.48f, 0.3f, 0.3f);
        leftDownRect = new Rectangle(7.4f, game.WORLD_HEIGHT*0.28f, 0.3f, 0.3f);
        rightDownRect = new Rectangle(7.4f, game.WORLD_HEIGHT*0.08f, 0.3f, 0.3f);

        helpRect = new Rectangle(1f, 1f, 6f, 3f);

        crosshairRect = new Rectangle(game.WORLD_WIDTH/2-crosshairSize/2, game.WORLD_HEIGHT/2-crosshairSize/2, crosshairSize, crosshairSize);
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);

        leftXMultiplier = left/10;
        upYMultiplier = up/10;
        rightXMultiplier = right/10;
        downYMultiplier = down/10;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1;

        font2 = generator.generateFont(parameter);
        font2.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();

        updateTexts();
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        game.calibrate();
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
        batch.draw(upArrowTexture, upUpRect.x, upUpRect.y, upUpRect.width, upUpRect.height);
        batch.draw(upArrowTexture, downUpRect.x, downUpRect.y, downUpRect.width, downUpRect.height);
        batch.draw(upArrowTexture, leftUpRect.x, leftUpRect.y, leftUpRect.width, leftUpRect.height);
        batch.draw(upArrowTexture, rightUpRect.x, rightUpRect.y, rightUpRect.width, rightUpRect.height);
        batch.draw(downArrowTexture, upDownRect.x, upDownRect.y, upDownRect.width, upDownRect.height);
        batch.draw(downArrowTexture, downDownRect.x, downDownRect.y, downDownRect.width, downDownRect.height);
        batch.draw(downArrowTexture, leftDownRect.x, leftDownRect.y, leftDownRect.width, leftDownRect.height);
        batch.draw(downArrowTexture, rightDownRect.x, rightDownRect.y, rightDownRect.width, rightDownRect.height);
        batch.draw(buttonTexture, helpRect.x, helpRect.y, helpRect.width, helpRect.height);
        update();

        batch.setProjectionMatrix(fontCamera.combined);

        if(help) {
            helpRect.x = 1f;
            helpRect.y = 1f;
            helpRect.width = 6f;
            helpRect.height = 3f;
            if(game.locale.getLanguage().equals("en")) {
                font2.draw(batch, "Calibrate the game with the right hand numbers.\nBigger number means more sensitive controls.\nYou should be able to get the cross hair to the 10-mark\non the board comfortably.\n\nTap here to continue.",
                        400, 300, 1, 1, true);
            }
            if(game.locale.getLanguage().equals("fi")) {
                font2.draw(batch, "Kalibroi peli käyttämällä oikeanpuoleisia numeroita.\nIsompi numero -> herkempi liikkuminen.\nKatso, että pääset tähtäimellä taulun joka laidassa kymmeneen.\n\nNapauta tästä jatkaaksesi.",
                        400, 300, 1, 1, true);
            }
        } else {
            font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
            font.draw(batch, "" + up, fontSpot(up), game.SCREEN_HEIGHT*0.72f);
            font.draw(batch, "" + down, fontSpot(down), game.SCREEN_HEIGHT*0.52f);
            font.draw(batch, "" + left, fontSpot(left), game.SCREEN_HEIGHT*0.32f);
            font.draw(batch, "" + right, fontSpot(right), game.SCREEN_HEIGHT*0.12f);
            font2.draw(batch, textSensitivity, game.SCREEN_WIDTH*0.75f, game.SCREEN_HEIGHT*0.915f);
            float textOffset = 20;
            font2.draw(batch, textUp, fontSpot(up), game.SCREEN_HEIGHT*0.75f + textOffset);
            font2.draw(batch, textDown, fontSpot(down), game.SCREEN_HEIGHT*0.55f + textOffset);
            font2.draw(batch, textLeft, fontSpot(left), game.SCREEN_HEIGHT*0.35f + textOffset);
            font2.draw(batch, textRight, fontSpot(right), game.SCREEN_HEIGHT*0.15f + textOffset);
            font.draw(batch, textHelp,50, 465, 1, 1, true);
        }

        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            game.rightXMultiplier = rightXMultiplier;
            game.leftXMultiplier = leftXMultiplier;
            game.upYMultiplier = upYMultiplier;
            game.downYMultiplier = downYMultiplier;
            game.setScreen(new SettingsScreen(game, font));
        }
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
        upArrowTexture.dispose();
        downArrowTexture.dispose();
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(helpRect.contains(touchPos.x, touchPos.y)) {
            help = !help;
            helpRect.x = 0f;
            helpRect.y = 4f;
            helpRect.width = 1f;
            helpRect.height = 1f;
        }
        if(menuRect.contains(touchPos.x, touchPos.y)){
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            dispose();
            game.rightXMultiplier = rightXMultiplier;
            game.leftXMultiplier = leftXMultiplier;
            game.upYMultiplier = upYMultiplier;
            game.downYMultiplier = downYMultiplier;
            game.setScreen(new SettingsScreen(game, font));
        }

        if(upUpRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(up < 10) {
                up++;
            }
        }
        if(downUpRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(down < 10) {
                down++;
            }
        }
        if(leftUpRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(left < 10) {
                left++;
            }
        }
        if(rightUpRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(right < 10) {
                right++;
            }
        }
        if(upDownRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(up > 1) {
                up--;
            }
        }
        if(downDownRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(down > 1) {
                down--;
            }
        }
        if(leftDownRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(left > 1) {
                left--;
            }
        }
        if(rightDownRect.contains(touchPos.x, touchPos.y)) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            if(right > 1) {
                right--;
            }
        }
        return false;
    }

    private Vector2 deadzoneInput() {
        float deadzone = 0.5f;

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

    private void moveCross(Vector2 positionVector) {
        float speed = 0.9f;
        //check the cursor's position in Y&X axis and multiply the movement speed by the given calibration values
        if (positionVector.y > 0) {
            crosshairRect.y = game.WORLD_HEIGHT/2 + positionVector.y*(speed + upYMultiplier);
        } else {
            crosshairRect.y = game.WORLD_HEIGHT/2 + positionVector.y*(speed + downYMultiplier);
        }
        if(positionVector.x > 0) {
            crosshairRect.x = game.WORLD_WIDTH/2 + positionVector.x * (speed + rightXMultiplier);
        } else {
            crosshairRect.x = game.WORLD_WIDTH/2 + positionVector.x * (speed + leftXMultiplier);
        }
        //make the cursor be within the calibration bounds
        stayWithinBounds(crosshairRect);
    }

    private void update() {
        updateSensitivities();
        if(!help) {
            batch.draw(crosshairTexture, crosshairRect.x - crosshairRect.width * 20, crosshairRect.y - crosshairRect.height * 20, crosshairRect.width * 40, crosshairRect.height * 40);
        }
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

    private void updateTexts() {
        textSensitivity = game.getMyBundle().get("sensitivity");
        textUp= game.getMyBundle().get("up");
        textDown = game.getMyBundle().get("down");
        textLeft = game.getMyBundle().get("left");
        textRight = game.getMyBundle().get("right");
        textHelp = game.getMyBundle().get("help");
    }

    private int fontSpot(float i) {
        if (i >= 10) {
            return 669;
        } else {
            return 679;
        }
    }
}
