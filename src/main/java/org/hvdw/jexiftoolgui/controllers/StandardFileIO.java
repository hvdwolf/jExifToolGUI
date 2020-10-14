package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.Application.OS_NAMES.APPLE;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.LAST_OPENED_FOLDER;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class StandardFileIO {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(StandardFileIO.class);

    public static String extract_resource_to_jexiftoolguiFolder(String resourcePath, String strjexiftoolguifolder, String targetSubFolder){
        String copyresult = "";
        Path resourceFile = Paths.get(strjexiftoolguifolder + File.separator);

        try {
            InputStream fileStream = StandardFileIO.getResourceAsStream(resourcePath);
            if(fileStream == null)
                return null;

            // Grab the file name
            String[] chopped = resourcePath.split("\\/");
            String fileName = chopped[chopped.length-1];
            if ("".equals(targetSubFolder)) {
                resourceFile = Paths.get(strjexiftoolguifolder + File.separator + fileName);
            } else {
                resourceFile = Paths.get(strjexiftoolguifolder + File.separator + targetSubFolder + File.separator + fileName);
            }

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
            logger.debug("success copying {}", resourcePath);
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
                logger.debug(line);
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
        String strCurrentLine;
        String strFileContents = "";

        try {
            InputStream is = getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            strFileContents = "";
            while ((strCurrentLine = reader.readLine()) != null) {
                strFileContents += strCurrentLine + "\r\n";
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
     * Get the files from the "Load images" command  via JFilechooser
     */
    public static File[] getFileNames(JPanel myComponent) {
        File[] files = null;
        javax.swing.filechooser.FileFilter imgFilter;
        FileFilter imageFormats = null;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        String userDefinedFilefilter = prefs.getByKey(USER_DEFINED_FILE_FILTER, "");
        String startFolder = getFolderPathToOpenBasedOnPreferences();
        logger.debug("startfolder for load images via JFilechooser {}", startFolder);

        final JFileChooser jchooser = new JFileChooser(startFolder, fsv);
        //final JFileChooser chooser = new NativeJFileChooser(startFolder);

        //FileFilter filter = new FileNameExtensionFilter("(images)", "jpg", "jpeg" , "png", "tif", "tiff");
        if (!"".equals(userDefinedFilefilter)) {
            String[] uDefFilefilter = userDefinedFilefilter.split(",");
            for (int i = 0; i < uDefFilefilter.length; i++)
                uDefFilefilter[i] = uDefFilefilter[i].trim();

            logger.trace("String userDefinedFilefilter {} ; String[] uDefFilefilter {}", userDefinedFilefilter, Arrays.toString(uDefFilefilter));
            imgFilter = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.userdefinedfilter"), uDefFilefilter);
            imageFormats = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.images"), MyConstants.SUPPORTED_IMAGES);
        } else {
            imgFilter = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.images"), MyConstants.SUPPORTED_IMAGES);
        }
        FileFilter audioFormats = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.audioformats"), MyConstants.SUPPORTED_AUDIOS);
        FileFilter videoFormats = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.videoformats"), MyConstants.SUPPORTED_VIDEOS);
        FileFilter supFormats = new FileNameExtensionFilter(ResourceBundle.getBundle("translations/program_strings").getString("stfio.allformats"), MyConstants.SUPPORTED_FORMATS);
        jchooser.setMultiSelectionEnabled(true);
        jchooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadimages"));
        jchooser.setFileFilter(imgFilter);
        if (!"".equals(userDefinedFilefilter)) {
            jchooser.addChoosableFileFilter(imageFormats);
        }
        jchooser.addChoosableFileFilter(audioFormats);
        jchooser.addChoosableFileFilter(videoFormats);
        jchooser.addChoosableFileFilter(supFormats);
        //chooser.showOpenDialog(mainScreen.this.rootPanel);
        int status = jchooser.showOpenDialog(myComponent);
        //logger.trace("Status returned from ");
        if (status == JFileChooser.APPROVE_OPTION) {
            files = jchooser.getSelectedFiles();
            MyVariables.setSelectedFiles(files);
            prefs.storeByKey(LAST_OPENED_FOLDER, jchooser.getCurrentDirectory().getAbsolutePath());
            logger.debug("jchooser.getCurrentDirectory().getAbsolutePath() {}", jchooser.getCurrentDirectory().getAbsolutePath());
        }
        return files;
    }

    /*
     * Get the files from the "Load images" command  via Awt Filedialog
     */
    public static File[] getFileNamesAwt(JPanel myComponent) {

        Frame dialogframe = new Frame();
        String startFolder = getFolderPathToOpenBasedOnPreferences();
        String userDefinedFilefilter = prefs.getByKey(USER_DEFINED_FILE_FILTER, "");
        logger.debug("startfolder for load images via Awt {}", startFolder);

        //logger.info("startfolder {}", startFolder);
        FileDialog fdchooser = new FileDialog(dialogframe, ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadimages"), FileDialog.LOAD);
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == APPLE) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            System.setProperty("apple.awt.use-file-dialog-packages", "true");
        }
        fdchooser.setDirectory(startFolder);
        fdchooser.setMultipleMode(true);

        fdchooser.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File file, String ext) {
                if (!"".equals(userDefinedFilefilter)) {
                    String[] uDefFilefilter = userDefinedFilefilter.split(",");
                    for (int i = 0; i < uDefFilefilter.length; i++)
                        uDefFilefilter[i] = uDefFilefilter[i].trim();
                    for (int i = 0; i < uDefFilefilter.length; i++) {
                        if (ext.toLowerCase().endsWith(uDefFilefilter[i])) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    for (int i = 0; i < MyConstants.SUPPORTED_FORMATS.length; i++) {
                        if (ext.toLowerCase().endsWith(MyConstants.SUPPORTED_FORMATS[i])) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        });

        fdchooser.setVisible(true);

        File[] files = fdchooser.getFiles();
        //File[] files = chooser.getSelectedFiles();
        if ( files.length == 0) {
            // no selection
            files = null;
        }
        MyVariables.setSelectedFiles(files);
        prefs.storeByKey(LAST_OPENED_FOLDER, fdchooser.getDirectory());
        return files;
    }


    /*
    * Get the files from a folder via the "Load Directory" via JFilechooser
     */
    public static File[] getFolderFiles(JPanel myComponent) {
        File[] files = null;
        String SelectedFolder;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File startFolder = new File(getFolderPathToOpenBasedOnPreferences());
        logger.debug("startFolder for Opening folder with Jfilechooser {}", getFolderPathToOpenBasedOnPreferences());
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == APPLE) {
            Path tmp = Paths.get(getFolderPathToOpenBasedOnPreferences());
            tmp = tmp.getParent();
            startFolder = tmp.toFile();
        } else {

        }

        final JFileChooser jchooser = new JFileChooser(startFolder, fsv);
        jchooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadfolder"));
        jchooser.resetChoosableFileFilters();
        jchooser.setAcceptAllFileFilterUsed(false);
        jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = jchooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedFolder = jchooser.getSelectedFile().getAbsolutePath();
            File folder = new File(SelectedFolder);
            //files = listFiles(SelectedFolder);
            files = folder.listFiles();
            MyVariables.setSelectedFiles(files);
            prefs.storeByKey(LAST_OPENED_FOLDER, jchooser.getCurrentDirectory().getAbsolutePath());
            logger.debug("jchooser.getCurrentDirectory().getAbsolutePath() {}", jchooser.getCurrentDirectory().getAbsolutePath());
        }

        return files;
    }

    /*
     * Get the files from a folder via the "Load Directory" via Awt Filedialog
     */
    public static File[] getFolderFilesAwt(JPanel myComponent) {
        File[] files = null;
        String SelectedFolder;

        Frame dialogframe = new Frame();
        String startFolder = getFolderPathToOpenBasedOnPreferences();
        logger.debug("startFolder for Opening folder with Awt {}", getFolderPathToOpenBasedOnPreferences());
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == APPLE) {
            Path tmp = Paths.get(getFolderPathToOpenBasedOnPreferences());
            tmp = tmp.getParent();
            startFolder = tmp.toFile().toString();
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            System.setProperty("apple.awt.use-file-dialog-packages", "false");
        }
        //logger.info("startfolder {}", startFolder);
        FileDialog fdchooser = new FileDialog(dialogframe, ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadfolder"), FileDialog.LOAD);
        fdchooser.setDirectory(startFolder);
        fdchooser.setMultipleMode(false);
        fdchooser.setVisible(true);

        SelectedFolder = fdchooser.getDirectory();
        if (SelectedFolder == null) {
            files = null;
        }
        File folder = new File(SelectedFolder);
        files = folder.listFiles();
        MyVariables.setSelectedFiles(files);
        prefs.storeByKey(LAST_OPENED_FOLDER, fdchooser.getDirectory());
        /*if (os == APPLE) {
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
        }*/
        return files;
    }

    /*
    / This method is called from the MetDataViewPanel and copies, when relevant, the custom config file to the "user home"/jexiftoolgui_data
    */
    public static String CopyCustomConfigFile(String fileName, String configFilePath) {
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
        String strfileToBe = strjexiftoolguifolder + File.separator + fileName;
        String copyResult = "";
        //NIO copy with replace existing
        Path copyFrom = Paths.get(configFilePath);
        Path copyTo = Paths.get(strfileToBe);
        try {
            Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
            copyResult = "successfully copied config file";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("copy of \"{}\" to \"{}\" failed with {}", configFilePath, strfileToBe, e);
            copyResult = e.toString();
        }
        return copyResult;
    }


    // Check if we have a jexiftoolgui_custom folder in $HOME with defaults
    public static String checkforjexiftoolguiFolder() {
        String method_result = "";
        String fileToBecopied = "";
        File copyFile = null;
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
        fileToBecopied = strjexiftoolguifolder + File.separator + "jexiftoolgui.db";
        copyFile = new File(fileToBecopied);
        if (!copyFile.exists()) {
            logger.debug("no database yet; trying to create it");
            method_result = extract_resource_to_jexiftoolguiFolder("jexiftoolgui.db", strjexiftoolguifolder, "");
            if ("success".equals(method_result)) {
                MyVariables.setjexiftoolguiDBPath(fileToBecopied);
                logger.info("copied the initial database");
            }
        } else { // the DB already exists
            method_result = "exists";
            logger.debug("the database already exists.");
            MyVariables.setjexiftoolguiDBPath(fileToBecopied);
        }
        // Now check if our "cantdisplay.png" already exists which is the placeholder for non-displayable RAW formats
        fileToBecopied = strjexiftoolguifolder + File.separator + "cantdisplay.png";
        copyFile = new File(fileToBecopied);
        if (!copyFile.exists()) {
            logger.debug("no cantdisplay.png yet; trying to create it");
            method_result = extract_resource_to_jexiftoolguiFolder("cantdisplay.png", strjexiftoolguifolder, "");
            if ("success".equals(method_result)) {
                MyVariables.setcantdisplaypng(fileToBecopied);
                logger.debug("copied cantdisplay.png");
            }
        } else { // the png already exists
            method_result = "exists";
            logger.debug("the cantdisplay.png already exists.");
            MyVariables.setcantdisplaypng(fileToBecopied);
        }

        //logger.info("string for DB: " + MyVariables.getjexiftoolguiDBPath());
        return method_result;
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static String RecreateOurTempFolder () {
        String result = "Success";
        boolean successfully_erased = true;
        //File tf = null;
        //String tmpfolder = "";

        // Get the temporary directory
        String tempWorkDir = System.getProperty("java.io.tmpdir") + File.separator + "jexiftoolgui";
        File tmpfolder = new File (tempWorkDir);
        MyVariables.settmpWorkFolder(tempWorkDir);
        if (tmpfolder.exists()) {
            boolean successfully_deleted = deleteDirectory(tmpfolder);
            if (!successfully_deleted) {
                successfully_erased = false;
                result = "Failed to erase " + tempWorkDir + File.separator + "jexiftoolgui";
                logger.error(result);
            }
        }
        // Now (re)create our tmpfolder
        try {
            //Files.createDirectories(Paths.get(tempWorkDir + File.separator + "jexiftoolgui"));
            Files.createDirectories(Paths.get(tempWorkDir));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            result = "Creating folder \"" + tempWorkDir + File.separator + "jexiftoolgui failed";
            logger.error(result);
        }
        // delete our tmp workfolder including contents on program exit
        tmpfolder.deleteOnExit();

        return result;
    }

    public static String noSpacePath () throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        String checkPath = MyVariables.getSelectedImagePath();
        if (checkPath.contains(" ")) { //Only checks for first space in string, but that's enough. Even one space is too much
            logger.debug("path contains spaces {}", checkPath);
            File imgfile = new File(MyVariables.getSelectedImagePath());
            String filename = imgfile.getName();
            File targetfile = new File(MyVariables.gettmpWorkFolder() + File.separator + filename);
            if (targetfile.exists()) {
                return MyVariables.gettmpWorkFolder() + File.separator + filename;
            } else {
                try {
                    //Files.copy(imgfile, targetfile);
                    sourceChannel = new FileInputStream(imgfile).getChannel();
                    destChannel = new FileOutputStream(targetfile).getChannel();
                    destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                }finally {
                    sourceChannel.close();
                    destChannel.close();
                }
                /*} catch (IOException e) {
                    // simply return original path. What else can we do?
                    return checkPath;
                }*/
                return targetfile.getPath();
            }
        } else {
            // simply return original path. Nothing to do
            logger.debug("No spaces in {}", checkPath);
            return checkPath;
        }
        //return checkPath;
    }
}
