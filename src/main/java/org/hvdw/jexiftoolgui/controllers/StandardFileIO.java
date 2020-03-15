package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.LAST_OPENED_FOLDER;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class StandardFileIO {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(StandardFileIO.class);

    public static String readTextFileAsString (String fileName) {
        // This will reference one line at a time
        String line = null;
        StringBuilder totalText = new StringBuilder("");

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                logger.info(line);
                totalText.append(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            logger.debug("Unable to open file '{}'", fileName);
        }
        catch(IOException ex) {
            logger.debug("Error reading file '{}'", fileName);
        }

        return totalText.toString();
    }


    public static InputStream getResourceAsStream(String path) {
        return Utils.class.getClassLoader().getResourceAsStream(path);
    }

    // Reads a text file from resources
    public static String readTextFileAsStringFromResource(String fileName) {
        String strFileContents = "";

        try {
            InputStream is = getResourceAsStream(fileName);
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            while((bytesRead = is.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }

        } catch(FileNotFoundException ex) {
            logger.debug("Unable to open file '{}'", fileName);
        } catch(IOException ex) {
            logger.debug("Error reading file '{}'", fileName);
        }
        return strFileContents;
    }

    /* General check method which folder to open
     * Based on preference default folder, "always Use Last used folder" or home folder
     */
    public static String getFolderPathToOpenBasedOnPreferences() {

        boolean useLastOpenedFolder = prefs.getByKey(USE_LAST_OPENED_FOLDER, false);
        String lastOpenedFolder = prefs.getByKey(LAST_OPENED_FOLDER, "");
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);

        String defaultStartFolder = prefs.getByKey(DEFAULT_START_FOLDER, "");

        //java_11 String startFolder = !defaultStartFolder.isBlank() ? defaultStartFolder : userHome;
        // At least for time being use java_1.8
        String startFolder = !defaultStartFolder.isEmpty() ? defaultStartFolder : userHome;

        //jave_11 if (useLastOpenedFolder && !lastOpenedFolder.isBlank()) {
        // At least for time being use java_1.8
        if (useLastOpenedFolder && !lastOpenedFolder.isEmpty()) {
            startFolder = lastOpenedFolder;
        }
        return startFolder;
    }

    /*
     * Get the files from the "Load images" command
     */
    public static File[] getFileNames(JPanel myComponent) {
        File[] files = null;

        String startFolder = getFolderPathToOpenBasedOnPreferences();

        final JFileChooser chooser = new JFileChooser(startFolder);
        //FileFilter filter = new FileNameExtensionFilter("(images)", "jpg", "jpeg" , "png", "tif", "tiff");
        javax.swing.filechooser.FileFilter imgFilter = new FileNameExtensionFilter("(images)", MyConstants.SUPPORTED_IMAGES);
        FileFilter supFormats = new FileNameExtensionFilter("(supported formats)", MyConstants.SUPPORTED_FORMATS);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle("Load Image(s)...");
        chooser.setFileFilter(imgFilter);
        chooser.addChoosableFileFilter(supFormats);
        //chooser.showOpenDialog(mainScreen.this.rootPanel);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            files = chooser.getSelectedFiles();
            MyVariables.setSelectedFiles(files);
            prefs.storeByKey(LAST_OPENED_FOLDER, chooser.getSelectedFile().getAbsolutePath());
        }
        return files;
    }
}
