package org.hvdw.jexiftoolgui;

import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    private static Preferences prefs = Preferences.userRoot();


    public static boolean containsIndices(int[] selectedIndices) {
        List<Integer> intList = IntStream.of(selectedIndices).boxed().collect(Collectors.toList());
        return intList.size() != 0;
    }

    /*
     * Opens the default browser of the Operating System
     * and displays the specified URL
     */
    static void openBrowser(String webUrl) {
        if(Desktop.isDesktopSupported()) { //probably windows, but could also be linux with Gnome
            try {
                Desktop.getDesktop().browse(new URI(webUrl));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            String OS = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (OS.contains("mac")) { //Apple
                try {
                    runtime.exec("open " + webUrl);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else { //Linux, or "something else"
                try {
                    runtime.exec("xdg-open " + webUrl);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    /*
     * The ImageInfo method uses exiftool to read image info which is outputted as csv
     * This method converts it to 3-column "tabular" data
     */
    public static void readTagsCSV(String tagname) {
        List<List<String>> tagrecords = new ArrayList<>();
        String tags = StandardFileIO.readTextFileAsStringFromResource("resources/tagxml/" + tagname + ".xml");
        if (tags.length() > 0) {
            String[] lines = tags.split(System.getProperty("line.separator"));

            for(int i = 0; i < lines.length; i++) {
                String[] tagvalues = lines[i].split(",");
                tagrecords.add(Arrays.asList(tagvalues));
            }
        }
    }

    // Displays the license in an option pane
    static void showLicense(JPanel myComponent) {

        String license = StandardFileIO.readTextFileAsStringFromResource("COPYING");
        JTextArea textArea = new JTextArea(license);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        JOptionPane.showMessageDialog(myComponent, scrollPane,"GNU GENERAL PUBLIC LICENSE Version 3",JOptionPane.INFORMATION_MESSAGE);
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
     * Checks whether the artist (creator) and Copyright (rights) preference exists
     * and uses these in the edit exif/xmp panes
     */
    static String[] checkPrefsArtistCopyRights() {
        String[] ArtistCopyRights = new String[2];
        boolean prefArtistExists = prefs.get("artist", null) != null;
        if (prefArtistExists) {
            ArtistCopyRights[0] = prefs.get("artist", "");
        } else {
            ArtistCopyRights[0] = "";
        }

        boolean prefCopyRightsExists = prefs.get("copyrights", null) != null;
        if (prefCopyRightsExists) {
            ArtistCopyRights[1] = prefs.get("copyrights", "");
        } else {
            ArtistCopyRights[1] = "";
        }
        return ArtistCopyRights;
    }

    /////////////////// Locate exiftool //////////////
    /*
     * File chooser to locate exiftool when user comes from checkExifTool
     * and selected "Specify Location"
     */
    static String whereIsExiftool(JPanel myComponent) {
        String exiftool = "";
        String selectedBinary = "";

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

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
                if (tmpstr.contains("exiftool.exe")){
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
            exiftool =  "cancelled";
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
        int choice = JOptionPane.showOptionDialog(null,String.format(ProgramTexts.HTML, 450, ProgramTexts.noExifTool),"exiftool missing",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // open file chooser
            // Do this from the PreferencesDialog class as it atually belongs there
            returnValue = whereIsExiftool(myComponent);
            if (returnValue.equals("cancelled")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.cancelledETlocatefromStartup,"Cancelled locate ExifTool",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if (returnValue.equals("no exiftool binary")) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromStartup,"Wrong executable",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else { // Yes. It looks like we have a correct exiftool selected
                // remove all possible line breaks
                returnValue = returnValue.replace("\n", "").replace("\r", "");
                prefs.put("exiftool", returnValue);
            }
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 450, ProgramTexts.downloadInstallET),"Download ExifTool",JOptionPane.INFORMATION_MESSAGE);
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
        String version = "";
        boolean newversion_startupcheck_exists = false;
        boolean versioncheck = false;

        if (fromWhere.equals("startup")) {
            Preferences prefs = Preferences.userRoot();
            newversion_startupcheck_exists = prefs.get("versioncheck", null) != null;
            if (newversion_startupcheck_exists) {
                versioncheck = prefs.getBoolean("versioncheck", false);
            }
        }

        if ((fromWhere.equals("menu")) || versioncheck) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/hvdwolf/jExifToolGUI/master/version.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputline;
                version = in.readLine();
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Version: " + version);
            if (Float.valueOf(version) > Float.valueOf(ProgramTexts.Version)) {
                String[] options = {"No", "Yes"};
                int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 400, ProgramTexts.newVersionText), "New version found",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    // Do something
                    openBrowser("https://github.com/hvdwolf/jExifToolGUI/releases");
                    System.exit(0);
                }

            } else {
                if (fromWhere.equals("menu")) {
                    JOptionPane.showMessageDialog(null, String.format(ProgramTexts.HTML, 250, ProgramTexts.LatestVersionText), "No newer version", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // Create correct exiftool command call depending on operating system
    public static String platformExiftool() {
        // exiftool on windows or other
        String exiftool = prefs.get("exiftool", "");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            exiftool = exiftool.replace("\\", "/");
        }
        return exiftool;
    }
    ////////////////////////////////// Load images and display them  ///////////////////////////////////


    /*
     * Display the loaded files with icon and name
     */
    //static void displayFiles(JTable jTable_File_Names, JTable ListexiftoolInfotable, JLabel Thumbview, File[] files) {
    static void displayFiles(JTable jTable_File_Names, JTable ListexiftoolInfotable, JLabel Thumbview) {

        int selectedRow, selectedColumn;
        File[] files = MyVariables.getSelectedFiles();
        DefaultTableModel model = (DefaultTableModel)jTable_File_Names.getModel();
        //model.setColumnIdentifiers(new String[]{"File Name(s)"});
        //ListexiftoolInfotable.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        jTable_File_Names.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            protected void setValue(Object value) {
                if( value instanceof ImageIcon ) {
                    setIcon((ImageIcon)value);
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


        for (File file : files) {
            try {
                //System.out.println(files[i].getName().replace("\\", "/"));
                filename = file.getName().replace("\\", "/");
                //System.out.println(files[i].getPath().replace("\\", "/"));
                BufferedImage img = ImageIO.read(new File(file.getPath().replace("\\", "/")));
                // resize it
                BufferedImage resizedImg = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(img, 0, 0, 160, 120, null);
                g2.dispose();
                ImageIcon icon = new ImageIcon(resizedImg);
                    /*ImgRow[tcolumn] = icon;
                    FilenameRow[tcolumn] = filename;*/
                ImgFilenameRow[0] = icon;
                ImgFilenameRow[1] = filename;
            } catch (IOException ex) {
                System.out.println("Error loading image");
            }

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

        String fpath ="";
        List<String> cmdparams = new ArrayList<String>();
        int selectedRow = MyVariables.getSelectedRow();

        //System.out.println("selectedRow: " + String.valueOf(selectedRow));
        String exiftool = prefs.get("exiftool", "");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
            //System.out.println("fpath is now: " + fpath);
            exiftool = exiftool.replace("\\", "/");
        } else {
            fpath = files[selectedRow].getPath();
        }
        // Need to build exiftool prefs check
        MyVariables.setSelectedImagePath(fpath);
        cmdparams.add(exiftool);
        cmdparams.addAll( Arrays.asList(whichInfo) );
        //System.out.println("image file path: " + fpath);
        //MyVariables.setSelectedImagePath(fpath);
        cmdparams.add(MyVariables.getSelectedImagePath());
        //System.out.print("before runCommand: " + cmdparams.toString());
        try {
            String res = CommandRunner.runCommand(cmdparams);
            //System.out.println("res is " + res);
            displayInfoForSelectedImage(res, ListexiftoolInfotable);
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
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
                params[0] = "-gps:all";
                break;
            case "gps/location":
                params = MyConstants.GPS_LOC__PARAMS;
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

        DefaultTableModel model = (DefaultTableModel)ListexiftoolInfotable.getModel();
        model.setColumnIdentifiers(new String[]{"Group", "Tag", "Value"});
        ListexiftoolInfotable.getColumnModel().getColumn(0).setPreferredWidth(100);
        ListexiftoolInfotable.getColumnModel().getColumn(1).setPreferredWidth(260);
        ListexiftoolInfotable.getColumnModel().getColumn(2).setPreferredWidth(500);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (exiftoolInfo.length() > 0) {
            String[] lines = exiftoolInfo.split(System.getProperty("line.separator"));

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
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
            //System.out.println("fpath is now: " + fpath);
        } else {
            fpath = files[selectedRow].getPath();
        }
        BufferedImage img= ImageIO.read(new File(fpath));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 225, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 225, null);
        g2.dispose();
        ImageIcon icon=new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }

    /*
     * This method displays a preview of the image in the bottom left panel
     * THIS METHOD IS NOT USED ANYMORE
     */
    public void displayTableImage(int selectedRow, File[] files, JLabel ThumbView) throws IOException {
        String fpath = "";
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
            //System.out.println("fpath is now: " + fpath);
        } else {
            fpath = files[selectedRow].getPath();
        }
        BufferedImage img= ImageIO.read(new File(fpath));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 225, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 225, null);
        g2.dispose();
        ImageIcon icon=new ImageIcon(resizedImg);
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
        ImageIcon icon=new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }

    static void extdisplaySelectedImageInDefaultViewer() {
        //myVariables myVars = new myVariables();

        //MyVariables.getMySelectedRow(), MyVariables.getSelectedImagePath()
        String OS = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();
        if (OS.contains("mac")) { //Apple
            try {
                runtime.exec("open /Applications/Preview.app " + MyVariables.getSelectedImagePath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (OS.contains("windows")) {
            String convImg = MyVariables.getSelectedImagePath().replace("/", "\\");
            String[] commands = { "cmd.exe", "/c", "start", "\"DummyTitle\"", "\"" + convImg + "\"" };
            try {
                runtime.exec(commands);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else { //Linux, or "something else"
            try {
                //System.out.println("xdg-open " +  MyVariables.getSelectedImagePath().replace(" ", "\\ "));
                runtime.exec("xdg-open " +  MyVariables.getSelectedImagePath().replace(" ", "\\ "));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    @SuppressWarnings("SameParameterValue")
    private static URL getResource(String path) {
        return Utils.class.getClassLoader().getResource(path);
    }
}
