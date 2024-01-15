package me.knighthat.plugin.command;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.knighthat.plugin.command.sub.*;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandManager implements CommandExecutor {

    @NotNull
    private final List<SubCommand> subCommandList = new ArrayList<>( 4 );

    @NotNull
    private final DefaultXpCommand defaultSubCommand = new DefaultXpCommand();

    public CommandManager() {
        subCommandList.add( new ReloadCommand() );
        subCommandList.add( new AllCommand() );
        subCommandList.add( new GiveCommand() );
    }

    private void execute( @NotNull SubCommand subCommand, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( !subCommand.hasPermission( sender ) ) {
            sender.sendMessage( Component.text( InfoKeeper.noPermission ) );
            return;
        }

        subCommand.execute( sender, command, alias, args );
    }

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args ) {
        if ( args.length == 0 ) {
            defaultSubCommand.printUsage( sender );
            return true;
        }

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
                    !command.getAliases().contains( args[0] ) )
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
