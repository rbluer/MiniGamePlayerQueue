package com.royalblueranger.mgpq.command;

import org.bukkit.command.CommandSender;

import com.royalblueranger.blues.commands.BaseCommands;
import com.royalblueranger.blues.commands.Command;
import com.royalblueranger.mgpq.MiniGamePlayerQueue;

public class CommandsMgpqCore
		extends BaseCommands
{
	
	public CommandsMgpqCore() {
		super("core" );
	}
	
	
	@Command( identifier = "pq version", 
				description = "Displays this plugins version and other details",
				onlyPlayers = false )
	public void versionInformationCommand( CommandSender sender ) {
		
		String message = String.format( "Mini-Game Player Queue: version %s ", 
				MiniGamePlayerQueue.getInstance().getPluginVersion() );
		
		sender.sendMessage( message );
		MiniGamePlayerQueue.getInstance().log( message );
	}
	
	
	@Command( identifier = "pq pets cats",
			aliases = {"cute kittens", "cats"},
			onlyPlayers = false )
	public void pqCatsCommand( CommandSender sender ) {
		sender.sendMessage( "oof cats" );
	}
	@Command( identifier = "pq pets dogs",
			aliases = {"cute puppies", "dogs", "pq pets doggos"},
			onlyPlayers = false )
	public void pqDogsCommand( CommandSender sender ) {
		sender.sendMessage( "oof dogs" );
	}

	
	@Command( identifier = "mgpq test", 
			description = "Just another command to test the command handler",
			onlyPlayers = false )
	public void mgpqTest1Command( CommandSender sender ) {
		
		String message = "### simple test ###";
		
		sender.sendMessage( message );
		MiniGamePlayerQueue.getInstance().log( message );
	}
	
	@Command( identifier = "mgpq more", 
			description = "Just another command to test the command handler",
			onlyPlayers = false )
	public void mgpqTest2Command( CommandSender sender ) {
		
		String message = "### simple test - more ###";
		
		sender.sendMessage( message );
		MiniGamePlayerQueue.getInstance().log( message );
	}
	
	
	@Command( identifier = "mgpq more stuff", 
			description = "Just another command to test the command handler",
			onlyPlayers = false )
	public void mgpqTest3Command( CommandSender sender ) {
		
		String message = "### simple test - more stuff ###";
		
		sender.sendMessage( message );
		MiniGamePlayerQueue.getInstance().log( message );
	}
	
	
}
