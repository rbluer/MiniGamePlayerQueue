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

package com.royalblueranger.blues.commands.handlers;

import org.bukkit.command.CommandSender;

import com.royalblueranger.blues.commands.CommandArgument;
import com.royalblueranger.blues.commands.TransformError;
import com.royalblueranger.blues.messages.CommandMessages;
import com.royalblueranger.blues.messages.CommandMessages.Messages;

public class IntegerArgumentHandler 
		extends NumberArgumentHandler<Integer> {

    public IntegerArgumentHandler() {
    }

    @Override public Integer transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new TransformError(
            	CommandMessages.getMessage( sender, 
                			Messages.mgpq_commands_numberParseError, value )
            	);
        }
    }
}
