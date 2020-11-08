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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.MiniGamePlayerQueue;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;




public class RegisteredCommand {

    private String label;
    private CommandHandler handler;
    private RegisteredCommand parent;
    
    private String description;
    private String[] permissions;
    private String[] altPermissions;
    private boolean onlyPlayers;
    private Method method;
    private Object methodInstance;

    private boolean set = false;

    private ArrayList<ExecutableArgument> methodArguments = new ArrayList<ExecutableArgument>();
    private ArrayList<CommandArgument> arguments = new ArrayList<CommandArgument>();

    private WildcardArgument wildcard;
    
    private ArrayList<Flag> flags = new ArrayList<Flag>();
    private Map<String, Flag> flagsByName = new LinkedHashMap<String, Flag>();

    private ArrayList<RegisteredCommand> suffixes = new ArrayList<RegisteredCommand>();
    private Map<String, RegisteredCommand> suffixesByName = new HashMap<String, RegisteredCommand>();

    
    RegisteredCommand(String label, CommandHandler handler, RegisteredCommand parent) {
        this.label = label;
        this.handler = handler;
        this.parent = parent;
    }

    /**
     * The suffix is converted to all lower case before adding to the map.
     *  
     * @param suffix
     * @param command
     */
    void addSuffixCommand(String suffix, RegisteredCommand command) {
        suffixesByName.put(suffix.toLowerCase(), command);
        suffixes.add(command);
    }

    /**
     * The suffix is converted to all lower case before checking to see if it exists in the map.
     * 
     * @param suffix
     * @return
     */
    boolean doesSuffixCommandExist(String suffix) {
        return suffixesByName.containsKey( suffix.toLowerCase() );
    }
    
    public String getCompleteLabel() {
    	return (parent == null ? "" : parent.getCompleteLabel() + " " ) + 
    			(label == null ? "-noCommandLabelDefined-" : label) ;
    }

    void execute(CommandSender sender, String[] args) {
        if (!testPermission(sender)) {
        	sender.sendMessage( 
        			MgpqMessages.getMessage( sender, 
                			Messages.mgpq_commands_noPermission )
        			);
//            Prison.get().getLocaleManager().getLocalizable("noPermission")
//                .sendTo(sender, LogLevel.ERROR);
            
        	MiniGamePlayerQueue.getInstance().log( 
        		String.format( 
        			"&cLack of Permission Error: &7Player &3%s &7lacks permission to " +
            		"run the command &3%s&7. Permissions needed: [&3%s&7]. Alt Permissions: [&3%s&7]", 
            			sender.getName(), getCompleteLabel(),
            			(permissions == null ? "-none-" : String.join( ", ", permissions )),
            			(altPermissions == null ? "-none-" : String.join( ", ", altPermissions ))
            		));
            return;
        }

        if (args.length > 0) {
            String suffixLabel = args[0].toLowerCase();
            if (suffixLabel.equals(CommandHandler.COMMAND_HELP_TEXT)) {
                sendHelpMessage(sender);
                return;
            }

            RegisteredCommand command = suffixesByName.get(suffixLabel);
            if (command == null) {
                
                executeMethod(sender, args);
            } else {
                String[] nargs = new String[args.length - 1];
                System.arraycopy(args, 1, nargs, 0, args.length - 1);
                command.execute(sender, nargs);
            }
        } else {
            executeMethod(sender, args);
        }

    }

