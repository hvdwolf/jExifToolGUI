package org.hvdw.jexiftoolgui.metadata;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportMetadata extends JDialog {
    private final static Logger logger = LoggerFactory.getLogger(ExportMetadata.class);

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel exportMetaDataUiText;
    private JRadioButton txtRadioButton;
    private JRadioButton tabRadioButton;
    private JRadioButton xmlRadioButton;
    private JRadioButton htmlRadioButton;
    private JRadioButton xmpRadioButton;
    private JRadioButton csvRadioButton;
    private JCheckBox exportAllMetadataCheckBox;
    private JCheckBox exportExifDataCheckBox;
    private JCheckBox exportXmpDataCheckBox;
    private JCheckBox exportGpsDataCheckBox;
    private JCheckBox exportIptcDataCheckBox;
    private JCheckBox exportICCDataCheckBox;
    private JCheckBox useMetadataTagLanguageCheckBox;

    public int[] selectedFilenamesIndices;
    public File[] files;
    public JProgressBar progBar;
    public BufferedWriter writer;

    public ExportMetadata() {
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

        exportAllMetadataCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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
            }
        });
    }

    private void initDialog() {
        //exportMetaDataUiText.setContentType("text/html");
        exportMetaDataUiText.setText(String.format(ProgramTexts.HTML, 420, ResourceBundle.getBundle("translations/program_strings").getString("emd.toptext")));


        ButtonGroup ExportRbBtns = new ButtonGroup();
        ExportRbBtns.add(txtRadioButton);
        ExportRbBtns.add(tabRadioButton);
        ExportRbBtns.add(xmlRadioButton);
        ExportRbBtns.add(htmlRadioButton);
        ExportRbBtns.add(xmpRadioButton);
        ExportRbBtns.add(csvRadioButton);
    }

    private void onOK() {
        // add your code here
        writeExport();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void writeExport() {
        boolean atLeastOneSelected = false;
        List<String> params = new ArrayList<String>();
        List<String> cmdparams = new ArrayList<String>(); // We need this for the csv option
        String filepath = ""; // Again: we need this for the csv option

        boolean isWindows = Utils.isOsFromMicrosoft();

        params.add(Utils.platformExiftool());
        if (useMetadataTagLanguageCheckBox.isSelected()) {
            // Check for chosen metadata language
            if (!"".equals(Utils.getmetadataLanguage())) {
                params.add("-lang");
                params.add(Utils.getmetadataLanguage());
            }
        }
        params.add("-a");
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
        if (exportAllMetadataCheckBox.isSelected()) {
            Message.append(ResourceBundle.getBundle("translations/program_strings").getString("emd.exportall"));
            params.add("-all");
            atLeastOneSelected = true;
        } else {
            Message.append("<ul>");
            if (exportExifDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportexif") + "</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (exportXmpDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.extportxmp") + "</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (exportGpsDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportgps") + "</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (exportIptcDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exportiptc") + "</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (exportICCDataCheckBox.isSelected()) {
                Message.append("<li>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.exporticc") + "</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        Message.append("Is this correct?</html>");
        if (atLeastOneSelected) {
            String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue")};
            int choice = JOptionPane.showOptionDialog(null, Message, ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgtitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (choice == 1) { //Yes
                // Check with export file format has been chosen
                if (txtRadioButton.isSelected()) {
                    params.add("-w!");
                    params.add("txt");
                } else if (tabRadioButton.isSelected()) {
                    params.add("-t");
                    params.add("-w!");
                    params.add("txt");
                } else if (xmlRadioButton.isSelected()) {
                    params.add("-X");
                    params.add("-w!");
                    params.add("xml");
                } else if (htmlRadioButton.isSelected()) {
                    params.add("-h");
                    params.add("-w!");
                    params.add("html");
                /*} else if (xmpRadioButton.isSelected()) {
                    params.add("xmpexport"); */
                } else if (csvRadioButton.isSelected()) {
                    params.add("-csv");
                }

                for (int index : selectedFilenamesIndices) {
                    //logger.info("index: {}  image path: {}", index, files[index].getPath());
                    if (isWindows) {
                        if (csvRadioButton.isSelected()) {
                            params.add("\"" + files[index].getPath().replace("\\", "/") + "\"");
                        } else {
                            params.add(files[index].getPath().replace("\\", "/"));
                        }
                    } else {
                        params.add(files[index].getPath());
                    }
                    // Again necessary for csv
                    filepath = files[index].getParent();
                }

                // Originally for csv we needed the > character to redirect output to a csv file, which we need to treat specially and differently on unixes and windows.
                // We also really needed the shell for it otherwise the > is seen as a file
                // We now read the output into a string and write tht string to file with a bufferedwriter
                if (csvRadioButton.isSelected()) {
                    if (isWindows) {
                        //params.add(" > " + filepath.replace("\\", "/") + "/out.csv");
                        //cmdparams.add("cmd");
                        //cmdparams.add("/c");
                        //cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > \"" + filepath.replace("\\", "/") + "/out.csv\" ");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " "));
                    } else {
                        // logger.info("params to string: {}", params.toString());
                        //cmdparams.add("/bin/sh");
                        //cmdparams.add("-c");
                        //cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > " + filepath + "/out.csv");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " "));
                    }
                } else {
                    cmdparams = params;
                }
                logger.info("cmdparams : {}", cmdparams);


                // Export metadata
                if (!csvRadioButton.isSelected()) {
                    CommandRunner.runCommandWithProgressBar(params, progBar);
                } else {
                    try {
                        String result = CommandRunner.runCommand(params);
                        if (isWindows) {
                            writer = new BufferedWriter(new FileWriter(filepath.replace("\\", "/") + File.separator + "out.csv"));
                        } else {
                            writer = new BufferedWriter(new FileWriter(filepath + File.separator + "out.csv"));
                        }
                        writer.write(result);
                        writer.close();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgnoexporttext"), ResourceBundle.getBundle("translations/program_strings").getString("emd.dlgnoexporttitle"), JOptionPane.WARNING_MESSAGE);
        }
    }


    public void showDialog(int[] selectedIndices, File[] openedfiles, JProgressBar progressBar) {
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;
        progBar = progressBar;

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("exportmetadata.title"));
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
        contentPane.setLayout(new GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(600, 450));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        exportMetaDataUiText = new JLabel();
        exportMetaDataUiText.setEnabled(true);
        exportMetaDataUiText.setRequestFocusEnabled(false);
        exportMetaDataUiText.setText("");
        exportMetaDataUiText.setVerifyInputWhenFocusTarget(false);
        panel3.add(exportMetaDataUiText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 1, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportto"));
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel5.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel5.add(panel6, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtRadioButton = new JRadioButton();
        txtRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(txtRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.txt"));
        panel6.add(txtRadioButton);
        tabRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tabRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.tab"));
        panel6.add(tabRadioButton);
        xmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xml"));
        panel6.add(xmlRadioButton);
        htmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(htmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.html"));
        panel6.add(htmlRadioButton);
        xmpRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmpRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xmp"));
        xmpRadioButton.setVisible(false);
        panel6.add(xmpRadioButton);
        csvRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(csvRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.csv"));
        panel6.add(csvRadioButton);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        exportAllMetadataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportAllMetadataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportall"));
        panel7.add(exportAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(5, 1, new Insets(0, 30, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        exportExifDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportExifDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportexif"));
        panel8.add(exportExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportXmpDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportXmpDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.extportxmp"));
        panel8.add(exportXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportGpsDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportGpsDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportgps"));
        panel8.add(exportGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportIptcDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportIptcDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportiptc"));
        panel8.add(exportIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportICCDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportICCDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exporticc"));
        panel8.add(exportICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        useMetadataTagLanguageCheckBox = new JCheckBox();
        useMetadataTagLanguageCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(useMetadataTagLanguageCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.uselang"));
        contentPane.add(useMetadataTagLanguageCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return contentPane;
    }

}
