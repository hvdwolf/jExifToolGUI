package org.hvdw.jexiftoolgui;

import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class PreferencesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextField ExiftoolLocationtextField;
    private JButton ExiftoolLocationbutton;
    private JTextField ImgStartFoldertextField;
    private JButton ImgStartFolderButton;
    private JTextField ArtisttextField;
    private JTextField CopyrightstextField;
    private JCheckBox UseLastOpenedFoldercheckBox;
    private JCheckBox CheckVersioncheckBox;

    // Initialize all the helper classes
    Utils myUtils = new Utils();
    myVariables myVars = new myVariables();
    //AppPreferences AppPrefs = new AppPreferences();
    Preferences prefs = Preferences.userRoot();


    public PreferencesDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);

        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
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
        ExiftoolLocationbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String ETpath = "";
                ETpath = myUtils.exiftoolLocator(contentPane);
                exiftoolpath(contentPane,ExiftoolLocationtextField, ETpath, "preferences");
            }
        });
        ImgStartFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefImgPath(contentPane, ImgStartFoldertextField);
            }
        });
    }

    private void onSave() {
        // add your code here
        savePrefs();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }



    // As the exiftoolLocator is already created in the Utils as startup check we simply leave it there
    // although you can discuss that it can also be a preference if we deviate from the standard one
    // of course we do need the return value which we will get from the listener
    public void exiftoolpath (JPanel myComponent, JTextField myExiftoolTextfield, String ePath, String fromWhere) {
        if (ePath == "cancelled") {
            if (fromWhere == "startup") {
                JOptionPane.showMessageDialog(myComponent, programTexts.cancelledETlocatefromStartup,"Cancelled locate ExifTool",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, programTexts.cancelledETlocatefromPrefs,"Cancelled locate ExifTool",JOptionPane.WARNING_MESSAGE);
            }
        } else if (ePath == "no exiftool binary") {
            if (fromWhere == "startup") {
                JOptionPane.showMessageDialog(myComponent, programTexts.wrongETbinaryfromStartup,"Wrong executable",JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(myComponent, programTexts.wrongETbinaryfromPrefs,"Wrong executable",JOptionPane.WARNING_MESSAGE);
            }
        } else { // Yes. It looks like we have a correct exiftool selected
            // remove all possible line breaks
            ePath = ePath.replace("\n", "").replace("\r", "");
            //prefs.put("exiftool", ePath);
            myExiftoolTextfield.setText(ePath);
        }
    }

    // Locate the default image path, if the user wants it
    public void DefImgPath(JPanel myComponent, JTextField defImgFolder) {
        String SelectedFolder;

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Locate preferred default image folder ...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedFolder = chooser.getSelectedFile().getAbsolutePath();
            defImgFolder.setText(SelectedFolder);
        }
    }

    void savePrefs() {
        System.out.println("Saving the preferences");
        System.out.println("artist " + ArtisttextField.getText());
        System.out.println("copyrights " + CopyrightstextField.getText());
        System.out.println("exiftool " + ExiftoolLocationtextField.getText());
        System.out.println("defaultstartfolder " + ImgStartFoldertextField.getText());
        System.out.println("uselastopenedfolder " + UseLastOpenedFoldercheckBox.isSelected());
        System.out.println("Check for new version on startup " + CheckVersioncheckBox.isSelected());

        if (!ArtisttextField.getText().isEmpty()) {
            prefs.node("artist");
            System.out.println("artist " + ArtisttextField.getText());
            prefs.put("artist", ArtisttextField.getText());
        }
        if (!CopyrightstextField.getText().isEmpty()) {
            prefs.node("copyrights");
            System.out.println("copyrights " + CopyrightstextField.getText());
            prefs.put("copyrights", CopyrightstextField.getText());
        }
        if (!ExiftoolLocationtextField.getText().isEmpty()) {
            prefs.node("exiftool");
            System.out.println("exiftool " + ExiftoolLocationtextField.getText());
            prefs.put("exiftool", ExiftoolLocationtextField.getText());
        }
        if (!ImgStartFoldertextField.getText().isEmpty()) {
            prefs.node("defaultstartfolder");
            System.out.println("defaultstartfolder " + ImgStartFoldertextField.getText());
            prefs.put("defaultstartfolder", ImgStartFoldertextField.getText());
        }
        if (UseLastOpenedFoldercheckBox.isSelected()) {
            prefs.node("uselastopenedfolder");
            prefs.putBoolean("uselastopenedfolder", true);
            System.out.println("uselastopenedfolder" + UseLastOpenedFoldercheckBox.isSelected());
        } else {
            prefs.node("uselastopenedfolder");
            prefs.putBoolean("uselastopenedfolder", false);
        }

        if (CheckVersioncheckBox.isSelected()) {
            prefs.node("versioncheck");
            prefs.putBoolean("versioncheck",true);
        } else {
            prefs.node("versioncheck");
            prefs.putBoolean("versioncheck", false);
        }

        JOptionPane.showMessageDialog(contentPane,"Settings saved","Settings saved",JOptionPane.INFORMATION_MESSAGE);

    }


    void retrievePreferences() {
        // get current preferences
        // exiftool must exist otherwise the app would not start at all
        String exiftool_path = prefs.get("exiftool", "");
        ExiftoolLocationtextField.setText(prefs.get("exiftool", ""));
        // check other settings before retrieving
        boolean defaultstartfolder = prefs.get("defaultstartfolder", null) != null;
        if (defaultstartfolder) {
            ImgStartFoldertextField.setText(prefs.get("defaultstartfolder", ""));
        }
        boolean lastusedfolderset = prefs.getBoolean("uselastopenedfolder", false) != false;
        if (lastusedfolderset) {
            UseLastOpenedFoldercheckBox.setSelected(prefs.getBoolean("uselastopenedfolder", false));
        }

        boolean artist = prefs.get("artist", null) != null;
        if (artist) {
            ArtisttextField.setText(prefs.get("artist", ""));
        }
        boolean copyrights = prefs.get("copyrights", null) != null;
        if (copyrights) {
            CopyrightstextField.setText(prefs.get("copyrights", ""));
        }
        boolean versioncheckset = prefs.getBoolean("versioncheck", false) != false;
        if (versioncheckset) {
            CheckVersioncheckBox.setSelected(prefs.getBoolean("versioncheck", false));
        }

    }
            // The  main" function of this class
    public void showDialog() {
        setSize(700, 400);
        double x = getParent().getBounds().getCenterX();
        double y = getParent().getBounds().getCenterY();
        //setLocation((int) x - getWidth() / 2, (int) y - getHeight() / 2);
        setLocationRelativeTo(null);
        retrievePreferences();
        setVisible(true);

    }

}
