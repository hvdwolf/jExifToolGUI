package org.hvdw.jexiftoolgui.model;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class Lenses {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Lenses.class);

    public static List<File> loadlensnames() {

        File lensFolder = new File(MyVariables.getlensFolder());
        File[] listOfFiles = lensFolder.listFiles();
        List<File> listOfCheckedFileNames = new ArrayList<File>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                //System.out.println(file.getName());
                listOfCheckedFileNames.add(file);
            }
        }
        return listOfCheckedFileNames;

    }
    public static void displaylensnames(List<File> lensnames, JTable lensnametable) {
        DefaultTableModel model = (DefaultTableModel) lensnametable.getModel();
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("sellens.name")});
        lensnametable.getColumnModel().getColumn(0).setPreferredWidth(250);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (lensnames.size() > 0) {
            //String[] lines = lensnames.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            //logger.debug("String[] lines {}", Arrays.toString(lines));

            for (File lensname : lensnames) {
                logger.debug("lensname {}", lensname.getName());
                model.addRow(new Object[]{lensname.getName()});
            }
        }
    }

}
