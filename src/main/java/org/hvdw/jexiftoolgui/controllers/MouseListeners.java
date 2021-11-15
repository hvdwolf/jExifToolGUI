package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;


public class MouseListeners {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(MouseListeners.class);

    public static void FileNamesTableMouseListener(JTable tableListfiles, JTable ListexiftoolInfotable, String params[]) {
        // Use the mouse listener for the single cell double-click selection for the left table to be able to
        // display the image in the default viewer

        tableListfiles.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int trow = table.rowAtPoint(point);
                int tcolumn = table.columnAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Utils.displaySelectedImageInExternalViewer();
                    logger.debug("double-click registered from thumbstable from row {} and column {}", String.valueOf(trow), String.valueOf(tcolumn));
                }
                logger.debug("mouselistener: selected cell in row {} and column {}", String.valueOf(trow), String.valueOf(tcolumn));
            }
        });

    }


    public static void filesJListListener(JList iconViewList, JTable ListexiftoolInfotable, JLabel[] mainScreenLabels) {
        iconViewList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

                int[] selectedIndices;
                List<Integer> selectedIndicesList = new ArrayList<Integer>();
                List<Integer> tmpselectedIndices = new ArrayList<>();
                JList list =(JList) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int listCell = list.getSelectedIndex();
                MyVariables.setSelectedRowOrIndex(listCell);
                if (mouseEvent.getClickCount() == 2 && list.getSelectedIndex() != -1) {
                    logger.debug("double-click registered from filesJlist from index {}", String.valueOf(listCell));
                    Utils.displaySelectedImageInExternalViewer();
                }
                logger.debug("single-click mouselistener: selected cell in index {}", String.valueOf(listCell));
                String[] params = MyVariables.getmainScreenParams();
                String res = Utils.getImageInfoFromSelectedFile(params);
                Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);

                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                File[] files = MyVariables.getLoadedFiles();
                if (res.startsWith("jExifToolGUI")) {
                    mainScreenLabels[3].setText(" ");
                } else {
                    mainScreenLabels[3].setText(files[selectedRowOrIndex].getPath());
                }
            }
        });
    }


}
