package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Anbu on 15.4.2018.
 */

public class Level extends GestureDetector.GestureAdapter implements Screen {

    private PiirusMain game;
    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture penTexture;
    private Texture penDot;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private Texture levelbg;
    private Texture finishPic;      //the beautified picture at the end
    private Texture pauseBg;
    private Texture pauseFill;
    private Texture pauseButtonTexture;
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Rectangle pauseMenuRectangle;
    private Rectangle clearRectangle;
    private Rectangle pauseContinue;
    private Rectangle pauseBack;
    private Float penSpeed = 2f;
    private Float penSize = 0.1f;
    private Float finishedTimer;
    private Float tapToContinueHeight;
    private ArrayList<Rectangle> penDots;
    private Cursor cursor;
    private int levelNumber;
    private int score;

    private boolean paused;

    private static int dotsCleared = 0;
    private int dotCount;       //how many dots there are in the level
    //Dots
    private Array<Dot> dotArray;

    //localization String
    private String textPaused;
    private String textContinue;
    private String textLevelSelect;
    private String textWin;
    private String textScore;
    private String textTapAnywhere;


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
        penDot = new Texture(Gdx.files.internal("dot.png"));
        buttonTexture = new Texture(Gdx.files.internal("levelbutton.png"));
        buttonPressedTexture = new Texture(Gdx.files.internal("levelbutton_pressed.png"));
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        levelbg = new Texture(Gdx.files.internal("levelbg.png"));
        pauseBg = new Texture(Gdx.files.internal("pauseBg.png"));
        pauseFill = new Texture(Gdx.files.internal("pauseFill.png"));

        penTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        penRectangle = new Rectangle(game.WORLD_WIDTH / 2, game.WORLD_HEIGHT / 2, 0.1f, 0.1f);
        penSizeMinusRectangle = new Rectangle(0, 0, 0.6f, 0.6f);
        penSizePlusRectangle = new Rectangle(1, 0, 0.6f, 0.6f);
        pauseMenuRectangle = new Rectangle(0, game.WORLD_HEIGHT - 0.6f, 0.6f, 0.6f);
        clearRectangle = new Rectangle(camera.viewportWidth - 0.6f, 0, 0.6f, 0.6f);
        pauseContinue = new Rectangle(4.3f, 1.3f, 2.5f, 0.63f);
        pauseBack = new Rectangle(1.16f, 1.3f, 2.5f, 0.63f);

        penDots = new ArrayList<Rectangle>();

        paused = false;
        finishedTimer = 0f;
        tapToContinueHeight = 0f;

        cursor = new Cursor(game, penSize);
        //dots are in an array. Dot coordinates are inputted manually.

