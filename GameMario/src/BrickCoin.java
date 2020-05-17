
import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class BrickCoin extends Tiles {

	private static TiledMapTileSet tileset;
	private final int blankCoin = 28;

	public BrickCoin(PlayScreen screen, MapObject object) {
		super(screen, object);
		// sets the tile set on the Tiled Map to Pictures.png
		tileset = map.getTileSets().getTileSet("Pictures");
		fixture.setUserData(this);
		// sets the category filter to coin
		setCatFilter(MarioGame.Coin);
	}

	@Override
	public void headHit() {
		// when a mushroom is contained in a coin block spawn the mushroom
		if (object.getProperties().containsKey("mushroom")) {
			screen.spawn(new Define(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioGame.PPM), Mushroom.class));
		}
		getCell().setTile(tileset.getTile(blankCoin));
		// adds 100 to the score
		ScoreBoard.getScore(100);
	}
}
