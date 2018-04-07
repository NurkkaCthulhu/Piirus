package fi.tamk.tiko.piirus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Rip10 on 7.4.2018.
 */

public class SplashScreen implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float waitTime;
    private Texture tamkTexture;
    private Texture exeriumTexture;
    private Texture tikoTexture;

    public SplashScreen(PiirusMain g){
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        waitTime = 0f;
        tamkTexture = new Texture(Gdx.files.internal("tamk.png"), true);
        exeriumTexture = new Texture(Gdx.files.internal("exerium_logo.png"), true);
        tikoTexture = new Texture(Gdx.files.internal("tiko_logo_musta_fin.png"), true);
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        tamkTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        exeriumTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        tikoTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        waitTime = waitTime + delta;

        Gdx.gl.glClearColor(1f, 1f,1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(tamkTexture, 0, 0, 2.8f, 1.5f);
        batch.draw(exeriumTexture, 1.75f, 2f, 4.6f, 1.5f);
        batch.draw(tikoTexture, 5, 0.25f, 2.8f, 1.1f);
        batch.end();

        if(Gdx.input.isTouched() || waitTime > 3f){
            game.calibrate();
            game.setScreen(new MainMenu(game));
        }
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
        tikoTexture.dispose();
        tamkTexture.dispose();
        exeriumTexture.dispose();
    }
}
