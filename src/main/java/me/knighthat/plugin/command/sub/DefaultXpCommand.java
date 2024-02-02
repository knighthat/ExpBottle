package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class DefaultXpCommand extends ExpCommand {

    public DefaultXpCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "main"; }

    @Override
    public @NotNull Collection<String> getAliases() { return List.of(); }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );

        int withdrawAmount;
        try {
            /*
             * If the number ends with the 'l' letter.
             * We convert the number before that from
             * level to EXP and add to withdrawAmount.
             */
            if ( args[0].toLowerCase().endsWith( "l" ) ) {
                String noL = args[0].substring( 0, args[0].length() - 1 );
                int level = Integer.parseInt( noL );
                withdrawAmount = ExpCalculator.at( level );
            } else
                withdrawAmount = Integer.parseInt( args[0] );
        } catch ( NumberFormatException e ) {
            plugin.messages.send( player, MessageFile.NOT_A_NUMBER );
            printUsage( player, alias );
            return;
        }

        setWithdrawAmount( withdrawAmount );
        action();
    }
}
