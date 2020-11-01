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
import com.royalblueranger.mgpq.messages.MgpqMessages;
import com.royalblueranger.mgpq.messages.MgpqMessages.Messages;

public abstract class NumberArgumentHandler<T extends Number> extends ArgumentHandler<T> {

    public NumberArgumentHandler() {
        addVerifier("min", new ArgumentVerifier<T>() {
            @Override
            public void verify(CommandSender sender, CommandArgument argument, String verifyName,
                String[] verifyArgs, T value, String valueRaw) throws VerifyError {
                if (verifyArgs.length != 1) {
                    throw new InvalidVerifyArgument(argument.getName());
                }

                try {
                    double min = Double.parseDouble(verifyArgs[0]);
                    if (value.doubleValue() < min) {
                        throw new VerifyError(
                        	MgpqMessages.getMessage( sender, 
                        			Messages.mgpq_commands_numberTooLow, 
                            		verifyArgs[0], String.valueOf(min) )
//                            Prison.get().getLocaleManager().getLocalizable("numberTooLow")
//                                .withReplacements(verifyArgs[0], String.valueOf(min))
//                                .localizeFor(sender)
                                );
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidVerifyArgument(argument.getName());
                }
            }
        });

        addVerifier("max", new ArgumentVerifier<T>() {
            @Override
            public void verify(CommandSender sender, CommandArgument argument, String verifyName,
                String[] verifyArgs, T value, String valueRaw) throws VerifyError {
                if (verifyArgs.length != 1) {
                    throw new InvalidVerifyArgument(argument.getName());
                }

                try {
                    double max = Double.parseDouble(verifyArgs[0]);
                    if (value.doubleValue() > max) {
                        throw new VerifyError(
                        	MgpqMessages.getMessage( sender, 	
                        			Messages.mgpq_commands_numberTooHigh, 
                        			verifyArgs[0], String.valueOf(max) )
//                            Prison.get().getLocaleManager().getLocalizable("numberTooHigh")
//                                .withReplacements(verifyArgs[0], String.valueOf(max))
//                                .localizeFor(sender)
                                );
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidVerifyArgument(argument.getName());
                }
            }
        });

        addVerifier("range", new ArgumentVerifier<T>() {
            @Override
            public void verify(CommandSender sender, CommandArgument argument, String verifyName,
                String[] verifyArgs, T value, String valueRaw) throws VerifyError {
                if (verifyArgs.length != 2) {
                    throw new InvalidVerifyArgument(argument.getName());
                }

                try {
                    double min = Double.parseDouble(verifyArgs[0]);
                    double max = Double.parseDouble(verifyArgs[1]);
                    double dvalue = value.doubleValue();
                    if (dvalue < min || dvalue > max) {
                        throw new VerifyError(
                        		MgpqMessages.getMessage( sender, 	
                            			Messages.mgpq_commands_numberRangeError, 
                            			valueRaw, verifyArgs[0], verifyArgs[1] )
//                            Prison.get().getLocaleManager().getLocalizable("numberRangeError")
//                                .withReplacements(valueRaw, verifyArgs[0], verifyArgs[1])
//                                .localizeFor(sender)
                                );
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidVerifyArgument(argument.getName());
                }
            }
        });
    }
}
