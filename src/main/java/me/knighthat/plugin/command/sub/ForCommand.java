package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForCommand extends PlayerCommand {

    public ForCommand( @NotNull ExpBottle plugin ) { super( plugin ); }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( args.length < 2 ) {
            printUsage( player, alias );
            return;
        }

        int withdrawAmount, toBottleAmount;
        try {
            int level = Integer.parseInt( args[1] );
            withdrawAmount = ExpCalculator.at( level );
        } catch ( NumberFormatException e ) {
            plugin.messages.send( player, MessageFile.NOT_A_NUMBER );
            printUsage( player, alias );
            return;
        }

        /*
         * If tax is on, deduct tax and set 'toBottleAmount' to the after tax amount,
         * If not, 'toBottleAmount' stays the same as 'withdrawAmount'
         */
        if ( plugin.config.isTaxEnabled() && !hasPermission( player, "bypasstax" ) )
            // If tax is enabled & player doesn't have "expbottle.bypasstax" permission
            toBottleAmount = (int) (withdrawAmount - (withdrawAmount * plugin.config.getTaxAmount()));
        else
            toBottleAmount = withdrawAmount;

        plugin.messages.send( player, MessageFile.FOR_COMMAND, player, null, withdrawAmount, toBottleAmount );
    }

    @Override
    public @NotNull String getName() { return "for"; }

    @Override
    public @NotNull String getPermission() { return "for"; }
}
