package org.hvdw.jexiftoolgui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.lang3.tuple.Pair;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.controllers.YourCommands;
import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.datetime.ModifyDateTime;
import org.hvdw.jexiftoolgui.datetime.ShiftDateTime;
import org.hvdw.jexiftoolgui.editpane.EditExifdata;
import org.hvdw.jexiftoolgui.editpane.EditGPSdata;
import org.hvdw.jexiftoolgui.editpane.EditGeotaggingdata;
import org.hvdw.jexiftoolgui.editpane.EditGpanodata;
import org.hvdw.jexiftoolgui.editpane.EditXmpdata;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.metadata.CreateArgsFile;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;
import org.hvdw.jexiftoolgui.metadata.MetaData;
import org.hvdw.jexiftoolgui.metadata.RemoveMetadata;
import org.hvdw.jexiftoolgui.renaming.RenamePhotos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;


public class mainScreen {
    private static final Logger logger = LoggerFactory.getLogger(mainScreen.class);

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
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
    private JPanel ViewRadiobuttonpanel;
    private JPanel ViewDatapanel;
    private JScrollPane ViewDatascrollpanel;
    private JTree FileTree;
    private JScrollPane Leftscrollpane;
    private JTable tableListfiles;
    private JTable ListexiftoolInfotable;
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
    private JPanel gpsLocationPanel;
    private JPanel gpsButtonPanel;
    private JPanel gpsCalculationPanel;
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
    private JButton resetGeotaggingbutton;
    private JLabel GeotaggingGeosyncExplainLabel;
    private JTextField gpsAltDecimaltextField;
    private JLabel gPanoTopText;
    private JFormattedTextField gpanoCAIHPtextField;
    private JFormattedTextField gpanoCAIWPtextField;
    private JFormattedTextField gpanoCALPtextField;
    private JFormattedTextField gpanoCATPtextField;
    private JTextField gpanoStitchingSoftwaretextField;
    private JFormattedTextField gpanoFPHPtextField;
    private JFormattedTextField gpanoFPWPtextField;
    private JComboBox gpanoPTcomboBox;
    private JCheckBox checkBox1;
    private JFormattedTextField gpanoPHDtextField;
    private JFormattedTextField gpanoIVHDtextField;
    private JCheckBox gpanoIVHDCheckBox;
    private JFormattedTextField gpanoIVPDtextField;
    private JCheckBox gpanoIVPDCheckBox;
    private JFormattedTextField gpanoIVRDtextField;
    private JCheckBox gpanoIVRDCheckBox;
    private JFormattedTextField gpanoIHFOVDtextField;
    private JCheckBox gpanoIHFOVDtextFieldCheckBox;
    private JButton gpanoResetFieldsbutton;
    private JButton gpanoHelpbutton;
    private JCheckBox gpanoOverwriteOriginalscheckBox;
    private JButton gpanoCopyFrombutton;
    private JButton gpanoCopyTobutton;
    private JLabel gpanoMinVersionText;
    private JCheckBox gpanoStitchingSoftwarecheckBox;
    private JCheckBox gpanoPHDcheckBox;
    private JTextField xmpCredittextField;
    private JCheckBox xmpCreditcheckBox;
    private JLabel xmpGoogleText;
    private JCheckBox CopyMakernotescheckBox;
    private JTextField ExiftoolLocationtextField;
    private MenuListener menuListener;
    private JPanel prefPanel;
    private ImageIcon icon;


    public File[] files;
    private int[] selectedIndices;
    private List<Integer> selectedIndicesList = new ArrayList<Integer>();
    private int SelectedRow;
    public int SelectedCell;
    private int SelectedCopyFromImageIndex;  // Used for the copy metadata from ..

    public String exiftool_path = "";
    private ListSelectionModel listSelectionModel;

    // Initialize all the helper classes
    PreferencesDialog prefsDialog = new PreferencesDialog();
    private MetaData metaData = new MetaData();
    private DateTime dateTime = new DateTime();
    private EditExifdata EEd = new EditExifdata();
    private EditXmpdata EXd = new EditXmpdata();
    private EditGeotaggingdata EGd = new EditGeotaggingdata();
    private EditGPSdata EGPSd = new EditGPSdata();
    private YourCommands YourCmnds = new YourCommands();
    private EditGpanodata EGpanod = new EditGpanodata();

//////////////////////////////////////////////////////////////////////////////////
    // Define the several arrays for the several Edit panes on the right side. An interface or getter/setter methods would be more "correct java", but also
    // creates way more code which doesn't make it clearer either.

    private JTextField[] getExifFields() {
        return new JTextField[]{ExifMaketextField, ExifModeltextField, ExifModifyDatetextField, ExifDateTimeOriginaltextField, ExifCreateDatetextField,
                ExifArtistCreatortextField, ExifCopyrighttextField, ExifUsercommenttextField};
    }

    private JTextArea[] getExifAreas() {
        return new JTextArea[]{ExifDescriptiontextArea};
    }

    private JCheckBox[] getExifBoxes() {
        return new JCheckBox[]{ExifMakecheckBox, ExifModelcheckBox, ExifModifyDatecheckBox, ExifDateTimeOriginalcheckBox, ExifCreateDatecheckBox,
                ExifArtistCreatorcheckBox, ExifCopyrightcheckBox, ExifUsercommentcheckBox, ExifDescriptioncheckBox, ExifBackupOriginalscheckBox};
    }
    private JTextField[] getXmpFields() {
    return new JTextField[] {xmpCreatortextField, xmpCredittextField, xmpRightstextField, xmpLabeltextField, xmpSubjecttextField, xmpTitletextField, xmpPersontextField, xmpRegionNametextField, xmpRegionTypetextField};
    }
    private JTextArea[] getXmpAreas() {
        return new JTextArea[] {xmpDescriptiontextArea};
    }

    private JCheckBox[] getXmpBoxes() {        return new JCheckBox[]{xmpCreatorcheckBox, xmpCreditcheckBox, xmpRightscheckBox, xmpLabelcheckBox, xmpSubjectcheckBox, xmpTitlecheckBox, xmpPersoncheckBox, xmpRegionNamecheckBox, xmpRegionTypecheckBox, xmpDescriptioncheckBox, xmpBackupOriginalscheckBox}; }

    private JTextField[] getGeotaggingFields() { return new JTextField[] {geotaggingImgFoldertextField, geotaggingGPSLogtextField, geotaggingGeosynctextField, geotaggingLocationtextfield, geotaggingCountrytextfield, geotaggingStatetextfield, geotaggingCitytextfield}; }

    private JCheckBox[] getGeotaggingBoxes() {return new JCheckBox[] {geotaggingLocationcheckbox, geotaggingCountrycheckbox, geotaggingStatecheckbox, geotaggingCitycheckbox};}

    private JRadioButton[] getCopyMetaDataRadiobuttons() {return new JRadioButton[] {copyAllMetadataRadiobutton, copyAllMetadataSameGroupsRadiobutton, copySelectiveMetadataradioButton}; }
    private JCheckBox[] getCopyMetaDataCheckBoxes() {return new JCheckBox[] {CopyExifcheckBox, CopyXmpcheckBox, CopyIptccheckBox, CopyIcc_profileDataCheckBox, CopyGpsCheckBox, CopyJfifcheckBox, CopyMakernotescheckBox, BackupOriginalscheckBox}; }

    private JTextField[] getGPSFields() {
        return new JTextField[] {gpsLatDecimaltextField, gpsLonDecimaltextField, gpsAltDecimaltextField, gpsLocationtextField, gpsCountrytextField, gpsStateProvincetextField, gpsCitytextField};
    }
    private JCheckBox[] getGpsBoxes() {
        return new JCheckBox[] {SaveLatLonAltcheckBox, gpsAboveSealevelcheckBox, gpsLocationcheckBox, gpsCountrycheckBox, gpsStateProvincecheckBox, gpsCitycheckBox, gpsBackupOriginalscheckBox};
    }

    private JFormattedTextField[] getGpanoFields() {
        return new JFormattedTextField[] {gpanoCAIHPtextField, gpanoCAIWPtextField, gpanoCALPtextField, gpanoCATPtextField, gpanoFPHPtextField, gpanoFPWPtextField, gpanoPHDtextField, gpanoIVHDtextField, gpanoIVPDtextField, gpanoIVRDtextField, gpanoIHFOVDtextField};
    }
    private JCheckBox[] getGpanoCheckBoxes() {
        return new JCheckBox[] {gpanoPHDcheckBox, gpanoStitchingSoftwarecheckBox, gpanoIVHDCheckBox, gpanoIVPDCheckBox, gpanoIVRDCheckBox, gpanoIHFOVDtextFieldCheckBox, gpanoOverwriteOriginalscheckBox};
    }

//////////////////////////////////////////////////////////////////////////////////


