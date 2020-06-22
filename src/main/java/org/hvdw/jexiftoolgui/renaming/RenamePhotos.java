package org.hvdw.jexiftoolgui.renaming;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.PreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hvdw.jexiftoolgui.controllers.CommandRunner.*;
import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.EXIFTOOL_PATH;

public class RenamePhotos extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel RenamingGeneralText;
    private JButton renamingInfobutton;
    private JTextField RenamingSourceFoldertextField;
    private JButton RenamingSourceFolderbutton;
    private JPanel RenamingOptions;
    private JPanel RenamingsuffixPanel;
    private JPanel RenamingNumberingPanel;
    private JLabel RenamingDuplicateNames;
    private JPanel RenamingFileExtPanel;
    private JRadioButton prefixDate_timeradioButton;
    private JComboBox prefixDate_timecomboBox;
    private JRadioButton prefixDateradioButton;
    private JComboBox prefixDatecomboBox;
    private JRadioButton prefixStringradioButton;
    private JTextField prefixStringtextField;
    private JRadioButton suffixDonNotUseradioButton;
    private JRadioButton suffixStringradioButton;
    private JTextField suffixStringtextField;
    private JRadioButton suffixDatetimeradioButton;
    private JComboBox suffixDatetimecomboBox;
    private JRadioButton suffixDateradioButton;
    private JComboBox suffixDatecomboBox;
    private JRadioButton suffixCameramodelradioButton;
    private JRadioButton suffixOriginalFilenameradioButton;
    private JComboBox DigitscomboBox;
    private JComboBox StartOnImgcomboBox;
    private JRadioButton extLeaveradioButton;
    private JRadioButton makeLowerCaseRadioButton;
    private JRadioButton makeUpperCaseRadioButton;
    private JProgressBar progressBar;
    private JRadioButton prefixCameramodelradioButton;

    private IPreferencesFacade prefs = PreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public RenamePhotos() {
        StringBuilder res = new StringBuilder();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // button listeners
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException | InterruptedException ee) {
                    logger.error("IOException error", ee);
                    res.append("IOException error")
                            .append(System.lineSeparator())
                            .append(ee.getMessage());
                }
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
        renamingInfobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        RenamingSourceFolderbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ImgPath = getImagePath(contentPane);
                if (!"".equals(ImgPath)) {
                    RenamingSourceFoldertextField.setText(ImgPath);
                }
            }
        });
    }

    public String getImagePath(JPanel myComponent) {
        String SelectedFolder;

        String startFolder = StandardFileIO.getFolderPathToOpenBasedOnPreferences();
        final JFileChooser chooser = new JFileChooser(startFolder);
        chooser.setDialogTitle("Locate the image folder ...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedFolder = chooser.getSelectedFile().getAbsolutePath();
            return SelectedFolder;
        } else {
            return "";
        }
    }


    // Start of the methods
    public void initDialog() {
        RenamingGeneralText.setText(String.format(ProgramTexts.HTML, 650, ProgramTexts.RenamingGeneralText));
        RenamingDuplicateNames.setText(String.format(ProgramTexts.HTML, 370, ProgramTexts.RenamingDuplicateNames));
        RenamingSourceFoldertextField.setText("");

        for (String item : MyConstants.DATES_TIMES_STRINGS) {
            prefixDate_timecomboBox.addItem(item);
            suffixDatetimecomboBox.addItem(item);
        }
        for (String item : MyConstants.DATES_STRINGS) {
            prefixDatecomboBox.addItem(item);
            suffixDatecomboBox.addItem(item);
        }
        for (int digit = 2; digit <= 6; digit++) {
            DigitscomboBox.addItem(digit);
        }
        StartOnImgcomboBox.addItem("start counting on 1st image");
        StartOnImgcomboBox.addItem("start counting on 2nd image");
    }

    private void rename_photos() {
        //int[] selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
        //File[] files = MyVariables.getSelectedFiles();
        StringBuilder res = new StringBuilder();
        String fpath = "";
        List<String> cmdparams = new ArrayList<String>();
        StringBuilder exifcommands = new StringBuilder();

        String rename_extension = "";
        String extension_message = "";
        String prefix = "${CreateDate}";
        String prefix_message = "";
        String prefixformat = "";
        boolean fulldatetime = false;
        String suffix = "${CreateDate}";
        String suffix_message = "";
        String suffixformat = "";
        String startcounting = "";
        String startcounting_message = "";
        boolean date_used = true;

        // exiftool on windows or other
        String exiftool = prefs.getByKey(EXIFTOOL_PATH, "");
        boolean isWindows = Utils.isOsFromMicrosoft();
        if (isWindows) {
            exiftool = exiftool.replace("\\", "/");
        }


        if ("".equals(RenamingSourceFoldertextField.getText())) { // Empty folder string
            JOptionPane.showMessageDialog(null, "No image folder path selected", "No path", JOptionPane.WARNING_MESSAGE);
        } else {
            // analyze what prefix radio button has been chosen
            if (prefixDate_timeradioButton.isSelected()) {
                if (prefixDate_timecomboBox.getSelectedItem() == "YYYYMMDDHHMMSS") {
                    prefix_message = "YYYYMMDDHHMMSS";
                    prefixformat = "%Y%m%d%H%M%S";
                } else if (prefixDate_timecomboBox.getSelectedItem() == "YYYYMMDD_HHMMSS") {
                    prefix_message = "YYYYMMDD_HHMMSS";
                    prefixformat = "%Y%m%d_%H%M%S";
                } else if (prefixDate_timecomboBox.getSelectedItem() == "YYYYMMDD-HHMMSS") {
                    prefix_message = "YYYYMMDD-HHMMSS";
                    prefixformat = "%Y%m%d-%H%M%S";
                } else if (prefixDate_timecomboBox.getSelectedItem() == "YYYY_MM_DD_HH_MM_SS") {
                    prefix_message = "YYYY_MM_DD_HH_MM_SS";
                    prefixformat = "%Y_%m_%d_%H_%M_%S";
                } else if (prefixDate_timecomboBox.getSelectedItem() == "YYYY-MM-DD-HH-MM-SS") {
                    prefix_message = "YYYY-MM-DD-HH-MM-SS";
                    prefixformat = "%Y-%m-%d-%H-%M-%S";
                }
                fulldatetime = true;
            } else if (prefixDateradioButton.isSelected()) {
                if (prefixDatecomboBox.getSelectedItem() == "YYYYMMDD") {
                    prefix_message = "YYYYMMDD";
                    prefixformat = "%Y%m%d";
                } else if (prefixDatecomboBox.getSelectedItem() == "YYYY_MM_DD") {
                    prefix_message = "YYYY_MM_DD";
                    prefixformat = "%Y_%m_%d";
                } else if (prefixDatecomboBox.getSelectedItem() == "YYYY-MM-DD") {
                    prefix_message = "YYYY-MM-DD";
                    prefixformat = "%Y-%m-%d";
                }
                fulldatetime = false;
            } else if (prefixStringradioButton.isSelected()) {
                prefix_message = prefixStringtextField.getText();
                prefix = prefixStringtextField.getText();
                prefixformat = "";
                fulldatetime = false;
            } else if (prefixCameramodelradioButton.isSelected()) {
                prefix_message = "${Exif:Model}";
                prefix = "${Exif:Model}";
                prefixformat = "";
                fulldatetime = false;
            }


            // analyze if and which suffix radio button has been chosen
            suffix = "${CreateDate}";
            if (suffixDonNotUseradioButton.isSelected()) {
                suffix_message = "";
                suffixformat = "";
                suffix = "";
            } else {
                if (suffixStringradioButton.isSelected()) {
                    suffix_message = suffixStringtextField.getText();
                    suffix = suffixStringtextField.getText();
                    suffixformat = "";
                } else if (suffixDatetimeradioButton.isSelected()) {
                    if (suffixDatetimecomboBox.getSelectedItem() == "YYYYMMDDHHMMSS") {
                        suffix_message = "YYYYMMDDHHMMSS";
                        suffixformat = "%Y%m%d%H%M%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYYMMDD_HHMMSS") {
                        suffix_message = "YYYYMMDD_HHMMSS";
                        suffixformat = "%Y%m%d_%H%M%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYMMDD-HHMMSS") {
                        suffix_message = "YYYMMDD-HHMMSS";
                        suffixformat = "%Y%m%d-%H%M%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY_MM_DD_HH_MM_SS") {
                        suffix_message = "YYYY_MM_DD_HH_MM_SS";
                        suffixformat = "%Y_%m_%d_%H_%M_%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY-MM-DD-HH-MM-SS") {
                        suffix_message = "YYYY-MM-DD-HH-MM-SS";
                        suffixformat = "%Y-%m-%d-%H-%M-%S";
                    }
                    fulldatetime = true;
                } else if (suffixDateradioButton.isSelected()) {
                    if (suffixDatecomboBox.getSelectedItem() == "YYYYMMDD") {
                        suffix_message = "YYYYMMDD";
                        suffixformat = "%Y%m%d";
                    } else if (suffixDatecomboBox.getSelectedItem() == "YYYY_MM_DD") {
                        suffix_message = "YYYY_MM_DD";
                        suffixformat = "%Y_%m_%d";
                    } else if (suffixDatecomboBox.getSelectedItem() == "YYYY-MM-DD") {
                        suffix_message = "YYYY-MM-DD";
                        suffixformat = "%Y-%m-%d";
                    }
                    fulldatetime = false;
                } else if (suffixCameramodelradioButton.isSelected()) {
                    suffix_message = "${Exif:Model}";
                    suffix = "${Exif:Model}";
                    suffixformat = "";
                } else if (suffixOriginalFilenameradioButton.isSelected()) {
                    suffix_message = "${filename}";
                    suffix = "${filename}";
                    suffixformat = "";
                }
            }

            // Now the extension: Does the user want lowercase, uppercase or doesn't care (leave as is)?
            if (extLeaveradioButton.isSelected()) {
                rename_extension = ".%e";
                extension_message = "Leave as is";
            } else if (makeLowerCaseRadioButton.isSelected()) {
                rename_extension = ".%le";
                extension_message = "Make lowercase";
            } else if (makeUpperCaseRadioButton.isSelected()) {
                rename_extension = ".%ue";
                extension_message = "Make uppercase";
            }

            // Finally: Does the user want to start counting as of the first image or starting on the second image
            if (StartOnImgcomboBox.getSelectedIndex() == 0) {
                startcounting = "nc";
                logger.info("start counting on 1st image");
                startcounting_message = "start counting on 1st image";
            } else {
                startcounting = "c";
                logger.info("start counting on 2nd image");
                startcounting_message = "start counting on 2nd image";
            }

            // Now ask the user whether the selected options are really what he/she wants
            String dialogMessage = "You selected:\n\nAs prefix:  ";
            dialogMessage += prefix_message;
            if (!suffixDonNotUseradioButton.isSelected()) {
                dialogMessage += "\n\nAnd as suffix:  ";
                dialogMessage += suffix_message;
            }
            dialogMessage += "\n\nFor counting:  ";
            dialogMessage += startcounting_message;
            dialogMessage += "\n\nFor the extension:  ";
            dialogMessage += extension_message;

            String[] options = {"Continue", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, dialogMessage, "In OnOK",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) { //Continue
                // Here is where we build the exiftool parameter string
                /* Examples
                Below you see "filename"  options: '-filename<CreateDate' -> single quotes for linux/MacOS; double quotes for windows

                exiftool '-filename<CreateDate' -d %Y%m%d%%-.3nc.%%le DIR
                for 3 images this will deliver for example
                20190628-001.jpg
                20190628-002.jpg
                20190628-003.jpg
                  -.3nc means: start with first image and use 3 digits; .%%le means: make the extension lowercase
                  '-filename<CreateDate' -> linux/MacOS need single quotes, windows need double quotes

                exiftool "-filename<CreateDate" -d %Y%m%d-Paris%-.3nc.%%le DIR
                will give
                20190628-Paris-001.jpg
                20190628-Paris-002.jpg
                20190628-Paris-003.jpg

                exiftool "-filename<CreateDate" -d Paris-%Y%m%d%-.3nc.%%le
                  will give:
                Paris-20190628-001.jpg # "some string", YYYYMMDD, 3 digits, lowercase extension
                Paris-20190628-002.jpg
                Paris-20190628-003.jpg

                exiftool '-filename<${model;}%-.2c.%e' DIR
                  will give:
                DMC-TZ80-00.JPG  # model, 2 digits, leave extension as is
                DMC-TZ80-01.JPG
                DMC-TZ80-02.JPG

                exiftool '-filename<London-${model;}%-.3c.%le'
                  will give:
                London-DMC-TZ80-000.jpg  # "some string", model, 3-digits, change to lower case extension (see above)
                London-DMC-TZ80-001.jpg
                London-DMC-TZ80-002.jpg
                */

                //cmdparams.add("-overwrite_original_in_place");
                // We need to first cmdparams here, as sometimes the string does not contains spaces and sometimes it does
                // When it does have spaces we need to create an addition cdmparams

                if ((suffixDonNotUseradioButton.isSelected()) && (prefixStringradioButton.isSelected())) {
                    // string as prefix and no suffix
                    if (isWindows) {
                        cmdparams.add(Utils.platformExiftool());
                        exifcommands = new StringBuilder("\"-FileName=" + prefix);
                    } else {
                        // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                        cmdparams.add("/bin/sh");
                        cmdparams.add("-c");
                        exifcommands = new StringBuilder(Utils.platformExiftool() + " '-FileName=" + prefix);
                    }
                } else { // Or a suffix or date(time), or both
                    if (isWindows) {
                        cmdparams.add(Utils.platformExiftool());
                        exifcommands = new StringBuilder("\"-FileName<" + prefix);
                    } else {
                        // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                        cmdparams.add("/bin/sh");
                        cmdparams.add("-c");
                        exifcommands = new StringBuilder(Utils.platformExiftool() + " '-FileName<" + prefix);
                    }
                }
                if (!suffixDonNotUseradioButton.isSelected()) {
                    exifcommands.append("_" + suffix);
                }
                if (fulldatetime) {
                    // This means that the autonumber should only work on images that have the same full datetime
                    exifcommands.append("%-" + DigitscomboBox.getSelectedItem() + startcounting);
                } else {
                    exifcommands.append("%-." + DigitscomboBox.getSelectedItem() + startcounting);
                }
                if (!"".equals(prefixformat)) {
                    // This means that the prefix is a date(time), we do need an additional cmdparams command
                    if (isWindows) {
                        exifcommands.append(rename_extension + "\" -d " + prefixformat);
                    } else {
                        exifcommands.append(rename_extension + "' -d " + prefixformat);
                    }
                } else {
                    // this means that we use a string instead of date(time) as prefix
                    // if the prefixformat is empty we need to move the "counter"
                    // It also means we must have a suffix
                    if (isWindows) {
                        exifcommands.append(rename_extension + "\"");
                    } else {
                        exifcommands.append(rename_extension + "'");
                    }
                    if (!"".equals(suffixformat)) {
                        // means that we have a date(time) in the suffix
                        exifcommands.append(" -d " + suffixformat);
                    }
                }
                if ((!"".equals(prefixformat)) || (!"".equals(suffixformat))) {
                    // if date time use -fileorder datetimeoriginal#
                    if (isWindows) {
                        exifcommands.append(" \"-fileorder datetimeoriginal#\"");
                    } else {
                        exifcommands.append(" '-fileorder datetimeoriginal#'");
                    }
                }
                // Add the dir
                if (isWindows) {
                    cmdparams.add(exifcommands.toString());
                    cmdparams.add(RenamingSourceFoldertextField.getText().replace(File.separator, "/"));
                } else {
                    exifcommands.append(" " + RenamingSourceFoldertextField.getText().replace(File.separator, "/"));
                    cmdparams.add(exifcommands.toString());
                }
                logger.info("final cmdparams: " + cmdparams);
                logger.info("final exifcommands: " + exifcommands.toString());

                CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
                //} //end of else statement where we do not have a string and not a suffix
            } // On Cancel we don't have to do anything
            //JOptionPane.showMessageDialog(null, dialogMessage, "In OnOK", JOptionPane.WARNING_MESSAGE);
        }


    }

    private void onOK() throws IOException, InterruptedException {
        rename_photos();
        //dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void showDialog() {
        //progBar = progressBar;
        progressBar.setVisible(false);

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(880, 650));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Close");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD | Font.ITALIC, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Name: Prefix_Suffix.extension");
        panel3.add(label1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel1.add(progressBar, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, 8), new Dimension(85, 15), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RenamingGeneralText = new JLabel();
        RenamingGeneralText.setText("RenamingGeneralText");
        panel5.add(RenamingGeneralText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Source folder:");
        panel5.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel5.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        renamingInfobutton = new JButton();
        renamingInfobutton.setText("Info");
        panel6.add(renamingInfobutton);
        RenamingSourceFoldertextField = new JTextField();
        RenamingSourceFoldertextField.setPreferredSize(new Dimension(600, 30));
        panel6.add(RenamingSourceFoldertextField);
        RenamingSourceFolderbutton = new JButton();
        RenamingSourceFolderbutton.setText("Browse");
        panel6.add(RenamingSourceFolderbutton);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        RenamingOptions = new JPanel();
        RenamingOptions.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(RenamingOptions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Select rename options:");
        RenamingOptions.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Prefix:");
        RenamingOptions.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixDate_timeradioButton = new JRadioButton();
        prefixDate_timeradioButton.setPreferredSize(new Dimension(140, 19));
        prefixDate_timeradioButton.setSelected(true);
        prefixDate_timeradioButton.setText("Date_time");
        panel8.add(prefixDate_timeradioButton);
        prefixDate_timecomboBox = new JComboBox();
        panel8.add(prefixDate_timecomboBox);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixDateradioButton = new JRadioButton();
        prefixDateradioButton.setPreferredSize(new Dimension(140, 19));
        prefixDateradioButton.setRolloverEnabled(false);
        prefixDateradioButton.setText("Date");
        panel9.add(prefixDateradioButton);
        prefixDatecomboBox = new JComboBox();
        panel9.add(prefixDatecomboBox);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel10, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixStringradioButton = new JRadioButton();
        prefixStringradioButton.setPreferredSize(new Dimension(140, 19));
        prefixStringradioButton.setText("String");
        panel10.add(prefixStringradioButton);
        prefixStringtextField = new JTextField();
        prefixStringtextField.setPreferredSize(new Dimension(200, 30));
        panel10.add(prefixStringtextField);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel11, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixCameramodelradioButton = new JRadioButton();
        prefixCameramodelradioButton.setPreferredSize(new Dimension(145, 19));
        prefixCameramodelradioButton.setText("Camera model");
        panel11.add(prefixCameramodelradioButton);
        final JLabel label5 = new JLabel();
        label5.setText("${Exif:Model}");
        panel11.add(label5);
        RenamingsuffixPanel = new JPanel();
        RenamingsuffixPanel.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(RenamingsuffixPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Suffix:");
        RenamingsuffixPanel.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDonNotUseradioButton = new JRadioButton();
        suffixDonNotUseradioButton.setMinimumSize(new Dimension(175, 19));
        suffixDonNotUseradioButton.setSelected(true);
        suffixDonNotUseradioButton.setText("Do not use");
        panel12.add(suffixDonNotUseradioButton);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel13, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixStringradioButton = new JRadioButton();
        suffixStringradioButton.setPreferredSize(new Dimension(175, 19));
        suffixStringradioButton.setText("String");
        panel13.add(suffixStringradioButton);
        suffixStringtextField = new JTextField();
        suffixStringtextField.setPreferredSize(new Dimension(200, 30));
        panel13.add(suffixStringtextField);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel14, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDatetimeradioButton = new JRadioButton();
        suffixDatetimeradioButton.setPreferredSize(new Dimension(175, 19));
        suffixDatetimeradioButton.setText("Date_time");
        panel14.add(suffixDatetimeradioButton);
        suffixDatetimecomboBox = new JComboBox();
        panel14.add(suffixDatetimecomboBox);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel15, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDateradioButton = new JRadioButton();
        suffixDateradioButton.setPreferredSize(new Dimension(175, 19));
        suffixDateradioButton.setText("Date");
        panel15.add(suffixDateradioButton);
        suffixDatecomboBox = new JComboBox();
        panel15.add(suffixDatecomboBox);
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel16, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixCameramodelradioButton = new JRadioButton();
        suffixCameramodelradioButton.setPreferredSize(new Dimension(175, 19));
        suffixCameramodelradioButton.setText("Camera model");
        panel16.add(suffixCameramodelradioButton);
        final JLabel label7 = new JLabel();
        label7.setText("${Exif:Model}");
        panel16.add(label7);
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel17, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixOriginalFilenameradioButton = new JRadioButton();
        suffixOriginalFilenameradioButton.setPreferredSize(new Dimension(175, 19));
        suffixOriginalFilenameradioButton.setText("Original filename");
        panel17.add(suffixOriginalFilenameradioButton);
        final JLabel label8 = new JLabel();
        label8.setText("${filename}");
        panel17.add(label8);
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel18, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel18.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        RenamingNumberingPanel = new JPanel();
        RenamingNumberingPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel18.add(RenamingNumberingPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RenamingDuplicateNames = new JLabel();
        Font RenamingDuplicateNamesFont = this.$$$getFont$$$(null, -1, -1, RenamingDuplicateNames.getFont());
        if (RenamingDuplicateNamesFont != null) RenamingDuplicateNames.setFont(RenamingDuplicateNamesFont);
        RenamingDuplicateNames.setText("RenamingDuplicateNames");
        RenamingNumberingPanel.add(RenamingDuplicateNames, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingNumberingPanel.add(panel19, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setPreferredSize(new Dimension(140, 18));
        label9.setText("No. of digits");
        panel19.add(label9);
        DigitscomboBox = new JComboBox();
        panel19.add(DigitscomboBox);
        StartOnImgcomboBox = new JComboBox();
        panel19.add(StartOnImgcomboBox);
        RenamingFileExtPanel = new JPanel();
        RenamingFileExtPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel18.add(RenamingFileExtPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, -1, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("File extension:");
        RenamingFileExtPanel.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        extLeaveradioButton = new JRadioButton();
        extLeaveradioButton.setSelected(true);
        extLeaveradioButton.setText("Leave as is");
        RenamingFileExtPanel.add(extLeaveradioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        makeLowerCaseRadioButton = new JRadioButton();
        makeLowerCaseRadioButton.setText("Make lower case");
        RenamingFileExtPanel.add(makeLowerCaseRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        makeUpperCaseRadioButton = new JRadioButton();
        makeUpperCaseRadioButton.setText("Make upper case");
        RenamingFileExtPanel.add(makeUpperCaseRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(suffixDonNotUseradioButton);
        buttonGroup.add(suffixStringradioButton);
        buttonGroup.add(suffixDatetimeradioButton);
        buttonGroup.add(suffixDateradioButton);
        buttonGroup.add(suffixCameramodelradioButton);
        buttonGroup.add(suffixOriginalFilenameradioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(extLeaveradioButton);
        buttonGroup.add(makeLowerCaseRadioButton);
        buttonGroup.add(makeUpperCaseRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(prefixDate_timeradioButton);
        buttonGroup.add(prefixDateradioButton);
        buttonGroup.add(prefixStringradioButton);
        buttonGroup.add(prefixCameramodelradioButton);
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
        return contentPane;
    }

}
