package org.hvdw.jexiftoolgui.view;

import ch.qos.logback.classic.Logger;
import com.itextpdf.layout.element.Table;
import org.hvdw.jexiftoolgui.ExportToPDF;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

public class CompareImagesWindow {
    private final static Logger logger = (Logger) getLogger(CompareImagesWindow.class);

    public static void Initialize(List<String[]> tableMetadata, List<String[]> allMetadata)  {
        ImageIcon icon = null;

        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();

        // Quick test
        /*for (int index : selectedIndices) {
            File file = files[index];
            String filename = file.getName();
            logger.info("index {} filename {}", index, filename);
        }
        for (String metadata[] : tableMetadata) {
            logger.info("array for table {}", Arrays.toString(metadata));
         }
        try {
            FileWriter fw = new FileWriter("/tmp/tablemetadata.txt");
            for (String metadata[] : tableMetadata) {
                fw.write(Arrays.toString(metadata) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw = new FileWriter("/tmp/allmetadata.txt");
            for (String metadata[] : allMetadata) {
                fw.write(Arrays.toString(metadata) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // Define the frame, the ScrollPanel with the table, the buttonpanel with the close button
        JFrame frame = new JFrame();
        frame.setTitle(ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.comparetitle"));
        frame.setIconImage(Utils.getFrameIcon());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel ciwRootPanel = new JPanel(new BorderLayout());
        //JPanel ciwRootPanel = new JPanel(new GridLayout(2,1));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton ciwCloseButton = new JButton();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setSize(200, 30);
        progressBar.setVisible(false);
        JLabel outputLabel = new JLabel();
        outputLabel.setText("");
        //outputLabel.setVisible(false);
        ciwCloseButton.setText(ResourceBundle.getBundle("translations/program_strings").getString("dlg.close"));
        ciwCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.dispose();
            }
        });
        JButton ciwExportToPDFbutton = new JButton();
        ciwExportToPDFbutton.setText(ResourceBundle.getBundle("translations/program_strings").getString("expimp.exptopdf"));
        ciwExportToPDFbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ExportToPDF.WriteToPDF(allMetadata);
                        progressBar.setVisible(false);
                        outputLabel.setText("");
                    }
                });
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        progressBar.setVisible(true);
                        outputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exppdf"));
                    }
                });

            }
        });
        buttonPanel.add(ciwExportToPDFbutton);
        buttonPanel.add(ciwCloseButton);
        buttonPanel.add(progressBar);
        buttonPanel.add(outputLabel);

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
        //JTable ciwTable = new JTable();
        JTable ciwTable = new JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        ciwTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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

        ciwTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        ciwTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        int counter = 2;
        for (int index: selectedIndices) {
            ciwTable.getColumnModel().getColumn(counter).setPreferredWidth(375);
            counter++;
        }
        // Add thumbnails for files to table
        Object[] ImgFilenameRow = new Object[(selectedIndices.length + 2)];
        ImgFilenameRow[0] = "";
        ImgFilenameRow[1] = "";
        counter = 2;
        for (int index : selectedIndices) {
            File file = files[index];
            icon = ImageFunctions.analyzeImageAndCreateIcon(file);
            //ImgFilenameRow[(index + 2)] = icon;
            ImgFilenameRow[counter] = icon;
            counter++;
        }
        ciwTable.setRowHeight(0,150);
        model.addRow(ImgFilenameRow);

        // Add the metadata to the table
        for (String[] metadata : tableMetadata) {
            model.addRow(metadata);
        }
        ciwTable.setRowHeight(0,150);
        //ciwTable.repaint();

        JScrollPane ciwScrollPanel = new JScrollPane(ciwTable);

        // Add the panels to the big container panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int TableWidth = 150 + 250 + (selectedIndices.length * 375);
        ciwTable.setSize(TableWidth, screenSize.height-100);
        ciwTable.repaint();
        ciwRootPanel.add(ciwScrollPanel, BorderLayout.CENTER);
        ciwRootPanel.add(buttonPanel, BorderLayout.PAGE_END);

        MyVariables.setScreenWidth(screenSize.width);
        MyVariables.setScreenHeight(screenSize.height);
        frame.add(ciwRootPanel);
        if ((screenSize.width - 50) > (TableWidth)) {
            MyVariables.setScreenWidth(TableWidth);
            frame.setSize(TableWidth, screenSize.height-50);
        } else {
            frame.setSize(screenSize.width, screenSize.height-50);
        }


        //frame.pack();
        frame.setVisible(true);
    }

}
