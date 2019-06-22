package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetaData {
    private final static Logger logger = LoggerFactory.getLogger(MetaData.class);

    public void copyToXmp() {
        String fpath ="";
        StringBuilder TotalOutput = new StringBuilder();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        List<String> cmdparams = new ArrayList<String>();
        String[] options = {"No", "Yes"};
        logger.debug("Copy all metadata to xmp format");
        int choice = JOptionPane.showOptionDialog(null, ProgramTexts.copymetadatatoxmp,"Copy all metadata to xmp format",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = Utils.isOsFromMicrosoft();

            String exiftool = Utils.platformExiftool();

            for (int index: selectedIndices) {
                // Unfortunately we need to do this file by file. It also means we need to initialize everything again and again
                //logger.debug("index: {}  image path: {}", index, files[index].getPath());
                cmdparams.clear();
                //cmdparams.add(exiftool);
                //cmdparams.add("-TagsFromfile" );
                if (isWindows) {
                    cmdparams.add(exiftool);
                    cmdparams.add("-TagsFromfile");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                    cmdparams.add("\"-all>xmp:all\"");
                    cmdparams.add("-overwrite_original");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add("/bin/sh");
                    cmdparams.add("-c");
                    cmdparams.add(exiftool + " -TagsFromfile " + files[index].getPath() + " '-all:all>xmp:all' -overwrite_original  " + files[index].getPath());
                }
                try {
                    String res = CommandRunner.runCommand(cmdparams);
                    logger.debug("res is\n{}", res);
                    TotalOutput.append(res);
                } catch(IOException | InterruptedException ex) {
                    logger.debug("Error executing command");
                }
            }
            CommandRunner.outputAfterCommand(TotalOutput.toString());
        }

    }

    public void repairJPGMetadata( JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {"No", "Yes"};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        logger.debug("Repair corrupted metadata in JPG(s)");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ProgramTexts.REPAIR_JPG_METADATA),"Repair corrupted metadata in JPG(s)",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            cmdparams.add(Utils.platformExiftool());
            cmdparams.add("-overwrite_original");
            for (String s : MyConstants.REPAIR_JPG_METADATA) {
                cmdparams.add(s);
            }
            boolean isWindows = Utils.isOsFromMicrosoft();

            for (int index: selectedIndices) {
                //logger.debug("index: {}  image path: {}", index, files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add(files[index].getPath());
                }
            }
            // export metadata
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
    }

    public void copyMetaData(JRadioButton[] CopyMetaDataRadiobuttons, JCheckBox[] CopyMetaDataCheckBoxes, int selectedRow, JProgressBar progressBar) {
        //int selectedRow = MyVariables.getSelectedRow();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        boolean atLeastOneSelected = false;
        String fpath ="";

        List<String> params = new ArrayList<String>();
        params.add(Utils.platformExiftool());
        params.add("-TagsFromfile");
        boolean isWindows = Utils.isOsFromMicrosoft();
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[selectedRow].getPath();
        }

        params.add(fpath);
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>You have selected to copy:<br>");
        if (CopyMetaDataRadiobuttons[0].isSelected()) {
            Message.append("All metadata writing info to same named tags to preferred groups<br><br>");
            atLeastOneSelected = true;
        } else if (CopyMetaDataRadiobuttons[1].isSelected()) {
            Message.append("All metadata preserving the original tag groups<br><br>");
            params.add("-all:all");
            atLeastOneSelected = true;
        } else { // The copySelectiveMetadataradioButton
            Message.append("<ul>");
            if (CopyMetaDataCheckBoxes[0].isSelected()) {
                Message.append("<li>the exif data</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[1].isSelected()) {
                Message.append("<li>the xmp data</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[2].isSelected()) {
                Message.append("<li>the iptc data</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[3].isSelected()) {
                Message.append("<li>the ICC data</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[4].isSelected()) {
                Message.append("<li>the gps data</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[5].isSelected()) {
                Message.append("<li>the jfif (header) data</li>");
                params.add("-jfif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[6].isSelected()) {
                Message.append("<li>the makernotes data</li>");
                params.add("-makernotes:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        if (!CopyMetaDataCheckBoxes[7].isSelected()) {
            params.add("-overwrite_original");
        }
        Message.append("Is this correct?</html>");
        if (atLeastOneSelected) {
            String[] options = {"Cancel", "Continue"};
            int choice = JOptionPane.showOptionDialog(null, Message,"You want to copy metadata",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
                // Copy metadata
                String[] etparams = params.toArray(new String[0]);
                for (int index: selectedIndices) {
                    //logger.debug("index: {}  image path: {}", index, files[index].getPath());
                    if (isWindows) {
                        params.add(files[index].getPath().replace("\\", "/"));
                    } else {
                        params.add(files[index].getPath());
                    }
                }
                // export metadata
                CommandRunner.runCommandWithProgressBar(params, progressBar);
            }
        } else {
            JOptionPane.showMessageDialog(null, ProgramTexts.NoOptionSelected,"No copy option selected",JOptionPane.WARNING_MESSAGE);
        }
    }


}
