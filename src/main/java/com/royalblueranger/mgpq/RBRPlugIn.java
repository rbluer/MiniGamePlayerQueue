package com.royalblueranger.mgpq;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import com.royalblueranger.mgpq.commands.Command;
import com.royalblueranger.mgpq.commands.CommandHandler;
import com.royalblueranger.mgpq.commands.PluginCommand;
import com.royalblueranger.mgpq.db.SQLite;





public abstract class RBRPlugIn
	extends JavaPlugin
{
	private List<PluginCommand> commands = new ArrayList<>();
	private CommandHandler commandHandler;
	private Field knownCommands;
	private Field commandMap;
	
	private boolean logsColor = true;
	private boolean logsDebug = true;

	private SQLite db;

    public RBRPlugIn()
    {
    	// Setup the configs first:
    	this.logsColor = getConfig().getBoolean( "logs.color", true );
    	this.logsDebug = getConfig().getBoolean( "logs.debug", true );

    }

    

    @Override
    public void onEnable()
    {
    	log( "Initializing..." );

    	initializeDb( "MiniGamePlayerQueue" );
    	
    	initCommandMap();
    	this.commandHandler = new CommandHandler( this );


//    	getServer().getPluginManager().registerEvents( this, this );

    	

//    	this.getCommand("RBRTpGrinder").setExecutor(new CommandRBRTpGrinder());



        // Load all existing grinders:
        //grinders = getDb().loadAllGrinders();

    }
    
    /**
     *  Fired when plugin is disabled
     */
    @Override
    public void onDisable()
    {

    	// Flush any unsaved database data:

    	log( "Finished shutting down." );

    }

    public void initializeDb( String databaseName )
    {
    	// create, register, build, and migrate the database if needed:
    	this.db = new SQLite( this, databaseName );
    	
    	this.commandHandler = new CommandHandler( this );
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
    		message =  ChatColor.RED + "[Debug]" + ChatColor.GREEN + " " + message;
    		log( message );
    	}
    }
    
    public String logError( String message )
    {
    	message =  ChatColor.RED + "[Error]" + ChatColor.LIGHT_PURPLE + " " + message;
    	log( message );
    	return message;
    }
    public String logError( CommandSender sender, String message )
    {
    	message =  logError( message );
    	sender.sendMessage( message );
    	return message;
    }

	public SQLite getDb()
	{
		return db;
	}
	


    public void registerCommand(PluginCommand command) {
        try {
        	org.bukkit.command.Command cmd = new org.bukkit.command.Command(command.getLabel(), command.getDescription(), command.getUsage(),
                    							Collections.emptyList()) {

                    @Override 
                    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                        if (sender instanceof org.bukkit.entity.Player) 
                        {
                            return getCommandHandler()
                                .onCommand( (org.bukkit.entity.Player) sender, command, commandLabel, args);
                        }
                        return getCommandHandler()
                            .onCommand( sender, command, commandLabel, args);
                        
                    }

			      
            };
        	
            @SuppressWarnings( "unused" )
			boolean success = 
            			((SimpleCommandMap) commandMap.get(Bukkit.getServer()))
            				.register(command.getLabel(), "mgpq", cmd );
            
            commands.add(command);
            
//            if ( !success ) {
//            	Output.get().logInfo( "SpigotPlatform.registerCommand: %s  " +
//            			"Duplicate command. Fall back to Prison: [%s] ", command.getLabel(), 
//            			cmd.getLabel() );
//            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
	
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
    
    public PluginCommand getMgpqCommand( String label ) {
    	PluginCommand results = null;
    	
    	for ( PluginCommand command : commands ) {
    		if (command.getLabel().equalsIgnoreCase(label)) {
    			results = command;
    		}
		}
    	return results;
    }
    @SuppressWarnings("unchecked") 
    public void unregisterCommand(String command) {
        try {
            ((Map<String, Command>) knownCommands
                .get(commandMap.get(Bukkit.getServer()))).remove(command);
            this.commands.removeIf(pluginCommand -> pluginCommand.getLabel().equals(command));
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // This should only happen if something's wrong up there.
        }
    }
    
    private void initCommandMap() {
        try {
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
        } 
        catch (NoSuchFieldException e) {
            logError(
                    "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }
}