        levelSelect();
        updateLevelTexts();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelbg, 0, 0, levelbg.getWidth() / 100, levelbg.getHeight() / 100);
        if (!levelFinished() && !paused) {
            penDraw();
            Cursor.joystickMoving(penRectangle);
            //draws all the dots on screen
            for (int i = 0; i < dotCount; i++) {
                dotArray.get(i).sprite.draw(batch);
            }

        //    batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
        //    batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);
       //     batch.draw(buttonTexture, clearRectangle.x, clearRectangle.y, clearRectangle.width, clearRectangle.height);

            batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width * 10 * game.penSize, penRectangle.height * 10 * game.penSize);

            if (Cursor.isPenMoved() && dotsCleared > 0) {
                addPaint(penRectangle);
            }
        } else if (levelFinished() && !paused) {
            if (score < 250)
                score = (int) Math.floor(Math.random() * 400 + 200);
            if (finishedTimer < 60)
                finishedTimer += Gdx.graphics.getRawDeltaTime();
            if (finishedTimer < 1)
                batch.setColor(1, 1, 1, finishedTimer);
            batch.draw(finishPic, game.WORLD_WIDTH * 0.1f, 0, finishPic.getWidth() / 110, finishPic.getHeight() / 110);
            batch.setColor(Color.WHITE);
            if (finishedTimer > 59) {
                batch.draw(pauseBg, 1, 1, 6, 3);
                batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
                batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
                showPressedButtons();
                batch.setProjectionMatrix(fontCamera.combined);
                font.draw(batch, textContinue, pauseContinue.x * 100 + pauseContinue.width * 100 / 3f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f);
                font.draw(batch, textLevelSelect, pauseBack.x * 100 + pauseBack.width * 100 / 4f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f);
                font.draw(batch, textWin, 350, 300);
                font.draw(batch, textScore + score, 300, 250);
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
        if (paused) {
            batch.draw(pauseFill, 0, 0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
            batch.draw(pauseBg, 1, 1, 6, 3);
            batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
            batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
            showPressedButtons();
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, textContinue, pauseContinue.x * 100 + pauseContinue.width * 100 / 3f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f);
            font.draw(batch, textLevelSelect, pauseBack.x * 100 + pauseBack.width * 100 / 4f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f);
            font.draw(batch, textPaused, 350, 300);
        }
        //batch.draw(finishPic, game.WORLD_WIDTH*0.1f, 0, finishPic.getWidth()/110, finishPic.getHeight()/110);
        holdButtonTouched();
        batch.end();


        if (!paused && dotsCleared < dotCount) {
            dotArray.get(dotsCleared).setVisible();
            dotArray.get(dotsCleared).checkCollisions(penRectangle);
        }

        /*if(Gdx.input.isButtonPressed(Input.Keys.SPACE)){
            levelNumber = 6;
            levelSelect();
        }*/

        //For rendering rectangles if you need debugging. Send the rectangle you want to render.
        /*renderRectangle(dotOne.sprite.getBoundingRectangle());
        renderRectangle(dotTwo.sprite.getBoundingRectangle());
        renderRectangle(dotThree.sprite.getBoundingRectangle());
        renderRectangle(dotFour.sprite.getBoundingRectangle());*/

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        if (dotsCleared != dotCount)
            paused = true;
    }

    @Override
    public void resume() {

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
    }


    private void penDraw() {
        if (!penDots.isEmpty()) {
            for (Rectangle r : penDots) {
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    public void addPaint(Rectangle rect) {
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
        if (score > 0)
            score--;
    }

    private void clearLine() {
        penDots.clear();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if (pauseMenuRectangle.contains(touchPos.x, touchPos.y) && !paused) {
            game.buttonSound.play(game.effectVolume);
            paused = true;
        }
        if (clearRectangle.contains(touchPos.x, touchPos.y) && !paused) {
            clearLine();
        }
        if (pauseContinue.contains(touchPos.x, touchPos.y) && paused) {
            game.buttonSound.play(game.effectVolume);
            paused = false;
        } else if (pauseContinue.contains(touchPos.x, touchPos.y) && finishedTimer >= 59) {
            game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            levelNumber++;
            finishedTimer = 0f;
            tapToContinueHeight = 0f;
            clearLine();
            levelSelect();
        }
        if (pauseBack.contains(touchPos.x, touchPos.y) && paused) {
            game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            tapToContinueHeight = 0f;
            game.setScreen(new LevelSelect(game, font));
        } else if (pauseBack.contains(touchPos.x, touchPos.y) && finishedTimer >= 59) {
            game.buttonSound.play(game.effectVolume);
            dotsCleared = 0;
            tapToContinueHeight = 0f;
            game.setScreen(new LevelSelect(game, font));
        }
        if (finishedTimer > 0.5f && finishedTimer < 59)
            finishedTimer = 60f;
        Gdx.app.log("TouchPos", "X:" + (touchPos.x / 8) + "||||||Y:" + (touchPos.y / 5));
        return false;
    }

    private void holdButtonTouched() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (penSizePlusRectangle.contains(touchPos.x, touchPos.y)) {
                penSize = penSize + 0.01f;
            }
            if (penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && penSize > 0.01f) {
                penSize = penSize - 0.01f;
            }
            //dotsCleared = dotCount;

        }
    }

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

    public void renderRectangle(Rectangle rect) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        //shapeRenderer.line(x, y, x2, y2);
        shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        //shapeRenderer.circle(x, y, radius);
        shapeRenderer.end();
/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(x, y, width, height);
        //shapeRenderer.circle(x, y, radius);
        shapeRenderer.end();*/
    }

    private boolean levelFinished() {
        if (dotsCleared == dotCount) {
            return true;
        } else {
            return false;
        }
    }

    public static void setDotsCleared() {
        dotsCleared++;
    }

    private void levelSelect() {
        switch (levelNumber) {
            case 1:
                LevelOne objectOne = new LevelOne(game);
                dotCount = objectOne.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectOne.dotsArray.get(i));
                }
                score = 3600;
                finishPic = new Texture("bread.png");
                break;
            case 2:
                LevelTwo objectTwo = new LevelTwo(game);
                dotCount = objectTwo.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectTwo.dotsArray.get(i));
                }
                score = 3600;
                finishPic = new Texture("tomato.png");
                break;
            case 3:
                LevelThree objectThree = new LevelThree(game);
                dotCount = objectThree.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectThree.dotsArray.get(i));
                }
                score = 7200;
                finishPic = new Texture("flower.png");
                break;
            case 4:
                LevelFour objectFour = new LevelFour(game);
                dotCount = objectFour.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectFour.dotsArray.get(i));
                }
                score = 8000;
                finishPic = new Texture("cat.png");
                break;
            case 5:
                LevelFive objectFive = new LevelFive(game);
                dotCount = objectFive.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectFive.dotsArray.get(i));
                }
                score = 8000;
                finishPic = new Texture("snowflake.png");
                break;
            case 6:
                LevelSix objectSix = new LevelSix(game);
                dotCount = objectSix.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectSix.dotsArray.get(i));
                }
                score = 8000;
                finishPic = new Texture("clock.png");
                break;
            default:
                dotsCleared = 0;
                game.setScreen(new LevelSelect(game, font));
        }
    }

    public void updateLevelTexts() {
        textPaused = game.getMyBundle().get("pause");
        textContinue = game.getMyBundle().get("continue");
        textLevelSelect = game.getMyBundle().get("levelSelect");
        textWin = game.getMyBundle().get("win");
        textScore = game.getMyBundle().get("score");
        textTapAnywhere = game.getMyBundle().get("tapAnywhere");
    }
}
