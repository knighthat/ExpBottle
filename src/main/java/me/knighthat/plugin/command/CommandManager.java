package me.knighthat.plugin.command;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.brannstroom.expbottle.handlers.MainHandler;
import me.brannstroom.expbottle.model.Experience;
import me.knighthat.plugin.ExpCalculator;
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
        if ( !checkPerm( player, "user" ) ) {
            InfoKeeper.sendInfoKeeper( player, InfoKeeper.noPermission, 0 );
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

        // If argument is "reload" or 1 of its aliases
        if ( args[0].equalsIgnoreCase( "reload" ) || InfoKeeper.reloadAliases.contains( args[0] ) ) {
            String msg = InfoKeeper.noPermission;
            if ( checkPerm( player, "admin" ) ) {
                InfoKeeper.updateConfig();
                msg = InfoKeeper.reloadSuccessful;
            }
            InfoKeeper.sendInfoKeeper( player, msg, 0 );
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
                if ( target == null ) {
                    InfoKeeper.sendInfoKeeper( player, InfoKeeper.playerNotOnline, 0 );
                    return true;
                }

                MainHandler.givePlayerExpBottle( target, amount );
                if ( target != player ) {
                    target.sendMessage( InfoKeeper.receiveInfoKeeper( player, target, InfoKeeper.xpBottleReceive, amount ) );
                    player.sendMessage( InfoKeeper.receiveInfoKeeper( player, target, InfoKeeper.xpBottleGive, amount ) );
                } else {
                    InfoKeeper.sendInfoKeeper( player, InfoKeeper.giveYourselfXp, amount );
                }
            } catch ( NumberFormatException e ) {
                InfoKeeper.sendInfoKeeper( player, InfoKeeper.xpNotANumber, 0 );
            }

            return true;
        }

        int amount;     // Amount to withdraw from player
        int toBottle;   // Amount to put into the bottle

        boolean isAll = args[0].equalsIgnoreCase( "all" ) || InfoKeeper.allAliases.contains( args[0] );
        if ( isAll )
            // If argument is "all" or 1 of its aliases
            amount = Experience.getExp( player );
        else
            // If the first argument isn't "all", "give", "xp", or "reload"
            // then default the first argument to become the amount of XP
            // to put into experience bottle.
            try {
                amount = Integer.parseInt( args[0] );
            } catch ( NumberFormatException e ) {
                InfoKeeper.sendInfoKeeper( player, InfoKeeper.xpNotANumber, 0 );
                return true;
            }

        if ( isOutOfRange( player, amount ) )
            return true;

        if ( InfoKeeper.tax && !checkPerm( player, "bypasstax" ) )
            // If tax is enabled & player doesn't have "expbottle.bypasstax" permission
            toBottle = (int) ( amount - ( amount * InfoKeeper.taxAmount ) );
        else
            toBottle = amount;

        InfoKeeper.sendInfoKeeper( player, InfoKeeper.successfulWithdraw, toBottle );

        MainHandler.givePlayerExpBottle( player, toBottle );
        ExpCalculator.take( player, amount );

        return true;
    }

    /**
     * Show usage to this player. The message is different
     * if that player is an admin (or oped).
     *
     * @param recipient who will get this message
     */
    private void printCommandUsage( @NotNull Player recipient ) {
        String msg = checkPerm( recipient, "admin" ) ? InfoKeeper.cmdUsageAdmin : InfoKeeper.cmdUsageUser;
        InfoKeeper.sendInfoKeeper( recipient, msg, 0 );
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
            InfoKeeper.sendInfoKeeper( player, InfoKeeper.overMaxUnderMin, amount );

        return !betweenRange;
    }

    private boolean checkPerm( @NotNull Player player, @NotNull String permission ) {
        return player.isOp() || player.hasPermission( "expbottle." + permission );
    }
}