    private void executeMethod(CommandSender sender, String[] args) {
        if (!set) {
            sendHelpMessage(sender);
            return;
        }

        ArrayList<Object> resultArgs = new ArrayList<Object>();
        resultArgs.add(sender);

        Arguments arguments;
        try {
            arguments = new Arguments(args, flagsByName);
        } catch (CommandError e) {
        	MiniGamePlayerQueue.getInstance().
        		logError( sender, e.getColorizedMessage() );
        	
//            Output.get().sendError(sender, e.getColorizedMessage());
            return;
        }

        for (ExecutableArgument ea : this.methodArguments) {
            try {
                resultArgs.add(ea.execute(sender, arguments));
            } catch (CommandError e) {
            	
            	MiniGamePlayerQueue.getInstance().
        			logError( sender, e.getColorizedMessage() );
            	
//                Output.get().sendError(sender, e.getColorizedMessage());
                if (e.showUsage()) {
                    sender.sendMessage(getUsage());
                }
                return;
            }
        }

        try {
            try {
                method.invoke(methodInstance, resultArgs.toArray());
            } 
            catch ( IllegalArgumentException | InvocationTargetException e) {
                if (e.getCause() instanceof CommandError) {
                    CommandError ce = (CommandError) e.getCause();
                    
                    MiniGamePlayerQueue.getInstance().
                    	logError( sender, ce.getColorizedMessage() );
                    
//                    Output.get().sendError(sender, ce.getColorizedMessage());
                    if (ce.showUsage()) {
                        sender.sendMessage(getUsage());
                    }
                } 
                else {
    				StringBuilder sb = new StringBuilder();
    				
    				for ( Object arg : resultArgs ) {
    					sb.append( "[" );
    					sb.append( arg );
    					sb.append( "] " );
    				}

                	String message = "RegisteredCommand.executeMethod(): Invoke error: [" +
                				e.getMessage() + "] cause: [" +
                				(e.getCause() == null ? "" : e.getCause().getMessage()) + "] " + 
                				" target instance: [" +
                				method.getName() + " " + method.getParameterCount() + " " + 
                				methodInstance.getClass().getCanonicalName() + "] " +
                				"command arguments: " + sb.toString()
                				;
                	
                	MiniGamePlayerQueue.getInstance().
        				logError( sender, message );
                	
//                	Output.get().sendError( sender, message );

                	// Generally these errors are major and require program fixes, so throw
                	// the exception so the stacklist is logged.
                    throw e;
                }
            }
        } catch (Exception e) {
        	
        	MiniGamePlayerQueue.getInstance().
				logError( sender, 
		        	MgpqMessages.getMessage( sender, 
		        			Messages.mgpq_commands_internalErrorOccurred ) );
        	
//            Prison.get().getLocaleManager().getLocalizable("internalErrorOccurred")
//                .sendTo(sender, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    private ArgumentHandler<?> getArgumenHandler(Class<?> argumentClass) {
        ArgumentHandler<?> argumentHandler = handler.getArgumentHandler(argumentClass);

        if (argumentHandler == null) {
            throw new RegisterCommandMethodException(method,
                "Could not find a ArgumentHandler for (" + argumentClass.getName() + ")");
        }

        return argumentHandler;
    }

    public List<CommandArgument> getArguments() {
        return arguments;
    }

    public String getDescription() {
        return description;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public String[] getHelpMessage() {
        return handler.getHelpHandler().getHelpMessage(this);
    }

    public String getLabel() {
        return label;
    }

    public RegisteredCommand getParent() {
        return parent;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public String[] getAltPermissions() {
		return altPermissions;
	}

    public RegisteredCommand getSuffixCommand(String suffix) {
        return suffixesByName.get(suffix);
    }

    public List<RegisteredCommand> getSuffixes() {
        return suffixes;
    }

    public String getUsage() {
        return handler.getHelpHandler().getUsage(this);
    }

    public WildcardArgument getWildcard() {
        return wildcard;
    }

    public boolean isOnlyPlayers() {
        return onlyPlayers;
    }

    public boolean isSet() {
        return set;
    }

    public boolean onlyPlayers() {
        return onlyPlayers;
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(getHelpMessage());
    }

    void set(Object methodInstance, Method method) {
        this.methodInstance = methodInstance;
        this.method = method;
        method.setAccessible(true);
        Command command = method.getAnnotation(Command.class);
        Flags flagsAnnotation = method.getAnnotation(Flags.class);
        this.description = command.description();
        this.permissions = command.permissions();
        this.altPermissions = command.altPermissions();
        this.onlyPlayers = command.onlyPlayers();

        Class<?>[] methodParameters = method.getParameterTypes();

        if (methodParameters.length == 0 || !CommandSender.class
            .isAssignableFrom(methodParameters[0])) {
            throw new RegisterCommandMethodException(method,
                "The first parameter in the command method must be assignable to the CommandSender interface.");
        }

        if (flagsAnnotation != null) {
            String[] flags = flagsAnnotation.identifier();
            String[] flagdescriptions = flagsAnnotation.description();

            for (int i = 0; i < flags.length; i++) {
                Flag flag =
                    new Flag(flags[i], i < flagdescriptions.length ? flagdescriptions[i] : "");
                this.flagsByName.put(flags[i], flag);
                this.flags.add(flag);
            }
        }

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 1; i < methodParameters.length; i++) {

            //Find the CommandArgument annotation
            Arg commandArgAnnotation = null;
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType() == Arg.class) {
                    commandArgAnnotation = (Arg) annotation;
                }
            }

            //Find the FlagArg annotation
            FlagArg flagArgAnnotation = null;
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType() == FlagArg.class) {
                    flagArgAnnotation = (FlagArg) annotation;
                }
            }

