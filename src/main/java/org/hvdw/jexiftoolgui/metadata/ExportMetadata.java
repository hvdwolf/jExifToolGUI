package org.hvdw.jexiftoolgui.metadata;

import ch.qos.logback.classic.Logger;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportMetadata {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ExportMetadata.class);

    public static void writeExport(JPanel rootPanel, JRadioButton[] GeneralExportRadiobuttons, JCheckBox[] GeneralExportCheckButtons, JComboBox exportUserCombicomboBox, JProgressBar progressBar) {
        boolean atLeastOneSelected = false;
        BufferedWriter writer;
        List<String> params = new ArrayList<String>();
        List<String> cmdparams = new ArrayList<String>(); // We need this for the csv option
        String filepath = ""; // Again: we need this for the csv option
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        // checkboxes: exportAllMetadataCheckBox, exportExifDataCheckBox, exportXmpDataCheckBox, exportGpsDataCheckBox, exportIptcDataCheckBox, exportICCDataCheckBox, GenExpuseMetadataTagLanguageCheckBoxport
        // translate to clarify
        // catmetadataradioButton, exportFromUserCombisradioButton, txtRadioButton, tabRadioButton, xmlRadioButton, htmlRadioButton, csvRadioButton
        JRadioButton catmetadataradioButton = GeneralExportRadiobuttons[0];
        JRadioButton exportFromUserCombisradioButton = GeneralExportRadiobuttons[1];
        JRadioButton txtRadioButton = GeneralExportRadiobuttons[2];
        JRadioButton tabRadioButton = GeneralExportRadiobuttons[3];
        JRadioButton xmlRadioButton = GeneralExportRadiobuttons[4];
        JRadioButton htmlRadioButton = GeneralExportRadiobuttons[5];
        JRadioButton csvRadioButton = GeneralExportRadiobuttons[6];

        boolean isWindows = Utils.isOsFromMicrosoft();

        params.add(Utils.platformExiftool());
        if (GeneralExportCheckButtons[6].isSelected()) {
            // Check for chosen metadata language
            if (!"".equals(Utils.getmetadataLanguage())) {
                params.add("-lang");
                params.add(Utils.getmetadataLanguage());
                logger.debug("Export in specific metadata language requested lang= {}", Utils.getmetadataLanguage());
            }
        }
        params.add("-a");
        StringBuilder Message = new StringBuilder();

        if (catmetadataradioButton.isSelected()) {
            // which options are selected from the checkboxes?
            Message.append("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
            if (GeneralExportCheckButtons[0].isSelected()) {
                Message.append(ResourceBundle.getBundle("translations/program_strings").getString("emd.exportall"));
                params.add("-all");
                Message.append("<br><br>");
                atLeastOneSelected = true;
            } else {
                Message.append("<ul>");
                if (GeneralExportCheckButtons[1].isSelected()) {
                    Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportexif") + "</li>");
                    params.add("-exif:all");
                    atLeastOneSelected = true;
                }
                if (GeneralExportCheckButtons[2].isSelected()) {
                    Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.extportxmp") + "</li>");
                    params.add("-xmp:all");
                    atLeastOneSelected = true;
                }
                if (GeneralExportCheckButtons[3].isSelected()) {
                    Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportgps") + "</li>");
                    params.add("-gps:all");
                    atLeastOneSelected = true;
                }
                if (GeneralExportCheckButtons[4].isSelected()) {
                    Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportiptc") + "</li>");
                    params.add("-iptc:all");
                    atLeastOneSelected = true;
                }
                if (GeneralExportCheckButtons[5].isSelected()) {
                    Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exporticc") + "</li>");
                    params.add("-icc_profile:all");
                    atLeastOneSelected = true;
                }
                Message.append("</ul><br><br>");
            }

        } else { // This is when the user has selected to export from a user combination
            atLeastOneSelected = true; // if we use the drop-down always one item is selected
            String SelectedCombi = exportUserCombicomboBox.getSelectedItem().toString();
            logger.debug("selected metadata set for export {}", SelectedCombi);
            String sql = "select tag from custommetadatasetLines where customset_name='" + SelectedCombi + "' order by rowcount";
            String queryResult = SQLiteJDBC.singleFieldQuery(sql, "tag");
            if (queryResult.length() > 0) {
                String[] customTags = queryResult.split("\\r?\\n");
                logger.debug("queryResult {}", queryResult);
                List<String> tmpparams = new ArrayList<String>();
                for (String customTag : customTags) {
                    logger.trace("customTag {}", customTag);
                    if (customTag.startsWith("-")) {
                        params.add(customTag);
                    } else {
                        params.add("-" + customTag);
                    }
                }
                //String[] tmpArray = new String[tmpparams.size()];
                //params = tmpparams.toArray(tmpArray);
                logger.debug("custom tags for export: {}", params.toString());
            }
            Message.append("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.askusercombi") + "<br>");
            Message.append(SelectedCombi);
            Message.append("<br><br>");
        }
        Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgisthiscorrect") + "</html>");
        if (atLeastOneSelected) {
            String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
            int choice = JOptionPane.showOptionDialog(null, Message, ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgtitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (choice == 1) { //Yes
                // Check with export file format has been chosen
                if (txtRadioButton.isSelected()) {
                    params.add("-w!");
                    params.add("txt");
                } else if (tabRadioButton.isSelected()) {
                    params.add("-t");
                    params.add("-w!");
                    params.add("txt");
                } else if (xmlRadioButton.isSelected()) {
                    params.add("-X");
                    params.add("-w!");
                    params.add("xml");
                } else if (htmlRadioButton.isSelected()) {
                    params.add("-h");
                    params.add("-w!");
                    params.add("html");
                /*} else if (xmpRadioButton.isSelected()) {
                    params.add("xmpexport"); */
                } else if (csvRadioButton.isSelected()) {
                    params.add("-csv");
                }

                for (int index : selectedIndices) {
                    //logger.info("index: {}  image path: {}", index, files[index].getPath());
                    if (isWindows) {
                        if (csvRadioButton.isSelected()) {
                            params.add("\"" + files[index].getPath().replace("\\", "/") + "\"");
                        } else {
                            params.add(files[index].getPath().replace("\\", "/"));
                        }
                    } else {
                        params.add(files[index].getPath());
                    }
                    // Again necessary for csv
                    filepath = files[index].getParent();
                }

                // Originally for csv we needed the > character to redirect output to a csv file, which we need to treat specially and differently on unixes and windows.
                // We also really needed the shell for it otherwise the > is seen as a file
                // We now read the output into a string and write tht string to file with a bufferedwriter
                if (csvRadioButton.isSelected()) {
                    if (isWindows) {
                        //params.add(" > " + filepath.replace("\\", "/") + "/out.csv");
                        //cmdparams.add("cmd");
                        //cmdparams.add("/c");
                        //cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > \"" + filepath.replace("\\", "/") + "/out.csv\" ");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " "));
                    } else {
                        // logger.info("params to string: {}", params.toString());
                        //cmdparams.add("/bin/sh");
                        //cmdparams.add("-c");
                        //cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > " + filepath + "/out.csv");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " "));
                    }
                } else {
                    cmdparams = params;
                }
                logger.debug("cmdparams : {}", cmdparams.toString());


                // Export metadata
                if (!csvRadioButton.isSelected()) {
                    logger.debug("CSV export requested");
                    CommandRunner.runCommandWithProgressBar(params, progressBar);
                } else {
                    try {
                        String result = CommandRunner.runCommand(params);
                        if (isWindows) {
                            writer = new BufferedWriter(new FileWriter(filepath.replace("\\", "/") + File.separator + "out.csv"));
                        } else {
                            writer = new BufferedWriter(new FileWriter(filepath + File.separator + "out.csv"));
                        }
                        writer.write(result);
                        writer.close();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                        logger.error("metadata export failed with error {}", e);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgnoexporttext"), ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgnoexporttitle"), JOptionPane.WARNING_MESSAGE);
        }
    }

}
