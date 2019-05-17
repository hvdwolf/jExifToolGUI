package org.hvdw.jexiftoolgui;

import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.datetime.ModifyDateTime;
import org.hvdw.jexiftoolgui.datetime.ShiftDateTime;
import org.hvdw.jexiftoolgui.editpane.EditExifdata;
import org.hvdw.jexiftoolgui.editpane.EditGeotaggingdata;
import org.hvdw.jexiftoolgui.editpane.EditXmpdata;
import org.hvdw.jexiftoolgui.metadata.*;
import org.hvdw.jexiftoolgui.renaming.RenamePhotos;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;


public class mainScreen {
    //private JFrame rootFrame;
    private JMenuBar menuBar;
    private JMenu myMenu;
    private JMenuItem menuItem;
    private JPanel rootPanel;
    private JTabbedPane tabbedPaneRight;
    private JButton buttonLoadImages;
    private JButton buttonShowImage;
    private JPanel LeftPanel;
    private JPanel LeftbuttonBar;
    private JRadioButton radioButtonViewAll;
    private JRadioButton radioButtonViewExif;
    private JRadioButton radioButtonViewXMP;
    private JRadioButton radioButtonViewGPS;
    private JRadioButton radioButtonViewIPTC;
    private JRadioButton radioButtonViewGPano;
    private JRadioButton radioButtonViewICC;
    private JRadioButton radioButtonViewMakernotes;
    private JPanel ViewRadiobuttonpanel;
    private JPanel ViewDatapanel;
    private JScrollPane ViewDatascrollpanel;
    private JTree FileTree;
    private JScrollPane Leftscrollpane;
    private JTable tableListfiles;
    private JTable ListexiftoolInfotable;
    private JPanel ThumbView;
    private JLabel iconLabel;
    private JLabel MyCommandsText;
    private JTextField CommandsParameterstextField;
    private JButton CommandsclearParameterSFieldButton;
    private JButton CommandsclearOutputFieldButton;
    private JButton CommandsgoButton;
    private JButton CommandshelpButton;
    private JTextArea YourCommandsOutputTextArea;
    private JTabbedPane tabbedPaneEditfunctions;
    private JTextField ExifMaketextField;
    private JCheckBox ExifMakecheckBox;
    private JTextField ExifModeltextField;
    private JCheckBox ExifModelcheckBox;
    private JTextField ExifModifyDatetextField;
    private JCheckBox ExifModifyDatecheckBox;
    private JTextField ExifDateTimeOriginaltextField;
    private JCheckBox ExifDateTimeOriginalcheckBox;
    private JTextField ExifCreateDatetextField;
    private JCheckBox ExifCreateDatecheckBox;
    private JTextField ExifArtistCreatortextField;
    private JCheckBox ExifArtistCreatorcheckBox;
    private JTextField ExifCopyrighttextField;
    private JCheckBox ExifCopyrightcheckBox;
    private JTextField ExifUsercommenttextField;
    private JCheckBox ExifUsercommentcheckBox;
    private JTextArea ExifDescriptiontextArea;
    private JCheckBox ExifDescriptioncheckBox;
    private JPanel Camera_Equipment;
    private JPanel DateTime;
    private JPanel CreativeTags;
    private JButton ExifcopyFromButton;
    private JButton ExifsaveToButton;
    private JCheckBox ExifBackupOriginalscheckBox;
    private JButton ExifcopyDefaultsButton;
    private JButton resetFieldsButton;
    private JButton ExifhelpButton;
    private JTextField xmpCreatortextField;
    private JCheckBox xmpCreatorcheckBox;
    private JTextField xmpRightstextField;
    private JCheckBox xmpRightscheckBox;
    private JTextArea xmpDescriptiontextArea;
    private JCheckBox xmpDescriptioncheckBox;
    private JTextField xmpLabeltextField;
    private JCheckBox xmpLabelcheckBox;
    private JTextField xmpSubjecttextField;
    private JCheckBox xmpSubjectcheckBox;
    private JTextField xmpTitletextField;
    private JCheckBox xmpTitlecheckBox;
    private JCheckBox xmpRatingcheckBox;
    private JRadioButton xmp1starradioButton;
    private JRadioButton xmp2starradioButton;
    private JRadioButton xmp3starradioButton;
    private JRadioButton xmp4starradioButton;
    private JRadioButton xmp5starradioButton;
    private JTextField xmpPersontextField;
    private JCheckBox xmpPersoncheckBox;
    private JButton xmpCopyFrombutton;
    private JButton xmpSaveTobutton;
    private JCheckBox xmpBackupOriginalscheckBox;
    private JButton xmpCopyDefaultsbutton;
    private JButton xmpResetFieldsbutton;
    private JButton xmpHelpbutton;
    private JTextField geotaggingImgFoldertextField;
    private JButton geotaggingImgFolderbutton;
    private JTextField geotaggingGPSLogtextField;
    private JButton geotaggingGPSLogbutton;
    private JTextField geotaggingGeosynctextField;
    private JButton geotaggingWriteInfobutton;
    private JButton geotaggingHelpbutton;
    private JLabel GeotaggingLeaveFolderEmptyLabel;
    private JCheckBox geotaggingOverwriteOriginalscheckBox;
    private JPanel ExifEditpanel;
    private JPanel GeotaggingEditpanel;
    private JTextField xmpRegionNametextField;
    private JCheckBox xmpRegionNamecheckBox;
    private JTextField xmpRegionTypetextField;
    private JCheckBox xmpRegionTypecheckBox;
    private JProgressBar progressBar;
    private JLabel OutputLabel;
    private JPanel gpsDateTimePanel;
    private JPanel gpsLocationPanel;
    private JPanel gpsButtonPanel;
    private JPanel gpsCalculationPanel;
    private JTextField gpsVersionIDtextField;
    private JTextField gpsTimeStamptextField;
    private JCheckBox gpstimeStampcheckBox;
    private JTextField gpsDateStamptextField;
    private JCheckBox gpsDateStampcheckBox;
    private JTextField gpsMapDatumtextField;
    private JButton gpsCopyFrombutton;
    private JButton gpsSaveTobutton;
    private JCheckBox gpsBackupOriginalscheckBox;
    private JButton gpsResetFieldsbutton;
    private JButton gpsMapcoordinatesbutton;
    private JButton gpsHelpbutton;
    private JLabel gpsCalculatorLabelText;
    private JTextField gpsLocationtextField;
    private JCheckBox gpsLocationcheckBox;
    private JCheckBox SaveLatLonAltcheckBox;
    private JTextField gpsLatDecimaltextField;
    private JTextField gpsLatdegtextField;
    private JTextField gpsLatDegreestextField;
    private JTextField gpsLatMinutestextField;
    private JTextField gpsLonDecimaltextField;
    private JTextField gpsLonDegreestextField;
    private JTextField gpsLonMinutestextField;
    private JTextField gpsLonSecondstextField;
    private JCheckBox gpsAboveSealevelcheckBox;
    private JPanel gpsLatLonAltPanel;
    private JRadioButton gpsNorthradioButton;
    private JRadioButton gpsEastradioButton;
    private JRadioButton gpsSouthRadioButton;
    private JRadioButton gpsWestradioButton;
    private JTextField gpsCountrytextField;
    private JCheckBox gpsCountrycheckBox;
    private JTextField gpsStateProvincetextField;
    private JCheckBox gpsStateProvincecheckBox;
    private JTextField gpsCitytextField;
    private JCheckBox gpsCitycheckBox;
    private JButton copyToInputFieldsButton;
    private JTextField CalcLatDecimaltextField;
    private JTextField CalcLatDegtextField;
    private JTextField CalcLatMintextField;
    private JTextField CalcLatSectextField;
    private JTextField gpsCalcLonDecimaltextField;
    private JTextField gpsCalcLondegtextField;
    private JTextField CalcLonMintextField;
    private JTextField CalcLonSectextField;
    private JRadioButton CalcNorthRadioButton;
    private JRadioButton CalcSouthRadioButton;
    private JRadioButton CalcEastradioButton;
    private JRadioButton CalcWestRadioButton;
    private JButton decimalToMinutesSecondsButton;
    private JButton minutesSecondsToDecimalButton;
    private JLabel CopyMetaDataUiText;
    private JRadioButton copyAllMetadataRadiobutton;
    private JRadioButton copyAllMetadataSameGroupsRadiobutton;
    private JRadioButton copySelectiveMetadataradioButton;
    private JCheckBox CopyExifcheckBox;
    private JCheckBox CopyXmpcheckBox;
    private JCheckBox CopyIptccheckBox;
    private JCheckBox CopyIcc_profileDataCheckBox;
    private JCheckBox BackupOriginalscheckBox;
    private JCheckBox CopyGpsCheckBox;
    private JCheckBox CopyJfifcheckBox;
    private JButton UseDataFrombutton;
    private JButton CopyDataCopyTobutton;
    private JButton CopyHelpbutton;
    private JRadioButton UseNonPropFontradioButton;
    private JRadioButton UsePropFontradioButton;
    private JRadioButton radioButtonByTagName;
    private JComboBox comboBoxViewByTagName;
    private JRadioButton radioButtoncommonTags;
    private JComboBox comboBoxViewCommonTags;
    private JRadioButton radioButtonCameraMakes;
    private JComboBox comboBoxViewCameraMake;
    private JTextField geotaggingLocationtextfield;
    private JTextField geotaggingCountrytextfield;
    private JCheckBox geotaggingLocationcheckbox;
    private JCheckBox geotaggingCountrycheckbox;
    private JTextField geotaggingStatetextfield;
    private JCheckBox geotaggingStatecheckbox;
    private JTextField geotaggingCitytextfield;
    private JCheckBox geotaggingCitycheckbox;
    private JLabel GeotaggingLocationLabel;
    private JTextField ExiftoolLocationtextField;
    private MenuListener menuListener;
    private JPanel prefPanel;
    public ImageIcon icon;


