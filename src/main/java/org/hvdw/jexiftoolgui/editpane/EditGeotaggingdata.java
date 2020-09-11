package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.PreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PREFERRED_FILEDIALOG;


public class EditGeotaggingdata {

    private String ImageFolder;
    private IPreferencesFacade prefs = PreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditGeotaggingdata.class);

    public String getImagePath(JPanel myComponent) {
        String SelectedFolder;
        String prefFileDialog = prefs.getByKey(PREFERRED_FILEDIALOG, "jfilechooser");

        String startFolder = StandardFileIO.getFolderPathToOpenBasedOnPreferences();
        if ("jfilechooser".equals(prefFileDialog)) {
            final JFileChooser chooser = new JFileChooser(startFolder);
            chooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadfolder"));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int status = chooser.showOpenDialog(myComponent);
            if (status == JFileChooser.APPROVE_OPTION) {
                SelectedFolder = chooser.getSelectedFile().getAbsolutePath();
                return SelectedFolder;
            } else {
                return "";
            }
        } else {
            JFrame dialogframe = new JFrame("");
            FileDialog chooser = new FileDialog(dialogframe, ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadfolder"), FileDialog.LOAD);
            chooser.setDirectory(startFolder);
            chooser.setMultipleMode(false);
            chooser.setVisible(true);

            SelectedFolder = chooser.getDirectory();
            if (SelectedFolder == null) {
                return "";
            } else {
                return SelectedFolder;
            }
        }
    }

    public String gpsLogFile(JPanel myComponent) {

        String startFolder = StandardFileIO.getFolderPathToOpenBasedOnPreferences();
        final JFileChooser chooser = new JFileChooser(startFolder);
        chooser.setMultiSelectionEnabled(false);
        String[] filexts = {"gpx", "gps", "log"};
        FileFilter filter = new FileNameExtensionFilter("(*.gpx)", filexts);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Locate GPS log file ...");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            String selectedLogFile = chooser.getSelectedFile().getPath();
            return selectedLogFile;
        } else {
            return "";
        }
    }

//    public void writeInfo(String onFolder, String gpslogfile, String geosync, boolean OverwiteOriginals, int[] selectedFilenamesIndices, File[] files) {
    public void writeInfo(boolean images_selected, JTextField[] geotaggingFields, JCheckBox[] geotaggingBoxes, boolean OverwiteOriginals, JProgressBar progressBar) {

        int[] selectedFilenamesIndices = new int[]{};
        File[] files = new File[]{};
        if (images_selected) {
            selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
            files = MyVariables.getSelectedFiles();
        }
        String fpath = "";
        List<String> cmdparams = new ArrayList<String>();
        String onFolder = geotaggingFields[0].getText().trim();
        String gpslogfile = geotaggingFields[1].getText().trim();
        String geosync = geotaggingFields[2].getText().trim();
        logger.info("onFolder: " + onFolder);

        boolean isWindows = Utils.isOsFromMicrosoft();

        cmdparams.add(Utils.platformExiftool());
        if (!OverwiteOriginals) {
            cmdparams.add("-overwrite_original_in_place");
        }
        cmdparams.add("-geotag");
        cmdparams.add(gpslogfile);

        if (!"".equals(geosync)) {
            cmdparams.add("-geosync=" + geosync);
        }
        // Check if also the location is to be added
        if (geotaggingBoxes[0].isSelected()) {
            cmdparams.add("-xmp:Location=" + geotaggingFields[3].getText().trim());
            cmdparams.add("-iptc:Sub-location=" + geotaggingFields[3].getText().trim());
        }
        if (geotaggingBoxes[1].isSelected()) {
            cmdparams.add("-xmp:Country=" + geotaggingFields[4].getText().trim());
            cmdparams.add("-iptc:Country-PrimaryLocationName=" + geotaggingFields[4].getText().trim());

        }
        if (geotaggingBoxes[2].isSelected()) {
            cmdparams.add("-xmp:State=" + geotaggingFields[5].getText().trim());
            cmdparams.add("-iptc:Province-State=" + geotaggingFields[5].getText().trim());
        }
        if (geotaggingBoxes[3].isSelected()) {
            cmdparams.add("-xmp:City=" + geotaggingFields[6].getText().trim());
            cmdparams.add("-iptc:City=" + geotaggingFields[6].getText().trim());
        }

        if ("".equals(onFolder)) { // Empty folder string which means we use selected files
            for (int index: selectedFilenamesIndices) {
                //logger.info("index: {}  image path: {}", index, files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add(files[index].getPath());
                }
            }
        } else { // We do have a non-empty folder string
            //cmdparams.addAll( Arrays.asList(params) );

            if (isWindows) {
                cmdparams.add(onFolder.replace("\\", "/"));
            } else {
                cmdparams.add(onFolder);
            }
        }

        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
    }

    public void ResetFields(JTextField[] geotaggingFields, JCheckBox[] geotaggingBoxes) {
        for (JTextField field: geotaggingFields) {
            field.setText("");
        }
        for (JCheckBox checkBox: geotaggingBoxes) {
            checkBox.setSelected(false);
        }
    }

}
