package org.hvdw.jexiftoolgui.metadata;

import ch.qos.logback.classic.Logger;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;

public class ExportMetadata {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ExportMetadata.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

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

    /**
     * This method is indirectly called from the "Compare images" screen. That screens opens a popup request the format to export to
     * @param allMetadata
     * @param ciwRootPanel
     * @param output ==> onecsvperimage or onecombinedcsv
     */
    public static void WriteCSVFromImgComp(List<String[]> allMetadata, JPanel ciwRootPanel, String output) {
        List<String[]> imageMetadata = new ArrayList<String[]>();
        File[] files = MyVariables.getLoadedFiles();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File tmpfile;
        String filename;
        String csvnamepath = "";
        String csvdata = "";
        String producedDocs = "";

        if ("onecsvperimage".equals(output)) {
            for (int index : selectedIndices) {
                String[] csv;
                filename = files[index].getName();
                csvdata = "";

                tmpfile = files[index];
                csvnamepath = tmpfile.getParent() + File.separator + Utils.getFileNameWithoutExtension(filename) + ".csv";
                File csvfile = new File(csvnamepath);
                logger.debug("csvnamepath {}", csvnamepath);
                // Get the data belonging to this file (index)
                for (String[] row : allMetadata) {
                    if (Integer.valueOf(row[1]) == index) {
                        csvdata = csvdata + "\"" + row[2] + "\",\"" + row[3] + "\",\"" + row[4] + "\"\r\n";
                        //logger.trace("index {} rowdata {}", index, Arrays.toString(row));
                    }
                }
                try {
                    //FileWriter fw = new FileWriter(csvfile, Charset.forName("UTF8"));
                    FileWriter fw = new FileWriter(csvnamepath);
                    fw.write(csvdata);
                    fw.close();
                    producedDocs += csvnamepath + "<br>";
                } catch (IOException e) {
                    logger.error("error writing csv {}", e);
                    e.printStackTrace();
                }
            }
            // use the setpdfDocs variable for it. No use to create another variable
            MyVariables.setpdfDocs(producedDocs);
            logger.debug("produced csv Docs {}", producedDocs);
        } else {   //onecombinedcsv
            producedDocs = "";
            List<String> csvrows = new ArrayList<String>();
            filename = files[0].getName();
            tmpfile = files[0];
            csvnamepath = tmpfile.getParent() + File.separator + "Output.csv";
            csvdata = "\"Category name\",\"Tag name\"";
            for (int index : selectedIndices) {
                tmpfile = files[index];
                filename = files[index].getName();
                csvdata = csvdata + ",\"" + filename + "\"";
            }
            csvrows.add(csvdata);
            // The allMetadata is in this case tableMetadata
            for (String[] row : allMetadata) {
                int columns = row.length;
                csvdata= "\"" + row[0] + "\"";
                for (int i = 1; i < columns; i++) {
                    csvdata = csvdata + ",\"" + row[i] + "\"";
                }
                //csvdata = csvdata;
                csvrows.add(csvdata);
            }
            // Sort the arraylist
            Collections.sort(csvrows);
            csvdata = "";
            for (String row : csvrows) {
                csvdata = csvdata + row + "\n";
            }
            try {
                FileWriter fw = new FileWriter(csvnamepath);
                fw.write(csvdata);
                fw.close();
                producedDocs += csvnamepath + "<br>";
            } catch (IOException e) {
                logger.error("error writing csv {}", e);
                e.printStackTrace();
            }
            MyVariables.setpdfDocs(producedDocs);
        }
    }


    /////////////////////// Below the Sidecar exports
    public static void SidecarChoices(JRadioButton[] SCradiobuttons, JPanel rootPanel, JProgressBar progressBar, JLabel OutputLabel) {
        if (SCradiobuttons[0].isSelected()) {
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exifsidecar"));
            exportExifMieExvSidecar(rootPanel, progressBar, "exif");
            OutputLabel.setText("");
        } else if (SCradiobuttons[1].isSelected()) {
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.xmpsidecar"));
            exportXMPSidecar(rootPanel, progressBar);
            OutputLabel.setText("");
        } else if (SCradiobuttons[2].isSelected()) {
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.miesidecar"));
            exportExifMieExvSidecar(rootPanel, progressBar, "mie");
            OutputLabel.setText("");
        } else {
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exvsidecar"));
            exportExifMieExvSidecar(rootPanel, progressBar, "exv");
            OutputLabel.setText("");
        }


        /*case "sidecarhelp":
        Utils.openBrowser(ProgramTexts.ProjectWebSite + "/manual/index.html#sidecar");
        break;*/


    }


