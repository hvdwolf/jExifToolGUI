package org.hvdw.jexiftoolgui.datetime;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ShiftDateTime extends JDialog {
    private JPanel shiftDateTimePane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox shiftForwardCheckBox;
    private JTextField ShiftDateTimetextField;
    private JLabel ShiftDateTimeLabel;
    private JCheckBox ShiftDateTimeOriginalcheckBox;
    private JCheckBox ShiftCreateDatecheckBox;
    private JCheckBox ShiftModifyDatecheckBox;
    private JCheckBox BackupOfOriginalscheckBox;
    private JCheckBox UpdatexmpcheckBox;

    private int[] selectedFilenamesIndices;
    public File[] files;
    private JProgressBar progBar;

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ShiftDateTime.class);

    public ShiftDateTime() {
        setContentPane(shiftDateTimePane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        shiftDateTimePane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initDialog() {

        ShiftDateTimeLabel.setText(String.format(ProgramTexts.HTML, 320, ResourceBundle.getBundle("translations/program_strings").getString("sdt.toptext")));
    }

    private void writeInfo() {
        String shiftOption;
        List<String> cmdparams = new LinkedList<>();

        String exiftool = Utils.platformExiftool();
        cmdparams.add(exiftool);
        if (!BackupOfOriginalscheckBox.isSelected()) {
            cmdparams.add("-overwrite_original");
        }
        if (shiftForwardCheckBox.isSelected()) {
            shiftOption = "+=";
        } else {
            shiftOption = "-=";
        }

        if (ShiftDateTimeOriginalcheckBox.isSelected()) {
            cmdparams.add("-exif:DateTimeOriginal" + shiftOption + ShiftDateTimetextField.getText().trim());
            if (UpdatexmpcheckBox.isSelected()) {
                cmdparams.add("-xmp:DateTimeOriginal" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }
        if (ShiftModifyDatecheckBox.isSelected()) {
            cmdparams.add("-exif:ModifyDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            if (UpdatexmpcheckBox.isSelected()) {
                cmdparams.add("-xmp:ModifyDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }
        if (ShiftCreateDatecheckBox.isSelected()) {
            cmdparams.add("-exif:CreateDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            if (UpdatexmpcheckBox.isSelected()) {
                cmdparams.add("-xmp:DateTimeDigitized" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }

        for (int index : selectedFilenamesIndices) {
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }

        logger.info(cmdparams.toString());
        CommandRunner.runCommandWithProgressBar(cmdparams, progBar);
    }

    private void onOK() {
        if ("0000:00:00 00:00:00".equals(ShiftDateTimetextField.getText())) {
            JOptionPane.showMessageDialog(shiftDateTimePane, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("sdt.dlgshiftzero")), ResourceBundle.getBundle("translations/program_strings").getString("sdt.dlgnoshift"), JOptionPane.ERROR_MESSAGE);
        } else if ((!ShiftDateTimeOriginalcheckBox.isSelected()) && (!ShiftModifyDatecheckBox.isSelected()) && (!ShiftCreateDatecheckBox.isSelected())) {
            // You did not select any date option
            JOptionPane.showMessageDialog(shiftDateTimePane, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("sdt.dlgshiftzero")), ResourceBundle.getBundle("translations/program_strings").getString("sdt.dlgnoshift"), JOptionPane.ERROR_MESSAGE);
        } else {
            writeInfo();
            dispose();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void showDialog(JProgressBar progressBar) {
        selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
        files = MyVariables.getSelectedFiles();
        progBar = progressBar;

        pack();
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("sdt.dlgtitle"));
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
        shiftDateTimePane = new JPanel();
        shiftDateTimePane.setLayout(new GridLayoutManager(7, 1, new Insets(10, 10, 10, 10), 1, 1));
        shiftDateTimePane.setPreferredSize(new Dimension(450, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        shiftDateTimePane.add(panel1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        shiftDateTimePane.add(panel3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        shiftForwardCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(shiftForwardCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.forward"));
        panel3.add(shiftForwardCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.shifttext"));
        panel3.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftDateTimetextField = new JTextField();
        ShiftDateTimetextField.setText("0000:00:00 00:00:00");
        panel3.add(ShiftDateTimetextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.format"));
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftDateTimeLabel = new JLabel();
        ShiftDateTimeLabel.setText("ShiftDateTimeLabel");
        shiftDateTimePane.add(ShiftDateTimeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        shiftDateTimePane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        ShiftDateTimeOriginalcheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(ShiftDateTimeOriginalcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.shiftdatetimeoriginal"));
        panel4.add(ShiftDateTimeOriginalcheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftCreateDatecheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(ShiftCreateDatecheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.shiftcreatedate"));
        panel4.add(ShiftCreateDatecheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftModifyDatecheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(ShiftModifyDatecheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.shiftmodifydate"));
        panel4.add(ShiftModifyDatecheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BackupOfOriginalscheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(BackupOfOriginalscheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "chkbox.makebackup"));
        shiftDateTimePane.add(BackupOfOriginalscheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UpdatexmpcheckBox = new JCheckBox();
        UpdatexmpcheckBox.setSelected(true);
        this.$$$loadButtonText$$$(UpdatexmpcheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "sdt.updatexmp"));
        shiftDateTimePane.add(UpdatexmpcheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return shiftDateTimePane;
    }

}
