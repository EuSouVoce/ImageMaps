package net.craftcitizen.imagemaps.clcore.command;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class CommandUtils {
    public static String[] parseArgumentStrings(final String[] args) {
        final List<String> tmp = new ArrayList<String>();
        final int[] open = new int[args.length];
        final int[] close = new int[args.length];
        for (int i = 0; i < args.length; ++i) {
            for (int j = 0; j < args[i].length() && args[i].charAt(j) == '\"'; ++j) {
                final int[] array = open;
                final int n = i;
                ++array[n];
            }
            for (int j = args[i].length() - 1; j >= 0 && args[i].charAt(j) == '\"'; --j) {
                final int[] array2 = close;
                final int n2 = i;
                ++array2[n2];
            }
        }
        for (int stringPtr = 0; stringPtr < args.length; ++stringPtr) {
            if (open[stringPtr] <= 0) {
                tmp.add(args[stringPtr]);
            } else {
                int count = 0;
                for (int k = stringPtr; k < args.length; ++k) {
                    count += open[k];
                    count -= close[k];
                    if (count <= 0) {
                        final String joined = String.join(" ",
                                (CharSequence[]) Arrays.copyOfRange(args, stringPtr, k + 1));
                        tmp.add(joined.substring(1, joined.length() - 1));
                        stringPtr = k;
                        break;
                    }
                }
                if (count > 0) {
                    tmp.add(args[stringPtr]);
                }
            }
        }
        return tmp.toArray(new String[tmp.size()]);
    }
}
