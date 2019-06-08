package org.hvdw.jexiftoolgui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    // Initialize all the helper classes
    //AppPreferences AppPrefs = new AppPreferences();
    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private static final Logger logger = LoggerFactory.getLogger(PreferencesDialog.class);

    PreferencesDialog() {
        setContentPane(contentPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);

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
            ETpath = Utils.whereIsExiftool(contentPanel);
            getExiftoolPath(contentPanel, ExiftoolLocationtextField, ETpath, "preferences");
        });
        ImgStartFolderButton.addActionListener(actionEvent -> getDefaultImagePath(contentPanel, ImgStartFoldertextField));
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
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.cancelledETlocatefromStartup, "Cancelled locate ExifTool", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.cancelledETlocatefromPrefs, "Cancelled locate ExifTool", JOptionPane.WARNING_MESSAGE);
            }
        } else if ("no exiftool binary".equals(ePath)) {
            if ("startup".equals(fromWhere)) {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromStartup, "Wrong executable", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, ProgramTexts.wrongETbinaryfromPrefs, "Wrong executable", JOptionPane.WARNING_MESSAGE);
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
        chooser.setDialogTitle("Locate preferred default image folder ...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedFolder = chooser.getSelectedFile().getAbsolutePath();
            defImgFolder.setText(SelectedFolder);
        }
    }

    private void savePrefs() {
        logger.debug("Saving the preferences");
        logger.debug("artist {}", ArtisttextField.getText());
        logger.debug("copyrights {}", CopyrightstextField.getText());
        logger.debug("exiftool {}", ExiftoolLocationtextField.getText());
        logger.debug("defaultstartfolder {}", ImgStartFoldertextField.getText());
        logger.debug("uselastopenedfolder {}", UseLastOpenedFoldercheckBox.isSelected());
        logger.debug("Check for new version on startup {}", CheckVersioncheckBox.isSelected());

        if (!ArtisttextField.getText().isEmpty()) {
            logger.trace("{}: {}", ARTIST.key, ArtisttextField.getText());
            prefs.storeByKey(ARTIST, ArtisttextField.getText());
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
            logger.trace("{}: {}", DEFAULT_START_FOLDER.key, ExiftoolLocationtextField.getText());
            prefs.storeByKey(DEFAULT_START_FOLDER, ExiftoolLocationtextField.getText());
        }

        logger.trace("{}: {}", USE_LAST_OPENED_FOLDER.key, UseLastOpenedFoldercheckBox.isSelected());
        prefs.storeByKey(USE_LAST_OPENED_FOLDER, UseLastOpenedFoldercheckBox.isSelected());


        logger.trace("{}: {}", VERSION_CHECK.key, CheckVersioncheckBox.isSelected());
        prefs.storeByKey(VERSION_CHECK, CheckVersioncheckBox.isSelected());

        JOptionPane.showMessageDialog(contentPanel, "Settings saved", "Settings saved", JOptionPane.INFORMATION_MESSAGE);
    }


    private void retrievePreferences() {
        // get current preferences
        ExiftoolLocationtextField.setText(prefs.getByKey(EXIFTOOL_PATH, ""));
        ImgStartFoldertextField.setText(prefs.getByKey(DEFAULT_START_FOLDER, ""));
        ArtisttextField.setText(prefs.getByKey(ARTIST, ""));
        CopyrightstextField.setText(prefs.getByKey(COPYRIGHTS, ""));
        UseLastOpenedFoldercheckBox.setSelected(prefs.getByKey(USE_LAST_OPENED_FOLDER, false));
        CheckVersioncheckBox.setSelected(prefs.getByKey(VERSION_CHECK, false));
    }

    // The  main" function of this class
    void showDialog() {
        setSize(700, 400);
        double x = getParent().getBounds().getCenterX();
        double y = getParent().getBounds().getCenterY();
        //setLocation((int) x - getWidth() / 2, (int) y - getHeight() / 2);
        setLocationRelativeTo(null);
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
        contentPanel.setLayout(new GridLayoutManager(9, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPanel.setPreferredSize(new Dimension(834, 600));
        contentPanel.setRequestFocusEnabled(false);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 1, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panel2.add(buttonSave, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Exiftool location:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ExiftoolLocationtextField = new JTextField();
        ExiftoolLocationtextField.setMinimumSize(new Dimension(300, 30));
        ExiftoolLocationtextField.setPreferredSize(new Dimension(550, 30));
        panel4.add(ExiftoolLocationtextField);
        ExiftoolLocationbutton = new JButton();
        ExiftoolLocationbutton.setText("Choose");
        panel4.add(ExiftoolLocationbutton);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Default image start directory:");
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ImgStartFoldertextField = new JTextField();
        ImgStartFoldertextField.setMinimumSize(new Dimension(300, 30));
        ImgStartFoldertextField.setPreferredSize(new Dimension(550, 30));
        panel5.add(ImgStartFoldertextField);
        ImgStartFolderButton = new JButton();
        ImgStartFolderButton.setText("Choose");
        panel5.add(ImgStartFolderButton);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 2, new Insets(0, 10, 0, 0), -1, -1));
        contentPanel.add(panel6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Values to always add to your images");
        panel6.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel6.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel6.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel6.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setMinimumSize(new Dimension(220, 18));
        label4.setPreferredSize(new Dimension(250, 18));
        label4.setText("Artist / Creator / Photographer");
        panel7.add(label4);
        ArtisttextField = new JTextField();
        ArtisttextField.setPreferredSize(new Dimension(350, 30));
        panel7.add(ArtisttextField);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel6.add(panel8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setMinimumSize(new Dimension(220, 18));
        label5.setPreferredSize(new Dimension(250, 18));
        label5.setText("Copyrights / Rights");
        panel8.add(label5);
        CopyrightstextField = new JTextField();
        CopyrightstextField.setPreferredSize(new Dimension(350, 30));
        panel8.add(CopyrightstextField);
        UseLastOpenedFoldercheckBox = new JCheckBox();
        UseLastOpenedFoldercheckBox.setText("Always use last opened folder");
        UseLastOpenedFoldercheckBox.setToolTipText("Selecting this checkbox will overrule the \"Default image start directory:\"");
        contentPanel.add(UseLastOpenedFoldercheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPanel.add(spacer4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        CheckVersioncheckBox = new JCheckBox();
        CheckVersioncheckBox.setText("Check for new jExifToolGUI version on program start");
        contentPanel.add(CheckVersioncheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return contentPanel;
    }
}
