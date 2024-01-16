package me.knighthat.plugin.file;

import me.brannstroom.expbottle.ExpBottle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

public class ConfigFile extends PluginFile {

    public ConfigFile( @NotNull ExpBottle plugin ) { super( plugin, "config" ); }

    private @NotNull Component color( @NotNull String input ) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize( input );
    }

    public boolean isThrowable() { return get().getBoolean( "throwable", true ); }

    public @Range ( from = 0x0, to = Integer.MAX_VALUE ) int getMin() { return get().getInt( "minimum_exp", 0 ); }

    public @Range ( from = 0x0, to = Integer.MAX_VALUE ) int getMax() { return get().getInt( "maximum_exp", Integer.MAX_VALUE ); }

    public boolean isTaxEnabled() { return get().getBoolean( "tax", false ); }

    public double getTaxAmount() { return get().getDouble( "tax_amount", .1d ); }

    public void applyConfiguration( @NotNull ItemMeta meta ) {
        String fromFile = get().getString( "bottle.name", "THERE IS SOMETHING WRONG WITH THIS LINE!" );
        meta.displayName( color( fromFile ) );

        List<Component> lore = new ArrayList<>();
        for ( String line : get().getStringList( "bottle.lore" ) )
            lore.add( color( line ) );
        meta.lore( lore );
    }
}
