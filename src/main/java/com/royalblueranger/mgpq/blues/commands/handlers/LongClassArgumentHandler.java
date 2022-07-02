package com.royalblueranger.mgpq.blues.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.blues.commands.CommandArgument;
import com.royalblueranger.mgpq.blues.commands.TransformError;
import com.royalblueranger.mgpq.blues.messages.CommandMessages;
import com.royalblueranger.mgpq.blues.messages.CommandMessages.Messages;

public class LongClassArgumentHandler 
		extends NumberArgumentHandler<Long> {

    public LongClassArgumentHandler() {
    }

    @Override 
    public Long transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Long results = null;
    	
    	if ( value != null ) {
    		try {
    			results = Long.parseLong(value);
    		} 
    		catch (NumberFormatException e) {
    			throw new TransformError(
    					CommandMessages.getMessage( sender, 
                    			Messages.mgpq_commands_numberParseError, value )
                	);
    		}
    	}
        
        return results;
    }


}
