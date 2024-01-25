package me.brannstroom.expbottle.listeners;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.bottle.ExpData;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExpBottleListener implements Listener {

    @NotNull
    private final ExpBottle plugin;

    public ExpBottleListener( @NotNull ExpBottle plugin ) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExpBottleThrown( @NotNull ExpBottleEvent event ) {
        ThrownExpBottle bottleEntity = event.getEntity();
        int exp = ExpData.extract( bottleEntity.getItem().getItemMeta() );
        if ( exp > -1 )
            event.setExperience( exp );
    }

    @EventHandler( priority = EventPriority.HIGHEST )
    public void onPlayerRightClickBottle( @NotNull PlayerInteractEvent event ) {
        // Only continue if throwable is disabled and the item isn't null
        if ( plugin.config.isThrowable() || event.getItem() == null )
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

        /*
            Due to a bug (I think from Minecraft).
            If you try to remove an item from off-hand,
            it'll remove every item in the inventory
            (EXCEPT the one on off-hand) that matches
            the description.

            This is just a temporal solution.
            More robust testing and debugging are needed
             to understand the root of this problem.
         */
        if ( event.getHand() == EquipmentSlot.HAND )
            player.getInventory().remove( item );
        else
            player.getInventory().setItem( EquipmentSlot.OFF_HAND, null );

        int exp = ExpData.extract( item.getItemMeta() );
        if ( exp > -1 )
            ExpCalculator.give( player, exp );
    }
}