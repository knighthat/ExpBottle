package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCommand extends ExpCommand {

    public GiveCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "give"; }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        int totalExp = (int) ExpCalculator.total( giver );
        if ( giver != receiver ) {
            plugin.messages.send( giver, MessageFile.GIVE, giver, receiver, toBottleAmount, totalExp );
            plugin.messages.send( receiver, MessageFile.RECEIVE, giver, receiver, toBottleAmount, totalExp );
        } else
            plugin.messages.send( receiver, MessageFile.SELF, giver, receiver, toBottleAmount, totalExp );
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
                plugin.messages.send( player, MessageFile.PLAYER_NOT_FOUND );
            } else
                setReceiver( target );

            action();
        } catch ( NumberFormatException e ) {
            plugin.messages.send( player, MessageFile.NOT_A_NUMBER );
        }
    }
}
