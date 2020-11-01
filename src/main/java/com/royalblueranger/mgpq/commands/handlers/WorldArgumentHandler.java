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

package com.royalblueranger.mgpq.commands.handlers;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.royalblueranger.mgpq.commands.ArgumentHandler;
import com.royalblueranger.mgpq.commands.ArgumentVariable;
import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.CommandError;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public class WorldArgumentHandler extends ArgumentHandler<World> {

    public WorldArgumentHandler() {
        addVariable("sender", "The command executor", new ArgumentVariable<World>() {
            @Override
            public World var(CommandSender sender, CommandArgument argument, String varName)
                throws CommandError {
                if (!(sender instanceof Player)) {
                    throw new CommandError(
                    		MgpqMessages.getMessage( sender, 
                        			Messages.mgpq_commands_invalidAsConsole )
//                        Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
//                            .localizeFor(sender)
                            );
                }

                return ((Player) sender).getLocation().getWorld();
            }
        });
    }

    @Override public World transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
    	World results = null;
    	
    	if ( value != null ) {
    		
    		for ( World world : sender.getServer().getWorlds() ) {
    			if ( world.getName().equalsIgnoreCase( value ) ) {
    				results = world;
    			}
    		}
    	}

    	if ( results == null ) {
    		throw new TransformError( 
    				MgpqMessages.getMessage( sender, 
                			Messages.mgpq_commands_worldNotFound, value )
    				);
    		
//    		return Prison.get().getPlatform().getWorld(value).orElseThrow(() -> new TransformError(
//    				Prison.get().getLocaleManager().getLocalizable("worldNotFound").withReplacements(value)
//    				.localizeFor(sender)));
    	}
        
        return results;
    }
}
