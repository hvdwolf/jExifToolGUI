package org.hvdw.jexiftoolgui;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

public class UtilsTest {

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void testReturnOfSystemBasedOnSystemProperty() {
        System.setProperty("os.name", "mac");
        Assert.assertEquals("lower case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());
        System.setProperty("os.name", "MAC");
        Assert.assertEquals("Upper case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());
        System.setProperty("os.name", "mAc");
        Assert.assertEquals("mixed case mac should be APPLE", Application.OS_NAMES.APPLE, Utils.getCurrentOsName());

        System.setProperty("os.name", "windows");
        Assert.assertEquals("lower case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());
        System.setProperty("os.name", "WINDOWS");
        Assert.assertEquals("Upper case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());
        System.setProperty("os.name", "wInDoWs");
        Assert.assertEquals("mixed case windows should be MICROSOFT", Application.OS_NAMES.MICROSOFT, Utils.getCurrentOsName());


        System.setProperty("os.name", "");
        Assert.assertEquals("Empty os should be LINUX", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
        System.setProperty("os.name", "asdfq    342");
        Assert.assertEquals("Random value should be LINUX", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
        System.setProperty("os.name", "Linux");
        Assert.assertEquals("Linux should be Linux", Application.OS_NAMES.LINUX, Utils.getCurrentOsName());
    }

}
