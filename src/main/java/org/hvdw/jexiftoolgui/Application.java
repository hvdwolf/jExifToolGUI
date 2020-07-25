package org.hvdw.jexiftoolgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

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

    public static void main(String[] args) {
        logger.info("Start application");
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == OS_NAMES.LINUX) {
            Utils.setUIFont (new FontUIResource("SansSerif", Font.PLAIN,12));
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(mainScreen::createAndShowGUI);
    }

    public enum OS_NAMES {
        APPLE, MICROSOFT, LINUX
    }
}
