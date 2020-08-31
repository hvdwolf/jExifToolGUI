package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class EditUserDefinedCombis {
    private final static Logger logger = LoggerFactory.getLogger(EditUserDefinedCombis.class);
    JTable usercombiTable;
    MyTableModel model;
    List<String> values = new ArrayList<String>();
    String strcustomconfigfile = "";

    /*
    / This option makes only the 3rd column (0, 1, 2) editable
    / column 1 (no. 0) & 2 (no. 1) are ready-only
     */
    public class MyTableModel extends DefaultTableModel {
        public boolean isCellEditable(int row, int column){
            return column == 2;
        }

    }
    /*
    / This method updates the table in case we have selected another combi from the JCombobox
     */
    public void UpdateTable(JPanel rootpanel, JComboBox combicombobox, JScrollPane userCombiPane) {

        List<String> values = new ArrayList<String>();
        usercombiTable = new JTable(new MyTableModel());

        model = ((MyTableModel) (usercombiTable.getModel()));
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("mduc.columnlabel"),
                ResourceBundle.getBundle("translations/program_strings").getString("mduc.columntag"),
                ResourceBundle.getBundle("translations/program_strings").getString("mduc.columndefault")});
        model.setRowCount(0);
        Object[] row = new Object[1];

        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select screen_label, tag, default_value from custommetadatasetLines where customset_name='" + setName.trim() + "' order by rowcount";
        String queryResult = SQLiteJDBC.generalQuery(sql);
        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t", 4);
                model.addRow(new Object[]{cells[0], cells[1], cells[2]});
                values.add(cells[2]);
            }
            MyVariables.setuserCombiTableValues(values);
        }
        userCombiPane.setViewportView(usercombiTable);

        usercombiTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                logger.debug("source {}; firstRow {}; lastRow {}, column{}", e.getSource(), e.getFirstRow(), e.getLastRow(), e.getColumn());
                logger.debug("tag {}; original value {}; modified tablecell value {}", model.getValueAt(e.getFirstRow(),1), values.get(e.getFirstRow()), model.getValueAt(e.getFirstRow(),e.getColumn()));
            }
        });
    }

    public void UpdateCustomConfigLabel(JComboBox combicombobox, JLabel customconfiglabel) {
        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select custom_config from custommetadataset where customset_name='" + setName.trim() + "'";
        String queryResult = SQLiteJDBC.singleFieldQuery(sql, "custom_config").trim();
        if ("".equals(queryResult) || queryResult.isEmpty() || "null".equals(queryResult)) {
            customconfiglabel.setText("");
            strcustomconfigfile = "";
            customconfiglabel.setVisible(false);
        } else {
            strcustomconfigfile = queryResult.trim();
            customconfiglabel.setText("This set is using custom config file: " + queryResult);
            customconfiglabel.setVisible(true);
        }
    }

    public void ResetFields(JScrollPane userCombiPane) {
        // Not used here: if user wants to reset, he/she simply selects the dropdown again.
    }

    public void SaveTableValues(JCheckBox udcOverwriteOriginalscheckBox, JProgressBar progressBar) {
        // if changed => save
        // else if !empty => save
        File[] files = MyVariables.getSelectedFiles();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        values = MyVariables.getuserCombiTableValues();
        int rowcounter = 0;
        List<String> cmdparams = new ArrayList<String>();

        cmdparams.add(Utils.platformExiftool());
        // -config parameter for custom config file has to be first parameter on command line
        if (!strcustomconfigfile.equals("")) {
            String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
            String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
            cmdparams.add("-config");
            cmdparams.add(strjexiftoolguifolder + File.separator + strcustomconfigfile);
        }
        if (!udcOverwriteOriginalscheckBox.isSelected()) {
            // default not select ->overwrite originals
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());

        for (String value : values) {
            if ( (!model.getValueAt(rowcounter, 2).equals(value)) || (!model.getValueAt(rowcounter, 2).equals("")) ) { //not equal, table value has changed OR table value not empty -> means default value
                logger.info("tag {}; original value {}; modified tablecell value {}", model.getValueAt(rowcounter,1), values.get(rowcounter), model.getValueAt(rowcounter,2));
                if (model.getValueAt(rowcounter,1).toString().startsWith("-")) {
                    cmdparams.add(model.getValueAt(rowcounter,1).toString() + "=" + model.getValueAt(rowcounter, 2).toString().trim());
                } else { //tag without - (minus sign/hyphen) as prefix
                    cmdparams.add("-" + model.getValueAt(rowcounter,1).toString() + "=" + model.getValueAt(rowcounter, 2).toString().trim());
                }
            }
            rowcounter++;
        }

        for (int index: selectedIndices) {
            //logger.info("index: {}  image path: {}", index, files[index].getPath());
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }

        logger.info("Save user combi parameter command {}", cmdparams);
        CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);

    }
}
