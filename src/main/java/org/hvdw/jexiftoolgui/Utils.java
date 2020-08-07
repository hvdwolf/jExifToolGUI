package org.hvdw.jexiftoolgui;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.JavaImageViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hvdw.jexiftoolgui.Application.OS_NAMES.APPLE;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.*;

public class Utils {

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);


    public static boolean containsIndices(int[] selectedIndices) {
        List<Integer> intList = IntStream.of(selectedIndices).boxed().collect(Collectors.toList());
        return intList.size() != 0;
    }


    /*
    / Set default font for everything in the Application
    / from: https://stackoverflow.com/questions/7434845/setting-the-default-font-of-swing-program (Romain Hippeau)
    / To be called like : Utils.setUIFont (new FontUIResource("SansSerif", Font.PLAIN,12));
     */
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }

    /*
     * Opens the default browser of the Operating System
     * and displays the specified URL
     */
    static void openBrowser(String webUrl) {

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(webUrl));
                return;
            }
            Application.OS_NAMES os = Utils.getCurrentOsName();

            Runtime runtime = Runtime.getRuntime();
            switch (os) {
                case APPLE:
                    runtime.exec("open " + webUrl);
                    return;
                case LINUX:
                    runtime.exec("xdg-open " + webUrl);
                    return;
                case MICROSOFT:
                    runtime.exec("explorer " + webUrl);
                    return;
            }
        }
        catch (IOException | IllegalArgumentException e) {
            logger.error("Could not open browser", e);
        }
    }

    public static String systemProgramInfo() {
        StringBuilder infostring = new StringBuilder();

        infostring.append("<big>System and Program Info</big><hr><br><table width=\"90%\" border=0>");
        infostring.append("<tr><td>Operating system:</td><td>" + SystemPropertyFacade.getPropertyByKey(OS_NAME) + "</td></tr>");
        infostring.append("<tr><td>Operating system architecture:</td><td>" + SystemPropertyFacade.getPropertyByKey(OS_ARCH) + "</td></tr>");
        infostring.append("<tr><td>Operating system version:</td><td>" + SystemPropertyFacade.getPropertyByKey(OS_VERSION) + "<br><br></td></tr>");
        infostring.append("<tr><td>jExifToolGUI version:</td><td>" + ProgramTexts.Version + "</td></tr>");
        infostring.append("<tr><td>Exiftool version:</td><td>" + MyVariables.getExiftoolVersion() + "<br><br></td></tr>");
        infostring.append("<tr><td>java version:</td><td>" + SystemPropertyFacade.getPropertyByKey(JAVA_VERSION) + "</td></tr>");
        infostring.append("<tr><td>java home:</td><td>" + SystemPropertyFacade.getPropertyByKey(JAVA_HOME) + "</td></tr>");
        infostring.append("</table></html>");

        return infostring.toString();
    }
        /*
     * The ImageInfo method uses exiftool to read image info which is outputted as csv
     * This method converts it to 3-column "tabular" data
     */
    public static void readTagsCSV(String tagname) {
        List<List<String>> tagrecords = new LinkedList<>();
        String tags = StandardFileIO.readTextFileAsStringFromResource("resources/tagxml/" + tagname + ".xml");
        if (tags.length() > 0) {
            String[] lines = tags.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] tagvalues = line.split(",");
                tagrecords.add(Arrays.asList(tagvalues));
            }
        }
    }

    // Displays the license in an option pane
    static void showLicense(JPanel myComponent) {

        String license = StandardFileIO.readTextFileAsStringFromResource("COPYING");
        JTextArea textArea = new JTextArea(license);
        boolean isWindows = Utils.isOsFromMicrosoft();
        if (isWindows) {
            textArea.setFont(new Font("Sans_Serif", Font.PLAIN, 13));
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(myComponent, scrollPane, "GNU GENERAL PUBLIC LICENSE Version 3", JOptionPane.INFORMATION_MESSAGE);
    }

    // Shows or hides the progressbar when called from some (long) running method
    static void progressStatus(JProgressBar progressBar, Boolean show) {
        if (show) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setBorderPainted(true);
            progressBar.repaint();
        } else {
            progressBar.setVisible(false);
        }
    }

    /*
     * Checks whether the artist (xmp-dc:creator) and Copyright (xmp-dc:rights) and Credits (xmp:credits) preference exists
     * and uses these in the edit exif/xmp panes
     */
    static String[] checkPrefsArtistCreditsCopyRights() {
        String[] ArtCredCopyPrefs = {
                prefs.getByKey(ARTIST, ""),
                prefs.getByKey(CREDIT, ""),
                prefs.getByKey(COPYRIGHTS, "")
        };
        return ArtCredCopyPrefs;
    }

    /////////////////// Locate exiftool //////////////
    /*
     * File chooser to locate exiftool when user comes from checkExifTool
     * and selected "Specify Location"
     */
    static String whereIsExiftool(JPanel myComponent) {
        String exiftool = "";
        String selectedBinary = "";

        boolean isWindows = isOsFromMicrosoft();

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
    static void checkExifTool(JPanel myComponent) {
        String returnValue = "";
        String[] options = {"specify location", "Download", "Stop"};
        //JOptionPane.showOptionDialog(null,"I can not find exiftool in the preferences or I can not find exiftool at all","exiftool missing",JOptionPane.ERROR_MESSAGE);
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ProgramTexts.noExifTool), "exiftool missing",
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
            openBrowser("https://www.sno.phy.queensu.ca/~phil/exiftool/");
            System.exit(0);
        } else {
            // exit program
            System.exit(0);
        }

    }

    /*
     * This method checks for a new version on the repo.
     * It can be called from startup (preferences setting) or from the Help menu
     */
    static void checkForNewVersion(String fromWhere) {
        String web_version = "";
        boolean versioncheck = prefs.getByKey(VERSION_CHECK, false);


        if (fromWhere.equals("menu") || versioncheck) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/hvdwolf/jExifToolGUI/master/version.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                web_version = in.readLine();
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String jv = SystemPropertyFacade.getPropertyByKey(JAVA_VERSION);
            logger.info("Using java version {}: ", jv);
            logger.info("Version on the web: " + web_version);
            logger.info("This version: " + ProgramTexts.Version);
            int version_compare = web_version.compareTo(ProgramTexts.Version);
            if ( version_compare > 0 ) { // This means the version on the web is newer
            //if (Float.valueOf(web_version) > Float.valueOf(ProgramTexts.Version)) {
                String[] options = {"No", "Yes"};
                int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("msd.jtgnewversionlong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.jtgnewversion"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    // Do something
                    openBrowser("https://github.com/hvdwolf/jExifToolGUI/releases");
                    System.exit(0);
                }

            } else {
                if (fromWhere.equals("menu")) {
                    JOptionPane.showMessageDialog(null, String.format(ProgramTexts.HTML, 250, ResourceBundle.getBundle("translations/program_strings").getString("msd.jtglatestversionlong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.jtglatestversion"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // Create correct exiftool command call depending on operating system
    public static String platformExiftool() {
        // exiftool on windows or other
        String exiftool = prefs.getByKey(EXIFTOOL_PATH, "");
        if (isOsFromMicrosoft()) {
            exiftool = exiftool.replace("\\", "/");
        }
        return exiftool;
    }

    // Get the configured metadata
    public static String getmetadataLanguage() {
        String metadatalanguage = prefs.getByKey(METADATA_LANGUAGE, "");
        if ( ("".equals(metadatalanguage)) || ("exiftool - default".equals(metadatalanguage)) ) {
            return "";
        } else {
            String[] parts = metadatalanguage.split(" - ");
            //logger.debug("metadatalanguage: " + parts[0]);
            //return "-lang " + parts[0];
            return parts[0];
        }
    }


    public static String stringBetween(String value, String before, String after) {
        // Return a substring between the two strings before and after.
        int posA = value.indexOf(before);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(after);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + before.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }
    ////////////////////////////////// Load images and display them  ///////////////////////////////////
    public static String getFileExtension(String filename) {

        int lastIndexOf = filename.lastIndexOf(".") + 1;
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return filename.substring(lastIndexOf);
    }

    /*
    * Create the icon
    */
    static ImageIcon createIcon(File file) {
        ImageIcon icon = null;
        int[] basicdata = {0, 0, 0};
        boolean bde = false;
        try {
            try {
                basicdata = ImageFunctions.getbasicImageData(file);
                //logger.info("Width {} Height {} Orientation {}", String.valueOf(basicdata[0]), String.valueOf(basicdata[1]), String.valueOf(basicdata[2]));
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                bde = true;

            }
            //logger.info("after getbasicdata");
            if (bde) {
                // We had some error. Mostly this is the orientation
                basicdata[2]= 1;
            }
            BufferedImage img = ImageIO.read(new File(file.getPath().replace("\\", "/")));
            BufferedImage resizedImg =  ImageFunctions.scaleImageToContainer(img, 160, 160);
            //logger.info("after scaleImageToContainer");
            if (basicdata[2] > 1) {
                resizedImg = ImageFunctions.rotate(resizedImg, basicdata[2]);
            }

            //logger.info("after rotate");

            icon = new ImageIcon(resizedImg);
            return icon;
        } catch (IIOException iex) {
            icon = null;
        } catch (IOException ex) {
            logger.error("Error loading image", ex);
            icon = null;
        }
        return icon;
    }

    /*
    / On Mac we let sips do the conversion of tif and heic images to previews
    / like "sips -s format JPEG -Z 160 test.heic --out test.jpg"
     */
    static String sipsConvertToJPG(File file, String size) {
        //ImageIcon icon = null;
        //Runtime runtime = Runtime.getRuntime();
        List<String> cmdparams = new ArrayList<String>();
        String exportResult = "Success";

        cmdparams.add("/usr/bin/sips");
        cmdparams.add("-s");
        cmdparams.add("format");
        cmdparams.add("JPEG");
        if (size.toLowerCase().equals("thumb")) {
            cmdparams.add("-Z");
            cmdparams.add("160");
        }
        // Get the file
        cmdparams.add(file.getPath().replace(" ", "\\ "));
        //cmdparams.add("\"" + file.getPath() + "\"");
        cmdparams.add("--out");

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();
        // Get the file name without extension
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        cmdparams.add(tempWorkDir + File.separator + fileName +".jpg");
        logger.info("final sips command: " + cmdparams.toString());

        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            //logger.info("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing sipd command to convert to jpg");
        }
        return exportResult;
    }
    /*
     * Display the loaded files with icon and name
     */
    static void displayFiles(JTable jTable_File_Names, JTable ListexiftoolInfotable, JLabel Thumbview) {
        int selectedRow, selectedColumn;

        boolean heicextension = false;
        String[] SimpleExtensions = MyConstants.JAVA_SUP_EXTENSIONS;

        boolean bSimpleExtension = false;
        String thumbfilename = "";
        File thumbfile = null;
        ImageIcon icon = null;
        File[] files = MyVariables.getSelectedFiles();
        DefaultTableModel model = (DefaultTableModel) jTable_File_Names.getModel();
        //model.setColumnIdentifiers(new String[]{"File Name(s)"});
        //ListexiftoolInfotable.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        jTable_File_Names.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            protected void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    setIcon(null);
                    super.setValue(value);
                }
            }
        });

        //model.setColumnIdentifiers(new String[]{"A", "B", "C"});
        model.setColumnIdentifiers(new String[]{"Photo", "Filename"});
        model.setRowCount(0);
        model.fireTableDataChanged();
        jTable_File_Names.clearSelection();
        jTable_File_Names.setCellSelectionEnabled(true);
        Object[] row = new Object[1];
        Object[] ImgRow = new Object[3];
        Object[] FilenameRow = new Object[3];
        Object[] ImgFilenameRow = new Object[2];
        String filename = "";
        int trow = 0;
        int tcolumn = 0;

        Application.OS_NAMES currentOsName = getCurrentOsName();
        for (File file : files) {
            //logger.trace(file.getName().replace("\\", "/"));
            bSimpleExtension = false;
            filename = file.getName().replace("\\", "/");
            //logger.info("Now working on image: " +filename);
            String filenameExt = getFileExtension(filename);
            if (filenameExt.toLowerCase().equals("heic")) {
                heicextension = true;
            }
            for (String ext : SimpleExtensions) {
                if (filenameExt.toLowerCase().equals(ext)) { // it is either bmp, gif, jp(e)g, png or tif(f)
                    bSimpleExtension = true;
                    break;
                }
            }

            if ( (heicextension) && currentOsName == APPLE) { // For Apple we deviate
//            if ( (tifextension || heicextension) && currentOsName == APPLE) { // For Apple we deviate
                logger.info("do sipsConvertToJPG for {}", filename);
                String exportResult = sipsConvertToJPG(file, "thumb");
                if ("Success".equals(exportResult)) {
                    logger.info("back from sipsconvert: result {}", exportResult);
                    //Hoping we have a thumbnail
                    thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + ".jpg";
                    thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    if (thumbfile.exists()) {
                        // Create icon of this thumbnail (thumbnail is 90% 160x120 already, but resize it anyway
                        //logger.info("create thumb nr1");
                        icon = createIcon(thumbfile);
                        if (icon != null) {
                            // display our created icon from the thumbnail
                            ImgFilenameRow[0] = icon;
                        }
                    }
                }
                //reset our heic flag
                heicextension = false;
            } else if (bSimpleExtension) {
                icon = createIcon(file);
                ImgFilenameRow[0] = icon;
            } else { //We have a RAW image extension or tiff or something else like audio/video
                // Export previews for current (RAW) image to tempWorkfolder
                String exportResult = ExportPreviewsThumbnailsForIconDisplay(file);
                if ("Success".equals(exportResult)) {
                    //Hoping we have a thumbnail
                    thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
                    thumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    //logger.info("thumb nr1:"  + MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    if (thumbfile.exists()) {
                        // Create icon of this thumbnail (thumbnail is 90% 160x120 already, but resize it anyway
                        //logger.info("create thumb nr1");
                        icon = createIcon(thumbfile);
                        if (icon != null) {
                            // display our created icon from the thumbnail
                            ImgFilenameRow[0] = icon;
                        }
                    } else { //thumbnail image probably doesn't exist, move to 2nd option
                        thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
                        thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                        if (thumbfile.exists()) {
                            // Create icon of this Preview
                            //logger.info("create thumb nr2");
                            icon = createIcon(thumbfile);
                            if (icon != null) {
                                // display our created icon from the preview
                                ImgFilenameRow[0] = icon;
                            }
                        } else { // So thumbnail and previewImage don't exist. Try 3rd option
                            thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_JpgFromRaw.jpg";
                            thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                            if (thumbfile.exists()) {
                                // Create icon of this Preview
                                icon = createIcon(thumbfile);
                                if (icon != null) {
                                    // display our created icon from the preview
                                    ImgFilenameRow[0] = icon;
                                }
                            } else {
                                // Use the cantdisplay.png for this preview. Should actually not be necessary here
                                thumbfile = new File(MyVariables.getcantdisplaypng());
                                if (thumbfile.exists()) {
                                    // Create icon of this Preview
                                    icon = createIcon(thumbfile);
                                    if (icon != null) {
                                        // display our created icon from the preview
                                        ImgFilenameRow[0] = icon;
                                    }
                                }
                            } // end of 3rd option creation ("else if") and cantdisplaypng option (else)
                        } // end of 2nd option creation ("else if") and 3rd option creation (else)
                    } // end of 1st option creation ("else if") and 2nd option creation (else)

                } else { // Our "String exportResult = ExportPreviewsThumbnailsForIconDisplay(file);"  completely failed due to some weird RAW format
                    // Use the cantdisplay.png for this preview
                    thumbfile = new File(MyVariables.getcantdisplaypng());
                    if (thumbfile.exists()) {
                        // Create icon of this Preview
                        icon = createIcon(thumbfile);
                        if (icon != null) {
                            // display our created icon from the preview
                            ImgFilenameRow[0] = icon;
                        }
                    }
                }

            }

            ImgFilenameRow[1] = filename;
            jTable_File_Names.setRowHeight(150);
            model.addRow(ImgFilenameRow);
        }

        MyVariables.setSelectedRow(0);
        MyVariables.setSelectedColumn(0);
    }

    /*
     * This is the ImageInfo method that is called by all when displaying the exiftool info from the image
     */
    //static void getImageInfoFromSelectedFile(String[] whichInfo, int selectedRow, File[] files, JTable ListexiftoolInfotable) {
    static void getImageInfoFromSelectedFile(String[] whichInfo, File[] files, JTable ListexiftoolInfotable) {

        String fpath = "";
        List<String> cmdparams = new ArrayList<String>();
        int selectedRow = MyVariables.getSelectedRow();

        //logger.info("selectedRow: {}", String.valueOf(selectedRow));
        if (isOsFromMicrosoft()) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[selectedRow].getPath();
        }
        //Testje metadata extractor
        /*logger.info("\n\nStart of test metadata extractor {}\n\n", fpath);
        File imgfile = new File(files[selectedRow].getPath());
        ImageFunctions.getbasicImageData(imgfile); */

        // Need to build exiftool prefs check
        MyVariables.setSelectedImagePath(fpath);
        Application.OS_NAMES currentOsName = getCurrentOsName();

        cmdparams.add(Utils.platformExiftool().trim());
        // Check for chosen metadata language
        if (!"".equals(getmetadataLanguage())) {
            cmdparams.add("-lang");
            cmdparams.add(getmetadataLanguage());
        }
        cmdparams.addAll(Arrays.asList(whichInfo));
        logger.trace("image file path: {}", fpath);
        cmdparams.add(MyVariables.getSelectedImagePath());

        logger.trace("before runCommand: {}", cmdparams);
        try {
            String res = CommandRunner.runCommand(cmdparams);
            logger.trace("res is {}", res);
            displayInfoForSelectedImage(res, ListexiftoolInfotable);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command", ex);
        }

    }

    // This is the "pre-ImageInfo" that is called when the option is chosen to display for a specific Tag Name from the dropdown list without changing the selected image.
    static void selectImageInfoByTagName(JComboBox comboBoxViewByTagName, File[] files, JTable ListexiftoolInfotable) {

        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        params[1] = "-G";
        params[2] = "-tab";
        getImageInfoFromSelectedFile(params, files, ListexiftoolInfotable);
    }

    // This is for the "all tags" and "camera makes"
    static String[] getWhichTagSelected(JComboBox comboBoxViewByTagName) {
        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        params[1] = "-G";
        params[2] = "-tab";
        return params;
    }

    // This is for the Common Tags as they can contain combined info
    static String[] getWhichCommonTagSelected(JComboBox comboBoxViewByTagName) {
        String[] params = {"-all"}; // We need to initialize with something
        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());

        switch (SelectedTagName) {
            case "exif":
                params = MyConstants.EXIF_PARAMS;
                break;
            case "xmp":
                params = MyConstants.XMP_PARAMS;
                break;
            case "iptc":
                params = MyConstants.IPTC_PARAMS;
                break;
            case "composite":
                params = MyConstants.COMPOSITE_PARAMS;
                break;
            case "gps":
                params = MyConstants.GPS_PARAMS;
                break;
            case "location":
                params = MyConstants.LOCATION_PARAMS;
                break;
            case "lens data":
                params = MyConstants.LENS_PARAMS;
                break;
            case "gpano":
                params = MyConstants.GPANO_PARAMS;
                break;
            case "icc_profile":
                params = MyConstants.ICC_PARAMS;
                break;
            case "makernotes":
                params = MyConstants.MAKERNOTES_PARAMS;
                break;
        }
        return params;
    }

    /*
     * This method displays the exiftool info in the right 3-column table
     */
    private static void displayInfoForSelectedImage(String exiftoolInfo, JTable ListexiftoolInfotable) {
        // This will display the exif info in the right panel

        DefaultTableModel model = (DefaultTableModel) ListexiftoolInfotable.getModel();
        model.setColumnIdentifiers(new String[]{ ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablegroup"),
                ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tabletag"),
                ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablevalue")});
        ListexiftoolInfotable.getColumnModel().getColumn(0).setPreferredWidth(100);
        ListexiftoolInfotable.getColumnModel().getColumn(1).setPreferredWidth(260);
        ListexiftoolInfotable.getColumnModel().getColumn(2).setPreferredWidth(500);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (exiftoolInfo.length() > 0) {
            String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String[] cells = line.split("\\t", 3);
                model.addRow(new Object[]{cells[0], cells[1], cells[2]});
            }
        }

    }

    /*
     * This method displays the selected image in the default image viewer for the relevant mime-type (the extension mostly)
     */
    public static void displaySelectedImageInDefaultViewer(int selectedRow, File[] files, JLabel ThumbView) throws IOException {
        String fpath = "";
        if (isOsFromMicrosoft()) {
            fpath = "\"" + files[selectedRow].getPath().replace("\\", "/") + "\"";
            logger.trace("fpath is now: {}", fpath);
        } else {
            fpath = "\"" + files[selectedRow].getPath() + "\"";
        }
        BufferedImage img = ImageIO.read(new File(fpath));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 225, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 225, null);
        g2.dispose();
        ImageIcon icon = new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }


    public static String getExiftoolPath() {
        String res;
        List<String> cmdparams;
        Application.OS_NAMES currentOs = getCurrentOsName();

        if (currentOs == Application.OS_NAMES.MICROSOFT) {
            String[] params = {"where", "exiftool"};
            cmdparams = Arrays.asList(params);
        } else {
            String[] params = {"which", "exiftool"};
            cmdparams = Arrays.asList(params);
        }

        try {
            res = CommandRunner.runCommand(cmdparams); // res returns path to exiftool; on error on windows "INFO: Could not ...", on linux returns nothing
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
            res = ex.getMessage();
        }

        return res;
    }

    /*
     * This method displays a preview of the image in the bottom left panel
     * THIS METHOD IS NOT USED ANYMORE
     */
    public void displayTableImage(int selectedRow, File[] files, JLabel ThumbView) throws IOException {
        String fpath = "";
        if (isOsFromMicrosoft()) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
            logger.trace("fpath is now: {}", fpath);
        } else {
            fpath = files[selectedRow].getPath();
        }
        BufferedImage img = ImageIO.read(new File(fpath));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 225, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 225, null);
        g2.dispose();
        ImageIcon icon = new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }

    /*
     * This method displays the program logo
     * * THIS METHOD IS NOT USED CURRENTLY
     */
    public void displayLogo(JLabel ThumbView) throws IOException {
        BufferedImage img = ImageIO.read(Utils.getResource("icons/jexiftoolgui-splashlogo.jpg"));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 300, null);
        g2.dispose();
        ImageIcon icon = new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }

    /*
    *
     */
    static void setCopyMetaDatacheckboxes(boolean state, JCheckBox[] CopyMetaDatacheckboxes) {
        for (JCheckBox chkbx : CopyMetaDatacheckboxes) {
            chkbx.setEnabled(state);
        }
    }
    /*
    / Retrieves the icone that is used for the window bar icons in the app (windows/linux)
     */
    static public BufferedImage getFrameIcon() {
         BufferedImage frameicon = null;
         try {
             frameicon = ImageIO.read(mainScreen.class.getResource("/icons/jexiftoolgui-frameicon.png"));
         } catch (IOException ioe) {
             logger.info("erorr loading frame icon {}", ioe.toString());
         }
         return frameicon;
    }
    /*
/ This method is called from displaySelectedImageInExternalViewer() in case of
/ - no raw viewer defined
/ - default image
/ - whatever other file type we encounter
 */
    static void viewInRawViewer(String RawViewerPath) {
        String command = ""; // macos/linux
        String[] commands = {}; // windows
        List<String> cmdparams = new ArrayList<String>();
        logger.info("bestand {} ", MyVariables.getSelectedImagePath());
        //String correctedPath = MyVariables.getSelectedImagePath().replace("\"", ""); //Can contain double quotes when double-clicked

        logger.info("RAW viewer started (trying to start)");
        Application.OS_NAMES currentOsName = getCurrentOsName();
        Runtime runtime = Runtime.getRuntime();
        //ProcessBuilder builder = new ProcessBuilder(cmdparams);
        //Process process;
        try {
            switch (currentOsName) {
                case APPLE:
                    String file_ext = getFileExtension(RawViewerPath);
                    if ("app".equals(file_ext)) {
                        command = "open " + RawViewerPath + " " + StandardFileIO.noSpacePath();
                    } else {
                        command = RawViewerPath + " " + StandardFileIO.noSpacePath();
                    }
                    runtime.exec(command);
                    return;
                case MICROSOFT:
                    String convImg = "\"" + StandardFileIO.noSpacePath().replace("/", "\\") + "\"";
                    commands = new String[] {RawViewerPath, convImg};
                    runtime.exec(commands);
                    return;
                case LINUX:
                    command = RawViewerPath + " " + StandardFileIO.noSpacePath();
                    runtime.exec(command);
                    return;
            }
        } catch (IOException e) {
            logger.error("Could not open image app.", e);
        }
    }

    /*
    / This method is called from displaySelectedImageInExternalViewer() in case of
    / - no raw viewer defined
    / - default image
    / - whatever other file type we encounter
     */
    static void viewInDefaultViewer() {

        JavaImageViewer JIV = new JavaImageViewer();
        JIV.ViewImageInFullscreenFrame();
    }

    /*
    / This method is called from main screen. It needs to detect if we have a raw image, a bmp/gif/jpg/png image, or whatever other kind of file
    / If it is a raw image and we have a raw viewer configured, use method viewInRawViewer
    / if it is an image (whatever) and we always want to use the raw viewer, use method viewInRawViewer
    / If no raw viewer defined, or we have whatever other extension (can also be normal or raw image), use method viewInDefaultViewer (based on mime type and default app)
     */
    static void displaySelectedImageInExternalViewer() {
        //String[] SimpleExtensions = {"bmp","gif,","jpg", "jpeg", "png"};
        String[] SimpleExtensions = MyConstants.JAVA_SUP_EXTENSIONS;;

        String RawViewer = prefs.getByKey(RAW_VIEWER_PATH, "");
        boolean AlwaysUseRawViewer = prefs.getByKey(RAW_VIEWER_ALL_IMAGES, false);
        boolean RawExtension = false;
        boolean defaultImg = false;

        Application.OS_NAMES currentOsName = getCurrentOsName();
        // check first if we have a raw image (can also be audio/video/whatever)
        String[] raw_images = MyConstants.RAW_IMAGES;
        String filenameExt = getFileExtension(MyVariables.getSelectedImagePath());
        for (String ext: raw_images) {
            if ( filenameExt.toLowerCase().equals(ext)) { // it is a raw image
                RawExtension = true;
                logger.info("RawExtension is true");
                break;
            }
        }
        if (!RawExtension) { //check if we have another image
            for (String ext : SimpleExtensions) {
                if ( filenameExt.toLowerCase().equals(ext)) { // it is a bmp, gif, jpg, png image or tif(f)
                    defaultImg = true;
                    //logger.info("default image is true");
                    break;
                } else if ( filenameExt.toLowerCase().equals("heic") && (currentOsName == APPLE) ) { // We first need to use sips to convert
                    File file = new File (MyVariables.getSelectedImagePath());
                    String filename = file.getName();
                    String exportResult = sipsConvertToJPG(file, "full");
                    String dispfilename = filename.substring(0, filename.lastIndexOf('.')) + ".jpg";
                    File dispfile = new File(MyVariables.gettmpWorkFolder() + File.separator + dispfilename);
                }
            }
        }
        if (RawExtension || defaultImg) { // we have an image
            if (AlwaysUseRawViewer) {
                viewInRawViewer(RawViewer);
            } else if (RawExtension) {
                if (!"".equals(RawViewer)) { // We do have a raw image AND a raw viewer defined
                    viewInRawViewer(RawViewer);
                }
            } else { // We have a defaultImg
                viewInDefaultViewer();
            }
        } else { // Whatever other extension, simply try by default mime type
            viewInDefaultViewer();
        }

    }

    public static Application.OS_NAMES getCurrentOsName() {
        String OS = SystemPropertyFacade.getPropertyByKey(OS_NAME).toLowerCase();
        if (OS.contains("mac")) return APPLE;
        if (OS.contains("windows")) return Application.OS_NAMES.MICROSOFT;
        return Application.OS_NAMES.LINUX;
    }

    public static boolean isOsFromMicrosoft() {
        return getCurrentOsName() == Application.OS_NAMES.MICROSOFT;
    }
    public static boolean isOsFromApple() {
        return getCurrentOsName() == APPLE;
    }

    public static boolean in_Range(int checkvalue, int lower, int upper) {
        // note: lower >=; upper <
        if (checkvalue >= lower && checkvalue < upper) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] gpsCalculator(JPanel rootPanel, String[] input_lat_lon) {
        // Here is where we calculate the degrees-minutes-seconds to decimal values
        float coordinate;
        //float coordinate;
        String[] lat_lon = {"", ""};

        // No complex method. Simply write it out
        // first latitude
        // Note that "South" latitudes and "West" longitudes convert to negative decimal numbers.
        // In decs-mins-secs, degrees < 90, minutes < 60, seconds <60. NOT <=
        if ( in_Range(Integer.parseInt(input_lat_lon[2]), 0, 60)) { //secs
            coordinate = Float.parseFloat(input_lat_lon[2]) / (float) 60;
            logger.info("converted seconds: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcsecerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[1]), 0, 60)) { //mins
            coordinate = (Float.parseFloat( input_lat_lon[1]) + coordinate) / (float) 60;
            logger.info("converted mins + secs: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcminerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[0]), 0, 90)) { //degs
            coordinate = Float.parseFloat( input_lat_lon[0]) + coordinate;
            if ("S".equals(input_lat_lon[3])) { //South means negative value
                coordinate = -(coordinate);
            }
            logger.info("decimal lat: " + coordinate);
            lat_lon[0] = String.valueOf(coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcdegerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        
        // Now do the same for longitude
        if ( in_Range(Integer.parseInt(input_lat_lon[6]), 0, 60)) { //secs
            coordinate = Float.parseFloat(input_lat_lon[6]) / (float) 60;
            logger.info("converted seconds: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcsecerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[5]), 0, 60)) { //mins
            coordinate = (Float.parseFloat( input_lat_lon[5]) + coordinate) / (float) 60;
            logger.info("converted mins + secs: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcminerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[4]), 0, 90)) { //degs
            coordinate = Float.parseFloat( input_lat_lon[4]) + coordinate;
            if ("W".equals(input_lat_lon[7])) { //West means negative value
                coordinate = -(coordinate);
            }
            lat_lon[1] = String.valueOf(coordinate);
            logger.info("decimal lon: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcdegerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }

        return lat_lon;
    }

    /*
    * This method is used to export all avalaible previews/thumbnails/jpegFromRaw which are in the selected images
     */
    public static void ExportPreviewsThumbnails(JProgressBar progressBar) {
        // tmpPath is optional and only used to create previews of raw images which can't be displayed directly
        List<String> cmdparams = new ArrayList<String>();
        File myFilePath;
        String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.no"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.yes")};
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();

        logger.info("Export previews/thumbnails..");
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("ept.dlgtext")),ResourceBundle.getBundle("translations/program_strings").getString("ept.dlgtitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            cmdparams.add(Utils.platformExiftool());
            boolean isWindows = Utils.isOsFromMicrosoft();

            myFilePath = files[0];
            String absolutePath = myFilePath.getAbsolutePath();
            String myPath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
            if (isWindows) {
                myPath = myFilePath.getPath().replace("\\", "/");
            }
            cmdparams.add("-a");
            cmdparams.add("-m");
            cmdparams.add("-b");
            cmdparams.add("-W");
            cmdparams.add(myPath + File.separator + "%f_%t%-c.%s");
            cmdparams.add("-preview:all");


            for (int index: selectedIndices) {
                //logger.info("index: {}  image path: {}", index, files[index].getPath());
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

    /*
    * This method is used to try to get a preview image for those (RAW) images that can't be converted directly to be displayed in the left images column
    * We will try to extract a jpg from the RAW to the tempdir and resize/display that one
     */
    public static String ExportPreviewsThumbnailsForIconDisplay(File file) {
        List<String> cmdparams = new ArrayList<String>();
        String exportResult = "Success";

        cmdparams.add(Utils.platformExiftool());
        boolean isWindows = Utils.isOsFromMicrosoft();

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();

        cmdparams.add("-a");
        cmdparams.add("-m");
        cmdparams.add("-b");
        cmdparams.add("-W");
        cmdparams.add(tempWorkDir + File.separator + "%f_%t%-c.%s");
        cmdparams.add("-preview:ThumbnailImage");
        cmdparams.add("-preview:PreviewImage");

        if (isWindows) {
            cmdparams.add(file.getPath().replace("\\", "/"));
        } else {
            cmdparams.add(file.getPath());
        }

        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            //logger.info("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command to export previes for one RAW");
            exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }
        return exportResult;
    }


    @SuppressWarnings("SameParameterValue")
    private static URL getResource(String path) {
        return Utils.class.getClassLoader().getResource(path);
    }
}
