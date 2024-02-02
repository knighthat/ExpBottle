package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.bottle.ExperienceBottle;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

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

        int firstEmpty = to.getInventory().firstEmpty();
        // -1 means no empty slot, any other positive number is an empty slot
        if ( firstEmpty > -1 )
            to.getInventory().setItem( firstEmpty, bottle );
        else
            to.getWorld().dropItemNaturally( to.getLocation(), bottle );
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

        plugin.messages.send( giver, giverMessagePath(), giver, receiver, withdrawAmount, toBottleAmount );
        if ( receiverMessagePath() != null )
            plugin.messages.send( receiver, receiverMessagePath(), giver, receiver, toBottleAmount, toBottleAmount );
    }

    public void setWithdrawAmount( @Range( from = 0x0, to = Integer.MAX_VALUE ) int amount ) { this.withdrawAmount = amount; }

    @Override
    public @NotNull String getPermission() { return "user"; }
}