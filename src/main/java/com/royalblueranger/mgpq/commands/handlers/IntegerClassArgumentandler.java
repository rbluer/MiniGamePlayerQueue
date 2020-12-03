package com.royalblueranger.mgpq.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public class IntegerClassArgumentandler 
		extends NumberArgumentHandler<Integer> {

    public IntegerClassArgumentandler() {
    }

    @Override public Integer transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Integer results = null;
    
    	if ( value != null ) {
    		
    		//value = value.replaceAll("$|%", "");
    		if ( value.trim().length() > 0 ) {
    			try {
    				results = Integer.parseInt(value);
    			} catch (NumberFormatException e) {
    				throw new TransformError(
    					MgpqMessages.getMessage( sender, 
    	                			Messages.mgpq_commands_numberParseError, value )
    	            	);
    			}
    		}
    	}
    	return results;
    }
}
