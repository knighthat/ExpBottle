package me.knighthat.plugin.command.sub;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommand extends SubCommand {

    public abstract void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args );

    @Override
    public boolean isPlayerCommand() { return true; }

    @Override
    public void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( !( sender instanceof Player ) ) {
            String msg = "Only player can execute this command!";
            TextColor color = TextColor.fromHexString( "#cc0000" );

            Component comp = Component.text( msg ).color( color );
            sender.sendMessage( comp );
        } else
            execute( (Player) sender, command, alias, args );
    }
}
