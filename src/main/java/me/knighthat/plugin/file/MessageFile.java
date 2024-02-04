package me.knighthat.plugin.file;

import me.brannstroom.expbottle.ExpBottle;
import me.knighthat.plugin.ExpCalculator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class MessageFile extends PluginFile {

    @NotNull
    public static final String NO_PERMISSION    = "no_permission";
    @NotNull
    public static final String INSUFFICIENT     = "xp_insufficient";
    @NotNull
    public static final String NOT_A_NUMBER     = "not_a_number";
    @NotNull
    public static final String OUT_OF_RANGE     = "out_of_range";
    @NotNull
    public static final String SUCCESS          = "success";
    @NotNull
    public static final String RELOAD           = "reload";
    @NotNull
    public static final String GIVE             = "give";
    @NotNull
    public static final String RECEIVE          = "receive";
    @NotNull
    public static final String SELF             = "self_give";
    @NotNull
    public static final String PLAYER_NOT_FOUND = "player_not_found";
    @NotNull
    public static final String PLAYER_ONLY      = "player_only";
    @NotNull
    public static final String BALANCE_SELF     = "balance_self";
    @NotNull
    public static final String BALANCE_OTHER    = "balance_other";
    @NotNull
    public static final String FOR_COMMAND      = "for_command";

    public MessageFile( @NotNull ExpBottle plugin ) { super( plugin, "messages" ); }

    @SuppressWarnings( { "deprecation" } )
    private @NotNull String replacePlayerPlaceHolders( @NotNull String message, @Nullable Player giver, @Nullable Player receiver ) {
        String msg = message.trim();

        if ( giver != null ) {
            msg = msg.replace( "%playername%", giver.getName() );
            msg = msg.replace( "%playerdisplayname%", giver.getDisplayName() );
        }

        if ( receiver != null ) {
            msg = msg.replace( "%receivername%", receiver.getName() );
            msg = msg.replace( "%receiverdisplayname%", receiver.getDisplayName() );
        }

        return msg;
    }

    private @NotNull String replaceXpPlaceHolders(
            @NotNull String message,
            @Range( from = -1, to = Integer.MAX_VALUE ) int withdraw,
            @Range( from = -1, to = Integer.MAX_VALUE ) int afterTax,
            @Range( from = -1, to = Integer.MAX_VALUE ) int totalExp ) {
        String msg = message.trim();

        // Replace the lowest XP can be withdrawn
        String minXp = String.valueOf( plugin.config.getMin() );
        msg = msg.replace( "%minxp%", minXp );

        // Replace the highest XP can be withdrawn
        String maxXp = String.valueOf( plugin.config.getMax() );
        msg = msg.replace( "%maxxp%", maxXp );

        // Replace player's XP
        if ( totalExp >= 0 )
            msg = msg.replace( "%playerxp%", String.valueOf( totalExp ) );

        // Replace the amount of XP will be withdrawn from player
        if ( withdraw >= 0 )
            msg = msg.replace( "%xp%", String.valueOf( withdraw ) );

        // Replace the amount XP will be put inside the bottle
        if ( afterTax >= 0 )
            msg = msg.replace( "%aftertax%", String.valueOf( afterTax ) );

        return msg;
    }

    private @NotNull String replaceTaxPlaceHolders( @NotNull String message ) {
        int tax = (int) plugin.config.getTaxAmount();
        return message.trim().replace( "%tax%", String.valueOf( tax ) );
    }

    public @NotNull String getPrefix() {
        return get().getString( "prefix", "[&6Exp Bottle%r] " );
    }

    @SuppressWarnings( "deprecation" )
    public @NotNull String getMessage( @NotNull String path ) {
        String message = getPrefix() + get().getString( path, "" );
        return ChatColor.translateAlternateColorCodes( '&', message );
    }

    /**
     * This method replaces all placeholders (if applicable)
     * then converts it into {@link Component} before sending
     * the message to {@link Audience}.
     *
     * @param to       who will receive this message
     * @param path     where in the message.yml file
     * @param giver    command executor ('null' to skip)
     * @param receiver who will get the bottle ('null' to skip)
     * @param withdraw XP to withdraw from 'giver' (-1 to skip)
     * @param afterTax XP to put inside the bottle (-1 to skip)
     */
    public void send(
            @NotNull Audience to,
            @NotNull String path,
            @Nullable Player giver,
            @Nullable Player receiver,
            @Range( from = -1, to = Integer.MAX_VALUE ) int withdraw,
            @Range( from = -1, to = Integer.MAX_VALUE ) int afterTax ) {
        int totalExp = giver == null ? -1 : ExpCalculator.total( giver );

        String message = getMessage( path );
        message = replaceXpPlaceHolders( message, withdraw, afterTax, totalExp );
        message = replacePlayerPlaceHolders( message, giver, receiver );
        message = replaceTaxPlaceHolders( message );

        to.sendMessage( Component.text( message ) );
    }

    public void send( @NotNull Audience to, @NotNull String path ) {
        send( to, path, null, null, -1, -1 );
    }
}
