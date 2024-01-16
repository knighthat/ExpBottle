package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ReloadCommand extends SubCommand {

    public ReloadCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "reload"; }

    @Override
    public @NotNull Collection<String> getAliases() { return InfoKeeper.reloadAliases; }

    @Override
    public @NotNull String getPermission() { return "admin"; }

    @Override
    public boolean isPlayerCommand() { return false; }

    @Override
    public void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        InfoKeeper.updateConfig();
        plugin.messages.reload();
        plugin.messages.send( sender, MessageFile.RELOAD );
    }
}
