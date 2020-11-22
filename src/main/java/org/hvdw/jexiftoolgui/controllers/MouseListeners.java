package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.slf4j.LoggerFactory.getLogger;

public class MouseListeners {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(MouseListeners.class);


    //public static void fileTreeAndFileNamesTableMouseListener(JTable tableListfiles, JTable ListexiftoolInfotable, JTree fileTree, String params[]) {
    public static void fileTreeAndFileNamesTableMouseListener(JTable tableListfiles, JTable ListexiftoolInfotable, String params[]) {
        // Use the mouse listener for the single cell double-click selection for the left table to be able to
        // display the image in the default viewer

        tableListfiles.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Utils.displaySelectedImageInExternalViewer();
                    logger.info("double-click registered from thumbstable");
                }
            }
        });
        // the mouse listener on the tree
/*        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                String fpath;
                int selRow = fileTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = fileTree.getPathForLocation(e.getX(), e.getY());
                if (!(selPath == null)) {
                    logger.info("TreePath {}", selPath.getLastPathComponent());
                    if (selRow != -1) {
                        File fileOrDirectory = new File(selPath.getLastPathComponent().toString());
                        if (!fileOrDirectory.isDirectory()) {
                            if (isOsFromMicrosoft()) {
                                fpath = selPath.getLastPathComponent().toString().replace("\\", "/");
                            } else {
                                fpath = selPath.getLastPathComponent().toString();
                            }
                            MyVariables.setSelectedImagePath(fpath);

                            if (e.getClickCount() == 1) {
                                //mySingleClick(selRow, selPath);
                                //String[] params = whichRBselected();
                                Utils.getImageInfoFromSelectedTreeFile(params, ListexiftoolInfotable);
                            } else if (e.getClickCount() == 2) {
                                //myDoubleClick(selRow, selPath);
                                Utils.displaySelectedImageInExternalViewer();
                            }
                        }
                    }
                }
            }
        };
        fileTree.addMouseListener(ml); */
    }

}
