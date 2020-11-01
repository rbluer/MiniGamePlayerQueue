package com.royalblueranger.mgpq.messages;

import org.bukkit.command.CommandSender;

public class MgpqMessages {
	
	private static MgpqMessages instance;

	/**
	 * <p>Warning: constructor is public only so the normal plugin manager can instantiate the class.
	 * Never ever use this, but instead use getInstance() if you need to use this class since
	 * there can only be one instance of this class in the VM.
	 */
	public MgpqMessages()
	{
		super();

		MgpqMessages.instance = this;
	}
	
	public static String getMessage( CommandSender sender, Messages message, Object... args ) {
		String results = null;
		
		if ( message != null ) {
			
			// Currently only one message, but can use the enum to get mappings
			// to other languages.
		
			results = String.format( message.getMessage(), args );
		}
		
		return results;
	}

	/**
	 * <p>Please note that this plugin DOES not follow the standard
	 * singleton pattern! The tooling framework that controls the plugins
	 * helps to ensure that it only gets instantiated once, so trust is placed
	 * heavily there.  Just in case the plugin was not instantiated I'm keeping
	 * the typical singleton code for getting the instance; I expect and hope
	 * it will never be used since that would imply a failure of another sort
	 * within the plugin manager.</p>
	 *
	 * @return
	 */
	public static MgpqMessages getInstance()
	{
		if ( instance == null )
		{
			synchronized ( MgpqMessages.class )
			{
				if ( instance == null )
				{
					instance = new MgpqMessages();
				}
			}
		}
		return instance;
	}


	public enum Messages {
		
		mgpq_test_message("A test message. Please ignore."),
		
		mgpq_commands_includeErrors("[%s] has an invalid value."),
		mgpq_commands_excludeError("[%s] has an invalid value."),
		
		mgpq_commands_invalidAsConsole("Console is unable to perform this action"),
		mgpq_commands_missingArgument("The argument [%s] is not defined (it has no default value)."),
		mgpq_commands_missingFlagArgument("The flag -%s does not have the required parameters."),
		mgpq_commands_undefinedFlagArgument("The argument [%s] to the flag -%s is not defined."),
		mgpq_commands_internalErrorOccurred("An internal error occurred while attempting to perform this command."),
		mgpq_commands_noPermission("You lack the necessary permissions to perform this command."),

		mgpq_commands_numberParseError("The parameter [%s] is not a number."),
		mgpq_commands_numberTooLow("The parameter [%s] must be equal or greater than %s."),
		mgpq_commands_numberTooHigh("The parameter [%s] must be equal or less than %s."),
		mgpq_commands_numberRangeError("The parameter [%s] must be equal or greater than %s and less than or equal to %s."),
		mgpq_commands_tooFewCharacters("The parameter [%s] must be equal or greater than %s characters."),
		mgpq_commands_tooManyCharacters("The parameter [%s] must be equal or less than %s characters."),
		mgpq_commands_playerNotOnline("The player %1 is not online."),
		mgpq_commands_worldNotFound("The world %1 was not found."),
		
		;
		
		
		private final String key;
		private final String message;
		
		private Messages( String message ) {
			this.key = getName( this );
			this.message = message;
		}

		public String getKey() {
			return key;
		}

		public String getMessage() {
			return message;
		}
		
		/**
		 * The enum names are defined with underscores, but this function
		 * replaces them with periods, which are the more common way of 
		 * using parameters.
		 * 
		 * @return
		 */
		private static String getName( Messages message) {
			return message.name().replaceFirst( "_", "." );
		}
		
		public static Messages fromString( String messageKey ) {
			Messages results = null;
			
			if ( messageKey != null ) {
				messageKey = messageKey.trim();
				
				for ( Messages m : values() ) {
					if ( messageKey.equalsIgnoreCase( m.name() ) ||
							messageKey.equalsIgnoreCase( m.getKey() )) {
						results = m;
						break;
					}
				}
			}
			
			
			return results;
		}
		
	}
}
