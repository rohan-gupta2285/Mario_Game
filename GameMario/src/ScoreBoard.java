
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScoreBoard implements Disposable{
	public Stage stages;
	private Viewport viewport;

	public static int worldTimer;
	private float timeCount;
	private static int score;
	private static int level;

	// Set Labels to each 
	private Label countdownLabel;
	private static Label scoreLabel;
	private Label timeLabel;
	private Label levelLabel;
	private Label worldLabel;
	private Label nameLabel;

	private boolean timeUp;

	public ScoreBoard(SpriteBatch s) {
		worldTimer = 100;
		timeCount = 0;
		score = 0;
		level = 1;

		viewport = new FitViewport(MarioGame.width, MarioGame.height, new OrthographicCamera());
		stages = new Stage(viewport, s);

		Table table = new Table();
		table.top();
		table.setFillParent(true);

		countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		worldLabel = new Label(String.format("World", level), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		nameLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		table.add(nameLabel).expandX().padTop(0);
		table.add(worldLabel).expandX().padTop(0);
		table.add(timeLabel).expandX().padTop(0);

		table.row();

		table.add(scoreLabel).expandX().padTop(0);
		table.add(levelLabel).expandX().padTop(0);
		table.add(countdownLabel).expandX().padTop(0);

		stages.addActor(table);	
	}

	public void update (float dt) {
		timeCount += dt;
		if (timeCount >= 1) {
			worldTimer--;
			countdownLabel.setText(String.format("%03d", worldTimer));
			timeCount = 0;
		}
	}

	// adds the score 
	public static void getScore(int value) {
		score += value;
		scoreLabel.setText(String.format("%06d", score));
	}
	
	public static int getTotalScore() {
		return score;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stages.dispose();
	}

	public boolean isTimeUp() { 
		return timeUp; 
	}
}