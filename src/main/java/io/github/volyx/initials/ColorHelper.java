package io.github.volyx.initials;

import java.util.Random;

class ColorHelper {

    /**
     * The MAX color constant.
     */
    private static final int MAX = 200;

    /**
     * The MIN color constant.
     */

    private static final int MIN = 80;

    private static Random random;

    private static int getFromRange(final int min, final int max, final double random) {
        return min + (int) (random * ((max - min) + 1));
    }


    private static int getRandomInt(final int min, final int max) {
        if (random == null) {
            random = new Random();
        }
        return getFromRange(min, max, random.nextDouble());
    }

    static int getRandomClearColorInt(final String seedString) {
        final int red = getRandomInt(MIN, MAX);
        final int green = getRandomInt(MIN, MAX);
        final int blue = getRandomInt(MIN, MAX);
        // http://stackoverflow.com/questions/4801366/convert-rgb-values-into-integer-pixel
        return ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff);
    }

}
