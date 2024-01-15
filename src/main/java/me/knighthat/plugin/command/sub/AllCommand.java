package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.knighthat.plugin.ExpCalculator;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AllCommand extends ExpCommand {

    @Override
    public @NotNull String getName() { return "all"; }

    @Override
    public @NotNull Collection<String> getAliases() { return InfoKeeper.allAliases; }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        InfoKeeper.sendInfoKeeper( giver, InfoKeeper.successfulWithdraw, toBottleAmount );
    }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );
        setWithdrawAmount( (int) ExpCalculator.total( player ) );
        action();
    }
}
