package org.hvdw.jexiftoolgui.renaming;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.PreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class RenamePhotos extends JDialog {
    private JPanel rootRenamingPane;
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
    private JRadioButton suffixCityNameradioButton;
    private JRadioButton suffixLocationradioButton;
    private JRadioButton suffixISOradioButton;
    private JRadioButton suffixFocalLengthradioButton;

    private final static IPreferencesFacade prefs = PreferencesFacade.defaultInstance;
    private final static Logger logger = (Logger) LoggerFactory.getLogger(RenamePhotos.class);

    private int[] selectedFilenamesIndices;
    public File[] files;
    boolean selected_files;

    public RenamePhotos() {
        StringBuilder res = new StringBuilder();

        setContentPane(rootRenamingPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setIconImage(Utils.getFrameIcon());
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        rootRenamingPane.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));

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
        rootRenamingPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // This is the info button on the Rename panel
        renamingInfobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(rootRenamingPane, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("renamingtext")), ResourceBundle.getBundle("translations/program_help_texts").getString("renamingtitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        RenamingSourceFolderbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ImgPath = getImagePath(rootRenamingPane);
                if (!"".equals(ImgPath)) {
                    RenamingSourceFoldertextField.setText(ImgPath);
                }
            }
        });
    }

    public String getImagePath(JPanel myComponent) {
        String SelectedFolder;
        //String prefFileDialog = prefs.getByKey(PREFERRED_FILEDIALOG, "jfilechooser");

        String startFolder = StandardFileIO.getFolderPathToOpenBasedOnPreferences();
        final JFileChooser chooser = new JFileChooser(startFolder);
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        chooser.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        chooser.setDialogTitle(ResourceBundle.getBundle("translations/program_strings").getString("rph.locateimgfolder"));
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
        RenamingGeneralText.setText(String.format(ProgramTexts.HTML, 650, ResourceBundle.getBundle("translations/program_strings").getString("rph.toptext")));
        RenamingDuplicateNames.setText(String.format(ProgramTexts.HTML, 370, ResourceBundle.getBundle("translations/program_strings").getString("rph.duplicatestext")));
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
        StartOnImgcomboBox.addItem(ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitszero"));
        StartOnImgcomboBox.addItem(ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitsone"));
        StartOnImgcomboBox.addItem(ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitstwo"));
        StartOnImgcomboBox.setSelectedIndex(1);
    }

    private void rename_photos() {
        //int[] selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
        //File[] files = MyVariables.getLoadedFiles();
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
        String exiftool = Utils.platformExiftool();
        boolean isWindows = Utils.isOsFromMicrosoft();


        if (("".equals(RenamingSourceFoldertextField.getText())) && (!selected_files)) { // Empty folder string and no files selected
            JOptionPane.showMessageDialog(null, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("translations/program_strings").getString("rph.noimgsnopathtext")), ResourceBundle.getBundle("translations/program_strings").getString("rph.noimgsnopathtitle"), JOptionPane.WARNING_MESSAGE);
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
                } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYMMDD HHMMSS") {
                    suffix_message = "YYYMMDD HHMMSS";
                    suffixformat = "%Y%m%d %H%M%S";
                } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY_MM_DD HH_MM_SS") {
                    suffix_message = "YYYY_MM_DD HH_MM_SS";
                    suffixformat = "%Y_%m_%d %H_%M_%S";
                } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY-MM-DD HH-MM-SS") {
                    suffix_message = "YYYY-MM-DD HH-MM-SS";
                    suffixformat = "%Y-%m-%d %H-%M-%S";
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
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYMMDD HHMMSS") {
                        suffix_message = "YYYMMDD HHMMSS";
                        suffixformat = "%Y%m%d %H%M%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY_MM_DD HH_MM_SS") {
                        suffix_message = "YYYY_MM_DD HH_MM_SS";
                        suffixformat = "%Y_%m_%d %H_%M_%S";
                    } else if (suffixDatetimecomboBox.getSelectedItem() == "YYYY-MM-DD HH-MM-SS") {
                        suffix_message = "YYYY-MM-DD HH-MM-SS";
                        suffixformat = "%Y-%m-%d %H-%M-%S";
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
                } else if (suffixLocationradioButton.isSelected()) {
                    suffix_message = "${xmp:location}";
                    suffix = "${xmp:location}";
                    suffixformat = "";
                } else if (suffixCityNameradioButton.isSelected()) {
                    suffix_message = "${xmp:city}";
                    suffix = "${xmp:city}";
                    suffixformat = "";
                } else if (suffixISOradioButton.isSelected()) {
                    suffix_message = "${exif:iso}ISO";
                    suffix = "${exif:iso}ISO";
                    suffixformat = "";
                } else if (suffixFocalLengthradioButton.isSelected()) {
                    suffix_message = "${exif:focallengthin35mmformat}";
                    suffix = "${exif:focallengthin35mmformat}";
                    suffixformat = "";
                } else if (suffixOriginalFilenameradioButton.isSelected()) {
                    //suffix_message = "${filename}";
                    //suffix = "${filename}";
                    suffix_message = "${BaseName}";
                    suffix = "${BaseName}";
                    suffixformat = "";
                }
            }

            // Now the extension: Does the user want lowercase, uppercase or doesn't care (leave as is)?
            if (extLeaveradioButton.isSelected()) {
                rename_extension = ".%e";
                extension_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.fileextleave");
            } else if (makeLowerCaseRadioButton.isSelected()) {
                rename_extension = ".%le";
                extension_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.fileextlower");
            } else if (makeUpperCaseRadioButton.isSelected()) {
                rename_extension = ".%ue";
                extension_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.fileextupper");
            }

            // Finally: Does the user want to start counting as of the first image or starting on the second image
            if (StartOnImgcomboBox.getSelectedIndex() == 0) {
                startcounting = "";
                logger.info("Do not count");
                startcounting_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitszero");
            } else if (StartOnImgcomboBox.getSelectedIndex() == 1) {
                startcounting = "nc";
                logger.info("start counting on 1st image");
                startcounting_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitsone");
            } else {
                startcounting = "c";
                logger.info("start counting on 2nd image");
                startcounting_message = ResourceBundle.getBundle("translations/program_strings").getString("rph.nooffidgitstwo");
            }

            // Now ask the user whether the selected options are really what he/she wants
            String dialogMessage = ResourceBundle.getBundle("translations/program_strings").getString("rph.youselected") + "\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("rph.asprefix") + " ";
            dialogMessage += prefix_message;
            if (!suffixDonNotUseradioButton.isSelected()) {
                dialogMessage += "\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("rph.assuffix") + " ";
                dialogMessage += suffix_message;
            }
            dialogMessage += "\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("rph.forcounting") + " ";
            dialogMessage += startcounting_message;
            dialogMessage += "\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("rph.forextension") + " ";
            dialogMessage += extension_message;

            String[] options = {ResourceBundle.getBundle("translations/program_strings").getString("dlg.continue"), ResourceBundle.getBundle("translations/program_strings").getString("dlg.cancel")};
            int choice = JOptionPane.showOptionDialog(null, dialogMessage, ResourceBundle.getBundle("translations/program_strings").getString("rph.selectedoptionstitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) { //Continue
                //cmdparams.add("-overwrite_original_in_place");
                // We need to first cmdparams here, as sometimes the string does not contains spaces and sometimes it does
                // When it does have spaces we need to create an addition cdmparams

                // Check if wee need to preserver the file modify date
                boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
                if ((suffixDonNotUseradioButton.isSelected()) && (prefixStringradioButton.isSelected())) {
                    String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
                    String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
                    // string as prefix and no suffix
                    if (isWindows) {
                        cmdparams.add(Utils.platformExiftool().replace("\\", "/"));
                        if (preserveModifyDate) {
                            cmdparams.add("-preserve");
                            cmdparams.add("-config");
                            cmdparams.add(strjexiftoolguifolder + File.separator + "extra_functions.config");
                        }
                        exifcommands = new StringBuilder("\"-FileName=" + prefix);
                    } else {
                        // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                        cmdparams.add("/bin/sh");
                        cmdparams.add("-c");
                        String configFile = " -config" + strjexiftoolguifolder + File.separator + "extra_functions.config ";
                        if (preserveModifyDate) {
                            exifcommands = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + configFile + " -preserve '-FileName=" + prefix);
                        } else {
                            exifcommands = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + configFile + " '-FileName=" + prefix);
                        }
                    }
                } else { // Or a suffix or date(time), or both
                    if (isWindows) {
                        cmdparams.add(Utils.platformExiftool().replace("\\", "/"));
                        exifcommands = new StringBuilder("\"-FileName<" + prefix);
                    } else {
                        // The < or > redirect options cannot directly be used within a single param on unixes/linuxes
                        cmdparams.add("/bin/sh");
                        cmdparams.add("-c");
                        exifcommands = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + " '-FileName<" + prefix);
                    }
                }
                if (!suffixDonNotUseradioButton.isSelected()) {
                    exifcommands.append("_" + suffix);
                }
                //logger.info("combobox selection " + StartOnImgcomboBox.getSelectedItem());
                if ((StartOnImgcomboBox.getSelectedIndex() > 0)) {
                    if (fulldatetime) {
                        // This means that the autonumber should only work on images that have the same full datetime
                        exifcommands.append("%-" + DigitscomboBox.getSelectedItem() + startcounting);
                    } else {
                        exifcommands.append("%-." + DigitscomboBox.getSelectedItem() + startcounting);
                    }
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
                // Check whether the directory has been povided (takes precedence) or files have been selected from the main page
                if (!"".equals(RenamingSourceFoldertextField.getText())) { // not empty, so a selected folder
                    // Add the dir
                    if (isWindows) {
                        cmdparams.add(exifcommands.toString());
                        cmdparams.add(RenamingSourceFoldertextField.getText().replace(File.separator, "/"));
                    } else {
                        exifcommands.append(" " + RenamingSourceFoldertextField.getText().replace(File.separator, "/"));
                        cmdparams.add(exifcommands.toString());
                    }
                } else { // we have images selected in the main screen
                    if (isWindows) {
                        cmdparams.add(exifcommands.toString());
                        for (int index : selectedFilenamesIndices) {
                            cmdparams.add(files[index].getPath().replace("\\", "/").replace("(", "\\(").replace(")", "\\)"));
                        }
                    } else {
                        for (int index : selectedFilenamesIndices) {
                            exifcommands.append(" '" + files[index].getPath().replace("(", "\\(").replace(")", "\\)") + "'");
                        }
                        cmdparams.add(exifcommands.toString());
                    }

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

    public void showDialog(boolean files_selected) {
        if (files_selected) {
            selected_files = true;
            selectedFilenamesIndices = MyVariables.getSelectedFilenamesIndices();
            files = MyVariables.getLoadedFiles();
        }

        progressBar.setVisible(false);

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("renamephotos.title"));
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
        rootRenamingPane = new JPanel();
        rootRenamingPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        rootRenamingPane.setPreferredSize(new Dimension(1100, 900));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        rootRenamingPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.close"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD | Font.ITALIC, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16776961));
        label1.setMinimumSize(new Dimension(400, 17));
        label1.setPreferredSize(new Dimension(400, 17));
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.nameexample"));
        panel3.add(label1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel1.add(progressBar, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, 8), new Dimension(85, 15), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootRenamingPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RenamingGeneralText = new JLabel();
        this.$$$loadLabelText$$$(RenamingGeneralText, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.toptext"));
        panel5.add(RenamingGeneralText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.sourcefolder"));
        panel5.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel5.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RenamingSourceFolderbutton = new JButton();
        this.$$$loadButtonText$$$(RenamingSourceFolderbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.browse"));
        panel6.add(RenamingSourceFolderbutton);
        RenamingSourceFoldertextField = new JTextField();
        RenamingSourceFoldertextField.setPreferredSize(new Dimension(600, 30));
        panel6.add(RenamingSourceFoldertextField);
        renamingInfobutton = new JButton();
        this.$$$loadButtonText$$$(renamingInfobutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        panel6.add(renamingInfobutton);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        RenamingOptions = new JPanel();
        RenamingOptions.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(RenamingOptions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.selrenameoptions"));
        RenamingOptions.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.prefix"));
        RenamingOptions.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixDate_timeradioButton = new JRadioButton();
        prefixDate_timeradioButton.setPreferredSize(new Dimension(140, 19));
        prefixDate_timeradioButton.setSelected(true);
        this.$$$loadButtonText$$$(prefixDate_timeradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.datetime"));
        panel8.add(prefixDate_timeradioButton);
        prefixDate_timecomboBox = new JComboBox();
        panel8.add(prefixDate_timecomboBox);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixDateradioButton = new JRadioButton();
        prefixDateradioButton.setPreferredSize(new Dimension(140, 19));
        prefixDateradioButton.setRolloverEnabled(false);
        this.$$$loadButtonText$$$(prefixDateradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.date"));
        panel9.add(prefixDateradioButton);
        prefixDatecomboBox = new JComboBox();
        panel9.add(prefixDatecomboBox);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel10, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixStringradioButton = new JRadioButton();
        prefixStringradioButton.setPreferredSize(new Dimension(140, 19));
        this.$$$loadButtonText$$$(prefixStringradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.string"));
        panel10.add(prefixStringradioButton);
        prefixStringtextField = new JTextField();
        prefixStringtextField.setPreferredSize(new Dimension(200, 30));
        panel10.add(prefixStringtextField);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingOptions.add(panel11, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prefixCameramodelradioButton = new JRadioButton();
        prefixCameramodelradioButton.setPreferredSize(new Dimension(145, 19));
        this.$$$loadButtonText$$$(prefixCameramodelradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.cammodel"));
        panel11.add(prefixCameramodelradioButton);
        final JLabel label5 = new JLabel();
        label5.setText("${Exif:Model}");
        panel11.add(label5);
        RenamingsuffixPanel = new JPanel();
        RenamingsuffixPanel.setLayout(new GridLayoutManager(11, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(RenamingsuffixPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.suffix"));
        RenamingsuffixPanel.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDonNotUseradioButton = new JRadioButton();
        suffixDonNotUseradioButton.setMinimumSize(new Dimension(190, 19));
        suffixDonNotUseradioButton.setSelected(true);
        this.$$$loadButtonText$$$(suffixDonNotUseradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.donotuse"));
        panel12.add(suffixDonNotUseradioButton);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel13, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixStringradioButton = new JRadioButton();
        suffixStringradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixStringradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.string"));
        panel13.add(suffixStringradioButton);
        suffixStringtextField = new JTextField();
        suffixStringtextField.setPreferredSize(new Dimension(200, 30));
        panel13.add(suffixStringtextField);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel14, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDatetimeradioButton = new JRadioButton();
        suffixDatetimeradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixDatetimeradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.datetime"));
        panel14.add(suffixDatetimeradioButton);
        suffixDatetimecomboBox = new JComboBox();
        panel14.add(suffixDatetimecomboBox);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel15, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixDateradioButton = new JRadioButton();
        suffixDateradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixDateradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.date"));
        panel15.add(suffixDateradioButton);
        suffixDatecomboBox = new JComboBox();
        panel15.add(suffixDatecomboBox);
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel16, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixCameramodelradioButton = new JRadioButton();
        suffixCameramodelradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixCameramodelradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.cammodel"));
        panel16.add(suffixCameramodelradioButton);
        final JLabel label7 = new JLabel();
        label7.setText("${Exif:Model}");
        panel16.add(label7);
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel17, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixOriginalFilenameradioButton = new JRadioButton();
        suffixOriginalFilenameradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixOriginalFilenameradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.orgfilename"));
        panel17.add(suffixOriginalFilenameradioButton);
        final JLabel label8 = new JLabel();
        label8.setText("BaseName");
        panel17.add(label8);
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel18, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixCityNameradioButton = new JRadioButton();
        suffixCityNameradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixCityNameradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.cityname"));
        panel18.add(suffixCityNameradioButton);
        final JLabel label9 = new JLabel();
        label9.setText("${Xmp:City}");
        panel18.add(label9);
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel19, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixLocationradioButton = new JRadioButton();
        suffixLocationradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixLocationradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.location"));
        panel19.add(suffixLocationradioButton);
        final JLabel label10 = new JLabel();
        label10.setText("${Xmp:Location}");
        panel19.add(label10);
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel20, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixISOradioButton = new JRadioButton();
        suffixISOradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixISOradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.iso"));
        panel20.add(suffixISOradioButton);
        final JLabel label11 = new JLabel();
        label11.setText("${Exif:ISO}");
        panel20.add(label11);
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingsuffixPanel.add(panel21, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suffixFocalLengthradioButton = new JRadioButton();
        suffixFocalLengthradioButton.setPreferredSize(new Dimension(300, 19));
        this.$$$loadButtonText$$$(suffixFocalLengthradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.foclength35mm"));
        panel21.add(suffixFocalLengthradioButton);
        final JLabel label12 = new JLabel();
        label12.setText("${exif:focallengthin35mmformat}");
        panel21.add(label12);
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel22, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        RenamingNumberingPanel = new JPanel();
        RenamingNumberingPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel22.add(RenamingNumberingPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RenamingDuplicateNames = new JLabel();
        Font RenamingDuplicateNamesFont = this.$$$getFont$$$(null, -1, -1, RenamingDuplicateNames.getFont());
        if (RenamingDuplicateNamesFont != null) RenamingDuplicateNames.setFont(RenamingDuplicateNamesFont);
        this.$$$loadLabelText$$$(RenamingDuplicateNames, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.duplicatestext"));
        RenamingNumberingPanel.add(RenamingDuplicateNames, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        RenamingNumberingPanel.add(panel23, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setPreferredSize(new Dimension(140, 18));
        this.$$$loadLabelText$$$(label13, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.noofdigits"));
        panel23.add(label13);
        DigitscomboBox = new JComboBox();
        panel23.add(DigitscomboBox);
        StartOnImgcomboBox = new JComboBox();
        panel23.add(StartOnImgcomboBox);
        RenamingFileExtPanel = new JPanel();
        RenamingFileExtPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel22.add(RenamingFileExtPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, -1, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        this.$$$loadLabelText$$$(label14, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.filextension"));
        RenamingFileExtPanel.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        extLeaveradioButton = new JRadioButton();
        extLeaveradioButton.setSelected(true);
        this.$$$loadButtonText$$$(extLeaveradioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.fileextleave"));
        RenamingFileExtPanel.add(extLeaveradioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        makeLowerCaseRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(makeLowerCaseRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.fileextlower"));
        RenamingFileExtPanel.add(makeLowerCaseRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        makeUpperCaseRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(makeUpperCaseRadioButton, this.$$$getMessageFromBundle$$$("translations/program_strings", "rph.fileextupper"));
        RenamingFileExtPanel.add(makeUpperCaseRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(suffixDonNotUseradioButton);
        buttonGroup.add(suffixStringradioButton);
        buttonGroup.add(suffixDatetimeradioButton);
        buttonGroup.add(suffixDateradioButton);
        buttonGroup.add(suffixCameramodelradioButton);
        buttonGroup.add(suffixOriginalFilenameradioButton);
        buttonGroup.add(suffixCityNameradioButton);
        buttonGroup.add(suffixLocationradioButton);
        buttonGroup.add(suffixISOradioButton);
        buttonGroup.add(suffixFocalLengthradioButton);
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
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
        return rootRenamingPane;
    }

}