    public File[] files;
    int[] selectedIndices;
    List<Integer> selectedIndicesList = new ArrayList<Integer>();
    public int SelectedRow;
    public int SelectedCopyFromImageIndex;  // Used for the copy metadata from ..

    private Preferences prefs;
    public String exiftool_path = "";
    ListSelectionModel listSelectionModel;

    // Initialize all the helper classes
    Utils myUtils = new Utils();
    myVariables myVars = new myVariables();
    PreferencesDialog prefsDialog = new PreferencesDialog();
    MetaData metaData = new MetaData();
    DateTime dateTime = new DateTime();
    EditExifdata EEd = new EditExifdata();
    EditXmpdata EXd = new EditXmpdata();
    EditGeotaggingdata EGd = new EditGeotaggingdata();
    YourCommands YourCmnds = new YourCommands();

//////////////////////////////////////////////////////////////////////////////////
    // Define the several arrays for the several Edit panes on the right side. An interface or getter/setter methods would be more "correct java", but also
    // creates way more code which doesn't make it clearer either.
    JTextField[] exifFields = {ExifMaketextField, ExifModeltextField, ExifModifyDatetextField, ExifDateTimeOriginaltextField,ExifCreateDatetextField,
        ExifArtistCreatortextField, ExifCopyrighttextField, ExifUsercommenttextField};
    JTextArea[] exifAreas = {ExifDescriptiontextArea};
    JCheckBox[] exifBoxes = {ExifMakecheckBox, ExifModelcheckBox, ExifModifyDatecheckBox, ExifDateTimeOriginalcheckBox,ExifCreateDatecheckBox,
            ExifArtistCreatorcheckBox, ExifCopyrightcheckBox, ExifUsercommentcheckBox, ExifDescriptioncheckBox, ExifBackupOriginalscheckBox};

