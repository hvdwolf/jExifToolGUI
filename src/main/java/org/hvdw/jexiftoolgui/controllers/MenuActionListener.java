package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.datetime.ModifyDateTime;
import org.hvdw.jexiftoolgui.datetime.ShiftDateTime;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.metadata.CreateArgsFile;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;
import org.hvdw.jexiftoolgui.metadata.MetaData;
import org.hvdw.jexiftoolgui.metadata.RemoveMetadata;
import org.hvdw.jexiftoolgui.renaming.RenamePhotos;
import org.hvdw.jexiftoolgui.view.DeleteFavorite;
import org.hvdw.jexiftoolgui.view.MetadataUserCombinations;
import org.hvdw.jexiftoolgui.view.SelectmyLens;
import org.hvdw.jexiftoolgui.view.SimpleWebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.OS_NAME;
import static org.slf4j.LoggerFactory.getLogger;

public class MenuActionListener implements ActionListener  {
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MenuActionListener.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(MenuActionListener.class);

    PreferencesDialog prefsDialog = new PreferencesDialog();
    private DateTime dateTime = new DateTime();
    private MetadataUserCombinations MD = new MetadataUserCombinations();
    private MetaData metaData = new MetaData();
    private SimpleWebView WV = new SimpleWebView();

    public int[] selectedIndices;
    public List<Integer> selectedIndicesList = new ArrayList<>();

    public JLabel OutputLabel;
    public JMenuBar menuBar;
    public JFrame frame;
    public JPanel rootPanel;
    public JPanel LeftPanel;
    public JProgressBar progressBar;
    public JComboBox UserCombiscomboBox;

    public MenuActionListener(JFrame frame, JPanel rootPanel, JPanel LeftPanel, JMenuBar menuBar, JLabel OutputLabel, JProgressBar progressBar, JComboBox UserCombiscomboBox) {

        this.frame = frame;
        this.rootPanel = rootPanel;
        this.LeftPanel = LeftPanel;
        this.menuBar = menuBar;
        this.OutputLabel = OutputLabel;
        this.progressBar = progressBar;
        this.UserCombiscomboBox = UserCombiscomboBox;
    }




