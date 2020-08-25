package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import ch.qos.logback.classic.Level;
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.event.Level;


import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Locale;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.LAST_OPENED_FOLDER;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PREFERRED_APP_LANGUAGE;

/**
 * Simple but important application class.
 * The start up and initial configuration point for the application.
 *
 * May set / load / change default configuration based on env and runtime.
 *
 * May also start several threads
 */
public class Application {
    //private final static Logger logger = LoggerFactory.getLogger(Application.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public static void main(String[] args) {

        //Locale.setDefault(new Locale("es", "ES"));
        //LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        //StatusPrinter.print(lc);
        logger.setLevel(Level.INFO);
        logger.info("Start application");

        String prefLocale = prefs.getByKey(PREFERRED_APP_LANGUAGE, "System default");
        if (!prefLocale.contains("default")) {
            String[] localearray = prefLocale.split("-");
            //logger.info("localearray[0] {}", localearray[0]);
            String[] splitlocale = localearray[0].split("_");
            //logger.info("splitlocale[0] {} splitlocale[1] {}", splitlocale[0].trim(), splitlocale[1].trim());
            Locale.setDefault(new Locale(splitlocale[0].trim(), splitlocale[1].trim()));
        } else {
            logger.info("Continuing in system language or, if not translated, in English");
        }

//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
//        StatusPrinter.print(lc);
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
