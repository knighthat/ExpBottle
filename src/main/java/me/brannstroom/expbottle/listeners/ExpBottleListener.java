package me.brannstroom.expbottle.listeners;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.brannstroom.expbottle.handlers.MainHandler;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.bottle.ExpData;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExpBottleListener implements Listener {

    @Deprecated
    //    @EventHandler
    public void onExpBottle( ExpBottleEvent event ) {
        ThrownExpBottle expBottle = event.getEntity();
        ItemStack item = expBottle.getItem();
        if ( MainHandler.isExpBottle( item ) ) {
            event.setExperience( MainHandler.getBottledExperience( item ) );
        }
    }

    @EventHandler
    public void onExpBottleThrown( @NotNull ExpBottleEvent event ) {
        ThrownExpBottle bottleEntity = event.getEntity();
        int exp = ExpData.extract( bottleEntity.getItem().getItemMeta() );
        if ( exp > -1 )
            event.setExperience( exp );
    }

    @Deprecated
    //    @EventHandler
    public void onPlayerRenameItem( InventoryClickEvent event ) {
        if ( event.getView().getType().equals( InventoryType.ANVIL ) ) {
            if ( event.getRawSlot() == 2 ) {
                if ( event.getInventory().getItem( 0 ) != null ) {
                    if ( event.getInventory().getItem( 0 ).getItemMeta().getDisplayName().equals( InfoKeeper.xpBottleName ) ) {
                        event.setCancelled( true );
                    }
                }
            }
        }
    }

    @Deprecated
    //    @EventHandler ( priority = EventPriority.HIGHEST )
    public void onPlayerInteract( PlayerInteractEvent event ) {
        if ( InfoKeeper.throwable )
            return;
        if ( event.getItem() == null )
            return;

        ItemStack item = event.getItem();
        boolean isExpBottle = MainHandler.isExpBottle( item );

        if ( !isExpBottle )
            return;
        if ( event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK )
            return;
        event.setCancelled( true );

        Player player = event.getPlayer();

        if ( item.getAmount() > 1 ) {
            item.setAmount( item.getAmount() - 1 );
        } else {
            player.getInventory().remove( item );
        }

        int bottledExperience = MainHandler.getBottledExperience( item );
        ExpCalculator.give( player, bottledExperience );
        //        Experience.changeExp(player, bottledExperience-1);
    }

    @EventHandler ( priority = EventPriority.HIGHEST )
    public void onPlayerRightClickBottle( @NotNull PlayerInteractEvent event ) {
        // Only continue if throwable is disabled and the item isn't null
        if ( InfoKeeper.throwable || event.getItem() == null )
            return;

        ItemStack item = event.getItem();
        if ( !ExpData.isPluginBottle( item ) )
            return;

        // Check if it is right click
        if ( event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK )
            return;

        // Cancel event to prevent player from throwing bottle
        event.setCancelled( true );

        Player player = event.getPlayer();

        if ( item.getAmount() > 1 )
            item.setAmount( item.getAmount() - 1 );
        else
            player.getInventory().remove( item );

        int exp = ExpData.extract( item.getItemMeta() );
        if ( exp > -1 )
            ExpCalculator.give( player, exp );
    }

    // TODO: Disable xp bottle from dispenser
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if(InfoKeeper.throwable) return;
        if(event.getItem() != null && MainHandler.isExpBottle(event.getItem())) {
            event.setCancelled(true);
        }
    }
}