package me.knighthat.plugin.command.sub;

import me.brannstroom.expbottle.handlers.InfoKeeper;
import me.brannstroom.expbottle.handlers.MainHandler;
import me.knighthat.plugin.ExpCalculator;
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

    protected abstract void sendMessage( @NotNull Player giver, @NotNull Player receiver, int withdrawAmount, int toBottleAmount );

    public void setWithdrawAmount( @Range ( from = 0x0, to = Integer.MAX_VALUE ) int amount ) { this.withdrawAmount = amount; }

    protected void setGiver( @NotNull Player giver ) { this.giver = giver; }

    protected void setReceiver( @NotNull Player receiver ) { this.receiver = receiver; }

    protected void action() {
        if ( isOutOfRange( withdrawAmount ) ) {
            InfoKeeper.sendInfoKeeper( giver, InfoKeeper.overMaxUnderMin, withdrawAmount );
            return;
        }

        // If tax is on, deduct tax and set 'toBottleAmount' to the after tax amount,
        // If not, 'toBottleAmount' stays the same as 'withdrawAmount'
        if ( InfoKeeper.tax && !hasPermission( giver, "bypasstax" ) )
            // If tax is enabled & player doesn't have "expbottle.bypasstax" permission
            toBottleAmount = (int) ( withdrawAmount - ( withdrawAmount * InfoKeeper.taxAmount ) );
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
    private boolean isOutOfRange( int amount ) { return amount > InfoKeeper.maxXp && amount < InfoKeeper.minXp; }

    @Override
    public @NotNull String getPermission() { return "user"; }
}