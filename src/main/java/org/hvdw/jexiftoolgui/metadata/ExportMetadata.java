package org.hvdw.jexiftoolgui.metadata;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
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
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ExportMetadata.class);

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
    private JRadioButton exportFromUserCombisradioButton;
    private JComboBox userCombicomboBox;
    private JRadioButton catmetadataradioButton;

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
                    userCombicomboBox.setEnabled(false);
                }
            }
        });
        exportFromUserCombisradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (exportFromUserCombisradioButton.isSelected()) {
                    userCombicomboBox.setEnabled(true);
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
                    userCombicomboBox.setEnabled(false);
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
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();

        boolean isWindows = Utils.isOsFromMicrosoft();

        params.add(Utils.platformExiftool());
        if (useMetadataTagLanguageCheckBox.isSelected()) {
            // Check for chosen metadata language
            if (!"".equals(Utils.getmetadataLanguage())) {
                params.add("-lang");
                params.add(Utils.getmetadataLanguage());
                logger.debug("Export in specific metadata language requested lang= {}", Utils.getmetadataLanguage());
            }
        }
        params.add("-a");
        StringBuilder Message = new StringBuilder();

        if (catmetadataradioButton.isSelected()) {
            // which options are selected from the checkboxes?
            Message.append("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgyouhaveselected"));
            if (exportAllMetadataCheckBox.isSelected()) {
                Message.append(ResourceBundle.getBundle("translations/program_strings").getString("emd.exportall"));
                params.add("-all");
                Message.append("<br><br>");
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

        } else { // This is when the user has selected to export from a user combination
            atLeastOneSelected = true; // if we use the drop-down always one item is selected
            String SelectedCombi = userCombicomboBox.getSelectedItem().toString();
            logger.debug("selected metadata set for export {}", SelectedCombi);
            String sql = "select tag from custommetadatasetLines where customset_name='" + SelectedCombi + "' order by rowcount";
            String queryResult = SQLiteJDBC.singleFieldQuery(sql, "tag");
            if (queryResult.length() > 0) {
                String[] customTags = queryResult.split("\\r?\\n");
                logger.debug("queryResult {}", queryResult);
                List<String> tmpparams = new ArrayList<String>();
                for (String customTag : customTags) {
                    logger.trace("customTag {}", customTag);
                    if (customTag.startsWith("-")) {
                        params.add(customTag);
                    } else {
                        params.add("-" + customTag);
                    }
                }
                //String[] tmpArray = new String[tmpparams.size()];
                //params = tmpparams.toArray(tmpArray);
                logger.debug("custom tags for export: {}", params.toString());
            }
            Message.append("<html>" + ResourceBundle.getBundle("translations/program_strings").getString("emd.askusercombi") + "<br>");
            Message.append(SelectedCombi);
            Message.append("<br><br>");
        }
        Message.append(ResourceBundle.getBundle("translations/program_strings").getString("copyd.dlgisthiscorrect") + "</html>");
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

                for (int index : selectedIndices) {
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
                logger.debug("cmdparams : {}", cmdparams.toString());


                // Export metadata
                if (!csvRadioButton.isSelected()) {
                    logger.debug("CSV export requested");
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
                        logger.error("metadata export failed with error {}", e);
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
        //Combobox on User combi edit tab; do as first as we need it again
        String sqlsets = SQLiteJDBC.getdefinedCustomSets();
        String[] views = sqlsets.split("\\r?\\n"); // split on new lines
        userCombicomboBox.setModel(new DefaultComboBoxModel(views));
        userCombicomboBox.setEnabled(false);

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
        contentPane.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(700, 450));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exportMetaDataUiText = new JLabel();
        exportMetaDataUiText.setEnabled(true);
        exportMetaDataUiText.setRequestFocusEnabled(false);
        exportMetaDataUiText.setText("");
        exportMetaDataUiText.setVerifyInputWhenFocusTarget(false);
        panel2.add(exportMetaDataUiText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportto"));
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel4.add(panel5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtRadioButton = new JRadioButton();
        txtRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(txtRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.txt"));
        panel5.add(txtRadioButton);
        tabRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(tabRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.tab"));
        panel5.add(tabRadioButton);
        xmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xml"));
        panel5.add(xmlRadioButton);
        htmlRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(htmlRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.html"));
        panel5.add(htmlRadioButton);
        xmpRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(xmpRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.xmp"));
        xmpRadioButton.setVisible(false);
        panel5.add(xmpRadioButton);
        csvRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(csvRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.csv"));
        panel5.add(csvRadioButton);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        exportAllMetadataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportAllMetadataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportall"));
        panel6.add(exportAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(5, 1, new Insets(0, 15, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        exportExifDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportExifDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportexif"));
        panel7.add(exportExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportXmpDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportXmpDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.extportxmp"));
        panel7.add(exportXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportGpsDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportGpsDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportgps"));
        panel7.add(exportGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportIptcDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportIptcDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportiptc"));
        panel7.add(exportIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportICCDataCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(exportICCDataCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exporticc"));
        panel7.add(exportICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useMetadataTagLanguageCheckBox = new JCheckBox();
        useMetadataTagLanguageCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(useMetadataTagLanguageCheckBox, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.uselang"));
        contentPane.add(useMetadataTagLanguageCheckBox, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel8, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        userCombicomboBox = new JComboBox();
        panel8.add(userCombicomboBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel9, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel9.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel9.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel9.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel10, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.exportoption"));
        panel10.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        catmetadataradioButton = new JRadioButton();
        catmetadataradioButton.setSelected(true);
        this.$$$loadButtonText$$$(catmetadataradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.catmetadata"));
        panel10.add(catmetadataradioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportFromUserCombisradioButton = new JRadioButton();
        this.$$$loadButtonText$$$(exportFromUserCombisradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "emd.usercombi"));
        panel10.add(exportFromUserCombisradioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(exportFromUserCombisradioButton);
        buttonGroup.add(exportFromUserCombisradioButton);
        buttonGroup.add(catmetadataradioButton);
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
