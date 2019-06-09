package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;


public class EditGpanodata {

    private Preferences prefs = Preferences.userRoot();
    // I had specified for the arrays:


    public void resetFields(JTextField[] gpanoFields, JCheckBox[] gpanoBoxes) {

        for (JTextField field: gpanoFields) {
            field.setText("");
        }
        for (JCheckBox checkbox: gpanoBoxes) {
            checkbox.setSelected(false);
        }
    }

    public void copyGpanoFromSelected(JTextField[] gpanoFields, JCheckBox[] gpanoBoxes) {
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(gpanoFields, gpanoBoxes);

        String exiftool = prefs.get("exiftool", "");
        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        cmdparams.add(Utils.platformExiftool());
        cmdparams.add("e");
        cmdparams.add("n");
        cmdparams.addAll( Arrays.asList(MyConstants.GPANO_PARAMS));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            System.out.println("res is\n" + res);
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(gpanoFields, res);
        }
    }

    private void displayCopiedInfo(JTextField[] gpanoFields, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(System.getProperty("line.separator"));
        //for(int i = 0; i < lines.length; i++) {
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //Wit ALL spaces removed from the tag we als need to use identiefiers without spaces
            System.out.print(SpaceStripped+ " ; value: " + cells[1] + "\n");
            if (SpaceStripped.contains("CroppedAreaImageHeightPixels")) {
                gpanoFields[0].setText(cells[1]);
            }
            if (SpaceStripped.contains("CroppedAreaImageWidthPixels")) {
                gpanoFields[1].setText(cells[1]);
            }
            if (SpaceStripped.contains("CroppedAreaLeftPixels")) {
                gpanoFields[2].setText(cells[1]);
            }
            if (SpaceStripped.contains("CroppedAreaTopPixels")) {
                gpanoFields[3].setText(cells[1]);
            }
            if (SpaceStripped.contains("FullPanoHeightPixels")) {
                gpanoFields[4].setText(cells[1]);
            }
            if (SpaceStripped.contains("FullPanoWidthPixels")) {
                gpanoFields[5].setText(cells[1]);
            }
            if (SpaceStripped.contains("ProjectionType")) {
                //gpanoFields[6].setText(cells[1]);
            }
            if (SpaceStripped.contains("PoseHeadingDegrees")) {
                gpanoFields[6].setText(cells[1]);
            }
            if (SpaceStripped.contains("StitchingSoftware")) {
                gpanoFields[7].setText(cells[1]);
            }
            if (SpaceStripped.contains("InitialViewHeadingDegrees")) {
                gpanoFields[8].setText(cells[1]);
            }
            if (SpaceStripped.contains("InitialViewPitchDegrees")) {
                gpanoFields[9].setText(cells[1]);
            }
            if (SpaceStripped.contains("InitialViewRollDegrees")) {
                gpanoFields[10].setText(cells[1]);
            }
            if (SpaceStripped.contains("InitialHorizontalFOVDegrees")) {
                gpanoFields[11].setText(cells[1]);
            }
        }
    }


    public void writeGpanoTags(JTextField[] gpanoFields, JCheckBox[] gpanoBoxes, JCheckBox usePanoViewer, JProgressBar progressBar) {

        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getSelectedFiles();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();

        cmdparams.add(Utils.platformExiftool());
        if (!gpanoBoxes[9].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }

        if (gpanoBoxes[0].isSelected()) {
            cmdparams.add("-xmp:StitchingSoftware=" + gpanoFields[0].getText().trim());
        }
        if (gpanoBoxes[1].isSelected()) {
            cmdparams.add("-xmp:CroppedAreaImageHeightPixels=" + gpanoFields[1].getText().trim());
        }
        if (gpanoBoxes[2].isSelected()) {
            cmdparams.add("-xmp:CroppedAreaImageWidthPixels=" + gpanoFields[2].getText().trim());
        }
        if (gpanoBoxes[3].isSelected()) {
            cmdparams.add("-xmp:CroppedAreaLeftPixels=" + gpanoFields[3].getText().trim());
        }
        if (gpanoBoxes[4].isSelected()) {
            cmdparams.add("-xmp:CroppedAreaTopPixels=" + gpanoFields[4].getText().trim());
        }
        if (gpanoBoxes[5].isSelected()) {
            cmdparams.add("-xmp:FullPanoHeightPixels=" + gpanoFields[5].getText().trim());
        }
        if (gpanoBoxes[6].isSelected()) {
            cmdparams.add("-xmp:FullPanoWidthPixels=" + gpanoFields[6].getText().trim());
        }
        if (gpanoBoxes[7].isSelected()) {
            cmdparams.add("-xmp:ProjectionType=" + gpanoFields[7].getText().trim());
        }
        if (gpanoBoxes[8].isSelected() && usePanoViewer.isSelected()) {
            cmdparams.add("-xmp:UsePanoramaViewer=1");
        }
        if (gpanoBoxes[9].isSelected()) {
            cmdparams.add("-xmp:PoseHeadingDegrees=" + gpanoFields[9].getText().trim());
        }
        if (gpanoBoxes[10].isSelected()) {
            cmdparams.add("-xmp:InitialViewHeadingDegrees=" + gpanoFields[10].getText().trim());
        }
        if (gpanoBoxes[11].isSelected()) {
            cmdparams.add("-xmp:InitialViewPitchDegrees=" + gpanoFields[11].getText().trim());
        }
        if (gpanoBoxes[12].isSelected()) {
            cmdparams.add("-xmp:InitialViewRollDegrees=" + gpanoFields[12].getText().trim());
        }
        if (gpanoBoxes[13].isSelected()) {
            cmdparams.add("-xmp:InitialHorizontalFOVDegrees=" + gpanoFields[13].getText().trim());
        }

        for (int index: selectedIndices) {
            //System.out.println("index: " + index + "  image path:" + files[index].getPath());
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }

        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);

    }
}
