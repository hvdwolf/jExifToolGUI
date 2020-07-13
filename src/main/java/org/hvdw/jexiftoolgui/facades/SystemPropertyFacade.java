package org.hvdw.jexiftoolgui.facades;

public class SystemPropertyFacade {
    public enum SystemPropertyKey {
        LINE_SEPARATOR("line.separator"),
        FILE_SEPARATOR(" file.separator"),
        OS_NAME("os.name"),
        OS_ARCH("os.arch"),
        OS_VERSION(" os.version"),
        USER_DIR("user.dir"),
        USER_HOME("user.home"),
        USER_NAME("user.name"),
        JAVA_HOME("java.home"),
        JAVA_VERSION("java.version");


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
