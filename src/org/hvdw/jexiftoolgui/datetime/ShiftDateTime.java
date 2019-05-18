package org.hvdw.jexiftoolgui.datetime;

import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.programTexts;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShiftDateTime extends JDialog {
    private JPanel contentPane;
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

    Utils myUtils = new Utils();
    public int[] selectedFilenamesIndices;
    public File[] files;

    public ShiftDateTime() {
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
    }

    public void initDialog() {

        ShiftDateTimeLabel.setText( String.format(programTexts.HTML, 320, programTexts.ShiftDateTimeLabel) );
    }

    public void writeInfo() {
        String fpath ="";
        String shiftOption = "";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        String exiftool = myUtils.platformExiftool();
        cmdparams.clear();
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
            if (UpdatexmpcheckBox.isSelected()){
                cmdparams.add("-xmp:DateTimeOriginal" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }
        if (ShiftModifyDatecheckBox.isSelected()) {
            cmdparams.add("-exif:ModifyDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            if (UpdatexmpcheckBox.isSelected()){
                cmdparams.add("-xmp:ModifyDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }
        if (ShiftCreateDatecheckBox.isSelected()) {
            cmdparams.add("-exif:CreateDate" + shiftOption + ShiftDateTimetextField.getText().trim());
            if (UpdatexmpcheckBox.isSelected()){
                cmdparams.add("-xmp:DateTimeDigitized" + shiftOption + ShiftDateTimetextField.getText().trim());
            }
        }

        for (int index: selectedFilenamesIndices) {
            if (isWindows) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
         }
        try {
            res = myUtils.runCommand(cmdparams);
            System.out.println(res);
        } catch(IOException | InterruptedException ex) {
            System.out.println("Error executing command");
        }

        myUtils.runCommandOutput(res);
    }

    private void onOK() {
        writeInfo();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void showDialog(int[] selectedIndices, File[] openedfiles) {
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;

        pack();
        setLocationByPlatform(true);
        setTitle("Shift Date/Time");
        initDialog();
        setVisible(true);
    }
}
