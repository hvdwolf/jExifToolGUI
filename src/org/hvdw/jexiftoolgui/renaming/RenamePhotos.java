package org.hvdw.jexiftoolgui.renaming;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.programTexts;

import javax.swing.*;
import java.awt.event.*;

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

    public RenamePhotos() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // button listeners
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
        renamingInfobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        RenamingSourceFolderbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    // Start of the methods
    public void initDialog() {
        RenamingGeneralText.setText( String.format(programTexts.HTML, 650, programTexts.RenamingGeneralText) );
        RenamingDuplicateNames.setText( String.format(programTexts.HTML, 370, programTexts.RenamingDuplicateNames) );

        for (String item: MyConstants.Dates_Times_Strings) {
            prefixDate_timecomboBox.addItem(item);
            suffixDatetimecomboBox.addItem(item);
        }
        for (String item: MyConstants.Dates_Strings) {
            prefixDatecomboBox.addItem(item);
            suffixDatecomboBox.addItem(item);
        }
        for (int digit = 2; digit <= 6; digit++) {
            DigitscomboBox.addItem(digit);
        }
        StartOnImgcomboBox.addItem("start counting on 1st image");
        StartOnImgcomboBox.addItem("start counting on 2nd image");
    }


    private void onOK() {
        // add your code here
        dispose();
    }
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void showDialog() {
        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        initDialog();
        setVisible(true);

    }
}
