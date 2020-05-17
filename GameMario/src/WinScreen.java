
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class  WinScreen implements Screen {

	private Stage stage;
	private MarioGame game;
	Skin skin;// to add to button 

	//sets size of button 
	private static final int BUTTON_WIDTH = 210;
	private static final int BUTTON_HEIGHT = 65;
	private static final int PLAY_BUTTON_Y = 215;

	public WinScreen(MarioGame game) {
		this.game = game;
		create();
	}

	private void create() {
		// TODO Auto-generated method stub
		stage  = new Stage();

		BaseActor background = new BaseActor();
		background.setTexture( new Texture(Gdx.files.internal("Background1.png")) ); //adds background image 
		stage.addActor( background );//adds background to stage 

		BaseActor title = new BaseActor();
		title.setTexture( new Texture(Gdx.files.internal("YouWin.png")) ); //adds "Super Runner" title
		title.setPosition(130,300); 
		stage .addActor( title ); //adds title to the stage 

		Gdx.input.setInputProcessor(stage);// Make the stage consume events caused in our case by the mouse 

		createBasicSkin(); //calls the format of the button 
		TextButton newGameButton = new TextButton("Play Again", skin); // Uses the initialized skin
		newGameButton.setPosition(310 ,  PLAY_BUTTON_Y);
		stage.addActor(newGameButton);
		
		Label.LabelStyle font= new Label.LabelStyle(new BitmapFont(),com.badlogic.gdx.graphics.Color.WHITE);
		Table table= new Table();
		table.center();
		table.setFillParent(true);

		Label superRunner= new Label("Score: " + ScoreBoard.getTotalScore(), font);
		
		table.add(superRunner).expandX().padTop(200);
		table.center();
		table.row();
		stage.addActor(table);
	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		int x= 310;

		//checks if the user is clicking within the button dimensions 
		if (Gdx.input.getX()<x + BUTTON_WIDTH && Gdx.input.getX()>x &&Gdx.input.getY()< PLAY_BUTTON_Y+ BUTTON_HEIGHT &&Gdx.input.getY()>PLAY_BUTTON_Y) {
			if(Gdx.input.isTouched()==true) {
				this.dispose();
				game.setScreen (new PlayScreen((MarioGame)game));
			}
		}

		// update
		stage.act(delta);

		// draw graphics
		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}


	private void createBasicSkin(){

		//Create a font
		BitmapFont font = new BitmapFont();
		skin = new Skin();
		skin.add("default", font);

		//Create a texture
		Pixmap pixmap = new Pixmap(BUTTON_WIDTH,BUTTON_HEIGHT, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background",new Texture(pixmap));

		//Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
	}

	public void resize(int width, int height) {  }

	public void pause()   {  }

	public void resume()  {  }

	public void dispose() {  }

	public void show()    {  }

	public void hide()    {  }	
}