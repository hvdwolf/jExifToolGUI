package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.programTexts;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportMetadata extends JDialog {
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

    Utils myUtils = new Utils();
    public int[] selectedFilenamesIndices;
    public File[] files;

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

    public void initDialog() {
        //exportMetaDataUiText.setContentType("text/html");
        exportMetaDataUiText.setText(programTexts.exportMetaDataUiText);


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
		Export();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void Export(){
	    boolean atLeastOneSelected = false;
        List<String> params = new ArrayList<String>();

	    // which options selected?
		StringBuilder Message = new StringBuilder("<html>You have selected to export:<br>");
		if (exportAllMetadataCheckBox.isSelected()) {
		    Message.append("All metadata<br><br>");
			params.add("-a -all");
			atLeastOneSelected = true;
		} else {
		    Message.append("<ul>");
		    if (exportExifDataCheckBox.isSelected()) {
			    Message.append("<li>the exif data</li>");
				params.add("-a -exif:all");
				atLeastOneSelected = true;
			}
			if (exportXmpDataCheckBox.isSelected()) {
			    Message.append("<li>the xmp data</li>");
				params.add("-a -xmp:all");
				atLeastOneSelected = true;
			}
			if (exportGpsDataCheckBox.isSelected()) {
			    Message.append("<li>the gps data</li>");
				params.add("-a -gps:all");
				atLeastOneSelected = true;
			}
			if (exportIptcDataCheckBox.isSelected()) {
			    Message.append("<li>the iptc data</li>");
				params.add("-a -iptc:all");
				atLeastOneSelected = true;
			}
			if (exportICCDataCheckBox.isSelected()) {
			    Message.append("<li>the ICC data</li>");
				params.add("-a -icc_profile:all");
				atLeastOneSelected = true;
			}
			Message.append("</ul><br><br>");
		}	
		Message.append("Is this correct?</html>");
		if (atLeastOneSelected) {
		    String[] options = {"Cancel", "Continue"};
            int choice = JOptionPane.showOptionDialog(null, Message,"You want to export metadata",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
                // Check with export file format has been chosen
                if (txtRadioButton.isSelected()) {
                    params.add("-w!");
                    params.add("txt");
                } else if (tabRadioButton.isSelected()){
                    params.add("-t");
                    params.add("-w!");
                    params.add("txt");
                } else if (xmlRadioButton.isSelected()){
                    params.add("-X");
                    params.add("-w!");
                    params.add("xml");
                } else if (htmlRadioButton.isSelected()){
                    params.add("-h");
                    params.add("-w!");
                    params.add("html");
                } else if (xmpRadioButton.isSelected()) {
                    params.add("xmpexport");
                } else if (csvRadioButton.isSelected()) {
                    params.add("-csv");
                }
                // Export metadata
                String[] etparams = params.toArray(new String[0]);
                //myUtils.runCommand();
            }
		} else {
		    JOptionPane.showMessageDialog(contentPane, programTexts.NoOptionSelected,"No export option selected",JOptionPane.WARNING_MESSAGE);
		}
    }


    public void showDialog(int[] selectedIndices, File[] openedfiles) {
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle("Export metadata");
        initDialog();
        setVisible(true);
    }
}
