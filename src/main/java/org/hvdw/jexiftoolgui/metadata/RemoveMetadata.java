package org.hvdw.jexiftoolgui.metadata;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;

public class RemoveMetadata extends JDialog {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(RemoveMetadata.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel RemoveMetaDataUiText;
    private JCheckBox removeAllMetadataCheckBox;
    private JCheckBox removeExifDataCheckBox;
    private JCheckBox removeXmpDataCheckBox;
    private JCheckBox removeGpsDataCheckBox;
    private JCheckBox removeIptcDataCheckBox;
    private JCheckBox removeICCDataCheckBox;
    private JCheckBox makeBackupOfOriginalsCheckBox;
    private JCheckBox removegeotagDataCheckbox;
    private JCheckBox removexmpgeotagDataCheckbox;

    public int[] selectedFilenamesIndices;
    public File[] files;
    public JProgressBar progBar;


    public RemoveMetadata() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        removeAllMetadataCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (removeAllMetadataCheckBox.isSelected()) {
                    removeExifDataCheckBox.setSelected(true);
                    removeXmpDataCheckBox.setSelected(true);
                    removeGpsDataCheckBox.setSelected(true);
                    removeIptcDataCheckBox.setSelected(true);
                    removeICCDataCheckBox.setSelected(true);
                    removegeotagDataCheckbox.setSelected(true);
                    removexmpgeotagDataCheckbox.setSelected(true);
                } else {
                    removeExifDataCheckBox.setSelected(false);
                    removeXmpDataCheckBox.setSelected(false);
                    removeGpsDataCheckBox.setSelected(false);
                    removeIptcDataCheckBox.setSelected(false);
                    removeICCDataCheckBox.setSelected(false);
                    removegeotagDataCheckbox.setSelected(false);
                    removexmpgeotagDataCheckbox.setSelected(false);
                }
            }
        });
    }

    private void initDialog() {
        //RemoveMetaDataUiText.setContentType("text/html");
        RemoveMetaDataUiText.setText(String.format(ProgramTexts.HTML, 320, ResourceBundle.getBundle("translations/program_strings").getString("rmd.toptext")));


    }

    private void onOK() {
        // add your code here
        removeMetadatafromImage();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void removeMetadatafromImage() {
        boolean atLeastOneSelected = false;

        List<String> params = new ArrayList<String>();
        params.add(Utils.platformExiftool());
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.youhaveselected"));
        if (removeAllMetadataCheckBox.isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("rmd.allmetadata"));
            params.add("-all=");
            atLeastOneSelected = true;
        } else {
            Message.append("<ul>");
            if (removeExifDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removeexif") + "</li>");
                params.add("-exif:all=");
                atLeastOneSelected = true;
            }
            if (removeXmpDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removexmp") + "</li>");
                params.add("-xmp:all=");
                atLeastOneSelected = true;
            }
            if (removeGpsDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removegps") + "</li>");
                params.add("-gps:all=");
                atLeastOneSelected = true;
            }
            if (removeIptcDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removeiptc") + "</li>");
                params.add("-iptc:all=");
                atLeastOneSelected = true;
            }
            if (removeICCDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removeicc") + "</li>");
                params.add("-icc_profile:all=");
                atLeastOneSelected = true;
            }
            if (removegeotagDataCheckbox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removegeotag") + "</li>");
                params.add("-geotag=");
                atLeastOneSelected = true;
            }
            if (removexmpgeotagDataCheckbox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("rmd.removegexmp") + "</li>");
                params.add("-xmp:geotag=");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        Message.append("Is this correct?</html>");
        if (atLeastOneSelected) {
            String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
            int choice = JOptionPane.showOptionDialog(null, Message, ResourceBundle.getBundle("translations/program_strings").getString("rmd.dlgyouwantto"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
                boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, false);
                if (preserveModifyDate) {
                    params.add("-preserve");
                }
                if (!makeBackupOfOriginalsCheckBox.isSelected()) {
                    params.add("-overwrite_original");
                }
                boolean isWindows = Utils.isOsFromMicrosoft();
                for (int index : selectedFilenamesIndices) {
                    //logger.info("index: {}  image path: {}", index, files[index].getPath());
                    if (isWindows) {
                        params.add(files[index].getPath().replace("\\", "/"));
                    } else {
                        params.add(files[index].getPath());
                    }
                }
                // remove metadata
                CommandRunner.runCommandWithProgressBar(params, progBar);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, ResourceBundle.getBundle("translations/program_strings").getString("rmd.dlgnooptiontext"), ResourceBundle.getBundle("translations/program_strings").getString("rmd.dlgnooptiontitle"), JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showDialog(JProgressBar progressBar) {
        //ExportMetadata dialog = new ExportMetadata();
        //setSize(400, 250);
        selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
        files = MyVariables.getSelectedFiles();
        progBar = progressBar;

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("removemetadata.title"));
        initDialog();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(450, 550));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RemoveMetaDataUiText = new JLabel();
        RemoveMetaDataUiText.setEnabled(true);
        RemoveMetaDataUiText.setRequestFocusEnabled(false);
        RemoveMetaDataUiText.setText("");
        RemoveMetaDataUiText.setVerifyInputWhenFocusTarget(false);
        panel3.add(RemoveMetaDataUiText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 1, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        makeBackupOfOriginalsCheckBox = new JCheckBox();
        makeBackupOfOriginalsCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(makeBackupOfOriginalsCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        panel4.add(makeBackupOfOriginalsCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        removeAllMetadataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeAllMetadataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removeall"));
        panel5.add(removeAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(7, 1, new Insets(0, 30, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        removeExifDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeExifDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removeexif"));
        panel6.add(removeExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeXmpDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeXmpDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removexmp"));
        panel6.add(removeXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeGpsDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeGpsDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removegps"));
        panel6.add(removeGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeIptcDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeIptcDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removeiptc"));
        panel6.add(removeIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeICCDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(removeICCDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removeicc"));
        panel6.add(removeICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removegeotagDataCheckbox = new JCheckBox();
        this.$$$loadButtonText$$$(removegeotagDataCheckbox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removegeotag"));
        panel6.add(removegeotagDataCheckbox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removexmpgeotagDataCheckbox = new JCheckBox();
        this.$$$loadButtonText$$$(removexmpgeotagDataCheckbox, this.$$$getMessageFromBundle$$$("translations/program_strings", "rmd.removegexmp"));
        panel6.add(removexmpgeotagDataCheckbox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        return contentPane;
    }

}
