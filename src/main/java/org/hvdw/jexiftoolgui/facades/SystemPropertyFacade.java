package org.hvdw.jexiftoolgui.facades;

public class SystemPropertyFacade {
    public enum SystemPropertyKey {
        LINE_SEPARATOR("line.separator"),
        OS_NAME("os.name"),
        USER_HOME("user.home");

        public final String key;
        SystemPropertyKey(String key) {
            this.key = key;
        }
    }

    private SystemPropertyFacade() {}

    public static String getPropertyByKey(SystemPropertyKey key) {
        return getPropertyByKey(key, null);
    }

    public static String getPropertyByKey(SystemPropertyKey key, String defaultValue) {
        return System.getProperty(key.key, defaultValue);
    }
}
