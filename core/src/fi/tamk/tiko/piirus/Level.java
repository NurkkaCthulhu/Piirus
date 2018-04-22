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
    private Texture levelbg;
    private Texture finishPic;      //the beautified picture at the end
    private Texture pauseBg;
    private Texture pauseFill;
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Rectangle pauseMenuRectangle;
    private Rectangle clearRectangle;
    private Rectangle pauseContinue;
    private Rectangle pauseBack;
    private Float penSpeed = 2f;
    private Float penSize = 0.1f;
    private ArrayList<Rectangle> penDots;
    private Cursor cursor;
    private int levelNumber;
    private boolean paused;

    private static int dotsCleared = 0;
    private int dotCount;       //how many dots there are in the level
    //Dots
    private Array<Dot> dotArray;


    public Level(PiirusMain g, BitmapFont f, int number){

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
        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
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

        cursor = new Cursor(game, penSize);
        //dots are in an array. Dot coordinates are inputted manually.

        levelSelect();

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.4f, 0.4f,0.4f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.draw(levelbg, 0, 0, levelbg.getWidth()/100, levelbg.getHeight()/100);
        if(!levelFinished() && !paused){
            penDraw();
            Cursor.joystickMoving(penRectangle);
            //draws all the dots on screen
            for(int i = 0; i < dotCount; i++) {
                dotArray.get(i).sprite.draw(batch);
            }



            batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
            batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);

            batch.draw(buttonTexture, clearRectangle.x, clearRectangle.y, clearRectangle.width, clearRectangle.height);

            batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width*10, penRectangle.height*10);

            if(Cursor.isPenMoved() && dotsCleared > 0) {
                addPaint(penRectangle);
            }
        } else if (levelFinished() && !paused) {
            batch.draw(finishPic, game.WORLD_WIDTH*0.1f, 0, finishPic.getWidth()/110, finishPic.getHeight()/110);
        }
        batch.draw(buttonTexture, pauseMenuRectangle.x, pauseMenuRectangle.y, pauseMenuRectangle.width, pauseMenuRectangle.height);
        if(paused){
            batch.draw(pauseFill, 0, 0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
            batch.draw(pauseBg, 1, 1, 6, 3);
            batch.draw(buttonTexture, pauseContinue.x, pauseContinue.y, pauseContinue.width, pauseContinue.height);
            batch.draw(buttonTexture, pauseBack.x, pauseBack.y, pauseBack.width, pauseBack.height);
            batch.setProjectionMatrix(fontCamera.combined);
            font.draw(batch, "Jatka", pauseContinue.x * 100 + pauseContinue.width * 100 / 3f, pauseContinue.y * 100 + pauseContinue.height * 100 / 1.5f);
            font.draw(batch, "Valikkoon", pauseBack.x * 100 + pauseBack.width * 100 / 4f, pauseBack.y * 100 + pauseBack.height * 100 / 1.5f);
            font.draw(batch, "Pause", 350, 300);
        }

        batch.end();


        if (dotsCleared < dotCount) {
            dotArray.get(dotsCleared).setVisible();
            dotArray.get(dotsCleared).checkCollisions(penRectangle);
        }

        game.letsFigurePositionForMePlease(pauseBack, 2f);

        holdButtonTouched();
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
    }


    private void penDraw(){
        if(!penDots.isEmpty()){
            for(Rectangle r : penDots){
                batch.draw(penDot, r.x, r.y, r.width, r.height);
            }
        }
    }

    public void addPaint(Rectangle rect){
        penDots.add(new Rectangle(rect.x, rect.y, penSize, penSize));
    }

    private void clearLine(){
        penDots.clear();
    }

    @Override
    public boolean tap(float x, float y, int count, int button){
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if(pauseMenuRectangle.contains(touchPos.x, touchPos.y) && !paused){
            paused = true;
        }
        if(clearRectangle.contains(touchPos.x, touchPos.y) && !paused){
            clearLine();
        }
        if(pauseContinue.contains(touchPos.x, touchPos.y) && paused){
            paused = false;
        }
        if(pauseBack.contains(touchPos.x, touchPos.y) && paused){
            dotsCleared = 0;
            game.setScreen(new LevelSelect(game, font));
        }
        return false;
    }

    private void holdButtonTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(penSizePlusRectangle.contains(touchPos.x, touchPos.y)){
                penSize = penSize + 0.01f;
            }
            if(penSizeMinusRectangle.contains(touchPos.x, touchPos.y) && penSize > 0.01f){
                penSize = penSize - 0.01f;
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
        if(dotsCleared == dotCount) {
            return true;
        } else {
            return false;
        }
    }
    public static void setDotsCleared() {
        dotsCleared++;
    }

    private void levelSelect() {
        switch(levelNumber) {
            case 1:
                LevelOne objectOne = new LevelOne(game);
                dotCount = objectOne.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectOne.dotsArray.get(i));
                }
                finishPic = new Texture("bread.png");
                break;
            case 2:
                LevelTwo objectTwo = new LevelTwo(game);
                dotCount = objectTwo.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectTwo.dotsArray.get(i));
                }
                finishPic = new Texture("tomato.png");
                break;
            case 3:
                LevelThree objectThree = new LevelThree(game);
                dotCount = objectThree.dots;
                dotArray = new Array<Dot>(dotCount);
                for (int i = 0; i < dotCount; i++) {
                    dotArray.insert(i, objectThree.dotsArray.get(i));
                }
                finishPic = new Texture("flower.png");
                break;
        }
    }
}
