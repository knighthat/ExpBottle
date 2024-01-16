package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllCommand extends ExpCommand {

    public AllCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "all"; }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );
        setWithdrawAmount( giverTotalExp );
        action();
    }
}
