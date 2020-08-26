package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;

public class CheckPreferences {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);
    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    //check preferences (a.o. exiftool)
    public boolean checkPreferences(JPanel rootPanel, JLabel OutputLabel) {
        boolean exiftool_exists;
        boolean exiftool_found = false;
        String res;
        List<String> cmdparams = new ArrayList<>();


        exiftool_exists = prefs.keyIsSet(EXIFTOOL_PATH);
        logger.trace("preference check exiftool_exists reports: {}",exiftool_exists);


        if (exiftool_exists) {
            String exiftool_path = prefs.getByKey(EXIFTOOL_PATH, "");
            File tmpFile = new File(exiftool_path);
            boolean exists = tmpFile.exists();
            if (!exists) {
                exiftool_path = null;
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 300,ResourceBundle.getBundle("translations/program_strings").getString("prefs.etprefincorrecttext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etprefincorrecttitle"), JOptionPane.WARNING_MESSAGE);
            }
            logger.trace("exists is {}", exists);
            logger.info("preference exiftool returned: {}",exiftool_path);
            if (exiftool_path == null || exiftool_path.isEmpty() || !exists) {
                res = Utils.getExiftoolPath();
                logger.trace("result from getExiftoolPath(): {}", res);
            } else {
                res = exiftool_path;
                //String[] cmdparams = {res, "-ver"};
                cmdparams.add(res);
                cmdparams.add("-ver");
                try {
                    String exv = CommandRunner.runCommand(cmdparams).replace("\n", "").replace("\r", "");
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exiftoolavailable") + exv);
                    MyVariables.setExiftoolVersion(exv);
                } catch (IOException | InterruptedException ex) {
                    logger.debug("Error executing command");
                }

            }
        } else { // does not exist
            res = Utils.getExiftoolPath();
        }

        if (res != null && !res.isEmpty() && !res.toLowerCase().startsWith("info")) {
            exiftool_found = true;
            // We already checked that the node did not exist and that it is empty or null
            // remove all possible line breaks
            res = res.replace("\n", "").replace("\r", "");
            if (exiftool_found) {
                prefs.storeByKey(EXIFTOOL_PATH, res);
            }
        }

        logger.info("exiftool_found mentions: {}", exiftool_found);
        return exiftool_found;
    }
}