    JTextField[] xmpFields = {xmpCreatortextField, xmpRightstextField,xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField};
    JTextArea[] xmpAreas = {xmpDescriptiontextArea};
    JCheckBox[] xmpBoxes = {xmpCreatorcheckBox, xmpRightscheckBox, xmpLabelcheckBox, xmpSubjectcheckBox, xmpTitlecheckBox, xmpPersoncheckBox, xmpRegionNamecheckBox, xmpRegionTypecheckBox, xmpDescriptioncheckBox, xmpBackupOriginalscheckBox};

    JTextField[] geotaggingFields = {geotaggingImgFoldertextField, geotaggingGPSLogtextField, geotaggingGeosynctextField, geotaggingLocationtextfield, geotaggingCountrytextfield, geotaggingStatetextfield, geotaggingCitytextfield};
    JCheckBox[] geotaggingBoxes = {geotaggingLocationcheckbox, geotaggingCountrycheckbox, geotaggingStatecheckbox, geotaggingCitycheckbox};

    JRadioButton[] CopyMetaDataRadiobuttons = {copyAllMetadataRadiobutton, copyAllMetadataSameGroupsRadiobutton, copySelectiveMetadataradioButton};
    JCheckBox[] CopyMetaDataCheckBoxes = {CopyExifcheckBox, CopyXmpcheckBox, CopyIptccheckBox, CopyIcc_profileDataCheckBox, CopyGpsCheckBox, CopyJfifcheckBox, BackupOriginalscheckBox};

//////////////////////////////////////////////////////////////////////////////////


