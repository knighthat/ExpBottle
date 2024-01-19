package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand {

    public ReloadCommand( ExpBottle plugin ) { super( plugin ); }

    @Override
    public @NotNull String getName() { return "reload"; }

    @Override
    public @NotNull String getPermission() { return "admin"; }

    @Override
    public void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        plugin.config.reload();
        plugin.messages.reload();
        plugin.messages.send( sender, MessageFile.RELOAD );
    }
}
