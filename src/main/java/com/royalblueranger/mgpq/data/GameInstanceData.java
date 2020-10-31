package com.royalblueranger.mgpq.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.royalblueranger.mgpq.data.GamesData.GameState;

public class GameInstanceData
{
	@JsonIgnore
	private GameData parent;
	
	private String name;
	
	private GameState state;
	
	private Location locationLobbySpawn = null;

	private Location locationGameZonePos1 = null;
	private Location locationGameZonePos2 = null;
	
	private int statsGamesPlayed;
	private int statsTotalPlayersPlayed;
	private long statsTotalTimePlayedSec;
	
	@JsonIgnore
	private List<Player> players;
	
	private int playersStart;
	private int playersCurrent;
	
	
	public GameInstanceData() {
		super();
	}
	
	public GameInstanceData( GameData parent, String name ) {
		super();
		
		this.name = name;
		
		this.state = GameState.disabled_not_configured;
		
		
		this.statsGamesPlayed = 0;
		this.statsTotalPlayersPlayed = 0;
		this.statsTotalTimePlayedSec = 0;
		
		this.players = new ArrayList<>();
		
		this.playersStart = 0;
		this.playersCurrent = 0;
	}
	
	public GameData getParent() {
		return parent;
	}
	public void setParent( GameData parent ){
		this.parent = parent;
	}

	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public GameState getState() {
		return state;
	}
	public void setState( GameState state ) {
		this.state = state;
	}

	public Location getLocationLobbySpawn() {
		return locationLobbySpawn;
	}
	public void setLocationLobbySpawn( Location locationLobbySpawn ) {
		this.locationLobbySpawn = locationLobbySpawn;
	}

	public Location getLocationGameZonePos1() {
		return locationGameZonePos1;
	}
	public void setLocationGameZonePos1( Location locationGameZonePos1 ) {
		this.locationGameZonePos1 = locationGameZonePos1;
	}

	public Location getLocationGameZonePos2() {
		return locationGameZonePos2;
	}
	public void setLocationGameZonePos2( Location locationGameZonePos2 ) {
		this.locationGameZonePos2 = locationGameZonePos2;
	}

	public int getStatsGamesPlayed() {
		return statsGamesPlayed;
	}
	public void setStatsGamesPlayed( int statsGamesPlayed ) {
		this.statsGamesPlayed = statsGamesPlayed;
	}

	public int getStatsTotalPlayersPlayed() {
		return statsTotalPlayersPlayed;
	}
	public void setStatsTotalPlayersPlayed( int statsTotalPlayersPlayed ) {
		this.statsTotalPlayersPlayed = statsTotalPlayersPlayed;
	}

	public long getStatsTotalTimePlayedSec() {
		return statsTotalTimePlayedSec;
	}
	public void setStatsTotalTimePlayedSec( long statsTotalTimePlayedSec ) {
		this.statsTotalTimePlayedSec = statsTotalTimePlayedSec;
	}

	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers( List<Player> players ) {
		this.players = players;
	}

	public int getPlayersStart() {
		return playersStart;
	}
	public void setPlayersStart( int playersStart ) {
		this.playersStart = playersStart;
	}

	public int getPlayersCurrent() {
		return playersCurrent;
	}
	public void setPlayersCurrent( int playersCurrent ) {
		this.playersCurrent = playersCurrent;
	}
}
