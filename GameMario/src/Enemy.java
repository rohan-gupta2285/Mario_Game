
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/*
 * A super class where all enemies obtain inheritance from the enemy class
 */
public abstract class Enemy extends Sprite {

	protected World world;
	protected PlayScreen screen;

	// sets the velocity used for the ghosts movements
	public Vector2 velocity;
	public Vector2 velocityGhostPositiveX;
	public Vector2 velocityGhostNegativeX;
	public Vector2 velocityGhostPositiveY;
	public Vector2 velocityGhostNegativeY;
	public Vector2 zeroVelocity;

	public Enemy (PlayScreen screen, float x, float y) {
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x,y);

		defineEnemy();

		// sets the velocity of the ghost for movement
		velocity = new Vector2(0.88f,0);
		velocityGhostPositiveX = new Vector2(1.7f,0);
		velocityGhostNegativeX = new Vector2(-1.2f,0);
		velocityGhostPositiveY = new Vector2(0,0.8f);
		velocityGhostNegativeY = new Vector2(0, -0.8f);
		zeroVelocity = new Vector2(0,0);
	}

	// other enemies inherit this class to create the box2D body
	protected abstract void defineEnemy();

	public abstract void hitOnHead(Mario mario);

	public abstract void update(float dt);

	// reverses the direction of the enemy when it collides with other objects
	public void reverseDirection(boolean x, boolean y){
		if (x)
			velocity.x = -velocity.x;
		if (y)
			velocity.y = -velocity.y;
	}
}