    // menuListener
    public void actionPerformed(ActionEvent mev) {
        String[] dummy = null;
        logger.info("Selected: {}", mev.getActionCommand());
        selectedIndicesList = MyVariables.getselectedIndicesList();
        if (selectedIndicesList == null) {
            selectedIndicesList = new ArrayList<>();
        }

        switch (mev.getActionCommand()) {
            case "Preferences":
                prefsDialog.showDialog();
                break;
            case "Exit":
                StandardFileIO.deleteDirectory(new File(MyVariables.gettmpWorkFolder()) );
                Utils.GetSetLeftPanelWidth(LeftPanel, "save");
                Utils.GetSetGuiSize(frame, "save");
                System.exit(0);
                break;
            case "Rename photos":
                RenamePhotos renPhotos = new RenamePhotos();
                //renPhotos.setTitle(ResourceBundle.getBundle("translations/program_strings").getString("renamephotos.title"));
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    renPhotos.showDialog(true);
                } else {
                    renPhotos.showDialog(false);
                }
                break;
            case "Copy all metadata to xmp format":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    metaData.copyToXmp(OutputLabel);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Repair JPGs with corrupted metadata":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.repairjpgs"));
                    metaData.repairJPGMetadata( progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            /*case "Export metadata":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ExportMetadata expMetadata = new ExportMetadata();
                    expMetadata.showDialog(selectedIndices, MyVariables.getLoadedFiles(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break; */
            case "sidecarhelp":
                Utils.openBrowser(ProgramTexts.ProjectWebSite + "/manual/index.html#sidecar");
                break;
            case "exportexifsidecar":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exifsidecar"));
                    metaData.exportExifMieExvSidecar(rootPanel, progressBar, "exif");
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "exportxmpsidecar":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.xmpsidecar"));
                    metaData.exportXMPSidecar(rootPanel, progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "exportmiesidecar":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.miesidecar"));
                    metaData.exportExifMieExvSidecar(rootPanel, progressBar, "mie");
                    //metaData.exportMIESidecar(progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "exportexvsidecar":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exvsidecar"));
                    metaData.exportExifMieExvSidecar(rootPanel, progressBar, "exv");
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Remove metadata":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    RemoveMetadata rmMetadata = new RemoveMetadata();
                    rmMetadata.showDialog(progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Shift Date/time":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ShiftDateTime SDT = new ShiftDateTime();
                    SDT.showDialog(progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Modify Date/time":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ModifyDateTime MDT = new ModifyDateTime();
                    MDT.showDialog(progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Set file date to DateTimeOriginal":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    dateTime.setFileDateTimeToDateTimeOriginal(progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Create args file(s)":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    CreateArgsFile CAF = new CreateArgsFile();
                    CAF.showDialog(selectedIndices, MyVariables.getLoadedFiles(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "Export all previews/thumbs from selected":
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.extractpreviewsthumbs"));
                    Utils.ExportPreviewsThumbnails(progressBar);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "UserMetadata":
                MD.showDialog(rootPanel);
                String sqlsets = SQLiteJDBC.getdefinedCustomSets();
                String[] views = sqlsets.split("\\r?\\n"); // split on new lines
                UserCombiscomboBox.setModel(new DefaultComboBoxModel(views));
                break;
            case "DeleteFavorites":
                DeleteFavorite DelFavs = new DeleteFavorite();
                DelFavs.showDialog(rootPanel);
                break;
            case "DeleteLenses":
                SelectmyLens SmL = new SelectmyLens();
                SmL.showDialog(rootPanel, "delete lens");
                break;
            case "ExiftoolDatabase":
                ExiftoolDatabase.showDialog();
                break;
            case "About jExifToolGUI":
                //JOptionPane.showMessageDialog(mainScreen.this.rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("abouttext")), ResourceBundle.getBundle("translations/program_help_texts").getString("abouttitle"), JOptionPane.INFORMATION_MESSAGE);
                WV.HTMLView(ResourceBundle.getBundle("translations/program_help_texts").getString("abouttitle"), ResourceBundle.getBundle("translations/program_help_texts").getString("abouttext"), 500, 450);
                break;
            case "About ExifTool":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("aboutexiftool")), ResourceBundle.getBundle("translations/program_help_texts").getString("aboutexiftooltitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "jExifToolGUI homepage":
                Utils.openBrowser(ProgramTexts.ProjectWebSite);
                break;
            case "ExifTool homepage":
                Utils.openBrowser("https://exiftool.org/");
                break;
            case "Online manual":
                Utils.openBrowser(ProgramTexts.ProjectWebSite + "/manual/index.html");
                break;
            case "Credits":
                //JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 400, ProgramTexts.CreditsText), "Credits", JOptionPane.INFORMATION_MESSAGE);
                String Credits = StandardFileIO.readTextFileAsStringFromResource("texts/credits.html");
                WV.HTMLView(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.credits"), String.format(ProgramTexts.HTML, 500, Credits), 600, 800);
                break;
            case "System/Program info":
                String os = SystemPropertyFacade.getPropertyByKey(OS_NAME);
                if (os.contains("APPLE") || os.contains("Mac") ) {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 650, Utils.systemProgramInfo()), "System and Program Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 500, Utils.systemProgramInfo()), "System and Program Information", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "License":
                Utils.showLicense(rootPanel);
                break;
            case "Check for new version":
                Utils.checkForNewVersion("menu");
                break;
            case "Translate":
                Utils.openBrowser("https://github.com/hvdwolf/jExifToolGUI/blob/master/translations/Readme.md");
                break;
            case "Changelog":
                Utils.openBrowser("https://github.com/hvdwolf/jExifToolGUI/blob/master/Changelog.md");
                break;
            case "Donate":
                Utils.openBrowser("https://hvdwolf.github.io/jExifToolGUI/donate.html");
                // Disable for the time being
                //WebPageInPanel WPIP = new WebPageInPanel();
                //WPIP.WebPageInPanel(rootPanel,"https://hvdwolf.github.io/jExifToolGUI/donate.html", 700,300);
                break;
            // Below this line we will add our Help sub menu containing the helptexts topics in this program
            case "editdataexif":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exifandxmphelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("exifhelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "editdataxmp":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exifandxmphelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("xmphelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "editdatagps":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "editdatageotag":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "editdatagpano":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "editdatalens":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "copydata":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatext")), ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "yourcommands":
//                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommands")), ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommandstitle"), JOptionPane.INFORMATION_MESSAGE);
                WV.HTMLView(ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommandstitle"), ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommands"), 700, 500);
                break;
            case "exiftooldb":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbhelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbtitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "menurenaminginfo":
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("renamingtext")), ResourceBundle.getBundle("translations/program_help_texts").getString("renamingtitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }

    }

}
