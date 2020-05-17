
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

public class Bomb extends Enemy {

	public Body body;
	private float timer;
	// animations of the bomb
	private Animation walkAnimation;
	private Animation explosionState;
	public enum State {Walk, Explode}
	private Array <TextureRegion> pixels;
	private boolean destroy;
	private boolean dead;
	public State currentState;
	public State previousState;
	public boolean runningRight;

	public Bomb (PlayScreen screen, float x, float y) {
		super(screen, x, y);

		pixels = new Array<TextureRegion>();
		
		// finds the region in the .pack file
		pixels.add(new TextureRegion(screen.getAtlas().findRegion("Bomb"), 0, 0, 30, 31));
		pixels.add(new TextureRegion(screen.getAtlas().findRegion("Bomb"), 35, 0, 30, 31));
		walkAnimation = new Animation(0.3f, pixels);

		for(int i = 0; i < 4; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("Explosion"), i * 33, 0, 36, 34));
		explosionState = new Animation(0.01f, pixels);
		
		timer = 0;
		// sets the position and size of the GUI component
		setBounds(getX(), getY(), 27 / MarioGame.PPM, 30 / MarioGame.PPM);
		// sets the original state 
		currentState = State.Walk;
		previousState = State.Walk;

		destroy = false;
		dead = false;
	}

	public void update(float dt) {
		setRegion(getPixel(dt));

		if(destroy && !dead) {
			dead = true;
			world.destroyBody(body);
			timer = 0;
		}

		else if (!dead) {	 
			body.setLinearVelocity(velocity);
			setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 3);
			setRegion(walkAnimation.getKeyFrame(timer, true));
		}
	}

	public TextureRegion getPixel(float dt) {
		TextureRegion region;

		switch (currentState) {
		case Explode:
			region = explosionState.getKeyFrame(timer);
			break;
		case Walk:
		default:
			region = walkAnimation.getKeyFrame(timer, true);
			break;
		}

		// flips the texture based on the case
		if (velocity.x > 0 && region.isFlipX() == false)
			region.flip(true, false);

		else if (velocity.x < 0 && region.isFlipX() == true)
			region.flip(true, false);

		if (currentState == previousState)
			timer += dt;
		else 
			timer = 0; 

		previousState = currentState;
		return region;
	}

	@Override
	protected void defineEnemy() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6/MarioGame.PPM);

		fixturedef.filter.categoryBits = MarioGame.Enemy;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Brick | MarioGame.Coin
				| MarioGame.Enemy | MarioGame.Object | 
				MarioGame.Player | MarioGame.Trampoline;
		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);

		// Create head
		PolygonShape head = new PolygonShape();
		Vector2[] points = new Vector2[4];
		points[0] = new Vector2(-5, 8).scl(1 /MarioGame.PPM);
		points[1] = new Vector2(5, 8).scl(1 /MarioGame.PPM);
		points[2] = new Vector2(-3,3).scl(1 /MarioGame.PPM);
		points[3] = new Vector2(3,3).scl(1 /MarioGame.PPM);
		head.set(points);

		fixturedef.shape = head;

		// elasticity of when a player jumps of the bombs head
		fixturedef.restitution = 0.5f;
		fixturedef.filter.categoryBits = MarioGame.Head;
		body.createFixture(fixturedef).setUserData(this);	
	}

	// if the bomb is not dead, draw the sprite
	public void draw(Batch batch) {
		if (!dead || timer < 1)
			super.draw(batch);
	}

	@Override
	// checks if Mario makes contact with the Bomb's head
	public void hitOnHead(Mario mario) {
		if (currentState == State.Walk && timer > 1)
			currentState = State.Explode;	
		destroy = true;
		// increments the score by 300
		ScoreBoard.getScore(300);
		ScoreBoard.worldTimer += 5;
	}
}
