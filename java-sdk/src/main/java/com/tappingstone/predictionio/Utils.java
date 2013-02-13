package com.tappingstone.predictionio;

/**
 * Utility class for PredictionIO Java SDK
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 1.0
 * @since 1.0
 */

public class Utils {
    public static String itypesAsString(int[] itypes) {
        StringBuilder sbuilder = new StringBuilder();
        int lastIdx = itypes.length - 1;
        for (int i = 0; i < itypes.length; i++) {
            sbuilder.append(Integer.toString(itypes[i]));
            if (i != lastIdx) {
                sbuilder.append(",");
            }
        }
        return sbuilder.toString();
    }
}
