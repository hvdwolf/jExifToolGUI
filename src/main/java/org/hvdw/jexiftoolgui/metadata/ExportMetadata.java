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
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportMetadata extends JDialog {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

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

    public int[] selectedFilenamesIndices;
    public File[] files;
    public JProgressBar progBar;

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
        exportMetaDataUiText.setText(String.format(ProgramTexts.HTML, 320, ProgramTexts.exportMetaDataUiText));


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
        params.add("-a");
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>You have selected to export:<br>");
        if (exportAllMetadataCheckBox.isSelected()) {
            Message.append("All metadata<br><br>");
            params.add("-all");
            atLeastOneSelected = true;
        } else {
            Message.append("<ul>");
            if (exportExifDataCheckBox.isSelected()) {
                Message.append("<li>the exif data</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (exportXmpDataCheckBox.isSelected()) {
                Message.append("<li>the xmp data</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (exportGpsDataCheckBox.isSelected()) {
                Message.append("<li>the gps data</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (exportIptcDataCheckBox.isSelected()) {
                Message.append("<li>the iptc data</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (exportICCDataCheckBox.isSelected()) {
                Message.append("<li>the ICC data</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        Message.append("Is this correct?</html>");
        if (atLeastOneSelected) {
            String[] options = {"Cancel", "Continue"};
            int choice = JOptionPane.showOptionDialog(null, Message, "You want to export metadata",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
                } else if (xmpRadioButton.isSelected()) {
                    params.add("xmpexport");
                } else if (csvRadioButton.isSelected()) {
                    params.add("-csv");
                }

                for (int index : selectedFilenamesIndices) {
                    //logger.debug("index: {}  image path: {}", index, files[index].getPath());
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

                // for csv we need the > character which we need to treat specially and differently on unixes and windows.
                // We also really need the shell for it otherwise the > is seen as a file
                if (csvRadioButton.isSelected()) {
                    if (isWindows) {
                        //params.add(" > " + filepath.replace("\\", "/") + "/out.csv");
                        cmdparams.add("cmd");
                        cmdparams.add("/c");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > \"" + filepath.replace("\\", "/") + "/out.csv\" ");
                    } else {
                        // logger.debug("params to string: {}", params.toString());
                        cmdparams.add("/bin/sh");
                        cmdparams.add("-c");
                        cmdparams.add(params.toString().substring(1, params.toString().length() - 1).replaceAll(", ", " ") + " > " + filepath + "/out.csv ");
                    }
                } else {
                    cmdparams = params;
                }
                logger.debug("cmdparams : {}", cmdparams);


                // Export metadata
                CommandRunner.runCommandWithProgressBar(params, progBar);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, ProgramTexts.NoOptionSelected, "No export option selected", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void showDialog(int[] selectedIndices, File[] openedfiles, JProgressBar progressBar) {
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;
        progBar = progressBar;

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle("Export metadata");
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
        contentPane.setPreferredSize(new Dimension(450, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        contentPane.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Export to:");
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
        txtRadioButton.setText("Txt");
        panel6.add(txtRadioButton);
        tabRadioButton = new JRadioButton();
        tabRadioButton.setText("tab");
        panel6.add(tabRadioButton);
        xmlRadioButton = new JRadioButton();
        xmlRadioButton.setText("xml");
        panel6.add(xmlRadioButton);
        htmlRadioButton = new JRadioButton();
        htmlRadioButton.setText("html");
        panel6.add(htmlRadioButton);
        xmpRadioButton = new JRadioButton();
        xmpRadioButton.setText("xmp");
        panel6.add(xmpRadioButton);
        csvRadioButton = new JRadioButton();
        csvRadioButton.setText("csv");
        panel6.add(csvRadioButton);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        exportAllMetadataCheckBox = new JCheckBox();
        exportAllMetadataCheckBox.setText("Export all metadata");
        panel7.add(exportAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(5, 1, new Insets(0, 30, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        exportExifDataCheckBox = new JCheckBox();
        exportExifDataCheckBox.setText("Export exif data");
        panel8.add(exportExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportXmpDataCheckBox = new JCheckBox();
        exportXmpDataCheckBox.setText("Export xmp data");
        panel8.add(exportXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportGpsDataCheckBox = new JCheckBox();
        exportGpsDataCheckBox.setText("Export gps data");
        panel8.add(exportGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportIptcDataCheckBox = new JCheckBox();
        exportIptcDataCheckBox.setText("Export iptc data");
        panel8.add(exportIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportICCDataCheckBox = new JCheckBox();
        exportICCDataCheckBox.setText("Export ICC data");
        panel8.add(exportICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
