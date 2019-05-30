package org.hvdw.jexiftoolgui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    myVariables myVars = new myVariables();
    Preferences prefs = Preferences.userRoot();

    public static String runCommand(List<String> cmdparams) throws InterruptedException, IOException {

        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        System.out.println(cmdparams.toString());

        ProcessBuilder builder = new ProcessBuilder(cmdparams);
        //System.out.println("Did ProcessBuilder builder = new ProcessBuilder(cmdparams);");
        try {
            builder.redirectErrorStream(true);
            Process process = builder.start();
            //Use a buffered reader to prevent hangs on Windows
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                res = res + line + System.lineSeparator();
                //System.out.println("tasklist: " + line);
            int errCode = process.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = e.getMessage();
        }
        return res;
    }

    public static void runCommandOutput (String output) {

        JTextArea textArea = new JTextArea(output);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        JOptionPane.showMessageDialog(null, scrollPane,"Output from the given command",JOptionPane.INFORMATION_MESSAGE);

    }

    public boolean containsIndices(int[] selectedIndices) {
        List<Integer> intList = IntStream.of(selectedIndices).boxed().collect(Collectors.toList());
        if (intList == null) {
            return false;
        } else {
            return true;
        }
    }

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

/*    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    } */

    public static void openBrowser (String webUrl) {
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

    public static String ResourceReader (String fileName) {
        String strFileContents = "";

        try {
            InputStream is = mainScreen.class.getResourceAsStream(fileName);
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
        //strFileContents = "<html><table width='600'><tr><td>" + strFileContents + "</td></tr></html>";
        return strFileContents;
    }

    public static void ReadTagsCSV(String tagname) {
        List<List<String>> tagrecords = new ArrayList<>();
        String tags = ResourceReader("resources/tagxml/" + tagname + ".xml");
        if (tags.length() > 0) {
            String[] lines = tags.split(System.getProperty("line.separator"));

            for(int i = 0; i < lines.length; i++) {
                String[] tagvalues = lines[i].split(",");
                tagrecords.add(Arrays.asList(tagvalues));
            }
        }
    }

    public static void License (JPanel myComponent) {
        ImageIcon icon = null;

        String license = ResourceReader("resources/COPYING");
        JTextArea textArea = new JTextArea(license);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        // icon for my dialogs
        //InputStream stream = getClass().getResourceAsStream("resources/jexiftoolgui-64.png");
        /*InputStream stream = ClassLoader.getSystemResourceAsStream("resources/jexiftoolgui-64.png");
        try {
            icon = new ImageIcon(ImageIO.read(stream));
        } catch(IOException ex) {
            System.out.println("Error executing command");
        }
        JOptionPane.showMessageDialog(myComponent, scrollPane,"GNU GENERAL PUBLIC LICENSE Version 3",JOptionPane.INFORMATION_MESSAGE, icon); */
        JOptionPane.showMessageDialog(myComponent, scrollPane,"GNU GENERAL PUBLIC LICENSE Version 3",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void progressStatus(JProgressBar progressBar, Boolean show) {
        if (show) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setBorderPainted(true);
            progressBar.repaint();
        } else {
            progressBar.setVisible(false);
        }
    }

    /////////////////// Locate exiftool //////////////
    public String exiftoolLocator (JPanel myComponent) {
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
            //JOptionPane.showMessageDialog(myComponent,programTexts.CanETlocation,"Locate ExifTool",JOptionPane.INFORMATION_MESSAGE);
            //System.exit(0);
        }
        return exiftool;
    }

    public void checkExifTool(JPanel myComponent) {
        String returnValue = "";
        String[] options = {"specify location", "Download", "Stop"};
        //JOptionPane.showOptionDialog(null,"I can not find exiftool in the preferences or I can not find exiftool at all","exiftool missing",JOptionPane.ERROR_MESSAGE);
        int choice = JOptionPane.showOptionDialog(null,String.format(programTexts.HTML, 450, programTexts.noExifTool),"exiftool missing",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            // open file chooser
            // Do this from the PreferencesDialog class as it atually belongs there
            returnValue = exiftoolLocator(myComponent);
            if (returnValue == "cancelled") {
                JOptionPane.showMessageDialog(myComponent, programTexts.cancelledETlocatefromStartup,"Cancelled locate ExifTool",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if (returnValue == "no exiftool binary") {
                JOptionPane.showMessageDialog(myComponent, programTexts.wrongETbinaryfromStartup,"Wrong executable",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else { // Yes. It looks like we have a correct exiftool selected
                // remove all possible line breaks
                returnValue = returnValue.replace("\n", "").replace("\r", "");
                prefs.put("exiftool", returnValue);
            }
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(myComponent, String.format(programTexts.HTML, 450, programTexts.downloadInstallET),"Download ExifTool",JOptionPane.INFORMATION_MESSAGE);
            // open exiftool site
            openBrowser("https://www.sno.phy.queensu.ca/~phil/exiftool/");
            System.exit(0);
        } else {
            // exit program
            System.exit(0);
        }

    }

    public static void CheckforNewVersion( String fromWhere) {
        String version = "";
        boolean newversion_startupcheck_exists = false;
        boolean versioncheck = false;

        if (fromWhere == "startup") {
            Preferences prefs = Preferences.userRoot();
            newversion_startupcheck_exists = prefs.get("versioncheck", null) != null;
            if (newversion_startupcheck_exists) {
                versioncheck = prefs.getBoolean("versioncheck", false);
            }
        }

        if ((fromWhere == "menu") || versioncheck) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/hvdwolf/JoyingBinRepo/master/version.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputline;
                /*while ((inputline = in.readLine()) != null)
                    System.out.println(inputline);
                version = inputline; */
                version = in.readLine();
                in.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Version: " + version);
            if (Float.valueOf(version) > Float.valueOf(programTexts.Version)) {
                String[] options = {"No", "Yes"};
                int choice = JOptionPane.showOptionDialog(null, String.format(programTexts.HTML, 400, programTexts.newVersionText), "New version found",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    // Do something
                    openBrowser("https://github.com/hvdwolf/Joying-RootAssistant/releases");
                    System.exit(0);
                }

            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // General check method which folder to open
    public String whichFolderToOpen() {
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
    // Which exiftool platform
    public String platformExiftool() {
        // exiftool on windows or other
        String exiftool = prefs.get("exiftool", "");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            exiftool = exiftool.replace("\\", "/");
        }
        return exiftool;
    }
    ////////////////////////////////// Load images and display them  ///////////////////////////////////
    public File[] getFileNames (JPanel myComponent) {
        File[] files = null;
        boolean imageDefaultFolder_exists = false;
        String startFolder = "";

        Preferences prefs = Preferences.userRoot();

        /*imageDefaultFolder_exists = prefs.get("defaultstartfolder", null) != null;
        if (imageDefaultFolder_exists) {
            startFolder = prefs.get("defaultstartfolder", "");
        } else {
            startFolder = System.getProperty("user.home");
        }*/
        startFolder = whichFolderToOpen();

        final JFileChooser chooser = new JFileChooser(startFolder);
        //FileFilter filter = new FileNameExtensionFilter("(images)", "jpg", "jpeg" , "png", "tif", "tiff");
        FileFilter imgFilter = new FileNameExtensionFilter("(images)", MyConstants.supportedImages);
        FileFilter supFormats = new FileNameExtensionFilter("(supported formats)", MyConstants.supportedFormats);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle("Load Image(s)...");
        chooser.setFileFilter(imgFilter);
        chooser.addChoosableFileFilter(supFormats);
        //chooser.showOpenDialog(mainScreen.this.rootPanel);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            files = chooser.getSelectedFiles();
            myVars.setSelectedFiles(files);
            prefs.node("lastopenedfolder");
            prefs.put("lastopenedfolder", chooser.getSelectedFile().getAbsolutePath());
        } else if (status == JFileChooser.CANCEL_OPTION) {
            files = null;;
        }
        return files;
    }


    public void displayFiles(JTable jTable_File_Names, JTable ListexiftoolInfotable, JLabel Thumbview, File[] files) {

        int selectedRow, selectedColumn;
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


        for(int i = 0; i < files.length; i++) {
                try {
                    //System.out.println(files[i].getName().replace("\\", "/"));
                    filename = files[i].getName().replace("\\", "/");
                    //System.out.println(files[i].getPath().replace("\\", "/"));
                    BufferedImage img = ImageIO.read(new File(files[i].getPath().replace("\\", "/")));
                    // resize it
                    BufferedImage resizedImg = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = resizedImg.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(img, 0, 0, 160, 120, null);
                    g2.dispose();
                    ImageIcon icon=new ImageIcon(resizedImg);
                    /*ImgRow[tcolumn] = icon;
                    FilenameRow[tcolumn] = filename;*/
                    ImgFilenameRow[0] = icon;
                    ImgFilenameRow[1] = filename;
                } catch(IOException ex) {
                    System.out.println("Error loading image");
                }

                /*if (tcolumn == 2) {
                    tcolumn = 0;
                    //jTable_File_Names.setRowHeight(trow, 150);
                    jTable_File_Names.setRowHeight(150);
                    model.addRow(ImgRow);
                    trow++;
                } else {
                    tcolumn++;
                } */
            jTable_File_Names.setRowHeight(150);
            model.addRow(ImgFilenameRow);
        }
        /*
        // In case we do not have a manifold of 3 images
        if (tcolumn == 1) {
            ImgRow[1] = null;
            ImgRow[2] = null;
            model.addRow(ImgRow);
        } else if (tcolumn == 2) {
            ImgRow[2] = null;
            model.addRow(ImgRow);
        } */
        myVars.setMySelectedRow(0);
        myVars.setMySelectedColumn(0);
    }


    // This is the ImageInfo method that is called by all when displaying info
    public void ImageInfo(String[] whichInfo, int selectedRow, File[] files, JTable ListexiftoolInfotable) {

        String fpath ="";
        List<String> cmdparams = new ArrayList<String>();

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
        myVars.setSelectedImagePath(fpath);
        cmdparams.add(exiftool);
        cmdparams.addAll( Arrays.asList(whichInfo) );
        //System.out.println("image file path: " + fpath);
        //myVars.setSelectedImagePath(fpath);
        cmdparams.add(myVars.getSelectedImagePath());
        //System.out.print("before runCommand: " + cmdparams.toString());
        try {
            String res = runCommand(cmdparams);
            //System.out.println("res is " + res);
            DisplayInfo(res, ListexiftoolInfotable);
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
        }

    }

    // This is the "pre-ImageInfo" that is called when the option is chosen to display for a specific Tag Name from the dropdown list without changing the selected image.
    public void ImageInfoByTagName(JComboBox comboBoxViewByTagName, int SelectedRow, File[] files, JTable ListexiftoolInfotable) {

        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        params[1] = "-G";
        params[2] = "-tab";
        ImageInfo(params, SelectedRow, files, ListexiftoolInfotable);
    }

    // This is for the "all tags" and "camera makes"
    public String[] WhichTagSelected(JComboBox comboBoxViewByTagName) {
        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());
        String[] params = new String[3];
        params[0] = "-" + SelectedTagName + ":all";
        params[1] = "-G";
        params[2] = "-tab";
        return params;
    }

    // This is for the Common Tags as they can contain combined info
    public String[] WhichCommonTagSelected(JComboBox comboBoxViewByTagName) {
        String[] params = {"-all"}; // We need to initialize with something
        String SelectedTagName = String.valueOf(comboBoxViewByTagName.getSelectedItem());

        switch (SelectedTagName) {
            case "exif":
                params = MyConstants.exif_params;
                break;
            case "xmp":
                params = MyConstants.xmp_params;
                break;
            case "iptc":
                params = MyConstants.iptc_params;
                break;
            case "gps":
                params[0] = "-gps:all";
                break;
            case "gps/location":
                params = MyConstants.gps_loc_params;
                break;
            case "gpano":
                params = MyConstants.gpano_params;
                break;
            case "icc_profile":
                params = MyConstants.icc_params;
                break;
            case "makernotes":
                params = MyConstants.makernotes_params;
                break;
        }
        return params;
    }

    public void DisplayInfo(String exiftoolInfo, JTable ListexiftoolInfotable) {
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

            for(int i = 0; i < lines.length; i++) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String[] cells = lines[i].split("\\t", 3);
                model.addRow(new Object[] { cells[0], cells[1], cells[2] });
            }
        }

    }

    public void DisplayImage(int selectedRow, File[] files, JLabel ThumbView) throws IOException {
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

    public void DisplayTableImage(int selectedRow, File[] files, JLabel ThumbView) throws IOException {
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


    public void DisplayLogo(JLabel ThumbView) throws IOException {
        BufferedImage img = ImageIO.read(getClass().getResource("resources/jexiftoolgui-splashlogo.jpg"));
        // resize it
        BufferedImage resizedImg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, 300, 300, null);
        g2.dispose();
        ImageIcon icon=new ImageIcon(resizedImg);
        ThumbView.setIcon(icon);
    }

    public void extDisplayImage() {
        //myVariables myVars = new myVariables();

        //myVars.getMySelectedRow(), myVars.getSelectedImagePath()
        String OS = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();
        if (OS.contains("mac")) { //Apple
            try {
                runtime.exec("open /Applications/Preview.app " + myVars.getSelectedImagePath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (OS.contains("windows")) {
            String convImg = myVars.getSelectedImagePath().replace("/", "\\");
            String[] commands = { "cmd.exe", "/c", "start", "\"DummyTitle\"", "\"" + convImg + "\"" };
            try {
                runtime.exec(commands);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else { //Linux, or "something else"
            try {
                //System.out.println("xdg-open " +  myVars.getSelectedImagePath().replace(" ", "\\ "));
                runtime.exec("xdg-open " +  myVars.getSelectedImagePath().replace(" ", "\\ "));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
