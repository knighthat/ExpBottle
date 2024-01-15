package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GiveCommand extends ExpCommand {

    @Override
    public @NotNull String getName() { return "give"; }

    @Override
    public @NotNull Collection<String> getAliases() { return InfoKeeper.giveAliases; }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        if ( giver != receiver ) {
            receiver.sendMessage( InfoKeeper.receiveInfoKeeper( giver, receiver, InfoKeeper.xpBottleReceive, toBottleAmount ) );
            giver.sendMessage( InfoKeeper.receiveInfoKeeper( giver, receiver, InfoKeeper.xpBottleGive, toBottleAmount ) );
        } else
            InfoKeeper.sendInfoKeeper( receiver, InfoKeeper.giveYourselfXp, toBottleAmount );
    }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );

        // "give" arg requires 2 additional arguments: player and amount
        if ( args.length < 3 ) {
            printUsage( player );
            return;
        }

        // Convert the third argument to amount of XP
        // and give you player indicated in the second argument
        try {
            setWithdrawAmount( Integer.parseInt( args[2] ) );

            // Verify if indicated player is online and
            // check for empty slot in said player's inventory.
            Player target = Bukkit.getPlayer( args[1] );
            if ( target == null ) {
                InfoKeeper.sendInfoKeeper( player, InfoKeeper.playerNotOnline, 0 );
            } else
                setReceiver( target );

            action();
        } catch ( NumberFormatException e ) {
            InfoKeeper.sendInfoKeeper( player, InfoKeeper.xpNotANumber, 0 );
        }
    }
}
