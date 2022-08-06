package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import org.hvdw.jexiftoolgui.controllers.*;
import org.hvdw.jexiftoolgui.datetime.DateTime;
import org.hvdw.jexiftoolgui.editpane.*;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;
import org.hvdw.jexiftoolgui.metadata.MetaData;
import org.hvdw.jexiftoolgui.metadata.SearchMetaData;
import org.hvdw.jexiftoolgui.model.CompareImages;
import org.hvdw.jexiftoolgui.model.GuiConfig;
import org.hvdw.jexiftoolgui.model.SQLiteModel;
import org.hvdw.jexiftoolgui.view.*;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ImageIcon;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hvdw.jexiftoolgui.controllers.StandardFileIO.checkforjexiftoolguiFolder;


public class mainScreen {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(mainScreen.class);

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    //private JFrame rootFrame;
    private JMenuBar menuBar;
    private JMenu myMenu;
    private JMenuItem menuItem;
    private JPanel rootPanel;
    private JTabbedPane tabbedPaneRight;
    private JButton buttonLoadImages;
    private JButton buttonShowImage;
    public JPanel LeftPanel;
    private JPanel LeftbuttonBar;
    private JRadioButton radioButtonViewAll;
    private JPanel ViewRadiobuttonpanel;
    private JPanel ViewDatapanel;
    private JScrollPane ViewDatascrollpanel;
    private JTree FileTree;
    private JScrollPane LeftTableScrollPanel;
    private JTable tableListfiles;
    private JTable ListexiftoolInfotable;
    private JLabel iconLabel;
    private JLabel MyCommandsText;
    private JTextField CommandsParameterstextField;
    private JButton CommandsclearParameterSFieldButton;
    private JButton CommandsclearOutputFieldButton;
    private JButton CommandsgoButton;
    private JButton CommandshelpButton;
    private JEditorPane YourCommandsOutputText;
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
    private JPanel gpsButtonPanel;
    private JPanel getGpsButtonPanel2;
    private JPanel gpsLocationPanel;
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
    private JFormattedTextField gpsLatDecimaltextField;
    private JFormattedTextField gpsLonDecimaltextField;
    private JCheckBox gpsAboveSealevelcheckBox;
    private JPanel gpsLatLonAltPanel;
    private JTextField gpsCountrytextField;
    private JCheckBox gpsCountrycheckBox;
    private JTextField gpsStateProvincetextField;
    private JCheckBox gpsStateProvincecheckBox;
    private JTextField gpsCitytextField;
    private JCheckBox gpsCitycheckBox;
    private JButton copyToInputFieldsButton;
    private JLabel CalcLatDecimaltextLabel;
    private JFormattedTextField CalcLatDegtextField;
    private JFormattedTextField CalcLatMintextField;
    private JFormattedTextField CalcLatSectextField;
    private JLabel CalcLonDecimaltextLabel;
    private JFormattedTextField CalcLonDegtextField;
    private JFormattedTextField CalcLonMintextField;
    private JFormattedTextField CalcLonSectextField;
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
    private JFormattedTextField gpsAltDecimaltextField;
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
    private JLabel xmpTopText;
    private JCheckBox CopyMakernotescheckBox;
    private JTextField lensmaketextField;
    private JCheckBox lensmakecheckBox;
    private JTextField lensmodeltextField;
    private JCheckBox lensmodelcheckBox;
    private JTextField lensserialnumbertextField;
    private JTextField focallengthtextField;
    private JCheckBox lensserialnumbercheckBox;
    private JCheckBox focallengthcheckBox;
    private JTextField focallengthIn35mmformattextField;
    private JCheckBox focallengthIn35mmformatcheckBox;
    private JTextField fnumbertextField;
    private JCheckBox fnumbercheckBox;
    private JTextField maxaperturevaluetextField;
    private JCheckBox maxaperturevaluecheckBox;
    private JComboBox meteringmodecomboBox;
    private JCheckBox meteringmodecheckBox;
    private JTextField focusdistancetextField;
    private JCheckBox focusdistancecheckBox;
    private JTextField lensidtextField;
    private JCheckBox lensidcheckBox;
    private JTextField conversionlenstextField;
    private JCheckBox conversionlenscheckBox;
    private JTextField lenstypetextField;
    private JCheckBox lenstypecheckBox;
    private JTextField lensfirmwareversiontextField;
    private JCheckBox lensfirmwareversioncheckBox;
    private JCheckBox lensOverwriteOriginalscheckBox;
    private JButton lensCopyFrombutton;
    private JButton lensSaveTobutton;
    private JButton lensResetFieldsbutton;
    private JButton lensHelpbutton;
    private JPanel saveloadlensconfigpanel;
    private JButton saveLensConfigurationbutton;
    private JButton loadLensConfigurationButton;
    private JLabel lensSaveLoadConfigLabel;
    private JPanel gpsButtonPanel2;
    private JButton buttonLoadDirectory;
    private JLabel StringsTopText;
    private JTextField StringsKeywordstextField;
    private JLabel StringsKeywordsIPTCcheckBox;
    private JRadioButton StringsKeywOverwriteradioButton;
    private JRadioButton StringsKeywAppendradioButton;
    private JRadioButton StringsKeywRemoveradioButton;
    private JRadioButton StringsKeywDontSaveradioButton;
    private JTextField StringsSubjecttextField;
    private JRadioButton StringsSubjectOverwriteradioButton;
    private JRadioButton StringsSubjectAppendradioButton;
    private JRadioButton StringsSubjectRemoveradioButton;
    private JRadioButton StringsSubjectDontSaveradioButton;
    private JTextField StringsPIItextField;
    private JLabel StringsIIPXmpcheckBox;
    private JCheckBox StringsIIPIPTCcheckBox;
    private JRadioButton StringsIIPOverwriteradioButton;
    private JRadioButton StringsIIPAppendradioButton;
    private JRadioButton StringsIIPRemoveradioButton;
    private JRadioButton StringsIIPDontSaveradioButton;
    private JCheckBox stringPlusOverwriteOriginalscheckBox;
    private JButton stringPlusCopyFrombutton;
    private JButton stringPlusSaveTobutton;
    private JButton stringPlusResetFieldsbutton;
    private JButton stringPlusHelpbutton;
    private JTextField xmpKeywordstextField;
    private JCheckBox xmpKeywordscheckBox;
    private JButton AddCommandFavoritebutton;
    private JButton LoadCommandFavoritebutton;
    private JButton SaveQuerybutton;
    private JButton loadQuerybutton;
    private JLabel JLabelDropReady;
    private JTable UserCombiTable;
    private JComboBox UserCombiscomboBox;
    private JButton udcCreateNewButton;
    private JLabel UserCombiTopText;
    private JCheckBox udcOverwriteOriginalscheckBox;
    private JButton udcCopyFrombutton;
    private JButton udcSaveTobutton;
    private JButton udcResetFieldsbutton;
    private JButton udcHelpbutton;
    private JScrollPane UserCombiScrollPane;
    private JLabel CustomConfiglabel;
    private JLabel lblLoadedFiles;
    private JRadioButton copyAllMetdataToXMPRadioButton;
    private JTabbedPane CopyDatatabbedPane;
    private JRadioButton CopyArgsRadioButton;
    private JRadioButton tagsToExifradioButton;
    private JRadioButton tagsToXmpradioButton;
    private JRadioButton tagsToIptcradioButton;
    private JRadioButton tagsToGpsradioButton;
    private JRadioButton tagsToPdfradioButton;
    private JCheckBox exif2xmpCheckBox;
    private JCheckBox gps2xmpCheckBox;
    private JCheckBox iptc2xmpCheckBox;
    private JCheckBox pdf2xmpCheckBox;
    private JCheckBox iptc2exifCheckBox;
    private JCheckBox xmp2exifCheckBox;
    private JCheckBox exif2iptcCheckBox;
    private JCheckBox xmp2iptcCheckBox;
    private JCheckBox xmp2gpsCheckBox;
    private JCheckBox xmp2pdfCheckBox;
    private JCheckBox CopyInsideImageMakeCopyOfOriginalscheckBox;
    private JButton copyInsideSaveDataTo;
    private JButton buttonCompare;
    private JButton buttonSlideshow;
    private JTabbedPane tabbedPaneExportImport;
    private JLabel exportMetaDataUiText;
    private JRadioButton catmetadataradioButton;
    private JRadioButton exportFromUserCombisradioButton;
    private JCheckBox exportAllMetadataCheckBox;
    private JCheckBox exportExifDataCheckBox;
    private JCheckBox exportXmpDataCheckBox;
    private JCheckBox exportGpsDataCheckBox;
    private JCheckBox exportIptcDataCheckBox;
    private JCheckBox exportICCDataCheckBox;
    private JComboBox exportUserCombicomboBox;
    private JRadioButton txtRadioButton;
    private JRadioButton tabRadioButton;
    private JRadioButton xmlRadioButton;
    private JRadioButton htmlRadioButton;
    private JRadioButton XMPRadioButton;
    private JRadioButton csvRadioButton;
    private JCheckBox GenExpuseMetadataTagLanguageCheckBoxport;
    private JButton GenExportbuttonOK;
    private JButton GenExportbuttonCancel;
    private JPanel bottomPanel;
    private JLabel expPdftextLabel;
    private JRadioButton A4radioButton;
    private JRadioButton LetterradioButton;
    private JRadioButton pdfPerImgradioButton;
    private JRadioButton pdfCombinedradioButton;
    private JButton ExpPDFOKbutton;
    private JSplitPane splitPanel;
    private JLabel exppdfDataText;
    private JRadioButton pdfradioButtonExpAll;
    private JComboBox pdfcomboBoxExpCommonTags;
    private JComboBox pdfcomboBoxExpByTagName;
    private JRadioButton pdfradioButtonExpCommonTags;
    private JRadioButton pdfradioButtonExpByTagName;
    private JLabel pdfLabelSupported;
    private JRadioButton exifSCradioButton;
    private JRadioButton xmpSCradioButton;
    private JRadioButton mieSCradioButton;
    private JRadioButton exvSCradioButton;
    private JButton SidecarExportButton;
    private JButton SideCarHelpbutton;
    private JTabbedPane geoformattabbedPane;
    private JButton gpssearchLocationButton;
    private JLabel lblMapcoordinates;
    private JLabel lblNominatimSearch;
    private JCheckBox gpsMinErrorcheckBox;
    private JButton buttonSearchMetadata;
    private JTextField ExpImgFoldertextField;
    private JButton ExpBrowseButton;
    private JTextField expToPdfFolderTextfield;
    private JLabel ExpLeaveFolderEmptyLabel;
    private JLabel ExpSidecarLeaveFolderEmptyLabel;
    private JButton expSidecarBrowseButton;
    private JTextField ExpSidecarFolderTextField;
    private JLabel GeoTfolderBrowseLabel;
    private JLabel genExpfolderBrowseLabel;
    private JCheckBox includeSubFoldersCheckBox;
    private JTextField ETCommandsFoldertextField;
    private JButton ETCBrowseButton;
    private JLabel etcFolderBrowseLabel;
    private JLabel etcLeaveFolderEmptyLabel;
    private JCheckBox etcIncludeSubFoldersCheckBox;
    private JLabel lblImgSourceFolder;
    private JPanel LeftCheckboxBar;
    private JCheckBox createPreviewsCheckBox;
    private JCheckBox loadMetadataCheckBox;
    private JButton leftCheckBoxBarHelpButton;
    private JLabel lblFileNamePath;
    private JLabel SeparatorText;
    private JRadioButton ImgSizeLargeradioButton;
    private JRadioButton ImgSizeSmallradioButton;
    private JRadioButton radioButtonComma;
    private JRadioButton radioButtonColon;
    private JRadioButton radioButtonSemiColon;
    private JRadioButton radioButtonHash;
    private JTextField textFieldOtherSeparator;
    private JRadioButton radioButtonOther;
    private JCheckBox gpsmakernotescheckBox;
    private JTable previewTable;
    private JPanel gpsButtonPanel3;
    private JButton gpssearchLocationButtonCoordinates;
    private JScrollPane LeftGridScrollPanel;
    private JList iconViewList;
    private ImageIcon icon;


    public File[] files;
    public int[] selectedIndices;
    public List<Integer> selectedIndicesList = new ArrayList<Integer>();
    private int SelectedRowOrIndex;
    private int SelectedCopyFromImageIndex;  // Used for the copy metadata from ..
    public List<HashMap> imagesData = new ArrayList<HashMap>();

    public String exiftool_path = "";
    private ListSelectionModel listSelectionModel;
    private ListSelectionModel icongridListSelectionModel;

    private JPopupMenu myPopupMenu;

    // Initialize all the helper classes
    PreferencesDialog prefsDialog = new PreferencesDialog();
    private MetaData metaData = new MetaData();
    private DateTime dateTime = new DateTime();
    private EditExifdata EEd = new EditExifdata();
    private EditXmpdata EXd = new EditXmpdata();
    private EditGeotaggingdata EGd = new EditGeotaggingdata();
    private EditGPSdata EGPSd = new EditGPSdata();
    private ExifToolCommands YourCmnds = new ExifToolCommands();
    private EditGpanodata EGpanod = new EditGpanodata();
    private EditLensdata ELd = new EditLensdata();
    //private DatabasePanel DBP = new DatabasePanel();
    private CreateUpdatemyLens CUL = new CreateUpdatemyLens();
    private EditStringdata ESd = new EditStringdata();
    private AddFavorite AddFav = new AddFavorite();
    private MetadataUserCombinations MD = new MetadataUserCombinations();
    private SimpleWebView WV = new SimpleWebView();
    private EditUserDefinedCombis EUDC = new EditUserDefinedCombis();


//////////////////////////////////////////////////////////////////////////////////
    // Define the several arrays for the several Edit panes on the right side. An interface or getter/setter methods would be more "correct java", but also
    // creates way more code which doesn't make it clearer either.

    public JPanel getRootPanel() {
        return rootPanel;
    }
    public JSplitPane getsplitPanel() { return splitPanel; }

    private JButton[] commandButtons() {
        return new JButton[] {buttonLoadDirectory, buttonLoadImages, buttonShowImage, buttonCompare, buttonSearchMetadata, buttonSlideshow};
    }

    private JCheckBox[] getLoadOptions() {
        return new JCheckBox[] {createPreviewsCheckBox, loadMetadataCheckBox};
    }

    private JLabel[] mainScreenLabels() { return new JLabel[] {OutputLabel, lblLoadedFiles, lblImgSourceFolder, lblFileNamePath}; }

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
    return new JTextField[] {xmpCreatortextField, xmpCredittextField, xmpRightstextField, xmpLabeltextField, xmpTitletextField, xmpKeywordstextField, xmpSubjecttextField, xmpPersontextField};
    }
    private JTextArea[] getXmpAreas() {
        return new JTextArea[] {xmpDescriptiontextArea};
    }

    private JCheckBox[] getXmpBoxes() { return new JCheckBox[]{xmpCreatorcheckBox, xmpCreditcheckBox, xmpRightscheckBox, xmpLabelcheckBox, xmpTitlecheckBox , xmpKeywordscheckBox,xmpSubjectcheckBox, xmpPersoncheckBox, xmpDescriptioncheckBox, xmpBackupOriginalscheckBox}; }

    private JTextField[] getGeotaggingFields() { return new JTextField[] {geotaggingImgFoldertextField, geotaggingGPSLogtextField, geotaggingGeosynctextField, geotaggingLocationtextfield, geotaggingCountrytextfield, geotaggingStatetextfield, geotaggingCitytextfield}; }

    private JCheckBox[] getGeotaggingBoxes() {return new JCheckBox[] {geotaggingLocationcheckbox, geotaggingCountrycheckbox, geotaggingStatecheckbox, geotaggingCitycheckbox};}

    private JRadioButton[] getCopyMetaDataRadiobuttons() {return new JRadioButton[] {copyAllMetadataRadiobutton, copyAllMetadataSameGroupsRadiobutton, copySelectiveMetadataradioButton}; }
    private JCheckBox[] getCopyMetaDataCheckBoxes() {return new JCheckBox[] {CopyExifcheckBox, CopyXmpcheckBox, CopyIptccheckBox, CopyIcc_profileDataCheckBox, CopyGpsCheckBox, CopyJfifcheckBox, CopyMakernotescheckBox, BackupOriginalscheckBox}; }
    private JRadioButton[] getInsideImageCopyRadiobuttons() {return new JRadioButton[] {copyAllMetdataToXMPRadioButton, CopyArgsRadioButton}; }
    private JRadioButton[] getInsideImageSubCopyRadiobuttons() {return new JRadioButton[] {tagsToXmpradioButton, tagsToExifradioButton, tagsToIptcradioButton, tagsToGpsradioButton, tagsToPdfradioButton}; }
    private JCheckBox[] getInsideImageCopyCheckboxes() {return new JCheckBox[] {exif2xmpCheckBox, gps2xmpCheckBox, iptc2xmpCheckBox, pdf2xmpCheckBox, iptc2exifCheckBox, xmp2exifCheckBox, exif2iptcCheckBox, xmp2iptcCheckBox, xmp2gpsCheckBox, xmp2pdfCheckBox, CopyInsideImageMakeCopyOfOriginalscheckBox}; }

    private JFormattedTextField[] getNumGPSdecFields() {
        return new JFormattedTextField[] {gpsLatDecimaltextField, gpsLonDecimaltextField, gpsAltDecimaltextField};
    }
    private JTextField[] getGPSLocationFields() {
        return new JTextField[] {gpsLocationtextField, gpsCountrytextField, gpsStateProvincetextField, gpsCitytextField};
    }
    private JCheckBox[] getGpsBoxes() {
        return new JCheckBox[] {SaveLatLonAltcheckBox, gpsAboveSealevelcheckBox, gpsLocationcheckBox, gpsCountrycheckBox, gpsStateProvincecheckBox, gpsCitycheckBox, gpsBackupOriginalscheckBox, gpsMinErrorcheckBox, gpsmakernotescheckBox};
    }

    private JFormattedTextField[] getGPSdmsFields() {
        return new JFormattedTextField[] {CalcLatDegtextField, CalcLatMintextField, CalcLatSectextField, CalcLonDegtextField, CalcLonMintextField, CalcLonSectextField};
    }
    private JRadioButton[] getGPSdmsradiobuttons() {
        return new JRadioButton[] {CalcNorthRadioButton, CalcSouthRadioButton, CalcEastradioButton, CalcWestRadioButton};
    }

    private JFormattedTextField[] getGpanoFields() {
        return new JFormattedTextField[] {gpanoCAIHPtextField, gpanoCAIWPtextField, gpanoCALPtextField, gpanoCATPtextField, gpanoFPHPtextField, gpanoFPWPtextField, gpanoPHDtextField, gpanoIVHDtextField, gpanoIVPDtextField, gpanoIVRDtextField, gpanoIHFOVDtextField};
    }
    private JCheckBox[] getGpanoCheckBoxes() {
        return new JCheckBox[] {gpanoPHDcheckBox, gpanoStitchingSoftwarecheckBox, gpanoIVHDCheckBox, gpanoIVPDCheckBox, gpanoIVRDCheckBox, gpanoIHFOVDtextFieldCheckBox, gpanoOverwriteOriginalscheckBox};
    }

    private JTextField[] getLensFields() {
        return new JTextField[] {lensmaketextField, lensmodeltextField, lensserialnumbertextField, focallengthtextField, focallengthIn35mmformattextField, fnumbertextField, maxaperturevaluetextField, focusdistancetextField, lensidtextField, conversionlenstextField, lenstypetextField, lensfirmwareversiontextField};
    }

    private JCheckBox[] getLensCheckBoxes() {
        return new JCheckBox[] {lensmakecheckBox, lensmodelcheckBox, lensserialnumbercheckBox, focallengthcheckBox, focallengthIn35mmformatcheckBox, fnumbercheckBox, maxaperturevaluecheckBox, focusdistancecheckBox, lensidcheckBox, conversionlenscheckBox, lenstypecheckBox, lensfirmwareversioncheckBox, meteringmodecheckBox, lensOverwriteOriginalscheckBox};
    }

    private JTextField[] getstringPlusFields() {
        return new JTextField[] {StringsKeywordstextField, StringsSubjecttextField, StringsPIItextField};
    }

    /*private JCheckBox[] getstringPlusBoxes() {
        return new JCheckBox[] {StringsKeywordsXmpcheckBox, StringsKeywordsIPTCcheckBox, stringPlusOverwriteOriginalscheckBox};
    }*/

    private String getStringPlusRadioButtons(JRadioButton Overwrite, JRadioButton Append, JRadioButton Remove, JRadioButton DontSave) {
        if (Overwrite.isSelected()) {
            return "=";
        }
        if (Append.isSelected()) {
            return "+=";
        }
        if (Remove.isSelected()) {
            return "-=";
        }
        if (DontSave.isSelected()) {
            return "";
//        } else { // Just to finish this method correctly
//            return "";
        }
        // Will not be reached, but we need a correct method
        return "";
    }
