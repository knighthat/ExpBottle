package me.knighthat.plugin.file;

import me.brannstroom.expbottle.ExpBottle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PluginFile {

    @NotNull
    protected final ExpBottle plugin;

    @NotNull
    private final String fileName;
    @Nullable
    private File file;
    @Nullable
    private YamlConfiguration yaml;

    PluginFile( @NotNull ExpBottle plugin, @NotNull String fileName ) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";

        startup();
        reload();
    }

    private void startup() {
        if ( file == null )
            this.file = new File( plugin.getDataFolder(), fileName );
        createIfNotExist();
    }

    private void createIfNotExist() {
        if ( file == null )
            startup();

        if ( !file.exists() )
            plugin.saveResource( fileName, false );
    }

    public void reload() {
        if ( file == null )
            startup();
        createIfNotExist();

        this.yaml = YamlConfiguration.loadConfiguration( this.file );
    }

    public @NotNull YamlConfiguration get() {
        if ( file == null || yaml == null )
            reload();

        return yaml;
    }
}
