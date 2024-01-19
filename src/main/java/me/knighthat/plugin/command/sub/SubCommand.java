package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.brannstroom.expbottle.handlers.InfoKeeper;
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

    public @NotNull Collection<String> getAliases() {
        String path = "aliases." + getName();
        return plugin.config.get().getStringList( path );
    }

    public abstract void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args );

    public void printUsage( @NotNull Audience to ) {
        String msg = InfoKeeper.cmdUsageUser;
        if ( to instanceof Permissible && hasPermission( (Permissible) to ) )
            msg = InfoKeeper.cmdUsageAdmin;

        to.sendMessage( Component.text( msg ) );
    }

    public boolean hasPermission( @NotNull Permissible permissible, @NotNull String permission ) {
        return permissible.isOp() || permissible.hasPermission( "expbottle." + permission );
    }

    public boolean hasPermission( @NotNull Permissible permissible ) {
        return hasPermission( permissible, getPermission() );
    }
}