            //If neither does not exist throw
            if (commandArgAnnotation == null && flagArgAnnotation == null) {
                throw new RegisterCommandMethodException(method,
                    "The command annotation is present on a method, however one of the parameters is not annotated.");
            }

            Flag flag = null;

            if (flagArgAnnotation != null) {
                flag = this.flagsByName.get(flagArgAnnotation.value());
                if (flag == null) {
                    throw new RegisterCommandMethodException(method,
                        "The flag annotation is present on a parameter, however the flag is not defined in the flags annotation.");
                }
            }

            Class<?> argumentClass = methodParameters[i];

            if (commandArgAnnotation == null) {
                if (argumentClass != boolean.class && argumentClass != Boolean.class) {
                    throw new RegisterCommandMethodException(method,
                        "The flag annotation is present on a parameter without the arg annonation, however the parameter type is not an boolean.");
                }

                methodArguments.add(flag);

                continue;
            }

            if (flagArgAnnotation == null) {
                CommandArgument argument;
                if (i == methodParameters.length - 1) {
                    //Find the Wildcard annotation
                    Wildcard wildcard = null;
                    for (Annotation annotation : parameterAnnotations[i]) {
                        if (annotation.annotationType() == Wildcard.class) {
                            wildcard = (Wildcard) annotation;
                        }
                    }

                    if (wildcard != null) {
                        boolean join = wildcard.join();
                        if (!join) {
                            argumentClass = argumentClass.getComponentType();
                            if (argumentClass == null) {
                                throw new RegisterCommandMethodException(method,
                                    "The wildcard argument needs to be an array if join is false.");
                            }
                        }
                        this.wildcard = new WildcardArgument(commandArgAnnotation, argumentClass,
                            getArgumenHandler(argumentClass), join);
                        argument = this.wildcard;

                    } else {
                        argument = new CommandArgument(commandArgAnnotation, argumentClass,
                            getArgumenHandler(argumentClass));
                        arguments.add(argument);
                    }
                } else {
                    argument = new CommandArgument(commandArgAnnotation, argumentClass,
                        getArgumenHandler(argumentClass));
                    arguments.add(argument);
                }

                methodArguments.add(argument);
            } else {
                FlagArgument argument = new FlagArgument(commandArgAnnotation, argumentClass,
                    getArgumenHandler(argumentClass), flag);
                methodArguments.add(argument);
                flag.addArgument(argument);
            }
        }

        this.set = true;
    }

    public boolean testPermission(CommandSender sender) {
        if (!set) {
            return true;
        }

        return handler.getPermissionHandler().hasPermission(sender, permissions);
    }

}
