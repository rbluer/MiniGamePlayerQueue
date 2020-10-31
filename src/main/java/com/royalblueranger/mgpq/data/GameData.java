package com.royalblueranger.mgpq.data;

import java.util.ArrayList;
import java.util.List;

public class GameData
{
	private String name;
	
	private String pluginName;
	private String pluginVersion;
	private Object plugin;
	
	private List<GameInstanceData> instances;

	private String gameCmdEnqueuePlayer;
	private String gameCmdStartGame;
	
	private int playersPerGameMin;
	private int playersPerGameMax;
	
	private int statsGamesPlayed;
	private int statsTotalPlayersPlayed;
	private long statsTotalTimePlayedSec;
	
	public GameData() {
		super();
	}
	public GameData( String name, String pluginName, String pluginVersion ) {
		super();
		
		this.name = name;
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		
		this.instances = new ArrayList<>();
		
		this.playersPerGameMin = 0;
		this.playersPerGameMax = 0;
		
		this.statsGamesPlayed = 0;
		this.statsTotalPlayersPlayed = 0;
		this.statsTotalTimePlayedSec = 0;
	}
	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName( String pluginName ) {
		this.pluginName = pluginName;
	}
	
	public String getPluginVersion() {
		return pluginVersion;
	}
	public void setPluginVersion( String pluginVersion ) {
		this.pluginVersion = pluginVersion;
	}
	
	public Object getPlugin() {
		return plugin;
	}
	public void setPlugin( Object plugin ) {
		this.plugin = plugin;
	}
	
	public List<GameInstanceData> getInstances() {
		return instances;
	}
	public void setInstances( List<GameInstanceData> instances ) {
		this.instances = instances;
	}
	
	public String getGameCmdEnqueuePlayer() {
		return gameCmdEnqueuePlayer;
	}
	public void setGameCmdEnqueuePlayer( String gameCmdEnqueuePlayer ) {
		this.gameCmdEnqueuePlayer = gameCmdEnqueuePlayer;
	}
	
	public String getGameCmdStartGame() {
		return gameCmdStartGame;
	}
	public void setGameCmdStartGame( String gameCmdStartGame ) {
		this.gameCmdStartGame = gameCmdStartGame;
	}
	
	public int getPlayersPerGameMin() {
		return playersPerGameMin;
	}
	public void setPlayersPerGameMin( int playersPerGameMin ) {
		this.playersPerGameMin = playersPerGameMin;
	}
	
	public int getPlayersPerGameMax() {
		return playersPerGameMax;
	}
	public void setPlayersPerGameMax( int playersPerGameMax ) {
		this.playersPerGameMax = playersPerGameMax;
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
	
}
