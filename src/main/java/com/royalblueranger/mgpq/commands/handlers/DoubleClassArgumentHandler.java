package com.royalblueranger.mgpq.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public class DoubleClassArgumentHandler 
		extends NumberArgumentHandler<Double> {

    public DoubleClassArgumentHandler() {
    }

    @Override 
    public Double transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Double results = null;
    
    	if ( value != null ) {
    		
    		value = value.replaceAll("$|%", "");
    		if ( value.trim().length() > 0 ) {
    			try {
    				results = Double.parseDouble(value);
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
