package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class DefaultXpCommand extends ExpCommand {

    @Override
    public @NotNull String getName() { return ""; }

    @Override
    public @NotNull Collection<String> getAliases() { return List.of(); }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        InfoKeeper.sendInfoKeeper( giver, InfoKeeper.successfulWithdraw, toBottleAmount );
    }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );

        try {
            setWithdrawAmount( Integer.parseInt( args[0] ) );
            action();
        } catch ( NumberFormatException e ) {
            InfoKeeper.sendInfoKeeper( player, InfoKeeper.xpNotANumber, 0 );
        }
    }
}
