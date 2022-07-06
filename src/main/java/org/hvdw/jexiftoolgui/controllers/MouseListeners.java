package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;


public class MouseListeners {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(MouseListeners.class);

    public static void FileNamesTableMouseListener(JTable tableListfiles, JTable ListexiftoolInfotable, String[] params, JLabel OutputLabel) {
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
                OutputLabel.setText("");
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
                mainScreenLabels[0].setText("");
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



    public static class iconViewListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            // Perfectly working row selection method of first program
            List<Integer> tmpselectedIndices = new ArrayList<>();
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            List<Integer> selectedIndicesList = new ArrayList<>();
            int[] selectedIndices = null;

            if (lsm.isSelectionEmpty()) {
                logger.debug("no grid view index selected");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        tmpselectedIndices.add(i);
                        int SelectedRowOrIndex = i;
                        MyVariables.setSelectedRowOrIndex(i);
                        //logger.info("MyVariables.getSelectedRowOrIndex() {}", MyVariables.getSelectedRowOrIndex());
                    }
                }
                String[] params = MyVariables.getmainScreenParams();
                String res = Utils.getImageInfoFromSelectedFile(params);
                //Utils.displayInfoForSelectedImage(res, ListexiftoolInfotable);

                int selectedRowOrIndex = MyVariables.getSelectedRowOrIndex();
                File[] files = MyVariables.getLoadedFiles();
                /*if (res.startsWith("jExifToolGUI")) {
                    lblFileNamePath.setText(" ");
                } else {
                    lblFileNamePath.setText(files[selectedRowOrIndex].getPath());
                }*/

                selectedIndices = tmpselectedIndices.stream().mapToInt(Integer::intValue).toArray();
                logger.debug("Selected grid indices: {}", tmpselectedIndices);
                //logger.info("Save indices {}", Arrays.toString(selectedIndices));
                selectedIndicesList = tmpselectedIndices;
                MyVariables.setselectedIndicesList(selectedIndicesList);
                MyVariables.setSelectedFilenamesIndices(selectedIndices);
            }
        }
    }


}