    //check preferences (a.o. exiftool)
    private boolean checkPreferences() {
        boolean exiftool_exists;
        boolean exiftool_found = false;
        String res;
        List<String> cmdparams = new ArrayList<>();


        exiftool_exists = prefs.keyIsSet(EXIFTOOL_PATH);
        logger.info("exiftool_exists reports: {}",exiftool_exists);


        if (exiftool_exists) {
            String exiftool_path = prefs.getByKey(EXIFTOOL_PATH, "");
            File tmpFile = new File(exiftool_path);
            boolean exists = tmpFile.exists();
            if (!exists) {
                exiftool_path = null;
                JOptionPane.showMessageDialog(rootPanel, ProgramTexts.ETpreferenceIncorrect, "exiftool preference incorrect", JOptionPane.WARNING_MESSAGE);
            }
            logger.info("exists is {}", exists);
            logger.info("preference exiftool returned: {}",exiftool_path);
            if (exiftool_path == null || exiftool_path.isEmpty() || !exists) {
                res = Utils.getExiftoolPath();
            } else {
                res = exiftool_path;
                //String[] cmdparams = {res, "-ver"};
                cmdparams.add(res);
                cmdparams.add("-ver");
                try {
                    OutputLabel.setText("Exiftool available;  Version: " + CommandRunner.runCommand(cmdparams));
                } catch (IOException | InterruptedException ex) {
                    logger.debug("Error executing command");
                }

            }
        } else { // does not exist
            res = Utils.getExiftoolPath();
        }

        if (res != null && !res.isEmpty() && !res.toLowerCase().startsWith("info")) {
            exiftool_found = true;
            // We already checked that the node did not exist and that it is empty or null
            // remove all possible line breaks
            res = res.replace("\n", "").replace("\r", "");
            if (!exiftool_exists) {
                prefs.storeByKey(EXIFTOOL_PATH, res);
            }
        }

        return exiftool_found;
    }
/////////////////////////// End of Startup checks //////////////////////////

