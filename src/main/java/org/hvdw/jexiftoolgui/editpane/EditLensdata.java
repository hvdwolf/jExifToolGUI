package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.CreateUpdatemyLens;
import org.hvdw.jexiftoolgui.view.SelectmyLens;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;


public class EditLensdata {

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditLensdata.class);

    private SelectmyLens SmL = new SelectmyLens();
    private CreateUpdatemyLens CUL = new CreateUpdatemyLens();

    public void resetFields(JTextField[] lensFields, JCheckBox[] lensBoxes, JComboBox meteringmodecomboBox) {

        for (JTextField field: lensFields) {
            field.setText("");
        }
        for (JCheckBox checkbox: lensBoxes) {
            checkbox.setSelected(true);
        }
        meteringmodecomboBox.setSelectedItem("Unknown");
    }


    public void copyLensDataFromSelected(JTextField[] lensFields, JComboBox meteringmodecomboBox, JCheckBox[] lensBoxes) {
        String[] lenscopyparams = {"-exif:lensmake","-exif:lensmodel","-exif:lensserialnumber","-makernotes:lensserialnumber","-exif:focallength","-exif:focallengthin35mmformat","-exif:fnumber","-exif:maxaperturevalue","-exif:meteringmode","-makernotes:focusdistance","-composite:lensid","-composite:lens","-makernotes:focusdistance","-makernotes:conversionlens","-makernotes:lenstype","-makernotes:lensfirmwareversion"};
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRowOrIndex();
        String fpath = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(lensFields, lensBoxes, meteringmodecomboBox);

        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
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

    public void displayCopiedInfo(JTextField[] lensFields, JCheckBox[] lensBoxes, JComboBox meteringmodecomboBox, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //With ALL spaces removed from the tag we als need to use identifiers without spaces
            //logger.info("SpaceStripped: " + SpaceStripped);
            if (SpaceStripped.contains("Make")) {
                lensFields[0].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("Model")) {
                lensFields[1].setText(cells[1].trim());
            }
            if (SpaceStripped.toLowerCase().contains("serialnumber")) {
                lensFields[2].setText(cells[1].trim());
            }
            if ("FocalLength".equals(SpaceStripped)) {
                lensFields[3].setText(cells[1].replace(" mm","").trim());
            }
            if (SpaceStripped.toLowerCase().contains("focallengthin35mmformat")) { // FocalLengthIn35mmFormat
                lensFields[4].setText(cells[1].replace(" mm","").trim());
            }
            if (SpaceStripped.contains("FNumber")) {
                lensFields[5].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("MaxApertureValue")) {
                lensFields[6].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("FocusDistance")) {
                lensFields[7].setText(cells[1].trim());
            }
            if (SpaceStripped.toLowerCase().contains("id")) {
                lensFields[8].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("ConversionLens")) {
                lensFields[9].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("LensType")) {
                lensFields[10].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("LensFirmwareVersion")) {
                lensFields[11].setText(cells[1].trim());
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
                //meteringmodecomboBox.setSelectedItem(cells[1].trim());
                //logger.info("metering mode (SpaceStripped, cells[1].trim()) " + SpaceStripped + " " + cells[1].trim());
            }
        }

    }

    public void writeLensTags(JTextField[] lensFields, JCheckBox[] lensBoxes, JComboBox meteringmodecomboBox, JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        cmdparams.add(Utils.platformExiftool());
        boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
        if (preserveModifyDate) {
            cmdparams.add("-preserve");
        }
        if (!lensBoxes[13].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());

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
            cmdparams.add("-makernotes:focusdistance=" + lensFields[7].getText().trim());
        }
        if (lensBoxes[8].isSelected()) {
            cmdparams.add("-composite:lensid=" + lensFields[8].getText().trim());
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
        if (lensBoxes[12].isSelected()) {
            cmdparams.add("-exif:meteringmode=" + meteringmodecomboBox.getSelectedItem());
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


    public void saveLensconfig(JTextField[] LensFields, JComboBox meteringmodecomboBox, JPanel rootpanel) {
        String sql = "";
        String queryresult = "";
        String writeresult = "";
        HashMap<String, String> lens = new HashMap<String, String>();

        String[] chosenValues = CUL.showDialog(rootpanel);
        // Put all our values into a HashMap
        lens.put("lens_name", chosenValues[0]);
        lens.put("lens_description", chosenValues[1]);
        lens.put("exif_lensmake", LensFields[0].getText());
        lens.put("exif_lensmodel", LensFields[1].getText());
        lens.put("exif_lensserialnumber", LensFields[2].getText());
        lens.put("exif_focallength", LensFields[3].getText());
        lens.put("exif_focallengthIn35mmformat", LensFields[4].getText());
        lens.put("exif_fnumber", LensFields[5].getText());
        lens.put("exif_maxaperturevalue", LensFields[6].getText());
        lens.put("makernotes_focusdistance", LensFields[7].getText());
        lens.put("composite_lensid", LensFields[7].getText());
        lens.put("makernotes_conversionlens", LensFields[9].getText());
        lens.put("makernotes_lenstype", LensFields[10].getText());
        lens.put("makernotes_lensfirmwareversion", LensFields[11].getText());
        lens.put("exif_meteringmode", (String) meteringmodecomboBox.getSelectedItem());

        logger.debug("chosen name/description: " + chosenValues[0] + " + " + chosenValues[1]);
        if (!"".equals(chosenValues[0])) { // So the user provided a name and not an empty string
            // Check if already exists
            String str_lens_file = MyVariables.getlensFolder() +  File.separator + chosenValues[0] + ".hashmap";
            logger.info("str_lens_file {}", str_lens_file);
            File lens_file = new File(str_lens_file);

            if (lens_file.exists()) { // so we have a existing lens file but do we want to overwite it?
                int result = JOptionPane.showConfirmDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.overwrite") + " " + chosenValues[0] + "?",
                        ResourceBundle.getBundle("translations/program_strings").getString("addlens.overwriteshort"), JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) { //OK
                    // user wants us to overwrite
                    logger.info("user wants to overwrite the lens file with name: " + chosenValues[0]);
                    writeresult = StandardFileIO.writeHashMapToFile(lens_file, lens);

                    if ("Error saving".contains(writeresult)) { //means we have an error saving the hashmap file
                        JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.updateerror") + chosenValues[0], ResourceBundle.getBundle("translations/program_strings").getString("addlens.updateerrorshort"), JOptionPane.ERROR_MESSAGE);
                    } else { //success
                        JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.saved") + " " + chosenValues[0], ResourceBundle.getBundle("translations/program_strings").getString("addlens.savedshort"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } // result 2 means cancel; do nothing
            } else {// lens file does not exist yet, so we can simply write it out.
                logger.info("save new lens file named: " + chosenValues[0]);

                writeresult = StandardFileIO.writeHashMapToFile(lens_file, lens);
                if ("Error saving".contains(writeresult)) { //means we have an error
                    JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.inserterror") + " " + chosenValues[0], ResourceBundle.getBundle("translations/program_strings").getString("addlens.inserterrorshort"), JOptionPane.ERROR_MESSAGE);
                } else { //success
                    JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.saved") + " " + chosenValues[0], ResourceBundle.getBundle("translations/program_strings").getString("addlens.savedshort"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else { // user did not provide a lensname to insert/update
            JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("addlens.noname"), ResourceBundle.getBundle("translations/program_strings").getString("addlens.nonameshort"), JOptionPane.ERROR_MESSAGE);
        }

    }


    /*
    public void loadLensconfig(JTextField[] lensFields, JComboBox meteringmodecomboBox, JPanel rootpanel) {
        String queryresult = "";
        String lensname = SmL.showDialog(rootpanel, "load lens");
        logger.debug("returned selected lensname: " + lensname);

        if (!"".equals(lensname)) {
            String sql = "select exif_lensmake, exif_lensmodel, exif_lensserialnumber, exif_focallength, "
                    +"exif_focallengthIn35mmformat, exif_fnumber, exif_maxaperturevalue, makernotes_focusdistance, "
                    +"composite_lensid, makernotes_conversionlens, makernotes_lenstype, makernotes_lensfirmwareversion, exif_meteringmode "
                    +"from myLenses where lens_name ='" + lensname.trim() + "'";
            queryresult = SQLiteJDBC.generalQuery(sql, "disk");
            logger.debug("returned lens: " + queryresult);
            for (JTextField field: lensFields) {
                field.setText("");
            }
            String[] cells = queryresult.split("\\t");
            int noOfFields= cells.length;
            int counter = 0;
            // Fill the textfields from our query result
            for (String cell : cells) {
                if ( (noOfFields) > (counter+1)) {
                    lensFields[counter].setText(cell);
                    counter++;
                } else {
                    if (cell.toLowerCase().contains("unknown")) {
                        meteringmodecomboBox.setSelectedItem("Unknown");
                    }
                    if (cell.toLowerCase().equals("average")) {
                        meteringmodecomboBox.setSelectedItem("Average");
                    }
                    if (cell.toLowerCase().equals("Spot")) {
                        meteringmodecomboBox.setSelectedItem("Spot");
                    }
                    if (cell.toLowerCase().contains("multi-spot")) {
                        meteringmodecomboBox.setSelectedItem("Multi-spot");
                    }
                    if (cell.toLowerCase().contains("weighted")) {
                        meteringmodecomboBox.setSelectedItem("Center-weighted average");
                    }
                    if (cell.toLowerCase().contains("segment")) {
                        meteringmodecomboBox.setSelectedItem("Multi-segment");
                    }
                    if (cell.toLowerCase().contains("partial")) {
                        meteringmodecomboBox.setSelectedItem("Partial");
                    }
                    if (cell.toLowerCase().contains("other")) {
                        meteringmodecomboBox.setSelectedItem("Other");
                    }
                }
            }
        }
    } */
    public void loadLensconfig(JTextField[] lensFields, JComboBox meteringmodecomboBox, JPanel rootpanel) {
        String queryresult = "";
        String lensname = SmL.showDialog(rootpanel, "load lens");
        logger.debug("returned selected lensname: " + lensname);

        if (!"".equals(lensname)) {
            String sql = "select exif_lensmake, exif_lensmodel, exif_lensserialnumber, exif_focallength, "
                    +"exif_focallengthIn35mmformat, exif_fnumber, exif_maxaperturevalue, makernotes_focusdistance, "
                    +"composite_lensid, makernotes_conversionlens, makernotes_lenstype, makernotes_lensfirmwareversion, exif_meteringmode "
                    +"from myLenses where lens_name ='" + lensname.trim() + "'";
            queryresult = SQLiteJDBC.generalQuery(sql, "disk");
            logger.debug("returned lens: " + queryresult);
            for (JTextField field: lensFields) {
                field.setText("");
            }
            String[] cells = queryresult.split("\\t");
            int noOfFields= cells.length;
            int counter = 0;
            // Fill the textfields from our query result
            for (String cell : cells) {
                if ( (noOfFields) > (counter+1)) {
                    lensFields[counter].setText(cell);
                    counter++;
                } else {
                    if (cell.toLowerCase().contains("unknown")) {
                        meteringmodecomboBox.setSelectedItem("Unknown");
                    }
                    if (cell.toLowerCase().equals("average")) {
                        meteringmodecomboBox.setSelectedItem("Average");
                    }
                    if (cell.toLowerCase().equals("Spot")) {
                        meteringmodecomboBox.setSelectedItem("Spot");
                    }
                    if (cell.toLowerCase().contains("multi-spot")) {
                        meteringmodecomboBox.setSelectedItem("Multi-spot");
                    }
                    if (cell.toLowerCase().contains("weighted")) {
                        meteringmodecomboBox.setSelectedItem("Center-weighted average");
                    }
                    if (cell.toLowerCase().contains("segment")) {
                        meteringmodecomboBox.setSelectedItem("Multi-segment");
                    }
                    if (cell.toLowerCase().contains("partial")) {
                        meteringmodecomboBox.setSelectedItem("Partial");
                    }
                    if (cell.toLowerCase().contains("other")) {
                        meteringmodecomboBox.setSelectedItem("Other");
                    }
                }
            }
        }
    }

}
