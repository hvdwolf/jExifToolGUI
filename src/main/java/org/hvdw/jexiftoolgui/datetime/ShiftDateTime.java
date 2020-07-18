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
import java.util.LinkedList;
import java.util.List;

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

    private final static Logger logger = LoggerFactory.getLogger(ShiftDateTime.class);

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

        ShiftDateTimeLabel.setText(String.format(ProgramTexts.HTML, 320, ProgramTexts.ShiftDateTimeLabel));
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
            JOptionPane.showMessageDialog(shiftDateTimePane, "You left the shift value at \"0000:00:00 00:00:00\".\nNothing to do!", "No shift value set", JOptionPane.ERROR_MESSAGE);
        } else if ((!ShiftDateTimeOriginalcheckBox.isSelected()) && (!ShiftModifyDatecheckBox.isSelected()) && (!ShiftCreateDatecheckBox.isSelected())) {
            // You did not select any date option
            JOptionPane.showMessageDialog(shiftDateTimePane, "You did not select any of the date options.\nNothing to do!", "No date option set", JOptionPane.ERROR_MESSAGE);
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
        setTitle("Shift Date/Time");
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
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        shiftDateTimePane.add(panel3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        shiftForwardCheckBox = new JCheckBox();
        shiftForwardCheckBox.setText("Shift forward");
        panel3.add(shiftForwardCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Shift date and/or time for selected images");
        panel3.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftDateTimetextField = new JTextField();
        ShiftDateTimetextField.setText("0000:00:00 00:00:00");
        panel3.add(ShiftDateTimetextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("YYYY:MM:DD hh:mm:ss");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftDateTimeLabel = new JLabel();
        ShiftDateTimeLabel.setText("ShiftDateTimeLabel");
        shiftDateTimePane.add(ShiftDateTimeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        shiftDateTimePane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        ShiftDateTimeOriginalcheckBox = new JCheckBox();
        ShiftDateTimeOriginalcheckBox.setText("shift exif:DateTimeOriginal");
        panel4.add(ShiftDateTimeOriginalcheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftCreateDatecheckBox = new JCheckBox();
        ShiftCreateDatecheckBox.setText("shift exif:CreateDate");
        panel4.add(ShiftCreateDatecheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ShiftModifyDatecheckBox = new JCheckBox();
        ShiftModifyDatecheckBox.setText("Shift exif:ModifyDate");
        panel4.add(ShiftModifyDatecheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BackupOfOriginalscheckBox = new JCheckBox();
        BackupOfOriginalscheckBox.setText("Make backup of originals");
        shiftDateTimePane.add(BackupOfOriginalscheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UpdatexmpcheckBox = new JCheckBox();
        UpdatexmpcheckBox.setSelected(true);
        UpdatexmpcheckBox.setText("Update xmp values as well");
        shiftDateTimePane.add(UpdatexmpcheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return shiftDateTimePane;
    }

}