//    private String getSeparatorString(JRadioButton radioButtonComma, JRadioButton radioButtonColon, JRadioButton radioButtonSemiColon, JRadioButton radioButtonHash, JRadioButton radioButtonOther, JTextField textFieldOtherSeparator) {
private String getSeparatorString() {
        if (radioButtonComma.isSelected()) {
            return ",";
        }
        if (radioButtonColon.isSelected()) {
            return ":";
        }
        if (radioButtonSemiColon.isSelected()) {
            return ";";
        }
        if (radioButtonHash.isSelected()) {
            return "#";
        }
        if (radioButtonOther.isSelected()) {
            if ( (textFieldOtherSeparator.getText() == null) || ("".equals(textFieldOtherSeparator.getText())) ) {
                return ",";
            } else {
                return (textFieldOtherSeparator.getText()).trim();
            }
        }
        //Should never be reached but we need a default
        return ",";
    }
    private String[] getSelectedRadioButtons() {
        return new String[]{
                getStringPlusRadioButtons(StringsKeywOverwriteradioButton, StringsKeywAppendradioButton, StringsKeywRemoveradioButton, StringsKeywDontSaveradioButton),
                getStringPlusRadioButtons(StringsSubjectOverwriteradioButton, StringsSubjectAppendradioButton, StringsSubjectRemoveradioButton, StringsSubjectDontSaveradioButton),
                getStringPlusRadioButtons(StringsIIPOverwriteradioButton, StringsIIPAppendradioButton, StringsIIPRemoveradioButton, StringsIIPDontSaveradioButton)
        };
    }

    private JCheckBox[] getCopyMetaDatacheckboxes() {
        return new JCheckBox[] {CopyExifcheckBox, CopyXmpcheckBox, CopyIptccheckBox, CopyIcc_profileDataCheckBox, CopyGpsCheckBox, CopyJfifcheckBox, CopyMakernotescheckBox};
    }

    private JRadioButton[] getGeneralExportRadiobuttons() {
        return new JRadioButton[] {catmetadataradioButton, exportFromUserCombisradioButton, txtRadioButton, tabRadioButton, xmlRadioButton, htmlRadioButton, csvRadioButton};
    }
    private JCheckBox[] getGeneralExportCheckButtons() {
        return new JCheckBox[] {exportAllMetadataCheckBox, exportExifDataCheckBox, exportXmpDataCheckBox, exportGpsDataCheckBox, exportIptcDataCheckBox, exportICCDataCheckBox, GenExpuseMetadataTagLanguageCheckBoxport};
    }

    private JRadioButton[] getPDFradiobuttons() {
        return new JRadioButton[] {A4radioButton, LetterradioButton, ImgSizeLargeradioButton, ImgSizeSmallradioButton, pdfPerImgradioButton, pdfCombinedradioButton, pdfradioButtonExpAll, pdfradioButtonExpCommonTags, pdfradioButtonExpByTagName};
    }
    private JComboBox[] getPDFcomboboxes() {
        return new JComboBox[] {pdfcomboBoxExpCommonTags, pdfcomboBoxExpByTagName};
    }

    private JRadioButton[] getSCradiobuttons() { return new JRadioButton[] {exifSCradioButton, xmpSCradioButton, mieSCradioButton, exvSCradioButton}; }


    private void fillAllComboboxes() {
        //Combobox on User combi edit tab; do as first as we need it again
        String sqlsets = SQLiteModel.getdefinedCustomSets();
        String[] views = sqlsets.split("\\r?\\n"); // split on new lines
        // use setter to be later use it for common tags in Utils.getWhichCommonTagSelected
        MyVariables.setCustomCombis(views);
        UserCombiscomboBox.setModel(new DefaultComboBoxModel(views));
        exportUserCombicomboBox.setModel(new DefaultComboBoxModel(views));
        exportUserCombicomboBox.setEnabled(false);



        // Fill all combo boxes in the View panel
        String TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CommonTags.txt");
        String[] Tags = TagNames.split("\\r?\\n"); // split on new lines
        // Now combine Tags[] and above views[] into one array
        int length1 = Tags.length;
        int length2 = views.length;
        String[] allTags = new String[length1 + length2];
        //Using arraycopy method to merge two arrays
        System.arraycopy(Tags, 0, allTags, 0, length1);
        System.arraycopy(views, 0, allTags, length1, length2);
        Arrays.sort(allTags);
        comboBoxViewCommonTags.setModel(new DefaultComboBoxModel(allTags));
        pdfcomboBoxExpCommonTags.setModel(new DefaultComboBoxModel(allTags));

        String sqlGroups = SQLiteModel.getGroups();
        Tags = sqlGroups.split("\\r?\\n"); // split on new lines
        comboBoxViewByTagName.setModel(new DefaultComboBoxModel(Tags));
        //comboBoxQueryByTagName.setModel(new DefaultComboBoxModel(Tags));
        pdfcomboBoxExpByTagName.setModel(new DefaultComboBoxModel(Tags));


        TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CameraTagNames.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxViewCameraMake.setModel(new DefaultComboBoxModel(Tags));

        // fill combobox in Lens panel
        TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/lensmeteringmodes.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        meteringmodecomboBox.setModel(new DefaultComboBoxModel(Tags));

        //Combobox on Gpano edit tab
        for (String item : MyConstants.GPANO_PROJECTIONS) {
            gpanoPTcomboBox.addItem(item);
        }
    }

    // region IntelliJ GUI Code Generated

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 3, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.setMinimumSize(new Dimension(1100, 720));
        rootPanel.setPreferredSize(new Dimension(1350, 880));
        rootPanel.setRequestFocusEnabled(true);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, 0));
        rootPanel.add(bottomPanel, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(-1, 32), new Dimension(-1, 32), null, 1, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bottomPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "pt.filesloaded"));
        panel1.add(label1);
        lblLoadedFiles = new JLabel();
        lblLoadedFiles.setText("");
        panel1.add(lblLoadedFiles);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1);
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "pt.folder"));
        panel1.add(label2);
        lblImgSourceFolder = new JLabel();
        lblImgSourceFolder.setText("");
        panel1.add(lblImgSourceFolder);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 32), new Dimension(-1, 32), null, 1, false));
        JLabelDropReady = new JLabel();
        JLabelDropReady.setText("");
        JLabelDropReady.setVisible(false);
        panel2.add(JLabelDropReady);
        OutputLabel = new JLabel();
        OutputLabel.setText("");
        panel2.add(OutputLabel);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(100, 15));
        progressBar.setStringPainted(false);
        panel2.add(progressBar);
        splitPanel = new JSplitPane();
        splitPanel.setDividerLocation(360);
        rootPanel.add(splitPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        LeftPanel = new JPanel();
        LeftPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        LeftPanel.setPreferredSize(new Dimension(500, -1));
        splitPanel.setLeftComponent(LeftPanel);
        LeftbuttonBar = new JPanel();
        LeftbuttonBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        LeftPanel.add(LeftbuttonBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoadDirectory = new JButton();
        buttonLoadDirectory.setIcon(new ImageIcon(getClass().getResource("/icons/outline_folder_black_36dp.png")));
        buttonLoadDirectory.setIconTextGap(2);
        buttonLoadDirectory.setMaximumSize(new Dimension(38, 38));
        buttonLoadDirectory.setMinimumSize(new Dimension(38, 38));
        buttonLoadDirectory.setPreferredSize(new Dimension(38, 38));
        buttonLoadDirectory.setText("");
        buttonLoadDirectory.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "btn.loaddirectory"));
        LeftbuttonBar.add(buttonLoadDirectory);
        buttonLoadImages = new JButton();
        buttonLoadImages.setIcon(new ImageIcon(getClass().getResource("/icons/outline_collections_black_36dp.png")));
        buttonLoadImages.setIconTextGap(2);
        buttonLoadImages.setMaximumSize(new Dimension(38, 38));
        buttonLoadImages.setMinimumSize(new Dimension(38, 38));
        buttonLoadImages.setPreferredSize(new Dimension(38, 38));
        buttonLoadImages.setText("");
        buttonLoadImages.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "btn.loadimages"));
        LeftbuttonBar.add(buttonLoadImages);
        buttonShowImage = new JButton();
        buttonShowImage.setEnabled(false);
        buttonShowImage.setIcon(new ImageIcon(getClass().getResource("/icons/outline_image_black_36dp.png")));
        buttonShowImage.setIconTextGap(2);
        buttonShowImage.setMaximumSize(new Dimension(38, 38));
        buttonShowImage.setMinimumSize(new Dimension(38, 38));
        buttonShowImage.setPreferredSize(new Dimension(38, 38));
        buttonShowImage.setText("");
        buttonShowImage.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "btn.displayimages"));
        buttonShowImage.putClientProperty("hideActionText", Boolean.FALSE);
        LeftbuttonBar.add(buttonShowImage);
        buttonCompare = new JButton();
        buttonCompare.setEnabled(false);
        buttonCompare.setFocusTraversalPolicyProvider(true);
        buttonCompare.setIcon(new ImageIcon(getClass().getResource("/icons/outline_compare_arrows_black_36dp.png")));
        buttonCompare.setIconTextGap(2);
        buttonCompare.setMaximumSize(new Dimension(38, 38));
        buttonCompare.setMinimumSize(new Dimension(38, 38));
        buttonCompare.setPreferredSize(new Dimension(38, 38));
        buttonCompare.setText("");
        buttonCompare.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "btn.compareimgs"));
        buttonCompare.setVisible(true);
        LeftbuttonBar.add(buttonCompare);
        buttonSlideshow = new JButton();
        buttonSlideshow.setEnabled(false);
        buttonSlideshow.setIcon(new ImageIcon(getClass().getResource("/icons/outline_slideshow_black_36dp.png")));
        buttonSlideshow.setIconTextGap(2);
        buttonSlideshow.setMaximumSize(new Dimension(38, 38));
        buttonSlideshow.setMinimumSize(new Dimension(38, 38));
        buttonSlideshow.setPreferredSize(new Dimension(38, 38));
        buttonSlideshow.setText("");
        buttonSlideshow.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "btn.slideshow"));
        buttonSlideshow.setVisible(false);
        LeftbuttonBar.add(buttonSlideshow);
        buttonSearchMetadata = new JButton();
        buttonSearchMetadata.setEnabled(false);
        buttonSearchMetadata.setIcon(new ImageIcon(getClass().getResource("/icons/outline_search_black_36dp.png")));
        buttonSearchMetadata.setMaximumSize(new Dimension(38, 38));
        buttonSearchMetadata.setMinimumSize(new Dimension(38, 38));
        buttonSearchMetadata.setPreferredSize(new Dimension(38, 38));
        buttonSearchMetadata.setText("");
        LeftbuttonBar.add(buttonSearchMetadata);
        LeftTableScrollPanel = new JScrollPane();
        LeftTableScrollPanel.setVisible(false);
        LeftPanel.add(LeftTableScrollPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableListfiles = new JTable();
        tableListfiles.setAutoResizeMode(4);
        tableListfiles.setPreferredScrollableViewportSize(new Dimension(-1, -1));
        tableListfiles.setShowHorizontalLines(true);
        tableListfiles.setShowVerticalLines(false);
        tableListfiles.setToolTipText(this.$$$getMessageFromBundle$$$("translations/program_strings", "lp.tooltip"));
        LeftTableScrollPanel.setViewportView(tableListfiles);
        LeftCheckboxBar = new JPanel();
        LeftCheckboxBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        LeftPanel.add(LeftCheckboxBar, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createPreviewsCheckBox = new JCheckBox();
        createPreviewsCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(createPreviewsCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "lp.crpreview"));
        LeftCheckboxBar.add(createPreviewsCheckBox);
        loadMetadataCheckBox = new JCheckBox();
        loadMetadataCheckBox.setEnabled(true);
        loadMetadataCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(loadMetadataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "lp.loadmetadata"));
        loadMetadataCheckBox.setVisible(false);
        LeftCheckboxBar.add(loadMetadataCheckBox);
        leftCheckBoxBarHelpButton = new JButton();
        leftCheckBoxBarHelpButton.setIcon(new ImageIcon(getClass().getResource("/icons/outline_info_black_24dp.png")));
        leftCheckBoxBarHelpButton.setLabel("");
        leftCheckBoxBarHelpButton.setMaximumSize(new Dimension(26, 26));
        leftCheckBoxBarHelpButton.setMinimumSize(new Dimension(26, 26));
        leftCheckBoxBarHelpButton.setPreferredSize(new Dimension(26, 26));
        leftCheckBoxBarHelpButton.setText("");
        leftCheckBoxBarHelpButton.setVisible(false);
        LeftCheckboxBar.add(leftCheckBoxBarHelpButton);
        previewTable = new JTable();
        previewTable.setShowHorizontalLines(false);
        previewTable.setShowVerticalLines(false);
        previewTable.setUpdateSelectionOnSort(false);
        previewTable.setVisible(false);
        LeftPanel.add(previewTable, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 160), null, 0, false));
        LeftGridScrollPanel = new JScrollPane();
        LeftGridScrollPanel.setVerticalScrollBarPolicy(22);
        LeftPanel.add(LeftGridScrollPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        iconViewList = new JList();
        LeftGridScrollPanel.setViewportView(iconViewList);
        tabbedPaneRight = new JTabbedPane();
        splitPanel.setRightComponent(tabbedPaneRight);
        ViewDatapanel = new JPanel();
        ViewDatapanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        ViewDatapanel.setMinimumSize(new Dimension(-1, -1));
        ViewDatapanel.setPreferredSize(new Dimension(-1, -1));
        tabbedPaneRight.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "maintab.viewdata"), ViewDatapanel);
        ViewRadiobuttonpanel = new JPanel();
        ViewRadiobuttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
        ViewDatapanel.add(ViewRadiobuttonpanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radioButtonViewAll = new JRadioButton();
        radioButtonViewAll.setSelected(true);
        this.$$$loadButtonText$$$(radioButtonViewAll, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.allradiobutton"));
        ViewRadiobuttonpanel.add(radioButtonViewAll);
        radioButtoncommonTags = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtoncommonTags, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.commontags"));
        ViewRadiobuttonpanel.add(radioButtoncommonTags);
        comboBoxViewCommonTags = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewCommonTags);
        radioButtonByTagName = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtonByTagName, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.bygroup"));
        ViewRadiobuttonpanel.add(radioButtonByTagName);
        comboBoxViewByTagName = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewByTagName);
        radioButtonCameraMakes = new JRadioButton();
        radioButtonCameraMakes.setLabel("By Camera");
        this.$$$loadButtonText$$$(radioButtonCameraMakes, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.bycamera"));
        ViewRadiobuttonpanel.add(radioButtonCameraMakes);
        comboBoxViewCameraMake = new JComboBox();
        ViewRadiobuttonpanel.add(comboBoxViewCameraMake);
        ViewDatascrollpanel = new JScrollPane();
        ViewDatapanel.add(ViewDatascrollpanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ListexiftoolInfotable = new JTable();
        ListexiftoolInfotable.setAutoResizeMode(0);
        ViewDatascrollpanel.setViewportView(ListexiftoolInfotable);
        lblFileNamePath = new JLabel();
        Font lblFileNamePathFont = this.$$$getFont$$$(null, Font.BOLD | Font.ITALIC, -1, lblFileNamePath.getFont());
        if (lblFileNamePathFont != null) lblFileNamePath.setFont(lblFileNamePathFont);
        lblFileNamePath.setText("");
        ViewDatapanel.add(lblFileNamePath, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneRight.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "maintab.editdata"), panel3);
        tabbedPaneEditfunctions = new JTabbedPane();
        panel3.add(tabbedPaneEditfunctions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        ExifEditpanel = new JPanel();
        ExifEditpanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 20, 0, 20), -1, -1));
        ExifEditpanel.setMinimumSize(new Dimension(750, 511));
        ExifEditpanel.setPreferredSize(new Dimension(800, 575));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.exiftab"), ExifEditpanel);
        Camera_Equipment = new JPanel();
        Camera_Equipment.setLayout(new GridLayoutManager(3, 3, new Insets(5, 0, 5, 0), -1, -1));
        ExifEditpanel.add(Camera_Equipment, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.make"));
        Camera_Equipment.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifMaketextField = new JTextField();
        ExifMaketextField.setPreferredSize(new Dimension(500, 25));
        Camera_Equipment.add(ExifMaketextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifMakecheckBox = new JCheckBox();
        ExifMakecheckBox.setSelected(true);
        ExifMakecheckBox.setText("");
        Camera_Equipment.add(ExifMakecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        Camera_Equipment.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setPreferredSize(new Dimension(650, 18));
        this.$$$loadLabelText$$$(label5, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.cameraequip"));
        Camera_Equipment.add(label5, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.model"));
        Camera_Equipment.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, -1, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label7, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.datetime"));
        DateTime.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setPreferredSize(new Dimension(500, 18));
        this.$$$loadLabelText$$$(label8, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.dtformat"));
        DateTime.add(label8, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label9, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.modifydate"));
        DateTime.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModifyDatetextField = new JTextField();
        ExifModifyDatetextField.setPreferredSize(new Dimension(500, 25));
        DateTime.add(ExifModifyDatetextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifModifyDatecheckBox = new JCheckBox();
        ExifModifyDatecheckBox.setSelected(true);
        ExifModifyDatecheckBox.setText("");
        DateTime.add(ExifModifyDatecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label10, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.datetimeoriginal"));
        DateTime.add(label10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDateTimeOriginaltextField = new JTextField();
        ExifDateTimeOriginaltextField.setPreferredSize(new Dimension(500, 25));
        DateTime.add(ExifDateTimeOriginaltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDateTimeOriginalcheckBox = new JCheckBox();
        ExifDateTimeOriginalcheckBox.setSelected(true);
        ExifDateTimeOriginalcheckBox.setText("");
        DateTime.add(ExifDateTimeOriginalcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label11, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.createdate"));
        DateTime.add(label11, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        final JLabel label12 = new JLabel();
        label12.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label12, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.artist"));
        CreativeTags.add(label12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifArtistCreatortextField = new JTextField();
        ExifArtistCreatortextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifArtistCreatortextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifArtistCreatorcheckBox = new JCheckBox();
        ExifArtistCreatorcheckBox.setSelected(true);
        ExifArtistCreatorcheckBox.setText("");
        CreativeTags.add(ExifArtistCreatorcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, -1, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        this.$$$loadLabelText$$$(label13, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.creatags"));
        CreativeTags.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setMaximumSize(new Dimension(250, 18));
        label14.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label14, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.copyright"));
        CreativeTags.add(label14, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCopyrighttextField = new JTextField();
        ExifCopyrighttextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifCopyrighttextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifCopyrightcheckBox = new JCheckBox();
        ExifCopyrightcheckBox.setSelected(true);
        ExifCopyrightcheckBox.setText("");
        CreativeTags.add(ExifCopyrightcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label15, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.usercomm"));
        CreativeTags.add(label15, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifUsercommenttextField = new JTextField();
        ExifUsercommenttextField.setPreferredSize(new Dimension(500, 25));
        CreativeTags.add(ExifUsercommenttextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifUsercommentcheckBox = new JCheckBox();
        ExifUsercommentcheckBox.setSelected(true);
        ExifUsercommentcheckBox.setText("");
        CreativeTags.add(ExifUsercommentcheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label16, this.$$$getMessageFromBundle$$$("translations/program_strings", "exif.description"));
        CreativeTags.add(label16, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDescriptiontextArea = new JTextArea();
        ExifDescriptiontextArea.setPreferredSize(new Dimension(500, 80));
        ExifDescriptiontextArea.setWrapStyleWord(true);
        CreativeTags.add(ExifDescriptiontextArea, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifDescriptioncheckBox = new JCheckBox();
        ExifDescriptioncheckBox.setSelected(true);
        ExifDescriptioncheckBox.setText("");
        CreativeTags.add(ExifDescriptioncheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        ExifEditpanel.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifcopyFromButton = new JButton();
        this.$$$loadButtonText$$$(ExifcopyFromButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel4.add(ExifcopyFromButton);
        ExifsaveToButton = new JButton();
        this.$$$loadButtonText$$$(ExifsaveToButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel4.add(ExifsaveToButton);
        ExifBackupOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(ExifBackupOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel4.add(ExifBackupOriginalscheckBox);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        ExifEditpanel.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExifcopyDefaultsButton = new JButton();
        this.$$$loadButtonText$$$(ExifcopyDefaultsButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copydefaults"));
        panel5.add(ExifcopyDefaultsButton);
        resetFieldsButton = new JButton();
        this.$$$loadButtonText$$$(resetFieldsButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel5.add(resetFieldsButton);
        ExifhelpButton = new JButton();
        this.$$$loadButtonText$$$(ExifhelpButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel5.add(ExifhelpButton);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(13, 3, new Insets(10, 20, 10, 20), -1, -1));
        panel6.setMinimumSize(new Dimension(750, 464));
        panel6.setPreferredSize(new Dimension(800, 575));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.xmptab"), panel6);
        final JLabel label17 = new JLabel();
        label17.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label17, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.creator"));
        panel6.add(label17, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCreatortextField = new JTextField();
        xmpCreatortextField.setPreferredSize(new Dimension(500, 25));
        xmpCreatortextField.setText("");
        panel6.add(xmpCreatortextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCreatorcheckBox = new JCheckBox();
        xmpCreatorcheckBox.setSelected(true);
        xmpCreatorcheckBox.setText("");
        panel6.add(xmpCreatorcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label18, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.rights"));
        panel6.add(label18, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRightstextField = new JTextField();
        xmpRightstextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpRightstextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpRightscheckBox = new JCheckBox();
        xmpRightscheckBox.setSelected(true);
        xmpRightscheckBox.setText("");
        panel6.add(xmpRightscheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label19, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.description"));
        panel6.add(label19, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpDescriptiontextArea = new JTextArea();
        xmpDescriptiontextArea.setPreferredSize(new Dimension(500, 80));
        xmpDescriptiontextArea.setWrapStyleWord(true);
        panel6.add(xmpDescriptiontextArea, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpDescriptioncheckBox = new JCheckBox();
        xmpDescriptioncheckBox.setSelected(true);
        xmpDescriptioncheckBox.setText("");
        panel6.add(xmpDescriptioncheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, -1, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        this.$$$loadLabelText$$$(label20, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        panel6.add(label20, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label21, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.label"));
        panel6.add(label21, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpLabeltextField = new JTextField();
        xmpLabeltextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpLabeltextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpLabelcheckBox = new JCheckBox();
        xmpLabelcheckBox.setSelected(true);
        xmpLabelcheckBox.setText("");
        panel6.add(xmpLabelcheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label22, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.subject"));
        panel6.add(label22, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpSubjecttextField = new JTextField();
        xmpSubjecttextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpSubjecttextField, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpSubjectcheckBox = new JCheckBox();
        xmpSubjectcheckBox.setSelected(true);
        xmpSubjectcheckBox.setText("");
        panel6.add(xmpSubjectcheckBox, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label23, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.pim"));
        panel6.add(label23, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpPersontextField = new JTextField();
        xmpPersontextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpPersontextField, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpPersoncheckBox = new JCheckBox();
        xmpPersoncheckBox.setSelected(true);
        xmpPersoncheckBox.setText("");
        panel6.add(xmpPersoncheckBox, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel6.add(panel7, new GridConstraints(11, 0, 1, 3, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(xmpCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel7.add(xmpCopyFrombutton);
        xmpSaveTobutton = new JButton();
        this.$$$loadButtonText$$$(xmpSaveTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel7.add(xmpSaveTobutton);
        xmpBackupOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(xmpBackupOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel7.add(xmpBackupOriginalscheckBox);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel6.add(panel8, new GridConstraints(12, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpCopyDefaultsbutton = new JButton();
        this.$$$loadButtonText$$$(xmpCopyDefaultsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copydefaults"));
        panel8.add(xmpCopyDefaultsbutton);
        xmpResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(xmpResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel8.add(xmpResetFieldsbutton);
        xmpHelpbutton = new JButton();
        this.$$$loadButtonText$$$(xmpHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel8.add(xmpHelpbutton);
        final JLabel label24 = new JLabel();
        this.$$$loadLabelText$$$(label24, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.credline"));
        panel6.add(label24, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 18), null, 0, false));
        xmpCredittextField = new JTextField();
        panel6.add(xmpCredittextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(500, 25), null, 0, false));
        xmpCreditcheckBox = new JCheckBox();
        xmpCreditcheckBox.setSelected(true);
        xmpCreditcheckBox.setText("");
        panel6.add(xmpCreditcheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpTopText = new JLabel();
        xmpTopText.setText("xmpTopText");
        panel6.add(xmpTopText, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(500, 18), null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setPreferredSize(new Dimension(150, 18));
        this.$$$loadLabelText$$$(label25, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.title"));
        panel6.add(label25, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpTitletextField = new JTextField();
        xmpTitletextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpTitletextField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpTitlecheckBox = new JCheckBox();
        xmpTitlecheckBox.setSelected(true);
        xmpTitlecheckBox.setText("");
        panel6.add(xmpTitlecheckBox, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        this.$$$loadLabelText$$$(label26, this.$$$getMessageFromBundle$$$("translations/program_strings", "xmp.keywords"));
        panel6.add(label26, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpKeywordstextField = new JTextField();
        xmpKeywordstextField.setPreferredSize(new Dimension(500, 25));
        panel6.add(xmpKeywordstextField, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xmpKeywordscheckBox = new JCheckBox();
        xmpKeywordscheckBox.setSelected(true);
        xmpKeywordscheckBox.setText("");
        panel6.add(xmpKeywordscheckBox, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(7, 1, new Insets(5, 5, 5, 10), -1, -1));
        panel9.setMinimumSize(new Dimension(750, 500));
        panel9.setPreferredSize(new Dimension(800, 550));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.gpstab"), panel9);
        gpsLocationPanel = new JPanel();
        gpsLocationPanel.setLayout(new GridLayoutManager(7, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel9.add(gpsLocationPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpsLocationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, -1, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        this.$$$loadLabelText$$$(label27, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.xmpiptcloc"));
        gpsLocationPanel.add(label27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        this.$$$loadLabelText$$$(label28, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.whereis"));
        gpsLocationPanel.add(label28, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        Font label29Font = this.$$$getFont$$$(null, Font.BOLD, -1, label29.getFont());
        if (label29Font != null) label29.setFont(label29Font);
        this.$$$loadLabelText$$$(label29, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        gpsLocationPanel.add(label29, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        label30.setPreferredSize(new Dimension(75, 18));
        this.$$$loadLabelText$$$(label30, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.location"));
        gpsLocationPanel.add(label30, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsLocationtextField = new JTextField();
        gpsLocationtextField.setPreferredSize(new Dimension(300, 30));
        gpsLocationPanel.add(gpsLocationtextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(450, 25), null, 0, false));
        gpsLocationcheckBox = new JCheckBox();
        gpsLocationcheckBox.setSelected(true);
        gpsLocationcheckBox.setText("");
        gpsLocationPanel.add(gpsLocationcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        this.$$$loadLabelText$$$(label31, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.country"));
        gpsLocationPanel.add(label31, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCountrytextField = new JTextField();
        gpsLocationPanel.add(gpsCountrytextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(450, 25), null, 0, false));
        gpsCountrycheckBox = new JCheckBox();
        gpsCountrycheckBox.setSelected(true);
        gpsCountrycheckBox.setText("");
        gpsLocationPanel.add(gpsCountrycheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label32 = new JLabel();
        this.$$$loadLabelText$$$(label32, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.statprov"));
        gpsLocationPanel.add(label32, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsStateProvincetextField = new JTextField();
        gpsStateProvincetextField.setText("");
        gpsLocationPanel.add(gpsStateProvincetextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(450, 25), null, 0, false));
        gpsStateProvincecheckBox = new JCheckBox();
        gpsStateProvincecheckBox.setSelected(true);
        gpsStateProvincecheckBox.setText("");
        gpsLocationPanel.add(gpsStateProvincecheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label33 = new JLabel();
        this.$$$loadLabelText$$$(label33, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.city"));
        gpsLocationPanel.add(label33, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsCitytextField = new JTextField();
        gpsLocationPanel.add(gpsCitytextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(450, 25), null, 0, false));
        gpsCitycheckBox = new JCheckBox();
        gpsCitycheckBox.setSelected(true);
        gpsCitycheckBox.setText("");
        gpsLocationPanel.add(gpsCitycheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsMinErrorcheckBox = new JCheckBox();
        gpsMinErrorcheckBox.setSelected(true);
        this.$$$loadButtonText$$$(gpsMinErrorcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.minorerror"));
        gpsLocationPanel.add(gpsMinErrorcheckBox, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsmakernotescheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(gpsmakernotescheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.addtomakernotes"));
        gpsLocationPanel.add(gpsmakernotescheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel9.add(spacer2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel9.add(panel10, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsButtonPanel = new JPanel();
        gpsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel10.add(gpsButtonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(gpsCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        gpsButtonPanel.add(gpsCopyFrombutton);
        gpsSaveTobutton = new JButton();
        this.$$$loadButtonText$$$(gpsSaveTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        gpsButtonPanel.add(gpsSaveTobutton);
        gpsBackupOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(gpsBackupOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        gpsButtonPanel.add(gpsBackupOriginalscheckBox);
        gpsButtonPanel2 = new JPanel();
        gpsButtonPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel10.add(gpsButtonPanel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpsResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(gpsResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        gpsButtonPanel2.add(gpsResetFieldsbutton);
        gpsMapcoordinatesbutton = new JButton();
        this.$$$loadButtonText$$$(gpsMapcoordinatesbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.btnopenmapcoords"));
        gpsButtonPanel2.add(gpsMapcoordinatesbutton);
        gpsHelpbutton = new JButton();
        this.$$$loadButtonText$$$(gpsHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        gpsButtonPanel2.add(gpsHelpbutton);
        gpsButtonPanel3 = new JPanel();
        gpsButtonPanel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel10.add(gpsButtonPanel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpssearchLocationButton = new JButton();
        this.$$$loadButtonText$$$(gpssearchLocationButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.searchbtn"));
        gpsButtonPanel3.add(gpssearchLocationButton);
        gpssearchLocationButtonCoordinates = new JButton();
        this.$$$loadButtonText$$$(gpssearchLocationButtonCoordinates, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.searchbycoordsbtn"));
        gpsButtonPanel3.add(gpssearchLocationButtonCoordinates);
        geoformattabbedPane = new JTabbedPane();
        panel9.add(geoformattabbedPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(600, 200), new Dimension(600, -1), 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        geoformattabbedPane.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.decdegrees"), panel11);
        gpsLatLonAltPanel = new JPanel();
        gpsLatLonAltPanel.setLayout(new GridLayoutManager(5, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel11.add(gpsLatLonAltPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(550, -1), new Dimension(550, -1), 1, false));
        final JLabel label34 = new JLabel();
        this.$$$loadLabelText$$$(label34, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.gps"));
        gpsLatLonAltPanel.add(label34, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label35 = new JLabel();
        this.$$$loadLabelText$$$(label35, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.decimal"));
        gpsLatLonAltPanel.add(label35, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        this.$$$loadLabelText$$$(label36, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.latitude"));
        gpsLatLonAltPanel.add(label36, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsLatDecimaltextField = new JFormattedTextField();
        gpsLatLonAltPanel.add(gpsLatDecimaltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 25), null, 0, false));
        final JLabel label37 = new JLabel();
        this.$$$loadLabelText$$$(label37, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.altitude"));
        gpsLatLonAltPanel.add(label37, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label38 = new JLabel();
        this.$$$loadLabelText$$$(label38, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.longitude"));
        gpsLatLonAltPanel.add(label38, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpsLonDecimaltextField = new JFormattedTextField();
        gpsLatLonAltPanel.add(gpsLonDecimaltextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 25), null, 0, false));
        gpsAltDecimaltextField = new JFormattedTextField();
        gpsLatLonAltPanel.add(gpsAltDecimaltextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 25), null, 0, false));
        gpsAboveSealevelcheckBox = new JCheckBox();
        gpsAboveSealevelcheckBox.setSelected(true);
        this.$$$loadButtonText$$$(gpsAboveSealevelcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.above"));
        gpsLatLonAltPanel.add(gpsAboveSealevelcheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        final JLabel label39 = new JLabel();
        label39.setText("dd.ddddddd");
        gpsLatLonAltPanel.add(label39, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label40 = new JLabel();
        label40.setText("ddd.ddddddd");
        gpsLatLonAltPanel.add(label40, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label41 = new JLabel();
        label41.setText("ddddd.dd");
        gpsLatLonAltPanel.add(label41, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        geoformattabbedPane.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.dmsformat"), panel12);
        gpsCalculationPanel = new JPanel();
        gpsCalculationPanel.setLayout(new GridLayoutManager(4, 8, new Insets(5, 5, 5, 5), -1, -1));
        panel12.add(gpsCalculationPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        final JLabel label42 = new JLabel();
        this.$$$loadLabelText$$$(label42, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.degrees"));
        gpsCalculationPanel.add(label42, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label43 = new JLabel();
        this.$$$loadLabelText$$$(label43, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.minutes"));
        gpsCalculationPanel.add(label43, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label44 = new JLabel();
        this.$$$loadLabelText$$$(label44, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.seconds"));
        gpsCalculationPanel.add(label44, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label45 = new JLabel();
        this.$$$loadLabelText$$$(label45, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.latitude"));
        gpsCalculationPanel.add(label45, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcLatDegtextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLatDegtextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), new Dimension(50, -1), 0, false));
        CalcLatMintextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLatMintextField, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), new Dimension(50, -1), 0, false));
        CalcLatSectextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLatSectextField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), new Dimension(50, -1), 0, false));
        final JLabel label46 = new JLabel();
        this.$$$loadLabelText$$$(label46, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.longitude"));
        gpsCalculationPanel.add(label46, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcLonDegtextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLonDegtextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), new Dimension(50, -1), 0, false));
        CalcLonMintextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLonMintextField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 25), new Dimension(50, -1), 0, false));
        CalcLonSectextField = new JFormattedTextField();
        gpsCalculationPanel.add(CalcLonSectextField, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, 25), new Dimension(40, -1), 0, false));
        CalcNorthRadioButton = new JRadioButton();
        CalcNorthRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(CalcNorthRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.n"));
        gpsCalculationPanel.add(CalcNorthRadioButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcSouthRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(CalcSouthRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.s"));
        gpsCalculationPanel.add(CalcSouthRadioButton, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcEastradioButton = new JRadioButton();
        CalcEastradioButton.setSelected(true);
        this.$$$loadButtonText$$$(CalcEastradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.e"));
        gpsCalculationPanel.add(CalcEastradioButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalcWestRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(CalcWestRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.w"));
        gpsCalculationPanel.add(CalcWestRadioButton, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        decimalToMinutesSecondsButton = new JButton();
        decimalToMinutesSecondsButton.setEnabled(false);
        decimalToMinutesSecondsButton.setText("Decimal to minutes-seconds =>");
        decimalToMinutesSecondsButton.setVisible(false);
        gpsCalculationPanel.add(decimalToMinutesSecondsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minutesSecondsToDecimalButton = new JButton();
        minutesSecondsToDecimalButton.setEnabled(false);
        minutesSecondsToDecimalButton.setLabel("Convert to decimal degrees");
        this.$$$loadButtonText$$$(minutesSecondsToDecimalButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.btnconvert"));
        minutesSecondsToDecimalButton.setVisible(false);
        gpsCalculationPanel.add(minutesSecondsToDecimalButton, new GridConstraints(3, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyToInputFieldsButton = new JButton();
        copyToInputFieldsButton.setEnabled(false);
        this.$$$loadButtonText$$$(copyToInputFieldsButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.btncopytoinp"));
        copyToInputFieldsButton.setVisible(false);
        gpsCalculationPanel.add(copyToInputFieldsButton, new GridConstraints(3, 5, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SaveLatLonAltcheckBox = new JCheckBox();
        SaveLatLonAltcheckBox.setSelected(true);
        this.$$$loadButtonText$$$(SaveLatLonAltcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.savella"));
        panel9.add(SaveLatLonAltcheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblNominatimSearch = new JLabel();
        lblNominatimSearch.setText("gps.searchtxt text string");
        panel9.add(lblNominatimSearch, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblMapcoordinates = new JLabel();
        lblMapcoordinates.setText("gps.extsearch translated strings");
        panel9.add(lblMapcoordinates, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GeotaggingEditpanel = new JPanel();
        GeotaggingEditpanel.setLayout(new GridLayoutManager(8, 1, new Insets(10, 20, 10, 20), -1, -1));
        GeotaggingEditpanel.setMinimumSize(new Dimension(750, 500));
        GeotaggingEditpanel.setPreferredSize(new Dimension(800, 550));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.geotaggingtab"), GeotaggingEditpanel);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 20, 0), -1, -1));
        GeotaggingEditpanel.add(panel13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label47 = new JLabel();
        Font label47Font = this.$$$getFont$$$(null, Font.BOLD, -1, label47.getFont());
        if (label47Font != null) label47.setFont(label47Font);
        this.$$$loadLabelText$$$(label47, this.$$$getMessageFromBundle$$$("translations/program_strings", "fld.imagefolder"));
        panel13.add(label47, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel13.add(panel14, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingImgFoldertextField = new JTextField();
        geotaggingImgFoldertextField.setPreferredSize(new Dimension(500, 25));
        panel14.add(geotaggingImgFoldertextField);
        geotaggingImgFolderbutton = new JButton();
        this.$$$loadButtonText$$$(geotaggingImgFolderbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.browse"));
        panel14.add(geotaggingImgFolderbutton);
        GeotaggingLeaveFolderEmptyLabel = new JLabel();
        GeotaggingLeaveFolderEmptyLabel.setText("");
        panel13.add(GeotaggingLeaveFolderEmptyLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(650, -1), null, 0, false));
        GeoTfolderBrowseLabel = new JLabel();
        GeoTfolderBrowseLabel.setText("GeoTfolderBrowseLabel");
        panel13.add(GeoTfolderBrowseLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        GeotaggingEditpanel.add(panel15, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label48 = new JLabel();
        Font label48Font = this.$$$getFont$$$(null, Font.BOLD, -1, label48.getFont());
        if (label48Font != null) label48.setFont(label48Font);
        this.$$$loadLabelText$$$(label48, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.gpslogfile"));
        panel15.add(label48, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel15.add(panel16, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingGPSLogtextField = new JTextField();
        geotaggingGPSLogtextField.setPreferredSize(new Dimension(500, 25));
        panel16.add(geotaggingGPSLogtextField);
        geotaggingGPSLogbutton = new JButton();
        this.$$$loadButtonText$$$(geotaggingGPSLogbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.browse"));
        panel16.add(geotaggingGPSLogbutton);
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        GeotaggingEditpanel.add(panel17, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label49 = new JLabel();
        Font label49Font = this.$$$getFont$$$(null, Font.BOLD, -1, label49.getFont());
        if (label49Font != null) label49.setFont(label49Font);
        this.$$$loadLabelText$$$(label49, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.geosynctime"));
        panel17.add(label49, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel17.add(panel18, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingGeosynctextField = new JTextField();
        geotaggingGeosynctextField.setPreferredSize(new Dimension(500, 25));
        geotaggingGeosynctextField.setText("0000:00:00 00:00:00");
        panel18.add(geotaggingGeosynctextField);
        final JLabel label50 = new JLabel();
        this.$$$loadLabelText$$$(label50, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.dtformat"));
        panel18.add(label50);
        GeotaggingGeosyncExplainLabel = new JLabel();
        this.$$$loadLabelText$$$(GeotaggingGeosyncExplainLabel, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.geosyncexpl"));
        panel17.add(GeotaggingGeosyncExplainLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        GeotaggingEditpanel.add(panel19, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingWriteInfobutton = new JButton();
        this.$$$loadButtonText$$$(geotaggingWriteInfobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.btnwritegeoinfo"));
        panel19.add(geotaggingWriteInfobutton);
        resetGeotaggingbutton = new JButton();
        this.$$$loadButtonText$$$(resetGeotaggingbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel19.add(resetGeotaggingbutton);
        geotaggingHelpbutton = new JButton();
        this.$$$loadButtonText$$$(geotaggingHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel19.add(geotaggingHelpbutton);
        geotaggingOverwriteOriginalscheckBox = new JCheckBox();
        geotaggingOverwriteOriginalscheckBox.setSelected(false);
        this.$$$loadButtonText$$$(geotaggingOverwriteOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        GeotaggingEditpanel.add(geotaggingOverwriteOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(6, 3, new Insets(5, 5, 5, 5), -1, -1));
        GeotaggingEditpanel.add(panel20, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel20.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label51 = new JLabel();
        Font label51Font = this.$$$getFont$$$(null, Font.BOLD, -1, label51.getFont());
        if (label51Font != null) label51.setFont(label51Font);
        this.$$$loadLabelText$$$(label51, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.xmpiptcloc"));
        panel20.add(label51, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label52 = new JLabel();
        this.$$$loadLabelText$$$(label52, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.whereis"));
        panel20.add(label52, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label53 = new JLabel();
        Font label53Font = this.$$$getFont$$$(null, Font.BOLD, -1, label53.getFont());
        if (label53Font != null) label53.setFont(label53Font);
        this.$$$loadLabelText$$$(label53, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        panel20.add(label53, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label54 = new JLabel();
        label54.setPreferredSize(new Dimension(75, 18));
        this.$$$loadLabelText$$$(label54, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.location"));
        panel20.add(label54, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        geotaggingLocationtextfield = new JTextField();
        geotaggingLocationtextfield.setPreferredSize(new Dimension(300, 30));
        panel20.add(geotaggingLocationtextfield, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(400, 25), null, 0, false));
        geotaggingLocationcheckbox = new JCheckBox();
        geotaggingLocationcheckbox.setSelected(false);
        geotaggingLocationcheckbox.setText("");
        panel20.add(geotaggingLocationcheckbox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label55 = new JLabel();
        this.$$$loadLabelText$$$(label55, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.country"));
        panel20.add(label55, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingCountrytextfield = new JTextField();
        panel20.add(geotaggingCountrytextfield, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(400, 25), null, 0, false));
        geotaggingCountrycheckbox = new JCheckBox();
        geotaggingCountrycheckbox.setSelected(false);
        geotaggingCountrycheckbox.setText("");
        panel20.add(geotaggingCountrycheckbox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label56 = new JLabel();
        this.$$$loadLabelText$$$(label56, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.statprov"));
        panel20.add(label56, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingStatetextfield = new JTextField();
        panel20.add(geotaggingStatetextfield, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(400, 25), null, 0, false));
        geotaggingStatecheckbox = new JCheckBox();
        geotaggingStatecheckbox.setSelected(false);
        geotaggingStatecheckbox.setText("");
        panel20.add(geotaggingStatecheckbox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label57 = new JLabel();
        this.$$$loadLabelText$$$(label57, this.$$$getMessageFromBundle$$$("translations/program_strings", "gps.city"));
        panel20.add(label57, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        geotaggingCitytextfield = new JTextField();
        geotaggingCitytextfield.setText("");
        panel20.add(geotaggingCitytextfield, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(400, 25), null, 0, false));
        geotaggingCitycheckbox = new JCheckBox();
        geotaggingCitycheckbox.setSelected(false);
        geotaggingCitycheckbox.setText("");
        panel20.add(geotaggingCitycheckbox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GeotaggingLocationLabel = new JLabel();
        this.$$$loadLabelText$$$(GeotaggingLocationLabel, this.$$$getMessageFromBundle$$$("translations/program_strings", "geo.geotagexpl"));
        panel20.add(GeotaggingLocationLabel, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(6, 1, new Insets(10, 0, 0, 0), -1, -1));
        panel21.setMinimumSize(new Dimension(750, 500));
        panel21.setPreferredSize(new Dimension(800, 550));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.gpanotab"), panel21);
        gPanoTopText = new JLabel();
        this.$$$loadLabelText$$$(gPanoTopText, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.toptext"));
        panel21.add(gPanoTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(5, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel21.add(panel22, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label58 = new JLabel();
        this.$$$loadLabelText$$$(label58, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.croppedareaimageheightpixels"));
        panel22.add(label58, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, -1), null, 0, false));
        gpanoCAIHPtextField = new JFormattedTextField();
        gpanoCAIHPtextField.setColumns(0);
        gpanoCAIHPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoCAIHPtextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label59 = new JLabel();
        label59.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label59, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.croppedareaimagewidthpixels"));
        panel22.add(label59, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCAIWPtextField = new JFormattedTextField();
        gpanoCAIWPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoCAIWPtextField, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label60 = new JLabel();
        label60.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label60, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.fullpanoheightpixels"));
        panel22.add(label60, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoFPHPtextField = new JFormattedTextField();
        gpanoFPHPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoFPHPtextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label61 = new JLabel();
        label61.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label61, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.fullpanowidthpixels"));
        panel22.add(label61, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoFPWPtextField = new JFormattedTextField();
        gpanoFPWPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoFPWPtextField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JLabel label62 = new JLabel();
        this.$$$loadLabelText$$$(label62, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.projectiontype"));
        panel22.add(label62, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoPTcomboBox = new JComboBox();
        panel22.add(gpanoPTcomboBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label63 = new JLabel();
        this.$$$loadLabelText$$$(label63, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.usepanoramaviewer"));
        panel22.add(label63, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox1 = new JCheckBox();
        checkBox1.setEnabled(false);
        checkBox1.setSelected(true);
        checkBox1.setText("");
        panel22.add(checkBox1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label64 = new JLabel();
        label64.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label64, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.croppedarealeftpixels"));
        panel22.add(label64, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label65 = new JLabel();
        label65.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label65, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.croppedareatoppixels"));
        panel22.add(label65, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCATPtextField = new JFormattedTextField();
        gpanoCATPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoCATPtextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoCALPtextField = new JFormattedTextField();
        gpanoCALPtextField.setPreferredSize(new Dimension(75, 30));
        panel22.add(gpanoCALPtextField, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(7, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel21.add(panel23, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel23.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label66 = new JLabel();
        this.$$$loadLabelText$$$(label66, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.initialviewheadingdegrees"));
        panel23.add(label66, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        gpanoIVHDtextField = new JFormattedTextField();
        panel23.add(gpanoIVHDtextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVHDCheckBox = new JCheckBox();
        gpanoIVHDCheckBox.setText("");
        panel23.add(gpanoIVHDCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label67 = new JLabel();
        this.$$$loadLabelText$$$(label67, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.initialviewpitchdegrees"));
        panel23.add(label67, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIVPDtextField = new JFormattedTextField();
        panel23.add(gpanoIVPDtextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVPDCheckBox = new JCheckBox();
        gpanoIVPDCheckBox.setText("");
        panel23.add(gpanoIVPDCheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label68 = new JLabel();
        this.$$$loadLabelText$$$(label68, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.initialviewrolldegrees"));
        panel23.add(label68, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIVRDtextField = new JFormattedTextField();
        panel23.add(gpanoIVRDtextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIVRDCheckBox = new JCheckBox();
        gpanoIVRDCheckBox.setText("");
        panel23.add(gpanoIVRDCheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label69 = new JLabel();
        this.$$$loadLabelText$$$(label69, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.initialhorizontalfovdegrees"));
        panel23.add(label69, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoIHFOVDtextField = new JFormattedTextField();
        panel23.add(gpanoIHFOVDtextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoIHFOVDtextFieldCheckBox = new JCheckBox();
        gpanoIHFOVDtextFieldCheckBox.setText("");
        panel23.add(gpanoIHFOVDtextFieldCheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label70 = new JLabel();
        label70.setPreferredSize(new Dimension(200, 18));
        this.$$$loadLabelText$$$(label70, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.stitchingsoftware"));
        panel23.add(label70, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, -1), null, 0, false));
        gpanoStitchingSoftwaretextField = new JTextField();
        gpanoStitchingSoftwaretextField.setPreferredSize(new Dimension(450, 25));
        panel23.add(gpanoStitchingSoftwaretextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(400, 25), null, 0, false));
        gpanoStitchingSoftwarecheckBox = new JCheckBox();
        gpanoStitchingSoftwarecheckBox.setText("");
        panel23.add(gpanoStitchingSoftwarecheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label71 = new JLabel();
        this.$$$loadLabelText$$$(label71, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.poseheadingdegrees"));
        panel23.add(label71, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpanoPHDtextField = new JFormattedTextField();
        panel23.add(gpanoPHDtextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 25), null, 0, false));
        gpanoPHDcheckBox = new JCheckBox();
        gpanoPHDcheckBox.setText("");
        panel23.add(gpanoPHDcheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label72 = new JLabel();
        Font label72Font = this.$$$getFont$$$(null, Font.BOLD, -1, label72.getFont());
        if (label72Font != null) label72.setFont(label72Font);
        label72.setRequestFocusEnabled(false);
        this.$$$loadLabelText$$$(label72, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        panel23.add(label72, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel21.add(panel24, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        gpanoCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(gpanoCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel24.add(gpanoCopyFrombutton);
        gpanoCopyTobutton = new JButton();
        this.$$$loadButtonText$$$(gpanoCopyTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel24.add(gpanoCopyTobutton);
        gpanoResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(gpanoResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel24.add(gpanoResetFieldsbutton);
        gpanoHelpbutton = new JButton();
        gpanoHelpbutton.setLabel("Help");
        this.$$$loadButtonText$$$(gpanoHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel24.add(gpanoHelpbutton);
        gpanoOverwriteOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(gpanoOverwriteOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel21.add(gpanoOverwriteOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        gpanoMinVersionText = new JLabel();
        this.$$$loadLabelText$$$(gpanoMinVersionText, this.$$$getMessageFromBundle$$$("translations/program_strings", "gpano.googlemapsfields"));
        gpanoMinVersionText.setVisible(true);
        panel21.add(gpanoMinVersionText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(20, 3, new Insets(10, 20, 10, 20), -1, -1));
        panel25.setMinimumSize(new Dimension(750, 500));
        panel25.setPreferredSize(new Dimension(800, 550));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.lenstab"), panel25);
        final JLabel label73 = new JLabel();
        this.$$$loadLabelText$$$(label73, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.lensmake"));
        panel25.add(label73, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensmaketextField = new JTextField();
        panel25.add(lensmaketextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel25.add(panel26, new GridConstraints(16, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lensCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(lensCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel26.add(lensCopyFrombutton);
        lensSaveTobutton = new JButton();
        this.$$$loadButtonText$$$(lensSaveTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel26.add(lensSaveTobutton);
        lensResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(lensResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel26.add(lensResetFieldsbutton);
        lensHelpbutton = new JButton();
        lensHelpbutton.setLabel("Help");
        this.$$$loadButtonText$$$(lensHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel26.add(lensHelpbutton);
        final JLabel label74 = new JLabel();
        this.$$$loadLabelText$$$(label74, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.lensmodel"));
        panel25.add(label74, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensmodeltextField = new JTextField();
        panel25.add(lensmodeltextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lensmodelcheckBox = new JCheckBox();
        lensmodelcheckBox.setSelected(true);
        lensmodelcheckBox.setText("");
        panel25.add(lensmodelcheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label75 = new JLabel();
        Font label75Font = this.$$$getFont$$$(null, Font.BOLD, -1, label75.getFont());
        if (label75Font != null) label75.setFont(label75Font);
        this.$$$loadLabelText$$$(label75, this.$$$getMessageFromBundle$$$("translations/program_strings", "label.save"));
        panel25.add(label75, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensmakecheckBox = new JCheckBox();
        lensmakecheckBox.setSelected(true);
        lensmakecheckBox.setText("");
        panel25.add(lensmakecheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label76 = new JLabel();
        this.$$$loadLabelText$$$(label76, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.serialnumber"));
        panel25.add(label76, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensserialnumbertextField = new JTextField();
        panel25.add(lensserialnumbertextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label77 = new JLabel();
        this.$$$loadLabelText$$$(label77, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.focallength"));
        panel25.add(label77, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focallengthtextField = new JTextField();
        panel25.add(focallengthtextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lensserialnumbercheckBox = new JCheckBox();
        lensserialnumbercheckBox.setSelected(true);
        lensserialnumbercheckBox.setText("");
        panel25.add(lensserialnumbercheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focallengthcheckBox = new JCheckBox();
        focallengthcheckBox.setSelected(true);
        focallengthcheckBox.setText("");
        panel25.add(focallengthcheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label78 = new JLabel();
        this.$$$loadLabelText$$$(label78, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.focallength35mm"));
        panel25.add(label78, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focallengthIn35mmformattextField = new JTextField();
        panel25.add(focallengthIn35mmformattextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        focallengthIn35mmformatcheckBox = new JCheckBox();
        focallengthIn35mmformatcheckBox.setSelected(true);
        focallengthIn35mmformatcheckBox.setText("");
        panel25.add(focallengthIn35mmformatcheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label79 = new JLabel();
        this.$$$loadLabelText$$$(label79, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.fnumber"));
        panel25.add(label79, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fnumbertextField = new JTextField();
        panel25.add(fnumbertextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fnumbercheckBox = new JCheckBox();
        fnumbercheckBox.setSelected(true);
        fnumbercheckBox.setText("");
        panel25.add(fnumbercheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label80 = new JLabel();
        this.$$$loadLabelText$$$(label80, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.maxaperturevalue"));
        panel25.add(label80, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxaperturevaluetextField = new JTextField();
        panel25.add(maxaperturevaluetextField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        maxaperturevaluecheckBox = new JCheckBox();
        maxaperturevaluecheckBox.setSelected(true);
        maxaperturevaluecheckBox.setText("");
        panel25.add(maxaperturevaluecheckBox, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label81 = new JLabel();
        this.$$$loadLabelText$$$(label81, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.meteringmode"));
        panel25.add(label81, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meteringmodecomboBox = new JComboBox();
        panel25.add(meteringmodecomboBox, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meteringmodecheckBox = new JCheckBox();
        meteringmodecheckBox.setSelected(true);
        meteringmodecheckBox.setText("");
        panel25.add(meteringmodecheckBox, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label82 = new JLabel();
        this.$$$loadLabelText$$$(label82, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.focusdistance"));
        panel25.add(label82, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusdistancetextField = new JTextField();
        panel25.add(focusdistancetextField, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        focusdistancecheckBox = new JCheckBox();
        focusdistancecheckBox.setSelected(true);
        focusdistancecheckBox.setText("");
        panel25.add(focusdistancecheckBox, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label83 = new JLabel();
        this.$$$loadLabelText$$$(label83, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.lensid"));
        panel25.add(label83, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensidtextField = new JTextField();
        panel25.add(lensidtextField, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lensidcheckBox = new JCheckBox();
        lensidcheckBox.setSelected(true);
        lensidcheckBox.setText("");
        panel25.add(lensidcheckBox, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label84 = new JLabel();
        this.$$$loadLabelText$$$(label84, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.conversionlens"));
        panel25.add(label84, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        conversionlenstextField = new JTextField();
        panel25.add(conversionlenstextField, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        conversionlenscheckBox = new JCheckBox();
        conversionlenscheckBox.setSelected(true);
        conversionlenscheckBox.setText("");
        panel25.add(conversionlenscheckBox, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label85 = new JLabel();
        this.$$$loadLabelText$$$(label85, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.lenstype"));
        panel25.add(label85, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lenstypetextField = new JTextField();
        panel25.add(lenstypetextField, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lenstypecheckBox = new JCheckBox();
        lenstypecheckBox.setSelected(true);
        lenstypecheckBox.setText("");
        panel25.add(lenstypecheckBox, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label86 = new JLabel();
        this.$$$loadLabelText$$$(label86, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.firmwarevers"));
        panel25.add(label86, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensfirmwareversiontextField = new JTextField();
        panel25.add(lensfirmwareversiontextField, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lensfirmwareversioncheckBox = new JCheckBox();
        lensfirmwareversioncheckBox.setSelected(true);
        lensfirmwareversioncheckBox.setText("");
        panel25.add(lensfirmwareversioncheckBox, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lensOverwriteOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(lensOverwriteOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel25.add(lensOverwriteOriginalscheckBox, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        final Spacer spacer3 = new Spacer();
        panel25.add(spacer3, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 25), new Dimension(-1, 25), null, 0, false));
        saveloadlensconfigpanel = new JPanel();
        saveloadlensconfigpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel25.add(saveloadlensconfigpanel, new GridConstraints(19, 0, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveloadlensconfigpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        saveLensConfigurationbutton = new JButton();
        this.$$$loadButtonText$$$(saveLensConfigurationbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.btnconfigsave"));
        saveLensConfigurationbutton.setToolTipText("This allows you to save lens configurations for future use");
        saveloadlensconfigpanel.add(saveLensConfigurationbutton);
        loadLensConfigurationButton = new JButton();
        this.$$$loadButtonText$$$(loadLensConfigurationButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.btnconfigload"));
        saveloadlensconfigpanel.add(loadLensConfigurationButton);
        final Spacer spacer4 = new Spacer();
        panel25.add(spacer4, new GridConstraints(17, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lensSaveLoadConfigLabel = new JLabel();
        this.$$$loadLabelText$$$(lensSaveLoadConfigLabel, this.$$$getMessageFromBundle$$$("translations/program_strings", "lens.saveloadconfig"));
        panel25.add(lensSaveLoadConfigLabel, new GridConstraints(18, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel27 = new JPanel();
        panel27.setLayout(new GridLayoutManager(9, 1, new Insets(10, 20, 10, 20), -1, -1));
        panel27.setMinimumSize(new Dimension(750, 397));
        panel27.setPreferredSize(new Dimension(800, 397));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.xmpiptcstringtab"), panel27);
        StringsTopText = new JLabel();
        StringsTopText.setText("StringstopText");
        panel27.add(StringsTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel28 = new JPanel();
        panel28.setLayout(new GridLayoutManager(2, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel27.add(panel28, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel28.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label87 = new JLabel();
        this.$$$loadLabelText$$$(label87, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.keywords"));
        panel28.add(label87, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(140, -1), null, 0, false));
        StringsKeywordstextField = new JTextField();
        panel28.add(StringsKeywordstextField, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        StringsKeywordsIPTCcheckBox = new JLabel();
        StringsKeywordsIPTCcheckBox.setText("IPTC");
        panel28.add(StringsKeywordsIPTCcheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel29 = new JPanel();
        panel29.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel28.add(panel29, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label88 = new JLabel();
        this.$$$loadLabelText$$$(label88, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.action"));
        panel29.add(label88);
        StringsKeywOverwriteradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsKeywOverwriteradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.overwrite"));
        panel29.add(StringsKeywOverwriteradioButton);
        StringsKeywAppendradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsKeywAppendradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.append"));
        panel29.add(StringsKeywAppendradioButton);
        StringsKeywRemoveradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsKeywRemoveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.remove"));
        panel29.add(StringsKeywRemoveradioButton);
        StringsKeywDontSaveradioButton = new JRadioButton();
        StringsKeywDontSaveradioButton.setSelected(true);
        this.$$$loadButtonText$$$(StringsKeywDontSaveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.dontsave"));
        panel29.add(StringsKeywDontSaveradioButton);
        final JPanel panel30 = new JPanel();
        panel30.setLayout(new GridLayoutManager(2, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel27.add(panel30, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel30.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label89 = new JLabel();
        this.$$$loadLabelText$$$(label89, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.subject"));
        panel30.add(label89, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(140, -1), null, 0, false));
        StringsSubjecttextField = new JTextField();
        panel30.add(StringsSubjecttextField, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label90 = new JLabel();
        label90.setText("XMP");
        panel30.add(label90, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel31 = new JPanel();
        panel31.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel30.add(panel31, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label91 = new JLabel();
        this.$$$loadLabelText$$$(label91, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.action"));
        panel31.add(label91);
        StringsSubjectOverwriteradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsSubjectOverwriteradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.overwrite"));
        panel31.add(StringsSubjectOverwriteradioButton);
        StringsSubjectAppendradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsSubjectAppendradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.append"));
        panel31.add(StringsSubjectAppendradioButton);
        StringsSubjectRemoveradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsSubjectRemoveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.remove"));
        panel31.add(StringsSubjectRemoveradioButton);
        StringsSubjectDontSaveradioButton = new JRadioButton();
        StringsSubjectDontSaveradioButton.setSelected(true);
        this.$$$loadButtonText$$$(StringsSubjectDontSaveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.dontsave"));
        panel31.add(StringsSubjectDontSaveradioButton);
        final JPanel panel32 = new JPanel();
        panel32.setLayout(new GridLayoutManager(2, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel27.add(panel32, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel32.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label92 = new JLabel();
        this.$$$loadLabelText$$$(label92, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.pim"));
        panel32.add(label92, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(140, -1), null, 0, false));
        StringsPIItextField = new JTextField();
        panel32.add(StringsPIItextField, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        StringsIIPXmpcheckBox = new JLabel();
        StringsIIPXmpcheckBox.setText("XMP");
        panel32.add(StringsIIPXmpcheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel33 = new JPanel();
        panel33.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel32.add(panel33, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label93 = new JLabel();
        this.$$$loadLabelText$$$(label93, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.action"));
        panel33.add(label93);
        StringsIIPOverwriteradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsIIPOverwriteradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.overwrite"));
        panel33.add(StringsIIPOverwriteradioButton);
        StringsIIPAppendradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsIIPAppendradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.append"));
        panel33.add(StringsIIPAppendradioButton);
        StringsIIPRemoveradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(StringsIIPRemoveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.remove"));
        panel33.add(StringsIIPRemoveradioButton);
        StringsIIPDontSaveradioButton = new JRadioButton();
        StringsIIPDontSaveradioButton.setSelected(true);
        this.$$$loadButtonText$$$(StringsIIPDontSaveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.dontsave"));
        panel33.add(StringsIIPDontSaveradioButton);
        stringPlusOverwriteOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(stringPlusOverwriteOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel27.add(stringPlusOverwriteOriginalscheckBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        final JPanel panel34 = new JPanel();
        panel34.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel27.add(panel34, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stringPlusCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(stringPlusCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel34.add(stringPlusCopyFrombutton);
        stringPlusSaveTobutton = new JButton();
        this.$$$loadButtonText$$$(stringPlusSaveTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel34.add(stringPlusSaveTobutton);
        stringPlusResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(stringPlusResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        panel34.add(stringPlusResetFieldsbutton);
        stringPlusHelpbutton = new JButton();
        stringPlusHelpbutton.setLabel("Help");
        stringPlusHelpbutton.setText("Help");
        stringPlusHelpbutton.setVisible(false);
        panel34.add(stringPlusHelpbutton);
        final Spacer spacer5 = new Spacer();
        panel27.add(spacer5, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        SeparatorText = new JLabel();
        SeparatorText.setText("Separator");
        panel27.add(SeparatorText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel35 = new JPanel();
        panel35.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel27.add(panel35, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label94 = new JLabel();
        this.$$$loadLabelText$$$(label94, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.separator"));
        panel35.add(label94);
        radioButtonComma = new JRadioButton();
        radioButtonComma.setSelected(true);
        this.$$$loadButtonText$$$(radioButtonComma, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.sepcomma"));
        panel35.add(radioButtonComma);
        radioButtonColon = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtonColon, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.sepcolon"));
        panel35.add(radioButtonColon);
        radioButtonSemiColon = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtonSemiColon, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.sepsemicolon"));
        panel35.add(radioButtonSemiColon);
        radioButtonHash = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtonHash, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.sephash"));
        panel35.add(radioButtonHash);
        radioButtonOther = new JRadioButton();
        this.$$$loadButtonText$$$(radioButtonOther, this.$$$getMessageFromBundle$$$("translations/program_strings", "xis.sepother"));
        panel35.add(radioButtonOther);
        textFieldOtherSeparator = new JTextField();
        textFieldOtherSeparator.setPreferredSize(new Dimension(25, 38));
        panel35.add(textFieldOtherSeparator);
        final JPanel panel36 = new JPanel();
        panel36.setLayout(new GridLayoutManager(1, 1, new Insets(10, 20, 10, 20), -1, -1));
        panel36.setMinimumSize(new Dimension(750, 204));
        panel36.setPreferredSize(new Dimension(800, 550));
        tabbedPaneEditfunctions.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "ed.usercombis"), panel36);
        final JPanel panel37 = new JPanel();
        panel37.setLayout(new GridLayoutManager(5, 1, new Insets(0, 5, 0, 5), -1, -1));
        panel36.add(panel37, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel38 = new JPanel();
        panel38.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel37.add(panel38, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label95 = new JLabel();
        this.$$$loadLabelText$$$(label95, this.$$$getMessageFromBundle$$$("translations/program_strings", "udc.sets"));
        panel38.add(label95);
        UserCombiscomboBox = new JComboBox();
        panel38.add(UserCombiscomboBox);
        udcCreateNewButton = new JButton();
        this.$$$loadButtonText$$$(udcCreateNewButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "udc.crbutton"));
        panel38.add(udcCreateNewButton);
        CustomConfiglabel = new JLabel();
        CustomConfiglabel.setText("");
        CustomConfiglabel.setVisible(false);
        panel38.add(CustomConfiglabel);
        UserCombiScrollPane = new JScrollPane();
        panel37.add(UserCombiScrollPane, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(700, -1), new Dimension(800, -1), null, 0, false));
        UserCombiTable = new JTable();
        UserCombiScrollPane.setViewportView(UserCombiTable);
        final JPanel panel39 = new JPanel();
        panel39.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel37.add(panel39, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        udcCopyFrombutton = new JButton();
        this.$$$loadButtonText$$$(udcCopyFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel39.add(udcCopyFrombutton);
        udcSaveTobutton = new JButton();
        this.$$$loadButtonText$$$(udcSaveTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel39.add(udcSaveTobutton);
        udcResetFieldsbutton = new JButton();
        this.$$$loadButtonText$$$(udcResetFieldsbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.resetfields"));
        udcResetFieldsbutton.setVisible(false);
        panel39.add(udcResetFieldsbutton);
        udcHelpbutton = new JButton();
        udcHelpbutton.setLabel("Help");
        this.$$$loadButtonText$$$(udcHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel39.add(udcHelpbutton);
        UserCombiTopText = new JLabel();
        this.$$$loadLabelText$$$(UserCombiTopText, this.$$$getMessageFromBundle$$$("translations/program_strings", "udc.toptext"));
        panel37.add(UserCombiTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel40 = new JPanel();
        panel40.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel37.add(panel40, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        udcOverwriteOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(udcOverwriteOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel40.add(udcOverwriteOriginalscheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel40.add(spacer6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel41 = new JPanel();
        panel41.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneRight.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "maintab.copydata"), panel41);
        CopyDatatabbedPane = new JTabbedPane();
        panel41.add(CopyDatatabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel42 = new JPanel();
        panel42.setLayout(new GridLayoutManager(1, 1, new Insets(10, 0, 0, 0), -1, -1));
        CopyDatatabbedPane.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "cd.copyfromto"), panel42);
        final JPanel panel43 = new JPanel();
        panel43.setLayout(new GridLayoutManager(6, 1, new Insets(10, 10, 5, 5), -1, -1));
        panel43.setPreferredSize(new Dimension(800, 500));
        panel42.add(panel43, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        copyAllMetadataRadiobutton = new JRadioButton();
        this.$$$loadButtonText$$$(copyAllMetadataRadiobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyallmetadatasame"));
        panel43.add(copyAllMetadataRadiobutton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyAllMetadataSameGroupsRadiobutton = new JRadioButton();
        copyAllMetadataSameGroupsRadiobutton.setText("<html>Copy the values of all writable tags from the source image to the target image(s), preserving the original tag groups</html>");
        panel43.add(copyAllMetadataSameGroupsRadiobutton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copySelectiveMetadataradioButton = new JRadioButton();
        copySelectiveMetadataradioButton.setSelected(true);
        copySelectiveMetadataradioButton.setText("Copy metadata using below mentioned selective group options");
        panel43.add(copySelectiveMetadataradioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel44 = new JPanel();
        panel44.setLayout(new GridLayoutManager(7, 1, new Insets(10, 0, 15, 0), -1, -1));
        panel43.add(panel44, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel44.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        CopyExifcheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyExifcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyexifcheckbox"));
        panel44.add(CopyExifcheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyXmpcheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyXmpcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyxmpcheckbox"));
        panel44.add(CopyXmpcheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyIptccheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyIptccheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyiptccheckbox"));
        panel44.add(CopyIptccheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyIcc_profileDataCheckBox = new JCheckBox();
        CopyIcc_profileDataCheckBox.setActionCommand("");
        this.$$$loadButtonText$$$(CopyIcc_profileDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyicc_profilecheckbox"));
        panel44.add(CopyIcc_profileDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyGpsCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyGpsCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copygpscheckbox"));
        panel44.add(CopyGpsCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyJfifcheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyJfifcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyjfifcheckbox"));
        panel44.add(CopyJfifcheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CopyMakernotescheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyMakernotescheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copymakernotescheckbox"));
        panel44.add(CopyMakernotescheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BackupOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(BackupOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel43.add(BackupOriginalscheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel45 = new JPanel();
        panel45.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel43.add(panel45, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UseDataFrombutton = new JButton();
        this.$$$loadButtonText$$$(UseDataFrombutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.copyfrom"));
        panel45.add(UseDataFrombutton);
        CopyDataCopyTobutton = new JButton();
        this.$$$loadButtonText$$$(CopyDataCopyTobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel45.add(CopyDataCopyTobutton);
        CopyHelpbutton = new JButton();
        this.$$$loadButtonText$$$(CopyHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel45.add(CopyHelpbutton);
        final JPanel panel46 = new JPanel();
        panel46.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        CopyDatatabbedPane.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "cd.copyinimage"), panel46);
        copyAllMetdataToXMPRadioButton = new JRadioButton();
        copyAllMetdataToXMPRadioButton.setSelected(false);
        this.$$$loadButtonText$$$(copyAllMetdataToXMPRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.copyallmetadatatoxmpformat"));
        panel46.add(copyAllMetdataToXMPRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel47 = new JPanel();
        panel47.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel46.add(panel47, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel47.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        CopyArgsRadioButton = new JRadioButton();
        CopyArgsRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(CopyArgsRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.fromgrouptogroup"));
        panel47.add(CopyArgsRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel48 = new JPanel();
        panel48.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel47.add(panel48, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        final JPanel panel49 = new JPanel();
        panel49.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel48.add(panel49, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel49.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagsToXmpradioButton = new JRadioButton();
        tagsToXmpradioButton.setSelected(true);
        this.$$$loadButtonText$$$(tagsToXmpradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.tagstoxmp"));
        panel49.add(tagsToXmpradioButton);
        final Spacer spacer7 = new Spacer();
        panel49.add(spacer7);
        final Spacer spacer8 = new Spacer();
        panel49.add(spacer8);
        exif2xmpCheckBox = new JCheckBox();
        exif2xmpCheckBox.setText("exif2xmp");
        panel49.add(exif2xmpCheckBox);
        gps2xmpCheckBox = new JCheckBox();
        gps2xmpCheckBox.setText("gps2xmp");
        panel49.add(gps2xmpCheckBox);
        iptc2xmpCheckBox = new JCheckBox();
        iptc2xmpCheckBox.setText("iptc2xmp");
        panel49.add(iptc2xmpCheckBox);
        pdf2xmpCheckBox = new JCheckBox();
        pdf2xmpCheckBox.setText("pdf2xmp");
        panel49.add(pdf2xmpCheckBox);
        final Spacer spacer9 = new Spacer();
        panel48.add(spacer9, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel50 = new JPanel();
        panel50.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel48.add(panel50, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel50.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagsToExifradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tagsToExifradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.tagstoexif"));
        panel50.add(tagsToExifradioButton);
        final Spacer spacer10 = new Spacer();
        panel50.add(spacer10);
        final Spacer spacer11 = new Spacer();
        panel50.add(spacer11);
        iptc2exifCheckBox = new JCheckBox();
        iptc2exifCheckBox.setText("iptc2exif");
        panel50.add(iptc2exifCheckBox);
        xmp2exifCheckBox = new JCheckBox();
        xmp2exifCheckBox.setText("xmp2exif");
        panel50.add(xmp2exifCheckBox);
        final JPanel panel51 = new JPanel();
        panel51.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel48.add(panel51, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel51.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagsToIptcradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tagsToIptcradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.tagstoiptc"));
        panel51.add(tagsToIptcradioButton);
        final Spacer spacer12 = new Spacer();
        panel51.add(spacer12);
        final Spacer spacer13 = new Spacer();
        panel51.add(spacer13);
        exif2iptcCheckBox = new JCheckBox();
        exif2iptcCheckBox.setText("exif2iptc");
        panel51.add(exif2iptcCheckBox);
        xmp2iptcCheckBox = new JCheckBox();
        xmp2iptcCheckBox.setText("xmp2iptc");
        panel51.add(xmp2iptcCheckBox);
        final JPanel panel52 = new JPanel();
        panel52.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel48.add(panel52, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel52.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagsToGpsradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tagsToGpsradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.tagstogps"));
        panel52.add(tagsToGpsradioButton);
        final Spacer spacer14 = new Spacer();
        panel52.add(spacer14);
        final Spacer spacer15 = new Spacer();
        panel52.add(spacer15);
        xmp2gpsCheckBox = new JCheckBox();
        xmp2gpsCheckBox.setText("xmp2gps");
        panel52.add(xmp2gpsCheckBox);
        final JPanel panel53 = new JPanel();
        panel53.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel48.add(panel53, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel53.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagsToPdfradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tagsToPdfradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "copyd.tagstopdf"));
        panel53.add(tagsToPdfradioButton);
        final Spacer spacer16 = new Spacer();
        panel53.add(spacer16);
        final Spacer spacer17 = new Spacer();
        panel53.add(spacer17);
        xmp2pdfCheckBox = new JCheckBox();
        xmp2pdfCheckBox.setText("xmp2pdf");
        panel53.add(xmp2pdfCheckBox);
        CopyInsideImageMakeCopyOfOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CopyInsideImageMakeCopyOfOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel46.add(CopyInsideImageMakeCopyOfOriginalscheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel54 = new JPanel();
        panel54.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel46.add(panel54, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        copyInsideSaveDataTo = new JButton();
        this.$$$loadButtonText$$$(copyInsideSaveDataTo, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.saveto"));
        panel54.add(copyInsideSaveDataTo);
        final JButton button1 = new JButton();
        this.$$$loadButtonText$$$(button1, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel54.add(button1);
        final JPanel panel55 = new JPanel();
        panel55.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneRight.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "maintab.exportimport"), panel55);
        tabbedPaneExportImport = new JTabbedPane();
        panel55.add(tabbedPaneExportImport, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel56 = new JPanel();
        panel56.setLayout(new GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPaneExportImport.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "expimp.generalexp"), panel56);
        final JPanel panel57 = new JPanel();
        panel57.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel56.add(panel57, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel58 = new JPanel();
        panel58.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel57.add(panel58, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label96 = new JLabel();
        this.$$$loadLabelText$$$(label96, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportoption"));
        panel58.add(label96, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        catmetadataradioButton = new JRadioButton();
        catmetadataradioButton.setSelected(true);
        this.$$$loadButtonText$$$(catmetadataradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.catmetadata"));
        panel58.add(catmetadataradioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportFromUserCombisradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(exportFromUserCombisradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.usercombi"));
        panel58.add(exportFromUserCombisradioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel59 = new JPanel();
        panel59.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel58.add(panel59, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel59.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        exportAllMetadataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportAllMetadataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportall"));
        panel59.add(exportAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel60 = new JPanel();
        panel60.setLayout(new GridLayoutManager(5, 1, new Insets(0, 15, 0, 0), -1, -1));
        panel59.add(panel60, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        exportExifDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportExifDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportexif"));
        panel60.add(exportExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportXmpDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportXmpDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.extportxmp"));
        panel60.add(exportXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportGpsDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportGpsDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportgps"));
        panel60.add(exportGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportIptcDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportIptcDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportiptc"));
        panel60.add(exportIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportICCDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportICCDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exporticc"));
        panel60.add(exportICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel61 = new JPanel();
        panel61.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel58.add(panel61, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel61.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        exportUserCombicomboBox = new JComboBox();
        panel61.add(exportUserCombicomboBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel62 = new JPanel();
        panel62.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel56.add(panel62, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel63 = new JPanel();
        panel63.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel62.add(panel63, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label97 = new JLabel();
        this.$$$loadLabelText$$$(label97, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportto"));
        panel63.add(label97, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel63.add(spacer18, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel64 = new JPanel();
        panel64.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel63.add(panel64, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtRadioButton = new JRadioButton();
        txtRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(txtRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.txt"));
        panel64.add(txtRadioButton);
        tabRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tabRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.tab"));
        panel64.add(tabRadioButton);
        xmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xml"));
        panel64.add(xmlRadioButton);
        htmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(htmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.html"));
        panel64.add(htmlRadioButton);
        XMPRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(XMPRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xmp"));
        XMPRadioButton.setVisible(false);
        panel64.add(XMPRadioButton);
        csvRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(csvRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.csv"));
        panel64.add(csvRadioButton);
        final JPanel panel65 = new JPanel();
        panel65.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel56.add(panel65, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GenExportbuttonOK = new JButton();
        this.$$$loadButtonText$$$(GenExportbuttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.export"));
        panel65.add(GenExportbuttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        GenExportbuttonCancel = new JButton();
        this.$$$loadButtonText$$$(GenExportbuttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel65.add(GenExportbuttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer19 = new Spacer();
        panel65.add(spacer19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        GenExpuseMetadataTagLanguageCheckBoxport = new JCheckBox();
        GenExpuseMetadataTagLanguageCheckBoxport.setSelected(true);
        this.$$$loadButtonText$$$(GenExpuseMetadataTagLanguageCheckBoxport, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.uselang"));
        panel56.add(GenExpuseMetadataTagLanguageCheckBoxport, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportMetaDataUiText = new JLabel();
        exportMetaDataUiText.setEnabled(true);
        exportMetaDataUiText.setRequestFocusEnabled(false);
        exportMetaDataUiText.setText("exportMetaDataUiText");
        exportMetaDataUiText.setVerifyInputWhenFocusTarget(false);
        panel56.add(exportMetaDataUiText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel66 = new JPanel();
        panel66.setLayout(new GridLayoutManager(7, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPaneExportImport.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "expimp.exptopdf"), panel66);
        expPdftextLabel = new JLabel();
        expPdftextLabel.setText("expPdftextLabell");
        panel66.add(expPdftextLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final Spacer spacer20 = new Spacer();
        panel66.add(spacer20, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel67 = new JPanel();
        panel67.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panel66.add(panel67, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExpPDFOKbutton = new JButton();
        this.$$$loadButtonText$$$(ExpPDFOKbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.export"));
        panel67.add(ExpPDFOKbutton);
        final JPanel panel68 = new JPanel();
        panel68.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel66.add(panel68, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel68.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label98 = new JLabel();
        this.$$$loadLabelText$$$(label98, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.paper"));
        label98.setVisible(false);
        panel68.add(label98, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LetterradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(LetterradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.letter"));
        LetterradioButton.setVisible(false);
        panel68.add(LetterradioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        A4radioButton = new JRadioButton();
        A4radioButton.setSelected(true);
        this.$$$loadButtonText$$$(A4radioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.a4"));
        A4radioButton.setVisible(false);
        panel68.add(A4radioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label99 = new JLabel();
        this.$$$loadLabelText$$$(label99, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.output"));
        panel68.add(label99, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pdfPerImgradioButton = new JRadioButton();
        pdfPerImgradioButton.setSelected(true);
        this.$$$loadButtonText$$$(pdfPerImgradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.single"));
        panel68.add(pdfPerImgradioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pdfCombinedradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(pdfCombinedradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.combined"));
        panel68.add(pdfCombinedradioButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel69 = new JPanel();
        panel69.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel66.add(panel69, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel69.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        exppdfDataText = new JLabel();
        this.$$$loadLabelText$$$(exppdfDataText, this.$$$getMessageFromBundle$$$("translations/program_strings", "exppdf.whichdata"));
        panel69.add(exppdfDataText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer21 = new Spacer();
        panel69.add(spacer21, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel70 = new JPanel();
        panel70.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
        panel69.add(panel70, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pdfradioButtonExpAll = new JRadioButton();
        pdfradioButtonExpAll.setSelected(true);
        this.$$$loadButtonText$$$(pdfradioButtonExpAll, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.allradiobutton"));
        panel70.add(pdfradioButtonExpAll);
        pdfradioButtonExpCommonTags = new JRadioButton();
        this.$$$loadButtonText$$$(pdfradioButtonExpCommonTags, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.commontags"));
        panel70.add(pdfradioButtonExpCommonTags);
        pdfcomboBoxExpCommonTags = new JComboBox();
        panel70.add(pdfcomboBoxExpCommonTags);
        pdfradioButtonExpByTagName = new JRadioButton();
        this.$$$loadButtonText$$$(pdfradioButtonExpByTagName, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.bygroup"));
        panel70.add(pdfradioButtonExpByTagName);
        pdfcomboBoxExpByTagName = new JComboBox();
        panel70.add(pdfcomboBoxExpByTagName);
        pdfLabelSupported = new JLabel();
        pdfLabelSupported.setText("Label");
        panel66.add(pdfLabelSupported, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel71 = new JPanel();
        panel71.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPaneExportImport.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "mmenu.exportsidecar"), panel71);
        final JPanel panel72 = new JPanel();
        panel72.setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));
        panel71.add(panel72, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exifSCradioButton = new JRadioButton();
        exifSCradioButton.setSelected(true);
        this.$$$loadButtonText$$$(exifSCradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "esc.exiftitle"));
        panel72.add(exifSCradioButton, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer22 = new Spacer();
        panel72.add(spacer22, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        xmpSCradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmpSCradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "esc.xmptitle"));
        panel72.add(xmpSCradioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mieSCradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(mieSCradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "esc.mietitle"));
        panel72.add(mieSCradioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exvSCradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(exvSCradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "esc.exvtitle"));
        panel72.add(exvSCradioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel73 = new JPanel();
        panel73.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel71.add(panel73, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        SidecarExportButton = new JButton();
        this.$$$loadButtonText$$$(SidecarExportButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.export"));
        panel73.add(SidecarExportButton);
        SideCarHelpbutton = new JButton();
        this.$$$loadButtonText$$$(SideCarHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel73.add(SideCarHelpbutton);
        final Spacer spacer23 = new Spacer();
        panel71.add(spacer23, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel74 = new JPanel();
        panel74.setLayout(new GridLayoutManager(5, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel55.add(panel74, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        final JLabel label100 = new JLabel();
        Font label100Font = this.$$$getFont$$$(null, Font.BOLD, -1, label100.getFont());
        if (label100Font != null) label100.setFont(label100Font);
        this.$$$loadLabelText$$$(label100, this.$$$getMessageFromBundle$$$("translations/program_strings", "fld.imagefolder"));
        panel74.add(label100, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel75 = new JPanel();
        panel75.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel74.add(panel75, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        ExpImgFoldertextField = new JTextField();
        ExpImgFoldertextField.setPreferredSize(new Dimension(500, 25));
        panel75.add(ExpImgFoldertextField);
        ExpBrowseButton = new JButton();
        this.$$$loadButtonText$$$(ExpBrowseButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.browse"));
        panel75.add(ExpBrowseButton);
        ExpLeaveFolderEmptyLabel = new JLabel();
        ExpLeaveFolderEmptyLabel.setText("ExpLeaveFolderEmptyLabel");
        panel74.add(ExpLeaveFolderEmptyLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(650, -1), null, 1, false));
        genExpfolderBrowseLabel = new JLabel();
        genExpfolderBrowseLabel.setText("genExpfolderBrowseLabel");
        panel74.add(genExpfolderBrowseLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        includeSubFoldersCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(includeSubFoldersCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "fld.inclsubfolders"));
        panel74.add(includeSubFoldersCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel76 = new JPanel();
        panel76.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        panel76.setPreferredSize(new Dimension(800, -1));
        tabbedPaneRight.addTab(this.$$$getMessageFromBundle$$$("translations/program_strings", "maintab.exiftoolcommands"), panel76);
        final JPanel panel77 = new JPanel();
        panel77.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel76.add(panel77, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        CommandsParameterstextField = new JTextField();
        panel77.add(CommandsParameterstextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label101 = new JLabel();
        Font label101Font = this.$$$getFont$$$(null, Font.BOLD, -1, label101.getFont());
        if (label101Font != null) label101.setFont(label101Font);
        this.$$$loadLabelText$$$(label101, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.parameters"));
        panel77.add(label101, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel78 = new JPanel();
        panel78.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel77.add(panel78, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        CommandsclearParameterSFieldButton = new JButton();
        this.$$$loadButtonText$$$(CommandsclearParameterSFieldButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnclrparamfield"));
        panel78.add(CommandsclearParameterSFieldButton);
        CommandsclearOutputFieldButton = new JButton();
        this.$$$loadButtonText$$$(CommandsclearOutputFieldButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnclroutput"));
        panel78.add(CommandsclearOutputFieldButton);
        CommandsgoButton = new JButton();
        this.$$$loadButtonText$$$(CommandsgoButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btngo"));
        panel78.add(CommandsgoButton);
        CommandshelpButton = new JButton();
        this.$$$loadButtonText$$$(CommandshelpButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel78.add(CommandshelpButton);
        final Spacer spacer24 = new Spacer();
        panel78.add(spacer24);
        final JPanel panel79 = new JPanel();
        panel79.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel78.add(panel79);
        final Spacer spacer25 = new Spacer();
        panel79.add(spacer25);
        AddCommandFavoritebutton = new JButton();
        this.$$$loadButtonText$$$(AddCommandFavoritebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnaddfav"));
        panel79.add(AddCommandFavoritebutton);
        LoadCommandFavoritebutton = new JButton();
        this.$$$loadButtonText$$$(LoadCommandFavoritebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnloadfav"));
        panel79.add(LoadCommandFavoritebutton);
        final JPanel panel80 = new JPanel();
        panel80.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel77.add(panel80, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label102 = new JLabel();
        this.$$$loadLabelText$$$(label102, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.labeloutput"));
        label102.setVerticalTextPosition(1);
        panel80.add(label102, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel80.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        YourCommandsOutputText = new JEditorPane();
        YourCommandsOutputText.setText("");
        scrollPane1.setViewportView(YourCommandsOutputText);
        final JPanel panel81 = new JPanel();
        panel81.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));
        panel80.add(panel81, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        UseNonPropFontradioButton = new JRadioButton();
        UseNonPropFontradioButton.setSelected(true);
        this.$$$loadButtonText$$$(UseNonPropFontradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.radbtnmonospace"));
        panel81.add(UseNonPropFontradioButton);
        UsePropFontradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(UsePropFontradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.radbtnproportional"));
        panel81.add(UsePropFontradioButton);
        MyCommandsText = new JLabel();
        MyCommandsText.setText("MyCommandsText");
        panel76.add(MyCommandsText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel82 = new JPanel();
        panel82.setLayout(new GridLayoutManager(5, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel76.add(panel82, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel82.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label103 = new JLabel();
        Font label103Font = this.$$$getFont$$$(null, Font.BOLD, -1, label103.getFont());
        if (label103Font != null) label103.setFont(label103Font);
        this.$$$loadLabelText$$$(label103, this.$$$getMessageFromBundle$$$("translations/program_strings", "fld.imagefolder"));
        panel82.add(label103, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel83 = new JPanel();
        panel83.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel82.add(panel83, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        ETCommandsFoldertextField = new JTextField();
        ETCommandsFoldertextField.setPreferredSize(new Dimension(500, 25));
        panel83.add(ETCommandsFoldertextField);
        ETCBrowseButton = new JButton();
        this.$$$loadButtonText$$$(ETCBrowseButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.browse"));
        panel83.add(ETCBrowseButton);
        etcLeaveFolderEmptyLabel = new JLabel();
        etcLeaveFolderEmptyLabel.setText("etcLeaveFolderEmptyLabel");
        panel82.add(etcLeaveFolderEmptyLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(650, -1), null, 1, false));
        etcFolderBrowseLabel = new JLabel();
        etcFolderBrowseLabel.setText("etcExpfolderBrowseLabel");
        panel82.add(etcFolderBrowseLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        etcIncludeSubFoldersCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(etcIncludeSubFoldersCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "fld.inclsubfolders"));
        panel82.add(etcIncludeSubFoldersCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(CalcNorthRadioButton);
        buttonGroup.add(CalcSouthRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(CalcEastradioButton);
        buttonGroup.add(CalcWestRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(copyAllMetadataSameGroupsRadiobutton);
        buttonGroup.add(copySelectiveMetadataradioButton);
        buttonGroup.add(copyAllMetadataRadiobutton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(UseNonPropFontradioButton);
        buttonGroup.add(UsePropFontradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonViewAll);
        buttonGroup.add(radioButtoncommonTags);
        buttonGroup.add(radioButtonByTagName);
        buttonGroup.add(radioButtonCameraMakes);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(StringsKeywOverwriteradioButton);
        buttonGroup.add(StringsKeywAppendradioButton);
        buttonGroup.add(StringsKeywRemoveradioButton);
        buttonGroup.add(StringsKeywDontSaveradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(StringsSubjectOverwriteradioButton);
        buttonGroup.add(StringsSubjectAppendradioButton);
        buttonGroup.add(StringsSubjectRemoveradioButton);
        buttonGroup.add(StringsSubjectDontSaveradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(StringsIIPOverwriteradioButton);
        buttonGroup.add(StringsIIPAppendradioButton);
        buttonGroup.add(StringsIIPRemoveradioButton);
        buttonGroup.add(StringsIIPDontSaveradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(copyAllMetdataToXMPRadioButton);
        buttonGroup.add(CopyArgsRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(tagsToExifradioButton);
        buttonGroup.add(tagsToXmpradioButton);
        buttonGroup.add(tagsToIptcradioButton);
        buttonGroup.add(tagsToGpsradioButton);
        buttonGroup.add(tagsToPdfradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(exportFromUserCombisradioButton);
        buttonGroup.add(exportFromUserCombisradioButton);
        buttonGroup.add(catmetadataradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(txtRadioButton);
        buttonGroup.add(tabRadioButton);
        buttonGroup.add(xmlRadioButton);
        buttonGroup.add(htmlRadioButton);
        buttonGroup.add(XMPRadioButton);
        buttonGroup.add(csvRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(A4radioButton);
        buttonGroup.add(LetterradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(pdfPerImgradioButton);
        buttonGroup.add(pdfCombinedradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(pdfradioButtonExpAll);
        buttonGroup.add(pdfradioButtonExpCommonTags);
        buttonGroup.add(pdfradioButtonExpByTagName);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(exifSCradioButton);
        buttonGroup.add(xmpSCradioButton);
        buttonGroup.add(mieSCradioButton);
        buttonGroup.add(exvSCradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonComma);
        buttonGroup.add(radioButtonColon);
        buttonGroup.add(radioButtonSemiColon);
        buttonGroup.add(radioButtonHash);
        buttonGroup.add(radioButtonOther);
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    // endregion

    // region Action Listeners and radio button groups
    /*
    / The SpecialMenuActionListener is a menu listener for the special function loadImages that is so tightly integrated
    / with the Gui that we can't take it out.
     */
    public class SpecialMenuActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            String[] dummy = null;
            logger.info("Selected: {}", ev.getActionCommand());

            switch (ev.getActionCommand()) {
                case "Load Images":
                    logger.debug("menu File -> Load Images pressed");
                    // identical to button "Load Images"
                    files = Utils.loadImages("images", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
                    break;
                case "Load Directory":
                    logger.debug("menu File -> Load Folder pressed");
                    // identical to button "Load Directory"
                    files = Utils.loadImages("folder", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
                    break;
                default:
                    break;
            }
        }
    }



    /**
     * Add a popup menu to the left tablelistfiles table to handle insertion and
     * deletion of rows.
     */
    public void createPopupMenu(JPopupMenu myPopupMenu) {

        //JPopupMenu myPopupMenu = new JPopupMenu();
        // Menu items.
        JMenuItem menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loaddirectory")); // index 0
        menuItem.addActionListener(new MyPopupMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loadimages"));           // index 1
        menuItem.addActionListener(new MyPopupMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.displayimages"));           // index 2
        menuItem.addActionListener(new MyPopupMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.compareimgs"));           // index 3
        menuItem.addActionListener(new MyPopupMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.slideshow"));           // index 4
        menuItem.addActionListener(new MyPopupMenuHandler());
        myPopupMenu.add(menuItem);

        tableListfiles.addMouseListener(new MyPopupListener());
    }

    /**
     * Listen for popup menu invocation.
     * Need both mousePressed and mouseReleased for cross platform support.
     */
    public class MyPopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // Enable or disable the "Display Image" and "Sildeshow" menu item
                // depending on whether a row is selected.
                myPopupMenu.getComponent(2).setEnabled(
                        tableListfiles.getSelectedRowCount() > 0);
                myPopupMenu.getComponent(4).setEnabled(
                        tableListfiles.getSelectedRowCount() > 0);
                // Enable or disable "Compare images" >= 2 rows selected
                myPopupMenu.getComponent(3).setEnabled(
                        tableListfiles.getSelectedRowCount() > 1);

                myPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Handle popup menu commands.
     */
    class MyPopupMenuHandler implements ActionListener {
        /**
         * Popup menu actions.
         *
         * @param e the menu event.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loaddirectory"))) {

            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loadimages"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.displayimages"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.compareimgs"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.slideshow"))) {
                //loadImages(e);
            }
        }
    }


    private void leftPanePopupMenuLister() {
        //JPopupMenu myPopupMenu = new JPopupMenu();
        //LeftPanePopupMenuListener lpppml = new LeftPanePopupMenuListener(tableListfiles, myPopupMenu);
        //addPopupMenu();
    }


    /*
    / the programButtonListeners functions brings all buttons from the main screen together in one method.
    / Where possible, the actionlistener will be put externally to the GuiActionListeners class.
    / Only the buttons that are too "intimately" coupled to the Gui and can't be moved, will be dealt with
    / directly in this method. Unfortunately that are quite some buttons.
     */
    private void programButtonListeners() {

        ButtonsActionListener gal = new ButtonsActionListener(rootPanel, OutputLabel, CommandsParameterstextField, geotaggingImgFoldertextField, geotaggingGPSLogtextField, UserCombiscomboBox, ExpImgFoldertextField, ETCommandsFoldertextField);
        selectedIndicesList = MyVariables.getselectedIndicesList();

        // Main screen left panel
        buttonLoadImages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button buttonLoadImages pressed");
                //File opener: Load the images; identical to Menu option Load Images.
                files = Utils.loadImages("images", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
            }
        });
        buttonLoadDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button buttonLoadFolder pressed");
                //File opener: Load folder with images; identical to Menu option Load Directory.
                files = Utils.loadImages("folder", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
            }
        });
        buttonShowImage.setActionCommand("bSI");
        buttonShowImage.addActionListener(gal);

        //buttonCompare.setActionCommand("bCo");
        //buttonCompare.addActionListener(gal);
        buttonCompare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                logger.info("buttonCompare pressed");
                if (selectedIndicesList.size() > 25) {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.max25imgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.max25imgs"), JOptionPane.WARNING_MESSAGE);
                } else if ( (selectedIndicesList != null) && (selectedIndicesList.size() > 0) ) {
                    //List<String[]> allMetadata = CompareImages.CompareImages(selectedIndicesList, whichRBselected(), progressBar, OutputLabel);
                    CompareImages.CompareImages(selectedIndicesList, whichRBselected(), progressBar, OutputLabel);
                    //CompareImagesWindow.Initialize(allMetadata);
                }else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        //buttonSearchMetadata.setActionCommand("SearchMetadata");
        //buttonSearchMetadata.addActionListener(gal);
        buttonSearchMetadata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("buttonSearchMetadata pressed");
                // Works on loaded images, not on selected images
                SearchMetadataDialog SMD = new SearchMetadataDialog();
                String searchPhrase = SMD.displayDialog(rootPanel);
                if ( (searchPhrase != null) && !(searchPhrase.isEmpty()) ) {
                    MyVariables.setSearchPhrase(searchPhrase);
                    logger.debug("searchPhrase: {}", searchPhrase);
                    List<String> result = SearchMetaData.searchMetaData(rootPanel, searchPhrase);
                    if ( (result != null) && !(result.isEmpty())) {
                        for (String line : result) {
                            logger.debug("found key or value {}",line);
                        }
                        FoundMetaData FMD = new FoundMetaData();
                        FMD.showDialog(rootPanel, result);
                        // Very dirty way of getting results from another class to see if we need to relaod the images from the search result
                        if (MyVariables.getreloadImagesFromSearchResult()) {
                            Utils.loadImages("reloadfromsearchresult", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, (ResourceBundle.getBundle("translations/program_strings").getString("smd.nothingfoundtxt") + " \"" + searchPhrase) + "\".", ResourceBundle.getBundle("translations/program_strings").getString("smd.nothingfoundtitle"), JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        leftCheckBoxBarHelpButton.setActionCommand("lCBBHB");
        leftCheckBoxBarHelpButton.addActionListener(gal);

        // Your Commands pane buttons
        CommandsclearParameterSFieldButton.setActionCommand("CommandsclearPSFB");
        CommandsclearParameterSFieldButton.addActionListener(gal);
        CommandsclearOutputFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                YourCommandsOutputText.setText("");
            }
        });
        ETCBrowseButton.setActionCommand("etCmdBtn");
        ETCBrowseButton.addActionListener(gal);
        CommandsgoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) || (!("".equals(ETCommandsFoldertextField.getText()))) ) {
                    if (CommandsParameterstextField.getText().length() > 0) {
                        OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.yourcommands"));
                        YourCmnds.executeCommands(CommandsParameterstextField.getText(), YourCommandsOutputText, UseNonPropFontradioButton, progressBar, ETCommandsFoldertextField.getText(), etcIncludeSubFoldersCheckBox.isSelected());
                        OutputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.yourcommandsoutput"));
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("msd.nocommandparams"), ResourceBundle.getBundle("translations/program_strings").getString("msd.nocommandparams"), JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        CommandshelpButton.setActionCommand("CommandshB");
        CommandshelpButton.addActionListener(gal);
        AddCommandFavoritebutton.setActionCommand("ACommFavorb");
        AddCommandFavoritebutton.addActionListener(gal);
        LoadCommandFavoritebutton.setActionCommand("LCommFavb");
        LoadCommandFavoritebutton.addActionListener(gal);


        // Edit Exif pane buttons
        ExifcopyFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EEd.copyExifFromSelected(getExifFields(), ExifDescriptiontextArea);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        ExifsaveToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EEd.writeExifTags(getExifFields(), ExifDescriptiontextArea, getExifBoxes(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        ExifcopyDefaultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArtistCreditsCopyrightDefaults();
            }
        });
        resetFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EEd.resetFields(getExifFields(), ExifDescriptiontextArea);
            }
        });
        ExifhelpButton.setActionCommand("ExifhB");
        ExifhelpButton.addActionListener(gal);


        // Edit xmp buttons
        xmpCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EXd.copyXmpFromSelected(getXmpFields(), xmpDescriptiontextArea);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        xmpSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EXd.writeXmpTags(getXmpFields(), xmpDescriptiontextArea, getXmpBoxes(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        xmpCopyDefaultsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArtistCreditsCopyrightDefaults();
            }
        });
        xmpResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EXd.resetFields(getXmpFields(), xmpDescriptiontextArea);
            }
        });
        xmpHelpbutton.setActionCommand("xmpHB");
        xmpHelpbutton.addActionListener(gal);


        // Edit geotagging buttons
        geotaggingImgFolderbutton.setActionCommand("geoIFb");
        geotaggingImgFolderbutton.addActionListener(gal);
        geotaggingGPSLogbutton.setActionCommand("geoGPSLb");
        geotaggingGPSLogbutton.addActionListener(gal);
        geotaggingWriteInfobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( ("".equals(geotaggingImgFoldertextField.getText())) && ( (selectedIndicesList == null) || (selectedIndicesList.size() == 0) ) ) {
                    JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("msd.geonoimgpathlong"), ResourceBundle.getBundle("translations/program_strings").getString("msd.geonoimgpath"), JOptionPane.WARNING_MESSAGE);
                } else {
                    if ("".equals(geotaggingGPSLogtextField.getText())) { // We do need a logfile
                        JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("msd.geonolog"), ResourceBundle.getBundle("translations/program_strings").getString("msd.geonolog"), JOptionPane.WARNING_MESSAGE);
                    } else { // We have a log file
                        selectedIndicesList = MyVariables.getselectedIndicesList();
                        if ( (selectedIndicesList == null) || (selectedIndicesList.size() == 0) ) { //we have no images selected but the folder is selected
                            EGd.writeInfo(false, getGeotaggingFields(), getGeotaggingBoxes(), geotaggingOverwriteOriginalscheckBox.isSelected(), progressBar);
                        } else {
                            EGd.writeInfo(true, getGeotaggingFields(), getGeotaggingBoxes(), geotaggingOverwriteOriginalscheckBox.isSelected(), progressBar);
                        }
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
        geotaggingHelpbutton.setActionCommand("geotHb");
        geotaggingHelpbutton.addActionListener(gal);

        // Edit gps buttons
        gpsCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EGPSd.copyGPSFromSelected(getNumGPSdecFields(), getGPSLocationFields(), getGpsBoxes(),getGPSdmsFields(), getGPSdmsradiobuttons());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gpsSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EGPSd.writeGPSTags(getNumGPSdecFields(), getGPSLocationFields(), getGpsBoxes(), getGPSdmsFields(), getGPSdmsradiobuttons(), progressBar, geoformattabbedPane.getSelectedIndex(), rootPanel);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }

        });
        gpsResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGPSd.resetFields(getNumGPSdecFields(), getGPSLocationFields(), getGPSdmsFields());
            }
        });
        gpsMapcoordinatesbutton.setActionCommand("gpsMcb");
        gpsMapcoordinatesbutton.addActionListener(gal);

        gpssearchLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button gpsSearchLocationbutton pressed");
                JxMapViewer JMV = new JxMapViewer();
                Map<String, String> place = JMV.showDialog( ""); // Search using empty fields
                if (!"empty".equals(place.get("empty"))) {
                    if (place.get("saveGpsLocation").equals("true")) {
                        gpsLatDecimaltextField.setText(place.get("geoLatitude"));
                        gpsLonDecimaltextField.setText(place.get("geoLongitude"));
                    }
                    gpsStateProvincetextField.setText(place.get("state"));
                    gpsCountrytextField.setText(place.get("country"));
                    gpsLocationtextField.setText(place.get("display_Name"));
                    if (!(place.get("city") == null)) {
                        gpsCitytextField.setText(place.get("city"));
                    } else if (!(place.get("town") == null)) {
                        gpsCitytextField.setText(place.get("town"));
                    } else if (!(place.get("village") == null)) {
                        gpsCitytextField.setText(place.get("village"));
                    } else if (!(place.get("hamlet") == null)) {
                        gpsCitytextField.setText(place.get("hamlet"));
                    } else if (!(place.get("isolated_dwelling") == null)) {
                        gpsCitytextField.setText(place.get("isolated_dwelling"));
                    }
                }
                // Now do the dec-min-sec fields
                String[] dmsLat = EGPSd.decDegToDegMinSec(place.get("geoLatitude"));
                CalcLatDegtextField.setText(dmsLat[0]);
                CalcLatMintextField.setText(dmsLat[1]);
                CalcLatSectextField.setText(dmsLat[2]);
                if (place.get("geoLatitude").startsWith("-")) {
                    // Negative means South
                    CalcSouthRadioButton.setSelected(true);
                } else {
                    CalcNorthRadioButton.setSelected(true);
                }
                String[] dmsLon = EGPSd.decDegToDegMinSec(place.get("geoLongitude"));
                CalcLonDegtextField.setText(dmsLon[0]);
                CalcLonMintextField.setText(dmsLon[1]);
                CalcLonSectextField.setText(dmsLon[2]);
                if (place.get("geoLongitude").startsWith("-")) {
                    CalcWestRadioButton.setSelected(true);
                } else {
                    CalcEastradioButton.setSelected(true);
                }
            }
        });

        gpssearchLocationButtonCoordinates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String coordinates = "";
                logger.debug("button gpsSearchLocationbutton pressed");
                if ( (gpsLatDecimaltextField.getText()==null) || ("".equals(gpsLatDecimaltextField.getText())) ||
                     (gpsLonDecimaltextField.getText()==null) || ("".equals(gpsLonDecimaltextField.getText())) ) {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("gps.nocoordinatestxt")), ResourceBundle.getBundle("translations/program_strings").getString("gps.nocoordinates"), JOptionPane.WARNING_MESSAGE);
                } else {
                    coordinates = gpsLatDecimaltextField.getText() + ", " + gpsLonDecimaltextField.getText();
                }
                JxMapViewer JMV = new JxMapViewer();
                Map<String, String> place = JMV.showDialog(coordinates); // Search using empty fields
                if (!"empty".equals(place.get("empty"))) {
                    if (place.get("saveGpsLocation").equals("true")) {
                        gpsLatDecimaltextField.setText(place.get("geoLatitude"));
                        gpsLonDecimaltextField.setText(place.get("geoLongitude"));
                    }
                    gpsStateProvincetextField.setText(place.get("state"));
                    gpsCountrytextField.setText(place.get("country"));
                    gpsLocationtextField.setText(place.get("display_Name"));
                    if (!(place.get("city") == null)) {
                        gpsCitytextField.setText(place.get("city"));
                    } else if (!(place.get("town") == null)) {
                        gpsCitytextField.setText(place.get("town"));
                    } else if (!(place.get("village") == null)) {
                        gpsCitytextField.setText(place.get("village"));
                    } else if (!(place.get("hamlet") == null)) {
                        gpsCitytextField.setText(place.get("hamlet"));
                    } else if (!(place.get("isolated_dwelling") == null)) {
                        gpsCitytextField.setText(place.get("isolated_dwelling"));
                    }
                }
                // Now do the dec-min-sec fields
                String[] dmsLat = EGPSd.decDegToDegMinSec(place.get("geoLatitude"));
                CalcLatDegtextField.setText(dmsLat[0]);
                CalcLatMintextField.setText(dmsLat[1]);
                CalcLatSectextField.setText(dmsLat[2]);
                if (place.get("geoLatitude").startsWith("-")) {
                    // Negative means South
                    CalcSouthRadioButton.setSelected(true);
                } else {
                    CalcNorthRadioButton.setSelected(true);
                }
                String[] dmsLon = EGPSd.decDegToDegMinSec(place.get("geoLongitude"));
                CalcLonDegtextField.setText(dmsLon[0]);
                CalcLonMintextField.setText(dmsLon[1]);
                CalcLonSectextField.setText(dmsLon[2]);
                if (place.get("geoLongitude").startsWith("-")) {
                    CalcWestRadioButton.setSelected(true);
                } else {
                    CalcEastradioButton.setSelected(true);
                }
            }
        });


        gpsHelpbutton.addActionListener(gal);
        gpsHelpbutton.setActionCommand("gpsHb");

        decimalToMinutesSecondsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // We will not use this one

            }
        });
        minutesSecondsToDecimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Here is where we calculate the degrees-minutes-seconds to decimal values
                String[] calc_lat_lon = new String[2];
                String[] input_lat_lon = new String[8];

                for (JFormattedTextField calcfield : getGPSdmsFields()) {
                    if (calcfield.getText().isEmpty()) {
                        calcfield.setText("0");
                    }
                }
                input_lat_lon[0] = CalcLatDegtextField.getText();
                input_lat_lon[1] = CalcLatMintextField.getText();
                input_lat_lon[2] = CalcLatSectextField.getText();
                if (CalcNorthRadioButton.isSelected()) {
                    input_lat_lon[3] = "N";
                } else {
                    input_lat_lon[3] = "S";
                }
                input_lat_lon[4] = CalcLonDegtextField.getText();
                input_lat_lon[5] = CalcLonMintextField.getText();
                input_lat_lon[6] = CalcLonSectextField.getText();
                if (CalcEastradioButton.isSelected()) {
                    input_lat_lon[7] = "E";
                } else {
                    input_lat_lon[7] = "W";
                }

                try {
                    calc_lat_lon = Utils.gpsCalculator(rootPanel, input_lat_lon);
                } catch (ParseException e) {
                    logger.error("Error converting deg-min-sec to decimal degrees {}", e.toString());
                    e.printStackTrace();
                }
                //Utils.gpsCalculator(CalcLatDegtextField.getText(), CalcLatMintextField.getText(), CalcLatSectextField.getText(), CalcLonDegtextField.getText(), CalcLonDegtextField.getText(), CalcLonMintextField.getText(), CalcLonSectextField.getText());
                if (!"error".equals(calc_lat_lon[0])) {
                    CalcLatDecimaltextLabel.setText(calc_lat_lon[0]);
                    CalcLonDecimaltextLabel.setText(calc_lat_lon[1]);
                }
            }
        });
        copyToInputFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // This will copy the calculated values to the lat/lon input fields
                gpsLatDecimaltextField.setText( CalcLatDecimaltextLabel.getText() );
                gpsLonDecimaltextField.setText( CalcLonDecimaltextLabel.getText() );
            }
        });

        // Copy metadata buttons from first copy data tab
        copyAllMetadataRadiobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.setCopyMetaDatacheckboxes(false, getCopyMetaDatacheckboxes());
            }
        });
        copyAllMetadataSameGroupsRadiobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.setCopyMetaDatacheckboxes(false, getCopyMetaDatacheckboxes());
            }
        });
        copySelectiveMetadataradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.setCopyMetaDatacheckboxes(true, getCopyMetaDatacheckboxes());
            }
        });
        UseDataFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    SelectedCopyFromImageIndex = SelectedRowOrIndex;
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        CopyDataCopyTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //metaData.copyMetaData(getCopyMetaDataRadiobuttons(), getCopyMetaDataCheckBoxes(), SelectedCopyFromImageIndex, selectedIndices, files, progressBar);
                metaData.copyMetaData(rootPanel, getCopyMetaDataRadiobuttons(), getCopyMetaDataCheckBoxes(), SelectedCopyFromImageIndex, progressBar);
            }
        });
        CopyHelpbutton.setActionCommand("CopyHb");
        CopyHelpbutton.addActionListener(gal);
        //Copydata 2nd tab, copy inside image
        copyAllMetdataToXMPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.setInsideCopyCheckboxesRadiobuttons(false, getInsideImageSubCopyRadiobuttons(), getInsideImageCopyCheckboxes());
            }
        });
        CopyArgsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Utils.setInsideCopyCheckboxesRadiobuttons(true, getInsideImageSubCopyRadiobuttons(), getInsideImageCopyCheckboxes());
            }
        });
        copyInsideSaveDataTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    metaData.copyInsideMetaData(rootPanel, getInsideImageCopyRadiobuttons(), getInsideImageSubCopyRadiobuttons(), getInsideImageCopyCheckboxes(), OutputLabel);
                    OutputLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        // The buttons from the Gpano edit tab
        gpanoCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EGpanod.copyGpanoFromSelected(getGpanoFields(), gpanoStitchingSoftwaretextField, gpanoPTcomboBox, getGpanoCheckBoxes());
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("gpano.savecheckboxestxt")), ResourceBundle.getBundle("translations/program_strings").getString("gpano.savecheckboxestitle"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gpanoCopyTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    // Now we need to check on completeness of mandatory fields
                    boolean allFieldsFilled = EGpanod.checkFieldsOnNotBeingEmpty(getGpanoFields(), gpanoPTcomboBox);
                    if (allFieldsFilled) {
                        EGpanod.writeGpanoTags(getGpanoFields(), getGpanoCheckBoxes(), gpanoStitchingSoftwaretextField, gpanoPTcomboBox, progressBar);
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_strings").getString("gpano.mandatoryfilestxt")), ResourceBundle.getBundle("translations/program_strings").getString("gpano.mandatoryfilestitle"), JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gpanoResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EGpanod.resetFields(getGpanoFields(), gpanoStitchingSoftwaretextField, getGpanoCheckBoxes());
            }
        });
        gpanoHelpbutton.setActionCommand("gpanoHb");
        gpanoHelpbutton.addActionListener(gal);

        // The buttons from the lens tab
        lensCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ELd.copyLensDataFromSelected(getLensFields(), meteringmodecomboBox, getLensCheckBoxes());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        lensSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ELd.writeLensTags(getLensFields(), getLensCheckBoxes(), meteringmodecomboBox, progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        lensResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ELd.resetFields(getLensFields(), getLensCheckBoxes(), meteringmodecomboBox);
            }
        });
        lensHelpbutton.setActionCommand("lensHb");
        lensHelpbutton.addActionListener(gal);

        saveLensConfigurationbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ELd.saveLensconfig(getLensFields(), meteringmodecomboBox, rootPanel);
            }
        });
        loadLensConfigurationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ELd.loadLensconfig(getLensFields(), meteringmodecomboBox, rootPanel);
            }
        });

        // The buttons from the XMP_IPTC_String+ tab
        stringPlusCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ESd.copyStringPlusFromSelected(getstringPlusFields(), stringPlusOverwriteOriginalscheckBox);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        stringPlusSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    ESd.writeStringPlusTags(getstringPlusFields(), stringPlusOverwriteOriginalscheckBox, getSelectedRadioButtons(), getSeparatorString(), progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        stringPlusResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ESd.resetFields(getstringPlusFields(), stringPlusOverwriteOriginalscheckBox );
            }
        });
        stringPlusHelpbutton.setActionCommand("sPHb");
        stringPlusHelpbutton.addActionListener(gal);

        // Button listeners for the User defined metadata combinations tab
        udcCreateNewButton.setActionCommand("udcCNB");
        udcCreateNewButton.addActionListener(gal);
        UserCombiscomboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EUDC.UpdateTable(rootPanel, UserCombiscomboBox, UserCombiScrollPane);
                EUDC.UpdateCustomConfigLabel(UserCombiscomboBox, CustomConfiglabel);
            }
        });
        udcCopyFrombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    if (MyVariables.getuserCombiTableValues() == null || MyVariables.getuserCombiTableValues().size() == 0) // Only update table only if user has not selected one
                        EUDC.UpdateTable(rootPanel, UserCombiscomboBox, UserCombiScrollPane);
                    EUDC.CopyFromSelectedImage();
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        udcSaveTobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedIndicesList = MyVariables.getselectedIndicesList();
                if ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) {
                    EUDC.SaveTableValues(udcOverwriteOriginalscheckBox, progressBar);
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        udcResetFieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Defined here for program consistency but not used
                // as the user can simply "reset" from the dropdown.
            }
        });
        udcHelpbutton.setActionCommand("udcHb");
        udcHelpbutton.addActionListener(gal);

        //Listeners for the Export subtab
        ExpBrowseButton.setActionCommand("expIFb");
        ExpBrowseButton.addActionListener(gal);

        exportAllMetadataCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (catmetadataradioButton.isSelected()) {
                    exportExifDataCheckBox.setEnabled(true);
                    exportXmpDataCheckBox.setEnabled(true);
                    exportGpsDataCheckBox.setEnabled(true);
                    exportIptcDataCheckBox.setEnabled(true);
                    exportICCDataCheckBox.setEnabled(true);
                    exportAllMetadataCheckBox.setEnabled(true);

                    if (exportAllMetadataCheckBox.isSelected()) {
                        exportExifDataCheckBox.setSelected(true);
                        exportXmpDataCheckBox.setSelected(true);
                        exportGpsDataCheckBox.setSelected(true);
                        exportIptcDataCheckBox.setSelected(true);
                        exportICCDataCheckBox.setSelected(true);
                    } else {
                        exportExifDataCheckBox.setSelected(false);
                        exportXmpDataCheckBox.setSelected(false);
                        exportGpsDataCheckBox.setSelected(false);
                        exportIptcDataCheckBox.setSelected(false);
                        exportICCDataCheckBox.setSelected(false);
                    }
                    exportUserCombicomboBox.setEnabled(false);
                }
            }
        });
        exportFromUserCombisradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (exportFromUserCombisradioButton.isSelected()) {
                    exportUserCombicomboBox.setEnabled(true);
                    exportExifDataCheckBox.setEnabled(false);
                    exportXmpDataCheckBox.setEnabled(false);
                    exportGpsDataCheckBox.setEnabled(false);
                    exportIptcDataCheckBox.setEnabled(false);
                    exportICCDataCheckBox.setEnabled(false);
                    exportAllMetadataCheckBox.setEnabled(false);
                } /*else {
                    userCombicomboBox.setEnabled(true);
                    exportExifDataCheckBox.setEnabled(false);
                    exportXmpDataCheckBox.setEnabled(false);
                    exportGpsDataCheckBox.setEnabled(false);
                    exportIptcDataCheckBox.setEnabled(false);
                    exportICCDataCheckBox.setEnabled(false);
                    exportAllMetadataCheckBox.setSelected(false);

                }*/
            }
        });
        catmetadataradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (catmetadataradioButton.isSelected()) {
                    exportExifDataCheckBox.setEnabled(true);
                    exportXmpDataCheckBox.setEnabled(true);
                    exportGpsDataCheckBox.setEnabled(true);
                    exportIptcDataCheckBox.setEnabled(true);
                    exportICCDataCheckBox.setEnabled(true);
                    exportAllMetadataCheckBox.setEnabled(true);
                    exportUserCombicomboBox.setEnabled(false);
                }
            }
        });
        GenExportbuttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( ( !(selectedIndicesList == null) && (selectedIndicesList.size() > 0) ) || (!("".equals(ExpImgFoldertextField.getText()))) ) {
                    ExportMetadata.writeExport(rootPanel, getGeneralExportRadiobuttons(), getGeneralExportCheckButtons(), exportUserCombicomboBox, progressBar, ExpImgFoldertextField.getText(), includeSubFoldersCheckBox.isSelected());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        ExpPDFOKbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( (!(selectedIndicesList == null) && (selectedIndicesList.size() > 0)) || (!("".equals(ExpImgFoldertextField.getText()))) ) {
                    MyVariables.setpdfDocs("");
                    ExportToPDF.CreatePDFs(rootPanel, getPDFradiobuttons(), getPDFcomboboxes(), progressBar, OutputLabel, ExpImgFoldertextField.getText(), includeSubFoldersCheckBox.isSelected());
                 } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        SidecarExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( (!(selectedIndicesList == null) && (selectedIndicesList.size() > 0)) || (!("".equals(ExpImgFoldertextField.getText()))) ) {
                    ExportMetadata.SidecarChoices(getSCradiobuttons(), rootPanel, progressBar, OutputLabel, ExpImgFoldertextField.getText(), includeSubFoldersCheckBox.isSelected());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgslong")), ResourceBundle.getBundle("translations/program_strings").getString("msd.noimgs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        SideCarHelpbutton.setActionCommand("sidecarhelp");
        SideCarHelpbutton.addActionListener(gal);

    } // End of private void programButtonListeners()

    private void ViewRadiobuttonListener() {
                //Add listeners
        radioButtonViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                //logger.info("MyVariables.getSelectedRowOrIndex {}", MyVariables.getSelectedRowOrIndex());
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    logger.info("radiobutton selected: {}", radioButtonViewAll.getText());
                    MyVariables.setmainScreenParams(whichRBselected());
                    String res = Utils.getImageInfoFromSelectedFile(MyConstants.ALL_PARAMS);
                    Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                    //int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                    //File[] files = MyVariables.getLoadedFiles();
                    if (res.startsWith("jExifToolGUI")) {
                        lblFileNamePath.setText(" ");
                    } else {
                        lblFileNamePath.setText(files[selectedRowOrIndex].getPath());
                    }
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        radioButtoncommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                //logger.info("MyVariables.getSelectedRowOrIndex {}", MyVariables.getSelectedRowOrIndex());
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    logger.info("radiobutton selected: {}", radioButtoncommonTags.getText());
                    MyVariables.setmainScreenParams(whichRBselected());
                    String[] params = Utils.getWhichCommonTagSelected(comboBoxViewCommonTags);
                    //Utils.selectImageInfoByTagName(comboBoxViewCommonTags, SelectedRowOrIndex, files, mainScreen.this.ListexiftoolInfotable);
                    String res = Utils.getImageInfoFromSelectedFile(params);
                    Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                    //int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();

                    if (res.startsWith("jExifToolGUI")) {
                        lblFileNamePath.setText(" ");
                    } else {
                        lblFileNamePath.setText(files[selectedRowOrIndex].getPath());
                    }
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        comboBoxViewCommonTags.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                //logger.info("MyVariables.getSelectedRowOrIndex {}", MyVariables.getSelectedRowOrIndex());
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    if (radioButtoncommonTags.isSelected()) {
                        MyVariables.setmainScreenParams(whichRBselected());
                        String[] params = Utils.getWhichCommonTagSelected(comboBoxViewCommonTags);
                        //Utils.selectImageInfoByTagName(comboBoxViewCommonTags, SelectedRowOrIndex, files, mainScreen.this.ListexiftoolInfotable);
                        String res = Utils.getImageInfoFromSelectedFile(params);
                        Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                        //int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                        //File[] files = MyVariables.getLoadedFiles();
                        if (res.startsWith("jExifToolGUI")) {
                            lblFileNamePath.setText(" ");
                        } else {
                            lblFileNamePath.setText(files[selectedRowOrIndex].getPath());
                        }
                    }
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        radioButtonByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    MyVariables.setmainScreenParams(whichRBselected());
                    String res = Utils.selectImageInfoByTagName(comboBoxViewByTagName, files);
                    Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        comboBoxViewByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    if (radioButtonByTagName.isSelected()) {
                        MyVariables.setmainScreenParams(whichRBselected());
                        String res = Utils.selectImageInfoByTagName(comboBoxViewByTagName, files);
                        Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                    }
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        radioButtonCameraMakes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    MyVariables.setmainScreenParams(whichRBselected());
                    String res = Utils.selectImageInfoByTagName(comboBoxViewCameraMake, files);
                    Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
        comboBoxViewCameraMake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                if ( !(selectedRowOrIndex == MyConstants.IMPOSSIBLE_INDEX) ) {
                    if (radioButtonCameraMakes.isSelected()) {
                        MyVariables.setmainScreenParams(whichRBselected());
                        String res = Utils.selectImageInfoByTagName(comboBoxViewCameraMake, files);
                        Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);
                    }
                } else {
                    logger.debug("radiobutton/dropdown action but no files loaded");
                }
            }
        });
    }

// radioButtonViewAll, radioButtoncommonTags,

    public String[] whichRBselected() {
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
        logger.debug("MyVariables.setmainScreenParams(params) {}", String.join(",", params));
        MyVariables.setmainScreenParams(params);
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
                        SelectedRowOrIndex = i;
                        MyVariables.setSelectedRowOrIndex(i);
                    }
                    int trow = tableListfiles.getSelectedRow();
                    int tcolumn = tableListfiles.getSelectedColumn();
                    logger.info("mainscreenListener: selected cell in row {} and column {}", String.valueOf(trow), String.valueOf(tcolumn));
                }
                String[] params = whichRBselected();
                String res = Utils.getImageInfoFromSelectedFile(params);
                Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);

                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                File[] files = MyVariables.getLoadedFiles();
                if (res.startsWith("jExifToolGUI")) {
                    lblFileNamePath.setText(" ");
                } else {
                    lblFileNamePath.setText(files[selectedRowOrIndex].getPath());
                }

                selectedIndices = tmpselectedIndices.stream().mapToInt(Integer::intValue).toArray();
                logger.info("Selected indices: {}", tmpselectedIndices);
                selectedIndicesList = tmpselectedIndices;
                MyVariables.setselectedIndicesList(selectedIndicesList);
                MyVariables.setSelectedFilenamesIndices(selectedIndices);
                //No previews wanted, so only display preview in bottom-left for selected image
                // and prevent 2nd event trigger
                if ( (!(createPreviewsCheckBox.isSelected())) && (!e.getValueIsAdjusting()) ) {
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Utils.selectedRowForSinglePreview();
                            Utils.displaySinglePreview(previewTable, loadMetadataCheckBox.isSelected());
                        }
                    });
                }
            }

        }
    }

/*    class iconViewListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            // Perfectly working row selection method of first program
            List<Integer> tmpselectedIndices = new ArrayList<>();
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            //List<Integer> selectedIndicesList = new ArrayList<>();
            //int[] selectedIndices = null;

            if (lsm.isSelectionEmpty()) {
                logger.debug("no grid view index selected");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        tmpselectedIndices.add(i);
                        int SelectedRowOrIndex = i;
                        MyVariables.setSelectedRowOrIndex(i);
                        logger.info("MyVariables.getSelectedRowOrIndex() {}", MyVariables.getSelectedRowOrIndex());
                    }
                }
                String[] params = MyVariables.getmainScreenParams();
                String res = Utils.getImageInfoFromSelectedFile(params);
                //Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);

                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                File[] files = MyVariables.getLoadedFiles();


                selectedIndices = tmpselectedIndices.stream().mapToInt(Integer::intValue).toArray();
                logger.info("Selected grid indices: {}", tmpselectedIndices);
                logger.info("Save indices {}", Arrays.toString(selectedIndices));
                selectedIndicesList = tmpselectedIndices;
                MyVariables.setselectedIndicesList(selectedIndicesList);
                MyVariables.setSelectedFilenamesIndices(selectedIndices);
            }
        }
    }*/

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void rootPanelDropListener() {
        //Listen to drop events

        rootPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        logger.debug("File path is: {}", file.getPath());
                    }
                    File[] droppedFilesArray = (File[]) droppedFiles.toArray(new File[droppedFiles.size()]);
                    MyVariables.setLoadedFiles(droppedFilesArray);
                    files = Utils.loadImages("dropped files", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Drag drop on rootpanel error {}", ex);
                }
            }
        });
    }


    // endregion

    /*
    / This creates the menu with the listener in its own external MenuActionListener class
     */
    private void createMenuBar(JFrame frame) {
        menuBar = new JMenuBar();


        // Due to the Load Iamge method with its own ActionListener we do the first menu items here
        // File menu
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.file"));
        myMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(myMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loadimages"));
        myMenu.setMnemonic(KeyEvent.VK_L);
        menuItem.setActionCommand("Load Images");
        //menuItem.addActionListener(mal);
        menuItem.addActionListener(new SpecialMenuActionListener());
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loaddirectory"));
        menuItem.setActionCommand("Load Directory");
        myMenu.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new SpecialMenuActionListener());
        myMenu.add(menuItem);

        // Now we continue with the rest of the entire menu outside the mainScreen in the external CreateMenu class
        CreateMenu crMenu = new CreateMenu();
        crMenu.CreateMenuBar(frame, rootPanel, splitPanel, menuBar, myMenu, OutputLabel, progressBar, UserCombiscomboBox);
    }


    // prevent null-pointer assignments
    //If we click a radio button in the top right without files loaded we get an NPE
    //Can't set null object in setter (File files[])
    // So make the selectedroworindex impossibly high
    private void preventNullPointerAssignments() {
        MyVariables.setSelectedRowOrIndex(MyConstants.IMPOSSIBLE_INDEX);
    }

    // Sets the necessary screen texts. We choose this way as we have now more control over width
    // without bothering the translators with <html></html> and/or <br> codes or length of total string(s).
    private void setProgramScreenTexts() {
        String version = "";
        MyCommandsText.setText(String.format(ProgramTexts.HTML, 600, ( ResourceBundle.getBundle("translations/program_strings").getString("yc.toptext") + "<br><br>") ));
        xmpTopText.setText(String.format(ProgramTexts.HTML, 600,ResourceBundle.getBundle("translations/program_strings").getString("xmp.toptext")));
        GeotaggingLeaveFolderEmptyLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.folderexplanation")));
        ExpLeaveFolderEmptyLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.folderexplanation")));
        etcLeaveFolderEmptyLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.folderexplanation")));
        GeoTfolderBrowseLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.explanation")));
        genExpfolderBrowseLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.explanation")));
        etcFolderBrowseLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("fld.explanation")));
        GeotaggingLocationLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("geo.geotagexpl")));
        GeotaggingGeosyncExplainLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("geo.geosyncexpl")));
        //gpsCalculatorLabelText.setText(String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("gps.calculatortext")));
        gPanoTopText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("gpano.toptext")));
        copyAllMetadataRadiobutton.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyallmetadatasame")));
        copyAllMetadataSameGroupsRadiobutton.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("copyd.copyallsamegroup")));
        copySelectiveMetadataradioButton.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("copyd.copybyselection")));
        lensSaveLoadConfigLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("lens.saveloadconfig")));
        //exiftoolDBText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("edb.toptext")));
        StringsTopText.setText(String.format(ProgramTexts.HTML, 600, ( ResourceBundle.getBundle("translations/program_strings").getString("xis.toptext") + "<br><br>")));
        SeparatorText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("xis.separatortext")));
        UserCombiTopText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("udc.toptext")));
        exportMetaDataUiText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("emd.toptext")));
        expPdftextLabel.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("exppdf.toptext")));
        pdfLabelSupported.setText(String.format(ProgramTexts.HTML, 600, "<br>" + ResourceBundle.getBundle("translations/program_strings").getString("exppdf.supp")));
        lblNominatimSearch.setText(String.format(ProgramTexts.HTML, 600, ( ResourceBundle.getBundle("translations/program_strings").getString("gps.searchtxt") + "<br><br>")));
        lblMapcoordinates.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("gps.extsearch")));


        // database version
        //exiftoolDBversion.setText(String.format(ProgramTexts.HTML,100,"exiftool DB version:<br>" + SQLiteJDBC.getDBversion()));
        // Special dynamic version string
        logger.trace("check for exiftool version");
        String exiftool = Utils.platformExiftool();
        List<String> cmdparams = new ArrayList<>();
        Application.OS_NAMES currentOsName = Utils.getCurrentOsName();
        logger.info("OS name {}", currentOsName);

        boolean isWindows = Utils.isOsFromMicrosoft();
        if (isWindows) {
            cmdparams.add("\"" + exiftool.replace("\\", "/") + "\"");
        } else {
            cmdparams.add(exiftool.trim());
        }
        cmdparams.add("-ver");

        try {
            logger.trace("Get exiftool version");
            version = (CommandRunner.runCommand(cmdparams));
            logger.trace("raw exiftool version: {}", version);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command");
            version = "I cannot determine the exiftool version";
        }
        Float floatversion = Float.parseFloat(version.trim());
        Float minversion = new Float("9.09");
        int retval = floatversion.compareTo(minversion);
        logger.trace("returned version for exiftool (>0: OK; <=0: No gpano): {}", retval);
        if (retval >0) { // cureent exiftool > 9.09
            gpanoMinVersionText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("gpano.googlemapsfields")));
        } else {
            gpanoMinVersionText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("gpano.disabledfields")));
            gpanoPHDtextField.setEnabled(false);
            gpanoStitchingSoftwaretextField.setEnabled(false);
            gpanoIVHDtextField.setEnabled(false);
            gpanoIVPDtextField.setEnabled(false);
            gpanoIVRDtextField.setEnabled(false);
            gpanoIHFOVDtextField.setEnabled(false);
        }

    }

    private void setArtistCreditsCopyrightDefaults() {
        // Try to set the defaults for artist, credit and copyrights in the edit exif/xmp panes if prefs available
        String[] ArtCredCopyPrefs = Utils.checkPrefsArtistCreditsCopyRights();
        ExifArtistCreatortextField.setText(ArtCredCopyPrefs[0]);
        ExifCopyrighttextField.setText(ArtCredCopyPrefs[2]);
        xmpCreatortextField.setText(ArtCredCopyPrefs[0]);
        xmpCredittextField.setText(ArtCredCopyPrefs[1]);
        xmpRightstextField.setText(ArtCredCopyPrefs[2]);
    }


    // This is where it all starts
    // initialisation of the Application
    public mainScreen(JFrame frame) throws IOException, InterruptedException {
        boolean exiftool_found = false;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            { GuiConfig.SetSplitPaneDivider(splitPanel); }
        });

        // Do not simply exit on closing the window. First delete our temp stuff and save gui settings
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StandardFileIO.deleteDirectory(new File (MyVariables.gettmpWorkFolder()) );
                GuiConfig.SaveGuiConfig(frame, rootPanel, splitPanel);
                CompareImages.CleanUp();
                System.exit(0);
            }
        });


        $$$setupUI$$$();
        Utils.progressStatus(progressBar, false);

        createMenuBar(frame);
        //createPopupMenu(myPopupMenu);
        

        String check_result = checkforjexiftoolguiFolder();
        if (check_result.contains("Error creating")) {
            JOptionPane.showMessageDialog(rootPanel, "Could not create the data folder " + MyConstants.MY_DATA_FOLDER + "  or one of its files", "error creating folder/files", JOptionPane.ERROR_MESSAGE);
        } else { // Set database to variable
            logger.info("string for DB: " + MyVariables.getjexiftoolguiDBPath());
        }
        // Delete and recreate {tmp dir}/jexiftoolgui
        check_result = StandardFileIO.RecreateOurTempFolder();
        if (!"Success".equals(check_result)) {
            JOptionPane.showMessageDialog(rootPanel, "Could not (re)create our temporary working folder", "error (re)creating temp folder", JOptionPane.ERROR_MESSAGE);
        }

        // Now check the preferences
        CheckPreferences CP = new CheckPreferences();
        /*String ET_preference = CP.checkExifToolPreference();
        if ( ET_preference.contains("Preference null_empty_notexisting") || ET_preference.contains("No exiftool preference yet") ) {
            String res = ExifTool.getExiftoolInPath();
        } */

        exiftool_found = CP.checkPreferences(rootPanel, OutputLabel);
        logger.debug("ExifToolPath from CheckPreferences: {}", MyVariables.getExifToolPath());
        if (!exiftool_found) {
            ExifTool.checkExifTool(mainScreen.this.rootPanel);
        } else {
            if ( (MyVariables.getExifToolPath() != null) && "c:\\windows\\exiftool.exe".equals( (MyVariables.getExifToolPath()).toLowerCase() ) ) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("exift.notinwinpathtext")), ResourceBundle.getBundle("translations/program_strings").getString("exift.notinwinpathtitle"), JOptionPane.WARNING_MESSAGE);
                ExifTool.checkExifTool(mainScreen.this.rootPanel);
            } else if ( (MyVariables.getExifToolPath() != null) && ( (MyVariables.getExifToolPath()).toLowerCase().contains("exiftool(-k).exe") || (MyVariables.getExifToolPath()).toLowerCase().contains("-k version")) ) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("exift.exifk")), ResourceBundle.getBundle("translations/program_strings").getString("exift.notinwinpathtitle"), JOptionPane.WARNING_MESSAGE);
                ExifTool.checkExifTool(mainScreen.this.rootPanel);
            }
            // Now check if it is executable
            String isExecutable = ExifTool.showVersion(OutputLabel);
            if (isExecutable.contains("Error executing command")) {
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("exift.wrongetbinaryfromstartup")), ResourceBundle.getBundle("translations/program_strings").getString("exift.wrongexebin"), JOptionPane.WARNING_MESSAGE);
                ExifTool.checkExifTool(mainScreen.this.rootPanel);
            }
        }

        // Set the text areas correctly
        ExifDescriptiontextArea.setWrapStyleWord(true);
        ExifDescriptiontextArea.setLineWrap(true);
        xmpDescriptiontextArea.setWrapStyleWord(true);
        xmpDescriptiontextArea.setLineWrap(true);

        // Do necessary updates when moving from older versions to newer versions
        // This is done in a swingworker background task
        UpdateActions.Updates();

        ViewRadiobuttonListener();
        fillAllComboboxes();

        setArtistCreditsCopyrightDefaults();
        // Some texts
        setProgramScreenTexts();


        // prevent null-pointer assignments after statup without any files loaded
        preventNullPointerAssignments();


        //Use the table listener for the selection of multiple cells
        listSelectionModel = tableListfiles.getSelectionModel();
        tableListfiles.setRowSelectionAllowed(true);
        tableListfiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());

        // Use the mouselistener for the double-click to display the image
        // on the filenamestable
        MouseListeners.FileNamesTableMouseListener(tableListfiles, ListexiftoolInfotable, whichRBselected());

        //Listen to drop events
        rootPanelDropListener();

        // Make left "tableListfiles" and right "ListexiftoolInfotable" tables read-only (un-editable)
        // This also fixes the double-click bug on the image where it retrieves the object name of the images on double-click
        tableListfiles.setDefaultEditor(Object.class, null);
        ListexiftoolInfotable.setDefaultEditor(Object.class, null);
        // Make all tables read-only unless ....
        //DBResultsTable.setDefaultEditor(Object.class, null);

        // icon for my dialogs
        InputStream stream = StandardFileIO.getResourceAsStream("icons/jexiftoolgui-64.png");
        try {
            icon = new ImageIcon(ImageIO.read(stream));
        } catch (IOException ex) {
            logger.error("Error executing command");
        }

        programButtonListeners();
        leftPanePopupMenuLister();

        // Set formatting for the JFormatted textFields
        EGpanod.setFormattedFieldFormats(getGpanoFields());
        // Set jFormattedFields
        EGPSd.setFormattedFieldMasks(getNumGPSdecFields(), getGPSdmsFields());

        //Check on command line arguments
        if ( MyVariables.getcommandLineArgsgiven()) {
            List<File> filesList = new ArrayList<File>();
            files = CommandLineArguments.ProcessArguments(filesList);
            MyVariables.setLoadedFiles(files);
            files = Utils.loadImages("commandline", rootPanel, LeftPanel, LeftTableScrollPanel, LeftGridScrollPanel, tableListfiles, previewTable, iconViewList, ListexiftoolInfotable, commandButtons(), mainScreenLabels(), progressBar, whichRBselected(), getLoadOptions());
        }

        Utils.checkForNewVersion("startup");
        //JLabelDropReady.addPropertyChangeListener(new PropertyChangeListener() {
        //});

        createPreviewsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (createPreviewsCheckBox.isSelected()) {
                    MyVariables.setcreatePreviewsCheckBox(true);
                    iconViewList.clearSelection();
                    DefaultListModel lm = (DefaultListModel) iconViewList.getModel();
                    lm.removeAllElements();
                } else {
                    MyVariables.setcreatePreviewsCheckBox(false);
                }
            }
        });

    }



    static void renderSplashFrame(Graphics2D g, int frame) {
        //final String[] comps = {"loading jExifToolGUI", "Please be patient"};
        final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
    }

    final static void showSplash() {
        final SplashScreen splash = SplashScreen.getSplashScreen();

        /*try {
            //URL splashUrl = mainScreen.class.getResource("/icons/new-jexiftoolgui-splashlogo.png");
            //URL splashUrl = mainScreen.class.getResource("/splash.gif");
            if (splashUrl == null) {
                logger.error("Cannot load the splashlogo {}", splashUrl.toString());
            } else {
                logger.info("setting the splash logo {}", splashUrl.toString());
                splash.setImageURL(splashUrl);
            }
        } catch (IOException e) {
            logger.error("loading splash image gives error {}", e.toString());
            e.printStackTrace();
        } */
        if (splash == null) {
            logger.error("SplashScreen.getSplashScreen() returned null");
            return;
        }
        //Graphics2D g = splashImg.createGraphics();
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            logger.error("g is null");
            return;
        }
        for (int i = 1; i < 100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
            }
        }
        //splash.close();
    }

    static void createAndShowGUI() {

        // Doesn't work but leave in
        System.out.println(SingletonEnum.INSTANCE);
        Application.OS_NAMES os = Utils.getCurrentOsName();

        JFrame frame = new JFrame("jExifToolGUI V" + ProgramTexts.Version + "  " + ResourceBundle.getBundle("translations/program_strings").getString("application.title"));

        try {
            if (os == Application.OS_NAMES.APPLE) {
                logger.info("running on Apple. set correct menu");
                // take the menu bar off the jframe and put it in the MacOS menu
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                // set the name of the application menu item
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "jExifToolGUI V" + ProgramTexts.Version + "   (for ExifTool by Phil Harvey)");

                // And the annoying splash screen on MacOS
                /* Leave for later investigation
                try {
                    showSplash();
                } catch (Exception e) {
                    logger.error("error displaying splash screen {}", e.toString());
                    e.printStackTrace();
                }*/

            }
            // Significantly improves the look of the output in
            // terms of the folder/file icons and file names returned by FileSystemView!
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //UIManager.setLookAndFeel(GTKLookAndFeel);
        } catch (Exception weTried) {
           logger.error("Could not start GUI.", weTried);
        }

        frame.setIconImage(Utils.getFrameIcon());
        try {
            frame.setContentPane(new mainScreen(frame).rootPanel);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            logger.error("InterruptedException or IOException: {}", e.toString());
        }

        logger.debug("Gui Width x Height: {} x {}", frame.getWidth(), String.valueOf(frame.getHeight()));
        GuiConfig.LoadGuiConfig(frame);
        //frame.setLocationRelativeTo(null);
        frame.setLocationByPlatform(true);
        frame.setDefaultLookAndFeelDecorated(true);
        Locale currentLocale = MyVariables.getCurrentLocale();
        logger.info("Set Locale to: {}", currentLocale);
        frame.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));

        frame.setVisible(true);

    }

}
