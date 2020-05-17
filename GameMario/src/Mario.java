
import com.badlogic.gdx.graphics.g2d.Animation;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Mario extends Sprite{
	
	public World world;
	public Body body;

	// Thanushon helped us out with the enumeration concept
	public enum State {FALLING, JUMPING, STANDING, DIEING, RUNNING};
	public State current;
	public State previous;
	private TextureRegion standAnimation;
	private TextureRegion deadAnimation;
	private Animation marioRun;
	private Animation marioJump;
	public boolean runningRight;
	private boolean marioIsDead;
	private float timer;
	public boolean destroy;
	public boolean dead;
	
	Array<TextureRegion> pixels;

	public Mario (PlayScreen screen) {

		// gets the texture region for Mario
		super(screen.getAtlas().findRegion("little_mario"));
		this.world = screen.getWorld();
		createMario();
		current = State.STANDING;
		previous = State.STANDING;
		timer = 0;
		runningRight = true;

		pixels = new Array<TextureRegion>();

		for(int i = 1; i < 4; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
		marioRun = new Animation(0.1f, pixels);
		pixels.clear();

		for(int i = 4; i < 6; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16));
		marioJump = new Animation(0.1f, pixels);
		pixels.clear();

		deadAnimation = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

		standAnimation = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
		setBounds(0,0,20/MarioGame.PPM, 17/ MarioGame.PPM);
		setRegion(standAnimation);

		destroy = false;
		dead = false;
	}

	public boolean getDead() {
		return marioIsDead; 
	}

	public float getTimer() {
		return timer;
	}

	public void update(float dt) {	
		timer += dt;
		if (destroy && !dead) {
			world.destroyBody(body);
			dead = true;
			timer = 0;
		}
		else if (!dead) {
			setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 3);
			setRegion(getPixel(dt));
		}
	}

	// when Mario collides with an enemy
	public void hit(Enemy enemy) {
		// adjusts the velocity of the Koopa when hit at it's standing state
		if (enemy instanceof Koopa && ((Koopa) enemy).current == Koopa.State.Stand) {
			if (this.getX() < enemy.getX())
				((Koopa) enemy).kickKoopa(Koopa.rightSpeed);
			else
				((Koopa) enemy).kickKoopa(Koopa.leftSpeed);
		} 
		else {
			if (!getDead()) {
				marioIsDead = true;
				// if Mario is in a collision with another object
				Filter filter = new Filter();
				filter.maskBits = MarioGame.Wall;

				for (Fixture fixture : body.getFixtureList()) 
					fixture.setFilterData(filter);
				body.applyLinearImpulse(new Vector2(0, 3f), body.getWorldCenter(), true);
			}
		}
	}

	// limits the player to one jump when key is pressed
	public void jumpLimitation() {
		if (current != State.JUMPING) {
			body.applyLinearImpulse(new Vector2(0, 4), body.getWorldCenter(), true);
			current = State.JUMPING;
		}
	}

	/*
	 * calls the current state and displays the texture to the screen
	 */
	public TextureRegion getPixel(float dt) {
		current = getState();

		TextureRegion region;
		switch(current) {
			case DIEING:
				region = deadAnimation;
				break;
			case JUMPING:
				region = marioJump.getKeyFrame(timer);
				break;
			case RUNNING:
				region = marioRun.getKeyFrame(timer, true);
				break;
			case FALLING:
			case STANDING:
			default:
				region = standAnimation;
				break;
		}

		// flips the texture based on Mario's direction
		if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		}
		else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}

		if (current == previous)
			timer += dt;
		else 
			timer = 0;

		previous = current;
		return region;
	}

	/*
	 * checks when each state should be initalized
	 */
	public State getState() {
		if (marioIsDead) 
			return State.DIEING;
		else if ((body.getLinearVelocity().y > 0) || (body.getLinearVelocity().y < 0 && previous == State.JUMPING)) 
			return State.JUMPING;
		else if (body.getLinearVelocity().x != 0) 
			return State.RUNNING;
		else if (body.getLinearVelocity().y < 0) 
			return State.FALLING;
		else 
			return State.STANDING;
	}

	public void createMario() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(100/MarioGame.PPM,100/MarioGame.PPM);
		bodydef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(4/MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Player;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Brick | 
				MarioGame.Coin | MarioGame.Enemy | MarioGame.Object | 
				MarioGame.Player | MarioGame.Head | MarioGame.Item 
				| MarioGame.BottomCollision | MarioGame.Trampoline;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
		fixturedef.shape = head;
		fixturedef.filter.categoryBits = MarioGame.Player_Head;
		fixturedef.isSensor = true;

		body.createFixture(fixturedef).setUserData(this);
	}

	public void jumpTrampoline(Trampoline trampoline) {
		if (trampoline.currentState == Trampoline.State.Stand)
			body.applyLinearImpulse(new Vector2(0, 7), body.getWorldCenter(), true);
	}
}
