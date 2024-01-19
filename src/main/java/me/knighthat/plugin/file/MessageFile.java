package me.knighthat.plugin.file;

import me.brannstroom.expbottle.ExpBottle;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageFile extends PluginFile {

    @NotNull
    public static final String NO_PERMISSION = "no_permission";
    @NotNull
    public static final String INSUFFICIENT = "xp_insufficient";
    @NotNull
    public static final String NOT_A_NUMBER = "not_a_number";
    @NotNull
    public static final String OUT_OF_RANGE = "out_of_range";
    @NotNull
    public static final String SUCCESS = "success";
    @NotNull
    public static final String RELOAD = "reload";
    @NotNull
    public static final String GIVE = "give";
    @NotNull
    public static final String RECEIVE = "receive";
    @NotNull
    public static final String SELF = "self_give";
    @NotNull
    public static final String PLAYER_NOT_FOUND = "player_not_found";
    @NotNull
    public static final String PLAYER_ONLY = "player_only";

    public MessageFile( @NotNull ExpBottle plugin ) { super( plugin, "messages" ); }

    @SuppressWarnings ( { "deprecation" } )
    private @NotNull String replacePlayerPlaceHolders( @NotNull String message, @Nullable Player giver, @Nullable Player receiver ) {
        String msg = message;

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

    private @NotNull String replaceXpPlaceHolders( @NotNull String message, int amount, int totalExp ) {
        String msg = message;

        String minXp = String.valueOf( plugin.config.getMin() );
        msg = msg.replace( "%minxp%", minXp );

        String maxXp = String.valueOf( plugin.config.getMax() );
        msg = msg.replace( "%maxxp%", maxXp );

        if ( amount >= 0 )
            msg = msg.replace( "%xp%", String.valueOf( amount ) );
        if ( totalExp >= 0 )
            msg = msg.replace( "%playerxp%", String.valueOf( totalExp ) );
        if ( amount >= 0 && totalExp >= 0 )
            msg = msg.replace( "%missingxp%", String.valueOf( amount - totalExp ) );

        return msg;
    }

    public @NotNull String getPrefix() {
        return get().getString( "prefix", "[&6Exp Bottle%r] " );
    }

    @SuppressWarnings ( "deprecation" )
    public @NotNull String getMessage( @NotNull String path ) {
        String message = getPrefix() + get().getString( path, "" );
        return ChatColor.translateAlternateColorCodes( '&', message );
    }

    public void send( @NotNull Audience to, @NotNull String path, @Nullable Player giver, @Nullable Player taker, int amount, int totalExp ) {
        String message = getMessage( path );
        message = replacePlayerPlaceHolders( message, giver, taker );
        message = replaceXpPlaceHolders( message, amount, totalExp );

        to.sendMessage( Component.text( message ) );
    }

    public void send( @NotNull Audience to, @NotNull String path ) {
        send( to, path, null, null, -1, -1 );
    }
}
