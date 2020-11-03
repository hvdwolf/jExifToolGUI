package org.hvdw.jexiftoolgui.view;

import ch.qos.logback.classic.Logger;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.slf4j.LoggerFactory.getLogger;

public class CompareImagesWindow {
    private final static Logger logger = (Logger) getLogger(CompareImagesWindow.class);

    public static void Initialize(List<String[]> allMetadata) {
        ImageIcon icon = null;

        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        // Quick test
        /*for (String metadata[] : allMetadata) {
            logger.info("array for table {}", Arrays.toString(metadata));
        }*/

        // Define the frame, the ScrollPanel with the table, the buttonpanel with the close button
        JFrame frame = new JFrame();
        frame.setTitle(ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.comparetitle"));
        frame.setIconImage(Utils.getFrameIcon());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel ciwRootPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton ciwCloseButton = new JButton();
        ciwCloseButton.setText(ResourceBundle.getBundle("translations/program_strings").getString("dlg.close"));
        ciwCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.dispose();
            }
        });
        buttonPanel.add(ciwCloseButton);

        //Create the table header for both tables
        List<String> theader = new ArrayList<String>();
        theader.add(ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablegroup"));
        theader.add(ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tabletag"));
        for (int index : selectedIndices) {
            String filename = files[index].getName();
            theader.add(files[index].getName());
        }
        String[] tableheader = theader.stream().toArray(String[]::new);

        // now we create the metadatatable
        JTable ciwTable = new JTable();
        DefaultTableModel model = (DefaultTableModel) ciwTable.getModel();
        ciwTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            protected void setValue(Object value){
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setHorizontalAlignment(JLabel.CENTER);
                    setText("");
                } else {
                    setIcon(null);
                    setHorizontalTextPosition(JLabel.LEFT);
                    setHorizontalAlignment(JLabel.LEFT);
                    super.setValue(value);
                }
            }
        });

        model.setRowCount(0);
        model.fireTableDataChanged();
        ciwTable.clearSelection();
        ciwTable.setCellSelectionEnabled(false);
        // Below line makes table uneditable (read-only)
        ciwTable.setDefaultEditor(Object.class, null);
        model.setColumnIdentifiers(tableheader);
        // Add thumbnails for files to table
        Object[] ImgFilenameRow = new Object[(selectedIndices.length + 2)];
        ImgFilenameRow[0] = "";
        ImgFilenameRow[1] = "";
        for (int index : selectedIndices) {
            File file = files[index];
            icon = ImageFunctions.analyzeImageAndCreateIcon(file);
            ImgFilenameRow[(index + 2)] = icon;
        }
        ciwTable.setRowHeight(0,150);
        model.addRow(ImgFilenameRow);

        // Add the metadata to the table
        for (String[] metadata : allMetadata) {
            model.addRow(metadata);
        }
        ciwTable.setRowHeight(0,150);
        ciwTable.repaint();

        JScrollPane ciwScrollPanel = new JScrollPane(ciwTable);
        //ciwScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ciwScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Add the panels to the big container panel
        ciwRootPanel.add(ciwScrollPanel, BorderLayout.CENTER);
        ciwRootPanel.add(buttonPanel, BorderLayout.PAGE_END);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrwidth = screenSize.width;
        MyVariables.setScreenWidth(screenSize.width);
        int scrheight = screenSize.height;
        MyVariables.setScreenHeight(screenSize.height);
        frame.add(ciwRootPanel);
        frame.setSize(screenSize.width, screenSize.height-50);

        //frame.pack();
        frame.setVisible(true);
    }

}
