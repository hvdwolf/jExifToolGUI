package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static org.hvdw.jexiftoolgui.Utils.in_Range;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;


public class EditGPSdata {

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditGPSdata.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    // I had specified for the arrays:
    //textfields:  gpsLatDecimaltextField, gpsLonDecimaltextField, gpsAltDecimaltextField, gpsLocationtextField, gpsCountrytextField, gpsStateProvincetextField, gpsCitytextField
    //checkboxes:  SaveLatLonAltcheckBox, gpsAboveSealevelcheckBox, gpsLocationcheckBox, gpsCountrycheckBox, gpsStateProvincecheckBox, gpsCitycheckBox, gpsBackupOriginalscheckBox

    public void setFormattedFieldMasks(JFormattedTextField[] gpsNumdecFields, JFormattedTextField[] gpsCalcFields) {
        //Always use the US decimal formtter .
        NumberFormat latformatter = NumberFormat.getNumberInstance(Locale.US );
        // Latitude 0-90
        latformatter.setMaximumIntegerDigits(2);
        latformatter.setMaximumFractionDigits(8);
        gpsNumdecFields[0].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(latformatter)));
        NumberFormat lonformatter = NumberFormat.getNumberInstance(Locale.US );
        // Longitude 0-180
        lonformatter.setMaximumIntegerDigits(3);
        lonformatter.setMaximumFractionDigits(8);
        gpsNumdecFields[1].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(lonformatter)));
        //Altitude
        NumberFormat altformatter = NumberFormat.getNumberInstance(Locale.US );
        altformatter.setMaximumIntegerDigits(5);
        altformatter.setMaximumFractionDigits(2);
        gpsNumdecFields[2].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(altformatter)));
        //GPS GMS fields
        // Deg (lat) and min are 2 digits integer
        NumberFormat degminformatter = NumberFormat.getNumberInstance(Locale.US );
        degminformatter.setMaximumIntegerDigits(2);
        degminformatter.setMaximumFractionDigits(0);
        //{CalcLatDegtextField, CalcLatMintextField, CalcLatSectextField, CalcLonDegtextField, CalcLonMintextField, CalcLonSectextField};
        gpsCalcFields[0].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(degminformatter)));
        gpsCalcFields[1].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(degminformatter)));
        gpsCalcFields[4].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(degminformatter)));
        // Deg lon is 3 digit integer
        NumberFormat londegformatter = NumberFormat.getNumberInstance(Locale.US );
        londegformatter.setMaximumIntegerDigits(3);
        londegformatter.setMaximumFractionDigits(0);
        gpsCalcFields[3].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(londegformatter)));
        // secs we make 2 digit, 3 decimals floats
        NumberFormat secformatter = NumberFormat.getNumberInstance(Locale.US );
        secformatter.setMaximumIntegerDigits(2);
        secformatter.setMaximumFractionDigits(3);
        gpsCalcFields[2].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(secformatter)));
        gpsCalcFields[5].setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(secformatter)));
    }


    public void resetFields(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields) {

        for (JFormattedTextField field: gpsNumdecFields) {
            field.setText("");
        }
        for (JTextField field: gpsLocationFields) {
            field.setText("");
        }
    }

    public void copyGPSFromSelected(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JCheckBox[] gpsBoxes) {
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        // Use "-n" for numerical values, like GPSAltitudeRef 0/1, instead of Above Sea Level/Below Sea Level
        String[] gpscopyparams = {"-e","-n","-exif:GPSLatitude","-exif:GPSLongitude","-exif:GPSAltitude","-exif:GPSAltitudeRef","-xmp:Location","-xmp:Country","-xmp:State","-xmp:City"};
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(gpsNumdecFields, gpsLocationFields);

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
            logger.info("res is\n{}", res);
        } catch(IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            displayCopiedInfo(gpsNumdecFields, gpsLocationFields, gpsBoxes, res);
        }
    }

    public void displayCopiedInfo(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JCheckBox[] gpsBoxes, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //Wit ALL spaces removed from the tag we als need to use identiefiers without spaces
            logger.info(SpaceStripped + "; value: " + cells[1], "\n");
            if (SpaceStripped.contains("Latitude")) {
                gpsNumdecFields[0].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("Longitude")) {
                gpsNumdecFields[1].setText(cells[1].trim());
            }
            if ("GPSAltitude".equals(SpaceStripped)) {
                gpsNumdecFields[2].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("AltitudeRef")) {
                if (cells[1].contains("0")) {
                    gpsBoxes[1].setSelected(true);
                } else {
                    gpsBoxes[1].setSelected(false);
                }
            }
            if (SpaceStripped.contains("Location")) {
                gpsLocationFields[0].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("Country")) {
                gpsLocationFields[1].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("State")) {
                gpsLocationFields[2].setText(cells[1].trim());
            }
            if (SpaceStripped.contains("City")) {
                gpsLocationFields[3].setText(cells[1].trim());
            }
        }

    }


    public void writeGPSTags(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JCheckBox[] gpsBoxes, JFormattedTextField[] gpsDMSFields, JRadioButton[] gpSdmsradiobuttons, JProgressBar progressBar, int selectedTabIndex, JPanel rootPanel) {

        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        List<String> cmdparams = new ArrayList<String>();
        boolean valuescorrect = true;

        cmdparams.add(Utils.platformExiftool());
        boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
        if (preserveModifyDate) {
            cmdparams.add("-preserve");
        }
        if (!gpsBoxes[6].isSelected()) { // default overwrite originals, when set do not
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());


        if (gpsBoxes[0].isSelected()) { // LatLonAlt
            if (selectedTabIndex == 0) { // This means the decimal format tab
                // Exiftool prefers to only set one tag (exif or xmp) and retrieve with composite,
                // but I prefer to set both to satisfy every user
                //check for correct numbers, no trimming in case of empty field. trim in method
                String checklat = checkValue(gpsNumdecFields[0].getText(), "double", 90);
                String checklon = checkValue(gpsNumdecFields[1].getText(), "double", 180);
                if ("incorrect".equals(checklat) || "incorrect".equals(checklon)) {
                    // Values to high or too low
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("translations/program_strings").getString("gps.declatlon")), ResourceBundle.getBundle("translations/program_strings").getString("gps.inputval"), JOptionPane.ERROR_MESSAGE);
                    valuescorrect = false;
                } else {
                    cmdparams.add("-exif:GPSLatitude=" + checklat);
                    if (Float.parseFloat(checklat) > 0) {
                        cmdparams.add("-exif:GPSLatitudeREF=N");
                    } else {
                        cmdparams.add("-exif:GPSLatitudeREF=S");
                    }
                    cmdparams.add("-exif:GPSLongitude=" + checklon);
                    if (Float.parseFloat(checklon) > 0) {
                        cmdparams.add("-exif:GPSLongitudeREF=E");
                    } else {
                        cmdparams.add("-exif:GPSLongitudeREF=W");
                    }
                    if ( !(gpsNumdecFields[2].getText() == null) && !gpsNumdecFields[2].getText().trim().isEmpty()) {
                        String alt = checkValue(gpsNumdecFields[2].getText(), "double", 100000);
                        if ("incorrect".equals(alt)) {
                            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("translations/program_strings").getString("gps.alterror")), ResourceBundle.getBundle("translations/program_strings").getString("gps.inputval"), JOptionPane.ERROR_MESSAGE);
                            valuescorrect = false;
                        } else {
                            cmdparams.add("-exif:GPSAltitude=" + gpsNumdecFields[2].getText().trim());
                            cmdparams.add("-xmp:GPSLatitude=" + checklat);
                            cmdparams.add("-xmp:GPSLongitude=" + checklon);
                            cmdparams.add("-xmp:GPSAltitude=" + gpsNumdecFields[2].getText().trim());
                            if (gpsBoxes[1].isSelected()) { //Altitude positive
                                cmdparams.add("-exif:GPSAltitudeREF=above");
                            } else {
                                cmdparams.add("-exif:GPSAltitudeREF=below");
                            }
                        }
                    }
                }
            } else { //This means the deg-min-sec format tab
                // First check values
                String latdeg = checkValue(gpsDMSFields[0].getText().trim(), "int", 90);
                String latmin = checkValue(gpsDMSFields[1].getText().trim(), "int", 60);
                String latsec = checkValue(gpsDMSFields[2].getText().trim(), "int", 60);
                String londeg = checkValue(gpsDMSFields[3].getText().trim(), "int", 180);
                String lonmin = checkValue(gpsDMSFields[4].getText().trim(), "int", 60);
                String lonsec = checkValue(gpsDMSFields[5].getText().trim(), "int", 60);
                if ("incorrect".equals(latdeg) || "incorrect".equals(latmin) || "incorrect".equals(latsec) || "incorrect".equals(londeg) || "incorrect".equals(lonmin) || "incorrect".equals(lonsec)) {
                    // Values to high or too low
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("translations/program_strings").getString("gps.dmserror")), ResourceBundle.getBundle("translations/program_strings").getString("gps.inputval"), JOptionPane.ERROR_MESSAGE);
                    valuescorrect = false;
                } else {
                    // all values correct
                    cmdparams.add("-exif:GPSLatitude=\"" + gpsDMSFields[0].getText().trim() + " " + gpsDMSFields[1].getText().trim() + " " + gpsDMSFields[2].getText().trim() + "\"");
                    logger.info("exif lat {}", "-exif:GPSLatitude=\"" + gpsDMSFields[0].getText().trim() + " " + gpsDMSFields[1].getText().trim() + " " + gpsDMSFields[2].getText().trim() + "\"");
                    cmdparams.add("-xmp:GPSLatitude=\"" + gpsDMSFields[0].getText().trim() + " " + gpsDMSFields[1].getText().trim() + " " + gpsDMSFields[2].getText().trim() + "\"");
                    if (gpSdmsradiobuttons[0].isSelected()) {
                        cmdparams.add("-exif:GPSLatitudeREF=N");
                    } else {
                        cmdparams.add("-exif:GPSLatitudeREF=S");
                    }
                    cmdparams.add("-exif:GPSLongitude=\"" + gpsDMSFields[3].getText().trim() + " " + gpsDMSFields[4].getText().trim() + " " + gpsDMSFields[5].getText().trim() + "\"");
                    logger.info("exif gpslongitude {}", "-exif:GPSLongitude=\"" + gpsDMSFields[3].getText().trim() + " " + gpsDMSFields[4].getText().trim() + " " + gpsDMSFields[5].getText().trim() + "\"");
                    cmdparams.add("-xmp:GPSLongitude=\"" + gpsDMSFields[3].getText().trim() + " " + gpsDMSFields[4].getText().trim() + " " + gpsDMSFields[5].getText().trim() + "\"");
                    if (gpSdmsradiobuttons[2].isSelected()) {
                        cmdparams.add("-exif:GPSLongitudeREF=E");
                    } else {
                        cmdparams.add("-exif:GPSLongitudeREF=W");
                    }
                }
            }
        }
        // Again: exiftool prefers to only set one tag, but I set both
        if (gpsBoxes[2].isSelected()) {
            cmdparams.add("-xmp:Location=" + gpsLocationFields[0].getText().trim());
            cmdparams.add("-iptc:Sub-location=" + gpsLocationFields[0].getText().trim());
        }
        if (gpsBoxes[3].isSelected()) {
            cmdparams.add("-xmp:Country=" + gpsLocationFields[1].getText().trim());
            cmdparams.add("-iptc:Country-PrimaryLocationName=" + gpsLocationFields[1].getText().trim());
        }
        if (gpsBoxes[4].isSelected()) {
            cmdparams.add("-xmp:State=" + gpsLocationFields[2].getText().trim());
            cmdparams.add("-iptc:Province-State=" + gpsLocationFields[2].getText().trim());
        }
        if (gpsBoxes[5].isSelected()) {
            cmdparams.add("-xmp:City=" + gpsLocationFields[3].getText().trim());
            cmdparams.add("-iptc:City=" + gpsLocationFields[3].getText().trim());
        }


        boolean isWindows = Utils.isOsFromMicrosoft();
        for (int index: selectedIndices) {
            //logger.info("index: {}  image path: {}", index,  files[index].getPath());
            if (isWindows) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }
        if (valuescorrect) {
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
    }

    private String checkValue (String value, String int_or_double, int maxvalue) {
        String checked_value = "incorrect";

        if ("int".equals(int_or_double)) {
            // deg-min-sec integers are always positive
            if ( in_Range(Integer.parseInt(value), 0, maxvalue)) {
                checked_value = value;
            }
        } else if ( "float".equals(int_or_double) || "double".equals(int_or_double) ) {
            int minvalue = -maxvalue;
            double dbl = Double.parseDouble(value);
            int intvalue = (int) dbl;
            if ( in_Range(intvalue, minvalue, maxvalue)) {
                checked_value = value;
            }
        }

        return checked_value;
    }

    /**
     * Convert a decimal coordiante back to deg-min-sec for the deg-min-sec textfields
     * @param coordinate
     * @return
     */
    public static String[] decDegToDegMinSec(String coordinate) {
        String deg;
        double decdegrees = 0.0;
        double decminutes = 0.0;
        double decseconds = 0.0;

        //int intDeg = Integer.parseInt(coordinate);
        decdegrees = Double.parseDouble(coordinate);
        int intDeg = (int) decdegrees;
        decminutes = (decdegrees - intDeg) * 60;
        int intMin = (int) decminutes;
        decseconds = (decminutes - intMin) * 60;
        //logger.info("decdegrees {} intDeg {} decminutes {} intMin {} decseconds {}", String.valueOf(decdegrees), String.valueOf(intDeg), String.valueOf(decminutes), String.valueOf(intMin), String.valueOf(decseconds));

        NumberFormat numsecs = NumberFormat.getInstance(new Locale("en", "US" ));
        numsecs.setMaximumFractionDigits(2);
        String strSeconds = numsecs.format(decseconds);
        //logger.info("strSeconds {}", strSeconds);
        String[] dmscoordinate = { String.valueOf(intDeg), String.valueOf(intMin), strSeconds};

        return dmscoordinate;
    }
}
