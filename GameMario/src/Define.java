
import com.badlogic.gdx.math.Vector2;

/*
 * Used to define/spawn items
 */
public class Define {
	public Vector2 position;
	public Class<Mushroom> type;

	public Define(Vector2 position, Class<Mushroom> type) {
		this.position = position;
		this.type = type;
	}
}
