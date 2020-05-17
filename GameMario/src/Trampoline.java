
import com.badlogic.gdx.graphics.g2d.Animation;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Trampoline extends Sprite {
	
	Body body;
	private Array <TextureRegion> pixels;
	private Animation up_Animation;
	public State currentState;
	public enum State {Move, Stand};
	public State previousState;
	private World world;
	private float timer;
	private TextureRegion stand;

	public Trampoline(PlayScreen screen, float x, float y) {
		this.world = screen.getWorld();
		setPosition(x,y);
		defineTrampoline();
		pixels = new Array<TextureRegion>();

		for (int i = 0; i < 2; i++)
			pixels.add(new TextureRegion(screen.getAtlas().findRegion("Trampoline"), 0, i * 19, 18, 19));
		up_Animation = new Animation(0.9f, pixels);
		currentState = previousState = State.Stand;

		setBounds(getX(), getY(), 16 / MarioGame.PPM, 20 / MarioGame.PPM);
	}

	protected void defineTrampoline() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.StaticBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(3/MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Trampoline;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Brick | 
				MarioGame.Coin | MarioGame.Object | MarioGame.Player | MarioGame.Enemy;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);	
	}

	public TextureRegion getPixel(float dt) {
		TextureRegion region;

		switch (currentState) {
			case Stand:
				region = stand;
				break;
			case Move:
			default:
				region = up_Animation.getKeyFrame(timer, true);
				break;
		}
		
		if (currentState == previousState)
			timer += dt;
		
		else
			timer = 0;
		
		previousState = currentState;
		return region;
	}

	public void update(float dt) {
		timer += dt;
		setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 16);
		setRegion(up_Animation.getKeyFrame(timer, true));
	}
}
