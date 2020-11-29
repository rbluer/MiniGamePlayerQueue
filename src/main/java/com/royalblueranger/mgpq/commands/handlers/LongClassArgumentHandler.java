package com.royalblueranger.mgpq.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

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
    					MgpqMessages.getMessage( sender, 
                    			Messages.mgpq_commands_numberParseError, value )
                	);
    		}
    	}
        
        return results;
    }


}
