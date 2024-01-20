package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand extends SubCommand {
    public HelpCommand( @NotNull ExpBottle plugin ) { super( plugin ); }

    private @NotNull Component color( @NotNull String s ) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize( s );
    }

    @Override
    public @NotNull String getName() { return "help"; }

    @Override
    public @NotNull String getPermission() { return ""; }

    @Override
    public void execute( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        TextComponent.Builder message = Component.text();

        /*
            The only command that can be executed by the console is the 'reload' command.
            If sender is not a Player, then print 'reload' usage.
         */

        if ( !( sender instanceof Player ) ) {

            String reloadUsage = plugin.messages.getPrefix() + "/" + alias + " reload";
            message.append( color( reloadUsage ) );

        } else {

            // Decide whether sender is an admin or just a normie
            String path = sender.isOp() || sender.hasPermission( "expbottle.admin" ) ? "admin" : "user";
            path = "help." + path;

            // Build the message line by line. Plus, replacing all %cmd% with the alias
            List<String> lines = plugin.messages.get().getStringList( path );
            for ( int i = 0 ; i < lines.size() ; i++ ) {
                String line = lines.get( i );

                // Replace %cmd% with alias and add prefix to the beginning.
                String completeLine = plugin.messages.getPrefix() + line.replace( "%cmd%", alias );
                message.append( color( completeLine ) );

                // If this is not the last line, start a new line
                if ( i < lines.size() - 1 )
                    message.appendNewline();
            }
        }

        sender.sendMessage( message.asComponent() );
    }
}
