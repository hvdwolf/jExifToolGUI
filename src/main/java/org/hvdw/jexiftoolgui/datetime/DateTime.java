package org.hvdw.jexiftoolgui.datetime;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DateTime {

    private static final Logger logger = LoggerFactory.getLogger(DateTime.class);


    public static void setFileDateTimeToDateTimeOriginal( JProgressBar progressBar) {
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();
        List<String> cmdparams = new ArrayList<>();
        StringBuilder tmpcmpstring = new StringBuilder();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        logger.trace("Set file date/time to DateTimeOriginal?");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("sfdt.text")),ResourceBundle.getBundle("translations/program_strings").getString("sfdt.title"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = Utils.isOsFromMicrosoft();
            if (isWindows) {
                cmdparams.add(Utils.platformExiftool());
                cmdparams.add("-overwrite_original");
                cmdparams.add("\"-FileModifyDate<DateTimeOriginal\"");
            } else {
                // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                cmdparams.add("/bin/sh");
                cmdparams.add("-c");
                tmpcmpstring = new StringBuilder(Utils.platformExiftool() + " -overwrite_original '-FileModifyDate<DateTimeOriginal' ");
            }
            for (int index: selectedIndices) {
                logger.trace("index: {} image path: {}", index, files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    tmpcmpstring.append(" ").append(files[index].getPath());
                }
            }
            if (!isWindows) {
                cmdparams.add(tmpcmpstring.toString());
            }
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
     }


}
