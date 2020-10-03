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


public class EditXmpdata {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditXmpdata.class);

    // I had specified for the arrays:
    //JTextField[] xmpFields = {xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField};
    //    JTextArea[] xmpAreas = {xmpDescriptiontextArea};
    //    JCheckBox[] xmpBoxes = {xmpCreatorcheckBox, xmpRightscheckBox, xmpDescriptioncheckBox, xmpLabelcheckBox, xmpSubjectcheckBox, xmpTitlecheckBox, xmpPersoncheckBox};

    public void resetFields(JTextField[] xmpFields, JTextArea Description) {

        for (JTextField field: xmpFields) {
            field.setText("");
        }
        Description.setText("");
    }


    public void copyXmpFromSelected(JTextField[] xmpFields, JTextArea Description) {
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        String[] xmpcopyparams = {"-e", "-n", "-xmp:Creator", "-xmp:Credit", "-xmp:Rights", "-xmp:Label", "-xmp-pdf:Keywords", "-xmp:Subject", "-xmp:Title", "-xmp:Description", "-xmp:Person", "-xmp:PersonInImage"};
        String fpath = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(xmpFields, Description);

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
            displayCopiedInfo(xmpFields, Description, res);
        }
    }

        public void displayCopiedInfo(JTextField[] xmpFields, JTextArea Description, String exiftoolInfo) {
            String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            for (String line : lines) {
                String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
                //With ALL spaces removed from the tag we als need to use identiefiers without spaces
                //xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField
                if (SpaceStripped.contains("Creator")) {
                    xmpFields[0].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Credit")) {
                    xmpFields[1].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Rights")) {
                    xmpFields[2].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Label")) {
                    xmpFields[3].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Title")) {
                    xmpFields[4].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Keywords")) {
                    xmpFields[5].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("Subject")) {
                    xmpFields[6].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("PersonInImage")) {
                    xmpFields[7].setText(cells[1].trim());
                }
                /*if (SpaceStripped.contains("RegionName")) {
                    xmpFields[7].setText(cells[1].trim());
                }
                if (SpaceStripped.contains("RegionType")) {
                    xmpFields[8].setText(cells[1].trim());
                }*/
                if (SpaceStripped.contains("Description")) { // Our text area
                    Description.setText(cells[1].trim());
                }
            }

        }
        
        public void writeXmpTags(JTextField[] xmpFields, JTextArea Description, JCheckBox[] xmpBoxes, JProgressBar progressBar) {
            List<String> cmdparams = new ArrayList<String>();
            int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
            File[] files = MyVariables.getSelectedFiles();

            cmdparams.add(Utils.platformExiftool());
            boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, false);
            if (preserveModifyDate) {
                cmdparams.add("-preserve");
            }
            if (!xmpBoxes[9].isSelected()) { // default overwrite originals, when set do not
                cmdparams.add("-overwrite_original");
            }
            cmdparams.addAll(Utils.AlwaysAdd());

            if (xmpBoxes[0].isSelected()) {
                cmdparams.add("-xmp:Creator=" + xmpFields[0].getText().trim());
            }
            if (xmpBoxes[1].isSelected()) {
                cmdparams.add("-xmp:Credit=" + xmpFields[1].getText().trim());
            }
            if (xmpBoxes[2].isSelected()) {
                cmdparams.add("-xmp:Rights=" + xmpFields[2].getText().trim());
            }
            if (xmpBoxes[3].isSelected()) {
                cmdparams.add("-xmp:Label=" + xmpFields[3].getText().trim());
            }
            if (xmpBoxes[4].isSelected()) {
                cmdparams.add("-xmp:Title=" + xmpFields[4].getText().trim());
            }
            if (xmpBoxes[5].isSelected()) {
                cmdparams.add("-xmp-pdf:Keywords=" + xmpFields[5].getText().trim());
            }
            if (xmpBoxes[6].isSelected()) {
                String[] subjects = xmpFields[6].getText().trim().split(",");
                for (String subject : subjects) {
                    cmdparams.add("-xmp:Subject=" + subject.trim());
                }
                //cmdparams.add("-xmp:Subject=" + xmpFields[4].getText().trim());
            }
    /*if (xmpBoxes[5.isSelected()) {
        if self.xmp_rating1.isSelected()) {
            rating = 1
        elif self.xmp_rating2.isSelected()) {
            rating = 2
        elif self.xmp_rating3.isSelected()) {
            rating = 3
        elif self.xmp_rating4.isSelected()) {
            rating = 4
        else) {
            rating = 5
        cmdparams.add("-xmp:Rating=" + rating); */
            if (xmpBoxes[7].isSelected()) {
                //cmdparams.add("-xmp:Person=" + xmpFields[6].getText());
                String[] persons = xmpFields[7].getText().trim().split(",");
                for (String person : persons) {
                    cmdparams.add("-xmp:PersonInImage=" + person.trim());
                }
                //cmdparams.add("-xmp:PersonInImage=" + xmpFields[6].getText().trim());
            }
            /*if (xmpBoxes[7].isSelected()) {
                cmdparams.add("-xmp:RegionName=\"" + xmpFields[7].getText().trim() + "\"");
            }
            if (xmpBoxes[8].isSelected()) {
                cmdparams.add("-xmp:RegionType=\"" + xmpFields[8].getText().trim() + "\"");
            }*/
            if (xmpBoxes[8].isSelected()) {
                cmdparams.add("-xmp:Description=" + Description.getText().trim());
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

            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
}
