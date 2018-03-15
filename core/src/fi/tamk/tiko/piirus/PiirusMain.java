package fi.tamk.tiko.piirus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PiirusMain extends Game {
    //Käyttää 800x400, koska ei tarvitse Box2D ja todennäköisesti yhdellä kameralla voidaan
    //fontit tulostaa. Thoughts?
    public int SCREEN_WIDTH = 800;
    public int SCREEN_HEIGHT = 400;
    private MainMenu mainMenu;
	SpriteBatch batch;

	public SpriteBatch getBatch() {
	    return batch;
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		mainMenu = new MainMenu(this);
        setScreen(mainMenu);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
