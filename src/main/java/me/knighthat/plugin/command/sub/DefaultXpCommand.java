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
    public @NotNull String getName() { return ""; }

    @Override
    public @NotNull Collection<String> getAliases() { return List.of(); }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        plugin.messages.send( giver, MessageFile.SUCCESS, giver, receiver, toBottleAmount, (int) ExpCalculator.total( giver ) );
    }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );

        try {
            setWithdrawAmount( Integer.parseInt( args[0] ) );
            action();
        } catch ( NumberFormatException e ) {
            plugin.messages.send( player, MessageFile.NOT_A_NUMBER );
        }
    }
}
