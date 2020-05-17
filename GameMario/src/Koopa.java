
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Koopa extends Enemy {

	public Body body;
	public static float leftSpeed = -2.3f;
	public static float rightSpeed = 2.3f;
	public enum State {Walk, Move, Stand} // sets the variable to predefined constants
	public State current;
	public State previous;
	private float timer;
	private Animation walkAnimation;
	private Array<TextureRegion> pixels;
	private TextureRegion shellState;

	public Koopa(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		pixels = new Array<TextureRegion>();
		// animations for all koopa states
		pixels.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
		pixels.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
		shellState = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
		walkAnimation = new Animation(0.2f, pixels);

		// initalize the current and previous state to the walking animation
		current = State.Walk;
		previous = State.Walk;

		setBounds(getX(), getY(), 20 / MarioGame.PPM, 28 / MarioGame.PPM);
	}

	@Override
	protected void defineEnemy() {
		// defines the box2D body of the Koopa
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Enemy;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Coin | MarioGame.Brick | MarioGame.Enemy | MarioGame.Trampoline | MarioGame.Object| MarioGame.Player;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);

		// Creates head
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5, 8).scl(1 / MarioGame.PPM);
		vertice[1] = new Vector2(5, 8).scl(1 / MarioGame.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / MarioGame.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / MarioGame.PPM);
		head.set(vertice);

		fixturedef.shape = head;
		fixturedef.restitution = 0.9f;
		fixturedef.filter.categoryBits = MarioGame.Head;
		body.createFixture(fixturedef).setUserData(this);
	}

	public TextureRegion getPixel(float dt) {
		TextureRegion region;

		// switch case which checks the current state 
		switch (current) {
			case Move:
			case Stand:
				region = shellState;
				break;
			case Walk:
			default:
				region = walkAnimation.getKeyFrame(timer, true);
				break;
		}

		// flips the texture based on direction
		if (velocity.x > 0 && region.isFlipX() == false)
			region.flip(true, false);

		else if (velocity.x < 0 && region.isFlipX() == true)
			region.flip(true, false);

		if (current == previous)
			timer += dt;
		else 
			timer = 0; 

		previous = current;
		return region;
	}

	@Override
	public void update(float dt) {
		setRegion(getPixel(dt));

		// if the turtle is in the shell for it waits 3 seconds to change to the walking state
		if(current == State.Stand && timer > 3) {
			current = State.Walk;
			velocity.x = 0.5f;
		}

		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 7.5f /MarioGame.PPM);
		velocity.y = body.getLinearVelocity().y;
		body.setLinearVelocity(velocity);
	}

	@Override
	public void hitOnHead(Mario mario) {
		if (current != State.Stand) {
			current = State.Stand;
			velocity.x = 0;
		}
	}

	// adjusts the speed of the Koopa when it is kicked
	public void kickKoopa(float speed) {
		velocity.x = speed;
		current = State.Move;
	}
}