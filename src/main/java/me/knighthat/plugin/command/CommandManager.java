package me.knighthat.plugin.command;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.command.sub.*;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandManager implements TabExecutor {

    @NotNull
    private static final List<String> SUGGESTED_EXP;

    static {
        SUGGESTED_EXP = List.of(
                "1",
                "1L",
                "2",
                "2L",
                "3",
                "3L",
                "5",
                "5L"
        );
    }

    @NotNull
    private final ExpBottle plugin;

    @NotNull
    private final List<SubCommand> subCommandList = new ArrayList<>( 4 );

    @NotNull
    private final DefaultXpCommand defaultSubCommand;

    public CommandManager( @NotNull ExpBottle plugin ) {
        this.plugin = plugin;

        subCommandList.add( new ReloadCommand( plugin ) );
        subCommandList.add( new AllCommand( plugin ) );
        subCommandList.add( new GiveCommand( plugin ) );
        subCommandList.add( new HelpCommand( plugin ) );
        subCommandList.add( new BalanceCommand( plugin ) );
        subCommandList.add( new ForCommand( plugin ) );
        defaultSubCommand = new DefaultXpCommand( plugin );
    }

    private void execute( @NotNull SubCommand subCommand, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( !subCommand.hasPermission( sender ) ) {
            plugin.messages.send( sender, MessageFile.NO_PERMISSION );
            return;
        }

        subCommand.execute( sender, command, alias, args );
    }

    private @Nullable String getArgumentUsage( SubCommand command, int pos ) {
        /*
         * This is not a perfect way to get all the arguments of a command.
         * However, this is what I can come up with.
         *
         * In short, we get usage from messages.yml file,
         * split them at '%cmd%' and keep the latter part,
         * then we split it again by the space character (' '),
         * finally, take the argument at the current position
         * (args.length) and add it to 'results'.
         *
         * NOTE: This algorithm has the potential to throw IndexOutOfBoundsException.
         * Some commands require 2, 3, 4 arguments while some don't require any
         * additional arguments (just the subcommand name).
         *
         * TODO: Implement this on each subcommand
         */
        try {
            String[] splitUsage = command.getUsage().split( "%cmd%" );
            return splitUsage[1].split( " " )[pos];
        } catch ( Exception e ) {
            return null;
        }
    }

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        // If no argument provided, default first arg to 'help' to print help;
        if ( args.length == 0 )
            args = new String[]{ "help" };

        /*
         * Due to the flexibility of the first argument (can be either a sub-command or a number).
         * By adding this boolean, we can verify the command is executed by the actual sub-command
         * and skip the number first argument option.
         */
        boolean isExecuted = false;

        /*
         * The "give", "all", and default sub commands make changes to the properties
         * such as 'giver', 'receiver', 'withdrawAmount', and 'toBottleAmount'.
         * But those changes are not supposed to be permanent.
         * Therefore, using iterator is the better option since it only makes changes
         * temporarily and won't reflect changes to the original object.
         */
        Iterator<SubCommand> subCommands = subCommandList.iterator();
        while (subCommands.hasNext()) {
            SubCommand sub = subCommands.next();

            /*
             * If the first arg (sub command) doesn't match with command's name,
             * then check if it falls into one of the aliases.
             * If both are failed, then move to the next sub-command.
             */
            if ( !sub.getName().equalsIgnoreCase( args[0] ) &&
                 !sub.getAliases().contains( args[0] ) )
                continue;

            execute( sub, sender, command, alias, args );

            isExecuted = true;
            break;
        }

        if ( !isExecuted )
            execute( defaultSubCommand, sender, command, alias, args );

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        List<String> results = new ArrayList<>();
        /*
         * This check is optional and is here to
         * prevent the plugin from throwing exceptions.
         */
        if ( args.length < 1 )
            return results;

        for (SubCommand subCommand : subCommandList) {
            if ( subCommand instanceof PlayerCommand && !(sender instanceof Player) )
                continue;
            if ( !subCommand.hasPermission( sender ) )
                continue;

            // The first argument is the name of the subcommand, always.
            if ( args.length != 1 ) {

                // Filter out all other subcommands arguments
                if ( !args[0].equalsIgnoreCase( subCommand.getName() ) )
                    continue;

                String argument = getArgumentUsage( subCommand, args.length );
                if ( argument != null )
                    results.add( argument );
            } else {
                results.add( subCommand.getName() );

                String argument = getArgumentUsage( defaultSubCommand, 1 );
                if ( argument != null )
                    results.add( argument );
                /*
                 * The default command "/expbottle [amount]L"
                 * This will add some examples of how the
                 * first argument works.
                 * Only when 'xp_suggestion' in config.yml is set to true
                 */
                if ( plugin.config.get().getBoolean( "xp_suggestion" ) )
                    results.addAll( SUGGESTED_EXP );
            }

        }
        return results;
    }
}
