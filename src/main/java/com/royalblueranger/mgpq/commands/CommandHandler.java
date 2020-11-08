/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.royalblueranger.mgpq.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import com.royalblueranger.mgpq.MiniGamePlayerQueue;
import com.royalblueranger.mgpq.RBRPlugIn;
import com.royalblueranger.mgpq.commands.handlers.DoubleArgumentHandler;
import com.royalblueranger.mgpq.commands.handlers.IntegerArgumentHandler;
import com.royalblueranger.mgpq.commands.handlers.PlayerArgumentHandler;
import com.royalblueranger.mgpq.commands.handlers.StringArgumentHandler;
import com.royalblueranger.mgpq.commands.handlers.WorldArgumentHandler;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;


public class CommandHandler {

	public static final String COMMAND_PRIMARY_ROOT_COMMAND = "pq";
	public static final String COMMAND_FALLBACK_PREFIX = "mgpq";

	private Map<String, BaseCommands> registeredCommands;
	
    private RBRPlugIn plugin;
    
    private Map<Class<?>, ArgumentHandler<?>> argumentHandlers =
    											new HashMap<Class<?>, ArgumentHandler<?>>();
    
    private Map<PluginCommand, RootCommand> rootCommands = new HashMap<>();

    public static final String COMMAND_HELP_TEXT = "help";

    
	private List<PluginCommand> commands = new ArrayList<>();
	private Field knownCommands;
	private Field commandMap;

    

    public CommandHandler( RBRPlugIn plugin ) {
        this.plugin = plugin;

        this.registeredCommands = new TreeMap<>();
        
        registerArgumentHandler(String.class, new StringArgumentHandler());
        registerArgumentHandler(int.class, new IntegerArgumentHandler());
        registerArgumentHandler(double.class, new DoubleArgumentHandler());
        registerArgumentHandler(Player.class, new PlayerArgumentHandler());
        registerArgumentHandler(World.class, new WorldArgumentHandler());
//        registerArgumentHandler(BlockType.class, new BlockArgumentHandler());
        
        
    	
        try {
//        	SimpleCommandMap sCmdMap = new SimpleCommandMap(plugin.getServer());
        	
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
        } 
        catch (NoSuchFieldException e) {
            this.plugin.logError(
                    "&c&lReflection error setting up the &3commandMap&c: " +
                    	"&7Ensure that you're using the latest version of Spigot and this plugin.");
            e.printStackTrace();
        }
        try {
        	knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
        	knownCommands.setAccessible(true);
        } 
        catch (NoSuchFieldException e) {
        	this.plugin.logError(
        			"&c&lReflection error setting up the &3knownCommands&c: " +
        				"&7Ensure that you're using the latest version of Spigot and this plugin.");
        	e.printStackTrace();
        }
    }

    

    public void registerCommand(PluginCommand command) {
        try {
        	
        	org.bukkit.command.Command cmd = new org.bukkit.command.Command(
        					command.getLabel(),
        					command.getDescription(), command.getUsage(),
                    							Collections.emptyList()) {

	            @Override 
	            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
	                if (sender instanceof org.bukkit.entity.Player) 
	                {
	                    return onCommand( (org.bukkit.entity.Player) sender, command, commandLabel, args);
	                }
	                return onCommand( sender, command, commandLabel, args);
	            }
	            
	            
			      
            };
        	
            
            @SuppressWarnings( "unused" )
			boolean success = 
            			((SimpleCommandMap) commandMap.get(Bukkit.getServer()))
            				.register(command.getLabel(), COMMAND_FALLBACK_PREFIX, cmd );
            
			// Always record the registered label:
			if ( cmd != null ) {
				command.setLabelRegistered( cmd.getLabel() );
			}
			
            commands.add(command);
            
        } 
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked") 
    public void unregisterCommand(String command) {
        try {
            ((Map<String, Command>) knownCommands
            					.get(commandMap.get(Bukkit.getServer()))).remove(command);
            this.commands.removeIf(pluginCommand -> pluginCommand.getLabel().equals(command));
        } 
        catch (IllegalAccessException e) {
        	plugin.logError( e.getMessage() );
            e.printStackTrace();
        }
    }
    