    /////////////////////// Startup checks /////////////////
// where is exiftool, if available
    static String exiftool_check() {
        String res = "";
        List<String> cmdparams = new ArrayList<String>();

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        if (isWindows) {
            String[] params = {"where", "exiftool"};
            cmdparams.addAll(Arrays.asList(params));
        } else {
            String[] params = {"which", "exiftool"};
            cmdparams.addAll(Arrays.asList(params));
        }

        try {
            Utils myUtils = new Utils();
            res = myUtils.runCommand(cmdparams); // res returns path to exiftool; on error on windows "INFO: Could not ...", on linux returns nothing
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
            res = ex.getMessage();
        }

        return res;
    }

    //check preferences (a.o. exiftool)
    boolean check_preferences() {
        boolean exiftool_exists = false;
        boolean exiftool_found = false;
        String res = "";
        boolean newversion_startupcheck_exists = false;

        Preferences prefs = Preferences.userRoot();

        exiftool_exists = prefs.get("exiftool", null) != null;
        System.out.println("exiftool_exists reports: " + exiftool_exists);


        if (exiftool_exists) {
            String exiftool_path = prefs.get("exiftool", "");
            System.out.println("preference exiftool returned: "+ exiftool_path);
            if (exiftool_path  == null || exiftool_path.isEmpty()) {
                res = exiftool_check();
            } else {
                res = exiftool_path;
                //String[] cmdparams = {res, "-ver"};
                List<String> cmdparams = new ArrayList<String>();
                cmdparams.add(res);
                cmdparams.add("-ver");
                try {
                    OutputLabel.setText("Exiftool available;  Version: " + myUtils.runCommand(cmdparams));
                } catch(IOException | InterruptedException ex) {
                    System.out.println("Error executing command");
                }

            }
        } else { // does not exist
            res = exiftool_check();
        }

            if (res == null || res.isEmpty() || res.toLowerCase().startsWith("info")) {
                exiftool_found = false;
            } else {
                exiftool_found = true;
                // We already checked that the node did not exist and that it is empty or null
                // remove all possible line breaks
                res = res.replace("\n", "").replace("\r", "");
                if (!exiftool_exists) {
                    prefs.put("exiftool", res);
                }
            }

        return exiftool_found;
    }
/////////////////////////// End of Startup checks //////////////////////////

    void LoadImages() {
        OutputLabel.setText("Loading images ....");
        files = myUtils.getFileNames(mainScreen.this.rootPanel);
        if (files != null) {
            myUtils.displayFiles(mainScreen.this.tableListfiles, mainScreen.this.ListexiftoolInfotable, mainScreen.this.iconLabel, files);
            myUtils.ImageInfo(MyConstants.all_params, 0, files, mainScreen.this.ListexiftoolInfotable);
            try {
                myUtils.DisplayImage(0, files, mainScreen.this.iconLabel);
            } catch(IOException ex) {
                System.out.println("Error reading Image");
            }
            mainScreen.this.buttonShowImage.setEnabled(true);
            OutputLabel.setText(" Images loaded ...");
        }
    }


    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            //System.out.println(comp.toString());
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    void setCopyMetaDatacheckboxes(boolean state) {
        CopyExifcheckBox.setEnabled(state);
        CopyXmpcheckBox.setEnabled(state);
        CopyIptccheckBox.setEnabled(state);
        CopyIcc_profileDataCheckBox.setEnabled(state);
        CopyGpsCheckBox.setEnabled(state);
        CopyJfifcheckBox.setEnabled(state);
    }

