package me.brannstroom.expbottle;

import me.brannstroom.expbottle.listeners.ExpBottleListener;
import me.knighthat.plugin.command.CommandManager;
import me.knighthat.plugin.file.ConfigFile;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExpBottle extends JavaPlugin {

    @NotNull
    public final ConfigFile  config;
    @NotNull
    public final MessageFile messages;

    public ExpBottle() {
        this.config = new ConfigFile( this );
        this.messages = new MessageFile( this );
    }

    private void registerCommand() {
        try {
            // Using reflection to create an instance of PluginCommand
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor( String.class, Plugin.class );
            constructor.setAccessible( true );

            PluginCommand command = constructor.newInstance( "expbottle", this );
            CommandManager cmdManager = new CommandManager( this );

            // Set executor and its aliases from config.yml
            command.setExecutor( cmdManager );
            command.setAliases( config.get().getStringList( "aliases.main" ) );
            command.setTabCompleter( cmdManager );

            getServer().getCommandMap().register( "expbottle", command );
        } catch ( InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e ) {
            //TODO: Implement logging
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        // Register event listener
        getServer().getPluginManager().registerEvents( new ExpBottleListener( this ), this );

        // Register command's executor and aliases
        registerCommand();
    }
}