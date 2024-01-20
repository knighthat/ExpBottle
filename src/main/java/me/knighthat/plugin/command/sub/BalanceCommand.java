package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand extends PlayerCommand {

    public BalanceCommand( @NotNull ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "balance"; }

    @Override
    public @NotNull String getPermission() { return "balance"; }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        Player target = player;

        if ( args.length >= 2 ) {
            target = Bukkit.getPlayer( args[1] );
            if ( target == null ) {
                plugin.messages.send( player, MessageFile.PLAYER_NOT_FOUND );
                return;
            }
        }
        int xp = ExpCalculator.total( target );

        String msgPath = player != target ? MessageFile.BALANCE_OTHER : MessageFile.BALANCE_SELF;
        plugin.messages.send( player, msgPath, target, null, -1, xp );
    }
}
