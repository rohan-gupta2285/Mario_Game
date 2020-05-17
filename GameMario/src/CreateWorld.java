
import com.badlogic.gdx.maps.MapObject;



import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class CreateWorld {

	// arrays of all the enemies and objects
	private Array<Goomba> goomba;
	private Array<Bomb> bomb;
	private Array<Koopa> koopas;
	private Array<PiranhaPlant> piranha;
	private Array<Trampoline> trampoline;

	public CreateWorld (PlayScreen screen)
	{
		World world = screen.getWorld();
		TiledMap map = screen.getMap();

		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		// Ground body and fixture
		for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioGame.PPM, (rect.getY() + rect.getHeight() / 2)/ MarioGame.PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM, rect.getHeight()/2/MarioGame.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		// Pipe body and fixture
		for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioGame.PPM, (rect.getY() + rect.getHeight() / 2)/ MarioGame.PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM, rect.getHeight()/2/MarioGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = MarioGame.Object;
			body.createFixture(fdef);
		}

		// Brick bodies and fixtures
		for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

			new Brick(screen, object);
		}

		// Coin body and fixture
		for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

			new BrickCoin(screen,object);
		}

		// Creates goombas
		goomba = new Array<Goomba>();
		for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			goomba.add(new Goomba(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}

		// Creates koopas
		koopas = new Array<Koopa>();
		for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			koopas.add(new Koopa(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}

		// Creates piranha plants
		piranha = new Array<PiranhaPlant>();
		for (MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			piranha.add(new PiranhaPlant(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}

		// Creates the bomb ombs
		bomb = new Array<Bomb>();
		for (MapObject object: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			bomb.add(new Bomb(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}

		// Wall Collisions
		for (MapObject object: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioGame.PPM, (rect.getY() + rect.getHeight() / 2)/ MarioGame.PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM, rect.getHeight()/2/MarioGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = MarioGame.Object;
			body.createFixture(fdef);
		}

		// Creates the trampoline
		trampoline = new Array<Trampoline>();
		for (MapObject object: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			trampoline.add(new Trampoline(screen, rect.getX() / MarioGame.PPM, rect.getY() / MarioGame.PPM));
		}
	}
	
	// getter method
	public Array<Trampoline> getTrampoline() {
		return trampoline;
	}
	
	// adds all enemies to the enemy array
	public Array<Enemy> getEnemies(){
		Array<Enemy> enemies = new Array<Enemy>();
		enemies.addAll(goomba);
		enemies.addAll(koopas);
		enemies.addAll(piranha);
		enemies.addAll(bomb);
		return enemies;
	}
}
