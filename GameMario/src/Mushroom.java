
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Mushroom extends Item {

	public Mushroom(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
		velocity = new Vector2(0.7f,0); // sets the velocity in the x-axis
	}

	@Override
	public void createItem() {
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(getX(), getY());
		bodydef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodydef);

		FixtureDef fixturedef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6/MarioGame.PPM);
		fixturedef.filter.categoryBits = MarioGame.Item;
		fixturedef.filter.maskBits = MarioGame.Player | MarioGame.Object 
				| MarioGame.Ground | MarioGame.Coin | MarioGame.Brick;

		fixturedef.shape = shape;
		b2body.createFixture(fixturedef).setUserData(this);
	}

	@Override
	// Adjusts the speed boost based on the direction of Mario
	public void useItem(Mario mario) {
		if (mario.runningRight == true)
			mario.body.applyLinearImpulse(new Vector2(5, 0f), mario.body.getWorldCenter(), true);
		else
			mario.body.applyLinearImpulse(new Vector2(-5, 0f), mario.body.getWorldCenter(), true);
		getDestroy();
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		velocity.y = b2body.getLinearVelocity().y;
		b2body.setLinearVelocity(velocity);
	}
}