    public static void exportExifMieExvSidecar(JPanel rootpanel, JProgressBar progressBar, String exportoption) {
        String commandstring = "";
        String pathwithoutextension = "";
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue"),  ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel")};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        int choice = 999;
        String logstring = "";
        String export_extension = exportoption.toLowerCase().trim();

        switch (exportoption.toLowerCase()) {
            case "mie":
                logger.info("Create MIE sidecar");
                logstring = "export mie sidecar cmdparams: {}";
                choice = JOptionPane.showOptionDialog(rootpanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("esc.mietext")), ResourceBundle.getBundle("translations/program_strings").getString("esc.mietitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                break;
            case "exv":
                logger.info("Create EXV sidecar");
                logstring = "export exv sidecar cmdparams: {}";
                choice = JOptionPane.showOptionDialog(rootpanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("esc.exvtext")), ResourceBundle.getBundle("translations/program_strings").getString("esc.exvtitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                break;
            case "exif":
                logger.info("Create EXIF sidecar");
                logstring = "export exif sidecar cmdparams: {}";
                choice = JOptionPane.showOptionDialog(rootpanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("esc.exiftext")), ResourceBundle.getBundle("translations/program_strings").getString("esc.exiftitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                break;
        }
        if ((choice == 0)) {
            // choice 0: exiftool -tagsfromfile a.jpg -all:all -icc_profile a.mie
            // exiftool -tagsfromfile a.jpg -all:all -icc_profile a.exv
            // exiftool -tagsfromfile a.jpg -all:all -icc_profile a.exif
            // choice 1: Cancel
            boolean isWindows = Utils.isOsFromMicrosoft();

            for (int index : selectedIndices) {
                cmdparams = new ArrayList<String>();; // initialize on every file
                cmdparams.add(Utils.platformExiftool());
                cmdparams.add("-tagsfromfile");

                if (isWindows) {
                    pathwithoutextension = Utils.getFilePathWithoutExtension(files[index].getPath().replace("\\", "/"));
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                    cmdparams.add("-all:all");
                    if (!"exif".equals(export_extension)) {
                        cmdparams.add("-icc_profile");
                    }
                    cmdparams.add(pathwithoutextension + "." + export_extension);
                } else {
                    pathwithoutextension = Utils.getFilePathWithoutExtension(files[index].getPath());
                    //cmdparams.add(files[index].getPath().replace(" ", "\\ "));
                    //cmdparams.add("\"" + files[index].getPath() + "\"");
                    cmdparams.add(files[index].getPath());
                    commandstring += files[index].getPath().replace(" ", "\\ ");
                    cmdparams.add("-all:all");
                    if (!"exif".equals(export_extension)) {
                        cmdparams.add("-icc_profile");
                    }
                    //cmdparams.add((pathwithoutextension + "." + export_extension).replace(" ", "\\ "));
                    //cmdparams.add("\"" + (pathwithoutextension + "." + export_extension) + "\"");
                    cmdparams.add((pathwithoutextension + "." + export_extension));
                }
                // export metadata
                logger.info(logstring, cmdparams);
                CommandRunner.runCommandWithProgressBar(cmdparams, progressBar,"off");
                //CommandRunner.runCommandWithProgressBar(commandstring, progressBar,false);
            }
            JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("esc.fintext"), ResourceBundle.getBundle("translations/program_strings").getString("esc.fintitle"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void exportXMPSidecar(JPanel rootpanel, JProgressBar progressBar) {
        String commandstring = "";
        String pathwithoutextension = "";
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("esc.all"), ResourceBundle.getBundle("translations/program_strings").getString("esc.xmp"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel")};
        //String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("esc.all"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel")};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        logger.info("Create xmp sidecar");
        int choice = JOptionPane.showOptionDialog(rootpanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("esc.xmptext")), ResourceBundle.getBundle("translations/program_strings").getString("esc.xmptitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (!(choice == 2)) {
            // choice 0: exiftool -tagsfromfile SRC.EXT DST.xmp
            // choice 1: exiftool -tagsfromfile SRC.EXT -xmp DST.xmp
            // choice 2: Cancel
            boolean isWindows = Utils.isOsFromMicrosoft();

            for (int index : selectedIndices) {
                commandstring = "";
                //logger.info("index: {}  image path: {}", index, files[index].getPath());
                cmdparams = new ArrayList<String>();; // initialize on every file
                cmdparams.add(Utils.platformExiftool());
                commandstring += Utils.platformExiftool();
                boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
                if (preserveModifyDate) {
                    cmdparams.add("-preserve");
                }
                //cmdparams.add("-overwrite_original");
                cmdparams.add("-tagsfromfile");
                commandstring += " -tagsfromfile ";

                if (isWindows) {
                    pathwithoutextension = Utils.getFilePathWithoutExtension(files[index].getPath().replace("\\", "/"));
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                    commandstring += files[index].getPath().replace("\\", "/");
                    if (choice == 1) {
                        cmdparams.add("-xmp");
                        commandstring += " -xmp ";
                    }
                    cmdparams.add(pathwithoutextension + ".xmp");
                    commandstring += pathwithoutextension + ".xmp";
                } else {
                    pathwithoutextension = Utils.getFilePathWithoutExtension(files[index].getPath());
                    //cmdparams.add(files[index].getPath().replace(" ", "\\ "));
                    cmdparams.add(files[index].getPath());
                    commandstring += files[index].getPath().replace(" ", "\\ ");
                    if (choice == 1) {
                        cmdparams.add("-xmp");
                        commandstring += " -xmp ";
                    }
                    //cmdparams.add((pathwithoutextension + ".xmp").replace(" ", "\\ "));
                    cmdparams.add(pathwithoutextension + ".xmp");
                    commandstring += (pathwithoutextension + ".xmp").replace(" ", "\\ ");
                }
                // export metadata
                logger.info("exportxmpsidecar cmdparams: {}", cmdparams);
                CommandRunner.runCommandWithProgressBar(cmdparams, progressBar,"off");
                //CommandRunner.runCommandWithProgressBar(commandstring, progressBar,false);
            }
            JOptionPane.showMessageDialog(rootpanel, ResourceBundle.getBundle("translations/program_strings").getString("esc.fintext"), ResourceBundle.getBundle("translations/program_strings").getString("esc.fintitle"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
