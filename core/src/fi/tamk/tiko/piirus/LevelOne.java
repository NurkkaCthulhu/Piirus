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
 * Created by Anu on 8.3.2018.
 */

/**
 * Nothing special, basic movement
 * 2 different movesets, toggleable?
 * only one works at the moment.
 *
 * For joystick movement maybe need to do 8 by 4 camera?
 * or coordinates * 100?
 */

public class LevelOne extends GestureDetector.GestureAdapter implements Screen {
    private PiirusMain game;
    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture penTexture;
    private Texture penDot;
    private Texture buttonTexture;
    private Texture levelbg;
    private Texture finishPic;      //the beautified picture at the end
    private Rectangle penRectangle;
    private Rectangle penSizePlusRectangle;
    private Rectangle penSizeMinusRectangle;
    private Rectangle pauseMenuRectanlge;
    private Rectangle clearRectanlge;
    private Float penSpeed = 2f;
    private Float penSize = 0.1f;
    private ArrayList<Rectangle> penDots;
    private Vector3 joyStickVector;
    private boolean levelFinish;    //is the level finished

    private static int dotsCleared = 0;
    private int dotCount = 4;       //how many dots there are in the level
    //Dots
    private Array<Dot> dotArray;


    public LevelOne(PiirusMain g, BitmapFont f){
        game = g;
        font = f;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);

        penTexture = new Texture(Gdx.files.internal("pen.png"));
        penDot = new Texture(Gdx.files.internal("dot.png"));
        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        levelbg = new Texture(Gdx.files.internal("levelbg.png"));
        finishPic = new Texture(Gdx.files.internal("bread.png"));

        penRectangle = new Rectangle(game.WORLD_WIDTH / 2, game.WORLD_HEIGHT / 2, 0.2f, 0.2f);
        penSizeMinusRectangle = new Rectangle(0, 0, 0.6f, 0.6f);
        penSizePlusRectangle = new Rectangle(1, 0, 0.6f, 0.6f);
        pauseMenuRectanlge = new Rectangle(0, game.WORLD_HEIGHT - 0.6f, 0.6f, 0.6f);
        clearRectanlge = new Rectangle(camera.viewportWidth - 0.6f, 0, 0.6f, 0.6f);

        penDots = new ArrayList<Rectangle>();
        joyStickVector = new Vector3(); //Refactor someday missleading name

        levelFinish = false;

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

        //dots are in an array. Dot coordinates are inputted manually.
        dotArray = new Array<Dot>(dotCount);

        for (int i = 0; i < dotCount; i++) {
            float x = 0;
            float y = 0;
            boolean visible;
            if(i==0){
                visible = true;
            }else{
                visible = false;
            }
            switch(i){
                case 0:
                    x = 5.5f; y = 3f;
                    break;
                case 1:
                    x = 2.6f; y = 3.1f;
                    break;
                case 2:
                    x = 1.8f; y = 1.5f;
                    break;
                case 3:
                    x = 5.6f; y = 1.3f;
                    break;
            }
            dotArray.insert(i, new Dot(x, y, visible));

        }
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

        if(!levelFinished()){
        //draws all the dots on screen
        for(int i = 0; i < 4; i++) {
            dotArray.get(i).sprite.draw(batch);
        }

        penDraw();

        batch.draw(buttonTexture, penSizePlusRectangle.x, penSizePlusRectangle.y, penSizePlusRectangle.width, penSizePlusRectangle.height);
        batch.draw(buttonTexture, penSizeMinusRectangle.x, penSizeMinusRectangle.y, penSizeMinusRectangle.width, penSizeMinusRectangle.height);

        batch.draw(buttonTexture, clearRectanlge.x, clearRectanlge.y, clearRectanlge.width, clearRectanlge.height);

        batch.draw(penTexture, penRectangle.x, penRectangle.y, penRectangle.width*6, penRectangle.height*6);
        }
        batch.draw(buttonTexture, pauseMenuRectanlge.x, pauseMenuRectanlge.y, pauseMenuRectanlge.width, pauseMenuRectanlge.height);
        //check if the beautified pic can be shown
        if (levelFinished()) {
            batch.draw(finishPic, 1f, 0, finishPic.getWidth()/125, finishPic.getHeight()/125);
        }

        batch.end();
        if(!levelFinished()) {
            topdownMoving(penRectangle, joyStickVector);
        }
        /*
        Cursor.joystickMoving(game, penRectangle, penSize);
        if(Cursor.isPenMoved()){
            addPaint(penRectangle);
        }*/
        //check the collision
       /* for(int i = 0; i < 4; i++) {
            dotArray.get(i).checkCollisions(penRectangle);
        }*/
        if (dotsCleared < dotCount) {
            dotArray.get(dotsCleared).setVisible();
            dotArray.get(dotsCleared).checkCollisions(penRectangle);
        }


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

    private void topdownMoving(Rectangle rect, Vector3 movement){
        /* Pitkä kommentti blokki on 8-diagonal movement, ei kommentti fluent topdown
        boolean moved = false;
        if(game.getAdjustedY() > 1 && rect.x < game.SCREEN_WIDTH - penSize){
            rect.setX(rect.x + penSpeed);
            moved = true;
        }

        if(game.getAdjustedY() < -1 && rect.x > 0){
            rect.setX(rect.x - penSpeed);
            moved = true;
        }

        if(game.getAdjustedZ() > 1 && rect.y < game.SCREEN_HEIGHT - penSize){
            rect.setY(rect.y + penSpeed);
            moved = true;
        }

        if(game.getAdjustedZ() < -1 && rect.y > 0){
            rect.setY(rect.y - penSpeed);
            moved = true;
        }

        if(moved){
            addPaint(rect);
        }

        Gdx.app.log("ForgotHowToAccelo", "Y=" + Gdx.input.getAccelerometerY() + "||X=" + Gdx.input.getAccelerometerX() + "||Z=" + Gdx.input.getAccelerometerZ());
        Gdx.app.log("AddjustedAccelo", "Y=" + game.getAdjustedY() + "||Z=" + game.getAdjustedZ());
        //z = y
        //y = x
        */


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
        if(pauseMenuRectanlge.contains(touchPos.x, touchPos.y)){
            dotsCleared = 0;
            game.setScreen(new LevelSelect(game, font));
        }
        if(clearRectanlge.contains(touchPos.x, touchPos.y)){
            clearLine();
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
    public boolean levelFinished() {
        if(dotsCleared == dotCount) {
            return true;
        } else {
            return false;
        }
    }
    public static void setDotsCleared() {
        dotsCleared++;
    }

}