    public void unregisterAllCommands() {
    	List<String> cmds = new ArrayList<>();
    	
    	for ( PluginCommand pluginCommand : commands ) {
    		cmds.add( pluginCommand.getLabel() );
		}
    	
    	for ( String lable : cmds ) {
    		unregisterCommand( lable );
		}
    }
    
    public PluginCommand findCommand( String label ) {
    	PluginCommand results = null;
    	
    	for ( PluginCommand command : commands ) {
    		if (command.getLabel().equalsIgnoreCase(label)) {
    			results = command;
    			break;
    		}
		}
    	return results;
    }
    
    private PermissionHandler permissionHandler = (sender, permissions) -> {
        for (String perm : permissions) {
            if (!sender.hasPermission(perm)) {
                return false;
            }
        }
        return true;
    };

    private HelpHandler helpHandler = new HelpHandler() {
        private String formatArgument(CommandArgument argument) {
            String def = argument.getDefault();
            if (def.equals(" ")) {
                def = "";
            } else if (def.startsWith("?")) {
                String varName = def.substring(1);
                def = argument.getHandler().getVariableUserFriendlyName(varName);
                if (def == null) {
                    throw new IllegalArgumentException(
                        "The ArgumentVariable '" + varName + "' is not registered.");
                }
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            } else {
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            }

            return ChatColor.AQUA + "[" + argument.getName() + def + ChatColor.AQUA + "] "
                + ChatColor.DARK_AQUA + argument.getDescription();
        }

        @Override 
        public String[] getHelpMessage(RegisteredCommand command) {
            ArrayList<String> message = new ArrayList<String>();

            if (command.isSet()) {
                message.add(ChatColor.DARK_AQUA + command.getDescription());
            }

            message.add(getUsage(command));

            if (command.isSet()) {
                for (CommandArgument argument : command.getArguments()) {
                    message.add(formatArgument(argument));
                }
                if (command.getWildcard() != null) {
                    message.add(formatArgument(command.getWildcard()));
                }
                List<Flag> flags = command.getFlags();
                if (flags.size() > 0) {
                    message.add(ChatColor.DARK_AQUA + "Flags:");
                    for (Flag flag : flags) {
                        StringBuilder args = new StringBuilder();
                        for (FlagArgument argument : flag.getArguments()) {
                            args.append(" [" + argument.getName() + "]");
                        }
                        message.add("-" + flag.getIdentifier() + ChatColor.AQUA + args.toString());
                        for (FlagArgument argument : flag.getArguments()) {
                            message.add(formatArgument(argument));
                        }
                    }
                }
                if ( command.getPermissions() != null && command.getPermissions().length > 0 ||
                	 command.getAltPermissions() != null && command.getAltPermissions().length > 0 ) {
                	
                	StringBuilder sb = new StringBuilder();
                	
                	if ( command.getPermissions() != null && command.getPermissions().length > 0 ) {
                		for ( String perm : command.getPermissions() ) {
                			if ( sb.length() > 0 ) {
                				sb.append( " " );
                			}
                			sb.append( perm );
                		}
                	}
            		if ( command.getAltPermissions() != null && command.getAltPermissions().length > 0 ) {
            			for ( String altPerm : command.getAltPermissions() ) {
            				if ( sb.length() > 0 ) {
            					sb.append( " " );
            				}
            				sb.append( altPerm );
            			}
            		}
            		
            		if ( sb.length() > 0 ) {
            			message.add(ChatColor.DARK_AQUA + "Permissions:");
            			
            			sb.insert( 0, ChatColor.AQUA );
            			sb.insert( 0, "   " );
            			message.add( sb.toString() );
            		}
                	
                }
            }

            List<RegisteredCommand> subcommands = command.getSuffixes();
            if (subcommands.size() > 0) {
                message.add(ChatColor.DARK_AQUA + "Subcommands:");
                // Force a sorting by use of a TreeSet. Collections.sort() would not work.
                TreeSet<String> subCommandSet = new TreeSet<>();
                for (RegisteredCommand scommand : subcommands) {
                	String subCmd = scommand.getUsage();
                	

                	int subCmdSubCnt = scommand.getSuffixes().size();
                	
                	subCommandSet.add(subCmd + (subCmdSubCnt <= 1 ? "" : 
                			ChatColor.DARK_AQUA + " (" + subCmdSubCnt + 
                			" Subcommands)"));
                }
                
                for (String subCmd : subCommandSet) {
                	message.add(subCmd);
                }
            }
            
            if ( command.getLabel().equalsIgnoreCase( COMMAND_PRIMARY_ROOT_COMMAND ) && 
            												rootCommands.size() > 1 ) {
                message.add(ChatColor.DARK_AQUA + "MGPQ Root Commands:");
                // Force a sorting by use of a TreeSet. Collections.sort() would not work.
                TreeSet<String> rootCommandSet = new TreeSet<>();

            	// Try adding in all other root commands:
            	Set<PluginCommand> rootKeys = getRootCommands().keySet();
            	
            	for ( PluginCommand rootKey : rootKeys ) {
//            		String rootCmd = "/" + rootKey.getLabelRegistered();
            		String rootCmd = rootKey.getUsage();
            		
            		rootCommandSet.add( rootCmd );
            		
            	}
            	
            	for (String rootCmd : rootCommandSet) {
            		message.add(rootCmd);
            	}
            }
            

            return message.toArray(new String[0]);
        }

        /**
         * <p>If the registration of this command was not successful as the original 
         * label, it would have had the fallback prefix added until the command
         * was unique. Therefore, use that registered label instead so the users
         * will know what command they need to enter.
         * </p>
         * 
         * @param label
         * @param command
         * @return 
         */
        private String getRootCommandRegisteredLabel( String label, RegisteredCommand command ) {
        	String commandLabel = command.getLabel();
        	if ( command instanceof RootCommand ) {
        		RootCommand rootCommand = (RootCommand) command;
        		if ( rootCommand.getBukkitCommand().getLabelRegistered() != null ) {
        			commandLabel = rootCommand.getBukkitCommand().getLabelRegistered();
        		}
        	}
        	return commandLabel;
        }
        
        @Override 
        public String getUsage(RegisteredCommand command) {
            StringBuilder usage = new StringBuilder();
            
            String cmdLabel = getRootCommandRegisteredLabel( command.getLabel(), command );
            usage.append(cmdLabel);

            RegisteredCommand parent = command.getParent();
            while (parent != null) {
            	String label = getRootCommandRegisteredLabel( parent.getLabel(), parent );
                usage.insert(0, label + " ");
                parent = parent.getParent();
            }

            usage.insert(0, "/");

            if (!command.isSet()) {
                return usage.toString();
            }

            usage.append(ChatColor.AQUA);

            for (CommandArgument argument : command.getArguments()) {
                usage.append(" [" + argument.getName() + "]");
            }

            usage.append(ChatColor.WHITE);

            for (Flag flag : command.getFlags()) {
                usage.append(" (-" + flag.getIdentifier() + ChatColor.AQUA);
                for (FlagArgument arg : flag.getArguments()) {
                    usage.append(" [" + arg.getName() + "]");
                }
                usage.append(ChatColor.WHITE + ")");
            }

            if (command.getWildcard() != null) {
                usage.append(ChatColor.AQUA + " [" + command.getWildcard().getName() + "]");
            }

            return usage.toString();
        }
    };

