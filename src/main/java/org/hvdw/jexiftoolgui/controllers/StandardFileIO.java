package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.prefs.Preferences;

public class StandardFileIO {

    private static Preferences prefs = Preferences.userRoot();

    public static String TextFileReader (String fileName) {
        // This will reference one line at a time
        String line = null;
        String totalText = "";

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                totalText = totalText + line;
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }

        return totalText;
    }


    public static InputStream getResourceAsStream(String path) {
        return Utils.class.getClassLoader().getResourceAsStream(path);
    }

    // Reads a text file from resources
    public static String ResourceReader(String fileName) {
        String strFileContents = "";

        try {
            InputStream is = getResourceAsStream(fileName);
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            while((bytesRead = is.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }

        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return strFileContents;
    }

    /* General check method which folder to open
     * Based on preference default folder, "always Use Last used folder" or home folder
     */
    public static String whichFolderToOpen() {
        boolean imageDefaultFolder_exists = false;
        boolean uselastopenedfolder_exists = false;
        String startFolder = "";
        Boolean uselastopenedfolder = false;

        uselastopenedfolder_exists = prefs.getBoolean("uselastopenedfolder", false) != false;
        if (uselastopenedfolder_exists) {
            if (prefs.getBoolean("uselastopenedfolder", false)) {
                startFolder = prefs.get("lastopenedfolder", System.getProperty("user.home"));
            } else {
                imageDefaultFolder_exists = prefs.get("defaultstartfolder", null) != null;
                if (imageDefaultFolder_exists) {
                    startFolder = prefs.get("defaultstartfolder", "");
                } else {
                    startFolder = System.getProperty("user.home");
                }
            }
        } else { // The uselastfolder settings is not made yet
            imageDefaultFolder_exists = prefs.get("defaultstartfolder", null) != null;
            if (imageDefaultFolder_exists) {
                startFolder = prefs.get("defaultstartfolder", "");
            } else {
                startFolder = System.getProperty("user.home");
            }
        }

        return startFolder;
    }

    /*
     * Get the files from the "Load images" command
     */
    public static File[] getFileNames(JPanel myComponent) {
        File[] files = null;
        boolean imageDefaultFolder_exists = false;
        String startFolder = "";

        startFolder = whichFolderToOpen();

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
            prefs.node("lastopenedfolder");
            prefs.put("lastopenedfolder", chooser.getSelectedFile().getAbsolutePath());
        } else if (status == JFileChooser.CANCEL_OPTION) {
            files = null;;
        }
        return files;
    }
}
