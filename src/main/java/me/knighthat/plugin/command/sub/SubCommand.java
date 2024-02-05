package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class SubCommand {

    @NotNull
    protected final ExpBottle plugin;

    public SubCommand( @NotNull ExpBottle plugin ) {
        this.plugin = plugin;
    }

    public abstract @NotNull String getName();

    public abstract @NotNull String getPermission();

    public abstract void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args );

    public @NotNull String getUsage() { return plugin.messages.getMessage( "usage." + getName() ); }

    public @NotNull Collection<String> getAliases() {
        String path = "aliases." + getName();
        return plugin.config.get().getStringList( path );
    }

    public void printUsage( @NotNull Audience to, @NotNull String alias ) {
        String message = getUsage().replace( "%cmd%", alias );
        to.sendMessage( Component.text( message ) );
    }

    public boolean hasPermission( @NotNull Permissible permissible, @NotNull String permission ) {
        return permissible.isOp() || permissible.hasPermission( "expbottle." + permission );
    }

    public boolean hasPermission( @NotNull Permissible permissible ) {
        return hasPermission( permissible, getPermission() );
    }
}
