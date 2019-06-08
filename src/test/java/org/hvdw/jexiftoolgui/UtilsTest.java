package org.hvdw.jexiftoolgui;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import static org.junit.Assert.*;

public class UtilsTest {

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void testReturnOfSystemBasedOnSystemProperty() {
        System.setProperty("os.name", "mac");
        assertEquals("lower case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());
        System.setProperty("os.name", "MAC");
        assertEquals("Upper case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());
        System.setProperty("os.name", "mAc");
        assertEquals("mixed case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());

        System.setProperty("os.name", "windows");
        assertEquals("lower case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());
        System.setProperty("os.name", "WINDOWS");
        assertEquals("Upper case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());
        System.setProperty("os.name", "wInDoWs");
        assertEquals("mixed case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());


        System.setProperty("os.name", "");
        assertEquals("Empty os should be LINUX", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
        System.setProperty("os.name", "asdfq    342");
        assertEquals("Random value should be LINUX", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
        System.setProperty("os.name", "Linux");
        assertEquals("Linux should be Linux", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
    }

}
