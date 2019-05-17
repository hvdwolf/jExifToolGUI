package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.programTexts;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveMetadata extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel RemoveMetaDataUiText;
    private JCheckBox removeAllMetadataCheckBox;
    private JCheckBox removeExifDataCheckBox;
    private JCheckBox removeXmpDataCheckBox;
    private JCheckBox removeGpsDataCheckBox;
    private JCheckBox removeIptcDataCheckBox;
    private JCheckBox removeICCDataCheckBox;
    private JCheckBox makeBackupOfOriginalsCheckBox;

    public int[] selectedFilenamesIndices;
    public File[] files;
    Utils myUtils = new Utils();


    public RemoveMetadata() {
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
        removeAllMetadataCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (removeAllMetadataCheckBox.isSelected()) {
                    removeExifDataCheckBox.setSelected(true);
                    removeXmpDataCheckBox.setSelected(true);
                    removeGpsDataCheckBox.setSelected(true);
                    removeIptcDataCheckBox.setSelected(true);
                    removeICCDataCheckBox.setSelected(true);
                } else {
                    removeExifDataCheckBox.setSelected(false);
                    removeXmpDataCheckBox.setSelected(false);
                    removeGpsDataCheckBox.setSelected(false);
                    removeIptcDataCheckBox.setSelected(false);
                    removeICCDataCheckBox.setSelected(false);
                }
            }
        });
    }

    public void initDialog() {
        //RemoveMetaDataUiText.setContentType("text/html");
        RemoveMetaDataUiText.setText(programTexts.RemoveMetaData);


    }

    private void onOK() {
        // add your code here
        Remove();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void Remove(){
	    boolean atLeastOneSelected = false;

        List<String> params = new ArrayList<String>();
        params.add(myUtils.platformExiftool());
	    // which options selected?
		StringBuilder Message = new StringBuilder("<html>You have selected to remove:<br>");
		if (removeAllMetadataCheckBox.isSelected()) {
		    Message.append("All metadata<br><br>");
			params.add("-all=");
			atLeastOneSelected = true;
		} else {
		    Message.append("<ul>");
		    if (removeExifDataCheckBox.isSelected()) {
			    Message.append("<li>the exif data</li>");
				params.add("-exif:all=");
				atLeastOneSelected = true;
			}
			if (removeXmpDataCheckBox.isSelected()) {
			    Message.append("<li>the xmp data</li>");
			    params.add("-xmp:all=");
				atLeastOneSelected = true;
			}
			if (removeGpsDataCheckBox.isSelected()) {
			    Message.append("<li>the gps data</li>");
				params.add("-gps:all=");
				atLeastOneSelected = true;
			}
			if (removeIptcDataCheckBox.isSelected()) {
			    Message.append("<li>the iptc data</li>");
				params.add("-iptc:all=");
				atLeastOneSelected = true;
			}
			if (removeICCDataCheckBox.isSelected()) {
			    Message.append("<li>the ICC data</li>");
				params.add("-icc_profile:all=");
				atLeastOneSelected = true;
			}
			Message.append("</ul><br><br>");
		}	
		Message.append("Is this correct?</html>");
		if (atLeastOneSelected) {
		    String[] options = {"Cancel", "Continue"};
            int choice = JOptionPane.showOptionDialog(null, Message,"You want to remove metadata",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
			    if (!makeBackupOfOriginalsCheckBox.isSelected()) {
				    params.add("-overwrite_original");
				}

                String[] etparams = params.toArray(new String[0]);
                boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
                for (int index: selectedFilenamesIndices) {
                    //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                    if (isWindows) {
                        params.add(files[index].getPath().replace("\\", "/"));
                    } else {
                        params.add(files[index].getPath());
                    }
                }
                // remove metadata
                try {
                    String res = myUtils.runCommand(params);
                    System.out.println(res);
                    myUtils.runCommandOutput(res);
                } catch(IOException | InterruptedException ex) {
                    System.out.println("Error executing command");
                }
            }
		} else {
		    JOptionPane.showMessageDialog(contentPane, programTexts.NoOptionSelected,"No removal option selected",JOptionPane.WARNING_MESSAGE);
		}
    }
    
    public void showDialog(int[] selectedIndices, File[] openedfiles) {
        //ExportMetadata dialog = new ExportMetadata();
        //setSize(400, 250);
        selectedFilenamesIndices = selectedIndices;
        files = openedfiles;

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle("Remove metadata");
        initDialog();
        setVisible(true);
    }
}
