package org.hvdw.jexiftoolgui.view;

import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;


public class ExifToolReferencePanel {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExifToolReferencePanel.class);

    private Favorites Favs = new Favorites();

    static String convertWritable(String writable) {
        // For some stupid reason SQLJDBC always changes "Yes" or "true" to 1, and "false" or "No" to null.
        // So here we have to change it back
        if ("1".equals(writable)) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public static void displayQueryResults (String queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        model.setColumnIdentifiers(new String[]{"Group", "Tagname", "TagType","Writable"});
        DBResultsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        DBResultsTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        DBResultsTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        DBResultsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String[] cells = line.split("\\t", 4);
                model.addRow(new Object[]{cells[0], cells[1], cells[2], convertWritable(cells[3])});
            }
        }

    }

    public static void displayOwnQueryResults (String sql, String queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        // get the fields that are being queried on and immediately remove spaces for our table header and number of columns
        String queryFields = Utils.stringBetween(sql.toLowerCase(), "select", "from").replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \;
        String[] headerFields = queryFields.split(",");

        model.setColumnIdentifiers(headerFields);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t");
                model.addRow(cells);
            }
        }
    }


}
