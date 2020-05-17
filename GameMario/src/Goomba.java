
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Goomba extends Enemy {

	public Body body;
	private float timer; // time in the game world
	private Animation walkingAnimation; 
	private Array <TextureRegion> pixels;
	private boolean destroy;
	private boolean dead; 

	public Goomba(PlayScreen screen, float x, float y) {
		super(screen, x, y);

		pixels = new Array<TextureRegion>();

		for (int i = 1; i < 2; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
		walkingAnimation = new Animation(0.4f,pixels);

		timer = 0;
		// sets the position and size of the GUI component
		setBounds(getX(), getY(), 20 / MarioGame.PPM,  21 / MarioGame.PPM);
		destroy = false;
		dead = false;
	}

	public void update(float dt) {
		timer += dt;

		if(destroy && !dead) {
			world.destroyBody(body); // remove the body
			dead = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
			timer = 0;
		}
		else if (!dead) {	 
			body.setLinearVelocity(velocity);
			setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 3); // sets the coordinates and texture to a specified region
			setRegion(walkingAnimation.getKeyFrame(timer, true));
		}
	}

	@Override
	protected void defineEnemy() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.DynamicBody; // moving body
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6/MarioGame.PPM);

		// collision filtering - defines a category object
		fixturedef.filter.categoryBits = MarioGame.Enemy;

		// identifies if the goomba collides with any of the objects
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Brick | MarioGame.Coin | MarioGame.Enemy | MarioGame.Object | MarioGame.Trampoline | MarioGame.Player;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);

		// Creates head
		PolygonShape head = new PolygonShape();
		Vector2[] points = new Vector2[4];
		points[0] = new Vector2(-5, 8).scl(1 /MarioGame.PPM);
		points[1] = new Vector2(5, 8).scl(1 /MarioGame.PPM);
		points[2] = new Vector2(-3,3).scl(1 /MarioGame.PPM);
		points[3] = new Vector2(3,3).scl(1 /MarioGame.PPM);
		head.set(points);

		fixturedef.shape = head;

		// elasticity of when a player jumps of the goombas head
		fixturedef.restitution = 0.5f;
		fixturedef.filter.categoryBits = MarioGame.Head;
		body.createFixture(fixturedef).setUserData(this);	
	}

	public void draw(Batch batch) {
		if (!dead || timer < 1)
			super.draw(batch);
	}

	@Override
	public void hitOnHead(Mario mario) {
		destroy = true;
	}
}
