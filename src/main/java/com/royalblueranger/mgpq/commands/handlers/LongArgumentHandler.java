package com.royalblueranger.mgpq.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public class LongArgumentHandler 
		extends NumberArgumentHandler<Long> {

    public LongArgumentHandler() {
    }

    @Override 
    public Long transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new TransformError(
            		MgpqMessages.getMessage( sender, 
                			Messages.mgpq_commands_numberParseError, value )
            	);
        }
    }

}
