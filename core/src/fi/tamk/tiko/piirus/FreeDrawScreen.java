package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Free draw screen contains all information for drawing freely with cursor.
 *
 * User can manipulate the "ink" size and clear it.
 * This class does not use the values from CalibrationScreen, since the cursor is "unlocked"
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class FreeDrawScreen extends GestureDetector.GestureAdapter implements Screen {
    //Main file that contains useful variables and methods.
    private PiirusMain game;
    //SpriteBatch which is used for drawing the textures
    private SpriteBatch batch;

    //camera is used to render everything else than fonts
    private OrthographicCamera camera;
    //fontCamera is used to render fonts on the screen
    private OrthographicCamera fontCamera;

    //Textures for buttons and background
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture backgroundTexture;
    private Texture pauseFill;
    private Texture pauseBg;

    //Rectangles are used to easily store positions and sizes of the buttons
    private Rectangle menuRect;
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Rectangle clearRectanlge;
    private Rectangle pauseContinue;
    private Rectangle pauseBack;

    //This is the main font which is made in splashScreen
    private BitmapFont font;

    //penDots array holds all the "ink" the player has drawn
    private ArrayList<Rectangle> penDots;

    //Used to save the manipulated size of the "ink"
    private Float penSize = 0.1f;

    //the "pen" of the player
    private Texture penTexture;

    //texture for the "ink"
    private Texture penDot;

    //tells if the game is paused or not
    private boolean paused;

    /**
     * The constructor of the class.
     *
     * @param g the main game object(can be used to call all sorts of things)
     * @param f the main font used in the game
     */
    FreeDrawScreen(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonPressedTexture = new Texture(Gdx.files.internal("levelbutton_pressed.png"));
        backgroundTexture = new Texture(Gdx.files.internal("levelbg.png"));
        pauseBg = new Texture(Gdx.files.internal("pauseBg.png"));
        pauseFill = new Texture(Gdx.files.internal("pauseFill.png"));
        penTexture = new Texture(Gdx.files.internal("pen.png"), true);
        penDot = new Texture(Gdx.files.internal("dot.png"));
        penTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        menuRect = new Rectangle(0,game.WORLD_HEIGHT, 0.4f, 0.4f);
        menuRect.setPosition(0, menuRect.y - menuRect.height);
        penRectangle = new Rectangle(game.WORLD_WIDTH / 2, game.WORLD_HEIGHT / 2, 0.2f, 0.2f);
        penSizeMinusRectangle = new Rectangle(0, 0, 0.6f, 0.6f);
        penSizePlusRectangle = new Rectangle(1, 0, 0.6f, 0.6f);
        clearRectanlge = new Rectangle(camera.viewportWidth - 0.6f, 0, 0.6f, 0.6f);
        pauseContinue = new Rectangle(4.3f, 1.3f, 2.5f, 0.63f);
        pauseBack = new Rectangle(1.16f, 1.3f, 2.5f, 0.63f);

        penDots = new ArrayList<Rectangle>();

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
        if(!paused){
            batch.draw(buttonTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);

            penDraw();
            batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width*6, penRectangle.height*6);
            batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
            batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);

            batch.draw(buttonTexture, clearRectanlge.x, clearRectanlge.y, clearRectanlge.width, clearRectanlge.height);

            holdButtonTouched();

            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
            font.draw(batch, "-", penSizeMinusRectangle.x*100 + 20, (penSizeMinusRectangle.y + penSizeMinusRectangle.getHeight() / 2)*100 );
            font.draw(batch, "+", penSizePlusRectangle.x*100 + 20, (penSizePlusRectangle.y + penSizePlusRectangle.getHeight() / 2)*100 );
            font.draw(batch, "Tyh.", clearRectanlge.x*100, (clearRectanlge.y + clearRectanlge.getHeight() / 2)*100 );

            topdownMoving(penRectangle);
        } else {
            batch.draw(pauseFill, 0, 0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
            batch.draw(pauseBg, 1, 1, 6, 3);
            batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
            batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);

            holdButtonTouched();

            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, game.getMyBundle().get("continue"), pauseContinue.x * 100 + pauseContinue.width * 100 / 2f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f, 1, 1, true);
            font.draw(batch, game.getMyBundle().get("mainMenu"), pauseBack.x * 100 + pauseBack.width * 100 / 2f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f, 1, 1, true);
            font.draw(batch, game.getMyBundle().get("pause"), 350, 300);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        paused = true;
        if(game.music)
            game.gameMusic.pause();
    }

    @Override
    public void resume() {
        if(game.music)
            game.gameMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        buttonTexture.dispose();
        backgroundTexture.dispose();
        font.dispose();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y) && !paused){
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            paused = true;
        } else if (clearRectanlge.contains(touchPos.x, touchPos.y) && !paused) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            clearLine();
        } else if (pauseContinue.contains(touchPos.x, touchPos.y) && paused){
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            paused = false;
        } else if (pauseBack.contains(touchPos.x,touchPos.y) && paused) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            dispose();
            game.setScreen(new MainMenu(game));
        }
        return false;
    }

    /**
     * Handles moving in "unlocked state"
     *
     * Accepts a rectangle which is inside the screen, if the rectangle is not in the screen,
     * it will be placed to the closest edge.
     *
     * @param rect the Rectangle which needs to move
     */
    private void topdownMoving(Rectangle rect){
        Vector3 movement = new Vector3();
        boolean moved = false;
        movement.x = game.getAdjustedY()/100;
        movement.y = game.getAdjustedZ()/100;

        if(rect.y < game.WORLD_HEIGHT - penSize && rect.y > 0){
            rect.setY(rect.y + movement.y);
            moved = true;
        }

        if(rect.y > game.WORLD_HEIGHT - penSize){
            rect.setY(game.WORLD_HEIGHT - penSize - 0.01f);
        }

        if(rect.y < 0){
            rect.setY(0.01f);
        }

        if(rect.x < game.WORLD_WIDTH - penSize && rect.x > 0){
            rect.setX(rect.x + movement.x);
            moved = true;
        }

        if(rect.x > game.WORLD_WIDTH - penSize){
            rect.setX(game.WORLD_WIDTH - penSize - 0.01f);
        }

        if(rect.x < 0){
            rect.setX(0.01f);
        }

        if(moved){
            addPaint(rect);
        }
    }

    /**
     * Adds "ink" at the position of the given rectangle
     *
     * @param rect Rectangle at which location "ink" should be added
     */
    private void addPaint(Rectangle rect){
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
    }

    /**
     * Draws the "ink" on the screen.
     */
    private void penDraw(){
        if(!penDots.isEmpty()){
            for(Rectangle r : penDots){
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    /**
     * Clears the "ink" from the screen by emptying penDots array
     */
    private void clearLine(){
        penDots.clear();
    }

    /**
     * Handles the functions for holding down buttons, also draws pressed buttons on the screen.
     */
    private void holdButtonTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(penSizePlusRectangle.contains(touchPos.x, touchPos.y) && !paused){
                penSize = penSize + 0.01f;
            }
            if(penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && penSize > 0.01f && !paused){
                penSize = penSize - 0.01f;
            }
            if(menuRect.contains(touchPos.x, touchPos.y) && !paused){
                batch.draw(buttonPressedTexture, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
            }
            if(penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && !paused){
                batch.draw(buttonPressedTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);
            }
            if(penSizePlusRectangle.contains(touchPos.x, touchPos.y) && !paused){
                batch.draw(buttonPressedTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
            }
            if(clearRectanlge.contains(touchPos.x, touchPos.y) && !paused){
                batch.draw(buttonPressedTexture, clearRectanlge.x, clearRectanlge.y, clearRectanlge.width, clearRectanlge.height);
            }
            if(pauseBack.contains(touchPos.x, touchPos.y) && paused) {
                batch.draw(buttonPressedTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
            }
            if(pauseContinue.contains(touchPos.x, touchPos.y) && paused) {
                batch.draw(buttonPressedTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
            }
        }
    }

}