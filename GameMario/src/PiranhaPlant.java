
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class PiranhaPlant extends Enemy implements Disposable {

	Body body;
	private float timer; // time in game world
	private Animation eatingAnimation;
	private Array <TextureRegion> pixels;
	public enum State {Move, Stand};
	public State current;
	public State previous;
	private TextureRegion stand;

	private PiranhaPlant piranha;

	public PiranhaPlant(PlayScreen screen, float x, float y) {
		super(screen, x, y);

		pixels = new Array<TextureRegion>();

		for (int i = 0; i < 3; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("PiranhaPlant"), i * 26, 0, 21, 31));
		eatingAnimation = new Animation(0.2f, pixels);
		current = previous = State.Stand;

		// sets the components of the piranha plant on the map
		setBounds(getX(), getY(), 20 / MarioGame.PPM, 36 / MarioGame.PPM);
	}

	@Override
	protected void defineEnemy() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.StaticBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(8/MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Enemy;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Brick | MarioGame.Coin | MarioGame.Object | MarioGame.Player;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);
	}

	public TextureRegion getPixel(float dt) {
		TextureRegion region;

		// changes the current state of the piranha plant
		switch (current) {
		case Stand:
			region = stand;
			break;
		case Move:
		default:
			region = eatingAnimation.getKeyFrame(timer, true);
			break;
		}

		if (current == previous)
			timer += dt;
		else
			timer = 0;

		previous = current;
		return region;
	}

	@Override
	public void update(float dt) {		
		timer += dt;		 
		setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 2);
		// sets the animation to continuous
		setRegion(eatingAnimation.getKeyFrame(timer, true));
	}
	
	@Override
	public void hitOnHead(Mario mario) {

	}

	@Override
	public void dispose() {
		piranha.dispose();
	}
}
