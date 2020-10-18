package org.hvdw.jexiftoolgui.controllers;

import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class LeftPanePopupMenuListener {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LeftPanePopupMenuListener.class);

    public JTable tableListfiles;
    public JPopupMenu myPopupMenu = new JPopupMenu();

    public LeftPanePopupMenuListener( JTable tableListfiles, JPopupMenu myPopupMenu) {

        this.tableListfiles = tableListfiles;
        this.myPopupMenu = myPopupMenu;
        //addPopupMenu();
    }


    /**
     * Insert one or more rows into the table at the clicked row.
     *
     * @param e
     */
    // just an example to further work out
    private void loadDirectory(ActionEvent e) {
        // something here
    }


    /**
     * Add a popup menu to the left tablelistfiles table to handle insertion and
     * deletion of rows.
     */
     public void addPopupMenu() {

        myPopupMenu = new JPopupMenu();
        // Menu items.
        JMenuItem menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loaddirectory")); // index 0
        menuItem.addActionListener(new MyMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loadimages"));           // index 1
        menuItem.addActionListener(new MyMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.displayimages"));           // index 2
        menuItem.addActionListener(new MyMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.compareimgs"));           // index 3
        menuItem.addActionListener(new MyMenuHandler());
        myPopupMenu.add(menuItem);

        menuItem = new JMenuItem(ResourceBundle.getBundle("translations/program_strings").getString("btn.slideshow"));           // index 4
        menuItem.addActionListener(new MyMenuHandler());
        myPopupMenu.add(menuItem);

        tableListfiles.addMouseListener(new MyPopupListener());
     }

	 /**
     * Listen for popup menu invocation.
     * Need both mousePressed and mouseReleased for cross platform support.
     */
    public class MyPopupListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // Enable or disable the "Display Image" and "Sildeshow" menu item
                // depending on whether a row is selected.
                myPopupMenu.getComponent(2).setEnabled(
                        tableListfiles.getSelectedRowCount() > 0);
                myPopupMenu.getComponent(4).setEnabled(
                        tableListfiles.getSelectedRowCount() > 0);
                // Enable or disable "Compare images" >= 2 rows selected
                myPopupMenu.getComponent(3).setEnabled(
                        tableListfiles.getSelectedRowCount() > 1);

                myPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Handle popup menu commands.
     */
    class MyMenuHandler implements ActionListener {
        /**
         * Popup menu actions.
         *
         * @param e the menu event.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loaddirectory"))) {
                //loadDirectory(e);
                //mainScreen.loadImages("images");
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("fmenu.loadimages"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.displayimages"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.compareimgs"))) {
                //loadImages(e);
            } else if (item.getText().equals(ResourceBundle.getBundle("translations/program_strings").getString("btn.slideshow"))) {
                //loadImages(e);
            }
        }
    }
}
