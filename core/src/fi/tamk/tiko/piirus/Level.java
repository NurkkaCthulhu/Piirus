package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Level class is the basic level structure that gets the selected level's dots from their respective classes.
 *
 * Level sees that the dots work, pencil is rendered, pause menu can be accessed, oversees the game state and ends the level.
 *
 * @author Santun Muijat
 * @version 2018.0508
 * @since 1.0
 */

public class Level extends GestureDetector.GestureAdapter implements Screen {

    private PiirusMain game;
    private BitmapFont font;
    private SpriteBatch batch;

    //camera
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;

    //textures
    private Texture penTexture;
    private Texture penDot;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture levelbg;
    private Texture finishPic;          //the beautified picture at the end
    private Texture pauseBg;
    private Texture pauseFill;
    private Texture pauseButtonTexture;
    private Texture tutorialButtonTexture;
    private Texture tutorialEnTexture;
    private Texture tutorialFiTexture;

    //rectangles
    private Rectangle penRectangle;
    private Rectangle pauseMenuRectangle;
    private Rectangle pauseContinue;
    private Rectangle pauseBack;
    private Rectangle tutorialRect;
    private Rectangle showTutoRect;

    private Float penSize = 0.1f;
    private Float finishedTimer;
    private Float tapToContinueHeight;
    private Float playerTime;
    private Float bestTime;
    //dots are in an array
    private ArrayList<Rectangle> penDots;
    private Cursor cursor;
    private int levelNumber;

    private boolean paused;
    private boolean newRecord;
    private boolean pausedWithBack;
    private boolean scoreCounted;
    private boolean showTutorial;

    private static int dotsCleared = 0;
    private int dotSoundsPlayed;
    private int dotCount;       //how many dots there are in the level
    private int playerMins;
    private int playerSecs;
    private int bestMins;
    private int bestSecs;

    //Dots
    private Array<Dot> dotArray;

    //localization String
    private String textPaused;
    private String textContinue;
    private String textLevelSelect;
    private String textWin;
    private String textRecord;
    private String textTapAnywhere;
    private String textBestTime;
    private String textYourTime;

    private Preferences bestTimes;

    private DecimalFormat df;

