package me.knighthat.plugin;

import me.brannstroom.expbottle.model.Experience;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.security.DrbgParameters;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpCalculatorTest {

    static Map<Integer, Integer> nextLevelRequirements = new HashMap<>(9);

    static Map<Integer, Integer> xpAt = new HashMap<>(9);

    @BeforeAll
    static void setup() {
        nextLevelRequirements.put( 0, 7 );
        nextLevelRequirements.put( 1, 9 );
        nextLevelRequirements.put( 2, 11 );
        nextLevelRequirements.put( 17, 47 );
        nextLevelRequirements.put( 18, 52 );
        nextLevelRequirements.put( 19, 57 );
        nextLevelRequirements.put( 31, 121 );
        nextLevelRequirements.put( 32, 130 );
        nextLevelRequirements.put( 33, 139 );

        xpAt.put( 0, 0 );
        xpAt.put( 1, 7 );
        xpAt.put(2, 16);
        xpAt.put( 17, 394 );
        xpAt.put( 18, 441 );
        xpAt.put( 19, 493 );
        xpAt.put( 31, 1507 );
        xpAt.put( 32, 1628 );
        xpAt.put( 33, 1758 );
    }

    int[] lvlToTest = {0,1,2,17,18,19,31,32,33};

    @Test
    void nextReq() {
        for (int lvl : lvlToTest)
            Assertions.assertEquals( ExpCalculator.nextReq( lvl ), nextLevelRequirements.get( lvl ));
    }

    @Test
    void at() {
        for (int lvl : lvlToTest)
            Assertions.assertEquals( ExpCalculator.at( lvl ), xpAt.get( lvl ) );
    }

    @Test
    void levelOf() {
        for (int lvl : lvlToTest) {
            int at = xpAt.get( lvl );
            Assertions.assertEquals( ExpCalculator.levelOf( at ), lvl );
        }

        for (int i = 0 ; i < 1001 ; i++) {
            int xpAt = ExpCalculator.at( i );
            Assertions.assertEquals( ExpCalculator.levelOf( xpAt ), Experience.getLevelFromExp( xpAt ) );
        }
    }
}