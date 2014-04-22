package com.slauson.tactics.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slauson.tactics.TacticsGame;

public class MainMenu implements com.badlogic.gdx.Screen {
	
	private static final float FONT_FACTOR = 0.005f;
	private static final float TABLE_ROW_PAD_FACTOR_VERTICAL = 0.25f;
	private static final float START_BUTTON_PAD_FACTOR_VERTICAL = 0.05f;
	private static final float START_BUTTON_PAD_FACTOR_HORIZONTAL = 0.05f;
	
	private TextButton startButton;
	private TacticsGame game;
	
	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	
	private BitmapFont defaultBitmapFont;
	private TextButtonStyle defaultTextButtonStyle;
	private LabelStyle defaultLabelStyle;
	
	public MainMenu(TacticsGame game) {
		this.game = game;
		createMenu();
	}
	
	private void createMenu() {
		stage = new Stage();
		batch = new SpriteBatch();
		
		skin = new Skin();
		
        // white pixel map
        Pixmap white = new Pixmap(1, 1, Format.RGBA8888);
        white.setColor(Color.WHITE);
        white.fill();
        skin.add("white", new Texture(white));
        
        // black pixel map
        Pixmap black = new Pixmap(1, 1, Format.RGBA8888);
        black.setColor(Color.BLACK);
        black.fill();
        skin.add("black", new Texture(black));
 
        // default bitmap font
        defaultBitmapFont = new BitmapFont();
        defaultBitmapFont.scale(1);
        skin.add("default", defaultBitmapFont);
 
        // default text button style
        defaultTextButtonStyle = new TextButtonStyle();
        defaultTextButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        defaultTextButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        defaultTextButtonStyle.checked = skin.newDrawable("white", Color.BLACK);
        defaultTextButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        defaultTextButtonStyle.font = skin.getFont("default");
        skin.add("default", defaultTextButtonStyle);
        
        // configure a label style
        defaultLabelStyle = new LabelStyle();
        defaultLabelStyle.font = skin.getFont("default");
        defaultLabelStyle.background = skin.newDrawable("black");
        skin.add("default", defaultLabelStyle);
        
        // get stage to get input processors
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		
		defaultBitmapFont.setScale(width * FONT_FACTOR);
		
		stage.clear();
		
        // play button
		startButton = new TextButton("Play", skin);
		startButton.pad(START_BUTTON_PAD_FACTOR_VERTICAL * height,
				START_BUTTON_PAD_FACTOR_HORIZONTAL * width,
				START_BUTTON_PAD_FACTOR_VERTICAL * height,
				START_BUTTON_PAD_FACTOR_HORIZONTAL * width);
		startButton.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {				
				game.showOverworld();
			}
		});
		
        Table table = new Table();
		table.setSkin(skin);
		table.setFillParent(true);
		
		// add the title
		table.add("Tactics").top().center();
		
		// move to the next row
		table.row();
		// add the start-game button sized
		table.add(startButton).padTop(height * TABLE_ROW_PAD_FACTOR_VERTICAL).center();
		
		stage.addActor(table);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
	
}