    /**
     * Constructor for the level class.
     * @param g the main game object(can be used to call all sorts of things)
     * @param f the main font used in the game
     * @param number which level was chosen
     */
    public Level(PiirusMain g, BitmapFont f, int number) {
        game = g;
        font = f;
        levelNumber = number;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        penTexture = new Texture(Gdx.files.internal("pen.png"), true);
        penTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        penDot = new Texture(Gdx.files.internal("dot.png"));
        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonPressedTexture = new Texture(Gdx.files.internal("levelbutton_pressed.png"));
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        levelbg = new Texture(Gdx.files.internal("levelbg.png"));
        pauseBg = new Texture(Gdx.files.internal("pauseBg.png"));
        pauseFill = new Texture(Gdx.files.internal("pauseFill.png"));
        tutorialButtonTexture = new Texture(Gdx.files.internal("tutorialButton.png"), true);
        tutorialEnTexture = new Texture(Gdx.files.internal("tutorialPicEn.png"), true);
        tutorialEnTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        tutorialFiTexture = new Texture(Gdx.files.internal("tutorialPicFi.png"), true);
        tutorialFiTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        penRectangle = new Rectangle(game.WORLD_WIDTH / 2, game.WORLD_HEIGHT / 2, 0.1f, 0.1f);
        pauseMenuRectangle = new Rectangle(0, game.WORLD_HEIGHT - 0.6f, 0.6f, 0.6f);
        tutorialRect = new Rectangle(0, game.WORLD_HEIGHT - 1.22f, 0.6f, 0.6f);
        pauseContinue = new Rectangle(4.3f, 1.3f, 2.5f, 0.63f);
        pauseBack = new Rectangle(1.16f, 1.3f, 2.5f, 0.63f);
        showTutoRect = new Rectangle(0,0, game.WORLD_WIDTH, game.WORLD_HEIGHT);

        penDots = new ArrayList<Rectangle>();

        paused = false;
        pausedWithBack = false;
        newRecord = false;
        scoreCounted = false;
        showTutorial = false;

        finishedTimer = 0f;
        tapToContinueHeight = 0f;
        dotSoundsPlayed = 0;

        cursor = new Cursor(game, penSize, penRectangle);

        bestTimes = Gdx.app.getPreferences("bestTimes");
        playerTime = 0f;

        levelSelect();
        updateLevelTexts();

        if(game.music)
            game.gameMusic.play();
        game.gameMusic.setVolume(game.gameMusicVolume);
        game.gameMusic.setLooping(true);

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        game.calibrate();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        fadeMusicIn();
        fadeMusicOut();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelbg, 0, 0, levelbg.getWidth() / 100, levelbg.getHeight() / 100);
        if (!levelFinished() && !paused) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
                paused = true;
                pausedWithBack = true;
            }
            penDraw();
            Cursor.move();
            //draws all the dots on screen
            for (int i = 0; i < dotCount; i++) {
                dotArray.get(i).sprite.draw(batch);
            }

            batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width * 10 * game.penSize, penRectangle.height * 10 * game.penSize);

            if (Cursor.isPenMoved() && dotsCleared > 0) {
                addPaint(penRectangle);
                playerTime += Gdx.graphics.getRawDeltaTime();
            }

        } else if (levelFinished() && !paused) {
            if (finishedTimer < 60)
                finishedTimer += Gdx.graphics.getRawDeltaTime();
            if (finishedTimer < 1)
                batch.setColor(1, 1, 1, finishedTimer);
            batch.draw(finishPic, game.WORLD_WIDTH * 0.1f, 0, finishPic.getWidth() / 110, finishPic.getHeight() / 110);
            batch.setColor(Color.WHITE);

            if (finishedTimer > 59) {
                if(!scoreCounted)
                    calculateScore();
                batch.draw(pauseBg, 1, 1, 6, 3);
                batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
                batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
                showPressedButtons();
                batch.setProjectionMatrix(fontCamera.combined);
                font.draw(batch, textContinue, pauseContinue.x * 100 + pauseContinue.width * 100 / 2f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f, 1, 1, true);
                font.draw(batch, textLevelSelect, pauseBack.x * 100 + pauseBack.width * 100 / 2f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f, 1, 1, true);
                font.draw(batch, textWin, 400, 345, 1, 1, true);
                //if the game tracks score, show score at the end
                if(game.scoreTracking){
                    if(playerTime < bestTime){
                        font.draw(batch, textRecord, 400, 300, 1, 1, true);
                        if(playerTime > 59)
                            font.draw(batch, playerMins + ":" + playerSecs, 400, 275, 1, 1, true);
                        else
                            font.draw(batch, "" + df.format(playerTime), 400, 275, 1, 1, true);
                    } else {
                        if(bestTime > 59)
                            font.draw(batch, textBestTime + bestMins + ":" + bestSecs, 400, 300, 1, 1, true);
                        else
                            font.draw(batch, textBestTime + df.format(bestTime), 400, 300, 1, 1, true);
                        if(playerTime > 59)
                            font.draw(batch, textYourTime + playerMins + ":" + playerSecs, 400, 275, 1, 1, true);
                        else
                            font.draw(batch, textYourTime + df.format(playerTime), 400, 275, 1, 1, true);
                    }
                }
                batch.setProjectionMatrix(camera.combined);
            }
            if (finishedTimer > 4 && finishedTimer < 59) {
                batch.setProjectionMatrix(fontCamera.combined);
                if (tapToContinueHeight < 40)
                    tapToContinueHeight += 0.25f;
                if (finishedTimer > 4)
                    font.setColor(1, 1, 1, (finishedTimer - 4) / 2);
                font.draw(batch, textTapAnywhere, 100, tapToContinueHeight);
                font.setColor(Color.WHITE);
                batch.setProjectionMatrix(camera.combined);
            }
        }
        batch.draw(pauseButtonTexture, pauseMenuRectangle.x, pauseMenuRectangle.y, pauseMenuRectangle.width, pauseMenuRectangle.height);
        batch.draw(tutorialButtonTexture, tutorialRect.x, tutorialRect.y, tutorialRect.width, tutorialRect.height);
        //draws the tutorial picture
        if(showTutorial == true) {
            if(game.getMyBundle().get("language").equalsIgnoreCase("fi")) {
                batch.draw(tutorialFiTexture, showTutoRect.x, showTutoRect.y, showTutoRect.width, showTutoRect.height);
            } else {
                batch.draw(tutorialEnTexture, showTutoRect.x, showTutoRect.y, showTutoRect.width, showTutoRect.height);
            }
        }
        if (paused) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) && !pausedWithBack){
                dotsCleared = 0;
                dotSoundsPlayed = 0;
                tapToContinueHeight = 0f;
                game.menuMusic.play();
                game.setScreen(new LevelSelect(game, font));
            }
            batch.draw(pauseFill, 0, 0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
            batch.draw(pauseBg, 1, 1, 6, 3);
            batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
            batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
            showPressedButtons();
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, textContinue, pauseContinue.x * 100 + pauseContinue.width * 100 / 2f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f, 1, 1, true);
            font.draw(batch, textLevelSelect, pauseBack.x * 100 + pauseBack.width * 100 / 2f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f, 1, 1, true);
            font.draw(batch, textPaused, 350, 300);
        }

        batch.end();

        pausedWithBack = false;

        if (!paused && dotsCleared < dotCount) {
            dotArray.get(dotsCleared).setVisible();
            dotArray.get(dotsCleared).checkCollisions(penRectangle);
            canIPlaySound();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            playerTime += 10f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            dotsCleared = dotCount;
            finishedTimer = 60f;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.gameMusic.pause();
        if (dotsCleared != dotCount)
            paused = true;
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
        penDot.dispose();
        penTexture.dispose();
        levelbg.dispose();
        finishPic.dispose();
        pauseFill.dispose();
        pauseBg.dispose();
        pauseButtonTexture.dispose();
        buttonPressedTexture.dispose();
        buttonTexture.dispose();
        tutorialButtonTexture.dispose();
        tutorialEnTexture.dispose();
        tutorialFiTexture.dispose();
    }

    /**
     * Draws the ink dots from the penDot array.
     */
    private void penDraw() {
        if (!penDots.isEmpty()) {
            for (Rectangle r : penDots) {
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    /**
     * Adds an ink dot to the penDots array.
     * @param rect the ink dot's position (=penRectangle's position)
     */
    private void addPaint(Rectangle rect) {
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
    }

    /**
     * Clears all the ink dots from the array -> no more ink on screen either.
     */
    private void clearLine() {
        penDots.clear();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if (pauseMenuRectangle.contains(touchPos.x, touchPos.y) && !paused) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            paused = true;
        }
         if (showTutoRect.contains(touchPos.x, touchPos.y)) {
            showTutorial = false;
            showTutoRect.y = 10f;
         }
        if (tutorialRect.contains(touchPos.x, touchPos.y) && !paused) {
            if(game.sounds) {
                game.buttonSound.play(game.effectVolume);
            }
            showTutorial = true;
            showTutoRect.y = 0;
        }
        if (pauseContinue.contains(touchPos.x, touchPos.y) && paused) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            game.calibrate();
            paused = false;
        } else if (pauseContinue.contains(touchPos.x, touchPos.y) && finishedTimer >= 59) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            dotSoundsPlayed = 0;
            updateScores();
            levelNumber++;
            finishedTimer = 0f;
            tapToContinueHeight = 0f;
            clearLine();
            scoreCounted = false;
            levelSelect();
        }
        if (pauseBack.contains(touchPos.x, touchPos.y) && paused) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            dotSoundsPlayed = 0;
            tapToContinueHeight = 0f;
            game.menuMusic.play();
            game.setScreen(new LevelSelect(game, font));
        } else if (pauseBack.contains(touchPos.x, touchPos.y) && finishedTimer >= 59) {
            if(game.sounds)
                game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            dotSoundsPlayed = 0;
            tapToContinueHeight = 0f;
            game.menuMusic.play();
            updateScores();
            game.setScreen(new LevelSelect(game, font));
        }
        if (finishedTimer > 0.5f && finishedTimer < 59)
            finishedTimer = 60f;
        //Gdx.app.log("TouchPos", "X:" + (touchPos.x / 8) + "||||||Y:" + (touchPos.y / 5));
        return false;
    }

    /**
     * Shows a different graphic for when player presses a button
     */
    private void showPressedButtons(){
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (pauseBack.contains(touchPos.x, touchPos.y)) {
                batch.draw(buttonPressedTexture, pauseBack.x, pauseBack.y, pauseBack.getWidth(), pauseBack.getHeight());
            }
            if (pauseContinue.contains(touchPos.x, touchPos.y)) {
                batch.draw(buttonPressedTexture, pauseContinue.x, pauseContinue.y, pauseContinue.getWidth(), pauseContinue.getHeight());
            }
        }
    }

    /**
     * Tells if the level is finished.
     * @return level's status (true if all dots are cleared, false if the level is still going)
     */
    private boolean levelFinished() {
        return dotsCleared == dotCount;
    }

    /**
     * Tells the game that a dot was cleared.
     */
    static void setDotsCleared() {
        dotsCleared++;
    }

    /**
     * Used to load the correct dot data to the level + which finishing picture is used.
     */
    private void levelSelect() {
        playerTime = 0f;
        switch (levelNumber) {
            case 1:
                LevelOne objectOne = new LevelOne(game);
                dotCount = objectOne.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectOne.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("one");
                finishPic = new Texture("levels/bread.png");
                showTutorial = true;
                break;
            case 2:
                LevelTwo objectTwo = new LevelTwo(game);
                dotCount = objectTwo.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectTwo.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("two");
                finishPic = new Texture("levels/tomato.png");
                break;
            case 3:
                LevelThree objectThree = new LevelThree(game);
                dotCount = objectThree.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectThree.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("three");
                finishPic = new Texture("levels/flower.png");
                break;
            case 4:
                LevelFour objectFour = new LevelFour(game);
                dotCount = objectFour.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectFour.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("four");
                finishPic = new Texture("levels/cat.png");
                break;
            case 5:
                LevelFive objectFive = new LevelFive(game);
                dotCount = objectFive.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectFive.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("five");
                finishPic = new Texture("levels/snowflake.png");
                break;
            case 6:
                LevelSix objectSix = new LevelSix(game);
                dotCount = objectSix.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectSix.dotsArray.get(i));
                }
                bestTime = bestTimes.getFloat("six");
                finishPic = new Texture("levels/clock.png");
                break;
            default:
                dotsCleared = 0;
                game.setScreen(new LevelSelect(game, font));
        }
    }

    /**
     * Updates the player's score level by level.
     */
    private void updateScores(){
        switch (levelNumber){
            case 1:
                if(playerTime < bestTime){
                    bestTimes.putFloat("one", playerTime);
                    newRecord = true;
                }
                break;
            case 2:
                if(playerTime < bestTime){
                    bestTimes.putFloat("two", playerTime);
                    newRecord = true;
                }
                break;
            case 3:
                if(playerTime < bestTime){
                    bestTimes.putFloat("three", playerTime);
                    newRecord = true;
                }
                break;
            case 4:
                if(playerTime < bestTime){
                    bestTimes.putFloat("four", playerTime);
                    newRecord = true;
                }
                break;
            case 5:
                if(playerTime < bestTime){
                    bestTimes.putFloat("five", playerTime);
                    newRecord = true;
                }
                break;
            case 6:
                if(playerTime < bestTime){
                    bestTimes.putFloat("six", playerTime);
                    newRecord = true;
                }
                break;
            default:
                newRecord = false;
        }
        if(newRecord && game.scoreTracking)
            bestTimes.flush();
    }

    /**
     * Updates the level's text to match with the chosen language.
     */
    private void updateLevelTexts() {
        textPaused = game.getMyBundle().get("pause");
        textContinue = game.getMyBundle().get("continue");
        textLevelSelect = game.getMyBundle().get("levelSelect");
        textWin = game.getMyBundle().get("win");
        textRecord = game.getMyBundle().get("newRecord");
        textTapAnywhere = game.getMyBundle().get("tapAnywhere");
        textBestTime = game.getMyBundle().get("bestTime");
        textYourTime = game.getMyBundle().get("yourTime");
    }

    /**
     * Plays the dot's clear sound.
     */
    private void canIPlaySound(){
        if(dotSoundsPlayed < dotsCleared){
            if(game.sounds)
                game.doneSound.play(game.effectVolume);
            dotSoundsPlayed++;
        }
    }

    /**
     * Fades the music in slowly.
     */
    private void fadeMusicIn(){
        if(game.gameMusicVolume < game.musicVolume)
            game.gameMusicVolume += 0.005f;
        if(game.gameMusicVolume < game.musicVolume)
            game.gameMusic.setVolume(game.gameMusicVolume);
    }
    /**
     * Fades the music out slowly.
     */
    private void fadeMusicOut(){
        if(game.menuMusicVolume > 0) {
            game.menuMusicVolume -= 0.01f;
            game.menuMusic.setVolume(game.menuMusicVolume);
        }
        if(game.menuMusicVolume <= 0)
            game.menuMusic.pause();
    }

    /**
     * Shows the Record time in correct format.
     */
    private void calculateScore(){
        if(!scoreCounted){
            scoreCounted = true;
            playerMins = 0;
            playerSecs = (int) Math.floor(playerTime);
            while(playerSecs > 59){
                playerMins++;
                playerSecs -= 60;
            }
            bestMins = 0;
            bestSecs = (int) Math.floor(bestTime);
            while(bestSecs > 59){
                bestMins++;
                bestSecs -= 60;
            }
        }
    }
}