    void FillViewTagNamesComboboxes() {
        // Filla all combo boxes in the View panel
        String TagNames = myUtils.ResourceReader("resources/ExifToolTagNames.txt");
        String[] Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewByTagName.setModel(new DefaultComboBoxModel(Tags));

        TagNames = myUtils.ResourceReader("resources/CommonTags.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewCommonTags.setModel(new DefaultComboBoxModel(Tags));

        TagNames = myUtils.ResourceReader("resources/CameraTagNames.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewCameraMake.setModel(new DefaultComboBoxModel(Tags));

    }
    ///////////////////////// Action Listeners ////////////////////////////////
    class MenuActionListener implements ActionListener {

    // menuListener
    public void actionPerformed(ActionEvent ev) {
            //myVariables myVars = new myVariables();
        String[] dummy = null;
            System.out.println("Selected: " + ev.getActionCommand());

            switch(ev.getActionCommand()) {
                case "Load Images":
                    // identical to button "Load Images"
                    LoadImages();
                    break;
                case "Preferences":
                    PreferencesDialog prefdialog = new PreferencesDialog();
                    prefdialog.showDialog();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "Rename photos":
                    RenamePhotos renPhotos = new RenamePhotos();
                    renPhotos.setTitle("Rename Photos");
                    renPhotos.showDialog();
                    break;
                case "Copy all metadata to xmp format":
                    if (selectedIndicesList.size() > 0) {
                        OutputLabel.setText("Copying all relevant data to its xmp variants, please be patient ...");
                        metaData.copytoxmp(selectedIndices, files);
                        OutputLabel.setText("Inactive ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Repair JPG(s) with corrupted metadata":
                    if (selectedIndicesList.size() > 0) {
                        OutputLabel.setText("Repairing jpg data, please be patient ...");
                        metaData.repairJPGmetadata(selectedIndices, files, progressBar);
                        OutputLabel.setText("Inactive ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Export metadata":
                    if (selectedIndicesList.size() > 0) {
                        ExportMetadata expMetadata = new ExportMetadata();
                        expMetadata.showDialog(selectedIndices, files);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Remove metadata":
                    if (selectedIndicesList.size() > 0) {
                        RemoveMetadata rmMetadata = new RemoveMetadata();
                        rmMetadata.showDialog(selectedIndices, files);
                        OutputLabel.setText("Inactive ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Shift Date/time":
                    if (selectedIndicesList.size() > 0) {
                        ShiftDateTime SDT = new ShiftDateTime();
                        SDT.showDialog(selectedIndices, files);
                        OutputLabel.setText("Inactive ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Modify Date/time":
                    if (selectedIndicesList.size() > 0) {
                        ModifyDateTime MDT = new ModifyDateTime();
                        MDT.showDialog(selectedIndices, files);
                        OutputLabel.setText("Inactive ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Set file date to DateTimeOriginal":
                    if (selectedIndicesList.size() > 0) {
                        dateTime.setFileDatetoDateTimeOriginal(selectedIndices, files);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Create args file(s)":
                    if (selectedIndicesList.size() > 0) {
                        CreateArgsFile CAF = new CreateArgsFile();
                        CAF.showDialog(selectedIndices, files);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "About jExifToolGUI":
                    JOptionPane.showMessageDialog(mainScreen.this.rootPanel,programTexts.aboutText,"About jExifToolGUI for ExifTool by Phil Harvey",JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "About ExifTool":
                    JOptionPane.showMessageDialog(mainScreen.this.rootPanel,programTexts.aboutExifToolText,"About ExifTool by Phil Harvey",JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "jExifToolGUI homepage":
                    myUtils.openBrowser(programTexts.ProjectWebSite);
                    break;
                case "ExifTool homepage":
                    myUtils.openBrowser("https://www.sno.phy.queensu.ca/~phil/exiftool/");
                    break;
                case "License":
                    myUtils.License(mainScreen.this.rootPanel);
                    break;
                case "Check for new version":
                    myUtils.CheckforNewVersion("menu");
                    break;
                default:
                    break;
            }

        }
    }


    private void programButtonListeners() {
        // Main screen left panel
        buttonLoadImages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //File opener: Load the images; identical to Menu option Load Images.
                LoadImages();
            }
        });
        buttonShowImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                myUtils.extDisplayImage();
            }
        });

        // Your Commands pane buttons
        CommandsclearParameterSFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CommandsParameterstextField.setText("");
            }
        });
        CommandsclearOutputFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                YourCommandsOutputTextArea.setText("");
            }
        });
        CommandsgoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    if (CommandsParameterstextField.getText().length() > 0) {
                        OutputLabel.setText("Now executing your commands ...");
                        YourCmnds.ExecuteCommands(CommandsParameterstextField.getText(), YourCommandsOutputTextArea, UseNonPropFontradioButton, selectedIndices, files );
                        OutputLabel.setText("The output should be displayed above ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, "No command parameters given","No parameters",JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        CommandshelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel,HelpTexts.YourCommandsHelp,"Help for the Your Commands panel",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit Exif pane buttons
        ExifcopyFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EEd.copyexiffromselected(exifFields, ExifDescriptiontextArea, files, SelectedRow);
            }
        });
        ExifsaveToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    EEd.writeExifTags(exifFields, ExifDescriptiontextArea, exifBoxes, selectedIndices, files);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        ExifcopyDefaultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        resetFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EEd.resetfields(exifFields, ExifDescriptiontextArea);
            }
        });
        ExifhelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel,HelpTexts.ExifAndXmpHelp,"Help for the Exif edit panel",JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Edit xmp buttons
        xmpCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EXd.copyxmpfromselected(xmpFields, xmpDescriptiontextArea, files, SelectedRow);
            }
        });
        xmpSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    EXd.writeXmpTags(xmpFields, xmpDescriptiontextArea, xmpBoxes, selectedIndices, files);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, programTexts.NoImgSelected,"No images selected",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        xmpCopyDefaultsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        xmpResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EXd.resetfields(xmpFields, xmpDescriptiontextArea);
            }
        });
        xmpHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel,HelpTexts.ExifAndXmpHelp,"Help for the XMP edit panel",JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Edit geotagging buttons
        geotaggingImgFolderbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ImgPath = EGd.ImgPath(rootPanel);
                if (!"".equals(ImgPath)) {
                    geotaggingImgFoldertextField.setText(ImgPath);
                }
            }
        });
        geotaggingGPSLogbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String TrackFile = EGd.gpsLogFile(rootPanel);
                if (!"".equals(TrackFile)) {
                    geotaggingGPSLogtextField.setText(TrackFile);
                }
            }
        });
        geotaggingWriteInfobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (("".equals(geotaggingImgFoldertextField.getText())) && (selectedIndicesList.size() == 0)) {
                    JOptionPane.showMessageDialog(rootPanel, "No images selected and no image folder path selected","No images, no path",JOptionPane.WARNING_MESSAGE);
                } else {
                    if ("".equals(geotaggingGPSLogtextField.getText())) {
                        JOptionPane.showMessageDialog(rootPanel, "No gps track log selected","No gps log",JOptionPane.WARNING_MESSAGE);
                    } else {
                        EGd.WriteInfo(geotaggingFields, geotaggingBoxes, geotaggingOverwriteOriginalscheckBox.isSelected(), selectedIndices, files);
                    }
                }
            }
        });
        geotaggingHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel,HelpTexts.GeotaggingHelp,"Help for the Geotagging edit panel",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit gps buttons
        gpsCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        gpsSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        gpsResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        gpsMapcoordinatesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                myUtils.openBrowser("https://www.mapcoordinates.net/en");
            }
        });
        gpsHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        decimalToMinutesSecondsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        minutesSecondsToDecimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        copyToInputFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // Copy metadata buttons
        copyAllMetadataRadiobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setCopyMetaDatacheckboxes(false);
            }
        });
        copyAllMetadataSameGroupsRadiobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setCopyMetaDatacheckboxes(false);
            }
        });
        copySelectiveMetadataradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setCopyMetaDatacheckboxes(true);
            }
        });
        UseDataFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SelectedCopyFromImageIndex = SelectedRow;
            }
        });
        CopyDataCopyTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                metaData.CopyMetaData(CopyMetaDataRadiobuttons, CopyMetaDataCheckBoxes, SelectedCopyFromImageIndex, selectedIndices, files);
            }
        });
        CopyHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel,HelpTexts.CopyMetaDataHelp,"Help for the Copy metadata panel",JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    void ViewRadiobuttonListener() {
        //String[] params;
        /*ButtonGroup ViewRbtns = new ButtonGroup();
        ViewRbtns.add(mainScreen.this.radioButtonViewAll);
        ViewRbtns.add(mainScreen.this.radioButtonByTagName);
        ViewRbtns.add(mainScreen.this.radioButtoncommonTags);
        ViewRbtns.add(mainScreen.this.radioButtonCameraMakes);
        ViewRbtns.add(mainScreen.this.radioButtonViewExif);
        ViewRbtns.add(mainScreen.this.radioButtonViewXMP);
        ViewRbtns.add(mainScreen.this.radioButtonViewIPTC);
        ViewRbtns.add(mainScreen.this.radioButtonViewGPS);
        ViewRbtns.add(mainScreen.this.radioButtonViewGPano);
        ViewRbtns.add(mainScreen.this.radioButtonViewICC);
        ViewRbtns.add(mainScreen.this.radioButtonViewMakernotes); */


        //myVariables myVars = new myVariables();

        //Add listeners
        radioButtonViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewAll.getText());
                myUtils.ImageInfo(MyConstants.all_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtoncommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] params = myUtils.WhichCommonTagSelected(comboBoxViewCommonTags);
                //myUtils.ImageInfoByTagName(comboBoxViewCommonTags, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                myUtils.ImageInfo(params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewCommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtoncommonTags.isSelected()) {
                    String[] params = myUtils.WhichCommonTagSelected(comboBoxViewCommonTags);
                    //myUtils.ImageInfoByTagName(comboBoxViewCommonTags, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                    myUtils.ImageInfo(params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });

        /*radioButtonViewExif.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewExif.getText());
                myUtils.ImageInfo(MyConstants.exif_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewXMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewXMP.getText());
                myUtils.ImageInfo(MyConstants.xmp_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewIPTC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewIPTC.getText());
                myUtils.ImageInfo(MyConstants.iptc_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewGPS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewGPS.getText());
                myUtils.ImageInfo(MyConstants.gpc_loc_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewGPano.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewGPano.getText());
                myUtils.ImageInfo(MyConstants.gpano_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewICC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewICC.getText());
                myUtils.ImageInfo(MyConstants.icc_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtonViewMakernotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button selected: " + radioButtonViewMakernotes.getText());
                myUtils.ImageInfo(MyConstants.makernotes_params, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        }); */
        radioButtonByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myUtils.ImageInfoByTagName(comboBoxViewByTagName, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtonByTagName.isSelected()) {
                    myUtils.ImageInfoByTagName(comboBoxViewByTagName, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });
        radioButtonCameraMakes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                myUtils.ImageInfoByTagName(comboBoxViewCameraMake, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewCameraMake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtonCameraMakes.isSelected()) {
                    myUtils.ImageInfoByTagName(comboBoxViewCameraMake, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });
    }


    public String[] whichRBselected() {
        // just to make sure anything is returned we defaultly return all
        String[] params = MyConstants.all_params;
        // Very simple if list
        if (mainScreen.this.radioButtonViewAll.isSelected()) {
            params = MyConstants.all_params;
        /*} else if (radioButtonViewExif.isSelected()) {
            params = MyConstants.exif_params;
        } else if (radioButtonViewXMP.isSelected()) {
            params = MyConstants.xmp_params;
        } else if (radioButtonViewIPTC.isSelected()) {
            params = MyConstants.iptc_params;
        } else if (radioButtonViewGPS.isSelected()) {
            params = MyConstants.gpc_loc_params;
        } else if (radioButtonViewGPano.isSelected()) {
            params = MyConstants.gpano_params;
        } else if (radioButtonViewICC.isSelected()) {
            params = MyConstants.icc_params;
        } else if (radioButtonViewMakernotes.isSelected()) {
            params = MyConstants.makernotes_params; */
        } else if (radioButtoncommonTags.isSelected()) {
            params = myUtils.WhichCommonTagSelected(comboBoxViewCommonTags);
        } else if (radioButtonByTagName.isSelected()) {
            params = myUtils.WhichTagSelected(comboBoxViewByTagName);
        } else if (radioButtonCameraMakes.isSelected()) {
            params = myUtils.WhichTagSelected(comboBoxViewCameraMake);
        }
        return params;
    }


    // This is the general table listener that also enables multi selection
    class SharedListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            List<Integer> tmpselectedIndices = new ArrayList<Integer>();
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();

            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();
            //System.out.println("selected indexes:");

            if (lsm.isSelectionEmpty()) {
                System.out.println("none selected");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        //System.out.println(" " + i);
                        tmpselectedIndices.add(i);
                        SelectedRow = i;
                    }
                }
                String[] params = whichRBselected();
                myUtils.ImageInfo(params, SelectedRow, files, ListexiftoolInfotable);
                try {
                    myUtils.DisplayImage(SelectedRow, files, mainScreen.this.iconLabel);
                } catch(IOException ex) {
                    System.out.println("Error reading Image");
                }
                selectedIndices = tmpselectedIndices.stream().mapToInt(Integer::intValue).toArray();
                selectedIndicesList = tmpselectedIndices;
                //System.out.println(Arrays.toString(selectedIndices));
                myVars.setSelectedFilenamesIndices(selectedIndices);
            }
            //output.setCaretPosition(output.getDocument().getLength());
        }
    }

    void fileNamesTableMouseListener() {
        // Use the mouse listener for the single selection for the left table with the file names

        mainScreen.this.tableListfiles.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = mainScreen.this.tableListfiles.rowAtPoint(evt.getPoint());

                if (row >= 0) {
                    //myVars.setMySelectedRow(row);
                    //myVars.setMySelectedColumn(col);
                    //System.out.println("valueChanged loop: selected row: " + String.valueOf(SelectedRow)); // + " selected column: " + String.valueOf(myVars.getMySelectedColumn()));
                    /*String[] params = whichRBselected();
                    myUtils.ImageInfo(params, row, files, ListexiftoolInfotable);
                    try {
                        myUtils.DisplayImage(row, files, mainScreen.this.iconLabel);
                    } catch(IOException ex) {
                        System.out.println("Error reading Image");
                    } */
                }

            }
        });

    }


