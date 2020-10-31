package com.royalblueranger.mgpq.data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is the core-root data object that contains all games, and all 
 * of their associated data and references.
 *
 */
public class GamesData
{
	private TreeMap<String, GameData> games;
	
	// Using ArrayList for now... will replace with a priority queue later.
	@JsonIgnore
	private List<GameInstanceData> instances;
	
	public enum GameState {

		waiting_has_players,
		waiting_no_players,
		
		active_starting,
		
		active,
		
		game_over_resetting,
		
		disabled,
		
		disabled_not_configured
		;
	}
	
	public GamesData() {
		super();
		
		this.games = new TreeMap<>();
		
		this.instances = new ArrayList<>();
	}
	
	

	public TreeMap<String, GameData> getGames() {
		return games;
	}
	public void setGames( TreeMap<String, GameData> games ) {
		this.games = games;
	}
}
