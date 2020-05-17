
import com.badlogic.gdx.graphics.g2d.Batch;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/*
 *  Inheritance class used for all items in the game
 */
public abstract class Item extends Sprite {

	protected PlayScreen screen;
	protected World world;
	protected Vector2 velocity;
	protected boolean destroy;
	protected boolean destroyed;
	protected Body b2body;

	public Item(PlayScreen screen, float x, float y) {
		this.screen = screen;
		this.world = screen.getWorld();
		setPosition(x,y);
		setBounds(getX(), getY(), 16 / MarioGame.PPM, 16 / MarioGame.PPM);
		createItem();
		destroy = false;
		destroyed = false;	
	}

	public abstract void createItem();
	public abstract void useItem(Mario mario);

	public void update(float dt) {
		if (destroy && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
		}
	}

	public void draw(Batch batch) {
		if (!destroyed)
			super.draw(batch);
	}

	// getter method
	public void getDestroy() {
		destroy = true;
	}

	// reverses the direction of the item when it collides with another object
	public void reverseVelocity(boolean x, boolean y) {
		if(x) 
			velocity.x = -velocity.x;
		if(x) 
			velocity.y = -velocity.y;
	}
}
