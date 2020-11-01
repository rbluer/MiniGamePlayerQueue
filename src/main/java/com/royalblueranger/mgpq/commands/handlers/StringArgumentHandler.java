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

import org.bukkit.command.CommandSender;

import com.royalblueranger.mgpq.commands.ArgumentHandler;
import com.royalblueranger.mgpq.commands.ArgumentVerifier;
import com.royalblueranger.mgpq.commands.CommandArgument;
import com.royalblueranger.mgpq.commands.InvalidVerifyArgument;
import com.royalblueranger.mgpq.commands.TransformError;
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public class StringArgumentHandler extends ArgumentHandler<String> {

    public StringArgumentHandler() {
        addVerifier("min", new ArgumentVerifier<String>() {
            @Override
            public void verify(CommandSender sender, CommandArgument argument, String verifyName,
                String[] verifyArgs, String value, String valueRaw) throws VerifyError {
                if (verifyArgs.length != 1) {
                    throw new InvalidVerifyArgument(argument.getName());
                }

                try {
                    int min = Integer.parseInt(verifyArgs[0]);
                    if (value.length() < min) {
                        throw new VerifyError(
                        	MgpqMessages.getMessage( sender, 
                            			Messages.mgpq_commands_tooFewCharacters,
                            			valueRaw, String.valueOf(min) )
//                            Prison.get().getLocaleManager().getLocalizable("tooFewCharacters")
//                                .withReplacements(valueRaw, String.valueOf(min))
//                                .localizeFor(sender)
                                );
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidVerifyArgument(argument.getName());
                }
            }
        });

        addVerifier("max", new ArgumentVerifier<String>() {
            @Override
            public void verify(CommandSender sender, CommandArgument argument, String verifyName,
                String[] verifyArgs, String value, String valueRaw) throws VerifyError {
                if (verifyArgs.length != 1) {
                    throw new InvalidVerifyArgument(argument.getName());
                }

                try {
                    int max = Integer.parseInt(verifyArgs[0]);
                    if (value.length() > max) {
                        throw new VerifyError(
                        	MgpqMessages.getMessage( sender, 
                            			Messages.mgpq_commands_tooManyCharacters,
                            			valueRaw, String.valueOf(max) )
//                            Prison.get().getLocaleManager().getLocalizable("tooManyCharacters")
//                                .withReplacements(valueRaw, String.valueOf(max))
//                                .localizeFor(sender)
                                );
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidVerifyArgument(argument.getName());
                }
            }
        });
    }

    @Override public String transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        return value;
    }
}
