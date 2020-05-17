
import com.badlogic.gdx.physics.box2d.Contact;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/*
 *  Built in collision detection listener which shows which fixtures are involved when contacts are made
 */
public class WorldContactListener implements ContactListener {

	/*
	 *  Source Code
	 * @ https://www.youtube.com/watch?v=W68WswniZCI
	 */

	@Override
	public void beginContact(Contact contact) {
		Fixture fixture_1 = contact.getFixtureA();
		Fixture fixture_2 = contact.getFixtureB();

		Fixture head;
		Fixture object;

		int define = fixture_1.getFilterData().categoryBits | fixture_2.getFilterData().categoryBits;

		//Check if mario head
		if (fixture_1.getUserData() == "head"|| fixture_2.getUserData() == "head") {
			if (fixture_1.getUserData() == "head")
				head = fixture_1;
			else 
				head = fixture_2;

			if (head == fixture_1)
				object = fixture_2;
			else
				object = fixture_1;

			if (object.getUserData() != null && Tiles.class.isAssignableFrom(object.getUserData().getClass())) {
				((Tiles) object.getUserData()).headHit();
			}
		}

		switch (define) {

		case MarioGame.Player | MarioGame.Enemy:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Player)
				((Mario) fixture_1.getUserData()).hit((Enemy)fixture_2.getUserData());
			else
				((Mario) fixture_2.getUserData()).hit((Enemy)fixture_1.getUserData());
			break;

		case MarioGame.Coin | MarioGame.Player_Head:
			if (fixture_1.getFilterData().categoryBits == MarioGame.Player_Head)
				((Tiles)(fixture_2.getUserData())).headHit();
			else if (fixture_2.getFilterData().categoryBits == MarioGame.Player_Head)
				((Tiles)(fixture_1.getUserData())).headHit();
			break;

		case MarioGame.Head | MarioGame.Player:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Head)
				((Enemy)fixture_1.getUserData()).hitOnHead((Mario) fixture_2.getUserData());
			else
				((Enemy)fixture_2.getUserData()).hitOnHead((Mario) fixture_1.getUserData());
			break;

		case MarioGame.Enemy | MarioGame.Object:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Enemy)
				((Enemy)fixture_1.getUserData()).reverseDirection(true, false);
			else
				((Enemy)fixture_2.getUserData()).reverseDirection(true, false);
			break;

		case MarioGame.Enemy | MarioGame.Enemy:
			((Enemy)fixture_1.getUserData()).reverseDirection(true, false);
			((Enemy)fixture_2.getUserData()).reverseDirection(true, false);
			break;

		case MarioGame.Item | MarioGame.Object:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Item)
				((Item)fixture_1.getUserData()).reverseVelocity(true, false);
			else
				((Item)fixture_2.getUserData()).reverseVelocity(true, false);
			break;

		case MarioGame.Item | MarioGame.Player:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Item)
				((Item)fixture_1.getUserData()).useItem((Mario) fixture_2.getUserData());
			else
				((Item)fixture_2.getUserData()).useItem((Mario) fixture_1.getUserData());
			break;

		case MarioGame.Player | MarioGame.Trampoline:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Player) 
				((Mario)fixture_1.getUserData()).jumpTrampoline((Trampoline) fixture_2.getUserData());
			else
				((Mario)fixture_2.getUserData()).jumpTrampoline((Trampoline) fixture_1.getUserData());
			break;
		case MarioGame.Enemy | MarioGame.Trampoline:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Enemy)
				((Enemy)fixture_1.getUserData()).reverseDirection(true, false);
			else
				((Enemy)fixture_2.getUserData()).reverseDirection(true, false);
			break;
		case MarioGame.Enemy | MarioGame.Brick:
			if(fixture_1.getFilterData().categoryBits == MarioGame.Enemy)
				((Enemy)fixture_1.getUserData()).reverseDirection(true, false);
			else
				((Enemy)fixture_2.getUserData()).reverseDirection(true, false);
			break;
		}
	}	

	@Override
	public void endContact(Contact contact) {	 }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		 }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {		}
}