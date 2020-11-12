package org.hvdw.jexiftoolgui;

import org.hvdw.jexiftoolgui.controllers.*;
import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.datetime.ModifyDateTime;
import org.hvdw.jexiftoolgui.datetime.ShiftDateTime;
import org.hvdw.jexiftoolgui.editpane.*;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.metadata.CreateArgsFile;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;
import org.hvdw.jexiftoolgui.metadata.MetaData;
import org.hvdw.jexiftoolgui.metadata.RemoveMetadata;
import org.hvdw.jexiftoolgui.renaming.RenamePhotos;
import org.hvdw.jexiftoolgui.view.*;
import org.slf4j.LoggerFactory;



import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Locale;

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
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public static void main(String[] args) {

        Utils.SetApplicationWideLogLevel();
        logger.info("Start application");


        if (args.length > 0) {
            MyVariables.setcommandLineArgsgiven(true);
            MyVariables.setcommandLineArgs(args);
            for (String arg : args) {
                logger.debug("arg: {}", arg);
            }
        } else {
            MyVariables.setcommandLineArgsgiven(false);
        }

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

        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == OS_NAMES.LINUX) {
            Utils.setUIFont (new FontUIResource("SansSerif", Font.PLAIN,12));
        } else if (os == OS_NAMES.APPLE) {
            System.setProperty("apple.laf.UseScreenMenuBar", "true");
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(mainScreen::createAndShowGUI);
    }

    public enum OS_NAMES {
        APPLE, MICROSOFT, LINUX
    }

}
