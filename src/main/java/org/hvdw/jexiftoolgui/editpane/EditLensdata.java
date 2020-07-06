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
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;


public class EditLensdata {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(EditLensdata.class);

    // I had specified for the arrays:
    //JTextField[] lensFields = {xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField};
    //    JTextArea[] xmpAreas = {xmpDescriptiontextArea};
    //    JCheckBox[] lensBoxes = {xmpCreatorcheckBox, xmpRightscheckBox, xmpDescriptioncheckBox, xmpLabelcheckBox, xmpSubjectcheckBox, xmpTitlecheckBox, xmpPersoncheckBox};

    public void resetFields(JTextField[] lensFields, JCheckBox[] lensBoxes) {

        for (JTextField field: lensFields) {
            field.setText("");
        }
        for (JCheckBox checkbox: lensBoxes) {
            checkbox.setSelected(true);
        }

    }


    public void copyLensDataFromSelected(JTextField[] lensFields, JComboBox meteringmodecomboBox, JCheckBox[] lensBoxes) {
        String[] lenscopyparams = {"-exif:lensmake","-exif:lensmodel","-exif:lensserialnumber","-makernotes:lensserialnumber","-exif:focallength","-exif:focallengthin35mmformat","-exif:fnumber","-exif:maxaperturevalue","-exif:meteringmode","-composite:lensid","-composite:lens","-makernotes:focusdistance","-makernotes:conversionlens","-makernotes:lenstype","-makernotes:lensfirmwareversion"};
        File[] files = MyVariables.getSelectedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        String fpath = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(lensFields, lensBoxes);

        String exiftool = prefs.getByKey(EXIFTOOL_PATH, "");
        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
            exiftool = exiftool.replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        logger.info(fpath);
        cmdparams.add(Utils.platformExiftool());
        cmdparams.addAll(Arrays.asList(lenscopyparams));
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            logger.info("res is\n{}", res);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(lensFields, lensBoxes, meteringmodecomboBox, res);
        }
    }

        public void displayCopiedInfo(JTextField[] lensFields, JCheckBox[] lesnBoxes, JComboBox meteringmodecomboBox, String exiftoolInfo) {
            String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            for (String line : lines) {
                String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
                //With ALL spaces removed from the tag we als need to use identifiers without spaces
                //logger.info("SpaceStripped: " + SpaceStripped);
                if (SpaceStripped.contains("Make")) {
                    lensFields[0].setText(cells[1]);
                }
                if (SpaceStripped.contains("Model")) {
                    lensFields[1].setText(cells[1]);
                }
                if (SpaceStripped.toLowerCase().contains("serialnumber")) {
                    lensFields[2].setText(cells[1]);
                }
                if ("FocalLength".equals(SpaceStripped)) {
                    lensFields[3].setText(cells[1].replace(" mm",""));
                }
                if (SpaceStripped.toLowerCase().contains("focallengthin35mmformat")) { // FocalLengthIn35mmFormat
                    lensFields[4].setText(cells[1].replace(" mm",""));
                }
                if (SpaceStripped.contains("FNumber")) {
                    lensFields[5].setText(cells[1]);
                }
                if (SpaceStripped.contains("MaxApertureValue")) {
                    lensFields[6].setText(cells[1]);
                }
                if (SpaceStripped.contains("FocusDistance")) {
                    lensFields[7].setText(cells[1]);
                }
                if (SpaceStripped.toLowerCase().contains("id")) {
                    lensFields[8].setText(cells[1]);
                }
                if (SpaceStripped.contains("ConversionLens")) {
                    lensFields[9].setText(cells[1]);
                }
                if (SpaceStripped.contains("LensType")) {
                    lensFields[10].setText(cells[1]);
                }
                if (SpaceStripped.contains("LensFirmwareVersion")) {
                    lensFields[11].setText(cells[1]);
                }
                if (SpaceStripped.contains("MeteringMode")) {
                    // simple set of if statements. somehow the direct cells[1] option does not work
                    if (cells[1].toLowerCase().contains("unknown")) {
                        meteringmodecomboBox.setSelectedItem("Unknown");
                    }
                    if (cells[1].toLowerCase().equals("average")) {
                        meteringmodecomboBox.setSelectedItem("Average");
                    }
                    if (cells[1].toLowerCase().equals("Spot")) {
                        meteringmodecomboBox.setSelectedItem("Spot");
                    }
                    if (cells[1].toLowerCase().contains("multi-spot")) {
                        meteringmodecomboBox.setSelectedItem("Multi-spot");
                    }
                    if (cells[1].toLowerCase().contains("weighted")) {
                        meteringmodecomboBox.setSelectedItem("Center-weighted average");
                    }
                    if (cells[1].toLowerCase().contains("segment")) {
                        meteringmodecomboBox.setSelectedItem("Multi-segment");
                    }
                    if (cells[1].toLowerCase().contains("partial")) {
                        meteringmodecomboBox.setSelectedItem("Partial");
                    }
                    if (cells[1].toLowerCase().contains("other")) {
                        meteringmodecomboBox.setSelectedItem("Other");
                    }
                    //meteringmodecomboBox.setSelectedItem(cells[1]);
                    logger.info("metering mode (SpaceStripped, cells[1]) " + SpaceStripped + " " + cells[1]);
                }
            }

        }
        
        public void writeLensTags(JTextField[] lensFields, JCheckBox[] lensBoxes, JComboBox meteringmodecomboBox, JProgressBar progressBar) {
            List<String> cmdparams = new ArrayList<String>();
            int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
            File[] files = MyVariables.getSelectedFiles();

            cmdparams.add(Utils.platformExiftool());
            if (!lensBoxes[13].isSelected()) { // default overwrite originals, when set do not
                cmdparams.add("-overwrite_original");
            }
            if (lensBoxes[0].isSelected()) {
                cmdparams.add("-exif:lensmake=" + lensFields[0].getText().trim());
            }
            if (lensBoxes[1].isSelected()) {
                cmdparams.add("-exif:lensmodel=" + lensFields[1].getText().trim());
            }
            if (lensBoxes[2].isSelected()) {
                cmdparams.add("-exif:lensserialnumber=" + lensFields[2].getText().trim());
                cmdparams.add("-makernotes:lensserialnumber=" + lensFields[2].getText().trim());
            }
            if (lensBoxes[3].isSelected()) {
                cmdparams.add("-exif:focallength=" + lensFields[3].getText().trim());
            }
            if (lensBoxes[4].isSelected()) {
                cmdparams.add("-exif:focallengthIn35mmformat=" + lensFields[4].getText().trim());
            }
            if (lensBoxes[5].isSelected()) {
                cmdparams.add("-exif:fnumber=" + lensFields[5].getText().trim());
            }	
            if (lensBoxes[6].isSelected()) {
                cmdparams.add("-exif:maxaperturevalue=" + lensFields[6].getText());
            }
            if (lensBoxes[7].isSelected()) {
                cmdparams.add("-composite:lensid=" + lensFields[7].getText().trim());
            }
            if (lensBoxes[8].isSelected()) {
                cmdparams.add("-makernotes:focusdistance=" + lensFields[8].getText().trim());
            }
            if (lensBoxes[9].isSelected()) {
                cmdparams.add("-makernotes:conversionlens=" + lensFields[9].getText().trim());
            }
            if (lensBoxes[10].isSelected()) {
                cmdparams.add("-makernotes:lenstype=" + lensFields[10].getText().trim());
            }
            if (lensBoxes[11].isSelected()) {
                cmdparams.add("-makernotes:lensfirmwareversion=" + lensFields[11].getText().trim());
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

        public void saveLensconfig(JTextField[] LensFields, JComboBox meteringmodecomboBox) {

        }

        public void loadLensconfig() {

        }
}
