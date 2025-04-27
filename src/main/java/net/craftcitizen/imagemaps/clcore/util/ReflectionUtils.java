package net.craftcitizen.imagemaps.clcore.util;

import java.lang.reflect.Method;

public class ReflectionUtils {
    public static Method getMethod(final Class<?> clazz, final String string) {
        for (final Method m : clazz.getMethods()) {
            if (m.getName().equals(string)) {
                return m;
            }
        }
        return null;
    }
}
