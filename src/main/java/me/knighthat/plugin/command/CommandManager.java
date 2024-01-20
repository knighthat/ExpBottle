package me.knighthat.plugin.command;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.command.sub.*;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandManager implements CommandExecutor {

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
        defaultSubCommand = new DefaultXpCommand( plugin );
    }

    private void execute( @NotNull SubCommand subCommand, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( !subCommand.hasPermission( sender ) ) {
            plugin.messages.send( sender, MessageFile.NO_PERMISSION );
            return;
        }

        subCommand.execute( sender, command, alias, args );
    }

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        // If no argument provided, default first arg to 'help' to print help;
        if ( args.length == 0 )
            args = new String[]{ "help" };

        /*
            Due to the flexibility of the first argument (can either be a sub-command or a number).
            By adding this boolean, we can verify the command is executed by the actual sub-command
            and skip the number first argument option.
         */
        boolean isExecuted = false;

        /*
          The "give", "all", and default sub commands make changes to the properties
          such as 'giver', 'receiver', 'withdrawAmount', and 'toBottleAmount'.
          But those changes are not supposed to be permanent.
          Therefore, using iterator is the better option since it only makes changes
          temporarily and won't reflect changes to the original object.
         */
        Iterator<SubCommand> subCommands = subCommandList.iterator();
        while ( subCommands.hasNext() ) {
            SubCommand sub = subCommands.next();

            // If the first arg (sub command) doesn't match with command's name,
            // then check if it falls into one of the aliases.
            // If both are failed, then move to the next sub-command.
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
}
