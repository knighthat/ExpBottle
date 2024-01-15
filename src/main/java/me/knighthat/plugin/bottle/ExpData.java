package me.knighthat.plugin.bottle;

import me.brannstroom.expbottle.ExpBottle;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ExpData {

    private static final @NotNull NamespacedKey NAMESPACED_KEY = new NamespacedKey( JavaPlugin.getPlugin( ExpBottle.class ), "expbottle" );

    public static boolean isPluginBottle( @NotNull ItemMeta meta ) {
        return meta.getPersistentDataContainer().has( NAMESPACED_KEY, PersistentDataType.INTEGER );
    }

    public static boolean isPluginBottle( @NotNull ItemStack item ) {

        if ( item.getType() != Material.EXPERIENCE_BOTTLE ||    // Is EXPERIENCE_BOTTLE
                !item.hasItemMeta() )                            // Has meta
            return false;

        return isPluginBottle( item.getItemMeta() );
    }

    public static void inject( @NotNull ItemMeta meta, int amount ) {
        meta.getPersistentDataContainer().set( NAMESPACED_KEY, PersistentDataType.INTEGER, amount );
    }

    public static int extract( @NotNull ItemMeta meta ) {
        if ( !isPluginBottle( meta ) )
            return -1;

        return meta.getPersistentDataContainer().getOrDefault( NAMESPACED_KEY, PersistentDataType.INTEGER, -1 );
    }
}
