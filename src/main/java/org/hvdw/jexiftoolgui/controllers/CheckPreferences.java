package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.Utils.getCurrentOsName;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;

/**
 * This class checks preferences that need to be checked.
 * Currently that is only exiftool
 */
public class CheckPreferences {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Utils.class);
    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    /**
     * Checks all possible incorrect entries of provided exiftool paths
     * @param exiftool_path
     * @return
     */
    private boolean check_ET_correctness( String exiftool_path) {
        boolean correctET = false;

        if ( (exiftool_path != null) && !(exiftool_path.isEmpty()) ) {
            switch (exiftool_path) {
                case "":
                    break;
                case "jexiftoolgui.db":
                    break;
                case "exiftool(-k).exe":
                    break;
                case "-k version":
                    break;
                default:
                    correctET = true;
            }
        }

        return correctET;
    }


    public String isExifToolExecutable(String etPath , JLabel OutputLabel) {
        String res = "";
        List<String> cmdparams = new ArrayList<>();
        boolean isWindows = Utils.isOsFromMicrosoft();

        if (isWindows) {
            cmdparams.add("\"" + res + "\"");
        } else {
            cmdparams.add(res);
        }
        cmdparams.add("-ver");
        try {
            String exv = CommandRunner.runCommand(cmdparams).replace("\n", "").replace("\r", "");
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exiftoolavailable") + exv);
            MyVariables.setExiftoolVersion(exv);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
            res = "Error executing command";
        }

        return res;
    }

    public String checkExifToolPreference() {
        boolean exiftool_preference_exists = false;
        String res = "";

        exiftool_preference_exists = prefs.keyIsSet(EXIFTOOL_PATH);
        logger.debug("preference check exiftool_exists reports: {}",exiftool_preference_exists);
        if (exiftool_preference_exists) {  // Yes, our preference setting exists
            String exiftool_path = prefs.getByKey(EXIFTOOL_PATH, "");
            logger.info("exiftoolpath contains {}", exiftool_path);

            File tmpFile = new File(exiftool_path);
            boolean file_exists = tmpFile.exists();

            if (exiftool_path == null || exiftool_path.isEmpty() || !file_exists) {
                res = "Preference null_empty_notexisting";
                logger.debug("result from getExiftoolInPath() for null/empty/nonexisting value: {}", res);
            } else { // Yes, our preference exists and has a value for which the file can be found (doesn't mean it is correct)
                res = exiftool_path;
            }
        } else {
            res = "No exiftool preference yet";
        }
        return res;
    }

    //check preferences (a.o. exiftool)
    public boolean checkPreferences(JPanel rootPanel, JLabel OutputLabel) {
        boolean exiftool_preference_exists = false;
        boolean exiftool_found = false;
        boolean correctET =false;
        String res = null;
        List<String> cmdparams = new ArrayList<>();

        boolean isWindows = Utils.isOsFromMicrosoft();
        exiftool_preference_exists = prefs.keyIsSet(EXIFTOOL_PATH);
        logger.debug("preference check exiftool_exists reports: {}",exiftool_preference_exists);



        if (exiftool_preference_exists) {
            String exiftool_path = prefs.getByKey(EXIFTOOL_PATH, "");
            logger.debug("prefs.getByKey exiftoolpath contains {}", exiftool_path);
            if ( !(exiftool_path.contains("jexiftoolgui.db")) &&  !(exiftool_path.contains("exiftool(-k).exe")) &&  !(exiftool_path.contains("-k version")) ) {
                File tmpFile = new File(exiftool_path);
                boolean file_exists = tmpFile.exists();
                if (!file_exists) {
                    logger.debug("the exiftool path in the preferences being \"{} \" isn't found", exiftool_path);
                    exiftool_path = null;
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 300, ResourceBundle.getBundle("translations/program_strings").getString("prefs.etprefincorrecttext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etprefincorrecttitle"), JOptionPane.WARNING_MESSAGE);
                }
                if (exiftool_path == null || exiftool_path.isEmpty() || !file_exists) {
                    // Try to find exiftool in the path
                    res = ExifTool.getExiftoolInPath();
                    //
                    logger.debug("result from getExiftoolInPath() after null/empty/not_exist: {}", res);
                } else {
                    res = exiftool_path;
                    logger.debug("preference exists and not empty/null/non_existent: {}", res);
                }
            } else { //user has selected jexiftoolgui.db as exiftool (it happens) or from a windows internal path.
                res = ExifTool.getExiftoolInPath();
            }
        } else { // exiftool preference does not exist
            res = ExifTool.getExiftoolInPath();
        }

        //////////////////////// old ///////////////////////////////////

        if ((res != null) && !res.isEmpty() && !res.toLowerCase().startsWith("info") && !(res.toLowerCase()).equals("c:\\windows\\exiftool.exe") )  {
            exiftool_found = true;
            // We already checked that the node did not exist and that it is empty or null
            // remove all possible line breaks
            res = res.replace("\n", "").replace("\r", "");
            if (exiftool_found) {
                prefs.storeByKey(EXIFTOOL_PATH, res);
                MyVariables.setExifToolPath(res);
            }
        } else if ("c:\\windows\\exiftool.exe".equals(res.toLowerCase())) {
            exiftool_found = true;
            MyVariables.setExifToolPath(res);
        }

        logger.info("exiftool_found mentions: {}", exiftool_found);
        logger.info("exiftool path: {}", res);
        return exiftool_found;
    }
}
