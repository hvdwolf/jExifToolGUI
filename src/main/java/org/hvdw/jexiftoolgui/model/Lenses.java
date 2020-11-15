package org.hvdw.jexiftoolgui.model;

import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class Lenses {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Lenses.class);

    public static String loadlensnames() {
        String sql = "select lens_name,lens_description from myLenses order by lens_Name";
        String lensnames = SQLiteJDBC.generalQuery(sql, "disk");
        //lblLensNames.setText(String.format(ProgramTexts.HTML, 300, lensnames.replace("\n", "<br>")));
        return lensnames;

    }
    public static void displaylensnames(String lensnames, JTable lensnametable) {
        DefaultTableModel model = (DefaultTableModel) lensnametable.getModel();
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("sellens.name"), ResourceBundle.getBundle("translations/program_strings").getString("sellens.descr")});
        lensnametable.getColumnModel().getColumn(0).setPreferredWidth(150);
        lensnametable.getColumnModel().getColumn(1).setPreferredWidth(300);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (lensnames.length() > 0) {
            String[] lines = lensnames.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            logger.debug("String[] lines {}", Arrays.toString(lines));

            for (String line : lines) {
                logger.debug("line {}", line);
                String[] cells = line.split("\\t");
                logger.debug("number of elements after splitted line {}", cells.length);
                if (cells.length > 1) {
                    model.addRow(new Object[]{cells[0], cells[1]});
                } else {
                    model.addRow(new Object[]{line, ""});
                }
            }
        }
    }

}
