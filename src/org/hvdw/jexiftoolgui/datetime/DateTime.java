package org.hvdw.jexiftoolgui.datetime;

import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.programTexts;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DateTime {

    Utils myUtils = new Utils();

    public void setFileDatetoDateTimeOriginal(int[] selectedIndices, File[] files) {
        List<String> cmdparams = new ArrayList<String>();
        String tmpcmpstring = "";
        String[] options = {"No", "Yes"};
        //System.out.println("Set file date/time to DateTimeOriginal?");
        int choice = JOptionPane.showOptionDialog(null, String.format(programTexts.HTML, 600, programTexts.setFileDatetoDateTimeOriginal),"Set file date/time to DateTimeOriginal?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (isWindows) {
                cmdparams.add(myUtils.platformExiftool());
                cmdparams.add("-overwrite_original");
                cmdparams.add("\"-FileModifyDate<DateTimeOriginal\"");
            } else {
                // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                cmdparams.add("/bin/sh");
                cmdparams.add("-c");
                tmpcmpstring = myUtils.platformExiftool() + " -overwrite_original '-FileModifyDate<DateTimeOriginal' ";
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
            try {
                String res = myUtils.runCommand(cmdparams);
                System.out.println(res);
                myUtils.runCommandOutput(res);
            } catch(IOException | InterruptedException ex) {
                System.out.println("Error executing command");
            }
        }
     }


}
