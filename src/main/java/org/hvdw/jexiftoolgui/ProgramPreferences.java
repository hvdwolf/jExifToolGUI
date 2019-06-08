package org.hvdw.jexiftoolgui;

import java.util.prefs.Preferences;

public class ProgramPreferences {
    /** Program preferences object. */
    private Preferences myPrefs = Preferences.userNodeForPackage(getClass());

    /* The preference nodes */
    private final String ARTIST = "artist";
    private final String COPYRIGHTS = "copyrights";
    private final String EXIFTOOL = "exiftool";
    private final String VERSIONCHECK = "versioncheck";
    private final String DEFAULTSTARTFOLDER = "defaultstartfolder";
    private final String USELASTOPENEDFOLDER = "uselastopenedfolder";

    public ProgramPreferences() {

        //Maybe we need something here to create and delete all preferences
        getPreferences();
    }

    private void getPreferences() {
        String artist = myPrefs.get(ARTIST, "");
        String copyRights = myPrefs.get(COPYRIGHTS, "");
        String exiftool = myPrefs.get(EXIFTOOL, "");
        boolean versioncheck = myPrefs.getBoolean(VERSIONCHECK, false);
        boolean defaultstartfolder = myPrefs.getBoolean(DEFAULTSTARTFOLDER, false);
        boolean uselastopenedfolder = myPrefs.getBoolean(USELASTOPENEDFOLDER, false);
    }
}
