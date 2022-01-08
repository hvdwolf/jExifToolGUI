package org.hvdw.jexiftoolgui.view;

import ch.qos.logback.classic.Logger;
import org.hvdw.jexiftoolgui.ExportToPDF;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;
import org.hvdw.jexiftoolgui.metadata.ExportMetadata;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

        // Define the frame, the ScrollPanel with the table, the buttonpanel with the close button
        JFrame frame = new JFrame();
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        frame.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        frame.setTitle(ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.comparetitle"));
        frame.setIconImage(Utils.getFrameIcon());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel ciwRootPanel = new JPanel(new BorderLayout());
        //JPanel ciwRootPanel = new JPanel(new GridLayout(2,1));

        ////////////////
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
        ciwExportToPDFbutton.setText(ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.exptopdf"));
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
                        JOptionPane.showMessageDialog(ciwRootPanel, String.format(ProgramTexts.HTML, 400, (ResourceBundle.getBundle("translations/program_strings").getString("exppdf.pdfscreated") + ":<br><br>" + MyVariables.getpdfDocs()), ResourceBundle.getBundle("translations/program_strings").getString("exppdf.pdfscreated"), JOptionPane.INFORMATION_MESSAGE));
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
        JButton ciwExpToCSVbutton = new JButton();
        ciwExpToCSVbutton.setText(ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.exptocsv"));
        ciwExpToCSVbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CsvFromCompareImages CVCI = new CsvFromCompareImages();
                String selection = CVCI.showDialog(ciwRootPanel);
                if ("onecsvperimage".equals(selection)) {
                    ExportMetadata.WriteCSVFromImgComp(allMetadata, ciwRootPanel, selection);
                    // Below option pane als uses the MyVariables.getpdfDocs() variable as we also use that for csvdocs
                    JOptionPane.showMessageDialog(ciwRootPanel, String.format(ProgramTexts.HTML, 400, (ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.csvscreated") + ":<br><br>" + MyVariables.getpdfDocs()), ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.csvscreated"), JOptionPane.INFORMATION_MESSAGE));
                } else if ("onecombinedcsv".equals(selection)){
                    ExportMetadata.WriteCSVFromImgComp(tableMetadata, ciwRootPanel, selection);
                    // Below option pane als uses the MyVariables.getpdfDocs() variable as we also use that for csvdocs
                    JOptionPane.showMessageDialog(ciwRootPanel, String.format(ProgramTexts.HTML, 400, (ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.csvscreated") + ":<br><br>" + MyVariables.getpdfDocs()), ResourceBundle.getBundle("translations/program_strings").getString("cmpimg.csvscreated"), JOptionPane.INFORMATION_MESSAGE));
                }
                //JOptionPane.showMessageDialog(ciwRootPanel, selection, selection, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(ciwExpToCSVbutton);
        buttonPanel.add(ciwExportToPDFbutton);
        buttonPanel.add(ciwCloseButton);
        buttonPanel.add(progressBar);
        buttonPanel.add(outputLabel);
        ////////////////

        //Create the table header for both tables
        List<String> theader = new ArrayList<String>();
        theader.add(ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tablegroup"));
        theader.add(ResourceBundle.getBundle("translations/program_strings").getString("vdtab.tabletag"));
        for (int index : selectedIndices) {
            String filename = files[index].getName();
            theader.add("<html>" + (files[index].getName()) + "</html>");
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
                    super.setValue("<html>" + value + "</html>");
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