/////////////////////// End of Action Listeners (and radio button groups) /////////////////////

    void createmyMenuBar(JFrame frame) {
        menuBar = new JMenuBar();

        // File menu
        myMenu = new JMenu("File");
        myMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(myMenu);
        menuItem = new JMenuItem("Load Images");
        myMenu.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Preferences");
        myMenu.setMnemonic(KeyEvent.VK_P);
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);

        // Extra menu
        myMenu = new JMenu("Extra");
        myMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(myMenu);
        menuItem = new JMenuItem("Rename photos");
        //myMenu.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem("Export metadata");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Copy all metadata to xmp format");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Remove metadata");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem("Shift Date/time");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Modify Date/time");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Set file date to DateTimeOriginal");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem("Repair JPG(s) with corrupted metadata");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Create args file(s)");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);

        // Help menu
        myMenu = new JMenu("Help");
        myMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(myMenu);
        menuItem = new JMenuItem("jExifToolGUI homepage");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("ExifTool homepage");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Manual");
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem("Donate");
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem("License");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("Check for new version");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("About ExifTool");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem("About jExifToolGUI");
        menuItem.addActionListener(new MenuActionListener());
        myMenu.add(menuItem);

        // Finally add menubar to the frame
        frame.setJMenuBar(menuBar);
    }

