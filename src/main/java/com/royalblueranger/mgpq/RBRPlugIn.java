package com.royalblueranger.mgpq;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.royalblueranger.mgpq.db.SQLite;

public abstract class RBRPlugIn
	extends JavaPlugin
{
	private boolean logsColor = true;
	private boolean logsDebug = true;

	private SQLite db;

    public RBRPlugIn()
    {
    	// Setup the configs first:
    	this.logsColor = getConfig().getBoolean( "logs.color", true );
    	this.logsDebug = getConfig().getBoolean( "logs.debug", true );

    }


    public void initializeDb( String databaseName )
    {
    	// create, register, build, and migrate the database if needed:
    	this.db = new SQLite( this, databaseName );
    }

    /**
     * Note that you cannot color normal server log entries; only calls to this function.
     *
     * @param message
     */
    public void log( String message )
    {
    	message =
    			ChatColor.GRAY + "[" +
    			ChatColor.WHITE + "Royal" +
    			ChatColor.AQUA + "Blue's" +
    			ChatColor.WHITE + " MGPQ" +
    			ChatColor.GRAY + "] " + message;
    	if ( !logsColor )
    	{
    		message = ChatColor.stripColor( message );
    	}

    	Bukkit.getConsoleSender().sendMessage( message );
    }

    public void logDebug( String message )
    {
    	if ( logsDebug )
    	{
    		message =  ChatColor.RED + "[Debug]" + ChatColor.RED + " " + message;
    		log( message );
    	}
    }

	public SQLite getDb()
	{
		return db;
	}
}
