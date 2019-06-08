package org.hvdw.jexiftoolgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.prefs.Preferences;

/**
 * Simple but important application class.
 * The start up and initial configuration point for the application.
 *
 * May set / load / change default configuration based on env and runtime.
 *
 * May also start several threads
 */
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    /* The user preferences from the model */
    private static ProgramPreferences myPrefs;

    public static ProgramPreferences getPrefs() {
        if (myPrefs == null) {
            myPrefs = new ProgramPreferences();
        }
        return myPrefs;
    }


    public static void main(String[] args) {
        logger.debug("Start application");
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(mainScreen::createAndShowGUI);
    }

    public enum OS_NAMES {
        APPLE, MICROSOFT, LINUX
    }
}
