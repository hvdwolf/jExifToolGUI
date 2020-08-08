package org.hvdw.jexiftoolgui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.controllers.ExifTool;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;

public class PreferencesDialog extends JDialog {
    JPanel contentPanel;
    JButton buttonSave;
    JButton buttonCancel;
    JTextField ExiftoolLocationtextField;
    JButton ExiftoolLocationbutton;
    JTextField ImgStartFoldertextField;
    JButton ImgStartFolderButton;
    JTextField ArtisttextField;
    JTextField CopyrightstextField;
    JCheckBox UseLastOpenedFoldercheckBox;
    JCheckBox CheckVersioncheckBox;
    private JComboBox metadataLanuagecomboBox;
    private JTextField CreditstextField;
    private JTextField RawViewerLocationtextField;
    private JButton RawViewerLocationButton;
    private JCheckBox RawViewercheckBox;

    // Initialize all the helper classes
    //AppPreferences AppPrefs = new AppPreferences();
    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private static final Logger logger = LoggerFactory.getLogger(PreferencesDialog.class);

    PreferencesDialog() {
        setContentPane(contentPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);
        this.setIconImage(Utils.getFrameIcon());

        buttonSave.addActionListener(e -> onSave());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        ExiftoolLocationbutton.addActionListener(actionEvent -> {
            String ETpath = "";
            ETpath = ExifTool.whereIsExiftool(contentPanel);
            getExiftoolPath(contentPanel, ExiftoolLocationtextField, ETpath, "preferences");
        });

        ImgStartFolderButton.addActionListener(actionEvent -> getDefaultImagePath(contentPanel, ImgStartFoldertextField));

        RawViewerLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getRawViewerLocation(contentPanel, RawViewerLocationtextField);
            }
        });
        RawViewercheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (RawViewerLocationtextField.getText().isEmpty()) {
                    RawViewercheckBox.setSelected(false);
                    JOptionPane.showMessageDialog(contentPanel, ResourceBundle.getBundle("translations/program_strings").getString("prefs.chkboxrawtext"), ResourceBundle.getBundle("translations/program_strings").getString("prefs.chkboxrawtitle"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void onSave() {
        // add your code here
        savePrefs();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    // As the exiftoolLocator is already created in the Utils as startup check we simply leave it there
    // although you can discuss that it can also be a preference if we deviate from the standard one
    // of course we do need the return value which we will get from the listener
    public void getExiftoolPath(JPanel myComponent, JTextField myExiftoolTextfield, String ePath, String fromWhere) {
        if ("cancelled".equals(ePath)) {
            if ("startup".equals(fromWhere)) {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("prefs.etlocatecanceltext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etlocatecanceltitle"), JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("prefs.etlocatecanceltext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etlocatecanceltitle"), JOptionPane.WARNING_MESSAGE);
            }
        } else if ("no exiftool binary".equals(ePath)) {
            if ("startup".equals(fromWhere)) {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("prefs.etwrongtext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etwrongtitle"), JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("prefs.etwrongtext")), ResourceBundle.getBundle("translations/program_strings").getString("prefs.etwrongtitle"), JOptionPane.WARNING_MESSAGE);
            }
        } else { // Yes. It looks like we have a correct exiftool selected
            // remove all possible line breaks
            ePath = ePath.replace("\n", "").replace("\r", "");
            //prefs.put("exiftool", ePath);
            myExiftoolTextfield.setText(ePath);
        }
    }

    // Locate the default image path, if the user wants it
    public void getDefaultImagePath(JPanel myComponent, JTextField defImgFolder) {
        String SelectedFolder;

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("prefs.locateprefimgfolder"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedFolder = chooser.getSelectedFile().getAbsolutePath();
            defImgFolder.setText(SelectedFolder);
        }
    }

    /*
    / Locate the raw viewer (if installed)
     */
    public void getRawViewerLocation(JPanel myComponent, JTextField RawViewerLocationtextField) {
        String SelectedRawViewer;
        String selectedBinary = "";

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("prefs.locaterawviewer"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            selectedBinary = chooser.getSelectedFile().getAbsolutePath();
            RawViewerLocationtextField.setText(selectedBinary);
        }
    }

    private void savePrefs() {
        logger.info("Saving the preferences");
        logger.info("artist {}", ArtisttextField.getText());
        logger.info("copyrights {}", CopyrightstextField.getText());
        logger.info("credit {}", CreditstextField.getText());
        logger.info("exiftool {}", ExiftoolLocationtextField.getText());
        logger.info("defaultstartfolder {}", ImgStartFoldertextField.getText());
        logger.info("uselastopenedfolder {}", UseLastOpenedFoldercheckBox.isSelected());
        logger.info("Check for new version on startup {}", CheckVersioncheckBox.isSelected());
        logger.info("metadatalanguage {}", metadataLanuagecomboBox.getSelectedItem());
        logger.info("raw viewer {}", RawViewerLocationtextField.getText());

        if (!ArtisttextField.getText().isEmpty()) {
            logger.trace("{}: {}", ARTIST.key, ArtisttextField.getText());
            prefs.storeByKey(ARTIST, ArtisttextField.getText());
        }
        if (!CreditstextField.getText().isEmpty()) {
            logger.trace("{}: {}", CREDIT.key, CreditstextField.getText());
            prefs.storeByKey(CREDIT, CreditstextField.getText());
        }
        if (!CopyrightstextField.getText().isEmpty()) {
            logger.trace("{}: {}", COPYRIGHTS.key, CopyrightstextField.getText());
            prefs.storeByKey(COPYRIGHTS, CopyrightstextField.getText());
        }
        if (!ExiftoolLocationtextField.getText().isEmpty()) {
            logger.trace("{}: {}", EXIFTOOL_PATH.key, ExiftoolLocationtextField.getText());
            prefs.storeByKey(EXIFTOOL_PATH, ExiftoolLocationtextField.getText());
        }
        if (!ImgStartFoldertextField.getText().isEmpty()) {
            logger.trace("{}: {}", DEFAULT_START_FOLDER.key, ImgStartFoldertextField.getText());
            prefs.storeByKey(DEFAULT_START_FOLDER, ImgStartFoldertextField.getText());
        }
        if (!RawViewerLocationtextField.getText().isEmpty()) {
            logger.trace("{}: {}", RAW_VIEWER_PATH.key, RawViewerLocationtextField.getText());
            prefs.storeByKey(RAW_VIEWER_PATH, RawViewerLocationtextField.getText());
        }

        logger.trace("{}: {}", USE_LAST_OPENED_FOLDER.key, UseLastOpenedFoldercheckBox.isSelected());
        prefs.storeByKey(USE_LAST_OPENED_FOLDER, UseLastOpenedFoldercheckBox.isSelected());

        logger.trace("{}: {}", VERSION_CHECK.key, CheckVersioncheckBox.isSelected());
        prefs.storeByKey(VERSION_CHECK, CheckVersioncheckBox.isSelected());

        logger.trace("{}: {}", METADATA_LANGUAGE.key, metadataLanuagecomboBox.getSelectedItem());
        prefs.storeByKey(METADATA_LANGUAGE, (String) metadataLanuagecomboBox.getSelectedItem());

        logger.trace("{}: {}", RAW_VIEWER_ALL_IMAGES.key, RawViewercheckBox.isSelected());
        prefs.storeByKey(RAW_VIEWER_ALL_IMAGES, RawViewercheckBox.isSelected());

        JOptionPane.showMessageDialog(contentPanel, ResourceBundle.getBundle("translations/program_strings").getString("prefs.settingssaved"), ResourceBundle.getBundle("translations/program_strings").getString("prefs.settingssaved"), JOptionPane.INFORMATION_MESSAGE);
    }


    private void retrievePreferences() {
        // get current preferences
        ExiftoolLocationtextField.setText(prefs.getByKey(EXIFTOOL_PATH, ""));
        ImgStartFoldertextField.setText(prefs.getByKey(DEFAULT_START_FOLDER, ""));
        ArtisttextField.setText(prefs.getByKey(ARTIST, ""));
        CreditstextField.setText(prefs.getByKey(CREDIT, ""));
        CopyrightstextField.setText(prefs.getByKey(COPYRIGHTS, ""));
        UseLastOpenedFoldercheckBox.setSelected(prefs.getByKey(USE_LAST_OPENED_FOLDER, false));
        CheckVersioncheckBox.setSelected(prefs.getByKey(VERSION_CHECK, false));
        metadataLanuagecomboBox.setSelectedItem(prefs.getByKey(METADATA_LANGUAGE, "exiftool - default"));
        RawViewerLocationtextField.setText(prefs.getByKey(RAW_VIEWER_PATH, ""));
        RawViewercheckBox.setSelected(prefs.getByKey(RAW_VIEWER_ALL_IMAGES, false));
    }

    // The  main" function of this class
    void showDialog() {
        //setSize(750, 600);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("preferences.title"));
        pack();
        double x = getParent().getBounds().getCenterX();
        double y = getParent().getBounds().getCenterY();
        //setLocation((int) x - getWidth() / 2, (int) y - getHeight() / 2);
        setLocationRelativeTo(null);
        String languages = StandardFileIO.readTextFileAsStringFromResource("texts/Languages.txt");
        String[] exiftoolLanguages = languages.split("\\r?\\n"); // split on new lines
        metadataLanuagecomboBox.setModel(new DefaultComboBoxModel(exiftoolLanguages));

        retrievePreferences();
        setVisible(true);

    }

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
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(13, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPanel.setMaximumSize(new Dimension(-1, -1));
        contentPanel.setMinimumSize(new Dimension(800, 600));
        contentPanel.setPreferredSize(new Dimension(850, 650));
        contentPanel.setRequestFocusEnabled(false);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 1, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSave = new JButton();
        this.$$$loadButtonText$$$(buttonSave, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.save"));
        panel2.add(buttonSave, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.exiftoolocation"));
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExiftoolLocationtextField = new JTextField();
        ExiftoolLocationtextField.setMinimumSize(new Dimension(300, 30));
        ExiftoolLocationtextField.setPreferredSize(new Dimension(550, 30));
        panel4.add(ExiftoolLocationtextField);
        ExiftoolLocationbutton = new JButton();
        this.$$$loadButtonText$$$(ExiftoolLocationbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.btnchoose"));
        panel4.add(ExiftoolLocationbutton);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.defaultimgstartdir"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ImgStartFoldertextField = new JTextField();
        ImgStartFoldertextField.setMinimumSize(new Dimension(300, 30));
        ImgStartFoldertextField.setPreferredSize(new Dimension(550, 30));
        panel5.add(ImgStartFoldertextField);
        ImgStartFolderButton = new JButton();
        this.$$$loadButtonText$$$(ImgStartFolderButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.btnchoose"));
        panel5.add(ImgStartFolderButton);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 1, new Insets(0, 10, 0, 0), -1, -1));
        contentPanel.add(panel6, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.alwaysaddvals"));
        panel6.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel6.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setMaximumSize(new Dimension(300, 18));
        label4.setMinimumSize(new Dimension(220, 18));
        label4.setPreferredSize(new Dimension(300, 18));
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.artist"));
        panel7.add(label4);
        ArtisttextField = new JTextField();
        ArtisttextField.setPreferredSize(new Dimension(350, 30));
        panel7.add(ArtisttextField);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel6.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setMaximumSize(new Dimension(300, 18));
        label5.setMinimumSize(new Dimension(220, 18));
        label5.setPreferredSize(new Dimension(300, 18));
        this.$$$loadLabelText$$$(label5, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.copyright"));
        panel8.add(label5);
        CopyrightstextField = new JTextField();
        CopyrightstextField.setPreferredSize(new Dimension(350, 30));
        panel8.add(CopyrightstextField);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel6.add(panel9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setMaximumSize(new Dimension(300, 18));
        label6.setMinimumSize(new Dimension(250, 18));
        label6.setPreferredSize(new Dimension(300, 18));
        this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.credits"));
        panel9.add(label6);
        CreditstextField = new JTextField();
        CreditstextField.setPreferredSize(new Dimension(350, 30));
        panel9.add(CreditstextField);
        UseLastOpenedFoldercheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(UseLastOpenedFoldercheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.alwaysuselastfolder"));
        UseLastOpenedFoldercheckBox.setToolTipText("Selecting this checkbox will overrule the \"Default image start directory:\"");
        contentPanel.add(UseLastOpenedFoldercheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPanel.add(spacer2, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        CheckVersioncheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(CheckVersioncheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.checknewversion"));
        contentPanel.add(CheckVersioncheckBox, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.add(panel10, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.metdatadisplaylang"));
        panel10.add(label7);
        metadataLanuagecomboBox = new JComboBox();
        metadataLanuagecomboBox.setPreferredSize(new Dimension(300, 30));
        panel10.add(metadataLanuagecomboBox);
        final Spacer spacer3 = new Spacer();
        contentPanel.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        contentPanel.add(panel11, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.rawviewerlocation"));
        panel11.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel11.add(panel12, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RawViewerLocationtextField = new JTextField();
        RawViewerLocationtextField.setMinimumSize(new Dimension(300, 30));
        RawViewerLocationtextField.setPreferredSize(new Dimension(550, 30));
        panel12.add(RawViewerLocationtextField);
        RawViewerLocationButton = new JButton();
        this.$$$loadButtonText$$$(RawViewerLocationButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.btnchoose"));
        panel12.add(RawViewerLocationButton);
        RawViewercheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(RawViewercheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.alwaysuserawviewer"));
        panel11.add(RawViewercheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.ITALIC, -1, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        this.$$$loadLabelText$$$(label9, this.$$$getMessageFromBundle$$$("translations/program_strings", "prefs.macosrawremark"));
        panel11.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return contentPanel;
    }

}
