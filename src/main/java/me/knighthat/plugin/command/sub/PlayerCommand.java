package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommand extends SubCommand {

    public PlayerCommand( ExpBottle plugin ) { super( plugin ); }

    public abstract void execute( @NotNull Player player, @NotNull Command command, @NotNull String alias, @NotNull String[] args );

    @Override
    public void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( !( sender instanceof Player ) )
            plugin.messages.send( sender, MessageFile.PLAYER_ONLY );
        else
            execute( (Player) sender, command, alias, args );
    }
}
