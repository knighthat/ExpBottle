package me.knighthat.plugin.file;

import me.brannstroom.expbottle.ExpBottle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public class ConfigFile extends PluginFile {

    public ConfigFile( @NotNull ExpBottle plugin ) { super( plugin, "config" ); }

    public boolean isThrowable() { return get().getBoolean( "throwable", true ); }

    public @Range ( from = 0x0, to = Integer.MAX_VALUE ) int getMin() { return get().getInt( "minimum_exp", 0 ); }

    public @Range ( from = 0x0, to = Integer.MAX_VALUE ) int getMax() { return get().getInt( "maximum_exp", Integer.MAX_VALUE ); }

    public boolean isTaxEnabled() { return get().getBoolean( "tax", false ); }

    public double getTaxAmount() { return get().getDouble( "tax_amount", .1d ); }

    public @NotNull String getBottleName() {
        return get().getString( "bottle.name", "THERE IS SOMETHING WRONG WITH THIS LINE!" );
    }

    public @NotNull List<String> getBottleLore() {
        return get().getStringList( "bottle.lore" );
    }
}
