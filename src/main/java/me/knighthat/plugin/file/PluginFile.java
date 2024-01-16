package me.knighthat.plugin.file;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PluginFile {

    @NotNull
    private final JavaPlugin plugin;

    @NotNull
    private final String fileName;
    @Nullable
    private File file;
    @Nullable
    private YamlConfiguration yaml;

    PluginFile( @NotNull JavaPlugin plugin, @NotNull String fileName ) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";

        startup();
        reload();
    }

    private void startup() {
        if ( file == null )
            this.file = new File( plugin.getDataFolder(), fileName );

        if ( !file.exists() )
            plugin.saveResource( fileName, false );
    }

    public void reload() {
        if ( file == null )
            startup();

        this.yaml = YamlConfiguration.loadConfiguration( this.file );
    }

    public @NotNull YamlConfiguration get() {
        if ( file == null || yaml == null )
            reload();

        return yaml;
    }
}
