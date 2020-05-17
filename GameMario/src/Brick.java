
import com.badlogic.gdx.maps.MapObject;

public class Brick extends Tiles {
	public Brick(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		// sets the category filter to brick
		setCatFilter(MarioGame.Brick);
	}

	@Override
	public void headHit() {
		// sets category filter to kill
		setCatFilter(MarioGame.Kill);
		getCell().setTile(null);
		// adds 200 to the score
		ScoreBoard.getScore(200);
	}
}
