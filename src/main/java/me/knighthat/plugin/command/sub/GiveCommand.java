package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GiveCommand extends ExpCommand {

    public GiveCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "give"; }

    @Override
    protected @NotNull String giverMessagePath() { return giver != receiver ? MessageFile.GIVE : MessageFile.SELF; }

    @Override
    protected @Nullable String receiverMessagePath() { return giver != receiver ? MessageFile.RECEIVE : null; }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );

        // "give" arg requires 2 additional arguments: player and amount
        if ( args.length < 3 ) {
            printUsage( player, alias );
            return;
        }

        /*
         * Convert the third argument to amount of XP
         * and give you player indicated in the second argument
         */
        int withdrawAmount;
        try {
            /*
             * If the number ends with the 'l' letter.
             * We convert the number before that from
             * level to EXP and add to withdrawAmount.
             */
            if ( args[2].toLowerCase().endsWith( "l" ) ) {
                String noL = args[2].substring( 0, args[2].length() - 1 );
                int level = Integer.parseInt( noL );
                withdrawAmount = ExpCalculator.at( level );
            } else
                withdrawAmount = Integer.parseInt( args[2] );
        } catch ( NumberFormatException e ) {
            plugin.messages.send( player, MessageFile.NOT_A_NUMBER );
            printUsage( player, alias );
            return;
        }
        setWithdrawAmount( withdrawAmount );

        Player target = Bukkit.getPlayer( args[1] );
        if ( target == null ) {
            plugin.messages.send( player, MessageFile.PLAYER_NOT_FOUND );
            return;
        }
        setReceiver( target );

        action();
    }
}
