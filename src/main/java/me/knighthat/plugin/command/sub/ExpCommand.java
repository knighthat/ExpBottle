package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import me.knighthat.plugin.file.MessageFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public abstract class ExpCommand extends PlayerCommand {

    @Range ( from = 0x0, to = Integer.MAX_VALUE )
    private int withdrawAmount = -1;
    @Range ( from = 0x0, to = Integer.MAX_VALUE )
    private int toBottleAmount = -1;
    @NotNull
    private Player giver;
    @NotNull
    private Player receiver;

    public ExpCommand( @NotNull ExpBottle plugin ) { super( plugin ); }

    protected abstract void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount );

    public void setWithdrawAmount( @Range ( from = 0x0, to = Integer.MAX_VALUE ) int amount ) { this.withdrawAmount = amount; }

    protected void setGiver( @NotNull Player giver ) { this.giver = giver; }

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
            toBottleAmount = (int) ( withdrawAmount - ( withdrawAmount * plugin.config.getTaxAmount() ) );
        else
            toBottleAmount = withdrawAmount;

        MainHandler.giveBottle( receiver, toBottleAmount );
        ExpCalculator.take( giver, withdrawAmount );

        sendMessage( giver, receiver, withdrawAmount, toBottleAmount );
    }

    /**
     * Verify if the given amount is within the
     * acceptable range stated in config.yml file.
     *
     * @param amount experience points
     * @return whether the amount is out of range
     */
    private boolean isOutOfRange( int amount ) { return amount > plugin.config.getMax() && amount < plugin.config.getMin(); }

    @Override
    public @NotNull String getPermission() { return "user"; }
}