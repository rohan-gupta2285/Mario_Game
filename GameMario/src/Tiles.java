
import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Tiles {
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bound;
	protected Body body;
	protected Fixture fixture;
	protected PlayScreen screen;
	
	protected MapObject object;
	
	public Tiles(PlayScreen screen, MapObject object) {
		this.object = object;
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.bound = ((RectangleMapObject) object).getRectangle();
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bound.getX() + bound.getWidth() / 2)/ MarioGame.PPM, (bound.getY() + bound.getHeight() / 2)/ MarioGame.PPM);
		
		body = world.createBody(bdef);
		
		shape.setAsBox(bound.getWidth()/2/ MarioGame.PPM, bound.getHeight()/2/MarioGame.PPM);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
	}
	
	public abstract void headHit();
	
	public void setCatFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}
	
	// sets the cell object layers from the tiled map
	public TiledMapTileLayer.Cell getCell() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int)(body.getPosition().x * MarioGame.PPM / 16),
                (int)(body.getPosition().y * MarioGame.PPM / 16));
	}	
}