package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;

public class ExifTool {

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExifTool.class);

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
        chooser.setDialogTitle("Locate ExifTool ...");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            selectedBinary = chooser.getSelectedFile().getPath();
            String tmpstr = selectedBinary.toLowerCase();
            if (isWindows) {
                if (tmpstr.contains("exiftool.exe")) {
                    exiftool = selectedBinary;
                } else if (tmpstr.contains("exiftool(-k).exe")) {
                    exiftool = "-k version";
                }
            } else if (tmpstr.contains("exiftool")) {
                exiftool = selectedBinary;
            } else {
                exiftool = "no exiftool binary";
            }
        } else if (status == JFileChooser.CANCEL_OPTION) {
            exiftool = "cancelled";
            //JOptionPane.showMessageDialog(myComponent,ProgramTexts.CanETlocation,"Locate ExifTool",JOptionPane.INFORMATION_MESSAGE);
            //System.exit(0);
        }
        return exiftool;
    }

    /*
     * If no exiftool found in the path and neither in the preferences, ask the user
     * where he/she installed it or offer to download it.
     * Otherwise simply exit the program
     */
    static public void checkExifTool(JPanel myComponent) {
        String returnValue = "";
        String[] options = {"specify location", "Download", "Stop"};
        //JOptionPane.showOptionDialog(null,"I can not find exiftool in the preferences or I can not find exiftool at all","exiftool missing",JOptionPane.ERROR_MESSAGE);
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("noexiftool")), ResourceBundle.getBundle("translations/program_help_texts").getString("noexiftooltitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // open file chooser
            // Do this from the PreferencesDialog class as it atually belongs there
            returnValue = whereIsExiftool(myComponent);
            if (returnValue.equals("cancelled")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.cancelledETlocatefromStartup, "Cancelled locate ExifTool", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if (returnValue.equals("no exiftool binary")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromStartup, "Wrong executable", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else { // Yes. It looks like we have a correct exiftool selected
                // remove all possible line breaks
                returnValue = returnValue.replace("\n", "").replace("\r", "");
                prefs.storeByKey(EXIFTOOL_PATH, returnValue);
            }
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 450, ProgramTexts.downloadInstallET), "Download ExifTool", JOptionPane.INFORMATION_MESSAGE);
            // open exiftool site
            Utils.openBrowser("https://exiftool.org/");
            System.exit(0);
        } else {
            // exit program
            System.exit(0);
        }

    }

}
