package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.MyVariables;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;


public class EditXmpdata {

    Preferences prefs = Preferences.userRoot();

    // I had specified for the arrays:
    //JTextField[] xmpFields = {xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField};
    //    JTextArea[] xmpAreas = {xmpDescriptiontextArea};
    //    JCheckBox[] xmpBoxes = {xmpCreatorcheckBox, xmpRightscheckBox, xmpDescriptioncheckBox, xmpLabelcheckBox, xmpSubjectcheckBox, xmpTitlecheckBox, xmpPersoncheckBox};

    public void resetfields(JTextField[] xmpFields, JTextArea Description) {

        for (JTextField field: xmpFields) {
            field.setText("");
        }
        Description.setText("");
    }


    public void copyxmpfromselected(JTextField[] xmpFields, JTextArea Description) {
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        String[] xmpcopyparams = {"-e", "-n", "-xmp:Creator", "-xmp:Rights", "-xmp:Label", "-xmp:Subject", "-xmp:Title", "-xmp:Rating", "-xmp:Description", "-xmp:Person", "-xmp:PersonInImage", "-xmp:RegionName" , "-xmp:RegionType"};
        String fpath = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetfields(xmpFields, Description);

        String exiftool = prefs.get("exiftool", "");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
            exiftool = exiftool.replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        System.out.println(fpath);
        cmdparams.add(exiftool);
        cmdparams.addAll(Arrays.asList(xmpcopyparams));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            System.out.println("res is\n" + res);
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error executing command");
        }
        if (res.length() > 0) {
            DisplayCopiedInfo(xmpFields, Description, res);
        }
    }

        public void DisplayCopiedInfo(JTextField[] xmpFields, JTextArea Description, String exiftoolInfo) {
            String[] lines = exiftoolInfo.split(System.getProperty("line.separator"));
            for(int i = 0; i < lines.length; i++) {
                String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
                //With ALL spaces removed from the tag we als need to use identiefiers without spaces
                //System.out.print(SpaceStripped+ " ; value: " + cells[1] + "\n");
                //xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField
                if (SpaceStripped.contains("Creator")) {
                    //System.out.print(" in if-Creator; ");
                    xmpFields[0].setText(cells[1]);
                }
                if (SpaceStripped.contains("Rights")) {
                    xmpFields[1].setText(cells[1]);
                }
                if (SpaceStripped.contains("Label")) {
                    xmpFields[2].setText(cells[1]);
                }
                if (SpaceStripped.contains("Subject")) {  /// Date/timeOriginal: / gives issues with contains????
                    xmpFields[3].setText(cells[1]);
                }
                if (SpaceStripped.contains("Title")) {
                    xmpFields[4].setText(cells[1]);
                }
                if (SpaceStripped.contains("PersonInImage")) {
                    xmpFields[5].setText(cells[1]);
                }
                if (SpaceStripped.contains("RegionName")) {
                    xmpFields[6].setText(cells[1]);
                }
                if (SpaceStripped.contains("RegionType")) {
                    xmpFields[7].setText(cells[1]);
                }
                if (SpaceStripped.contains("Description")) { // Our text area
                    Description.setText(cells[1]);
                }
            }

        }
        
        public void writeXmpTags(JTextField[] xmpFields, JTextArea Description, JCheckBox[] xmpBoxes, JProgressBar progressBar) {
            List<String> cmdparams = new ArrayList<String>();
            int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
            File[] files = MyVariables.getSelectedFiles();

            cmdparams.add(Utils.platformExiftool());
            if (!xmpBoxes[9].isSelected()) { // default overwrite originals, when set do not
                cmdparams.add("-overwrite_original");
            }
            if (xmpBoxes[0].isSelected()) {
                cmdparams.add("-xmp:Creator=" + xmpFields[0].getText().trim());
            }
            if (xmpBoxes[1].isSelected()) {
                cmdparams.add("-xmp:Rights=" + xmpFields[1].getText().trim());
            }
            if (xmpBoxes[2].isSelected()) {
                cmdparams.add("-xmp:Label=" + xmpFields[2].getText().trim());
            }
            if (xmpBoxes[3].isSelected()) {
                cmdparams.add("-xmp:Subject=" + xmpFields[3].getText().trim());
            }
            if (xmpBoxes[4].isSelected()) {
                cmdparams.add("-xmp:Title=" + xmpFields[4].getText().trim());
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
            if (xmpBoxes[5].isSelected()) {
                cmdparams.add("-xmp:Person=" + xmpFields[5].getText());
                cmdparams.add("-xmp:PersonInImage=" + xmpFields[5].getText().trim());
            }
            if (xmpBoxes[6].isSelected()) {
                cmdparams.add("-xmp:RegionName=" + xmpFields[6].getText().trim());
            }
            if (xmpBoxes[7].isSelected()) {
                cmdparams.add("-xmp:RegionType=" + xmpFields[7].getText().trim());
            }
            if (xmpBoxes[8].isSelected()) {
                cmdparams.add("-xmp:Description=" + Description.getText().trim());
            }

            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            for (int index: selectedIndices) {
                //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add(files[index].getPath());
                }
            }

            CommandRunner.RunCommandWithProgress(cmdparams, progressBar);
        }
}
