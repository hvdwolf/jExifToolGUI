package org.hvdw.jexiftoolgui.metadata;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class CreateArgsFile extends JDialog {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel CreateArgsMetaDataUiText;
    private JCheckBox exportAllMetadataCheckBox;
    private JCheckBox exportExifDataCheckBox;
    private JCheckBox exportXmpDataCheckBox;
    private JCheckBox exportGpsDataCheckBox;
    private JCheckBox exportIptcDataCheckBox;
    private JCheckBox exportICCDataCheckBox;
    private JCheckBox makeBackupOfOriginalsCheckBox;

    public int[] selectedFilenamesIndices;
    public File[] files;
    public JProgressBar progBar;

    public CreateArgsFile() {
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
        CreateArgsMetaDataUiText.setText(String.format(ProgramTexts.HTML, 270, ProgramTexts.CreateArgsMetaDataUiText));


    }

    private void onOK() {
        // add your code here
        //createfile(selectedFilenamesIndices, files);
        writeFile();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    //public void createfile(int[] selectedFilenamesIndices, File[] files){
    private void writeFile() {
        boolean atLeastOneSelected = false;

        String[] CAparams = MyConstants.CREATE_ARGS_FILE_STRINGS;


        List<String> params = new ArrayList<String>();
        params.add(Utils.platformExiftool());

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
                params.addAll(Arrays.asList(MyConstants.CREATE_ARGS_FILE_STRINGS));
                // images selected
                boolean isWindows = Utils.isOsFromMicrosoft();

                for (int index : selectedFilenamesIndices) {
                    //logger.debug("index: {}  image path: {}", index, files[index].getPath());
                    if (isWindows) {
                        params.add(files[index].getPath().replace("\\", "/"));
                    } else {
                        params.add(files[index].getPath());
                    }
                }
                // export metadata
                CommandRunner.runCommandWithProgressBar(params, progBar);

            }
        } else {
            JOptionPane.showMessageDialog(contentPane, ProgramTexts.NoOptionSelected, "No removal option selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showDialog(int[] selectedIndices, File[] openedfiles, JProgressBar progressBar) {
        //ExportMetadata dialog = new ExportMetadata();
        //setSize(400, 250);
        // first set the public variables I need
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;
        progBar = progressBar;
        //files = myVars.getSelectedFiles();

        //Now do the gui
        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle("Create args file(s)");
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(400, 400));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        CreateArgsMetaDataUiText = new JLabel();
        CreateArgsMetaDataUiText.setEnabled(true);
        CreateArgsMetaDataUiText.setRequestFocusEnabled(false);
        CreateArgsMetaDataUiText.setText("");
        CreateArgsMetaDataUiText.setVerifyInputWhenFocusTarget(false);
        panel3.add(CreateArgsMetaDataUiText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 1, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        exportAllMetadataCheckBox = new JCheckBox();
        exportAllMetadataCheckBox.setText("Add all metadata to args file(s)");
        panel4.add(exportAllMetadataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(5, 1, new Insets(0, 30, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        exportExifDataCheckBox = new JCheckBox();
        exportExifDataCheckBox.setText("Add exif data to args file(s)");
        panel5.add(exportExifDataCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportXmpDataCheckBox = new JCheckBox();
        exportXmpDataCheckBox.setText("Add xmp data to args file(s)");
        panel5.add(exportXmpDataCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportGpsDataCheckBox = new JCheckBox();
        exportGpsDataCheckBox.setText("Add gps data to args file(s)");
        panel5.add(exportGpsDataCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportIptcDataCheckBox = new JCheckBox();
        exportIptcDataCheckBox.setText("Add iptc data to args file(s)");
        panel5.add(exportIptcDataCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportICCDataCheckBox = new JCheckBox();
        exportICCDataCheckBox.setText("Add ICC data to args file(s)");
        panel5.add(exportICCDataCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
