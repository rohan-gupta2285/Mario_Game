
import java.util.concurrent.LinkedBlockingQueue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen {

	public static final int width = 400;
	public static final int height = 210;

	float x;
	float y;

	private Mario player;
	private Ghost ghost;
	private Mushroom mushroom;

	private MarioGame game;
	public static OrthographicCamera gamecam;
	public Viewport gamePort;
	private ScoreBoard scoreBoard;

	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	//Box2D variables
	private World world;
	private Box2DDebugRenderer render;

	private TextureAtlas atlas;

	protected Fixture fixture;

	private CreateWorld create;

	private Array<Item> items;
	private LinkedBlockingQueue <Define> spawn;

	public PlayScreen(MarioGame game) 
	{
		atlas = new TextureAtlas("Textures.pack");	

		this.game = game;

		gamecam = new OrthographicCamera();
		gamePort = new FitViewport(MarioGame.width/ MarioGame.PPM, MarioGame.height / MarioGame.PPM, gamecam);
		scoreBoard = new ScoreBoard(game.batch);

		maploader = new TmxMapLoader();
		map = maploader.load("NewMap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/ MarioGame.PPM);
		//gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		// Gravity
		world = new World(new Vector2(0,-11), true);
		render = new Box2DDebugRenderer();

		create = new CreateWorld(this);

		// Spawns mario in the world
		player = new Mario(this);

		ghost = new Ghost(this, x, y);

		world.setContactListener(new WorldContactListener());

		items = new Array<Item>();
		spawn = new LinkedBlockingQueue<Define>();
	}

	public void spawn(Define def) {
		spawn.add(def);
	}

	public void HandleItems() {
		if (!spawn.isEmpty()) {
			//pop
			Define def = spawn.poll();
			if (def.type == Mushroom.class)
				items.add(new Mushroom(this, def.position.x, def.position.y));
		}
	}

	@Override
	public void show() {

	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void handleInput(float dt) {	
		if (player.current != Mario.State.DIEING) {
			// limits the jump to one jump
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				player.jumpLimitation();
			}
			// if the right key is pressed and the velocity in the x-axis is less than 2 pixels per seconds
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2){
				// calculates the impulse of the player
				player.body.applyLinearImpulse(new Vector2(0.1f,0), player.body.getWorldCenter(), true);
			}	
			// if the left key is pressed and the velocity in the x-axis is greater than -2 pixels per seconds
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2){
				player.body.applyLinearImpulse(new Vector2(-0.1f,0), player.body.getWorldCenter(), true);
			}
		}
	}

	public void update(float dt) {
		handleInput(dt);
		HandleItems();

		player.update(dt);
		ghost.getMove(player);
		ghost.update(dt);

		for(Enemy enemy : create.getEnemies())
			enemy.update(dt);
		for (Item item: items)
			item.update(dt);
		for (Trampoline trampoline: create.getTrampoline())
			trampoline.update(dt);

		scoreBoard.update(dt);

		world.step(1/80f,6, 2);
		if (player.current != Mario.State.DIEING)
			if(player.body.getPosition().x>=MarioGame.width/2/MarioGame.PPM)
				gamecam.position.x = player.body.getPosition().x;

		if (player.body.getPosition().y>=MarioGame.height/2/MarioGame.PPM)
			gamecam.position.y = player.body.getPosition().y;
		
		gamecam.update();
		renderer.setView(gamecam);
	}

	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		render.render(world, gamecam.combined);

		game.batch.setProjectionMatrix(scoreBoard.stages.getCamera().combined);
		scoreBoard.stages.draw();

		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		ghost.draw(game.batch);

		for (Enemy enemy : create.getEnemies())
			enemy.draw(game.batch);
		for (Item item: items)
			item.draw(game.batch);
		for (Trampoline trampoline: create.getTrampoline())
			trampoline.draw(game.batch);
		game.batch.end();

		if (ScoreBoard.worldTimer == 0 || GameOver()) 
			game.setScreen(new GameOver(game));
		if (WinScreen())
			game.setScreen(new WinScreen(game));
	}

	public boolean GameOver() {
		if (player.current == Mario.State.DIEING && player.getTimer() > 4)
			return true;
		else
			return false;
	}

	public boolean WinScreen() {
		if (player.getX() > 72.95f)
			return true;
		else
			return false;
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	public TiledMap getMap() {
		return map;
	}

	public World getWorld() {
		return world;
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
		map.dispose();
		renderer.dispose();
		world.dispose();
		render.dispose();
		scoreBoard.dispose();
		((Screen) mushroom).dispose();
	}

	public ScoreBoard getHud() { 
		return scoreBoard; 
	}
}
