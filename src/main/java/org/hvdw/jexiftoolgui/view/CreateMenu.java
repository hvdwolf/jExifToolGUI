package org.hvdw.jexiftoolgui.view;

import org.hvdw.jexiftoolgui.controllers.MenuActionListener;
import org.hvdw.jexiftoolgui.mainScreen;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class CreateMenu {
    
    private JMenuItem menuItem;

    public void CreateMenuBar(JFrame frame, JPanel rootPanel, JMenuBar menuBar, JMenu myMenu, JLabel OutputLabel, JProgressBar progressBar, JComboBox UserCombiscomboBox) {


        MenuActionListener mal = new MenuActionListener(rootPanel, menuBar, OutputLabel, progressBar, UserCombiscomboBox);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.preferences"));
        myMenu.setMnemonic(KeyEvent.VK_P);
        menuItem.setActionCommand("Preferences");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.mdusercombis"));
        menuItem.setActionCommand("UserMetadata");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.exit"));
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setActionCommand("Exit");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);

        // Rename photos menu
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.renaming"));
        menuBar.add(myMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("rmenu.renamephotos"));
        menuItem.setActionCommand("Rename photos");
        //myMenu.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);

        JMenu exportSidecarSubMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("mmenu.exportsidecar"));
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subexpsidecarmenu.exif"));
        menuItem.setActionCommand("exportexivsidecar");
        menuItem.addActionListener(mal);
        exportSidecarSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subexpsidecarmenu.xmp"));
        menuItem.setActionCommand("exportxmpsidecar");
        menuItem.addActionListener(mal);
        exportSidecarSubMenu.add(menuItem);


        // metadata menu
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.metadata"));
        myMenu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(myMenu);
        //myMenu.addSeparator();
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("mmenu.exportmetadata"));
        menuItem.setActionCommand("Export metadata");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        myMenu.add(exportSidecarSubMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("mmenu.copyallmetadatatoxmpformat"));
        menuItem.setActionCommand("Copy all metadata to xmp format");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("mmenu.removemetadata"));
        menuItem.setActionCommand("Remove metadata");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);

        // Date/time menu
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.datetime"));
        menuBar.add(myMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("dtmenu.shiftdatetime"));
        menuItem.setActionCommand("Shift Date/time");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("dtmenu.modifydatetime"));
        menuItem.setActionCommand("Modify Date/time");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("dtmenu.setfiledatetodatetimeoriginal"));
        menuItem.setActionCommand("Set file date to DateTimeOriginal");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        //myMenu.addSeparator();

        // Other
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.other"));
        menuBar.add(myMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("omenu.repairjpgs"));
        menuItem.setActionCommand("Repair JPGs with corrupted metadata");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("omenu.createargfiles"));
        menuItem.setActionCommand("Create args file(s)");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("omenu.exportallpreviews"));
        menuItem.setActionCommand("Export all previews/thumbs from selected");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);


        // exiftool database
        //myMenu = new JMenu("Database");
        //menuBar.add(myMenu);
        //menuItem = new JMenuItem("Query the exiftool groups/tags database");
        //menuItem.addActionListener(mal);
        // this will be a sub menu of the Help menu containing the help dialogs for the several buttons
        JMenu helpSubMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.helptopicsprogram"));
        //menuItem.setActionCommand("Remove metadata");
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdataexif"));
        menuItem.setActionCommand("editdataexif");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdataxmp"));
        menuItem.setActionCommand("editdataxmp");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdatagps"));
        menuItem.setActionCommand("editdatagps");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdatageotag"));
        menuItem.setActionCommand("editdatageotag");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdatagpano"));
        menuItem.setActionCommand("editdatagpano");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.editdatalens"));
        menuItem.setActionCommand("editdatalens");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.copydata"));
        menuItem.setActionCommand("copydata");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.yourcommands"));
        menuItem.setActionCommand("yourcommands");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.exiftooldb"));
        menuItem.setActionCommand("exiftooldb");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);
        helpSubMenu.addSeparator();
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("subhmenu.menurenaminginfo"));
        menuItem.setActionCommand("menurenaminginfo");
        menuItem.addActionListener(mal);
        helpSubMenu.add(menuItem);



        // Help menu
        myMenu = new JMenu(ResourceBundle.getBundle("translations/program_strings").getString("menu.help"));
        myMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(myMenu);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.jexiftoolguihomepage"));
        menuItem.setActionCommand("jExifToolGUI homepage");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.exiftoolhomepage"));
        menuItem.setActionCommand("ExifTool homepage");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        //menuItem = new JMenuItem("Manual");
        //myMenu.add(menuItem);
        myMenu.addSeparator();
        // Here we add the sub menu with help topics
        myMenu.add(helpSubMenu);
        myMenu.addSeparator();
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.credits"));
        menuItem.setActionCommand("Credits");
        myMenu.add(menuItem);
        menuItem.addActionListener(mal);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.donate"));
        menuItem.setActionCommand("Donate");
        myMenu.add(menuItem);
        menuItem.addActionListener(mal);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.license"));
        menuItem.setActionCommand("License");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.translate"));
        menuItem.setActionCommand("Translate");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.sysproginfo"));
        menuItem.setActionCommand("System/Program info");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.checkfornewversion"));
        menuItem.setActionCommand("Check for new version");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.changelog"));
        menuItem.setActionCommand("Changelog");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        myMenu.addSeparator();
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.aboutexiftool"));
        menuItem.setActionCommand("About ExifTool");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);
        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("hmenu.aboutjexiftoolgui"));
        menuItem.setActionCommand("About jExifToolGUI");
        menuItem.addActionListener(mal);
        myMenu.add(menuItem);

        // Finally add menubar to the frame
        frame.setJMenuBar(menuBar);

    }
}
