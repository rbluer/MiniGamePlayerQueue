package com.royalblueranger.mgpq;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.royalblueranger.mgpq.blues.commands.CommandHandler;
import com.royalblueranger.mgpq.db.SQLite;


/**
 * <p>This abstract class is intended to handle more of the messy plugin stuff
 * so the main class for this plugin can focus more on the plugin specific 
 * details.  Examples of the messy stuff would be logging, database initialization,
 * or command registration.
 * </p>
 *
 */
public abstract class RBRPlugIn
	extends JavaPlugin
{
	private CommandHandler commandHandler;
	
	private boolean logsColor = true;
	private boolean logsDebug = true;

	private SQLite db;

    public RBRPlugIn()
    {
    	// Setup the configs first:
    	this.logsColor = getConfig().getBoolean( "logs.color", true );
    	this.logsDebug = getConfig().getBoolean( "logs.debug", true );

    }

    
    /**
     * Fired when the plugin is enabled.
     */
    @Override
    public void onEnable()
    {
    	log( "Initializing..." );

//    	initializeDb( "MiniGamePlayerQueue" );
    	
    	


//    	getServer().getPluginManager().registerEvents( this, this );

    	



        // Load all existing grinders:
        //grinders = getDb().loadAllGrinders();

    }
    
    /**
     *  Fired when plugin is disabled
     */
    @Override
    public void onDisable()
    {
    	getCommandHandler().unregisterAllCommands();
    	
    	// Flush any unsaved database data:

    	log( "Finished shutting down." );

    }

    public void initializeDb( String databaseName )
    {
    	// create, register, build, and migrate the database if needed:
    	this.db = new SQLite( this, databaseName );
    }

    
    public String getPluginVersion() {
        return getDescription().getVersion();
    }

    public File getPluginDirectory() {
        return getDataFolder();
    }
    
    private String logPrefix() {
    	return ChatColor.GRAY + "[" +
    			ChatColor.AQUA + "Blue's" +
    			ChatColor.WHITE + " MGPQ" +
    			ChatColor.GRAY + "] ";
    }
    
    /**
     * Note that you cannot color normal server log entries; only calls to this function.
     *
     * @param message
     */
    public void log( String message, Object... args )
    {
    	message = logPrefix() + 
    			String.format( message, args );
    	
    	message = ChatColor.translateAlternateColorCodes('&', message);

    	if ( !logsColor )
    	{
    		message = ChatColor.stripColor( message );
    	}

    	Bukkit.getConsoleSender().sendMessage( message );
    }

    public void logDebug( String message, Object... args )
    {
    	if ( logsDebug )
    	{
    		message =  ChatColor.RED + "[Debug]" + ChatColor.GREEN + " " + 
						String.format( message, args );
    		log( message );
    	}
    }
    
    public String logError( String message, Object... args )
    {
    	message =  ChatColor.RED + "[Error]" + ChatColor.LIGHT_PURPLE + " " +
    					String.format( message, args );
    	log( message );
    	return message;
    }
    public String logError( CommandSender sender, String message, Object... args )
    {
    	message =  logError( message, args );
    	sender.sendMessage( message );
    	return message;
    }

	public SQLite getDb()
	{
		return db;
	}
	

    /**
     * <p>This initializes the command handler, and the related command maps,
     * if the commandHander class variable is null.
     * </p>
     * 
     */
    public CommandHandler getCommandHandler() {
    	if ( commandHandler == null ) {
    		
    		this.commandHandler = new CommandHandler( this );
    	}
        return commandHandler;
    }
    
}
