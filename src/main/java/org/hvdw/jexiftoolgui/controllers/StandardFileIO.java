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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.LAST_OPENED_FOLDER;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class StandardFileIO {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(StandardFileIO.class);

    public static String extract_resource_to_jexiftoolguiFolder(String resourcePath, String strjexiftoolguifolder){
        String copyresult = "";
        Path resourceFile = Paths.get(strjexiftoolguifolder + File.separator);

        try {
            InputStream fileStream =StandardFileIO.getResourceAsStream(resourcePath);
            if(fileStream == null)
                return null;

            // Grab the file name
            String[] chopped = resourcePath.split("\\/");
            String fileName = chopped[chopped.length-1];
            resourceFile = Paths.get(strjexiftoolguifolder + File.separator + fileName);

            // Create an output stream
            OutputStream out = new FileOutputStream(String.valueOf(resourceFile));

            // Write the file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Close the streams
            fileStream.close();
            out.close();

        } catch (IOException e) {
            copyresult = "Error creating file " + resourcePath;
            logger.error("Error creating file " + resourcePath);
            return null;
        }
        if ("".equals(copyresult)) {
            copyresult = "success";
            logger.info("success");
        }
        return copyresult;
    }

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

        //java_11 
        //String startFolder = !defaultStartFolder.isBlank() ? defaultStartFolder : userHome;
        // At least for time being use java_1.8
        String startFolder = !defaultStartFolder.isEmpty() ? defaultStartFolder : userHome;

        //java_11 
        //if (useLastOpenedFolder && !lastOpenedFolder.isBlank()) {
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
        FileFilter audioFormats = new FileNameExtensionFilter("(audio formats)", MyConstants.SUPPORTED_AUDIOS);
        FileFilter videoFormats = new FileNameExtensionFilter("(video formats)", MyConstants.SUPPORTED_VIDEOS);
        FileFilter supFormats = new FileNameExtensionFilter("(all supported formats)", MyConstants.SUPPORTED_FORMATS);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle("Load Image(s)... (or select other file filter)");
        chooser.setFileFilter(imgFilter);
        chooser.addChoosableFileFilter(audioFormats);
        chooser.addChoosableFileFilter(videoFormats);
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

    // Check if we have a jexiftoolgui_custom folder in $HOME with defaults
    public static String checkforjexiftoolguiFolder() {
        String method_result = "";
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        // Check if folder exists
        String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
        File jexiftoolguifolder = new File(strjexiftoolguifolder);
        if (!jexiftoolguifolder.exists()) { // no folder yet
            // First create jexiftoolgui_custom in userHome
            try {
                Files.createDirectories(Paths.get(strjexiftoolguifolder));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                method_result = "Error creating directory " + strjexiftoolguifolder;
                logger.error("Error creating directory " + strjexiftoolguifolder);
            }
        } else { //folder exists
            method_result = "exists";
        }
       /* // Now check if our first custom.csv exists
        String strcustomcsv = strjexiftoolguifolder + File.separator + "custom.csv";
        File customcsv = new File(strcustomcsv);
        if (!customcsv.exists()) {
            logger.debug("no custom.csv; trying to create it");
            method_result = extract_resource_to_jexiftoolguiFolder("texts/custom.csv", strjexiftoolguifolder);
        } else { //custom.csv exists
            method_result = "exists";
        } */
        // Now check if our database exists
        String strDB = strjexiftoolguifolder + File.separator + "jexiftoolgui.db";
        File jexifDB = new File(strDB);
        if (!jexifDB.exists()) {
            logger.debug("no database yet; trying to create it");
            method_result = extract_resource_to_jexiftoolguiFolder("jexiftoolgui.db", strjexiftoolguifolder);
            if ("success".equals(method_result)) {
                MyVariables.setjexiftoolguiDBPath(strDB);
                logger.info("copied the initial database");
            }
        } else { // the DB already exists
            method_result = "exists";
            logger.info("the database already exists.");
            MyVariables.setjexiftoolguiDBPath(strDB);
        }
        //logger.info("string for DB: " + MyVariables.getjexiftoolguiDBPath());
        return method_result;
    }

}
