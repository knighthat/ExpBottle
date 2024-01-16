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

        /*
            "Due to how Minecraft calculates fractional XP points,
            you may need to add 1 additional XP point to fully reach the desired level.

            For example, 2920 total XP points is not enough to fully reach Level 40 so 2921 points may be required instead."
            - DigMinecraft

            For details, visit: https://www.digminecraft.com/getting_started/experience.php
         */
        return meta.getPersistentDataContainer().getOrDefault( NAMESPACED_KEY, PersistentDataType.INTEGER, -1 ) + 1;
    }
}
