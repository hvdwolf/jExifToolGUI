package org.hvdw.jexiftoolgui;

import org.hvdw.jexiftoolgui.controllers.SingletonEnum;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.LoggerFactory;



import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Locale;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;

/**
 * Simple but important application class.
 * The start up and initial configuration point for the application.
 *
 * May set / load / change default configuration based on env and runtime.
 *
 * May also start several threads
 */
public class Application {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public static void main(String[] args) {

        // Doesn't work but leave in
        System.out.println(SingletonEnum.INSTANCE);

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
            String[] localearray = prefLocale.split(" - ");
            //logger.info("localearray[0] {}", localearray[0]);
            String[] splitlocale = localearray[0].split("_");
            //logger.info("splitlocale[0] {} splitlocale[1] {}", splitlocale[0].trim(), splitlocale[1].trim());
            Locale.setDefault(new Locale(splitlocale[0].trim(), splitlocale[1].trim()));
        } else {
            logger.info("Continuing in system language or, if not translated, in English");
        }

        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == OS_NAMES.APPLE) {
            System.setProperty("apple.laf.UseScreenMenuBar", "true");
        }
        // Get user defined font or use default font
        String userFont = prefs.getByKey(USER_DEFINED_FONT, "sans-serif");
        int userFontSize = Integer.parseInt(prefs.getByKey(USER_DEFINED_FONTSIZE, "12"));
        Utils.setUIFont(new FontUIResource(userFont, Font.PLAIN, userFontSize));

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(mainScreen::createAndShowGUI);
    }

    public enum OS_NAMES {
        APPLE, MICROSOFT, LINUX
    }

}
