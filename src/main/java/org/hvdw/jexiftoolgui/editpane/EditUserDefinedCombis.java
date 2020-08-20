package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class EditUserDefinedCombis {
    private final static Logger logger = LoggerFactory.getLogger(EditUserDefinedCombis.class);
    JTable usercombiTable;

    /*
    / This option makes only the 3rd column (0, 1, 2) editable
    / column 1 & 2 are ready-only
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

        usercombiTable = new JTable(new MyTableModel());

        MyTableModel model = ((MyTableModel) (usercombiTable.getModel()));
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("mct.columnlabel"),
                ResourceBundle.getBundle("translations/program_strings").getString("mct.columntag"),
                ResourceBundle.getBundle("translations/program_strings").getString("mct.columndefault")});
        model.setRowCount(0);
        Object[] row = new Object[1];

        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select screen_label, tag, default_value from custommetadatasetLines where customset_name='" + setName.trim() + "'";
        String queryResult = SQLiteJDBC.generalQuery(sql);
        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t", 4);
                model.addRow(new Object[]{cells[0], cells[1], cells[2]});
            }
        }
        userCombiPane.setViewportView(usercombiTable);
    }

    public void UpdateCustomConfigLabel(JComboBox combicombobox, JLabel customconfiglabel) {
        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select custom_config from custommetadataset where customset_name='" + setName.trim() + "'";
        String queryResult = SQLiteJDBC.singleFieldQuery(sql, "custom_config").trim();
        if ("".equals(queryResult) || queryResult.isEmpty() || "null".equals(queryResult)) {
            customconfiglabel.setVisible(false);
        } else {
            customconfiglabel.setText("This set used custom config file: " + queryResult);
            customconfiglabel.setVisible(true);
        }
    }

    public void ResetFields(JScrollPane userCombiPane) {
        
    }
}
