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
import java.util.ResourceBundle;

public class MetaData {
    private final static Logger logger = LoggerFactory.getLogger(MetaData.class);

    public void copyToXmp() {
        String fpath ="";
        StringBuilder TotalOutput = new StringBuilder();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        List<String> cmdparams = new ArrayList<String>();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        logger.info("Copy all metadata to xmp format");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 500,ResourceBundle.getBundle("translations/program_strings").getString("cmd.dlgtext")),ResourceBundle.getBundle("translations/program_strings").getString("cmd.dlgtitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = Utils.isOsFromMicrosoft();

            String exiftool = Utils.platformExiftool();

            for (int index: selectedIndices) {
                // Unfortunately we need to do this file by file. It also means we need to initialize everything again and again
                //logger.info("index: {}  image path: {}", index, files[index].getPath());
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
                    logger.info("res is\n{}", res);
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
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        logger.info("Repair corrupted metadata in JPG(s)");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("rjpg.dialogtext")),ResourceBundle.getBundle("translations/program_strings").getString("rjpg.dialogtitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            cmdparams.add(Utils.platformExiftool());
            cmdparams.add("-overwrite_original");
            for (String s : MyConstants.REPAIR_JPG_METADATA) {
                cmdparams.add(s);
            }
            boolean isWindows = Utils.isOsFromMicrosoft();

            for (int index: selectedIndices) {
                //logger.info("index: {}  image path: {}", index, files[index].getPath());
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
        StringBuilder Message = new StringBuilder("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
        if (CopyMetaDataRadiobuttons[0].isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.alltopreferred"));
            atLeastOneSelected = true;
        } else if (CopyMetaDataRadiobuttons[1].isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.alltoorggroup"));
            params.add("-all:all");
            atLeastOneSelected = true;
        } else { // The copySelectiveMetadataradioButton
            Message.append("<ul>");
            if (CopyMetaDataCheckBoxes[0].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyexifcheckbox") + "</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[1].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyxmpcheckbox") + "</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[2].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyiptccheckbox") + "</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[3].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyicc_profilecheckbox") + "</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[4].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copygpscheckbox") + "</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[5].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyjfifcheckbox") + "</li>");
                params.add("-jfif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[6].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copymakernotescheckbox") + "</li>");
                params.add("-makernotes:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        if (!CopyMetaDataCheckBoxes[7].isSelected()) {
            params.add("-overwrite_original");
        }
        Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgisthiscorrect" ) + "</html>");
        if (atLeastOneSelected) {
            String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
            int choice = JOptionPane.showOptionDialog(null, Message,ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgtitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
                // Copy metadata
                String[] etparams = params.toArray(new String[0]);
                for (int index: selectedIndices) {
                    //logger.info("index: {}  image path: {}", index, files[index].getPath());
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
