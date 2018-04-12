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
    private Rectangle crosshairRect;
    private float crosshairSize = 0.01f;
    private Vector3 crosshairVector;

    //saved calibration values
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;

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
        crosshairRect = new Rectangle(game.WORLD_WIDTH/2-0.005f, game.WORLD_HEIGHT/2-0.005f, crosshairSize, crosshairSize);
        menuRect = new Rectangle(0,0, 0.4f, 0.4f);
        crosshairVector = new Vector3(0,0,0);

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
        batch.draw(crosshairTexture, crosshairRect.x-crosshairRect.width*20, crosshairRect.y-crosshairRect.height*20, crosshairRect.width*40, crosshairRect.height*40);
        batch.setProjectionMatrix(fontCamera.combined);
        font.draw(batch, "<-", menuRect.x*100, (menuRect.y + menuRect.getHeight() / 2)*100 );
        font.draw(batch, "1", 658, 325);
        font.draw(batch, "2", 658, 275);
        font.draw(batch, "3", 658, 225);
        font.draw(batch, "4", 658, 175);
        batch.end();
        moveCrosshair(crosshairRect, crosshairVector);
        renderRectangle(crosshairRect);
        Gdx.app.log("crosshair y", "" + crosshairRect.y);
        Gdx.app.log("crosshair x", "" + crosshairRect.x);
        //Gdx.app.log("Pure AdjustedZ", "" + Gdx.input.getAccelerometerZ());
        //Gdx.app.log("Pure AdjustedY", "" + Gdx.input.getAccelerometerY());

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
        buttonTexture.dispose();
        backgroundTexture.dispose();
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);
        if(menuRect.contains(touchPos.x, touchPos.y)){
            dispose();
            game.setScreen(new SettingsScreen(game, font));
        }
        return false;
    }
    public void moveCrosshair(Rectangle rect, Vector3 movement) {
        movement.x = game.WORLD_WIDTH / 2 + (game.getAdjustedY());
        movement.y = game.WORLD_HEIGHT / 2 + (game.getAdjustedZ());

        if(rect.y < game.WORLD_HEIGHT - crosshairSize && rect.y > 0){
            rect.setY(rect.y + movement.y);
        }

        if(rect.y > game.WORLD_HEIGHT - crosshairSize){
            rect.setY(game.WORLD_HEIGHT - crosshairSize - 0.01f);
        }

        if(rect.y < 0){
            rect.setY(0.01f);
        }

        if(rect.x < game.WORLD_WIDTH - crosshairSize && rect.x > 0){
            rect.setX(rect.x + movement.x);
        }

        if(rect.x > game.WORLD_WIDTH - crosshairSize){
            rect.setX(game.WORLD_WIDTH - crosshairSize - 0.01f);
        }

        if(rect.x < 0){
            rect.setX(0.01f);
        }
    }

    public void checkCollision(Rectangle crossRect, Rectangle checkRect) {
        float countdown = 0; //move this away
        if (crossRect.overlaps(checkRect)){
            //Gdx.app.log("asd", "Osuuuuu");
            countdown += Gdx.graphics.getRawDeltaTime();
        } else {
            if (countdown > 0) {
                countdown -= Gdx.graphics.getRawDeltaTime();
            }
        }

        //if the cursor is held in the dot for long enough, you clear it and can move to the next dot
        if (countdown > 3) {
            countdown = 0;
        }
    }

    public void renderRectangle(Rectangle rect) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        shapeRenderer.end();
    }
}
