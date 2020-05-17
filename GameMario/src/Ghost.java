
import com.badlogic.gdx.graphics.g2d.Animation;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Ghost extends Enemy {

	public Body body;
	public Bomb bomb;
	private float timer;
	private Animation walkingAnimation;
	private TextureRegion region;
	private boolean destroy;
	private boolean dead;

	public Ghost(PlayScreen screen, float x, float y) {
		super(screen, x, y);

		// sets the animation of the ghost
		region = new TextureRegion(screen.getAtlas().findRegion("Ghost"), 0, 0, 32, 30);

		walkingAnimation = new Animation(0.4f,region);
		timer = 0;
		setBounds(getX(), getY(), 23 / MarioGame.PPM,  25 / MarioGame.PPM);
		destroy = false;
		dead = false;
	}

	/*
	 * AI
	 * Checks Mario's position is in relation to the ghost, and the ghost will move accordingly to follow Mario
	 */
	public void getMove(Mario mario) {	
		if (timer > 3) {
			if (!mario.dead) {
				body.setLinearVelocity(velocityGhostNegativeX);
				if (mario.getY() > this.getY()) 
					body.setLinearVelocity(velocityGhostPositiveY);

				else if (mario.getX() > this.getX()) {
					if (region.isFlipX())
						region.flip(true, false);
					body.setLinearVelocity(velocityGhostPositiveX);
				}

				else if (mario.getX() < this.getX() && region.isFlipX() == false) {
					region.flip(true, false);
					body.setLinearVelocity(velocityGhostNegativeX);
					body.setLinearVelocity(velocityGhostNegativeY);
				}
			}
			else if (mario.dead){
				body.setLinearVelocity(zeroVelocity);
				if (region.isFlipX())
					region.flip(false, false);
			}
		}
	}

	public void update(float dt) {
		timer += dt;
		if(destroy && !dead) {
			world.destroyBody(body);
			dead = true;
			timer = 0;
		}
		else if (!dead) {	 
			setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() / 2);
			setRegion(walkingAnimation.getKeyFrame(timer, true));
		}
	}

	@Override
	/*
	 * Creates the body and fixture of the Ghost
	 */
	protected void defineEnemy() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(20/MarioGame.PPM, 150/MarioGame.PPM);
		bodydef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(7/MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Enemy;
		fixturedef.filter.maskBits = MarioGame.Ground | MarioGame.Player;

		fixturedef.shape = shape;
		body.createFixture(fixturedef).setUserData(this);	
	}

	/*
	 * when alive draw the texture
	 */
	public void draw(Batch batch) {
		if (!dead || timer < 1)
			super.draw(batch);
	}

	@Override
	public void hitOnHead(Mario mario) {
		destroy = true;
	}
}