    @SuppressWarnings("unchecked")
    public <T> ArgumentHandler<? extends T> getArgumentHandler(Class<T> clazz) {
        return (ArgumentHandler<? extends T>) argumentHandlers.get(clazz);
    }

    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    public void setHelpHandler(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public <T> void registerArgumentHandler(Class<? extends T> clazz,
        ArgumentHandler<T> argHandler) {
        if (argumentHandlers.get(clazz) != null) {
            throw new IllegalArgumentException(
                "There is already a ArgumentHandler bound to the class " + clazz.getName() + ".");
        }

        argHandler.handler = this;
        argumentHandlers.put(clazz, argHandler);
    }

    public void registerCommands(BaseCommands commands) {
    	
    	// Keep a reference to the registered command object so it can be 
    	// accessed in the future if needed for other uses.
    	getRegisteredCommands().put( commands.getCmdGroup(), commands );
    	
    	
        for (Method method : commands.getClass().getDeclaredMethods()) {
            Command commandAnno = method.getAnnotation(Command.class);
            if (commandAnno == null) {
                continue;
            }

            String[] identifiers = commandAnno.identifier().split(" ");
            if (identifiers.length == 0) {
                throw new RegisterCommandMethodException(method, "Invalid identifiers");
            }
            
 
            PluginCommand rootPCommand = findCommand(identifiers[0]);

            if ( rootPCommand == null ) {
            	rootPCommand = new PluginCommand(identifiers[0], commandAnno.description(),
            																	"/" + identifiers[0]);
                registerCommand(rootPCommand);
            } 

            
            // If getRootCommands() does not contain the rootPCommand then add it:
            if ( !getRootCommands().containsKey( rootPCommand ) ) {
            	getRootCommands().put( rootPCommand, new RootCommand( rootPCommand, this ) );
            }
            
            RegisteredCommand mainCommand = getRootCommands().get( rootPCommand );

            
            for (int i = 1; i < identifiers.length; i++) {
                String suffix = identifiers[i];
                
                // NOTE: There appears to be an issue with the following command... it's not returning prexisting suffixes:
                if (mainCommand.doesSuffixCommandExist(suffix)) {
                    mainCommand = mainCommand.getSuffixCommand(suffix);
                } else {
                    RegisteredCommand newCommand = new RegisteredCommand(suffix, this, mainCommand);
                    mainCommand.addSuffixCommand(suffix, newCommand);
                    mainCommand = newCommand;
                }
            }


            // Validate that the first parameter, if it exists, is actually a CommandSender:
            if ( method.getParameterCount() > 0 ) {
            	
            	// The first parameter "should" be CommandSender:
            	Class<?> cmdSender = method.getParameterTypes()[0];
            	
            	if ( !cmdSender.getSimpleName().equalsIgnoreCase( "CommandSender") ) {
            		
            		MiniGamePlayerQueue.getInstance().
	    				logError(  
	            			String.format( 
	            				"Possible issue has been detected with " +
	            				"registering a command where " +
	            				"the first parameter is not a CommandSender: " +
	            				"class = [%s] method = [%s] first parameter type = [%s]",
	            				method.getDeclaringClass().getSimpleName(), method.getName(),
	            				cmdSender.getSimpleName()
	            					));
            	}
            	
            }
            
            mainCommand.set(commands, method);
        }
    }


    public boolean onCommand(CommandSender sender, PluginCommand command, String label,
    								String[] args) {
    	
        RootCommand rootCommand = rootCommands.get(command);
        if (rootCommand == null) {
        	// plugin.log( "onCommand: " + command.getLabel() + " : 2 : No root command found. " );
            return false;
        }

        if (rootCommand.onlyPlayers() && !(sender instanceof Player)) {
        	
        	sender.sendMessage( 
        			MgpqMessages.getMessage( sender, 
        					Messages.mgpq_commands_invalidAsConsole )
        			);
        }
        
        else {
        	
        	rootCommand.execute(sender, args);
        }
        

        return true;
    }

	public Map<String, BaseCommands> getRegisteredCommands() {
		return registeredCommands;
	}
	public void setRegisteredCommands( Map<String, BaseCommands> registeredCommands ) {
		this.registeredCommands = registeredCommands;
	}

	public Map<PluginCommand, RootCommand> getRootCommands() {
		return rootCommands;
	}
	public void setRootCommands( Map<PluginCommand, RootCommand> rootCommands ) {
		this.rootCommands = rootCommands;
	}

/*
 * ###Tab-Complete###
 * 
 * Disabled for now until a full solution can be implemented for tab complete.
 * 
    public List<String> getRootCommandKeys() {
    	List<String> results = new ArrayList<>();
    	
    	Set<PluginCommand> keys = rootCommands.keySet();
    	for ( PluginCommand pluginCommand : keys ) {
    		// These are the core command sets:
			results.add( pluginCommand.getLabel() );
			
			// Then expand them to all the sub commands that are assoicated with the cores:
			RootCommand cmd = rootCommands.get( pluginCommand );
			List<RegisteredCommand> regCmds = cmd.getSuffixes();
			for ( RegisteredCommand regCmd : regCmds ) {
				results.add( pluginCommand.getLabel() + " " + regCmd.getLabel() );
			}
			
		}
    	
    	return results;
    }
 */
    
    
}