    private void loadImages() {
        OutputLabel.setText("Loading images ....");
        files = StandardFileIO.getFileNames(mainScreen.this.rootPanel);
        if (files != null) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Utils.displayFiles(mainScreen.this.tableListfiles, mainScreen.this.ListexiftoolInfotable, mainScreen.this.iconLabel);
                    MyVariables.setSelectedRow(0);
                    String[] params = whichRBselected();
                    Utils.getImageInfoFromSelectedFile(params, files, mainScreen.this.ListexiftoolInfotable);
                    mainScreen.this.buttonShowImage.setEnabled(true);
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
        }
    }


    private static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            //logger.info(comp.toString());
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    private void setCopyMetaDatacheckboxes(boolean state) {
        CopyExifcheckBox.setEnabled(state);
        CopyXmpcheckBox.setEnabled(state);
        CopyIptccheckBox.setEnabled(state);
        CopyIcc_profileDataCheckBox.setEnabled(state);
        CopyGpsCheckBox.setEnabled(state);
        CopyJfifcheckBox.setEnabled(state);
    }

    private void fillViewTagNamesComboboxes() {
        // Fill all combo boxes in the View panel
        String TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/ExifToolTagNames.txt");
        String[] Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewByTagName.setModel(new DefaultComboBoxModel(Tags));

        TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CommonTags.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewCommonTags.setModel(new DefaultComboBoxModel(Tags));

        TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CameraTagNames.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewCameraMake.setModel(new DefaultComboBoxModel(Tags));

    }

    // region IntelliJ GUI Code Generated

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.setMinimumSize(new Dimension(1200, 800));
        rootPanel.setPreferredSize(new Dimension(1350, 800));
        rootPanel.setRequestFocusEnabled(true);
        LeftPanel = new JPanel();
        LeftPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(LeftPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(320, -1), new Dimension(400, -1), null, 2, false));
        LeftbuttonBar = new JPanel();
        LeftbuttonBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        LeftPanel.add(LeftbuttonBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoadImages = new JButton();
        buttonLoadImages.setText("Load Images");
        buttonLoadImages.setToolTipText("Load images for which you want to view or edit metadata");
        LeftbuttonBar.add(buttonLoadImages);
        final Spacer spacer1 = new Spacer();
        LeftbuttonBar.add(spacer1);
        final Spacer spacer2 = new Spacer();
        LeftbuttonBar.add(spacer2);
        buttonShowImage = new JButton();
        buttonShowImage.setEnabled(false);
        buttonShowImage.setText("Display Image");
        buttonShowImage.setToolTipText("Display the selected image in the default image viewer (if supported movies will play in the default movie player)");
        buttonShowImage.putClientProperty("hideActionText", Boolean.FALSE);
        LeftbuttonBar.add(buttonShowImage);
        Leftscrollpane = new JScrollPane();
        LeftPanel.add(Leftscrollpane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableListfiles = new JTable();
        tableListfiles.setAutoResizeMode(4);
        tableListfiles.setPreferredScrollableViewportSize(new Dimension(400, 400));
        tableListfiles.setShowHorizontalLines(true);
        tableListfiles.setShowVerticalLines(false);
        tableListfiles.setToolTipText("Double-clicking the thumbnail or filename will open the image in the default viewer");
        Leftscrollpane.setViewportView(tableListfiles);
        tabbedPaneRight = new JTabbedPane();
        rootPanel.add(tabbedPaneRight, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(600, -1), new Dimension(700, -1), null, 6, false));
        ViewDatapanel = new JPanel();
        ViewDatapanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        ViewDatapanel.setMinimumSize(new Dimension(500, 90));
        ViewDatapanel.setPreferredSize(new Dimension(600, 490));
        tabbedPaneRight.addTab("View Data", ViewDatapanel);
        ViewRadiobuttonpanel = new JPanel();
        ViewRadiobuttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        ViewDatapanel.add(ViewRadiobuttonpanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        radioButtonViewAll = new JRadioButton();
        radioButtonViewAll.setSelected(true);
        radioButtonViewAll.setText("All");
        ViewRadiobuttonpanel.add(radioButtonViewAll);
        radioButtoncommonTags = new JRadioButton();
        radioButtoncommonTags.setText("Common Tags");
        ViewRadiobuttonpanel.add(radioButtoncommonTags);
        comboBoxViewCommonTags = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewCommonTags);
        radioButtonByTagName = new JRadioButton();
        radioButtonByTagName.setText("All Tag Names:");
        ViewRadiobuttonpanel.add(radioButtonByTagName);
        comboBoxViewByTagName = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewByTagName);
        radioButtonCameraMakes = new JRadioButton();
        radioButtonCameraMakes.setText("Camera make:");
        ViewRadiobuttonpanel.add(radioButtonCameraMakes);
        comboBoxViewCameraMake = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewCameraMake);
        ViewDatascrollpanel = new JScrollPane();
        ViewDatapanel.add(ViewDatascrollpanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ListexiftoolInfotable = new JTable();
        ListexiftoolInfotable.setAutoResizeMode(0);
        ViewDatascrollpanel.setViewportView(ListexiftoolInfotable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneRight.addTab("Edit Data", panel1);
        tabbedPaneEditfunctions = new JTabbedPane();
        panel1.add(tabbedPaneEditfunctions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        ExifEditpanel = new JPanel();
        ExifEditpanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 20, 0, 20), -1, -1));
        tabbedPaneEditfunctions.addTab("Exif", ExifEditpanel);
        Camera_Equipment = new JPanel();
        Camera_Equipment.setLayout(new GridLayoutManager(3, 3, new Insets(5, 0, 5, 0), -1, -1));
        ExifEditpanel.add(Camera_Equipment, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setPreferredSize(new Dimension(150, 18));
        label1.setText("Make:");
        Camera_Equipment.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifMaketextField = new JTextField();
        ExifMaketextField.setPreferredSize(new Dimension(500, 25));
        Camera_Equipment.add(ExifMaketextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifMakecheckBox = new JCheckBox();
        ExifMakecheckBox.setSelected(true);
        ExifMakecheckBox.setText("");
        Camera_Equipment.add(ExifMakecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Save");
        Camera_Equipment.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setPreferredSize(new Dimension(650, 18));
        label3.setText("Camera/Equipment");
        Camera_Equipment.add(label3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setPreferredSize(new Dimension(150, 18));
        label4.setText("Model:");
        Camera_Equipment.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModeltextField = new JTextField();
        ExifModeltextField.setPreferredSize(new Dimension(500, 25));
        Camera_Equipment.add(ExifModeltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModelcheckBox = new JCheckBox();
        ExifModelcheckBox.setSelected(true);
        ExifModelcheckBox.setText("");
        Camera_Equipment.add(ExifModelcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DateTime = new JPanel();
        DateTime.setLayout(new GridLayoutManager(4, 3, new Insets(5, 0, 5, 0), -1, -1));
        ExifEditpanel.add(DateTime, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setPreferredSize(new Dimension(150, 18));
        label5.setText("Date and Time");
        DateTime.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setPreferredSize(new Dimension(500, 18));
        label6.setText("YYYY:MM:DD hh:mm:ss (24Hrs); no checking done");
        DateTime.add(label6, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setPreferredSize(new Dimension(150, 18));
        label7.setText("ModifyDate:");
        DateTime.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModifyDatetextField = new JTextField();
        ExifModifyDatetextField.setPreferredSize(new Dimension(500, 25));
        DateTime.add(ExifModifyDatetextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModifyDatecheckBox = new JCheckBox();
        ExifModifyDatecheckBox.setSelected(true);
        ExifModifyDatecheckBox.setText("");
        DateTime.add(ExifModifyDatecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setPreferredSize(new Dimension(150, 18));
        label8.setText("DateTimeOriginal:");
        DateTime.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDateTimeOriginaltextField = new JTextField();
        ExifDateTimeOriginaltextField.setPreferredSize(new Dimension(500, 25));
        DateTime.add(ExifDateTimeOriginaltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDateTimeOriginalcheckBox = new JCheckBox();
        ExifDateTimeOriginalcheckBox.setSelected(true);
        ExifDateTimeOriginalcheckBox.setText("");
        DateTime.add(ExifDateTimeOriginalcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setPreferredSize(new Dimension(150, 18));
        label9.setText("CreateDate:");
        DateTime.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCreateDatetextField = new JTextField();
        ExifCreateDatetextField.setPreferredSize(new Dimension(500, 25));
        DateTime.add(ExifCreateDatetextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCreateDatecheckBox = new JCheckBox();
        ExifCreateDatecheckBox.setSelected(true);
        ExifCreateDatecheckBox.setText("");
        DateTime.add(ExifCreateDatecheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        CreativeTags = new JPanel();
        CreativeTags.setLayout(new GridLayoutManager(5, 3, new Insets(5, 0, 5, 0), -1, -1));
        ExifEditpanel.add(CreativeTags, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setPreferredSize(new Dimension(150, 18));
        label10.setText("Artist / Creator:");
        CreativeTags.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifArtistCreatortextField = new JTextField();
        ExifArtistCreatortextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifArtistCreatortextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifArtistCreatorcheckBox = new JCheckBox();
        ExifArtistCreatorcheckBox.setSelected(true);
        ExifArtistCreatorcheckBox.setText("");
        CreativeTags.add(ExifArtistCreatorcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, -1, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Creative Tags");
        CreativeTags.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setMaximumSize(new Dimension(250, 18));
        label12.setPreferredSize(new Dimension(150, 18));
        label12.setText("Copyright:");
        CreativeTags.add(label12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCopyrighttextField = new JTextField();
        ExifCopyrighttextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifCopyrighttextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCopyrightcheckBox = new JCheckBox();
        ExifCopyrightcheckBox.setSelected(true);
        ExifCopyrightcheckBox.setText("");
        CreativeTags.add(ExifCopyrightcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setPreferredSize(new Dimension(150, 18));
        label13.setText("User comment:");
        CreativeTags.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifUsercommenttextField = new JTextField();
        ExifUsercommenttextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifUsercommenttextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifUsercommentcheckBox = new JCheckBox();
        ExifUsercommentcheckBox.setSelected(true);
        ExifUsercommentcheckBox.setText("");
        CreativeTags.add(ExifUsercommentcheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setPreferredSize(new Dimension(150, 18));
        label14.setText("Description:");
        CreativeTags.add(label14, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDescriptiontextArea = new JTextArea();
        ExifDescriptiontextArea.setPreferredSize(new Dimension(500, 80));
        CreativeTags.add(ExifDescriptiontextArea, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDescriptioncheckBox = new JCheckBox();
        ExifDescriptioncheckBox.setSelected(true);
        ExifDescriptioncheckBox.setText("");
        CreativeTags.add(ExifDescriptioncheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        ExifEditpanel.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifcopyFromButton = new JButton();
        ExifcopyFromButton.setText("Copy from selected image");
        panel2.add(ExifcopyFromButton);
        ExifsaveToButton = new JButton();
        ExifsaveToButton.setText("Save to selected image(s)");
        panel2.add(ExifsaveToButton);
        ExifBackupOriginalscheckBox = new JCheckBox();
        ExifBackupOriginalscheckBox.setText("Make backup of originals");
        panel2.add(ExifBackupOriginalscheckBox);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        ExifEditpanel.add(panel3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifcopyDefaultsButton = new JButton();
        ExifcopyDefaultsButton.setText("Copy Defaults");
        panel3.add(ExifcopyDefaultsButton);
        resetFieldsButton = new JButton();
        resetFieldsButton.setText("Reset fields");
        panel3.add(resetFieldsButton);
        ExifhelpButton = new JButton();
        ExifhelpButton.setText("Help");
        panel3.add(ExifhelpButton);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(14, 3, new Insets(10, 20, 10, 20), -1, -1));
        tabbedPaneEditfunctions.addTab("xmp", panel4);
        final JLabel label15 = new JLabel();
        label15.setPreferredSize(new Dimension(150, 18));
        label15.setText("Creator*:");
        panel4.add(label15, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCreatortextField = new JTextField();
        xmpCreatortextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpCreatortextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCreatorcheckBox = new JCheckBox();
        xmpCreatorcheckBox.setSelected(true);
        xmpCreatorcheckBox.setText("");
        panel4.add(xmpCreatorcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setPreferredSize(new Dimension(150, 18));
        label16.setText("Rights*:");
        panel4.add(label16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRightstextField = new JTextField();
        xmpRightstextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpRightstextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRightscheckBox = new JCheckBox();
        xmpRightscheckBox.setSelected(true);
        xmpRightscheckBox.setText("");
        panel4.add(xmpRightscheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setPreferredSize(new Dimension(150, 18));
        label17.setText("Description:");
        panel4.add(label17, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpDescriptiontextArea = new JTextArea();
        xmpDescriptiontextArea.setPreferredSize(new Dimension(500, 80));
        panel4.add(xmpDescriptiontextArea, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpDescriptioncheckBox = new JCheckBox();
        xmpDescriptioncheckBox.setSelected(true);
        xmpDescriptioncheckBox.setText("");
        panel4.add(xmpDescriptioncheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, -1, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setText("Save");
        panel4.add(label18, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setPreferredSize(new Dimension(150, 18));
        label19.setText("Label:");
        panel4.add(label19, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpLabeltextField = new JTextField();
        xmpLabeltextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpLabeltextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpLabelcheckBox = new JCheckBox();
        xmpLabelcheckBox.setSelected(true);
        xmpLabelcheckBox.setText("");
        panel4.add(xmpLabelcheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setPreferredSize(new Dimension(150, 18));
        label20.setText("Subject:");
        panel4.add(label20, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpSubjecttextField = new JTextField();
        xmpSubjecttextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpSubjecttextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpSubjectcheckBox = new JCheckBox();
        xmpSubjectcheckBox.setSelected(true);
        xmpSubjectcheckBox.setText("");
        panel4.add(xmpSubjectcheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setPreferredSize(new Dimension(150, 18));
        label21.setText("Title:");
        panel4.add(label21, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpTitletextField = new JTextField();
        xmpTitletextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpTitletextField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpTitlecheckBox = new JCheckBox();
        xmpTitlecheckBox.setSelected(true);
        xmpTitlecheckBox.setText("");
        panel4.add(xmpTitlecheckBox, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setPreferredSize(new Dimension(150, 18));
        label22.setText("Person (in image):");
        panel4.add(label22, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpPersontextField = new JTextField();
        xmpPersontextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpPersontextField, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpPersoncheckBox = new JCheckBox();
        xmpPersoncheckBox.setSelected(true);
        xmpPersoncheckBox.setText("");
        panel4.add(xmpPersoncheckBox, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setPreferredSize(new Dimension(150, 18));
        label23.setText("Region Name:");
        panel4.add(label23, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRegionNametextField = new JTextField();
        xmpRegionNametextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpRegionNametextField, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRegionNamecheckBox = new JCheckBox();
        xmpRegionNamecheckBox.setSelected(true);
        xmpRegionNamecheckBox.setText("");
        panel4.add(xmpRegionNamecheckBox, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setPreferredSize(new Dimension(150, 18));
        label24.setText("Region Type:");
        panel4.add(label24, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRegionTypetextField = new JTextField();
        xmpRegionTypetextField.setPreferredSize(new Dimension(500, 25));
        panel4.add(xmpRegionTypetextField, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRegionTypecheckBox = new JCheckBox();
        xmpRegionTypecheckBox.setSelected(true);
        xmpRegionTypecheckBox.setText("");
        panel4.add(xmpRegionTypecheckBox, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setPreferredSize(new Dimension(150, 18));
        label25.setText("Rating:");
        panel4.add(label25, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setPreferredSize(new Dimension(500, 24));
        panel4.add(panel5, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmp1starradioButton = new JRadioButton();
        xmp1starradioButton.setLabel("*");
        xmp1starradioButton.setText("*");
        panel5.add(xmp1starradioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmp2starradioButton = new JRadioButton();
        xmp2starradioButton.setLabel("**");
        xmp2starradioButton.setText("**");
        panel5.add(xmp2starradioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmp3starradioButton = new JRadioButton();
        xmp3starradioButton.setSelected(true);
        xmp3starradioButton.setText("***");
        panel5.add(xmp3starradioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmp4starradioButton = new JRadioButton();
        xmp4starradioButton.setText("****");
        panel5.add(xmp4starradioButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmp5starradioButton = new JRadioButton();
        xmp5starradioButton.setLabel("*****");
        xmp5starradioButton.setText("*****");
        panel5.add(xmp5starradioButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmpRatingcheckBox = new JCheckBox();
        xmpRatingcheckBox.setSelected(true);
        xmpRatingcheckBox.setText("");
        panel4.add(xmpRatingcheckBox, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        panel4.add(panel6, new GridConstraints(12, 0, 1, 3, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCopyFrombutton = new JButton();
        xmpCopyFrombutton.setText("Copy from selected image");
        panel6.add(xmpCopyFrombutton);
        xmpSaveTobutton = new JButton();
        xmpSaveTobutton.setText("Save to selected image(s)");
        panel6.add(xmpSaveTobutton);
        xmpBackupOriginalscheckBox = new JCheckBox();
        xmpBackupOriginalscheckBox.setText("Make backup of originals");
        panel6.add(xmpBackupOriginalscheckBox);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        panel4.add(panel7, new GridConstraints(13, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCopyDefaultsbutton = new JButton();
        xmpCopyDefaultsbutton.setText("Copy Defaults");
        panel7.add(xmpCopyDefaultsbutton);
        xmpResetFieldsbutton = new JButton();
        xmpResetFieldsbutton.setText("Reset fields");
        panel7.add(xmpResetFieldsbutton);
        xmpHelpbutton = new JButton();
        xmpHelpbutton.setText("Help");
        panel7.add(xmpHelpbutton);
        final JLabel label26 = new JLabel();
        label26.setText("Credit Line*:");
        panel4.add(label26, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 18), null, 0, false));
        xmpCredittextField = new JTextField();
        panel4.add(xmpCredittextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(500, 25), null, 0, false));
        xmpCreditcheckBox = new JCheckBox();
        xmpCreditcheckBox.setSelected(true);
        xmpCreditcheckBox.setText("");
        panel4.add(xmpCreditcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpGoogleText = new JLabel();
        xmpGoogleText.setText("xmpGoogleText");
        panel4.add(xmpGoogleText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(500, 18), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 10), -1, -1));
        tabbedPaneEditfunctions.addTab("gps", panel8);
        gpsLocationPanel = new JPanel();
        gpsLocationPanel.setLayout(new GridLayoutManager(5, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel8.add(gpsLocationPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpsLocationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, -1, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setText("XMP/IPTC location");
        gpsLocationPanel.add(label27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        label28.setText("(Where is/are the photo(s) taken)");
        gpsLocationPanel.add(label28, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        Font label29Font = this.$$$getFont$$$(null, Font.BOLD, -1, label29.getFont());
        if (label29Font != null) label29.setFont(label29Font);
        label29.setText("Save");
        gpsLocationPanel.add(label29, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        label30.setPreferredSize(new Dimension(75, 18));
        label30.setText("Location");
        gpsLocationPanel.add(label30, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsLocationtextField = new JTextField();
        gpsLocationtextField.setPreferredSize(new Dimension(300, 30));
        gpsLocationPanel.add(gpsLocationtextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        gpsLocationcheckBox = new JCheckBox();
        gpsLocationcheckBox.setSelected(true);
        gpsLocationcheckBox.setText("");
        gpsLocationPanel.add(gpsLocationcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        label31.setText("Country");
        gpsLocationPanel.add(label31, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCountrytextField = new JTextField();
        gpsLocationPanel.add(gpsCountrytextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        gpsCountrycheckBox = new JCheckBox();
        gpsCountrycheckBox.setSelected(true);
        gpsCountrycheckBox.setText("");
        gpsLocationPanel.add(gpsCountrycheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label32 = new JLabel();
        label32.setText("State / Province");
        gpsLocationPanel.add(label32, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsStateProvincetextField = new JTextField();
        gpsLocationPanel.add(gpsStateProvincetextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        gpsStateProvincecheckBox = new JCheckBox();
        gpsStateProvincecheckBox.setSelected(true);
        gpsStateProvincecheckBox.setText("");
        gpsLocationPanel.add(gpsStateProvincecheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label33 = new JLabel();
        label33.setText("City");
        gpsLocationPanel.add(label33, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCitytextField = new JTextField();
        gpsLocationPanel.add(gpsCitytextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        gpsCitycheckBox = new JCheckBox();
        gpsCitycheckBox.setSelected(true);
        gpsCitycheckBox.setText("");
        gpsLocationPanel.add(gpsCitycheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsButtonPanel = new JPanel();
        gpsButtonPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(gpsButtonPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsCopyFrombutton = new JButton();
        gpsCopyFrombutton.setText("Copy from selected image");
        gpsButtonPanel.add(gpsCopyFrombutton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsSaveTobutton = new JButton();
        gpsSaveTobutton.setText("Save to selected image(s)");
        gpsButtonPanel.add(gpsSaveTobutton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsBackupOriginalscheckBox = new JCheckBox();
        gpsBackupOriginalscheckBox.setText("Backup original images");
        gpsButtonPanel.add(gpsBackupOriginalscheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsResetFieldsbutton = new JButton();
        gpsResetFieldsbutton.setText("Reset Fields");
        gpsButtonPanel.add(gpsResetFieldsbutton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsMapcoordinatesbutton = new JButton();
        gpsMapcoordinatesbutton.setText("Open MapCoordinates.net");
        gpsButtonPanel.add(gpsMapcoordinatesbutton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsHelpbutton = new JButton();
        gpsHelpbutton.setText("Help");
        gpsButtonPanel.add(gpsHelpbutton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsLatLonAltPanel = new JPanel();
        gpsLatLonAltPanel.setLayout(new GridLayoutManager(5, 7, new Insets(5, 5, 5, 5), -1, -1));
        panel8.add(gpsLatLonAltPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpsLatLonAltPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        SaveLatLonAltcheckBox = new JCheckBox();
        SaveLatLonAltcheckBox.setSelected(true);
        SaveLatLonAltcheckBox.setText("Save Latitude/Longitude/Altitude");
        gpsLatLonAltPanel.add(SaveLatLonAltcheckBox, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label34 = new JLabel();
        label34.setText("GPS");
        gpsLatLonAltPanel.add(label34, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label35 = new JLabel();
        label35.setText("Decimal");
        gpsLatLonAltPanel.add(label35, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        label36.setText("deg");
        gpsLatLonAltPanel.add(label36, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label37 = new JLabel();
        label37.setText("min");
        gpsLatLonAltPanel.add(label37, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label38 = new JLabel();
        label38.setText("sec");
        gpsLatLonAltPanel.add(label38, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label39 = new JLabel();
        label39.setText("Latitude");
        gpsLatLonAltPanel.add(label39, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsLatDecimaltextField = new JTextField();
        gpsLatLonAltPanel.add(gpsLatDecimaltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 25), null, 0, false));
        gpsLatdegtextField = new JTextField();
        gpsLatdegtextField.setEditable(false);
        gpsLatLonAltPanel.add(gpsLatdegtextField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        gpsLatDegreestextField = new JTextField();
        gpsLatDegreestextField.setEditable(false);
        gpsLatDegreestextField.setFocusCycleRoot(false);
        gpsLatLonAltPanel.add(gpsLatDegreestextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        gpsLatMinutestextField = new JTextField();
        gpsLatMinutestextField.setEditable(false);
        gpsLatLonAltPanel.add(gpsLatMinutestextField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        final JLabel label40 = new JLabel();
        label40.setText("Altitude");
        gpsLatLonAltPanel.add(label40, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsNorthradioButton = new JRadioButton();
        gpsNorthradioButton.setEnabled(false);
        gpsNorthradioButton.setSelected(true);
        gpsNorthradioButton.setText("N");
        gpsLatLonAltPanel.add(gpsNorthradioButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsSouthRadioButton = new JRadioButton();
        gpsSouthRadioButton.setEnabled(false);
        gpsSouthRadioButton.setText("S");
        gpsLatLonAltPanel.add(gpsSouthRadioButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label41 = new JLabel();
        label41.setText("Longitude");
        gpsLatLonAltPanel.add(label41, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsLonDecimaltextField = new JTextField();
        gpsLatLonAltPanel.add(gpsLonDecimaltextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 25), null, 0, false));
        gpsLonDegreestextField = new JTextField();
        gpsLonDegreestextField.setEditable(false);
        gpsLatLonAltPanel.add(gpsLonDegreestextField, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        gpsLonMinutestextField = new JTextField();
        gpsLonMinutestextField.setEditable(false);
        gpsLatLonAltPanel.add(gpsLonMinutestextField, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        gpsLonSecondstextField = new JTextField();
        gpsLonSecondstextField.setEditable(false);
        gpsLatLonAltPanel.add(gpsLonSecondstextField, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        gpsEastradioButton = new JRadioButton();
        gpsEastradioButton.setEnabled(false);
        gpsEastradioButton.setSelected(true);
        gpsEastradioButton.setText("E");
        gpsLatLonAltPanel.add(gpsEastradioButton, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsWestradioButton = new JRadioButton();
        gpsWestradioButton.setEnabled(false);
        gpsWestradioButton.setText("W");
        gpsLatLonAltPanel.add(gpsWestradioButton, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsAltDecimaltextField = new JTextField();
        gpsLatLonAltPanel.add(gpsAltDecimaltextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 25), null, 0, false));
        gpsAboveSealevelcheckBox = new JCheckBox();
        gpsAboveSealevelcheckBox.setSelected(false);
        gpsAboveSealevelcheckBox.setText("Above sea level");
        gpsLatLonAltPanel.add(gpsAboveSealevelcheckBox, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCalculationPanel = new JPanel();
        gpsCalculationPanel.setLayout(new GridLayoutManager(4, 8, new Insets(5, 5, 5, 5), -1, -1));
        panel8.add(gpsCalculationPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpsCalculationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        gpsCalculatorLabelText = new JLabel();
        gpsCalculatorLabelText.setText("Label");
        gpsCalculationPanel.add(gpsCalculatorLabelText, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label42 = new JLabel();
        Font label42Font = this.$$$getFont$$$(null, Font.BOLD, -1, label42.getFont());
        if (label42Font != null) label42.setFont(label42Font);
        label42.setText("Calculator");
        gpsCalculationPanel.add(label42, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label43 = new JLabel();
        label43.setText("Decimal");
        gpsCalculationPanel.add(label43, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label44 = new JLabel();
        label44.setText("deg");
        gpsCalculationPanel.add(label44, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label45 = new JLabel();
        label45.setText("min");
        gpsCalculationPanel.add(label45, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label46 = new JLabel();
        label46.setText("sec");
        gpsCalculationPanel.add(label46, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label47 = new JLabel();
        label47.setText("Latitude");
        gpsCalculationPanel.add(label47, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcLatDecimaltextField = new JTextField();
        gpsCalculationPanel.add(CalcLatDecimaltextField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        CalcLatDegtextField = new JTextField();
        gpsCalculationPanel.add(CalcLatDegtextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        CalcLatMintextField = new JTextField();
        gpsCalculationPanel.add(CalcLatMintextField, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        CalcLatSectextField = new JTextField();
        gpsCalculationPanel.add(CalcLatSectextField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        final JLabel label48 = new JLabel();
        label48.setText("Longitude");
        gpsCalculationPanel.add(label48, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCalcLonDecimaltextField = new JTextField();
        gpsCalculationPanel.add(gpsCalcLonDecimaltextField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpsCalcLondegtextField = new JTextField();
        gpsCalculationPanel.add(gpsCalcLondegtextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        CalcLonMintextField = new JTextField();
        gpsCalculationPanel.add(CalcLonMintextField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        CalcLonSectextField = new JTextField();
        gpsCalculationPanel.add(CalcLonSectextField, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), null, 0, false));
        CalcNorthRadioButton = new JRadioButton();
        CalcNorthRadioButton.setSelected(true);
        CalcNorthRadioButton.setText("N");
        gpsCalculationPanel.add(CalcNorthRadioButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcSouthRadioButton = new JRadioButton();
        CalcSouthRadioButton.setText("S");
        gpsCalculationPanel.add(CalcSouthRadioButton, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcEastradioButton = new JRadioButton();
        CalcEastradioButton.setSelected(true);
        CalcEastradioButton.setText("E");
        gpsCalculationPanel.add(CalcEastradioButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcWestRadioButton = new JRadioButton();
        CalcWestRadioButton.setText("W");
        gpsCalculationPanel.add(CalcWestRadioButton, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decimalToMinutesSecondsButton = new JButton();
        decimalToMinutesSecondsButton.setText("Decimal to minutes-seconds =>");
        gpsCalculationPanel.add(decimalToMinutesSecondsButton, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minutesSecondsToDecimalButton = new JButton();
        minutesSecondsToDecimalButton.setText("<= Minutes-seconds to decimal");
        gpsCalculationPanel.add(minutesSecondsToDecimalButton, new GridConstraints(3, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyToInputFieldsButton = new JButton();
        copyToInputFieldsButton.setText("Copy to input fields");
        gpsCalculationPanel.add(copyToInputFieldsButton, new GridConstraints(3, 5, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GeotaggingEditpanel = new JPanel();
        GeotaggingEditpanel.setLayout(new GridLayoutManager(8, 1, new Insets(10, 20, 10, 20), -1, -1));
        tabbedPaneEditfunctions.addTab("Geotagging", GeotaggingEditpanel);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 20, 0), -1, -1));
        GeotaggingEditpanel.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label49 = new JLabel();
        Font label49Font = this.$$$getFont$$$(null, Font.BOLD, -1, label49.getFont());
        if (label49Font != null) label49.setFont(label49Font);
        label49.setText("Folder containing the images");
        panel9.add(label49, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel9.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingImgFoldertextField = new JTextField();
        geotaggingImgFoldertextField.setPreferredSize(new Dimension(500, 25));
        panel10.add(geotaggingImgFoldertextField);
        geotaggingImgFolderbutton = new JButton();
        geotaggingImgFolderbutton.setText("Browse");
        panel10.add(geotaggingImgFolderbutton);
        GeotaggingLeaveFolderEmptyLabel = new JLabel();
        GeotaggingLeaveFolderEmptyLabel.setText("GeotaggingLeaveFolderEmpty");
        panel9.add(GeotaggingLeaveFolderEmptyLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(650, -1), null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        GeotaggingEditpanel.add(panel11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label50 = new JLabel();
        Font label50Font = this.$$$getFont$$$(null, Font.BOLD, -1, label50.getFont());
        if (label50Font != null) label50.setFont(label50Font);
        label50.setText("GPS log file:");
        panel11.add(label50, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel11.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingGPSLogtextField = new JTextField();
        geotaggingGPSLogtextField.setPreferredSize(new Dimension(500, 25));
        panel12.add(geotaggingGPSLogtextField);
        geotaggingGPSLogbutton = new JButton();
        geotaggingGPSLogbutton.setText("Browse");
        panel12.add(geotaggingGPSLogbutton);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        GeotaggingEditpanel.add(panel13, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label51 = new JLabel();
        Font label51Font = this.$$$getFont$$$(null, Font.BOLD, -1, label51.getFont());
        if (label51Font != null) label51.setFont(label51Font);
        label51.setText("Geosync time:");
        panel13.add(label51, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel13.add(panel14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingGeosynctextField = new JTextField();
        geotaggingGeosynctextField.setPreferredSize(new Dimension(500, 25));
        geotaggingGeosynctextField.setText("0000:00:00 00:00:00");
        panel14.add(geotaggingGeosynctextField);
        final JLabel label52 = new JLabel();
        label52.setText("YYYY:MM:DD hh:mm:ss");
        panel14.add(label52);
        GeotaggingGeosyncExplainLabel = new JLabel();
        GeotaggingGeosyncExplainLabel.setText("geotaggingGeosyncExplainLabel");
        panel13.add(GeotaggingGeosyncExplainLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new FlowLayout(FlowLayout.RIGHT, 25, 5));
        GeotaggingEditpanel.add(panel15, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingWriteInfobutton = new JButton();
        geotaggingWriteInfobutton.setText("Write geotag information to image(s)");
        panel15.add(geotaggingWriteInfobutton);
        resetGeotaggingbutton = new JButton();
        resetGeotaggingbutton.setText("Reset fields");
        panel15.add(resetGeotaggingbutton);
        geotaggingHelpbutton = new JButton();
        geotaggingHelpbutton.setText("Help");
        panel15.add(geotaggingHelpbutton);
        geotaggingOverwriteOriginalscheckBox = new JCheckBox();
        geotaggingOverwriteOriginalscheckBox.setSelected(true);
        geotaggingOverwriteOriginalscheckBox.setText("Overwrite Originals");
        GeotaggingEditpanel.add(geotaggingOverwriteOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(6, 3, new Insets(5, 5, 5, 5), -1, -1));
        GeotaggingEditpanel.add(panel16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel16.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JLabel label53 = new JLabel();
        Font label53Font = this.$$$getFont$$$(null, Font.BOLD, -1, label53.getFont());
        if (label53Font != null) label53.setFont(label53Font);
        label53.setText("XMP/IPTC location");
        panel16.add(label53, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label54 = new JLabel();
        label54.setText("(Where is/are the photo(s) taken)");
        panel16.add(label54, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label55 = new JLabel();
        Font label55Font = this.$$$getFont$$$(null, Font.BOLD, -1, label55.getFont());
        if (label55Font != null) label55.setFont(label55Font);
        label55.setText("Save");
        panel16.add(label55, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label56 = new JLabel();
        label56.setPreferredSize(new Dimension(75, 18));
        label56.setText("Location");
        panel16.add(label56, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingLocationtextfield = new JTextField();
        geotaggingLocationtextfield.setPreferredSize(new Dimension(300, 30));
        panel16.add(geotaggingLocationtextfield, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        geotaggingLocationcheckbox = new JCheckBox();
        geotaggingLocationcheckbox.setSelected(false);
        geotaggingLocationcheckbox.setText("");
        panel16.add(geotaggingLocationcheckbox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label57 = new JLabel();
        label57.setText("Country");
        panel16.add(label57, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingCountrytextfield = new JTextField();
        panel16.add(geotaggingCountrytextfield, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        geotaggingCountrycheckbox = new JCheckBox();
        geotaggingCountrycheckbox.setSelected(false);
        geotaggingCountrycheckbox.setText("");
        panel16.add(geotaggingCountrycheckbox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label58 = new JLabel();
        label58.setText("State / Province");
        panel16.add(label58, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingStatetextfield = new JTextField();
        panel16.add(geotaggingStatetextfield, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        geotaggingStatecheckbox = new JCheckBox();
        geotaggingStatecheckbox.setSelected(false);
        geotaggingStatecheckbox.setText("");
        panel16.add(geotaggingStatecheckbox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label59 = new JLabel();
        label59.setText("City");
        panel16.add(label59, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingCitytextfield = new JTextField();
        panel16.add(geotaggingCitytextfield, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        geotaggingCitycheckbox = new JCheckBox();
        geotaggingCitycheckbox.setSelected(false);
        geotaggingCitycheckbox.setText("");
        panel16.add(geotaggingCitycheckbox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GeotaggingLocationLabel = new JLabel();
        GeotaggingLocationLabel.setText("GeotaggingLocationLabel");
        panel16.add(GeotaggingLocationLabel, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(6, 1, new Insets(10, 0, 0, 0), -1, -1));
        tabbedPaneEditfunctions.addTab("Gpano", panel17);
        gPanoTopText = new JLabel();
        gPanoTopText.setText("gPanoTopText");
        panel17.add(gPanoTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(5, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel17.add(panel18, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel18.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JLabel label60 = new JLabel();
        label60.setText("Cropped Area Image Height pixels *");
        panel18.add(label60, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, -1), null, 0, false));
        gpanoCAIHPtextField = new JFormattedTextField();
        gpanoCAIHPtextField.setColumns(0);
        gpanoCAIHPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoCAIHPtextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label61 = new JLabel();
        label61.setPreferredSize(new Dimension(200, 18));
        label61.setText("Cropped Area Image Width pixels *");
        panel18.add(label61, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCAIWPtextField = new JFormattedTextField();
        gpanoCAIWPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoCAIWPtextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label62 = new JLabel();
        label62.setPreferredSize(new Dimension(200, 18));
        label62.setText("Cropped Area Left Pixels *");
        panel18.add(label62, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCALPtextField = new JFormattedTextField();
        gpanoCALPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoCALPtextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label63 = new JLabel();
        label63.setPreferredSize(new Dimension(200, 18));
        label63.setText("Cropped Area Top Pixels *");
        panel18.add(label63, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCATPtextField = new JFormattedTextField();
        gpanoCATPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoCATPtextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label64 = new JLabel();
        label64.setPreferredSize(new Dimension(200, 18));
        label64.setText("Full Pano Height Pixels *");
        panel18.add(label64, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoFPHPtextField = new JFormattedTextField();
        gpanoFPHPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoFPHPtextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label65 = new JLabel();
        label65.setPreferredSize(new Dimension(200, 18));
        label65.setText("Full Pano Width Pixels *");
        panel18.add(label65, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoFPWPtextField = new JFormattedTextField();
        gpanoFPWPtextField.setPreferredSize(new Dimension(75, 30));
        panel18.add(gpanoFPWPtextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label66 = new JLabel();
        label66.setText("Projection Type *");
        panel18.add(label66, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoPTcomboBox = new JComboBox();
        panel18.add(gpanoPTcomboBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label67 = new JLabel();
        label67.setText("Use panorama viewer *");
        panel18.add(label67, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox1 = new JCheckBox();
        checkBox1.setEnabled(false);
        checkBox1.setSelected(true);
        checkBox1.setText("");
        panel18.add(checkBox1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(6, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel17.add(panel19, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel19.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JLabel label68 = new JLabel();
        label68.setText("Initial View Heading Degrees ***");
        panel19.add(label68, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        gpanoIVHDtextField = new JFormattedTextField();
        panel19.add(gpanoIVHDtextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVHDCheckBox = new JCheckBox();
        gpanoIVHDCheckBox.setText("Save");
        panel19.add(gpanoIVHDCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label69 = new JLabel();
        label69.setText("Initial View Pitch Degrees ***");
        panel19.add(label69, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIVPDtextField = new JFormattedTextField();
        panel19.add(gpanoIVPDtextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVPDCheckBox = new JCheckBox();
        gpanoIVPDCheckBox.setText("Save");
        panel19.add(gpanoIVPDCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label70 = new JLabel();
        label70.setText("Initial View Roll Degrees ***");
        panel19.add(label70, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIVRDtextField = new JFormattedTextField();
        panel19.add(gpanoIVRDtextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVRDCheckBox = new JCheckBox();
        gpanoIVRDCheckBox.setText("Save");
        panel19.add(gpanoIVRDCheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label71 = new JLabel();
        label71.setText("Initial Horizontal FOV Degrees ***");
        panel19.add(label71, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIHFOVDtextField = new JFormattedTextField();
        panel19.add(gpanoIHFOVDtextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIHFOVDtextFieldCheckBox = new JCheckBox();
        gpanoIHFOVDtextFieldCheckBox.setText("Save");
        panel19.add(gpanoIHFOVDtextFieldCheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label72 = new JLabel();
        label72.setPreferredSize(new Dimension(200, 18));
        label72.setText("Stitching Software ***");
        panel19.add(label72, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, -1), null, 0, false));
        gpanoStitchingSoftwaretextField = new JTextField();
        gpanoStitchingSoftwaretextField.setPreferredSize(new Dimension(450, 25));
        panel19.add(gpanoStitchingSoftwaretextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(400, 25), null, 0, false));
        gpanoStitchingSoftwarecheckBox = new JCheckBox();
        gpanoStitchingSoftwarecheckBox.setText("Save");
        panel19.add(gpanoStitchingSoftwarecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label73 = new JLabel();
        label73.setText("Pose Heading Degrees **");
        panel19.add(label73, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoPHDtextField = new JFormattedTextField();
        panel19.add(gpanoPHDtextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoPHDcheckBox = new JCheckBox();
        gpanoPHDcheckBox.setText("Save");
        panel19.add(gpanoPHDcheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        panel17.add(panel20, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCopyFrombutton = new JButton();
        gpanoCopyFrombutton.setText("Copy from selected image");
        panel20.add(gpanoCopyFrombutton);
        gpanoCopyTobutton = new JButton();
        gpanoCopyTobutton.setText("Save to selected image(s)");
        panel20.add(gpanoCopyTobutton);
        gpanoResetFieldsbutton = new JButton();
        gpanoResetFieldsbutton.setText("Reset Fields");
        panel20.add(gpanoResetFieldsbutton);
        gpanoHelpbutton = new JButton();
        gpanoHelpbutton.setLabel("Help");
        gpanoHelpbutton.setText("Help");
        panel20.add(gpanoHelpbutton);
        gpanoOverwriteOriginalscheckBox = new JCheckBox();
        gpanoOverwriteOriginalscheckBox.setText("Make backup of originals");
        panel17.add(gpanoOverwriteOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpanoMinVersionText = new JLabel();
        gpanoMinVersionText.setText("gpanoMinVersionText");
        panel17.add(gpanoMinVersionText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneEditfunctions.addTab("IPTC", panel21);
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(6, 1, new Insets(10, 10, 5, 5), -1, -1));
        tabbedPaneRight.addTab("Copy Data", panel22);
        copyAllMetadataRadiobutton = new JRadioButton();
        copyAllMetadataRadiobutton.setText("<html>Copy the values of all writable tags from the source image to the target image(s), writing the information to same-named tags in the preferred groups</html>");
        panel22.add(copyAllMetadataRadiobutton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyAllMetadataSameGroupsRadiobutton = new JRadioButton();
        copyAllMetadataSameGroupsRadiobutton.setText("<html>Copy the values of all writable tags from the source image to the target image(s), preserving the original tag groups</html>");
        panel22.add(copyAllMetadataSameGroupsRadiobutton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copySelectiveMetadataradioButton = new JRadioButton();
        copySelectiveMetadataradioButton.setSelected(true);
        copySelectiveMetadataradioButton.setText("Copy metadata using below mentioned selective group options");
        panel22.add(copySelectiveMetadataradioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(7, 1, new Insets(10, 0, 15, 0), -1, -1));
        panel22.add(panel23, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel23.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        CopyExifcheckBox = new JCheckBox();
        CopyExifcheckBox.setText("Copy exif data (-exif:all)");
        panel23.add(CopyExifcheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyXmpcheckBox = new JCheckBox();
        CopyXmpcheckBox.setText("Copy xmp data (-xmp:all)");
        panel23.add(CopyXmpcheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyIptccheckBox = new JCheckBox();
        CopyIptccheckBox.setText("Copy IPTC data (-iptc:all)");
        panel23.add(CopyIptccheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyIcc_profileDataCheckBox = new JCheckBox();
        CopyIcc_profileDataCheckBox.setActionCommand("");
        CopyIcc_profileDataCheckBox.setText("Copy ICC(_profile) data (-icc_profile:all)");
        panel23.add(CopyIcc_profileDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyGpsCheckBox = new JCheckBox();
        CopyGpsCheckBox.setText("Copy gps data (-gps:all)");
        panel23.add(CopyGpsCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyJfifcheckBox = new JCheckBox();
        CopyJfifcheckBox.setText("Copy JFIF data (from the jpeg JFIF header)");
        panel23.add(CopyJfifcheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyMakernotescheckBox = new JCheckBox();
        CopyMakernotescheckBox.setText("Copy makernotes (-makernotes:all)");
        panel23.add(CopyMakernotescheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BackupOriginalscheckBox = new JCheckBox();
        BackupOriginalscheckBox.setText("Make backup of originals");
        panel22.add(BackupOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel22.add(panel24, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UseDataFrombutton = new JButton();
        UseDataFrombutton.setText("Use data from selected image");
        panel24.add(UseDataFrombutton);
        CopyDataCopyTobutton = new JButton();
        CopyDataCopyTobutton.setText("Copy to selected image(s)");
        panel24.add(CopyDataCopyTobutton);
        CopyHelpbutton = new JButton();
        CopyHelpbutton.setText("Help");
        panel24.add(CopyHelpbutton);
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPaneRight.addTab("Your Commands", panel25);
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel25.add(panel26, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        CommandsParameterstextField = new JTextField();
        panel26.add(CommandsParameterstextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label74 = new JLabel();
        Font label74Font = this.$$$getFont$$$(null, Font.BOLD, -1, label74.getFont());
        if (label74Font != null) label74.setFont(label74Font);
        label74.setText("Parameters:");
        panel26.add(label74, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel27 = new JPanel();
        panel27.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel26.add(panel27, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        CommandsclearParameterSFieldButton = new JButton();
        CommandsclearParameterSFieldButton.setText("Clear parameter(s) field");
        panel27.add(CommandsclearParameterSFieldButton);
        CommandsclearOutputFieldButton = new JButton();
        CommandsclearOutputFieldButton.setText("Clear output field");
        panel27.add(CommandsclearOutputFieldButton);
        CommandsgoButton = new JButton();
        CommandsgoButton.setText("Go");
        panel27.add(CommandsgoButton);
        CommandshelpButton = new JButton();
        CommandshelpButton.setText("Help");
        panel27.add(CommandshelpButton);
        final JPanel panel28 = new JPanel();
        panel28.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel26.add(panel28, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label75 = new JLabel();
        label75.setText("The final output will be displayed below:");
        label75.setVerticalTextPosition(1);
        panel28.add(label75, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel28.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        YourCommandsOutputTextArea = new JTextArea();
        YourCommandsOutputTextArea.setText("");
        scrollPane1.setViewportView(YourCommandsOutputTextArea);
        final JPanel panel29 = new JPanel();
        panel29.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        panel28.add(panel29, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UseNonPropFontradioButton = new JRadioButton();
        UseNonPropFontradioButton.setSelected(true);
        UseNonPropFontradioButton.setText("Use a non-proportional \"monospaced\" font");
        panel29.add(UseNonPropFontradioButton);
        UsePropFontradioButton = new JRadioButton();
        UsePropFontradioButton.setText("Use a proportional font");
        panel29.add(UsePropFontradioButton);
        MyCommandsText = new JLabel();
        MyCommandsText.setText("MyCommandsText");
        panel25.add(MyCommandsText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel30 = new JPanel();
        panel30.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rootPanel.add(panel30, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        OutputLabel = new JLabel();
        OutputLabel.setText("");
        panel30.add(OutputLabel);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(100, 15));
        progressBar.setStringPainted(false);
        panel30.add(progressBar);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(xmp1starradioButton);
        buttonGroup.add(xmp2starradioButton);
        buttonGroup.add(xmp3starradioButton);
        buttonGroup.add(xmp4starradioButton);
        buttonGroup.add(xmp5starradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(gpsNorthradioButton);
        buttonGroup.add(gpsSouthRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(gpsEastradioButton);
        buttonGroup.add(gpsWestradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(CalcNorthRadioButton);
        buttonGroup.add(CalcSouthRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(CalcEastradioButton);
        buttonGroup.add(CalcWestRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(copyAllMetadataRadiobutton);
        buttonGroup.add(copyAllMetadataSameGroupsRadiobutton);
        buttonGroup.add(copySelectiveMetadataradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(UseNonPropFontradioButton);
        buttonGroup.add(UsePropFontradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonViewAll);
        buttonGroup.add(radioButtoncommonTags);
        buttonGroup.add(radioButtonByTagName);
        buttonGroup.add(radioButtonCameraMakes);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    // endregion

    // region Action Listeners and radio button groups
    class MenuActionListener implements ActionListener {

        // menuListener
        public void actionPerformed(ActionEvent ev) {
            String[] dummy = null;
            logger.info("Selected: {}", ev.getActionCommand());

            switch (ev.getActionCommand()) {
                case "Load Images":
                    // identical to button "Load Images"
                    loadImages();
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
                        metaData.copyToXmp();
                        OutputLabel.setText("");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Repair JPG(s) with corrupted metadata":
                    if (selectedIndicesList.size() > 0) {
                        OutputLabel.setText("Repairing jpg data, please be patient ...");
                        metaData.repairJPGMetadata( progressBar);
                        OutputLabel.setText("");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Export metadata":
                    if (selectedIndicesList.size() > 0) {
                        ExportMetadata expMetadata = new ExportMetadata();
                        expMetadata.showDialog(selectedIndices, files, progressBar);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Remove metadata":
                    if (selectedIndicesList.size() > 0) {
                        RemoveMetadata rmMetadata = new RemoveMetadata();
                        rmMetadata.showDialog(progressBar);
                        OutputLabel.setText("");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Shift Date/time":
                    if (selectedIndicesList.size() > 0) {
                        ShiftDateTime SDT = new ShiftDateTime();
                        SDT.showDialog(progressBar);
                        OutputLabel.setText("");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Modify Date/time":
                    if (selectedIndicesList.size() > 0) {
                        ModifyDateTime MDT = new ModifyDateTime();
                        MDT.showDialog(selectedIndices, files);
                        OutputLabel.setText("");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Set file date to DateTimeOriginal":
                    if (selectedIndicesList.size() > 0) {
                        dateTime.setFileDateTimeToDateTimeOriginal(progressBar);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "Create args file(s)":
                    if (selectedIndicesList.size() > 0) {
                        CreateArgsFile CAF = new CreateArgsFile();
                        CAF.showDialog(selectedIndices, files, progressBar);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "About jExifToolGUI":
                    JOptionPane.showMessageDialog(mainScreen.this.rootPanel, String.format(ProgramTexts.HTML, 450, ProgramTexts.aboutText), "About jExifToolGUI for ExifTool by Phil Harvey", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "About ExifTool":
                    JOptionPane.showMessageDialog(mainScreen.this.rootPanel, String.format(ProgramTexts.HTML, 450, ProgramTexts.aboutExifToolText), "About ExifTool by Phil Harvey", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "jExifToolGUI homepage":
                    Utils.openBrowser(ProgramTexts.ProjectWebSite);
                    break;
                case "ExifTool homepage":
                    Utils.openBrowser("https://www.sno.phy.queensu.ca/~phil/exiftool/");
                    break;
                case "License":
                    Utils.showLicense(mainScreen.this.rootPanel);
                    break;
                case "Check for new version":
                    Utils.checkForNewVersion("menu");
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
                loadImages();
            }
        });
        buttonShowImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.extdisplaySelectedImageInDefaultViewer();
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
                        YourCmnds.executeCommands(CommandsParameterstextField.getText(), YourCommandsOutputTextArea, UseNonPropFontradioButton, progressBar);
                        OutputLabel.setText("The output should be displayed above ...");
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, "No command parameters given", "No parameters", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        CommandshelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, HelpTexts.YourCommandsHelp), "Help for the Your Commands panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit Exif pane buttons
        ExifcopyFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EEd.copyExifFromSelected(getExifFields(), ExifDescriptiontextArea);
            }
        });
        ExifsaveToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    EEd.writeExifTags(getExifFields(), ExifDescriptiontextArea, getExifBoxes(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
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
                EEd.resetFields(getExifFields(), ExifDescriptiontextArea);
            }
        });
        ExifhelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, HelpTexts.ExifAndXmpHelp), "Help for the Exif edit panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Edit xmp buttons
        xmpCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EXd.copyXmpFromSelected(getXmpFields(), xmpDescriptiontextArea);
            }
        });
        xmpSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    EXd.writeXmpTags(getXmpFields(), xmpDescriptiontextArea, getXmpBoxes(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
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
                EXd.resetFields(getXmpFields(), xmpDescriptiontextArea);
            }
        });
        xmpHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, HelpTexts.ExifAndXmpHelp), "Help for the XMP edit panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Edit geotagging buttons
        geotaggingImgFolderbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ImgPath = EGd.getImagePath(rootPanel);
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
                    JOptionPane.showMessageDialog(rootPanel, "No images selected and no image folder path selected", "No images, no path", JOptionPane.WARNING_MESSAGE);
                } else {
                    if ("".equals(geotaggingGPSLogtextField.getText())) {
                        JOptionPane.showMessageDialog(rootPanel, "No gps track log selected", "No gps log", JOptionPane.WARNING_MESSAGE);
                    } else {
                        EGd.writeInfo(getGeotaggingFields(), getGeotaggingBoxes(), geotaggingOverwriteOriginalscheckBox.isSelected(), progressBar);
                    }
                }
                OutputLabel.setText("");
            }
        });
        resetGeotaggingbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGd.ResetFields(getGeotaggingFields(), getGeotaggingBoxes());
            }
        });
        geotaggingHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, HelpTexts.GeotaggingHelp), "Help for the Geotagging edit panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit gps buttons
        gpsCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGPSd.copyGPSFromSelected(getGPSFields(), getGpsBoxes());
            }
        });
        gpsSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    EGPSd.writeGPSTags(getGPSFields(), getGpsBoxes(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                }
            }

        });
        gpsResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGPSd.resetFields(getGPSFields());
            }
        });
        gpsMapcoordinatesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.openBrowser("https://www.mapcoordinates.net/en");
            }
        });
        gpsHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, HelpTexts.GPSHelp), "Help for the GPS-Location edit panel", JOptionPane.INFORMATION_MESSAGE);
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
                //metaData.copyMetaData(getCopyMetaDataRadiobuttons(), getCopyMetaDataCheckBoxes(), SelectedCopyFromImageIndex, selectedIndices, files, progressBar);
                metaData.copyMetaData(getCopyMetaDataRadiobuttons(), getCopyMetaDataCheckBoxes(), SelectedCopyFromImageIndex, progressBar);
            }
        });
        CopyHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, HelpTexts.CopyMetaDataHelp), "Help for the Copy metadata panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // The buttons from the Gpano edit tab
        gpanoCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGpanod.copyGpanoFromSelected(getGpanoFields(), gpanoStitchingSoftwaretextField, gpanoPTcomboBox, getGpanoCheckBoxes());
                JOptionPane.showMessageDialog(rootPanel, ProgramTexts.GpanoSetSaveCheckboxes, "Set Save checkboxes", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gpanoCopyTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedIndicesList.size() > 0) {
                    // Now we need to check on completeness of mandatory fields
                    boolean allFieldsFilled = EGpanod.checkFieldsOnNotBeingEmpty(getGpanoFields(), gpanoPTcomboBox);
                    if (allFieldsFilled) {
                        EGpanod.writeGpanoTags(getGpanoFields(), getGpanoCheckBoxes(), gpanoStitchingSoftwaretextField, gpanoPTcomboBox, progressBar);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NotAllMandatoryFields, "Not all manadatory fields complete", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ProgramTexts.NoImgSelected, "No images selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gpanoResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGpanod.resetFields(getGpanoFields(), gpanoStitchingSoftwaretextField, getGpanoCheckBoxes());
            }
        });
        gpanoHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, HelpTexts.GpanoHelp), "Help for the Edit Gpano metadata panel", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    private void ViewRadiobuttonListener() {

        //Add listeners
        radioButtonViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //logger.info("button selected: {}", radioButtonViewAll.getText());
                Utils.getImageInfoFromSelectedFile(MyConstants.ALL_PARAMS, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        radioButtoncommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] params = Utils.getWhichCommonTagSelected(comboBoxViewCommonTags);
                //Utils.selectImageInfoByTagName(comboBoxViewCommonTags, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                Utils.getImageInfoFromSelectedFile(params, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewCommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtoncommonTags.isSelected()) {
                    String[] params = Utils.getWhichCommonTagSelected(comboBoxViewCommonTags);
                    //Utils.selectImageInfoByTagName(comboBoxViewCommonTags, SelectedRow, files, mainScreen.this.ListexiftoolInfotable);
                    Utils.getImageInfoFromSelectedFile(params, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });

        radioButtonByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.selectImageInfoByTagName(comboBoxViewByTagName, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtonByTagName.isSelected()) {
                    Utils.selectImageInfoByTagName(comboBoxViewByTagName, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });
        radioButtonCameraMakes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.selectImageInfoByTagName(comboBoxViewCameraMake, files, mainScreen.this.ListexiftoolInfotable);
            }
        });
        comboBoxViewCameraMake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioButtonCameraMakes.isSelected()) {
                    Utils.selectImageInfoByTagName(comboBoxViewCameraMake, files, mainScreen.this.ListexiftoolInfotable);
                }
            }
        });
    }



    private String[] whichRBselected() {
        // just to make sure anything is returned we defaultly return all
        String[] params = MyConstants.ALL_PARAMS;
        // Very simple if list
        if (mainScreen.this.radioButtonViewAll.isSelected()) {
            params = MyConstants.ALL_PARAMS;
        } else if (radioButtoncommonTags.isSelected()) {
            params = Utils.getWhichCommonTagSelected(comboBoxViewCommonTags);
        } else if (radioButtonByTagName.isSelected()) {
            params = Utils.getWhichTagSelected(comboBoxViewByTagName);
        } else if (radioButtonCameraMakes.isSelected()) {
            params = Utils.getWhichTagSelected(comboBoxViewCameraMake);
        }
        return params;
    }
    

    // This is the general table listener that also enables multi row selection
    class SharedListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            // Perfectly working row selection method of first program
            List<Integer> tmpselectedIndices = new ArrayList<>();
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            if (lsm.isSelectionEmpty()) {
                logger.debug("no index selected");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        tmpselectedIndices.add(i);
                        SelectedRow = i;
                        MyVariables.setSelectedRow(i);
                    }
                }
                String[] params = whichRBselected();
                Utils.getImageInfoFromSelectedFile(params, files, ListexiftoolInfotable);

                selectedIndices = tmpselectedIndices.stream().mapToInt(Integer::intValue).toArray();
                logger.info("Selected indices: {}", tmpselectedIndices);
                selectedIndicesList = tmpselectedIndices;
                MyVariables.setSelectedFilenamesIndices(selectedIndices);
            }

        }
    }

    void fileNamesTableMouseListener() {
        // Use the mouse listener for the single cell double-click selection for the left table to be able to
        // display the image in the default viewer

        tableListfiles.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Utils.extdisplaySelectedImageInDefaultViewer();
                    logger.info("double-click registered");
                }
            }
        });
    }
    // endregion

    private void createmyMenuBar(JFrame frame) {
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

    // Sets the necessary screen texts. This might later be transformed in some i18n option.
    private void setProgramScreenTexts() {
        String version = "";
        MyCommandsText.setText(ProgramTexts.MyCommandsText);
        xmpGoogleText.setText(ProgramTexts.XmpGoogleText);
        GeotaggingLeaveFolderEmptyLabel.setText(ProgramTexts.GeotaggingLeaveFolderEmpty);
        GeotaggingLocationLabel.setText(String.format(ProgramTexts.HTML, 600, ProgramTexts.GeotaggingLocationLabel));
        GeotaggingGeosyncExplainLabel.setText(String.format(ProgramTexts.HTML, 600, ProgramTexts.GeotaggingGeosyncExplainLabel));
        gpsCalculatorLabelText.setText(String.format(ProgramTexts.HTML, 110, ProgramTexts.gpsCalculatorLabelText));
        gPanoTopText.setText(String.format(ProgramTexts.HTML, 600, ProgramTexts.GPanoTopText));
        // Special dynamic version string
        String exiftool = prefs.getByKey(EXIFTOOL_PATH, "");
        List<String> cmdparams = new ArrayList<>();
        cmdparams.add(exiftool);
        cmdparams.add("-ver");

        try {
            version = CommandRunner.runCommand(cmdparams);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command");
            version = "I cannot determine the exiftool version";
        }
        gpanoMinVersionText.setText(String.format(ProgramTexts.HTML, 600, ProgramTexts.GpanoMinVersionText + version));
    }

    private void setFormattedFieldFormats(JFormattedTextField[] theFields) {
        Locale currentLocale = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getNumberInstance(currentLocale );
        formatter.setMaximumFractionDigits(4);
        for (JFormattedTextField field : theFields) {
            field.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(formatter)));
        }
    }

    private mainScreen(JFrame frame) {
        boolean preferences = false;

        Utils.progressStatus(progressBar, false);

        createmyMenuBar(frame);
        ViewRadiobuttonListener();
        fillViewTagNamesComboboxes();

        // Now check the preferences
        preferences = checkPreferences();
        if (!preferences) {
            Utils.checkExifTool(mainScreen.this.rootPanel);
        }

        // Use the mouselistener for the double-click to display the image
        fileNamesTableMouseListener();
        //Use the table listener for theselection of multiple cells
        listSelectionModel = tableListfiles.getSelectionModel();
        tableListfiles.setRowSelectionAllowed(true);
        tableListfiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        //cellSelectionModel.addListSelectionListener(new SharedListSelectionListener());

        // Make left "tableListfiles" and right "ListexiftoolInfotable" tables read-only (un-editable)
        // This also fixex the double-click bug on the image where it retrieves the object name of the images on double-click
        tableListfiles.setDefaultEditor(Object.class, null);
        ListexiftoolInfotable.setDefaultEditor(Object.class, null);

        // icon for my dialogs
        InputStream stream = StandardFileIO.getResourceAsStream("icons/jexiftoolgui-64.png");
        try {
            icon = new ImageIcon(ImageIO.read(stream));
        } catch (IOException ex) {
            logger.debug("Error executing command");
        }


        // Try to set the defaults for artist and copyrights in the edit exif/xmp panes if prefs available
        Pair<String, String> prefsArtistCopyRights = Utils.checkPrefsArtistCopyRights();
        ExifArtistCreatortextField.setText(prefsArtistCopyRights.getLeft());
        xmpCreatortextField.setText(prefsArtistCopyRights.getLeft());
        ExifCopyrighttextField.setText(prefsArtistCopyRights.getRight());
        xmpRightstextField.setText(prefsArtistCopyRights.getRight());

        //Combobox on Gpano edit tab
        for (String item : MyConstants.GPANO_PROJECTIONS) {
            gpanoPTcomboBox.addItem(item);
        }

        programButtonListeners();
        // Some texts
        setProgramScreenTexts();
        // Set formatting for the JFormatted textFields
        setFormattedFieldFormats(getGpanoFields());

        Utils.checkForNewVersion("startup");
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("jExifToolGUI V" + ProgramTexts.Version + "   for ExifTool by Phil Harvey");
        frame.setContentPane(new mainScreen(frame).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*frame.setIconImage(
                new ImageIcon(getClass().getClassLoader().getResource("resources/jexiftoolgui.ico"))
        );*/
        try {
            // Significantly improves the look of the output in
            // terms of the folder/file icons and file names returned by FileSystemView!
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception weTried) {
           logger.error("Could nod start GUI.", weTried);
        }

        frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
