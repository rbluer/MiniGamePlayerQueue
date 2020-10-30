package com.royalblueranger.mgpq.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRBRTpGrinder
	implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		boolean success = false;

		if (sender instanceof Player) {
			Player player = (Player) sender;

//			GrinderBuilder gb = new GrinderBuilder();
//			success = gb.rbrTpGrinder( player );

			player.sendMessage( "Done. Thanks." );
		}

		return success;
	}

}
