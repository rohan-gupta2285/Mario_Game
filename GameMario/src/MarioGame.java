
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MarioGame extends Game {
	
	public static final int width = 350; 
	public static final int height = 208;
	public static final float PPM = 100; // Pixels per metre

	public SpriteBatch batch;

	// Bits used for collision detection
	public static final short Wall = 0;
	public static final short Ground = 1;
	public static final short Player = 2;
	public static final short Brick = 4;
	public static final short Coin = 8;
	public static final short Kill  = 16;
	public static final short Object = 32;
	public static final short Enemy = 64;
	public static final short Head = 128;
	public static final short Item = 256;
	public static final short Player_Head = 512;
	public static final short Hammer = 1024;
	public static final short Flower = 2048;
	public static final short BottomCollision = 4096;
	public static final short Trampoline = 8192;
	public static final short Fire = 16384;


	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));			
	}

	public void render(float delta) {
		super.render();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
