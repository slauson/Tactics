package com.slauson.tactics.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.slauson.tactics.model.Battle;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.model.Overworld.Phase;
import com.slauson.tactics.utils.RegionUtils;

/**
 * Renders the overworld.
 * @author josh
 *
 */
public class OverworldRenderer extends Renderer {
	
	private static final Color SELECTED_REGION_COLOR = Color.WHITE;
	private static final float MARKED_REGION_COLOR_FACTOR = 0.25f;
	private static final float NON_TURN_COLOR_FACTOR = 0.5f;
	private static final int TURN_BOX_HEIGHT = 20;
	private static final int TURN_BOX_MIN_WIDTH = 20;
	private static final int TURN_BOX_MAX_WIDTH = 100;
	
	private ShapeRenderer renderer;
	
	private Overworld overworld;
	private Battle battle;
	
	public OverworldRenderer(Overworld overworld, Battle battle) {
		super();
		this.overworld = overworld;
		this.battle = battle;
		
		renderer = new ShapeRenderer();
	}
	
	/**
	 * Renders the overworld.
	 */
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {

		if (overworld.phase == Phase.ATTACK || overworld.phase == Phase.BATTLE) {
			Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		} else {
			Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		}
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		renderer.setProjectionMatrix(camera.combined);
		
//		if (overworld.activePlayer().type != Player.PlayerType.PLAYER) {
//			System.out.println(overworld);
//		}
		
		// draw colors
		renderer.begin(ShapeType.FilledRectangle);
		for (Region region : overworld.regions) {
			// selected region
			if (region.selected) {
				renderer.setColor(SELECTED_REGION_COLOR);
			}
			// marked region
			else if (region.marked) {
				renderer.setColor(region.player.color.r*MARKED_REGION_COLOR_FACTOR, region.player.color.g*MARKED_REGION_COLOR_FACTOR, region.player.color.b*MARKED_REGION_COLOR_FACTOR, 1);
			}
			// normal region
			else {
				renderer.setColor(region.player.color);
			}
			
			renderer.filledRect(region.position.x, region.position.y, region.bounds.width, region.bounds.height);
		}
		renderer.end();
		
		// draw units
		for (Region region : overworld.regions) {
			
			if (region.unit != null) {
			
				// player with current turn has black units
				if (region.player == overworld.activePlayer() && (region.unit.hasAttack || region.unit.hasMove || overworld.phase == Phase.REINFORCE)) {
					renderer.setColor(Color.BLACK);
				}
				// other players are lighter
				else {
					renderer.setColor(region.player.color.r*NON_TURN_COLOR_FACTOR,
							region.player.color.g*NON_TURN_COLOR_FACTOR,
							region.player.color.b*NON_TURN_COLOR_FACTOR,
							1);
				}
				
				// set color based on health and player color
//				renderer.setColor(region.player.color.r*(Unit.MAX_HEALTH-region.unit.health)/Unit.MAX_HEALTH/2,
//						region.player.color.g*(Unit.MAX_HEALTH-region.unit.health)/Unit.MAX_HEALTH/2,
//						region.player.color.b*(Unit.MAX_HEALTH-region.unit.health)/Unit.MAX_HEALTH/2,
//						1);
				
				// draw all idle units
				if (!region.unit.state.animate()) {
					drawUnit(renderer, region);
				}
			}
		}

		// draw outline
		renderer.begin(ShapeType.Rectangle);
		renderer.setColor(Color.BLACK);
		for (Region region : overworld.regions) {
			renderer.rect(region.position.x, region.position.y, region.bounds.width, region.bounds.height);
		}
		renderer.end();
		
		// draw turns
		screenRenderer.begin(ShapeType.FilledRectangle);
		int activePlayerNum = 0;
		for (int i = 0; i < overworld.players.size(); i++) {
			Player player = overworld.players.get((i + overworld.activePlayerIndex()) % overworld.players.size());
			
			// only draw active players
			if (player.regions > 0) {
			
				screenRenderer.setColor(player.color);
				float width = RegionUtils.getOverworldStrength(overworld, player) * TURN_BOX_MAX_WIDTH;
				
				if (width < TURN_BOX_MIN_WIDTH) {
					width = TURN_BOX_MIN_WIDTH;
				}

				// draw box
				screenRenderer.filledRect(screenWidth - width, screenHeight - (i+1)*TURN_BOX_HEIGHT, width, TURN_BOX_HEIGHT);
				
				// draw reinforcement lines
				// TODO scale these based on size of turn box?
				screenRenderer.setColor(Color.BLACK);
				for (int j = 0; j < player.reinforcements; j++) {
					screenRenderer.filledRect(screenWidth - (j+1)*2, screenHeight - (activePlayerNum+1)*TURN_BOX_HEIGHT, 1, TURN_BOX_HEIGHT);
				}
				
				activePlayerNum++;
			}
		}
		screenRenderer.end();
		
		super.render(camera, delta, debug);
	}
}
