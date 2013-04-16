package io.prediction;

/**
 * Utility class for PredictionIO Java SDK
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.4.1
 * @since 0.2
 */

public class Utils {
    public static String arrayToString(String[] itypes) {
        StringBuilder sbuilder = new StringBuilder();
        int lastIdx = itypes.length - 1;
        for (int i = 0; i < itypes.length; i++) {
            sbuilder.append(itypes[i]);
            if (i != lastIdx) {
                sbuilder.append(",");
            }
        }
        return sbuilder.toString();
    }
}
