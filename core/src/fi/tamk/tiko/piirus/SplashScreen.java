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
    private Texture tamkFiTexture;
    private Texture tamkEnTexture;
    private Texture exeriumTexture;
    private Texture tikoFiTexture;
    private Texture tikoEnTexture;

    public SplashScreen(PiirusMain g){
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        waitTime = 0f;
        tamkEnTexture = new Texture(Gdx.files.internal("tamk_en.png"), true);
        tamkFiTexture = new Texture(Gdx.files.internal("tamk_fi.png"), true);
        exeriumTexture = new Texture(Gdx.files.internal("exerium_logo.png"), true);
        tikoFiTexture = new Texture(Gdx.files.internal("tiko_fi.png"), true);
        tikoEnTexture = new Texture(Gdx.files.internal("tiko_en.png"), true);
        camera.setToOrtho(false, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        tamkEnTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        tamkFiTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        exeriumTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        tikoEnTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        tikoFiTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        game.buttonSound = Gdx.audio.newSound(Gdx.files.internal("button_press.mp3"));
        game.doneSound = Gdx.audio.newSound(Gdx.files.internal("done.mp3"));
        game.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Lobby_Time.mp3"));
        game.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Danse_Morialta.mp3"));
        game.menuMusicVolume = 0;
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
        batch.draw(exeriumTexture, game.WORLD_WIDTH * 0.21875f, game.WORLD_HEIGHT * 0.5f, game.WORLD_WIDTH * 0.575f, game.WORLD_WIDTH * 0.575f * 0.3249f);
        if (game.getLanguage().equalsIgnoreCase("fi")){
            batch.draw(tamkFiTexture, 0, 0, game.WORLD_WIDTH * 0.35f, game.WORLD_WIDTH * 0.35f * 0.53735f);
            batch.draw(tikoFiTexture, game.WORLD_WIDTH * 0.625f, game.WORLD_HEIGHT * 0.0625f, game.WORLD_WIDTH * 0.35f, game.WORLD_WIDTH * 0.35f * 0.3929f);
        } else {
            batch.draw(tamkEnTexture, 0, 0, game.WORLD_WIDTH * 0.35f, game.WORLD_WIDTH * 0.35f * 0.53735f);
            batch.draw(tikoEnTexture, game.WORLD_WIDTH * 0.625f, game.WORLD_HEIGHT * 0.0625f, game.WORLD_WIDTH * 0.35f, game.WORLD_WIDTH * 0.35f * 0.3929f);
        }
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
        tikoFiTexture.dispose();
        tamkEnTexture.dispose();
        tikoEnTexture.dispose();
        tamkFiTexture.dispose();
        exeriumTexture.dispose();
    }
}
