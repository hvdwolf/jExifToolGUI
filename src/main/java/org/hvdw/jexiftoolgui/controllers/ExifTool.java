package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.Utils.getCurrentOsName;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;

public class ExifTool {

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExifTool.class);


    /*
     * This method tries to find exiftool in the path on the several OSes
     */
    public static String getExiftoolInPath() {
        String res;
        List<String> cmdparams;
        Application.OS_NAMES currentOs = getCurrentOsName();

        if (currentOs == Application.OS_NAMES.MICROSOFT) {
            //String[] params = {"c:\\Windows\\System32\\where.exe", "exiftool.exe"};
            String[] params = {"c:\\Windows\\System32\\cmd.exe", "/c", "where", "exiftool"};
            //String[] params = {"where", "exiftool"};
            cmdparams = Arrays.asList(params);
        } else {
            String[] params = {"which", "exiftool"};
            cmdparams = Arrays.asList(params);
        }

        try {
            res = CommandRunner.runCommand(cmdparams); // res returns path to exiftool; on error on windows "INFO: Could not ...", on linux returns nothing
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command. error {}", ex.toString());
            //res = ex.getMessage();
            res = "Error executing find command.";
        }

        return res;
    }

    /**
     * This method checkWindowPaths checks if:
     * the string does not contain multiple paths
     * and if exiftool is not in c:\windows or in c:\windows\System32
     * @param pathResult
     * @return String res with PATH or message
     */
    public static String checkWindowPaths (String pathResult) {
        String res = "";
        // First check whether we have multiple exiftool versions in our path
        String[] lines = pathResult.split(System.getProperty("line.separator"));
        logger.debug("lines is {}", Arrays.toString(lines));
        logger.info("line 0 is {}", lines[0]);
        logger.info("number of lines {}", Integer.toString(lines.length));
        for ( int i = 0; i<= lines.length; i++) {
            if ( (lines[i].toLowerCase()).contains("c:\\windows\\exiftool.exe") || (lines[i].toLowerCase()).contains("c:\\windows\\system32\\exiftool.exe") ) {
                res = "not allowed windows PATH";
            } else if ( (lines[i].toLowerCase()).contains("exiftool(-k).exe") ) {
                res = "exiftool -k version";
            } else {
                res = lines[i];
            }
        }

        return res;
    }

    /////////////////// Locate exiftool //////////////
    /*
     * File chooser to locate exiftool when user comes from checkExifTool
     * and selected "Specify Location"
     */
    public static String whereIsExiftool(JPanel myComponent) {
        String exiftool = "";
        String selectedBinary = "";

        boolean isWindows = Utils.isOsFromMicrosoft();

        final JFileChooser chooser = new JFileChooser();
        if (isWindows) {
            FileFilter filter = new FileNameExtensionFilter("(*.exe)", "exe");
            chooser.setFileFilter(filter);
        }
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("exift.dlgtitle"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            selectedBinary = chooser.getSelectedFile().getPath();
            String tmpstr = selectedBinary.toLowerCase();
            if (isWindows) {
                if (tmpstr.contains("exiftool.exe")) {
                    exiftool = selectedBinary;
                } else if (tmpstr.contains("exiftool(-k).exe")) {
                    logger.info("User tries to use exiftool(-k).exe. Ask to rename and try again");
                    //JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromStartup, ResourceBundle.getBundle("translations/program_strings").getString("exift.wrongexebin"), JOptionPane.WARNING_MESSAGE);
                    exiftool = "exiftool(-k).exe";
                } else {
                    exiftool = "no exiftool binary";
                }
            } else if ( (tmpstr.contains("exiftool")) && (!(tmpstr.contains("jexiftoolgui.db"))) ) {
                exiftool = selectedBinary;
            } else {
                exiftool = "no exiftool binary";
            }
        } else if (status == JFileChooser.CANCEL_OPTION) {
            exiftool = "cancelled";
        }
        //logger.info("what is given back from whereisexiftool: {}", exiftool);
        return exiftool;
    }

    /*
     * If no exiftool found in the path and neither in the preferences, ask the user
     * where he/she installed it or offer to download it.
     * Otherwise simply exit the program
     */
    static public void checkExifTool(JPanel myComponent) {
        String returnValue = "";
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("exift.optlocation"), ResourceBundle.getBundle("translations/program_strings").getString("exift.optdownload"), ResourceBundle.getBundle("translations/program_strings").getString("exift.optstop")};
        //JOptionPane.showOptionDialog(null,"I can not find exiftool in the preferences or I can not find exiftool at all","exiftool missing",JOptionPane.ERROR_MESSAGE);
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("noexiftool")), ResourceBundle.getBundle("translations/program_help_texts").getString("noexiftooltitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // open file chooser
            // Do this from the PreferencesDialog class as it actually belongs there
            returnValue = whereIsExiftool(myComponent);

            logger.info("returnValue is: {}", returnValue);
            if (returnValue.equals("cancelled")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.cancelledETlocatefromStartup, ResourceBundle.getBundle("translations/program_strings").getString("exift.lookupcancelled"), JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if (returnValue.equals("no exiftool binary")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromStartup, ResourceBundle.getBundle("translations/program_strings").getString("exift.wrongexebin"), JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if ( returnValue.contains("exiftool(-k).exe") || returnValue.contains("-k")) {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("exift.exifktxt")), ResourceBundle.getBundle("translations/program_strings").getString("exift.exifktitle"), JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else { // Yes. It looks like we have a correct exiftool selected
                // remove all possible line breaks
                returnValue = returnValue.replace("\n", "").replace("\r", "");
                prefs.storeByKey(EXIFTOOL_PATH, returnValue);
            }
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 450, ProgramTexts.downloadInstallET), ResourceBundle.getBundle("translations/program_strings").getString("exift.download"), JOptionPane.INFORMATION_MESSAGE);
            // open exiftool site
            Utils.openBrowser("https://exiftool.org/");
            System.exit(0);
        } else {
            // exit program
            System.exit(0);
        }

    }

    public static String showVersion(JLabel OutputLabel) {
        boolean isWindows = Utils.isOsFromMicrosoft();
        List<String> cmdparams = new ArrayList<>();
        String exv = "";

        String exiftool_path = prefs.getByKey(EXIFTOOL_PATH, "");

        if (isWindows) {
            cmdparams.add("\"" + exiftool_path + "\"");
        } else {
            cmdparams.add(exiftool_path);
        }
        cmdparams.add("-ver");
        try {
            exv = CommandRunner.runCommand(cmdparams).replace("\n", "").replace("\r", "");
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exiftoolavailable") + exv);
            MyVariables.setExiftoolVersion(exv);
            logger.debug("version from isExecutable check {}", exv);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
            exv = "Error executing command";
        }

        return exv;
    }

}
