package me.knighthat.plugin;

import me.brannstroom.expbottle.model.Experience;
import me.knighthat.plugin.logging.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

public class ExpCalculator {

    /**
     * Return the amount of XP required to go to the next level
     * <p>
     * NOTE: The amount is the start of provided level to the start of the next level.
     * For example, level 0 to 1 requires 7 points, this method will return 7 if input
     * level is 0.
     * <p>
     * For in-depth formula and charts, visit <a href="https://minecraft.fandom.com/wiki/Experience">WIKI - Experience</a>
     *
     * @param level current level
     * @return required amount of xp to go to the next level
     */
    public static int nextReq( int level ) {
        int diff1 = 2, diff2 = 7;
        if ( level >= 31 ) {
            diff1 = 9;
            diff2 = -158;
        } else if ( level >= 17 ) {
            diff1 = 5;
            diff2 = -38;
        }
        return ( diff1 * level ) + diff2;
    }

    /**
     * Return XP points needed to get to this level from level 0.
     * <p>
     * For in-depth formula and charts, visit <a href="https://minecraft.fandom.com/wiki/Experience">WIKI - Experience</a>
     *
     * @param level current level
     * @return total amount to get to this level from level 0
     */
    public static int at( int level ) {
        float levelSqr = (float) Math.pow( level, 2 ), diff1 = 1f, diff2 = 6f;
        int diff3 = 0;

        if ( level > 31 ) {
            diff1 = 4.5f;
            diff2 = -162.5f;
            diff3 = 2220;
        } else if ( level > 16 ) {
            diff1 = 2.5f;
            diff2 = -40.5f;
            diff3 = 360;
        }

        float result =( diff1 * levelSqr ) + ( diff2 * level ) + diff3;
        return (int) result;
    }

    /**
     * The total amount of XP points a player currently has.
     * <p>
     * For in-depth formula and charts, visit <a href="https://minecraft.fandom.com/wiki/Experience">WIKI - Experience</a>
     *
     * @param player who to check
     * @return the amount of XP points this player has
     */
    public static float total( @NotNull Player player ) {
        int level = player.getLevel();
        return ( at( level ) + ( nextReq( level ) * player.getExp() ) );
    }

    /**
     * Convert total XP to level
     * <p>
     * For in-depth formula and charts, visit <a href="https://minecraft.fandom.com/wiki/Experience">WIKI - Experience</a>
     *
     * @param total number of XP to convert to level
     * @return level at this xp
     */
    public static double levelOf(long total) {

        double diff1 = 1d, diff2 = 9d,diff3 = 0d, diff4 = 1d, diff5 = -3d;

        if (total > 1395) {
            // Level 30+
            diff1 = 72d;
            diff2 = -54215;
            diff3 = 325;
            diff4 = 18;
            diff5 = 0;
        }else if (total > 315) {
            // Level 16 to 29
            diff1 = 40;
            diff2 = -7839;
            diff4 = 10;
            diff5 = 8.1;
        }

        return (Math.sqrt(diff1 * total + diff2) + diff3) / diff4 + diff5;
    }

    public static void set(@NotNull Player player, int total) {
        double levelAndExp = levelOf( total );
        int level = (int) levelAndExp;

        player.setLevel(level);
        player.setExp((float) (levelAndExp - level));
    }

    public static void give(@NotNull Player player, int amount) {
        int total = (int) (total( player ) + amount);
        set( player, total );
    }

    public static void take(@NotNull Player player, int amount) {
        int remaining = (int) (total( player ) - amount);
        set( player, remaining );
    }
}
