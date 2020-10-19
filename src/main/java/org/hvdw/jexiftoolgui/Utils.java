package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.Level;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.JavaImageViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hvdw.jexiftoolgui.Application.OS_NAMES.APPLE;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.*;
import static org.slf4j.LoggerFactory.getLogger;

public class Utils {

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Utils.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(Utils.class);

    private Utils() {
        SetLoggingLevel(Utils.class);
        //logger.setLevel(Level.ALL);
    }

    public static boolean containsIndices(int[] selectedIndices) {
        List<Integer> intList = IntStream.of(selectedIndices).boxed().collect(Collectors.toList());
        return intList.size() != 0;
    }

    static public void SetLoggingLevel(Class usedClass) {
        String logLevel = prefs.getByKey(LOG_LEVEL, "Info");
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(usedClass);
        // hardcode in case of debugging/troubleshooting
        //logLevel = "Trace";

        switch (logLevel) {
            case "Off":
                logger.setLevel(Level.OFF);
                break;
            case "Error":
                logger.setLevel(Level.ERROR);
                break;
            case "Warn":
                logger.setLevel(Level.WARN);
                break;
            case "Info":
                logger.setLevel(Level.INFO);
                break;
            case "Debug":
                logger.setLevel(Level.DEBUG);
                break;
            case "Trace":
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.INFO);
                break;
        }
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
    static public void openBrowser(String webUrl) {

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
        infostring.append("<tr><td>Operating system architecture:</td><td>" + SystemPropertyFacade.getPropertyByKey(OS_ARCH).replaceAll("(\\r|\\n)", "") + "</td></tr>");
        // or use replaceAll(LINE_SEPATATOR, "") or replaceAll("(\\r|\\n)", "")
        infostring.append("<tr><td>Operating system version:</td><td>" + SystemPropertyFacade.getPropertyByKey(OS_VERSION).replaceAll(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR), "") + "</td></tr>");
        infostring.append("<tr><td>User home directory:</td><td>" + SystemPropertyFacade.getPropertyByKey(USER_HOME).replaceAll(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR), "") + "</td></tr>");
        infostring.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
        infostring.append("<tr><td>jExifToolGUI version:</td><td>" + ProgramTexts.Version.replaceAll("(\\r|\\n)", "") + "</td></tr>");
        infostring.append("<tr><td>Exiftool version:</td><td>" + (MyVariables.getExiftoolVersion()).replaceAll("(\\r|\\n)", "") + "</td></tr>");
        infostring.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
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
    public static void showLicense(JPanel myComponent) {

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
    public static String[] checkPrefsArtistCreditsCopyRights() {
        String[] ArtCredCopyPrefs = {
                prefs.getByKey(ARTIST, ""),
                prefs.getByKey(CREDIT, ""),
                prefs.getByKey(COPYRIGHTS, "")
        };
        return ArtCredCopyPrefs;
    }

    public static List<String> AlwaysAdd() {
        List<String> AlwaysAddParams = new ArrayList<String>();
        String exifartist = prefs.getByKey(ARTIST, "");
        String copyright = prefs.getByKey(COPYRIGHTS, "");
        String credits = prefs.getByKey(CREDIT, "");
        if (!prefs.getByKey(ARTIST, "").equals("") && !prefs.getByKey(ARTIST, "").equals(" ") ) {
            exifartist = "-exif:Artist=" + prefs.getByKey(ARTIST, "");
            AlwaysAddParams.add(exifartist);
            exifartist = "-xmp-dc:creator=" + prefs.getByKey(ARTIST, "");
            AlwaysAddParams.add(exifartist);
            exifartist = "-iptc:by-line=" + prefs.getByKey(ARTIST, "");
            AlwaysAddParams.add(exifartist);
        }
        if (!prefs.getByKey(CREDIT, "").equals("") && !prefs.getByKey(CREDIT, "").equals(" ")) {
            credits = "-xmp-photoshop:Credit=" + prefs.getByKey(CREDIT, "");
            AlwaysAddParams.add(credits);
            credits = "-iptc:Credit=" + prefs.getByKey(CREDIT, "");
            AlwaysAddParams.add(credits);
        }
        if (!prefs.getByKey(COPYRIGHTS, "").equals("") && !prefs.getByKey(COPYRIGHTS, "").equals(" ")) {
            copyright = "-exif:Copyright=" + prefs.getByKey(COPYRIGHTS, "");
            AlwaysAddParams.add(copyright);
            copyright = "-xmp-dc:rights=" + prefs.getByKey(COPYRIGHTS, "");
            AlwaysAddParams.add(copyright);
            copyright = "-iptc:copyrightnotice=" + prefs.getByKey(COPYRIGHTS, "");
            AlwaysAddParams.add(copyright);
        }
        AlwaysAddParams.add("-exif:ProcessingSoftware=jExifToolGUI " + ProgramTexts.Version);
        //AlwaysAddParams.add("-exif:Software=jExifToolGUI " + ProgramTexts.Version);
        AlwaysAddParams.add("-xmp:Software=jExifToolGUI " + ProgramTexts.Version);

        return AlwaysAddParams;
    }


    /*
     * This method checks for a new version on the repo.
     * It can be called from startup (preferences setting) or from the Help menu
     */
    public static void checkForNewVersion(String fromWhere) {
        String web_version = "";
        boolean versioncheck = prefs.getByKey(VERSION_CHECK, true);


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
            logger.debug("metadatalanguage: " + parts[0]);
            //return "-lang " + parts[0];
            return parts[0];
        }
    }

    public static boolean UseDecimalDegrees() {
        Boolean usedecimaldegrees = prefs.getByKey(SHOW_DECIMAL_DEGREES, false);
        return usedecimaldegrees;
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
    public static String getFileNameWithoutExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }
    public static String getFilePathWithoutExtension(String filepath) {
        // Duplicate of above, but makes it easier when working with file paths or only file names
        return filepath.replaceFirst("[.][^.]+$", "");
    }

    /*
    / an instance of LabelIcon holds an icon and label pair for each row.
     */
    private static class LabelIcon {

        Icon icon;
        String label;

        public LabelIcon(Icon icon, String label) {
            this.icon = icon;
            this.label = label;
        }
    }

    /*
    / This is the extended TableCellRenderer
    / Because DefaultTableCellRenderer is JLabel, you can use it's text alignment properties in a custom renderer to label the icon.
     */
    private static class LabelIconRenderer extends DefaultTableCellRenderer {

        public LabelIconRenderer() {
            setHorizontalTextPosition(JLabel.CENTER);
            setVerticalTextPosition(JLabel.BOTTOM);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object
                value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel r = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
            setIcon(((LabelIcon) value).icon);
            setText(((LabelIcon) value).label);
            return r;
        }
    }

    private static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            /*JLabel r = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            setIcon(((LabelIcon) value).icon);
            setText(((LabelIcon) value).label);*/

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
                if (table.isCellEditable(row, column)) {
                    setForeground(UIManager.getColor("Table.focusCellForeground"));
                    setBackground(UIManager.getColor("Table.focusCellBackground"));
                }
            } else {
                setBorder(new EmptyBorder(1, 2, 1, 2));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    static public String returnBasicImageDataString(String filename, String stringType) {
        String strImgData = "";
        // hashmap basicImgData: ImageWidth, ImageHeight, Orientation, ISO, FNumber, ExposureTime, focallength, focallengthin35mmformat
        HashMap<String, String> imgBasicData = MyVariables.getimgBasicData();
        StringBuilder imginfo = new StringBuilder();

        if ("html".equals(stringType)) {
            //imginfo.append("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.filename") + ": " + filename);
            imginfo.append("<html>" + filename);
            imginfo.append("<br><br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.imagesize") + ": " + imgBasicData.get("ImageWidth") + " x " + imgBasicData.get("ImageHeight"));
            //imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.orientation") + imgBasicData.get("Orientation"));
            imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.iso") + ": " + imgBasicData.get("ISO"));
            imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.fnumber") + ": " + imgBasicData.get("FNumber"));
            if (!(imgBasicData.get("ExposureTime") == null)) {
                imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.exposuretime") + ": 1/" + String.valueOf(Math.round(1 / Float.parseFloat(imgBasicData.get("ExposureTime")))));
            } else {
                imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.exposuretime") + ": " + imgBasicData.get("ExposureTime"));
            }
            imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.focallength") + ": " + imgBasicData.get("FocalLength") + " mm");
            imginfo.append("<br>" + ResourceBundle.getBundle("translations/program_strings").getString("lp.focallength35mm") + ": " + imgBasicData.get("FocalLengthIn35mmFormat") + " mm");
            strImgData = imginfo.toString();
        } else {
            imginfo.append(ResourceBundle.getBundle("translations/program_strings").getString("lp.filename") + ": " + filename);
            imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.imagesize") + ": " + imgBasicData.get("ImageWidth") + " x " + imgBasicData.get("ImageHeight"));
            //imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.orientation") + imgBasicData.get("Orientation"));
            imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.iso") + ": " + imgBasicData.get("ISO"));
            imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.fnumber") + ": " + imgBasicData.get("FNumber"));
            if (!(imgBasicData.get("ExposureTime") == null)) {
                imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.exposuretime") + ": 1/" + String.valueOf(Math.round(1 / Float.parseFloat(imgBasicData.get("ExposureTime")))));
            } else {
                imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.exposuretime") + ": " + imgBasicData.get("ExposureTime"));
            }
            imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.focallength") + ": " + imgBasicData.get("FocalLength") + " mm");
            imginfo.append("; " + ResourceBundle.getBundle("translations/program_strings").getString("lp.focallength35mm") + ": " + imgBasicData.get("FocalLengthIn35mmFormat") + " mm");
            strImgData = imginfo.toString();
        }
        return strImgData;
    }

    public static File[] loadImages(String loadingType, JPanel rootPanel, JPanel LeftPanel, JTable tableListfiles, JTable ListexiftoolInfotable, JButton[] commandButtons, JLabel[] mainScreenLabels, JProgressBar progressBar, String[] params) {
        File[] files;

        // "Translate" for clarity, instead of using the array index;
        JLabel OutputLabel = mainScreenLabels[0];
        JLabel lblLoadedFiles = mainScreenLabels[1];
        JButton buttonShowImage = commandButtons[2];
        JButton buttonCompare = commandButtons[3];
        JButton buttonSlideshow = commandButtons[4];


        String prefFileDialog = prefs.getByKey(PREFERRED_FILEDIALOG, "jfilechooser");
        if ("images".equals(loadingType)) {
            logger.debug("load images pushed or menu load images");
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.loadingimages"));
            if ("jfilechooser".equals(prefFileDialog)) {
                logger.debug("load images using jfilechooser");
                files = StandardFileIO.getFileNames(rootPanel);
                logger.debug("AFTER load images using jfilechooser");
            } else {
                logger.debug("load images pushed or menu load images using AWT file dialog");
                files = StandardFileIO.getFileNamesAwt(rootPanel);
                logger.debug("AFTER load images using AWT file dialog");
            }
        } else if ("folder".equals(loadingType)) { // loadingType = folder
            OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.loadingdirectory"));
            logger.debug("load folder pushed or menu load folder");
            if ("jfilechooser".equals(prefFileDialog)) {
                logger.debug("load folder using jfilechooser");
                files = StandardFileIO.getFolderFiles(rootPanel);
                logger.debug("AFTER load folder using jfilechooser");
            } else {
                logger.debug("load folder using AWT file dialog");
                files = StandardFileIO.getFolderFilesAwt(rootPanel);
                logger.debug("AFTER load folder using AWT file dialog");
            }
        } else { // files dropped onto our app
            OutputLabel.setText("Loading files dropped onto the app");
            files = MyVariables.getLoadedFiles();
        }
        if (files != null) {
            lblLoadedFiles.setText(String.valueOf(files.length));
            logger.debug("After loading images, loading files or dropping files: no. of files > 0");
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int jpegcounter = 0;
                    String filename;
                    for (File file : files) {
                        filename = file.getName().replace("\\", "/");
                        logger.debug("Checking on extension for JPG extraction on image: " +filename);
                        String filenameExt = Utils.getFileExtension(filename);
                        if ( (filenameExt.toLowerCase().equals("jpg")) || (filenameExt.toLowerCase().equals("jpeg")) ) {
                            jpegcounter++;
                        }
                    }
                    if (jpegcounter >= 3) {
                        // Always try to extract thumbnails and previews of selected images. This normally only works for JPGs and RAWs
                        ImageFunctions.extractThumbnails();
                    }
                    Utils.displayFiles(tableListfiles, LeftPanel);
                    MyVariables.setSelectedRow(0);
                    //tableListfiles.getSelectionBackground();
                    // Select first row and repaint table
                    /*tableListfiles.setRowSelectionInterval(0,0);
                    tableListfiles.setSelectionModel(listSelectionModel);
                    tableListfiles.repaint(); */
                    //tableListfiles.addRowSelectionInterval(0,0);
                    //DefaultTableModel model = (DefaultTableModel) tableListfiles.getModel();
                    //model.setRowColour(0, tableListfiles.getSelectionBackground());
                    //tableListfiles.set
                    /*Component prepareRenderer(TableCellRenderer, int row, int column){
                        if (tableListfiles.isRowSelected(0)) {
                            Component c = super.prepareRenderer(renderer, row, column);
                        }
                    }*/
                    //String[] params = whichRBselected();
                    String res = Utils.getImageInfoFromSelectedFile(params, files);
                    Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                    buttonShowImage.setEnabled(true);
                    buttonCompare.setEnabled(true);
                    buttonSlideshow.setEnabled(true);
                    //OutputLabel.setText(" Images loaded ...");
                    OutputLabel.setText("");
                    // progressbar enabled immedately after this void run starts in the InvokeLater, so I disable it here at the end of this void run
                    Utils.progressStatus(progressBar, false);
                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setVisible(true);
                }
            });
        } else {
            logger.debug("no files loaded. User pressed cancel.");
            lblLoadedFiles.setText("");
            OutputLabel.setText("");
        }
        List<Integer> selectedIndicesList = new ArrayList<>();
        MyVariables.setselectedIndicesList(selectedIndicesList);
        MyVariables.setLoadedFiles(files);

        return files;
    }


    /*
     * Display the loaded files with icon and name
     */
    static void displayFiles(JTable jTable_File_Names, JPanel LeftPanel) {
        int selectedRow, selectedColumn;

        ImageIcon icon = null;
        File[] files = MyVariables.getLoadedFiles();
        boolean singleColumnTable = false;
        boolean columns = prefs.getByKey(DUAL_COLUMN, true);
        if (columns) {
            singleColumnTable = false;
        } else {
            singleColumnTable = true;
        }

        DefaultTableModel model = (DefaultTableModel) jTable_File_Names.getModel();

        boolean finalSingleColumnTable = singleColumnTable;
        jTable_File_Names.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            protected void setValue(Object value) {
                if (finalSingleColumnTable) {
                    if (value instanceof LabelIcon) {
                        setIcon(((LabelIcon) value).icon);
                        setHorizontalTextPosition(JLabel.CENTER);
                        setVerticalTextPosition(JLabel.BOTTOM);
                        setText(((LabelIcon) value).label);
                    }
                } else {
                    if (value instanceof ImageIcon) {
                        setIcon((ImageIcon) value);
                        setText("");
                    } else {
                        setIcon(null);
                        super.setValue(value);
                    }
                }
            }
        });

        if (singleColumnTable) {
            jTable_File_Names.setDefaultRenderer(LabelIcon.class, new LabelIconRenderer());
            model.setColumnIdentifiers(new String[]{"Photo / Filename"});
            jTable_File_Names.getColumnModel().getColumn(0).setMinWidth(170);
            jTable_File_Names.getColumnModel().getColumn(0).setPreferredWidth(210);
            jTable_File_Names.setRowHeight(180); //180
            //LeftPanel.setSize(220,-1);
            LeftPanel.setPreferredSize(new Dimension(230, -1));
        } else {
            model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("lp.thumbtablephotos"), ResourceBundle.getBundle("translations/program_strings").getString("lp.thumbtabledata")});
            jTable_File_Names.getColumnModel().getColumn(0).setPreferredWidth(170);
            jTable_File_Names.getColumnModel().getColumn(1).setPreferredWidth(260);
            jTable_File_Names.setRowHeight(150);
            //LeftPanel.setSize(430,-1);
            LeftPanel.setPreferredSize(new Dimension(440, -1));
        }
        model.setRowCount(0);
        model.fireTableDataChanged();
        jTable_File_Names.clearSelection();
        jTable_File_Names.setCellSelectionEnabled(true);
        Object[] row = new Object[1];
        Object[] ImgRow = new Object[3];
        Object[] FilenameRow = new Object[3];
        Object[] ImgFilenameRow = new Object[2];
        String filename = "";

        Application.OS_NAMES currentOsName = getCurrentOsName();
        for (File file : files) {
            filename = file.getName().replace("\\", "/");
            logger.debug("Now working on image: " +filename);

            icon = ImageFunctions.analyzeImageAndCreateIcon(file);

            if (singleColumnTable){

                ImgFilenameRow[0] = new LabelIcon(icon, filename);
            } else {
                String imginfo = returnBasicImageDataString(filename, "html");
                logger.debug("imginfo {}", imginfo);
                ImgFilenameRow[0] = icon;
                ImgFilenameRow[1] = imginfo;
            }
            model.addRow(ImgFilenameRow);
        }

        MyVariables.setSelectedRow(0);
        MyVariables.setSelectedColumn(0);
    }

    public static void getImageInfoFromSelectedTreeFile(String[] whichInfo,JTable ListexiftoolInfotable) {

        //String fpath = "";
        List<String> cmdparams = new ArrayList<String>();
        //int selectedRow = MyVariables.getSelectedRow();
        //List<Integer> selectedIndicesList =  MyVariables.getselectedIndicesList();

        //if (selectedIndicesList.size() < 2) { //Meaning we have only one image selected
            Application.OS_NAMES currentOsName = getCurrentOsName();

            cmdparams.add(Utils.platformExiftool().trim());
            // Check if we want to use G1 instead of G
            boolean useGroup1 = prefs.getByKey(USE_G1_GROUP, false);
            if (useGroup1) {
                for (int i = 0; i < whichInfo.length; i++) {
                    if ("-G".equals(whichInfo[i])) {
                        whichInfo[i] = whichInfo[i].replace("-G", "-G1");
                    }
                }
            }
            // Check for chosen metadata language
            if (!"".equals(getmetadataLanguage())) {
                cmdparams.add("-lang");
                cmdparams.add(getmetadataLanguage());
            }
            // Check if user wants to see decimal degrees
            if (UseDecimalDegrees()) {
                cmdparams.add("-c");
                cmdparams.add("%.6f");
            }
            cmdparams.addAll(Arrays.asList(whichInfo));
            logger.trace("image file path: {}", MyVariables.getSelectedImagePath());
            cmdparams.add(MyVariables.getSelectedImagePath());

            logger.trace("before runCommand: {}", cmdparams);
            try {
                String res = CommandRunner.runCommand(cmdparams);
                logger.trace("res is {}", res);
                displayInfoForSelectedImage(res, ListexiftoolInfotable);
            } catch (IOException | InterruptedException ex) {
                logger.error("Error executing command", ex);
            }
        /*} else {
            // We have multiple images selected. There is no direct link to the images anymore,
            // apart from the fact that the last image is automatically selected
            String res = "jExifToolGUI\t" +
                    ResourceBundle.getBundle("translations/program_strings").getString("vdtab.multfiles") + "\t" +
                    ResourceBundle.getBundle("translations/program_strings").getString("vdtab.seloption");
            displayInfoForSelectedImage(res, ListexiftoolInfotable);
        }*/

    }

    /*
     * This is the ImageInfo method that is called by all when displaying the exiftool info from the image
     */
    static String getImageInfoFromSelectedFile(String[] whichInfo, File[] files) {

        String res = "";
        String fpath = "";
        List<String> cmdparams = new ArrayList<String>();
        int selectedRow = MyVariables.getSelectedRow();
        List<Integer> selectedIndicesList =  MyVariables.getselectedIndicesList();

        if (selectedIndicesList.size() < 2) { //Meaning we have only one image selected
            logger.debug("selectedRow: {}", String.valueOf(selectedRow));
            if (isOsFromMicrosoft()) {
                fpath = files[selectedRow].getPath().replace("\\", "/");
            } else {
                fpath = files[selectedRow].getPath();
            }

            // Need to build exiftool prefs check
            MyVariables.setSelectedImagePath(fpath);
            Application.OS_NAMES currentOsName = getCurrentOsName();

            cmdparams.add(Utils.platformExiftool().trim());
            // Check if we want to use G1 instead of G
            boolean useGroup1 = prefs.getByKey(USE_G1_GROUP, false);
            if (useGroup1) {
                for (int i = 0; i < whichInfo.length; i++) {
                    if ("-G".equals(whichInfo[i])) {
                        whichInfo[i] = whichInfo[i].replace("-G", "-G1");
                    }
                }
            }
            // Check for chosen metadata language
            if (!"".equals(getmetadataLanguage())) {
                cmdparams.add("-lang");
                cmdparams.add(getmetadataLanguage());
            }
            // Check if user wants to see decimal degrees
            if (UseDecimalDegrees()) {
                cmdparams.add("-c");
                cmdparams.add("%.6f");
            }
            cmdparams.addAll(Arrays.asList(whichInfo));
            logger.trace("image file path: {}", fpath);
            cmdparams.add(MyVariables.getSelectedImagePath());

            logger.trace("before runCommand: {}", cmdparams);
            try {
                res = CommandRunner.runCommand(cmdparams);
                logger.trace("res is {}", res);
                //displayInfoForSelectedImage(res, ListexiftoolInfotable);
            } catch (IOException | InterruptedException ex) {
                logger.error("Error executing command", ex);
            }
        } else {
            // We have multiple images selected. There is no direct link to the images anymore,
            // apart from the fact that the last image is automatically selected
            res = "jExifToolGUI\t" +
                    ResourceBundle.getBundle("translations/program_strings").getString("vdtab.multfiles") + "\t" +
                    ResourceBundle.getBundle("translations/program_strings").getString("vdtab.seloption");
            //displayInfoForSelectedImage(res, ListexiftoolInfotable);
        }

        return res;
    }

    // This is the "pre-ImageInfo" that is called when the option is chosen to display for a specific Tag Name from the dropdown list without changing the selected image.
    public static String selectImageInfoByTagName(JComboBox comboBoxViewByTagName, File[] files) {

        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        boolean useGroup1 = prefs.getByKey(USE_G1_GROUP, false);
        if (useGroup1) {
            params[1] = "-G1";
        } else {
            params[1] = "-G";
        }
        params[2] = "-tab";
        String res = getImageInfoFromSelectedFile(params, files);
        //displayInfoForSelectedImage(res, ListexiftoolInfotable);
        return res;
    }

    // This is for the "all tags" and "camera makes"
    static String[] getWhichTagSelected(JComboBox comboBoxViewByTagName) {
        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        boolean useGroup1 = prefs.getByKey(USE_G1_GROUP, false);
        if (useGroup1) {
            params[1] = "-G1";
        } else {
            params[1] = "-G";
        }
        params[2] = "-tab";
        return params;
    }

    // This is for the Common Tags as they can contain combined info
    static String[] getWhichCommonTagSelected(JComboBox comboBoxViewByTagName) {
        String[] params = {"-a","-G","-tab","-exiftool:all"}; // We need to initialize with something
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
            default:
                // Here is where we check if we have "user custom combi tags"
                String[] customCombis = MyVariables.getCustomCombis();
                for (String customCombi : customCombis) {
                    if (customCombi.equals(SelectedTagName)) {
                        //logger.info("SelectedTagName: {}; customCombi: {}",SelectedTagName, customCombi);
                        String sql = "select tag from custommetadatasetLines where customset_name='" + SelectedTagName + "' order by rowcount";
                        String queryResult = SQLiteJDBC.singleFieldQuery(sql,"tag");
                        if (queryResult.length() > 0) {
                            String[] customTags = queryResult.split("\\r?\\n");
                            logger.debug("queryResult {}",queryResult);
                            List<String> tmpparams = new ArrayList<String>();
                            tmpparams.add("-a");
                            tmpparams.add("-G");
                            tmpparams.add("-tab");
                            for (String customTag : customTags) {
                                logger.trace("customTag {}", customTag);
                                if (customTag.startsWith("-")) {
                                    tmpparams.add(customTag);
                                } else {
                                    tmpparams.add("-" + customTag);
                                }
                            }
                            String[] tmpArray = new String[tmpparams.size()];
                            params = tmpparams.toArray(tmpArray);
                            logger.trace("custom tags: {}", params.toString());
                        }
                    }
                }
                break;
        }
        boolean useGroup1 = prefs.getByKey(USE_G1_GROUP, false);
        if (useGroup1) {
            for (int i =0; i < params.length; i++) {
                if ("-G".equals(params[i])) {
                    params[i] = params[i].replace("-G", "-G1");
                }
            }
        }
        return params;
    }

    /*
     * This method displays the exiftool info in the right 3-column table
     */
    public static void displayInfoForSelectedImage(String exiftoolInfo, JTable ListexiftoolInfotable) {
        // This will display the metadata info in the right panel

        logger.trace("String exiftoolInfo {}", exiftoolInfo);
        DefaultTableModel model = (DefaultTableModel) ListexiftoolInfotable.getModel();
        model.setColumnIdentifiers(new String[]{ ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablegroup"),
                ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tabletag"),
                ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablevalue")});
        ListexiftoolInfotable.getColumnModel().getColumn(0).setPreferredWidth(100);
        ListexiftoolInfotable.getColumnModel().getColumn(1).setPreferredWidth(260);
        ListexiftoolInfotable.getColumnModel().getColumn(2).setPreferredWidth(500);
        model.setRowCount(0);

        Object[] row = new Object[1];

        logger.debug("exiftoolInfo.length() {}; Warning?? {}; Error ?? {}", exiftoolInfo.length(),exiftoolInfo.trim().startsWith("Warning"), exiftoolInfo.trim().startsWith("Error"));
//        if ( (exiftoolInfo.length() > 0) && !( exiftoolInfo.trim().startsWith("Warning") || exiftoolInfo.trim().startsWith("Error") ) ) {
        if (exiftoolInfo.length() > 0) {
            if (exiftoolInfo.trim().startsWith("Warning")) {
                model.addRow(new Object[]{"ExifTool", "Warning", "Invalid Metadata data"});
            } else if (exiftoolInfo.trim().startsWith("Error")) {
                model.addRow(new Object[]{"ExifTool", "Error", "Invalid Metadata data"});
            } else {
                String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

                for (String line : lines) {
                    //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                    String[] cells = line.split("\\t", 3);
                    model.addRow(new Object[]{cells[0], cells[1], cells[2]});
                }
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
        } else {
            fpath = "\"" + files[selectedRow].getPath() + "\"";
        }
        logger.debug("fpath for displaySelectedImageInDefaultViewer is now: {}", fpath);
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
    * Setc checkboxes on first copy data tab. These are the cehckboxes for the selective copy
     */
    static void setCopyMetaDatacheckboxes(boolean state, JCheckBox[] CopyMetaDatacheckboxes) {
        for (JCheckBox chkbx : CopyMetaDatacheckboxes) {
            chkbx.setEnabled(state);
        }
    }
    /*
    / Set checkboxes and radiobuttons on the second copy data tab
     */
    static void setInsideCopyCheckboxesRadiobuttons(boolean state, JRadioButton[] InsideCopyMetadataradiobuttons, JCheckBox[] InsideCopyMetadacheckboxes) {
        for (JRadioButton rdbtn: InsideCopyMetadataradiobuttons) {
            rdbtn.setEnabled(state);
        }
        for (JCheckBox chkbx : InsideCopyMetadacheckboxes) {
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
             logger.info("error loading frame icon {}", ioe.toString());
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
        logger.debug("Selected file {} ", MyVariables.getSelectedImagePath());
        //String correctedPath = MyVariables.getSelectedImagePath().replace("\"", ""); //Can contain double quotes when double-clicked

        logger.debug("RAW viewer started (trying to start)");
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
    public static void displaySelectedImageInExternalViewer() {
        //String[] SimpleExtensions = {"bmp","gif,","jpg", "jpeg", "png"};
        //String[] SimpleExtensions = MyConstants.JAVA_SUP_EXTENSIONS;
        String[] SimpleExtensions = MyConstants.SUPPORTED_IMAGES;

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
                logger.debug("RawExtension is true");
                break;
            }
        }
        if (!RawExtension) { //check if we have another image
            for (String ext : SimpleExtensions) {
                if ( filenameExt.toLowerCase().equals(ext)) { // it is a bmp, gif, jpg, png image or tif(f)
                    defaultImg = true;
                    logger.debug("default image is true");
                    break;
                } else if ( filenameExt.toLowerCase().equals("heic") && (currentOsName == APPLE) ) { // We first need to use sips to convert
                    File file = new File (MyVariables.getSelectedImagePath());
                    String filename = file.getName();
                    String exportResult = ImageFunctions.sipsConvertToJPG(file, "full");
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
            //viewInDefaultViewer();
            Runtime runtime = Runtime.getRuntime();
            try {
                //Application.OS_NAMES currentOsName = getCurrentOsName();
                switch (currentOsName) {
                    case APPLE:
                        runtime.exec("open /Applications/Preview.app " + MyVariables.getSelectedImagePath());
                        return;
                    case MICROSOFT:
                        String convImg = MyVariables.getSelectedImagePath().replace("/", "\\");
                        String[] commands = {"cmd.exe", "/c", "start", "\"DummyTitle\"", String.format("\"%s\"", convImg)};
                        runtime.exec(commands);
                        return;
                    case LINUX:
                        String selectedImagePath = MyVariables.getSelectedImagePath().replace(" ", "\\ ");
                        logger.trace("xdg-open {}", selectedImagePath);
                        runtime.exec("xdg-open " + MyVariables.getSelectedImagePath().replace(" ", "\\ "));
                        return;
                }
            } catch (IOException e) {
                logger.error("Could not open image app.", e);
            }

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
            logger.debug("converted latitude seconds: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcsecerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[1]), 0, 60)) { //mins
            coordinate = (Float.parseFloat( input_lat_lon[1]) + coordinate) / (float) 60;
            logger.debug("converted latitude mins + secs: " + coordinate);
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
            logger.debug("decimal lat: " + coordinate);
            lat_lon[0] = String.valueOf(coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcdegerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        
        // Now do the same for longitude
        if ( in_Range(Integer.parseInt(input_lat_lon[6]), 0, 60)) { //secs
            coordinate = Float.parseFloat(input_lat_lon[6]) / (float) 60;
            logger.debug("converted longitude seconds: " + coordinate);
        } else {
            JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("gps.calcsecerror"), ResourceBundle.getBundle("translations/program_strings").getString("gps.calcerror"), JOptionPane.ERROR_MESSAGE);
            lat_lon[0] = "error";
            return lat_lon;
        }
        if (in_Range(Integer.parseInt(input_lat_lon[5]), 0, 60)) { //mins
            coordinate = (Float.parseFloat( input_lat_lon[5]) + coordinate) / (float) 60;
            logger.debug("converted longitude mins + secs: " + coordinate);
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
            logger.debug("decimal lon: " + coordinate);
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
        File[] files = MyVariables.getLoadedFiles();

        logger.debug("Export previews/thumbnails..");
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
                logger.debug("index: {}  image path: {}", index, files[index].getPath());
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
/*    public static String ExportPreviewsThumbnailsForIconDisplay(File file) {
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
            logger.debug("Error executing command to export previews for one RAW");
            exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }
        return exportResult;
    } */


    @SuppressWarnings("SameParameterValue")
    private static URL getResource(String path) {
        return Utils.class.getClassLoader().getResource(path);
    }
}
