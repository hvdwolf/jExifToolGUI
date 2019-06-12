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

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;


public class EditGPSdata {

    private final static Logger logger = LoggerFactory.getLogger(EditGPSdata.class);
    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    // I had specified for the arrays:
    //textfields:  gpsLatDecimaltextField, gpsLonDecimaltextField, gpsAltDecimaltextField, gpsLocationtextField, gpsCountrytextField, gpsStateProvincetextField, gpsCitytextField
    //checkboxes:  SaveLatLonAltcheckBox, gpsAboveSealevelcheckBox, gpsLocationcheckBox, gpsCountrycheckBox, gpsStateProvincecheckBox, gpsCitycheckBox, gpsBackupOriginalscheckBox

    public void resetFields(JTextField[] gpsFields) {

        for (JTextField field: gpsFields) {
            field.setText("");
        }
    }

    public void copyGPSFromSelected(JTextField[] gpsFields, JCheckBox[] gpsBoxes) {
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        // Use "-n" for numerical values, like GPSAltitudeRef 0/1, instead of Above Sea Level/Below Sea Level
        String[] gpscopyparams = {"-e","-n","-exif:GPSLatitude","-exif:GPSLongitude","-exif:GPSAltitude","-exif:GPSAltitudeRef","-xmp:Location","-xmp:Country","-xmp:State","-xmp:City"};
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(gpsFields);

        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        cmdparams.add(Utils.platformExiftool());
        cmdparams.addAll( Arrays.asList(gpscopyparams));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            logger.debug("res is\n{}", res);
        } catch(IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(gpsFields, gpsBoxes, res);
        }
    }

    public void displayCopiedInfo(JTextField[] gpsFields, JCheckBox[] gpsBoxes, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //Wit ALL spaces removed from the tag we als need to use identiefiers without spaces
            logger.debug(SpaceStripped, " ; value: ", cells[1], "\n");
            if (SpaceStripped.contains("Latitude")) {
                gpsFields[0].setText(cells[1]);
            }
            if (SpaceStripped.contains("Longitude")) {
                gpsFields[1].setText(cells[1]);
            }
            if (SpaceStripped.contains("Altitude")) {
                gpsFields[2].setText(cells[1]);
            }
            if (SpaceStripped.contains("AltitudeRef")) {
                if (cells[1].contains("0")) {
                    gpsBoxes[1].setSelected(true);
                } else {
                    gpsBoxes[1].setSelected(false);
                }
            }
            if (SpaceStripped.contains("Location")) {
                gpsFields[3].setText(cells[1]);
            }
            if (SpaceStripped.contains("Country")) {
                gpsFields[4].setText(cells[1]);
            }
            if (SpaceStripped.contains("State")) {
                gpsFields[5].setText(cells[1]);
            }
            if (SpaceStripped.contains("City")) {
                gpsFields[6].setText(cells[1]);
            }
        }

    }


    public void writeGPSTags(JTextField[] gpsFields, JCheckBox[] gpsBoxes, JProgressBar progressBar) {

        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();
        List<String> cmdparams = new ArrayList<String>();

        cmdparams.add(Utils.platformExiftool());
        if (!gpsBoxes[6].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }

        if (gpsBoxes[0].isSelected()) { // LatLonAlt
            // Exiftool prefers to only set one tag (exif or xmp) and retrieve with composite,
            // but I prefer to set both to satisfy every user
            cmdparams.add("-exif:GPSLatitude=" + gpsFields[0].getText().trim());
            cmdparams.add("-exif:GPSLongitude=" + gpsFields[1].getText().trim());
            cmdparams.add("-exif:GPSAltitude=" + gpsFields[2].getText().trim());
            cmdparams.add("-xmp:GPSLatitude=" + gpsFields[0].getText().trim());
            cmdparams.add("-xmp:GPSLongitude=" + gpsFields[1].getText().trim());
            cmdparams.add("-xmp:GPSAltitude=" + gpsFields[2].getText().trim());
            if (gpsBoxes[1].isSelected()) { //Altitude positive
                cmdparams.add("-exif:GPSAltitudeREF=above");
            } else {
                cmdparams.add("-exif:GPSAltitudeREF=below");
            }
        }
        // Again: exiftool prefers to only set one tag, but I set both
        if (gpsBoxes[2].isSelected()) {
            cmdparams.add("-xmp:Location=" + gpsFields[3].getText().trim());
            cmdparams.add("-iptc:Sub-location=" + gpsFields[3].getText().trim());
        }
        if (gpsBoxes[3].isSelected()) {
            cmdparams.add("-xmp:Country=" + gpsFields[4].getText().trim());
            cmdparams.add("-iptc:Country-PrimaryLocationName=" + gpsFields[4].getText().trim());
        }
        if (gpsBoxes[4].isSelected()) {
            cmdparams.add("-xmp:State=" + gpsFields[5].getText().trim());
            cmdparams.add("-iptc:Province-State=" + gpsFields[5].getText().trim());
        }
        if (gpsBoxes[5].isSelected()) {
            cmdparams.add("-xmp:City=" + gpsFields[6].getText().trim());
            cmdparams.add("-iptc:City=" + gpsFields[6].getText().trim());
        }


        boolean isWindows = Utils.isOsFromMicrosoft();
        for (int index: selectedIndices) {
            //logger.debug("index: {}  image path: {}", index,  files[index].getPath());
            if (isWindows) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }


        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);


    }
}
