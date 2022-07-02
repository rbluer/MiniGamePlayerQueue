package com.royalblueranger.mgpq.blues.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.blues.commands.CommandArgument;
import com.royalblueranger.mgpq.blues.commands.TransformError;
import com.royalblueranger.mgpq.blues.messages.CommandMessages;
import com.royalblueranger.mgpq.blues.messages.CommandMessages.Messages;

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
            		CommandMessages.getMessage( sender, 
                			Messages.mgpq_commands_numberParseError, value )
            	);
        }
    }

}
