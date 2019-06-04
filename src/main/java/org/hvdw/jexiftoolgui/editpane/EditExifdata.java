package org.hvdw.jexiftoolgui.editpane;

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


public class EditExifdata {

    private Preferences prefs = Preferences.userRoot();
    // I had specified for the arrays:
    //JTextField[] exifFields = {ExifMaketextField, ExifModeltextField, ExifModifyDatetextField, ExifDateTimeOriginaltextField,ExifCreateDatetextField,
    //        ExifArtistCreatortextField, ExifCopyrighttextField, ExifUsercommenttextField};
    //    JCheckBox[] exifBoxes = {ExifMakecheckBox, ExifModelcheckBox, ExifModifyDatecheckBox, ExifDateTimeOriginalcheckBox,ExifCreateDatecheckBox,
    //            ExifArtistCreatorcheckBox, ExifCopyrightcheckBox, ExifUsercommentcheckBox, ExifDescriptioncheckBox};

    public void resetFields(JTextField[] exifFields, JTextArea exiftextArea) {

        for (JTextField field: exifFields) {
            field.setText("");
        }
        exiftextArea.setText("");
    }

    public void copyExifFromSelected(JTextField[] exifFields, JTextArea exiftextArea) {
        String[] exifcopyparams = {"-e","-n","-exif:Make","-exif:Model","-exif:ModifyDate","-exif:DateTimeOriginal","-exif:CreateDate","-exif:Artist","-exif:Copyright","-exif:UserComment","-exif:ImageDescription"};
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(exifFields, exiftextArea);

        String exiftool = prefs.get("exiftool", "");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        cmdparams.add(Utils.platformExiftool());
        cmdparams.addAll( Arrays.asList(exifcopyparams));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            System.out.println("res is\n" + res);
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(exifFields, exiftextArea, res);
        }
    }

    private void displayCopiedInfo(JTextField[] exifFields, JTextArea exiftextArea, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(System.getProperty("line.separator"));
        //for(int i = 0; i < lines.length; i++) {
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //Wit ALL spaces removed from the tag we als need to use identiefiers without spaces
            System.out.print(SpaceStripped+ " ; value: " + cells[1] + "\n");
            if (SpaceStripped.contains("Make")) {
                System.out.print(" in if-Make; ");
                exifFields[0].setText(cells[1]);
            }
            if (SpaceStripped.contains("CameraModelName")) {
                exifFields[1].setText(cells[1]);
            }
            if (SpaceStripped.contains("ModifyDate")) {
                exifFields[2].setText(cells[1]);
            }
            if (SpaceStripped.contains("Date/TimeOriginal")) {  /// Date/timeOriginal: / gives issues with contains????
                exifFields[3].setText(cells[1]);
            }
            if (SpaceStripped.contains("CreateDate")) {
                exifFields[4].setText(cells[1]);
            }
            if (SpaceStripped.contains("Artist")) {
                exifFields[5].setText(cells[1]);
            }
            if (SpaceStripped.contains("Copyright")) {
                exifFields[6].setText(cells[1]);
            }
            if (SpaceStripped.contains("UserComment")) {
                exifFields[7].setText(cells[1]);
            }
            if (SpaceStripped.contains("ImageDescription")) {
                exiftextArea.setText(cells[1]);
            }
        }

    }


    public void writeExifTags(JTextField[] exifFields, JTextArea Description, JCheckBox[] exifBoxes, JProgressBar progressBar) {

        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getSelectedFiles();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();

        cmdparams.add(Utils.platformExiftool());
        if (!exifBoxes[9].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }

        if (exifBoxes[0].isSelected()) {
            cmdparams.add("-exif:Make=" + exifFields[0].getText().trim());
        }
        if (exifBoxes[1].isSelected()) {
            cmdparams.add("-exif:Model=" + exifFields[1].getText().trim());
        }
        if (exifBoxes[2].isSelected()) {
            cmdparams.add("-exif:ModifyDate=" + exifFields[2].getText().trim());
        }
        if (exifBoxes[3].isSelected()) {
            cmdparams.add("-exif:DateTimeOriginal=" + exifFields[3].getText().trim());
        }
        if (exifBoxes[4].isSelected()) {
            cmdparams.add("-exif:CreateDate=" + exifFields[4].getText().trim());
        }
        if (exifBoxes[5].isSelected()) {
            cmdparams.add("-exif:Artist=" + exifFields[5].getText().trim());
        }
        if (exifBoxes[6].isSelected()) {
            cmdparams.add("-exif:Copyright=" + exifFields[6].getText().trim());
        }
        if (exifBoxes[7].isSelected()) {
            cmdparams.add("-exif:UserComment=" + exifFields[7].getText().trim());
        }
        if (exifBoxes[8].isSelected()) {
            cmdparams.add("-exif:ImageDescription=" + Description.getText().trim());
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

        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);

    }
}
