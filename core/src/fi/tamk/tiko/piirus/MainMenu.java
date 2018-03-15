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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anu on 8.3.2018.
 */

/*
* Very basic menu functionality 15.3.2018
* Also some position figuring code for menu buttons
* No need for trial and error! :muscle:
*/

public class MainMenu implements Screen {
    private PiirusMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture buttonTexture; //Kaikille oma menu texture, vai vain yksi ja sitä piirretään monta kertaa?
    private Rectangle gameRect; //Peliin "nappi"

    private BitmapFont font; //FreeType best

    public MainMenu(PiirusMain g){
        game = g;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.RED;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);
        generator.dispose();

        buttonTexture = new Texture(Gdx.files.internal("rectFill.png"));
        gameRect = new Rectangle(300, 170, 150, 50);
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
        batch.draw(buttonTexture, gameRect.x, gameRect.y, gameRect.width, gameRect.height);
        font.draw(batch, "Peliin(WIP)", gameRect.x, gameRect.y + gameRect.getHeight() / 2);
        batch.end();

        letsFigurePositionForMePlease(gameRect);

        whatHasBeenTouched();
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

    }

    private void letsFigurePositionForMePlease(Rectangle rect){
        float speed = 20f;

        //Moving
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)){
            rect.setX(rect.x + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)){
            rect.setX(rect.x - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)){
            rect.setY(rect.y + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)){
            rect.setY(rect.y - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("POSITION", "" + rect.x + "|||||" + rect.y);
        }

        //Size
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            rect.setWidth(rect.width - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            rect.setWidth(rect.width + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            rect.setHeight(rect.height + speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            rect.setHeight(rect.height - speed * Gdx.graphics.getDeltaTime());
            Gdx.app.log("SIZE", "" + rect.width + "|||||" + rect.height);
        }
    }

    private void whatHasBeenTouched(){
        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if(touchPos.x > gameRect.getX() && touchPos.x < gameRect.getX() + gameRect.getWidth() && touchPos.y > gameRect.getY() && touchPos.y < gameRect.getY() + gameRect.getHeight()){
                //Mene peli ruutuun + anteeksi tästä silli salaatti iffistä :sadface:
                //Parempaa tapaa odotellessa :D
                Gdx.app.log("gameRect", "I GOT TOUCHED!");
            }
        }
    }
}
