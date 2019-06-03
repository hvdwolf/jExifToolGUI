package org.hvdw.jexiftoolgui.datetime;

import org.hvdw.jexiftoolgui.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.ProgramTexts;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DateTime {


    public void SET_FILEDATETIME_TO_DATETIMEORIGINAL( JProgressBar progressBar) {
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();
        List<String> cmdparams = new ArrayList<String>();
        String tmpcmpstring = "";
        String[] options = {"No", "Yes"};
        //System.out.println("Set file date/time to DateTimeOriginal?");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 600, ProgramTexts.SET_FILEDATETIME_TO_DATETIMEORIGINAL),"Set file date/time to DateTimeOriginal?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (isWindows) {
                cmdparams.add(Utils.platformExiftool());
                cmdparams.add("-overwrite_original");
                cmdparams.add("\"-FileModifyDate<DateTimeOriginal\"");
            } else {
                // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                cmdparams.add("/bin/sh");
                cmdparams.add("-c");
                tmpcmpstring = Utils.platformExiftool() + " -overwrite_original '-FileModifyDate<DateTimeOriginal' ";
            }
            for (int index: selectedIndices) {
                //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    //cmdparams.add(files[index].getPath());
                    tmpcmpstring += " " + files[index].getPath();
                }
            }
            if (!isWindows) {
                cmdparams.add(tmpcmpstring);
            }
            CommandRunner.RunCommandWithProgress(cmdparams, progressBar);
        }
     }


}