////////////////////////////////////////////////////////////////////////////////////////
    public mainScreen(JFrame frame) {
        boolean preferences = false;
        Preferences prefs = Preferences.userRoot();

        progressBar.setVisible(false);
        //progressBar.setStringPainted(false);
        createmyMenuBar(frame);
        ViewRadiobuttonListener();
        /*FillViewCommonTagsCombobox();
        FillViewCameraMakeComboBox();*/
        FillViewTagNamesComboboxes();
        //fileNamesTableMouseListener();
        /////
        listSelectionModel = tableListfiles.getSelectionModel();
        tableListfiles.setRowSelectionAllowed(true);
        tableListfiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());

        // icon for my dialogs
        InputStream stream = getClass().getResourceAsStream("resources/jexiftoolgui-64.png");
        try {
            icon = new ImageIcon(ImageIO.read(stream));
        } catch(IOException ex) {
            System.out.println("Error executing command");
        }

        try {
            myUtils.DisplayLogo(mainScreen.this.iconLabel);
        } catch(IOException ex) {
            System.out.println("Error reading Logo");
        }

        preferences = check_preferences();
        if (!preferences) {
            myUtils.checkExifTool(mainScreen.this.rootPanel);
        }

        programButtonListeners();

        // Some texts
        MyCommandsText.setText(programTexts.MyCommandsText);
        GeotaggingLeaveFolderEmptyLabel.setText(programTexts.GeotaggingLeaveFolderEmpty);
        GeotaggingLocationLabel.setText(programTexts.GeotaggingLocationLabel);
        gpsCalculatorLabelText.setText(programTexts.gpsCalculatorLabelText);

        myUtils.CheckforNewVersion("startup");


    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("jExifToolGUI V" + programTexts.Version + "   for ExifTool by Phil Harvey");
        frame.setContentPane(new mainScreen(frame).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*frame.setIconImage(
                new ImageIcon(getClass().getClassLoader().getResource("resources/jexiftoolgui.ico"))
        );*/
        try {
            // Significantly improves the look of the output in
            // terms of the folder/file icons and file names returned by FileSystemView!
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception weTried) {
        }

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }
}
