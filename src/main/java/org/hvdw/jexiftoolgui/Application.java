package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.hvdw.jexiftoolgui.controllers.*;
import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.datetime.ModifyDateTime;
import org.hvdw.jexiftoolgui.datetime.ShiftDateTime;
import org.hvdw.jexiftoolgui.editpane.*;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import ch.qos.logback.classic.Level;
//import org.slf4j.Logger;
import org.hvdw.jexiftoolgui.metadata.CreateArgsFile;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;
import org.hvdw.jexiftoolgui.metadata.MetaData;
import org.hvdw.jexiftoolgui.metadata.RemoveMetadata;
import org.hvdw.jexiftoolgui.renaming.RenamePhotos;
import org.hvdw.jexiftoolgui.view.*;
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
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class);
    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public static void main(String[] args) {

        SetApplicationWideLogLevel();
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

    public static void SetApplicationWideLogLevel() {
        //Do this for all classes
        // first to do
        //main level
        Utils.SetLoggingLevel(Application.class);
        Utils.SetLoggingLevel(Utils.class);
        Utils.SetLoggingLevel(MenuActionListener.class);
        Utils.SetLoggingLevel(ButtonsActionListener.class);
        Utils.SetLoggingLevel(SQLiteJDBC.class);
        Utils.SetLoggingLevel(StandardFileIO.class);
        Utils.SetLoggingLevel(CheckPreferences.class);
        Utils.SetLoggingLevel(CommandRunner.class);
        Utils.SetLoggingLevel(ExifTool.class);
        Utils.SetLoggingLevel(UpdateActions.class);
        Utils.SetLoggingLevel(YourCommands.class);
        Utils.SetLoggingLevel(mainScreen.class);

        Utils.SetLoggingLevel(TablePasteAdapter.class);
        Utils.SetLoggingLevel(PreferencesDialog.class);
        Utils.SetLoggingLevel(RenamePhotos.class);

        Utils.SetLoggingLevel(ImageFunctions.class);


        Utils.SetLoggingLevel(DateTime.class);
        Utils.SetLoggingLevel(ModifyDateTime.class);
        Utils.SetLoggingLevel(ShiftDateTime.class);

        Utils.SetLoggingLevel(EditExifdata.class);
        Utils.SetLoggingLevel(EditGeotaggingdata.class);
        Utils.SetLoggingLevel(EditGpanodata.class);
        Utils.SetLoggingLevel(EditGPSdata.class);
        Utils.SetLoggingLevel(EditLensdata.class);
        Utils.SetLoggingLevel(EditStringdata.class);
        Utils.SetLoggingLevel(EditUserDefinedCombis.class);
        Utils.SetLoggingLevel(EditXmpdata.class);

        Utils.SetLoggingLevel(MetaData.class);
        Utils.SetLoggingLevel(CreateArgsFile.class);
        Utils.SetLoggingLevel(ExportMetadata.class);
        Utils.SetLoggingLevel(RemoveMetadata.class);

        Utils.SetLoggingLevel(CreateMenu.class);
        Utils.SetLoggingLevel(DatabasePanel.class);
        Utils.SetLoggingLevel(JavaImageViewer.class);
        Utils.SetLoggingLevel(LinkListener.class);
        Utils.SetLoggingLevel(WebPageInPanel.class);
        Utils.SetLoggingLevel(AddFavorite.class);
        Utils.SetLoggingLevel(CreateUpdatemyLens.class);
        Utils.SetLoggingLevel(DeleteFavorite.class);
        Utils.SetLoggingLevel(MetadataUserCombinations.class);
        Utils.SetLoggingLevel(SelectFavorite.class);
        Utils.SetLoggingLevel(SelectmyLens.class);
        Utils.SetLoggingLevel(SimpleWebView.class);
    }
}
