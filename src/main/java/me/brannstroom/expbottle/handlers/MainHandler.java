package me.brannstroom.expbottle.handlers;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainHandler {

    public static boolean hasInventorySpace( Player player ) {
        boolean hasSpace = false;
        for ( int i = 0 ; i < 29 ; i++ ) {
            if ( player.getInventory().firstEmpty() == i ) {
                hasSpace = true;
            }
        }

        return hasSpace;
    }

    public static boolean isExpBottle( ItemStack item ) {
        if ( item.getType() != Material.EXPERIENCE_BOTTLE )
            return false;
        if ( !item.hasItemMeta() )
            return false;
        if ( !item.getItemMeta().hasLore() )
            return false;
        return item.getItemMeta().getLore().get( getXpLoreLine() ).matches( ".*\\d+.*" );
    }

    public static void givePlayerItemStack( Player player, ItemStack item ) {
        if ( hasInventorySpace( player ) ) {
            player.getInventory().addItem( item );
        } else {
            player.getWorld().dropItem( player.getLocation(), item );
        }
    }

    public static void givePlayerExpBottle( Player player, int exp ) {
        ItemStack xpBottle = new ItemStack( Material.EXPERIENCE_BOTTLE, 1 );
        ItemMeta xpBottleMeta = xpBottle.getItemMeta();
        xpBottleMeta.displayName( InfoKeeper.getInfoKeeper( player, InfoKeeper.xpBottleName, exp ) );

        List<Component> lore = new ArrayList<>();
        for ( String string : InfoKeeper.xpBottleLore ) {
            string = ChatColor.translateAlternateColorCodes( '&', string );
            lore.add( InfoKeeper.getInfoKeeper( player, string, exp ) );
        }
        xpBottleMeta.lore( lore );
        xpBottle.setItemMeta( xpBottleMeta );

        MainHandler.givePlayerItemStack( player, xpBottle );
    }

    public static int getXpLoreLine() {
        int line = 0;

        for ( int i = 0 ; i < InfoKeeper.xpBottleLore.size() ; i++ ) {
            if ( InfoKeeper.xpBottleLore.get( i ).contains( "%xp%" ) ) {
                line = i;
            }
        }

        return line;
    }

    public static boolean containsXpLine() {
        for ( String str : InfoKeeper.xpBottleLore ) {
            if ( str.contains( "%xp%" ) )
                return true;
        }
        return false;
    }

    public static int getBottledExperience( ItemStack item ) {
        if ( item.hasItemMeta() ) {
            if ( item.getItemMeta().hasLore() ) {
                List<String> lore = item.getItemMeta().getLore();
                String str = org.bukkit.ChatColor.stripColor( lore.get( MainHandler.getXpLoreLine() ) );

                int xp = Integer.parseInt( str.replaceAll( "\\D+", "" ) );
                return xp;
            }
        }
        return 0;
    }

    public static String getXpLoreString( int line ) {

        String str = "";

        if ( InfoKeeper.xpBottleLore.get( line ) != null )
            str = InfoKeeper.xpBottleLore.get( line );

        return str;
    }
}
