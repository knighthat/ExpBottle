package me.knighthat.plugin.bottle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

public class ExperienceBottle extends ItemStack {

    @NotNull
    private final Player creator;
    @Range ( from = 0x0, to = Integer.MAX_VALUE )
    private final int totalExp;
    @Range ( from = 0x0, to = Integer.MAX_VALUE )
    private final int exp;

    public ExperienceBottle(
            @NotNull Player creator,
            @Range ( from = 0x0, to = Integer.MAX_VALUE ) int exp,
            @Range ( from = 0x0, to = Integer.MAX_VALUE ) int totalExp
    ) {
        super( Material.EXPERIENCE_BOTTLE );

        this.creator = creator;
        this.exp = exp;
        this.totalExp = totalExp;

        // Inject XP to Persistent Data Container
        editMeta( meta -> ExpData.inject( meta, exp ) );
    }

    private @NotNull Component color( @NotNull String s ) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize( s );
    }

    private @NotNull String replacePlaceholders( @NotNull String s ) {
        String text = s;

        // Replace creator's name and display name
        text = text.replace( "%playername%", creator.getName() );
        text = text.replace( "%playerdisplayname%", creator.getDisplayName() );

        // Replace XP values
        text = text.replace( "%xp%", String.valueOf( exp ) );
        text = text.replace( "%playerxp%", String.valueOf( totalExp ) );
        text = text.replace( "%missingxp%", String.valueOf( exp - totalExp ) );

        return text;
    }

    public void setDisplayName( @NotNull String name ) {
        Component display = color( replacePlaceholders( name ) );
        editMeta( meta -> meta.displayName( display ) );
    }

    @Override
    public void setLore( @Nullable List<String> lore ) {
        int size = lore == null ? 0 : lore.size();
        List<Component> newLore = new ArrayList<>( size );

        if ( lore != null )
            for ( String line : lore ) {
                Component newLine = color( replacePlaceholders( line ) );
                newLore.add( newLine );
            }

        super.lore( newLore );
    }
}
