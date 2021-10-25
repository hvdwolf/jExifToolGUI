package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class MetaData {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MetaData.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public void copyToXmp(JLabel OutputLabel) {
        String fpath = "";
        StringBuilder TotalOutput = new StringBuilder();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        List<String> cmdparams = new ArrayList<String>();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        logger.info("Copy all metadata to xmp format");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 500, ResourceBundle.getBundle("translations/program_strings").getString("cmd.dlgtext")), ResourceBundle.getBundle("translations/program_strings").getString("cmd.dlgtitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.copyallxmpdata"));
            boolean isWindows = Utils.isOsFromMicrosoft();

            String exiftool = Utils.platformExiftool();

            for (int index : selectedIndices) {
                // Unfortunately we need to do this file by file. It also means we need to initialize everything again and again
                cmdparams.clear();
                if (isWindows) {
                    cmdparams.add(exiftool);
                    cmdparams.add("-TagsFromfile");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                    cmdparams.add("\"-all>xmp:all\"");
                    boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
                    if (preserveModifyDate) {
                        cmdparams.add("-preserve");
                    }
                    cmdparams.add("-overwrite_original");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add("/bin/sh");
                    cmdparams.add("-c");
                    cmdparams.add(exiftool + " -TagsFromfile " + files[index].getPath().replaceAll(" ", "\\ ") + " '-all:all>xmp:all' -overwrite_original  " + files[index].getPath().replaceAll(" ", "\\ "));
                }
                try {
                    String res = CommandRunner.runCommand(cmdparams);
                    logger.debug("res is\n{}", res);
                    TotalOutput.append(res);
                } catch (IOException | InterruptedException ex) {
                    logger.debug("Error executing command");
                }
            }
            CommandRunner.outputAfterCommand(TotalOutput.toString());
        }

    }

    public void repairJPGMetadata(JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        logger.info("Repair corrupted metadata in JPG(s)");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("rjpg.dialogtext")), ResourceBundle.getBundle("translations/program_strings").getString("rjpg.dialogtitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            cmdparams.add(Utils.platformExiftool());
            boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
            if (preserveModifyDate) {
                cmdparams.add("-preserve");
            }
            cmdparams.add("-overwrite_original");
            for (String s : MyConstants.REPAIR_JPG_METADATA) {
                cmdparams.add(s);
            }
            boolean isWindows = Utils.isOsFromMicrosoft();

            for (int index : selectedIndices) {
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add(files[index].getPath());
                }
            }
            // export metadata
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        }
    }

    public void copyMetaData(JPanel rootpanel, JRadioButton[] CopyMetaDataRadiobuttons, JCheckBox[] CopyMetaDataCheckBoxes, int selectedRow, JProgressBar progressBar) {
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        boolean atLeastOneSelected = false;
        boolean copyAllToXMP = false;
        String fpath = "";

        List<String> params = new ArrayList<String>();
        params.add(Utils.platformExiftool());
        params.add("-TagsFromfile");
        boolean isWindows = Utils.isOsFromMicrosoft();
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[selectedRow].getPath();
        }

        params.add(fpath);
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
        if (CopyMetaDataRadiobuttons[0].isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.alltopreferred") + "<br><br>");
            atLeastOneSelected = true;
        } else if (CopyMetaDataRadiobuttons[1].isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.alltoorggroup") + "<br><br>");
            params.add("-all:all");
            atLeastOneSelected = true;
        } else { // The copySelectiveMetadataradioButton
            Message.append("<ul>");
            if (CopyMetaDataCheckBoxes[0].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyexifcheckbox") + "</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[1].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyxmpcheckbox") + "</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[2].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyiptccheckbox") + "</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[3].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyicc_profilecheckbox") + "</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[4].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copygpscheckbox") + "</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[5].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyjfifcheckbox") + "</li>");
                params.add("-jfif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[6].isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.copymakernotescheckbox") + "</li>");
                params.add("-makernotes:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        //if (!copyAllToXMP) { // This is actually a dirty way of starting the copyalltoxmp and bypassing the rest in this method
            boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
            if (preserveModifyDate) {
                params.add("-preserve");
            }
            if (!CopyMetaDataCheckBoxes[7].isSelected()) {
                params.add("-overwrite_original");
            }
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgisthiscorrect") + "</html>");
            if (atLeastOneSelected) {
                String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
                int choice = JOptionPane.showOptionDialog(null, Message, ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgtitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    // Copy metadata
                    String[] etparams = params.toArray(new String[0]);
                    for (int index : selectedIndices) {
                        //logger.info("index: {}  image path: {}", index, files[index].getPath());
                        if (isWindows) {
                            params.add(files[index].getPath().replace("\\", "/"));
                        } else {
                            params.add(files[index].getPath());
                        }
                    }
                    // export metadata
                    CommandRunner.runCommandWithProgressBar(params, progressBar);
                }
            } else {
                JOptionPane.showMessageDialog(rootpanel, ProgramTexts.NoOptionSelected, "No copy option selected", JOptionPane.WARNING_MESSAGE);
            }
        //}
    }

    public void copyInsideMetaData(JPanel rootpanel, JRadioButton[] InsideCopyMetaDataRadiobuttons, JRadioButton[] InsideSubCopyMetaDataRadiobuttons, JCheckBox[] InsideCopyMetaDataCheckBoxes, JLabel OutputLabel) {
        StringBuilder TotalOutput = new StringBuilder();
        int selectedIndices[] = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        String strjexiftoolguiARGSfolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER + File.separator + "args";

        boolean atLeastOneSelected = false;
        boolean copyAllToXMP = false;
        String fpath = "";

        List<String> params = new ArrayList<String>();

        // which options selected?
        StringBuilder Message = new StringBuilder("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
        if (InsideCopyMetaDataRadiobuttons[0].isSelected()) {
            copyToXmp(OutputLabel);
            atLeastOneSelected = true;
            copyAllToXMP= true;
        } else if (InsideCopyMetaDataRadiobuttons[1].isSelected()) {
            // we have the following checkboxes
            if (InsideSubCopyMetaDataRadiobuttons[0].isSelected()) {  // To XMP
                // to xmp (0, 1, 2, 3): exif2xmpCheckBox, gps2xmpCheckBox, iptc2xmpCheckBox, pdf2xmpCheckBox,
                Message.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.tagstoxmp") + "<ul>");
                if (InsideCopyMetaDataCheckBoxes[0].isSelected()) {
                    Message.append("<li>exif2xmp</li>");
                    params.add("-@");
                    params.add(strjexiftoolguiARGSfolder + File.separator + "exif2xmp.args");
                    atLeastOneSelected = true;
                }
                if (InsideCopyMetaDataCheckBoxes[1].isSelected()) {
                    Message.append("<li>gps2xmp</li>");
                    params.add("-@");
                    params.add(strjexiftoolguiARGSfolder + File.separator + "gps2xmp.args");
                    atLeastOneSelected = true;
                }
                if (InsideCopyMetaDataCheckBoxes[2].isSelected()) {
                    Message.append("<li>iptc2xmp</li>");
                    params.add("-@");
                    params.add(strjexiftoolguiARGSfolder + File.separator + "iptc2xmp.args");
                    atLeastOneSelected = true;
                }
                if (InsideCopyMetaDataCheckBoxes[3].isSelected()) {
                    Message.append("<li>pdf2xmp</li>");
                    params.add("-@");
                    params.add(strjexiftoolguiARGSfolder + File.separator + "pdf2xmp.args");
                    atLeastOneSelected = true;
                }
            }
            if (InsideSubCopyMetaDataRadiobuttons[1].isSelected()) { // To Exif
                // to exif (4, 5): iptc2exifCheckBox, xmp2exifCheckBox,
                Message.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.tagstoexif") + "<ul>");
                if (InsideCopyMetaDataCheckBoxes[4].isSelected()) {
                    Message.append("<li>iptc2exif</li>");
                    params.add("-@");
                    params.add("iptc2exif.args");
                    atLeastOneSelected = true;
                }
                if (InsideCopyMetaDataCheckBoxes[5].isSelected()) {
                    Message.append("<li>xmp2exif</li>");
                    params.add("-@");
                    params.add("xmp2exif.args");
                    atLeastOneSelected = true;
                }
            }
            if (InsideSubCopyMetaDataRadiobuttons[2].isSelected()) { // To IPTC
                // to iptc (6, 7): exif2iptcCheckBox, xmp2iptcCheckBox,
                Message.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.tagstoiptc") + "<ul>");
                if (InsideCopyMetaDataCheckBoxes[6].isSelected()) {
                    Message.append("<li>exif2iptc</li>");
                    params.add("-@");
                    params.add("exif2iptc.args");
                    atLeastOneSelected = true;
                }
                if (InsideCopyMetaDataCheckBoxes[7].isSelected()) {
                    Message.append("<li>xmp2iptc</li>");
                    params.add("-@");
                    params.add("xmp2iptc.args");
                    atLeastOneSelected = true;
                }
            }
            if (InsideSubCopyMetaDataRadiobuttons[3].isSelected()) { // To Gps
                // to gps (8): xmp2gpsCheckBox,
                Message.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.tagstogps") + "<ul>");
                if (InsideCopyMetaDataCheckBoxes[8].isSelected()) {
                    Message.append("<li>xmp2gps</li>");
                    params.add("-@");
                    params.add("xmp2gps.args");
                    atLeastOneSelected = true;
                }
            }
            if (InsideSubCopyMetaDataRadiobuttons[4].isSelected()) { //To PDF
                // to pdf (9): xmp2pdfCheckBox,
                Message.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.tagstopdf") + "<ul>");
                if (InsideCopyMetaDataCheckBoxes[9].isSelected()) {
                    Message.append("<li>xmp2pdf</li>");
                    params.add("-@");
                    params.add("xmp2pdf.args");
                    atLeastOneSelected = true;
                }
            }
            Message.append("</ul><br><br>");
        }
        if (!copyAllToXMP) { // This is actually a dirty way of bypassing the copyalltoxmp which has been called earlier
            boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
            if (preserveModifyDate) {
                params.add("-preserve");
            }
            // and of course (10): CopyInsideImageMakeCopyOfOriginalscheckBox
            if (!InsideCopyMetaDataCheckBoxes[10].isSelected()) {
                params.add("-overwrite_original");
            }
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgisthiscorrect") + "</html>");
            if (atLeastOneSelected) {
                String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
                int choice = JOptionPane.showOptionDialog(null, Message, ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgtitle"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.inscopytagtotag"));
                    // Copy metadata
                    String[] etparams = params.toArray(new String[0]);
                    for (int index : selectedIndices) {
                        List<String> cmdparams = new ArrayList<String>();
                        boolean isWindows = Utils.isOsFromMicrosoft();
                        cmdparams.add(Utils.platformExiftool());
                        cmdparams.add("-Tagsfromfile");

                        //logger.info("index: {}  image path: {}", index, files[index].getPath());
                        if (isWindows) {
                            cmdparams.add(files[index].getPath().replace("\\", "/"));
                            cmdparams.addAll(params);
                            cmdparams.add(files[index].getPath().replace("\\", "/"));
                        } else {
                            cmdparams.add(files[index].getPath());
                            cmdparams.addAll(params);
                            cmdparams.add(files[index].getPath().replace("\\", "/"));
                        }
                        logger.debug("insidecopy command {}", cmdparams.toString());
                        try {
                            String res = CommandRunner.runCommand(cmdparams);
                            logger.debug("res is\n{}", res);
                            TotalOutput.append(res);
                        } catch (IOException | InterruptedException ex) {
                            logger.debug("Error executing command");
                        }
                    }
                }
                CommandRunner.outputAfterCommand(TotalOutput.toString());
            } else {
                JOptionPane.showMessageDialog(rootpanel, ProgramTexts.NoOptionSelected, "No copy option selected", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
