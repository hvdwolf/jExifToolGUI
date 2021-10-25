package org.hvdw.jexiftoolgui.datetime;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;

public class DateTime {

    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(DateTime.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;


    public static void setFileDateTimeToDateTimeOriginal( JProgressBar progressBar, String fileType) {
        int counter = 0;  //Used in loop for first time initialization; can't do that outside the loop
        String action = "";
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        List<String> cmdparams = new ArrayList<>();
        StringBuilder tmpcmpstring = new StringBuilder();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        logger.trace("Set file date/time to DateTimeOriginal?");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("sfdt.text")),ResourceBundle.getBundle("translations/program_strings").getString("sfdt.title"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
            boolean isWindows = Utils.isOsFromMicrosoft();
            if (isWindows) {
                cmdparams.add("\"" + Utils.platformExiftool().replace("\\", "/") + "\"" );
                if (preserveModifyDate) {
                    cmdparams.add("-preserve");
                }
                cmdparams.add("-overwrite_original");
                if ("image".equals(fileType)) {
                    cmdparams.add("\"-FileModifyDate<DateTimeOriginal\"");
                } else {
                    cmdparams.add("\"-CreateDate<DateTimeOriginal\"");
                }
            } else {
                // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                cmdparams.add("/bin/sh");
                cmdparams.add("-c");
                if ("image".equals(fileType)) {
                    action = "-FileModifyDate<DateTimeOriginal";
                } else {
                    action = "-CreateDate<DateTimeOriginal";
                }
                if (preserveModifyDate) {
                    tmpcmpstring = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + " -overwrite_original -preserve '" + action + "' ");
                } else {
                    tmpcmpstring = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + " -overwrite_original '\" + action + \"' ");
                }
            }
            for (int index: selectedIndices) {

                logger.trace("index: {} image path: {}", index, files[index].getPath());
                if (isWindows) {
                    cmdparams.add("\"" + files[index].getPath().replace("\\", "/") + "\"");
                } else {
                    //tmpcmpstring.append(" ").append(files[index].getPath().replaceAll(" ", "\\ "));
                    tmpcmpstring.append(" ").append("'" + files[index].getPath() + "'");
                }
            }
            if (!isWindows) {
                cmdparams.add(tmpcmpstring.toString());
            }
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
     }


}
