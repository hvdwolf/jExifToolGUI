package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class EditStringdata {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditStringdata.class);


    public void resetFields(JTextField[] stringPlusFields, JCheckBox[] stringPlusCheckboxes) {

        for (JTextField field: stringPlusFields) {
            field.setText("");
        }
        for (JCheckBox chkbx: stringPlusCheckboxes) {
            chkbx.setSelected(false);
        }
    }

    public void copyStringPlusFromSelected(JTextField[] stringPlusFields, JCheckBox[] stringPlusCheckboxes) {
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        String[] xmpcopyparams = {"-e", "-n", "-xmp-acdsee:keywords","-xmp:Subject", "-xmp:PersonInImage"};
        String fpath = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(stringPlusFields, stringPlusCheckboxes);

        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        logger.info(fpath);
        cmdparams.add(Utils.platformExiftool());
        cmdparams.addAll(Arrays.asList(xmpcopyparams));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            logger.info("res is\n{}", res);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(stringPlusFields, res);
        }
    }
    public void displayCopiedInfo(JTextField[] stringPlusFields, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            if (SpaceStripped.contains("Keywords")) {
                stringPlusFields[0].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("Subject")) {
                stringPlusFields[1].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("PersonInImage")) {
                stringPlusFields[2].setText(cells[1].trim());
            }
        }

    }

    public List<String> fillcmdparams (List<String> cmdparams, String textField, String action, String tag) {
        String[] words = textField.trim().split(",");

        for (String word : words) {
            cmdparams.add(tag + action + word.trim());
        }
        return cmdparams;
    }

    public void writeStringPlusTags(JTextField[] stringPlusFields, JCheckBox[] stringPlusBoxes, String[] selectedRadioButtons, JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        cmdparams.add(Utils.platformExiftool());
        boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
        if (preserveModifyDate) {
            cmdparams.add("-preserve");
        }
        if (!stringPlusBoxes[2].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());

        //keywords -> xmp keywords
        if ( (stringPlusFields[0].getText().length() > 0) && (!"".equals(selectedRadioButtons[0])) && (stringPlusBoxes[0].isSelected()) ) {
            cmdparams = fillcmdparams(cmdparams, stringPlusFields[0].getText(), selectedRadioButtons[0], "-xmp-acdsee:keywords");
        }
        // keywords -> IPTC
        if ( (stringPlusFields[0].getText().length() > 0) && (!"".equals(selectedRadioButtons[0])) && (stringPlusBoxes[1].isSelected()) ) {
            cmdparams = fillcmdparams(cmdparams, stringPlusFields[0].getText(), selectedRadioButtons[0], "-iptc:keywords");
        }
        // Subject -> only XMP
        if ( (stringPlusFields[1].getText().length() > 0) && (!"".equals(selectedRadioButtons[1])) ) {
            cmdparams = fillcmdparams(cmdparams, stringPlusFields[1].getText(), selectedRadioButtons[1], "-xmp:subject");
        }
        // PersonInImage -> only xmp
        if ( (stringPlusFields[2].getText().length() > 0) && (!"".equals(selectedRadioButtons[2])) ) {
            cmdparams = fillcmdparams(cmdparams, stringPlusFields[2].getText(), selectedRadioButtons[2], "-xmp:personinimage");
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

        logger.info("params for string+ " + cmdparams.toString());
        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
    }

}
