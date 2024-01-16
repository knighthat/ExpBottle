package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllCommand extends ExpCommand {

    public AllCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "all"; }

    @Override
    protected void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount ) {
        plugin.messages.send( giver, MessageFile.SUCCESS, giver, receiver, toBottleAmount, (int) ExpCalculator.total( giver ) );
    }

    @Override
    public void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        setGiver( player );
        setReceiver( player );
        setWithdrawAmount( (int) ExpCalculator.total( player ) );
        action();
    }
}
