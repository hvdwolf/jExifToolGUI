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


    public void resetFields(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JFormattedTextField[] GPSdmsFields) {

        for (JFormattedTextField field: gpsNumdecFields) {
            field.setText("");
        }
        for (JTextField field: gpsLocationFields) {
            field.setText("");
        }
        for (JFormattedTextField field: GPSdmsFields) {
            field.setText("");
        }
    }

    public void copyGPSFromSelected(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JCheckBox[] gpsBoxes, JFormattedTextField[] GPSdmsFields, JRadioButton[] GPSdmsradiobuttons) {
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRow();
        // Use "-n" for numerical values, like GPSAltitudeRef 0/1, instead of Above Sea Level/Below Sea Level
        String[] gpscopyparams = {"-e","-n","-s","-exif:GPSLatitude","-exif:GPSLatitudeRef","-exif:GPSLongitude","-exif:GPSLongitudeRef","-exif:GPSAltitude","-exif:GPSAltitudeRef","-xmp:Location","-xmp:Country","-xmp:State","-xmp:City"};
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        //First clean the fields
        resetFields(gpsNumdecFields, gpsLocationFields, GPSdmsFields);

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
            displayCopiedInfo(gpsNumdecFields, gpsLocationFields, gpsBoxes, GPSdmsFields, GPSdmsradiobuttons, res);
        }
    }

    public void displayCopiedInfo(JFormattedTextField[] gpsNumdecFields, JTextField[] gpsLocationFields, JCheckBox[] gpsBoxes, JFormattedTextField[] GPSdmsFields, JRadioButton[] GPSdmsradiobuttons, String exiftoolInfo) {
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        boolean latMin = false;
        boolean lonMin = false;
        for (String line : lines) {
            String[] cells = line.split(":", 2); // Only split on first : as some tags also contain (multiple) :
            String SpaceStripped = cells[0].replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \
            //With ALL spaces removed from the tag we als need to use identifiers without spaces
            logger.info(SpaceStripped + "; value: " + cells[1], "\n");
            if ("GPSLatitude".equals(SpaceStripped)) {
                gpsNumdecFields[0].setText(cells[1].trim());
                String[] dmsFields = decDegToDegMinSec(cells[1].trim());
                GPSdmsFields[0].setText(dmsFields[0]);
                GPSdmsFields[1].setText(dmsFields[1]);
                GPSdmsFields[2].setText(dmsFields[2]);
            }
            if (SpaceStripped.contains("GPSLatitudeRef")) {
                if ("S".equals(cells[1].trim())) {
                    // South means Negative
                    latMin = true;
                }
            }

            if ("GPSLongitude".equals(SpaceStripped)) {
                gpsNumdecFields[1].setText(cells[1].trim());
                String[] dmsFields = decDegToDegMinSec(cells[1].trim());
                GPSdmsFields[3].setText(dmsFields[0]);
                GPSdmsFields[4].setText(dmsFields[1]);
                GPSdmsFields[5].setText(dmsFields[2]);
            }
            if (SpaceStripped.contains("GPSLongitudeRef")) {
                if ("W".equals(cells[1].trim())) {
                    // West means Negative
                    lonMin = true;
                }
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
        // Do the final updates
        if (latMin) {
            gpsNumdecFields[0].setText("-" + gpsNumdecFields[0].getText());
            GPSdmsradiobuttons[1].setSelected(true);
        }
        if (lonMin) {
            gpsNumdecFields[1].setText("-" + gpsNumdecFields[1].getText());
            GPSdmsradiobuttons[3].setSelected(true);
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
        if (gpsBoxes[7].isSelected()) { // Use "-m" parameter to allow for longer IPTC fields
            cmdparams.add("-m");
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
                String latsec = checkValue(gpsDMSFields[2].getText().trim(), "double", 60);
                String londeg = checkValue(gpsDMSFields[3].getText().trim(), "int", 180);
                String lonmin = checkValue(gpsDMSFields[4].getText().trim(), "int", 60);
                String lonsec = checkValue(gpsDMSFields[5].getText().trim(), "double", 60);
                String NorS = "";
                String EorW = "";
                if (gpSdmsradiobuttons[0].isSelected()) {
                    NorS = "N";
                } else {
                    NorS =  "S";
                }
                if (gpSdmsradiobuttons[2].isSelected()) {
                    EorW = "E";
                } else {
                    EorW = "W";
                }
                if ("incorrect".equals(latdeg) || "incorrect".equals(latmin) || "incorrect".equals(latsec) || "incorrect".equals(londeg) || "incorrect".equals(lonmin) || "incorrect".equals(lonsec)) {
                    // Values to high or too low
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("translations/program_strings").getString("gps.dmserror")), ResourceBundle.getBundle("translations/program_strings").getString("gps.inputval"), JOptionPane.ERROR_MESSAGE);
                    valuescorrect = false;
                } else {
                    // all values correct
                    String[] lat_lon = null;
                    // For xmp values we still need the decimal version as we can't easily save it using the exiftool advised formats for xmp
                    try {
                        lat_lon = Utils.gpsCalculator(rootPanel, new String[]{latdeg, latmin, latsec, NorS, londeg, lonmin, lonsec, EorW});
                    } catch (ParseException e) {
                        logger.error("error converting deg-min-sec to decimal degrees {}", e.toString());
                        e.printStackTrace();
                    }
                    cmdparams.add("-exif:GPSLatitude=" + lat_lon[0]);
                    if (Float.parseFloat(lat_lon[0]) > 0) {
                        cmdparams.add("-exif:GPSLatitudeREF=N");
                    } else {
                        cmdparams.add("-exif:GPSLatitudeREF=S");
                    }
                    cmdparams.add("-exif:GPSLongitude=" + lat_lon[1]);
                    if (Float.parseFloat(lat_lon[1]) > 0) {
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
                            cmdparams.add("-xmp:GPSLatitude=" + lat_lon[0]);
                            cmdparams.add("-xmp:GPSLongitude=" + lat_lon[1]);
                            cmdparams.add("-xmp:GPSAltitude=" + gpsNumdecFields[2].getText().trim());
                            if (gpsBoxes[1].isSelected()) { //Altitude positive
                                cmdparams.add("-exif:GPSAltitudeREF=above");
                            } else {
                                cmdparams.add("-exif:GPSAltitudeREF=below");
                            }
                        }
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
        logger.debug("total cmdparams from GPS {}", cmdparams);
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
        if (coordinate.startsWith("-")) {
            coordinate = coordinate.substring(1);
        }
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
