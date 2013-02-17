package io.prediction;

/**
 * Utility class for PredictionIO Java SDK
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.2
 * @since 0.2
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
