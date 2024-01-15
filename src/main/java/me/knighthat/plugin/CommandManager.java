package me.knighthat.plugin;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.brannstroom.expbottle.handlers.MainHandler;
import me.brannstroom.expbottle.model.Experience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        // Check if sender is player
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( Component.text( "Only player can execute this command!" ) );
            return true;
        }

        Player player = (Player) sender;
        // Return if this player doesn't have "expbottle.user" permission or isn't 'oped'
        if ( !( player.hasPermission( "expbottle.user" ) || player.isOp() ) ) {
            player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.noPermission, 0, Experience.getExp( player ) ) );
            return true;
        }

        // Print command usage if no argument provided
        if ( args.length == 0 ) {
            printCommandUsage( player );
            return true;
        }

        // If config.yml (XP Bottle Lore) does not contain "%xp%", then send error
        if ( !MainHandler.containsXpLine() && !args[0].equalsIgnoreCase( "reload" ) ) {
            player.sendMessage( InfoKeeper.couldNotFindXpLine );
            return true;
        }

        // If argument is "all" or 1 of its aliases
        if ( args[0].equalsIgnoreCase( "all" ) || InfoKeeper.allAliases.contains( args[0] ) ) {
            int xp = Experience.getExp( player );

            if ( isOutOfRange( player, xp ) )
                return true;

            if ( InfoKeeper.tax && !player.hasPermission( "expbottle.bypasstax" ) ) {
                player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.successfulWithdraw, (int) ( xp / ( 1 + InfoKeeper.taxAmount ) ), Experience.getExp( player ) ) );
                MainHandler.givePlayerExpBottle( player, (int) ( xp / ( 1 + InfoKeeper.taxAmount ) ) );
                ExpCalculator.take( player, xp );
                //                            MainHandler.removePlayerExp(player, xp);
            } else {
                player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.successfulWithdraw, xp, Experience.getExp( player ) ) );
                MainHandler.givePlayerExpBottle( player, xp );
                ExpCalculator.take( player, xp );
                //                            MainHandler.removePlayerExp(player, xp);
            }

            return true;
        }

        // If argument is "reload" or 1 of its aliases
        if ( args[0].equalsIgnoreCase( "reload" ) || InfoKeeper.reloadAliases.contains( args[0] ) ) {
            if ( player.hasPermission( "expbottle.admin" ) ) {
                InfoKeeper.updateConfig();
                player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.reloadSuccessful, 0, Experience.getExp( player ) ) );
            } else {
                player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.noPermission, 0, Experience.getExp( player ) ) );
            }
            return true;
        }

        // If argument is "give" or 1 of its aliases
        if ( args[0].equalsIgnoreCase( "give" ) || InfoKeeper.giveAliases.contains( args[0] ) ) {
            // "give" arg requires 2 additional arguments: player and amount
            if ( args.length < 3 ) {
                printCommandUsage( player );
                return true;
            }

            // Convert the third argument to amount of XP
            // and give you player indicated in the second argument
            try {
                int amount = Integer.parseInt( args[2] );
                if ( isOutOfRange( player, amount ) )
                    return true;

                // Verify if indicated player is online and
                // check for empty slot in said player's inventory.
                Player target = Bukkit.getPlayer( args[1] );
                if ( target != null ) {
                    MainHandler.givePlayerExpBottle( target, amount );
                    if ( target != player ) {
                        target.sendMessage( InfoKeeper.getReceiveInfoKeeper( player, target, InfoKeeper.xpBottleReceive, amount ) );
                        player.sendMessage( InfoKeeper.getReceiveInfoKeeper( player, target, InfoKeeper.xpBottleGive, amount ) );
                    } else {
                        player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.giveYourselfXp, amount, Experience.getExp( player ) ) );
                    }
                } else {
                    player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.playerNotOnline, 0, Experience.getExp( player ) ) );
                }
            } catch ( NumberFormatException e ) {
                player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.xpNotANumber, 0, Experience.getExp( player ) ) );
            }

            return true;
        }

        // If the first argument isn't "all", "give", "xp", or "reload"
        // then default the first argument to become the amount of XP
        // to put into experience bottle.
        try {
            int amount = Integer.parseInt( args[0] );
            if ( isOutOfRange( player, amount ) )
                return true;

            if ( InfoKeeper.tax && !player.hasPermission( "expbottle.bypasstax" ) ) {
                double price = ( amount * ( 1 + InfoKeeper.taxAmount ) );
                if ( price <= Experience.getExp( player ) ) {
                    player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.successfulWithdraw, amount, Experience.getExp( player ) ) );
                    MainHandler.givePlayerExpBottle( player, amount );
                    ExpCalculator.take( player, (int) price );
                    //                                MainHandler.removePlayerExp(player, (int)price);
                } else {
                    player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.notEnoughXp, amount, Experience.getExp( player ) ) );
                }
            } else {
                if ( amount <= Experience.getExp( player ) ) {
                    player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.successfulWithdraw, amount, Experience.getExp( player ) ) );
                    MainHandler.givePlayerExpBottle( player, amount );
                    ExpCalculator.take( player, amount );
                    //                                MainHandler.removePlayerExp(player, xp+1);
                } else {
                    player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.notEnoughXp, amount, Experience.getExp( player ) ) );
                }
            }
        } catch ( NumberFormatException e ) {
            player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.xpNotANumber, 0, Experience.getExp( player ) ) );
        }

        return true;
    }

    /**
     * Show usage to this player. The message is different
     * if that player is an admin (or oped).
     *
     * @param recipient who will get this message
     */
    private void printCommandUsage( @NotNull Player recipient ) {
        if ( recipient.hasPermission( "expbottle.admin" ) ) {
            recipient.sendMessage( InfoKeeper.getInfoKeeper( recipient, InfoKeeper.cmdUsageAdmin, 0, Experience.getExp( recipient ) ) );
        } else {
            recipient.sendMessage( InfoKeeper.getInfoKeeper( recipient, InfoKeeper.cmdUsageUser, 0, Experience.getExp( recipient ) ) );
        }
    }

    /**
     * Verify if the given amount is within the
     * acceptable range stated in config.yml file.
     * <p>
     * Player gets a decline message if the amount does
     * go out of range
     *
     * @param player command executor
     * @param amount experience points
     * @return whether the amount is out of range
     */
    private boolean isOutOfRange( @NotNull Player player, int amount ) {
        boolean betweenRange = amount <= InfoKeeper.maxXp && amount >= InfoKeeper.minXp;

        // Send an error message if the amount is out of acceptable range
        if ( !betweenRange )
            player.sendMessage( InfoKeeper.getInfoKeeper( player, InfoKeeper.overMaxUnderMin, amount, Experience.getExp( player ) ) );

        return !betweenRange;
    }
}
