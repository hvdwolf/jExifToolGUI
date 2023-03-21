package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import org.apache.commons.io.FileUtils;


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
        String startFolder = !defaultStartFolder.isBlank() ? defaultStartFolder : userHome;

        //java_11 
        if (useLastOpenedFolder && !lastOpenedFolder.isBlank()) {
            startFolder = lastOpenedFolder;
        }
        return startFolder;
    }

    /**
     * When use folder loading we sometimes encounter a folder with images, but also containing one or more folders
     * We only want the images.
     * @param files
     * @return
     */
    public static File[] stripFoldersFromFiles (File[] files) {
        //File[] realFiles = null;
        List<File> tmpFiles = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile()) {
                tmpFiles.add(file);
            }
        }
        File[] realFiles = tmpFiles.toArray(new File[tmpFiles.size()]);
        logger.debug("realFiles {}", Arrays.toString(realFiles));
        return realFiles;
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
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        jchooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));

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
        int status = jchooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            files = jchooser.getSelectedFiles();
            MyVariables.setLoadedFiles(files);
            prefs.storeByKey(LAST_OPENED_FOLDER, jchooser.getCurrentDirectory().getAbsolutePath());
            logger.debug("jchooser.getCurrentDirectory().getAbsolutePath() {}", jchooser.getCurrentDirectory().getAbsolutePath());
            return files;
        } else {
            files = null;
        }
        return files;
    }

    /*
     * Get the files from the "Load images" command  via Awt Filedialog
     */
    public static File[] getFileNamesAwt(JPanel myComponent) {

        Frame dialogframe = new Frame();
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        dialogframe.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        String startFolder = getFolderPathToOpenBasedOnPreferences();
        String userDefinedFilefilter = prefs.getByKey(USER_DEFINED_FILE_FILTER, "");
        logger.debug("startfolder for load images via Awt {}", startFolder);

        //logger.info("startfolder {}", startFolder);
        FileDialog fdchooser = new FileDialog(dialogframe, ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadimages"), FileDialog.LOAD);
        //Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        fdchooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
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
        if ( files.length == 0) {
            // no selection
            return files = null;
        }
        MyVariables.setLoadedFiles(files);
        prefs.storeByKey(LAST_OPENED_FOLDER, fdchooser.getDirectory());
        return files;
    }


    /*
    * Get the files from a folder via the "Load Directory" via JFilechooser
     */
    public static File[] getFolderFiles(JPanel myComponent) {
        File[] files = null;
        File[] realFiles = null;
        String SelectedFolder;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File startFolder = new File(getFolderPathToOpenBasedOnPreferences());
        logger.debug("startFolder for Opening folder with Jfilechooser {}", getFolderPathToOpenBasedOnPreferences());
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if (os == APPLE) {
            Path tmp = Paths.get(getFolderPathToOpenBasedOnPreferences());
            tmp = tmp.getParent();
            startFolder = tmp.toFile();
        }

        final JFileChooser jchooser = new JFileChooser(startFolder, fsv);
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        jchooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
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
            realFiles = stripFoldersFromFiles(files);
            MyVariables.setLoadedFiles(realFiles);
            prefs.storeByKey(LAST_OPENED_FOLDER, jchooser.getCurrentDirectory().getAbsolutePath());
            logger.debug("jchooser.getCurrentDirectory().getAbsolutePath() {}", jchooser.getCurrentDirectory().getAbsolutePath());

            return realFiles;
        } else {
            return files = null;
        }

        //return files;
    }

    /*
     * Get the files from a folder via the "Load Directory" via Awt Filedialog
     */
    public static File[] getFolderFilesAwt(JPanel myComponent) {
        File[] files = null;
        File[] realFiles = null;
        String SelectedFolder;

        Frame dialogframe = new Frame();
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        dialogframe.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
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
        FileDialog fdchooser = new FileDialog(dialogframe, ResourceBundle.getBundle("translations/program_strings").getString("stfio.loadfolder"), FileDialog.LOAD);
        fdchooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        fdchooser.setDirectory(startFolder);
        fdchooser.setMultipleMode(false);
        fdchooser.setVisible(true);

        SelectedFolder = fdchooser.getDirectory();
        if (SelectedFolder == null) {
            files = null;
        }
        File folder = new File(SelectedFolder);
        files = folder.listFiles();
        realFiles = stripFoldersFromFiles(files);
        MyVariables.setLoadedFiles(realFiles);
        prefs.storeByKey(LAST_OPENED_FOLDER, fdchooser.getDirectory());

        return realFiles;
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
        //logger.info("copyFrom {} copyTo {}", configFilePath, strfileToBe);
        File testFile = new File(strfileToBe);
        if (testFile.exists()) {
            testFile.delete();
        }
        try {
            Path path = Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
            copyResult = "successfully copied config file";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("copy of \"{}\" to \"{}\" failed with {}", configFilePath, strfileToBe, e.toString());
            copyResult = e.toString();
        }
        return copyResult;
    }

    /*
    / This method is called from the MetDataViewPanel and exports, when relevant, the custom config file to the "user home"
    */
    public static String ExportCustomConfigFile(String fileName) {
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        String strCopyTo = userHome + File.separator + fileName;
        String strCopyFrom = userHome + File.separator + MyConstants.MY_DATA_FOLDER + File.separator + fileName;
        String copyResult = "";
        //NIO copy with replace existing
        Path copyTo = Paths.get(strCopyTo);
        Path copyFrom = Paths.get(strCopyFrom);
        //logger.info("copyFrom {} copyTo {}", configFilePath, strfileToBe);
        File testFile = new File(userHome + File.separator + fileName);
        if (testFile.exists()) {
            testFile.delete();
        }
        try {
            Path path = Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
            copyResult = "successfully copied config file";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("copy of \"{}\" to \"{}\" failed with {}", strCopyFrom , strCopyTo, e.toString());
            copyResult = e.toString();
        }
        return copyResult;
    }

    /*
    / This method is used to import a custom config file from the path where the user also specified
    / the csv file for import
    */
    public static String ImportCustomConfigFile(String fileName) {
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        //String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
        String strCopyTo = userHome + File.separator + MyConstants.MY_DATA_FOLDER + File.separator + fileName;
        String strCopyFrom = fileName;
        String copyResult = "";
        //NIO copy with replace existing
        Path copyTo = Paths.get(strCopyTo);
        Path copyFrom = Paths.get(strCopyFrom);
        //logger.info("copyFrom {} copyTo {}", configFilePath, strfileToBe);
        File testFile = new File(strCopyTo);
        if (testFile.exists()) {
            testFile.delete();
        }
        try {
            Path path = Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
            copyResult = "successfully copied config file";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("copy of \"{}\" to \"{}\" failed with {}", strCopyFrom , strCopyTo, e.toString());
            copyResult = e.toString();
        }
        return copyResult;
    }

    static void moveFiles(File source, File destination) {
        try {
            FileUtils.copyDirectory(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create subfolders inside our jexiftoolgui data folder
    // We do this from our below checkforjexiftoolguiFolder()
    public static String checkforJTGsubfolder(String subFolderName) {

        String result = "";

        File newsubFolder = new File(subFolderName);
        if (!newsubFolder.exists()) { // the cache folder does not exist yet
            try {
                Files.createDirectories(Paths.get(subFolderName));
            } catch (IOException e) {
                logger.error("Error creating cache directory " + subFolderName);
                e.printStackTrace();
                result = "Error creating subfolder " + subFolderName;
            }
        }
        result = subFolderName;
        return result;
    }

    // Check if we have a jexiftoolgui_custom folder in $HOME with defaults
    public static String checkforjexiftoolguiFolder() {
        String method_result = "";
        String fileToBecopied = "";
        File copyFile = null;
        Path dataFolder, cacheFolder, logsFolder;
        dataFolder = cacheFolder = logsFolder = null;
        File fdataFolder, fcacheFolder, flogsFolder;
        fdataFolder = fcacheFolder = flogsFolder = null;
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        // Check if folder exists
        String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;

        /* Do this for later if we are really going to move our jexiftoolgui_data folder
        if (Utils.isOsFromMicrosoft()) {
            dataFolder = Paths.get(userHome, "AppData", "Roaming", MyConstants.MY_BASE_FOLDER);
            cacheFolder = Paths.get(userHome, "AppData", "Local", MyConstants.MY_BASE_FOLDER, "cache");
            logsFolder = Paths.get(userHome, "AppData", "Local", MyConstants.MY_BASE_FOLDER, "logs");
        } else {
            dataFolder = Paths.get(userHome, ".local", "share", MyConstants.MY_BASE_FOLDER, "data");
            cacheFolder = Paths.get(userHome, ".cache", MyConstants.MY_BASE_FOLDER);
            logsFolder = Paths.get(userHome, ".local", "share", MyConstants.MY_BASE_FOLDER, "logs");
        }
        */
        File jexiftoolguifolder = new File(strjexiftoolguifolder);
        /* Do this for later if we are really going to move our jexiftoolgui_data folder
        if (jexiftoolguifolder.exists()) {

            fcacheFolder = cacheFolder.toFile();
            if (!fcacheFolder.exists()) { // no cachefolder yet
                try {
                    Files.createDirectories(cacheFolder);
                    File oldCacheFolder = new File(strjexiftoolguifolder + File.separator + "cache");
                    moveFiles(new File(strjexiftoolguifolder + File.separator + "cache"), fcacheFolder);
                    oldCacheFolder.delete();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    method_result = "Error creating directory " + cacheFolder;
                    logger.error("Error creating directory " + cacheFolder);
                }
            }
            fdataFolder = dataFolder.toFile();
            if (!fdataFolder.exists()) { // no datafolder yet

            }
        }


        flogsFolder = logsFolder.toFile();


        File jexiftoolguifolder = new File(strjexiftoolguifolder);
        */
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

        // Check on our cache directory and if it doesn't exist: create it!
        String strsubfolder = checkforJTGsubfolder(strjexiftoolguifolder + File.separator + "cache");
        // Save this much used to a getter/setter
        MyVariables.setjexiftoolguiCacheFolder(strsubfolder);
        // Check on our lenses directory and if it doesn't exist: create it!
        strsubfolder = checkforJTGsubfolder(strjexiftoolguifolder + File.separator + "lenses");
        String result = extract_resource_to_jexiftoolguiFolder("example_lens.hashmap", strjexiftoolguifolder, "lenses");
        MyVariables.setlensFolder(strsubfolder);
        // Check on our custommetadataset directory and if it doesn't exist: create it!
        strsubfolder = checkforJTGsubfolder(strjexiftoolguifolder + File.separator + "custommetadatasets");
        MyVariables.setcustommetadatasetFolder(strsubfolder);
        // Check on our args directory and if it doesn't exist: create it!
        strsubfolder = checkforJTGsubfolder(strjexiftoolguifolder + File.separator + "args");
        String argsfolder = strjexiftoolguifolder + File.separator + "args";
        String args_files[] = {"exif2iptc.args","gps2xmp.args","iptc2xmp.args","pdf2xmp.args","xmp2gps.args","xmp2pdf.args","exif2xmp.args","iptc2exif.args","iptcCore.args","xmp2exif.args","xmp2iptc.args"};
        for (String args_file : args_files) {
            File tst_arg_file = new File(argsfolder + File.separator + args_file);
            if (!tst_arg_file.exists()) {
                result = extract_resource_to_jexiftoolguiFolder("args" + File.separator + args_file, strjexiftoolguifolder, "args");
            }
        }

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

        // Check if we have our (default) favorites.hashmap file
        fileToBecopied = strjexiftoolguifolder + File.separator + "favorites.hashmap";
        copyFile = new File(fileToBecopied);
        if (!copyFile.exists()) {
            logger.debug("no favorites.hashmap yet; trying to create it");
            method_result = extract_resource_to_jexiftoolguiFolder("favorites.hashmap", strjexiftoolguifolder, "");
            if ("success".equals(method_result)) {
                logger.info("copied the initial favorites.hashmap yet");
            }
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

                return targetfile.getPath();
            }
        } else {
            // simply return original path. Nothing to do
            logger.debug("No spaces in {}", checkPath);
            return checkPath;
        }
    }

    /*
    * This method delivers a folder path containing images to the several screens
     */
    public String getImagePath(JPanel myComponent) {
        String SelectedFolder;
        String prefFileDialog = prefs.getByKey(PREFERRED_FILEDIALOG, "jfilechooser");

        String startFolder = StandardFileIO.getFolderPathToOpenBasedOnPreferences();
        if ("jfilechooser".equals(prefFileDialog)) {
            final JFileChooser chooser = new JFileChooser(startFolder);
            Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
            chooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
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
            Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
            dialogframe.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
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

    /*
    / This method copies the thumb files from the tmp folder to the cache folder
    / It does this in a swingworker background process
     */
    public static void copyThumbsToCache() {
        // use with *ThumbnailImage.jpg and *PhotoshopThumbnail.jpg

        SwingWorker sw = new SwingWorker<Void, Void>() {
            public Void doInBackground() {

                String tmpWorkDir = MyVariables.gettmpWorkFolder();
                Path tmpworkDirPath = Paths.get(tmpWorkDir);
                File tmpWorkDirFile = new File(tmpWorkDir);
                String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
                File[] allExtractedThumbs = tmpWorkDirFile.listFiles();
                for (File thumb : allExtractedThumbs) {
                    String thumbName = thumb.getName();
                    logger.debug("tmp thumb file {} in listing", thumb.getName());
                    if ( (thumbName.contains("ThumbnailImage.jpg")) || (thumbName.contains("PhotoshopThumbnail.jpg")) ) {
                        logger.debug("copy thumb file {}", thumbName);
                        String strCopyTo = MyVariables.getjexiftoolguiCacheFolder() + File.separator;
                        String copyResult = "";
                        //NIO copy with replace existing
                        Path copyTo = Paths.get(strCopyTo + thumbName);
                        Path copyFrom = Paths.get(tmpWorkDir + File.separator + thumbName);
                        logger.debug("thumbs copy to cache: From {} To {}", copyFrom.toString(), copyTo.toString());
                        File testFile = new File(strCopyTo + thumbName);
                        if (testFile.exists()) {
                            testFile.delete();
                        }
                        try {
                            Path path = Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
                            copyResult = "successfully copied config file";
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.error("copy of \"{}\" to \"{}\" failed with {}", (tmpWorkDir + File.separator + thumbName) , strCopyTo, e.toString());
                            copyResult = e.toString();
                        }
                        // Now delete from our /tmp folder
                        File delThumb = new File(tmpWorkDir + File.separator + thumbName);
                        delThumb.delete();
                    }
                }
                return null;
            }
        };
        sw.execute();
    }

    /*
/ This method saves.copies a single icon to cache after having created this from an image without preview
/ It does this in a swingworker background process
 */
    public static void saveIconToCache(String fileName, BufferedImage bi) {
        // use with *ThumbnailImage.jpg and *PhotoshopThumbnail.jpg
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        String thumbFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "_ThumbnailImage.jpg";

        SwingWorker sw = new SwingWorker<Void, Void>() {
            public Void doInBackground() {

                logger.info("thumb to save {}", thumbFileName);
                logger.info("outputfile {}", MyVariables.getjexiftoolguiCacheFolder() + File.separator + thumbFileName.replaceAll(" ", "\\ "));
                File outputfile = new File(MyVariables.getjexiftoolguiCacheFolder() + File.separator + thumbFileName.replaceAll(" ", "\\ "));
                try {
                    ImageIO.write(bi, "JPEG", outputfile);
                } catch (IOException e) {
                    logger.error("saving icon to cache errors out with {}", e.toString());
                    e.printStackTrace();
                }
                return null;
            }
        };
        sw.execute();

    }

    // Write Hashmap file
    public static String writeHashMapToFile(File hashmapfile, HashMap<String, String> myHashMap) {
        String result = "";
        BufferedWriter bf = null;

        try {
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(hashmapfile));  // BufferedWriter overwites if exists, otherwise creates
            // iterate map entries
            for (Map.Entry<String, String> key_value : myHashMap.entrySet()) {
                // put key and value separated by a colon
                bf.write(key_value.getKey() + "::" + key_value.getValue());
                // new line
                bf.newLine();
            }
            bf.flush();
            result = "successfully saved";
        }
        catch (IOException e) {
            logger.error("Saving hashmap to file {} gives error {}", hashmapfile.toString(), e.toString());
            e.printStackTrace();
            result = "Error saving hashmap to file " + hashmapfile.toString();
        }
        finally {
            try {
                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }
        return result;
    }

    public static HashMap<String, String> readHashMapFromFile(File hashmapfile) {

        HashMap<String, String> myHashmap = new HashMap<String, String>();
        BufferedReader br = null;
        try {
            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(hashmapfile));

            String line = null;

            // read file line by line
            while ((line = br.readLine()) != null) {
                // split the line by the separator:
                String[] parts = line.split("::");
                // first part is name, second is number
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (!key.equals("") && !value.equals(""))
                    myHashmap.put(key, value);
            }
        }
        catch (Exception e) {
            logger.error("Loading hashmap from file {} failed with error {}", hashmapfile.getName(), e.toString());
            e.printStackTrace();
        }
        finally {
            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }

        return myHashmap;
    }

}
