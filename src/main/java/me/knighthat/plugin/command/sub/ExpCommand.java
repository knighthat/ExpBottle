package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.bottle.ExperienceBottle;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Map;

public abstract class ExpCommand extends PlayerCommand {

    @NotNull
    protected Player giver;
    @NotNull
    protected Player receiver;
    @Range( from = 0x0, to = Integer.MAX_VALUE )
    protected int    giverTotalExp;
    @Range( from = 0x0, to = Integer.MAX_VALUE )
    private   int    withdrawAmount = -1;
    @Range( from = 0x0, to = Integer.MAX_VALUE )
    private   int    toBottleAmount = -1;

    public ExpCommand( @NotNull ExpBottle plugin ) { super( plugin ); }

    /**
     * Verify if the given amount is within the
     * acceptable range stated in config.yml file.
     *
     * @param amount experience points
     *
     * @return whether the amount is out of range
     */
    private boolean isOutOfRange( int amount ) { return amount > plugin.config.getMax() || amount < plugin.config.getMin(); }

    /**
     * This method does three simple jobs.
     * 1. Initialize Experience Bottle instance
     * 2. Set its properties (name & lore)
     * 3. Give it to provided player
     * <p>
     * NOTE: If the receiver does not have at lease 1 empty
     * space in his/her inventory, the bottle will be
     * thrown to the ground in front of said player.
     *
     * @param to  who will get this bottle
     * @param exp how many xp will this bottle contain
     *
     * @see ExperienceBottle
     */
    private void giveBottle( @NotNull Player to, int exp ) {
        ExperienceBottle bottle = new ExperienceBottle( giver, exp, giverTotalExp );
        bottle.setDisplayName( plugin.config.getBottleName() );
        bottle.setLore( plugin.config.getBottleLore() );

        /*
         * Inventory#addItem() will attempt to add provided item(s) to provided player.
         * If no match is found in the player's inventory, it'll put that item to a map,
         * and return it after going through all item(s).
         *
         * However, this method excludes the offhand slot, meaning; even the player
         * is holding the exact bottle; it won't add this bottle to that slot
         */
        Map<Integer, ItemStack> leftOvers = to.getInventory().addItem( bottle );
        /*
         * This piece ensures that for each "left-over" item
         * gets checked against offhand slot.
         * If it's not similar, we drop the item on the ground
         */
        ItemStack offHand = to.getInventory().getItemInOffHand();
        for (ItemStack item : leftOvers.values())
            /*
             * Do NOT use ItemStack#equals() because the item from the server
             * is NMS CraftItem, which is completely different from ItemStack.
             * ItemStack#isSimilar() compares its properties such as material,
             * amount, ItemMeta, etc.
             */
            if ( item.isSimilar( to.getInventory().getItemInOffHand() ) )
                offHand.setAmount( offHand.getAmount() + item.getAmount() );
            else
                to.getWorld().dropItem( to.getLocation(), item );
    }

    protected @NotNull String giverMessagePath() { return MessageFile.SUCCESS; }

    protected @Nullable String receiverMessagePath() { return null; }

    protected void setGiver( @NotNull Player giver ) {
        this.giver = giver;
        this.giverTotalExp = ExpCalculator.total( giver );
    }

    protected void setReceiver( @NotNull Player receiver ) { this.receiver = receiver; }

    protected void action() {
        if ( isOutOfRange( withdrawAmount ) ) {
            plugin.messages.send( giver, MessageFile.OUT_OF_RANGE );
            return;
        }

        // If tax is on, deduct tax and set 'toBottleAmount' to the after tax amount,
        // If not, 'toBottleAmount' stays the same as 'withdrawAmount'
        if ( plugin.config.isTaxEnabled() && !hasPermission( giver, "bypasstax" ) )
            // If tax is enabled & player doesn't have "expbottle.bypasstax" permission
            toBottleAmount = (int) (withdrawAmount - (withdrawAmount * plugin.config.getTaxAmount()));
        else
            toBottleAmount = withdrawAmount;

        // If exp is insufficient, send a message
        if ( withdrawAmount > giverTotalExp ) {
            plugin.messages.send( giver, MessageFile.INSUFFICIENT, giver, receiver, withdrawAmount, toBottleAmount );
            return;
        }

        giveBottle( receiver, toBottleAmount );
        ExpCalculator.take( giver, withdrawAmount );
        receiver.updateInventory();

        plugin.messages.send( giver, giverMessagePath(), giver, receiver, withdrawAmount, toBottleAmount );
        if ( receiverMessagePath() != null )
            plugin.messages.send( receiver, receiverMessagePath(), giver, receiver, toBottleAmount, toBottleAmount );
    }

    public void setWithdrawAmount( @Range( from = 0x0, to = Integer.MAX_VALUE ) int amount ) { this.withdrawAmount = amount; }

    @Override
    public @NotNull String getPermission() { return "user"; }
}